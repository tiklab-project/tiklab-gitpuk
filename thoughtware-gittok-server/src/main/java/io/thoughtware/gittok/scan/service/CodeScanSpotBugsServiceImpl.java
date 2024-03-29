package io.thoughtware.gittok.scan.service;

import io.thoughtware.gittok.commit.model.Commit;
import io.thoughtware.gittok.commit.model.CommitMessage;
import io.thoughtware.gittok.commit.service.CommitServer;
import io.thoughtware.gittok.common.GitTokYamlDataMaService;
import io.thoughtware.gittok.common.git.GitUntil;
import io.thoughtware.gittok.repository.model.Repository;
import io.thoughtware.gittok.scan.model.*;
import io.thoughtware.core.exception.SystemException;
import io.thoughtware.eam.common.context.LoginContext;
import io.thoughtware.user.user.model.User;
import io.thoughtware.gittok.common.RepositoryUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CodeScanSpotBugsServiceImpl implements CodeScanSpotBugsService {
    private static Logger logger = LoggerFactory.getLogger(CodeScanSpotBugsServiceImpl.class);

    @Autowired
    GitTokYamlDataMaService yamlDataMaService;

    @Autowired
    ScanSchemeRuleService schemeRuleService;

    @Autowired
    ScanRecordService scanRecordService;

    @Autowired
    ScanRecordInstanceService recordInstanceService;

    @Autowired
    CommitServer commitServer;

    //扫描项目的所有类
    public static Map<String , List<String>> projectClassFileMap = new HashMap<>();


    //执行扫描的日志
    public static Map<String , String> scanExecLog = new HashMap<>();

    //执行扫描的状态
    public static Map<String , ScanRecord> scanExecRecord = new HashMap<>();


    //执行扫描执行开始时间
    public static Map<String , Date> scanExecStarTime = new HashMap<>();

    @Override
    public void codeScanBySpotBugs(ScanPlay scanPlay)  {
        ScanRecord scanRecord = new ScanRecord();
        //执行开始时间
        scanExecStarTime.put(scanPlay.getId(),new Date(System.currentTimeMillis()));
        //每次执行先清空当前计划的日志
        scanExecLog.remove(scanPlay.getId());
        scanExecRecord.remove(scanPlay.getId());

        //初始化扫描记录数据
        initScanRecord(scanRecord,scanPlay);

        //拼接日志
        joinScanLog(scanRecord, "执行扫描任务：" + scanPlay.getPlayName(),"run");

        Repository repository = scanPlay.getRepository();

        //spotbugs的地址
        String spotbugsAddress = yamlDataMaService.spotbugsAddress();
        String spotbugsPath = spotbugsAddress + "/spotbugs";

        try {
            //仓库原地址
            String repositoryUrl = RepositoryUtil.SystemTypeAddress(yamlDataMaService.repositoryAddress() + "/" + scanPlay.getRepository().getRpyId() + ".git");
            //git clone后存放的位置
            String cloneUrl = RepositoryUtil.SystemTypeAddress(yamlDataMaService.repositoryAddress() + "/clone/" + scanPlay.getRepository().getRpyId());
            logger.info("SpotBugs扫描->git clone");
            File file = new File(cloneUrl);
            if (file.exists()) {
                FileUtils.deleteDirectory(new File(cloneUrl));
            }
            //GitUntil.cloneRepository(repositoryUrl, scanPlay.getBranch(), cloneUrl);
            GitUntil.cloneRepositoryByFile(repositoryUrl, cloneUrl);
            logger.info("SpotBugs扫描->mvn 开始编译");
            Process  mvnProcess = RepositoryUtil.process(cloneUrl, "mvn clean compile");

            joinScanLog(scanRecord,"开始编译项目："+scanPlay.getRepository().getName(),"run");
            //项目编译日志
            readFile(scanRecord,mvnProcess);

            int mvnWaitFor = mvnProcess.waitFor();
            logger.info("SpotBugs扫描->mvn 编译结果"+mvnWaitFor);
            if (mvnWaitFor==0){
                joinScanLog(scanRecord,"编译成功","run");
                //将扫描项目的所有类文件放入map
                List<String> filePath = RepositoryUtil.getFilePath(new File(cloneUrl), new ArrayList<>());
                List<String> stringList = filePath.stream().filter(a -> a.endsWith(".java")).collect(Collectors.toList());
                projectClassFileMap.put(repository.getRpyId(), stringList);

                //扫描后输出结果文件的位置
                String scanFolderPath = yamlDataMaService.scanFileAddress() + "/" + String.valueOf(System.currentTimeMillis()).substring(0, 9);
                File scanFile = new File(scanFolderPath);
                if (!scanFile.exists()) {
                    scanFile.mkdirs();
                }
                String scanFilePath = scanFolderPath + "/" + repository.getName() + ".xml";

                String execOrder = "sh " + spotbugsPath + " -low -textui -effort:max -xml:withMessages -output " +
                        scanFilePath + " " +cloneUrl;

                logger.info("SpotBugs扫描->spotbugsPath路径"+spotbugsPath);
                logger.info("SpotBugs扫描->扫描结果输出路径"+scanFilePath);
                joinScanLog(scanRecord,"开始执行仓库扫描，扫描时间稍长，请耐心等待 ，执行中...","run");
                Process  process = RepositoryUtil.process(spotbugsAddress, execOrder);

                //读取执行的日志
                readFile(scanRecord,process);

                int waitFor = process.waitFor();
                if (waitFor==0){
                    logger.info("SpotBugs扫描->扫描成功");
                    joinScanLog(scanRecord,"扫描执行完成","run");

                    //扫描成功创建扫描记录
                    createSpotBugRecord(scanRecord);
                    //解析扫描结果xml文件信息
                    findScanBugs(scanPlay,scanRecord,scanFilePath);
                }else {
                    logger.info("SpotBugs扫描-扫描失败");


                    joinScanLog(scanRecord,"扫描失败","fail");
                    //扫描失败创建扫描记录
                    createSpotBugRecord(scanRecord);
                }
                FileUtils.deleteDirectory(new File(scanFilePath.substring(0, scanFilePath.lastIndexOf("/"))));
            }else {
                logger.info("SpotBugs扫描->mvn 编译扫描项目失败");


                joinScanLog(scanRecord,"编译失败","fail");

                //编译失败创建扫描记录
                createSpotBugRecord(scanRecord);
            }
            //执行完成后删除clone 的文件
            FileUtils.deleteDirectory(new File(cloneUrl));
        }catch (Exception e){
            logger.info("SpotBugs扫描->SpotBugs扫描失败"+e.getMessage());

            joinScanLog(scanRecord,"扫描失败"+e.getMessage(),"fail");


            //编译失败创建扫描记录
            createSpotBugRecord(scanRecord);

            throw  new SystemException(600,"SpotBugs扫描失败："+e.getMessage());
        }
    }

    @Override
    public ScanRecord findScanBySpotBugs(String scanPlayId) {
        Date date = scanExecStarTime.get(scanPlayId);
        //计算扫描耗时
        String time = RepositoryUtil.time(date,"scan");
        ScanRecord scanRecord = scanExecRecord.get(scanPlayId);
        if (ObjectUtils.isNotEmpty(scanRecord)){
            scanRecord.setScanTime(time);
            scanRecord.setExecLog(scanExecLog.get(scanPlayId));
        }

        return   scanRecord;
    }




    /**
     * 分析每个bug的实例
     * @param xmlPath xml路径
     * @param  scanPlay 扫描计划
     */
    public void findScanBugs(ScanPlay scanPlay,ScanRecord scanRecord,String xmlPath) throws Exception {
        joinScanLog(scanRecord,"开始解析扫描结果","run");
        try {
            // 读取XML文件
            File xmlFile = new File(xmlPath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            // 可选：根据需要对Document进行额外的配置
            doc.getDocumentElement().normalize();

            // 获取根元素
            Element rootElement = doc.getDocumentElement();

            // 获取BugInstance元素的列表
            NodeList bugInstanceList = rootElement.getElementsByTagName("BugInstance");

            //解析BugPattern节点获取扫描问题的概述和详细
            Map<String, Map<String, String>> analysisBugPattern = analysisBugPattern(rootElement);
            logger.info("SpotBugs扫描->解析xml");
            //扫描方案中的规则
            List<ScanSchemeRule> schemeRuleList = schemeRuleService.findScanSchemeRuleList(new ScanSchemeRuleQuery().setScanSchemeId(scanPlay.getScanScheme().getId()));
            List<ScanSchemeRule> scanSchemeRules = schemeRuleList.stream().filter(a -> a.getIsDisable() == 0&&
                    ("SpotBugs").equals(a.getScanRule().getScanTool())).collect(Collectors.toList());
            logger.info("SpotBugs扫描->查询schemeRuleList"+schemeRuleList);
            logger.info("SpotBugs扫描->查询scanSchemeRules"+scanSchemeRules);
            if (CollectionUtils.isNotEmpty(scanSchemeRules)){
                int severityNum=0;
                int noticeNum=0;
                int suggestNum=0;
                logger.info("SpotBugs扫描->进入解析xml");
                // 遍历BugInstance元素列表
                for (int i = 0; i < bugInstanceList.getLength(); i++) {
                    //扫描记录实例
                    ScanRecordInstance recordInstance = new ScanRecordInstance();
                    Node bugInstanceNode = bugInstanceList.item(i);
                    if (bugInstanceNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element bugInstanceElement = (Element) bugInstanceNode;

                        //bug 类型
                        String bugType = bugInstanceElement.getAttribute("type");
                        Map<String, String> problemMap = analysisBugPattern.get(bugType);
                        List<String> stringList = scanSchemeRules.stream().map(a -> a.getScanRule().getRuleName()).collect(Collectors.toList());
                        logger.info("SpotBugs扫描->扫描方案的规则类型："+stringList);
                        List<ScanSchemeRule> scanSchemeRuleList = scanSchemeRules.stream().filter(a -> bugType.equals(a.getScanRule().getRuleName())).collect(Collectors.toList());
                        logger.info("SpotBugs扫描->扫描的bug类型："+bugType);

                        if (CollectionUtils.isEmpty(scanSchemeRuleList)){
                            continue;
                        }
                        // 获取Bug 级别
                        String priority = bugInstanceElement.getAttribute("priority");

                        //修复描述
                        String bugDescription = bugInstanceElement.getElementsByTagName("LongMessage")
                                .item(0).getTextContent();
                        //修复概述
                        String shortMessage = bugInstanceElement.getElementsByTagName("ShortMessage")
                                .item(0).getTextContent();

                        // 解析SourceLine
                        Map<String, String> sourceLineMap = analysisSourceLine(bugInstanceElement,scanPlay.getRepository().getRpyId());
                        if (ObjectUtils.isEmpty(sourceLineMap)){
                            continue;
                        }
                        if (("false").equals(sourceLineMap.get("state"))){
                            continue;
                        }

                        joinScanLog(scanRecord,"解析文件"+sourceLineMap.get("fileName"),"run");
                        String abbrev = bugInstanceElement.getAttribute("abbrev");

                        String category = bugInstanceElement.getAttribute("category");

                        //创建扫描记录实例
                        String filePath = sourceLineMap.get("filePath");
                        String repositoryId = scanPlay.getRepository().getRpyId();
                        int fileIndex = filePath.indexOf(repositoryId) + repositoryId.length();
                        String substring = filePath.substring(fileIndex);
                        recordInstance.setFilePath(substring);

                        recordInstance.setScanRecordId(scanRecord.getId());  //扫描记录id
                        recordInstance.setScanPlayId(scanPlay.getId());   //扫描计划id
                        recordInstance.setFileName(sourceLineMap.get("fileName"));  //class 名称
                        String problemLine = sourceLineMap.get("problemLine");
                        if (StringUtils.isNotEmpty(problemLine)){
                            recordInstance.setProblemLine(Integer.valueOf(problemLine));     //问题行
                        }
                        recordInstance.setProblemState("unsolved");
                        if (("1").equals(priority)){
                            severityNum+=1;
                            recordInstance.setProblemLevel(1);   //问题等级
                        }
                        if (("2").equals(priority)){
                            noticeNum+=1;
                            recordInstance.setProblemLevel(2);   //问题等级
                        }
                        if (("3").equals(priority)){
                            suggestNum+=1;
                            recordInstance.setProblemLevel(3);   //问题等级
                        }
                        recordInstance.setRepairDesc(bugDescription);  //修复描述
                        recordInstance.setRepairOverview(shortMessage); //修复概述
                        recordInstance.setProblemDesc(problemMap.get("problemDesc"));  //问题描述
                        recordInstance.setProblemOverview(problemMap.get("problemOverview"));  //问题概述
                        recordInstance.setRuleName(bugType);   //规则名称
                        String instance = recordInstanceService.createScanRecordInstance(recordInstance);
                        logger.info("SpotBugs扫描->创建记录实例成功"+instance);
                    }
                }
                logger.info("SpotBugs扫描->扫描记录实例成功");



                int allTrouble = severityNum + noticeNum + suggestNum;
                scanRecord.setSeverityTrouble(severityNum);
                scanRecord.setNoticeTrouble(noticeNum);
                scanRecord.setSuggestTrouble(suggestNum);
                scanRecord.setAllTrouble(allTrouble);
                scanRecord.setScanResult("success");
                joinScanLog(scanRecord,"扫描结果解析成功","success");

                //计算扫描耗时
                String time = RepositoryUtil.time(new Date(scanRecord.getCreateTime().getTime()),"scan");
                scanRecord.setScanTime(time);
                scanRecord.setExecLog(scanExecLog.get(scanRecord.getScanPlayId()));

                scanRecordService.updateScanRecord(scanRecord);
            }
        }catch (Exception e){
            joinScanLog(scanRecord,"扫描结果解析失败","fail");
            logger.info("SpotBugs扫描->扫描失败");
            scanRecord.setScanResult("fail");

            //计算扫描耗时
            String time = RepositoryUtil.time(new Date(scanRecord.getCreateTime().getTime()),"scan");
            scanRecord.setScanTime(time);
            scanRecord.setExecLog(scanExecLog.get(scanRecord.getScanPlayId()));
            scanRecordService.updateScanRecord(scanRecord);
            throw new SystemException("解析Xml文件失败，path:"+xmlPath+" ，message："+e.getMessage());
        }

    }

    /**
     * 分析BugPattern  (代码的问题的概述、详细)
     * @param element xml 的根节点
     */
    public Map<String, Map<String,String>> analysisBugPattern(Element element){
        Map<String, Map<String,String>> bugPatternMap = new HashMap<>();
        NodeList bugInstanceList = element.getElementsByTagName("BugPattern");
        for (int i = 0; i < bugInstanceList.getLength(); i++) {
            Map<String, String> descMap = new HashMap<>();
            Node bugInstanceNode = bugInstanceList.item(i);

            if (bugInstanceNode.getNodeType() == Node.ELEMENT_NODE) {
                Element categoryElement = (Element) bugInstanceNode;

                String category = categoryElement.getAttribute("category");
                //问题类型（编号名称）
                String type = categoryElement.getAttribute("type");
                String abbrev = categoryElement.getAttribute("abbrev");
                //问题概述
                String shortDescription = categoryElement.getElementsByTagName("ShortDescription")
                        .item(0).getTextContent();
                //问题详细
                String details = categoryElement.getElementsByTagName("Details")
                        .item(0).getTextContent();

                descMap.put("problemDesc",details);
                descMap.put("problemOverview",shortDescription);
                bugPatternMap.put(type,descMap);
            }
        }
        return bugPatternMap;
    }


    /**
     * 分析SourceLine  (问题类、问题代码的行数)
     * @param element xml 的根节点
     * @param rpyId 扫描项目的id
     */
    private Map<String, String> analysisSourceLine(Element element,String rpyId){
        Map<String, String> sourceLineMap = new HashMap<>();

        List<String> classFile = projectClassFileMap.get(rpyId);
        NodeList sourceLine = element.getElementsByTagName("SourceLine");

        for (int i = 0; i < sourceLine.getLength(); i++) {
            Node sourceLineNode = sourceLine.item(i);
            if (sourceLineNode.getNodeType() == Node.ELEMENT_NODE) {
                Element lineNode = (Element) sourceLineNode;

                String start = lineNode.getAttribute("start");
                String end = lineNode.getAttribute("end");
                if (ObjectUtils.isEmpty(start)||!(start).equals(end)){
                    continue;
                }
                //问题类路径
                String sourcePath = lineNode.getAttribute("sourcepath");
                logger.info("SpotBugs扫描->扫描的sourcePath："+sourcePath);
                List<String> stringList = classFile.stream().filter(a -> a.endsWith(sourcePath)).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(stringList)){
                    sourceLineMap.put("state","false");
                    continue;
                }
                //问题类
                String sourceFile = lineNode.getAttribute("sourcefile");
                logger.info("SpotBugs扫描->扫描项目sourcePath："+stringList.get(0));
               // String message =  lineNode.getElementsByTagName("Message").item(0).getTextContent();
                sourceLineMap.put("state","true");
                sourceLineMap.put("filePath",stringList.get(0));
                sourceLineMap.put("fileName",sourcePath);
                sourceLineMap.put("problemLine",start);

            }
        }
        return sourceLineMap;
    }


    /**
     *  执行日志
     *  @param process:process
     * @param  scanRecord 扫描计划的id
     */
    public void  readFile(ScanRecord scanRecord,Process process) throws IOException {

        // 获取命令行输出
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        // mvn 编译日志
        while ((line = reader.readLine()) != null) {
            logger.info("执行命令日志:"+line);
            joinScanLog(scanRecord,line,"run");
        }

        //spotBugs日志
        InputStream errorStream = process.getErrorStream();
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
        String errorLine;
        while ((errorLine = errorReader.readLine()) != null) {
            logger.info("执行命令日志02:"+errorLine);
            joinScanLog(scanRecord,errorLine,"run");
        }

    }



    /**
     * 解析代码扫描统计信息
     * @param scanPlay
     * @param  xmlPath 扫描结果路径
     * @return 解析结果
     */
    public void findBugFileBySpotBUgs( ScanPlay scanPlay,String xmlPath ){

        ScanRecord scanRecord = new ScanRecord();

        // String xmlPath="/Users/limingliang/source/spotbugs-gittok.xml";

        try {

            File xmlFile = new File(xmlPath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            // 可选：根据需要对Document进行额外的配置
            doc.getDocumentElement().normalize();

            // 获取根元素
            Element rootElement = doc.getDocumentElement();

            NodeList findBugsSummaryList = rootElement.getElementsByTagName("FindBugsSummary");
            for (int i = 0; i < findBugsSummaryList.getLength(); i++) {
                Node findBugsSummaryNode = findBugsSummaryList.item(i);
                if (findBugsSummaryNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element findBugsSummaryElement = (Element) findBugsSummaryNode;
                    String totalClasses = findBugsSummaryElement.getAttribute("total_classes"); //总共扫描的类的数量。
                    String referencedClasses = findBugsSummaryElement.getAttribute("referenced_classes");//引用的类的数量。
                    String totalBugs = findBugsSummaryElement.getAttribute("total_bugs");//总共检测到的问题（Bugs）的数量。
                    String numPackages = findBugsSummaryElement.getAttribute("num_packages");//项目中的包（package）数量。
                    String vmVersion = findBugsSummaryElement.getAttribute("vm_version");//vm_version Java 版本信息。
                    String priority1 = findBugsSummaryElement.getAttribute("priority_1");//优先级为 1 的问题数量。
                    String priority2 = findBugsSummaryElement.getAttribute("priority_2");//优先级为 2 的问题数量。
                    String priority3 = findBugsSummaryElement.getAttribute("priority_3");//优先级为 3 的问题数量。


                    //创建扫描记录
                    scanRecord.setScanPlayId(scanPlay.getId());
                    scanRecord.setRepositoryId(scanPlay.getRepository().getRpyId());
                    User user = new User();
                    user.setId(LoginContext.getLoginId());
                    scanRecord.setScanUser(user);

                    int allTrouble = StringUtils.isNotEmpty(totalBugs) ? Integer.valueOf(totalBugs) : 0;
                    scanRecord.setAllTrouble(allTrouble);

                    int priority01 = StringUtils.isNotEmpty(priority1) ? Integer.valueOf(priority1) : 0;
                    scanRecord.setSeverityTrouble(Integer.valueOf(priority01));

                    int priority02 = StringUtils.isNotEmpty(priority2) ? Integer.valueOf(priority2) : 0;
                    scanRecord.setNoticeTrouble(Integer.valueOf(priority02));

                    int priority03 = StringUtils.isNotEmpty(priority3) ? Integer.valueOf(priority3) : 0;
                    scanRecord.setSuggestTrouble(Integer.valueOf(priority03));
                    String scanRecordId = scanRecordService.createScanRecord(scanRecord);

                    scanRecord.setId(scanRecordId);
                }
            }
        }catch (Exception e){
            throw new SystemException("解析Xml文件失败，path:"+xmlPath+" ，message："+e.getMessage());
        }
    }

    /**
     *  初始化扫描结果数据
     *  @param scanPlay 扫描计划
     * @param scanRecord scanRecord
     */
    public void initScanRecord(ScanRecord scanRecord,ScanPlay scanPlay ) {

        scanRecord.setRepositoryId(scanPlay.getRepository().getRpyId());
        scanRecord.setScanPlayId(scanPlay.getId());
        User user = new User();
        user.setId(LoginContext.getLoginId());

        scanRecord.setScanUser(user);
        scanRecord.setCreateTime(new Timestamp(System.currentTimeMillis()));
        scanRecord.setScanResult("run");
        scanRecord.setScanWay("hand");
        //扫描对象
        Commit commit = new Commit();
        commit.setRpyId(scanPlay.getRepository().getRpyId());
        commit.setBranch(scanPlay.getBranch());
        CommitMessage branchCommit = commitServer.findLatelyBranchCommit(commit);
        scanRecord.setScanObject(branchCommit.getCommitId());

        scanExecRecord.put(scanPlay.getId(),scanRecord);
    }

    /**
     *  拼接扫描日志
     * @param  scanRecord 扫描记录
     * @param  execState  执行状态
     *  @param log 日志
     */
    public void joinScanLog(ScanRecord scanRecord,String log,String execState){
        String scanPlayId = scanRecord.getScanPlayId();
        LocalDateTime now = LocalDateTime.now();
        // 自定义时间格式
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String customDateTime = now.format(customFormatter);
        //拼接的日志
        String resultLog = "["+customDateTime + "] " + log;
        String logs = scanExecLog.get(scanPlayId);
        if (StringUtils.isEmpty(logs)){
            scanExecLog.put(scanPlayId,resultLog);
        }else {
            scanExecLog.put(scanPlayId,logs+"\n"+resultLog);
        }
        initRecordLog(scanRecord,execState);
    }

    /**
     *  初始化扫描结果的日志
     * @param  scanRecord 扫描记录
     * @param  execState  执行状态
     */
    public void initRecordLog(ScanRecord scanRecord,String execState){
        scanRecord.setScanResult(execState);
        scanExecRecord.put(scanRecord.getScanPlayId(),scanRecord);

    }



    /**
     *  创建扫描结果
     * @param  scanRecord 扫描记录
     */
    public void createSpotBugRecord(ScanRecord scanRecord){
        //计算扫描耗时
        String time = RepositoryUtil.time(new Date(scanRecord.getCreateTime().getTime()),"scan");
        scanRecord.setScanTime(time);
        scanRecord.setExecLog( scanExecLog.get(scanRecord.getScanPlayId()));

        String scanRecordId = scanRecordService.createScanRecord(scanRecord);
        scanRecord.setId(scanRecordId);
    }



    @Override
    public String findLog(String playId) {
            try {
                // 读取XML文件
                File xmlFile = new File("/Users/limingliang/tiklab/thoughtware-gittok/scan/170296861/thoughtware-gittok.xml");
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(xmlFile);

                // 可选：根据需要对Document进行额外的配置
                doc.getDocumentElement().normalize();

                // 获取根元素
                Element rootElement = doc.getDocumentElement();

                // 获取BugInstance元素的列表
                NodeList bugInstanceList = rootElement.getElementsByTagName("BugInstance");

                //解析BugPattern节点获取扫描问题的概述和详细
                Map<String, Map<String, String>> analysisBugPattern = analysisBugPattern(rootElement);
                logger.info("SpotBugs扫描->解析xml");
                //扫描方案中的规则
                List<ScanSchemeRule> schemeRuleList = schemeRuleService.findScanSchemeRuleList(new ScanSchemeRuleQuery().setScanSchemeId("6edce30db979"));
                List<ScanSchemeRule> scanSchemeRules = schemeRuleList.stream().filter(a -> a.getIsDisable() == 0&&
                        ("SpotBugs").equals(a.getScanRule().getScanTool())).collect(Collectors.toList());
                logger.info("SpotBugs扫描->查询schemeRuleList"+schemeRuleList);
                logger.info("SpotBugs扫描->查询scanSchemeRules"+scanSchemeRules);
                if (CollectionUtils.isNotEmpty(scanSchemeRules)) {
                    int severityNum = 0;
                    int noticeNum = 0;
                    int suggestNum = 0;
                    logger.info("SpotBugs扫描->进入解析xml");
                    // 遍历BugInstance元素列表
                    for (int i = 0; i < bugInstanceList.getLength(); i++) {
                        //扫描记录实例
                        ScanRecordInstance recordInstance = new ScanRecordInstance();
                        Node bugInstanceNode = bugInstanceList.item(i);
                        if (bugInstanceNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element bugInstanceElement = (Element) bugInstanceNode;


                            // 解析SourceLine
                            Map<String, String> sourceLineMap = analysisSourceLine(bugInstanceElement, "397a18472f7a");
                        }
                    }
                }
            }catch (Exception e){
                System.out.println("错误信息："+e.getMessage());
            }

        return null;
    }
}
