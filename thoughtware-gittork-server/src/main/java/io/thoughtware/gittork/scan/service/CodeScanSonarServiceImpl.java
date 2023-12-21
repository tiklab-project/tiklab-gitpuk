package io.thoughtware.gittork.scan.service;

import com.alibaba.fastjson.JSONObject;
import io.thoughtware.gittork.commit.model.Commit;
import io.thoughtware.gittork.commit.model.CommitMessage;
import io.thoughtware.gittork.commit.service.CommitServer;
import io.thoughtware.gittork.common.GitTorkYamlDataMaService;
import io.thoughtware.gittork.scan.model.*;
import io.thoughtware.core.exception.ApplicationException;
import io.thoughtware.core.exception.SystemException;
import io.thoughtware.core.page.Pagination;
import io.thoughtware.eam.common.context.LoginContext;
import io.thoughtware.user.user.model.User;
import io.thoughtware.gittork.common.RepositoryUtil;
import io.thoughtware.gittork.common.git.GitUntil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class CodeScanSonarServiceImpl implements CodeScanSonarService {
    private static Logger logger = LoggerFactory.getLogger(CodeScanSonarServiceImpl.class);

    @Autowired
    ScanPlayService scanPlayService;

    @Autowired
    DeployServerService deployServerService;

    @Autowired
    GitTorkYamlDataMaService yamlDataMaService;

    @Autowired
    ScanRecordService scanRecordService;

    @Autowired
    CommitServer commitServer;

    @Autowired
    ScanSchemeSonarService schemeSonarService;

    public static Map<String , String> codeScanResult = new HashMap<>();

    //执行扫描的日志
    public static Map<String , String> scanExecLog = new HashMap<>();

    //执行扫描的状态
    public static Map<String , ScanRecord> scanExecRecord = new HashMap<>();


    //执行扫描执行开始时间
    public static Map<String , java.sql.Date> scanExecStarTime = new HashMap<>();

    @Override
    public void codeScanBySonar(ScanPlay scanPlay) {
        ScanRecord scanRecord = new ScanRecord();
        String scanPlayId = scanPlay.getId();
        logger.info("sonar扫描->开始执行");
        codeScanResult.put(scanPlayId,"start");
        //每次执行先清空当前计划的日志
        scanExecLog.remove(scanPlay.getId());
        scanExecRecord.remove(scanPlayId);
        //执行开始时间
        scanExecStarTime.put(scanPlay.getId(),new java.sql.Date(System.currentTimeMillis()));
        //初始化扫描结果数据
        initScanRecord(scanRecord,scanPlay);

        //拼接日志
        joinScanLog(scanRecord, "执行扫描任务：" + scanPlay.getPlayName());


        try {
            List<ScanSchemeSonar> schemeSonarList = schemeSonarService.findScanSchemeSonarList(new ScanSchemeSonarQuery().setScanSchemeId(scanPlay.getScanScheme().getId()));
            ScanSchemeSonar scanSchemeSonar = schemeSonarList.get(0);
            DeployEnv deployEnv = scanSchemeSonar.getDeployEnv();
            DeployServer deployServer = scanSchemeSonar.getDeployServer();
            if (ObjectUtils.isEmpty(deployServer)){
                joinScanLog(scanRecord, "没有配置sonar环境");
                scanRecord(scanRecord,scanPlay.getRepository().getName(),"fail");
                return;
            }

            joinScanLog(scanRecord, "获取sonar环境");

            String execOrder =  "mvn clean verify sonar:sonar ";

            execOrder = execOrder +
                    " -Dsonar.projectKey="+ scanPlay.getRepository().getName()+
                    " -Dsonar.host.url="+ deployServer.getServerAddress();

            //账号认证、密钥认证
            if ( "account".equals(deployServer.getAuthType())){
                execOrder = execOrder +
                        " -Dsonar.login="+deployServer.getUserName()+
                        " -Dsonar.password="+deployServer.getPassWord();
            }else {
                execOrder = execOrder +
                        " -Dsonar.login="+deployServer.getPrivateKey();
            }
            //git clone后存放的位置
            String cloneRepositoryUrl = RepositoryUtil.SystemTypeAddress(yamlDataMaService.repositoryAddress()+"/clone/" + scanPlay.getRepository().getRpyId());
            String order = " ./" + execOrder + " " + "-f" +" " +cloneRepositoryUrl ;
            if (RepositoryUtil.findSystemType() == 1){
                order = " .\\" + execOrder + " " + "-f"+" "  +cloneRepositoryUrl;
            }

            //仓库原地址
            String repositoryUrl = RepositoryUtil.SystemTypeAddress(yamlDataMaService.repositoryAddress() +"/"+ scanPlay.getRepository().getRpyId() + ".git");
            logger.info("sonar扫描->git clone");
            File file = new File(cloneRepositoryUrl);
            if (file.exists()){
                FileUtils.deleteDirectory(new File(cloneRepositoryUrl));
            }
            joinScanLog(scanRecord, "开始执行sonar扫描");
            GitUntil.cloneRepository(repositoryUrl, scanPlay.getBranch(), cloneRepositoryUrl);
            logger.info("sonar扫描->推送sonar");
            Process  process = RepositoryUtil.process(deployEnv.getEnvAddress(), order);
            //读取执行日志
            readFile(scanRecord,process);
            int waitFor = process.waitFor();
            if (waitFor==0){
                logger.info("sonar扫描->扫描成功");
                joinScanLog(scanRecord, "扫描成功");
                scanRecord(scanRecord,scanPlay.getRepository().getName(),"success");
                codeScanResult.put(scanPlayId,"success");
            }else {
                logger.info("sonar扫描->扫描失败");
                joinScanLog(scanRecord, "扫描失败");
                scanRecord(scanRecord,scanPlay.getRepository().getName(),"fail");
                codeScanResult.put(scanPlayId,"fail");
            }
            //执行完成后删除clone 的文件
            FileUtils.deleteDirectory(new File(cloneRepositoryUrl));

        }catch (Exception e){
            logger.info("使用sonar扫描错误日志:"+e.getMessage());

            joinScanLog(scanRecord, "使用sonar扫描错误"+e.getMessage());
            scanRecord(scanRecord,scanPlay.getRepository().getName(),"fail");
            codeScanResult.put(scanPlayId,"fail");
        }
    }

    @Override
    public ScanRecord findScanBySonar(String scanPlayId) {
        java.sql.Date date = scanExecStarTime.get(scanPlayId);
        //计算扫描耗时
        String time = RepositoryUtil.time(date,"scan");
        ScanRecord scanRecord = scanExecRecord.get(scanPlayId);
        if (org.apache.commons.lang3.ObjectUtils.isNotEmpty(scanRecord)){
            scanRecord.setScanTime(time);
            scanRecord.setExecLog(scanExecLog.get(scanPlayId));
        }

        return   scanRecord;
    }


    /**
     *  创建扫描记录
     *  @param scanRecord
     * @param  state 执行结果状态
     */
    public void scanRecord(ScanRecord scanRecord,String repositoryName,String state) {
        scanRecord.setScanResult(state);
        scanExecRecord.put(scanRecord.getScanPlayId(),scanRecord);
        try {
            if (("fail").equals(state)){
                scanRecord.setRepositoryId(scanRecord.getRepositoryId());
                scanRecord.setScanPlayId(scanRecord.getScanPlayId());

                User user = new User();
                user.setId(LoginContext.getLoginId());
                scanRecord.setScanUser(user);
                scanRecord.setScanWay("hand");
            }else {
                List<DeployServer> deployServerList = deployServerService.findDeployServerList(new DeployServerQuery().setServerName("sonar"));
                DeployServer deployServer = deployServerList.get(0);
                String findRepositoryUrl=deployServer.getServerAddress()+"/api/projects/search?projects="+repositoryName;
                JSONObject findResultBody = restTemplate(findRepositoryUrl, deployServer);
                Object components = findResultBody.get("components");
                List<Object> resultList = new ArrayList<>();
                for (Object o : (List<Object>) components){
                    resultList.add(o);
                }
                LinkedHashMap hashMap = (LinkedHashMap) resultList.get(0);
                String key = hashMap.get("key").toString();

                //查询错误统计
                String statisticsUrl=deployServer.getServerAddress()+"/api/issues/search?componentKeys="+key+"&s=FILE_LINE&resolved=false&&facets=severities&&additionalFields=_all";
                JSONObject statisticsBody = restTemplate(statisticsUrl, deployServer);
                Object facets = statisticsBody.get("facets");
                List<HashMap> arrayList = (ArrayList) facets;
                if (CollectionUtils.isNotEmpty(arrayList)){
                    HashMap server = arrayList.get(0);
                    ArrayList severities = (ArrayList) server.get("values");
                    Integer allTrouble=0;
                    Integer suggestNum=0;
                    for (Object obj:severities){
                        HashMap map = (HashMap) obj;
                        if (("BLOCKER").equals(map.get("val"))){
                            allTrouble+=Integer.valueOf(map.get("count").toString());
                            scanRecord.setSeverityTrouble(Integer.valueOf(map.get("count").toString()));
                        }
                        if (("CRITICAL").equals(map.get("val"))){
                            allTrouble+=Integer.valueOf(map.get("count").toString());
                            scanRecord.setErrorTrouble(Integer.valueOf(map.get("count").toString()));
                        }
                        if (("MAJOR").equals(map.get("val"))){
                            allTrouble+=Integer.valueOf(map.get("count").toString());
                            scanRecord.setNoticeTrouble(Integer.valueOf(map.get("count").toString()));
                        }
                        if (("MINOR").equals(map.get("val"))){
                            allTrouble+=Integer.valueOf(map.get("count").toString());
                            suggestNum+=Integer.valueOf(map.get("count").toString());
                        }
                        if (("INFO").equals(map.get("val"))){
                            allTrouble+=Integer.valueOf(map.get("count").toString());
                            suggestNum+=Integer.valueOf(map.get("count").toString());
                        }
                    }
                    scanRecord.setSuggestTrouble(suggestNum);
                    scanRecord.setAllTrouble(allTrouble);
                }
                //查询项目扫描状态
                String stateUrl=deployServer.getServerAddress()+"/api/qualitygates/project_status?projectKey="+repositoryName;
                JSONObject statResultBody = restTemplate(stateUrl, deployServer);
                LinkedHashMap projectStatus = (LinkedHashMap) statResultBody.get("projectStatus");
                String status = projectStatus.get("status").toString();
                String result = ("OK").equals(status) ? "success" : "fail";
                scanRecord.setScanResult(result);

               /* //查询项目扫描结果
                String codeScanUrl=deployServer.getServerAddress()+"/api/measures/component?component="+key+"&metricKeys=bugs,coverage,vulnerabilities,code_smells";
                JSONObject scanResultBody = restTemplate(codeScanUrl, deployServer);
                LinkedHashMap component =(LinkedHashMap) scanResultBody.get("component");
                Object measures = component.get("measures");
                for (Object result1 : (List<Object>) measures) {
                    LinkedHashMap jsonObject = (LinkedHashMap) result1;
                    if ("bugs".equals(jsonObject.get("metric"))) {
                        scanRecord.setSeverityTrouble(Integer.valueOf(jsonObject.get("value").toString()));
                    }
                    if ("vulnerabilities".equals(jsonObject.get("metric"))) {
                        scanRecord.setWarnTrouble(Integer.valueOf(jsonObject.get("value").toString()));
                    }
                    if ("code_smells".equals(jsonObject.get("metric"))) {
                        scanRecord.setSuggestTrouble(Integer.valueOf(jsonObject.get("value").toString()));
                    }
                }*/
                scanRecord.setRepositoryId(scanRecord.getRepositoryId());
                scanRecord.setScanPlayId(scanRecord.getScanPlayId());
                User user = new User();
                user.setId(LoginContext.getLoginId());
                scanRecord.setScanUser(user);
                scanRecord.setScanWay("hand");
            }
            scanRecordService.createScanRecord(scanRecord);
        }catch (Exception e){
            logger.info("创建sonar扫描记录失败:"+e.getMessage());
            throw  new SystemException("创建sonar扫描记录失败："+e.getMessage());
        }
    }


    @Override
    public Pagination<ScanRecordInstance> findScanIssuesBySonar(ScanRecordInstanceQuery scanRecordInstanceQuery) {
        Pagination<ScanRecordInstance> objectPagination = new Pagination<>();
        List<ScanRecordInstance> resultList = new ArrayList<>();

        ScanPlay scanPlay = scanPlayService.findOne(scanRecordInstanceQuery.getScanPlayId());
        try {
            List<DeployServer> deployServerList = deployServerService.findDeployServerList(new DeployServerQuery().setServerName("sonar"));
            DeployServer deployServer = deployServerList.get(0);
            String findRepositoryUrl=deployServer.getServerAddress()+"/api/projects/search?projects="+scanPlay.getRepository().getName();
            JSONObject findResultBody = restTemplate(findRepositoryUrl, deployServer);
            Object components = findResultBody.get("components");
            List<Object> list = new ArrayList<>();
            for (Object o : (List<Object>) components){
                list.add(o);
            }
            LinkedHashMap hashMap = (LinkedHashMap) list.get(0);
            String key = hashMap.get("key").toString();

            Integer pageSize = scanRecordInstanceQuery.getPageParam().getPageSize();
            Integer currentPage = scanRecordInstanceQuery.getPageParam().getCurrentPage();

            String findReList=deployServer.getServerAddress()+ "/api/issues/search?componentKeys="+key+"&severities=INFO,MINOR,MAJOR,CRITICAL,BLOCKER&p="+currentPage+"&ps="+pageSize;
            JSONObject statResultBody = restTemplate(findReList, deployServer);
            //问题总条数
            Integer allTotal =Integer.valueOf(statResultBody.get("total").toString()) ;

            List<Objects> objectsList = (List<Objects>) statResultBody.get("issues");
            for (Object object:objectsList){
                ScanRecordInstance recordInstance = new ScanRecordInstance();
                HashMap value = (HashMap) object;

                recordInstance.setId(value.get("key").toString());
                recordInstance.setRuleName(value.get("rule").toString());
                String filName = value.get("component").toString();

                recordInstance.setFileName(filName);
                String pattern = "yyyy-MM-dd";
                SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                Date creationDate = dateFormat.parse(value.get("creationDate").toString());
                recordInstance.setImportTime(new Timestamp(creationDate.getTime()));
                recordInstance.setProblemLine(Integer.valueOf(value.get("line").toString()));
                recordInstance.setProblemOverview(value.get("message").toString());
                String severity = value.get("severity").toString();
                if (("BLOCKER").equals(severity)){
                    recordInstance.setProblemLevel(1);
                }
                if (("CRITICAL").equals(severity)){
                    recordInstance.setProblemLevel(2);
                }
                if (("MAJOR").equals(severity)){
                    recordInstance.setProblemLevel(3);
                }
                if (("MINOR").equals(severity)){
                    recordInstance.setProblemLevel(3);
                }
                if (("INFO").equals(severity)){
                    recordInstance.setProblemLevel(3);
                }
                resultList.add(recordInstance);
            }
            objectPagination.setTotalRecord(allTotal);
            Integer size = scanRecordInstanceQuery.getPageParam().getPageSize();
            int ceil =(int) Math.ceil((double) allTotal / size);
            objectPagination.setTotalPage(ceil);
            objectPagination.setCurrentPage(scanRecordInstanceQuery.getPageParam().getCurrentPage());
        }catch (Exception e){
            throw new SystemException(e.getMessage());
        }
        objectPagination.setDataList(resultList);
        return objectPagination;
    }

    @Override
    public List<ScanIssuesDetails> findScanIssuesDeBySonar(String issueKey, String component) {
        List<ScanIssuesDetails> arrayList = new ArrayList<>();

        List<DeployServer> deployServerList = deployServerService.findDeployServerList(new DeployServerQuery().setServerName("sonar"));
        DeployServer deployServer = deployServerList.get(0);
        try {
            String findRepositoryUrl=deployServer.getServerAddress()+"/api/sources/issue_snippets?issueKey="+issueKey;
            JSONObject resultBody = restTemplate(findRepositoryUrl, deployServer);

            Object data = resultBody.get(component);
            if (ObjectUtils.isEmpty(data)){
                return null;
            }
            HashMap hashMap = (HashMap) data;
            List<Objects> sources = (List<Objects>) hashMap.get("sources");
            for (Object object:sources){
                ScanIssuesDetails details = new ScanIssuesDetails();
                HashMap value = (HashMap) object;

                details.setLine(Integer.valueOf(value.get("line").toString()));
                details.setCode(value.get("code").toString());
                details.setIsNew(value.get("isNew").toString());
                arrayList.add(details);
            }

        }catch (Exception e){
            throw new SystemException(e.getMessage());
        }
        return arrayList;
    }


    /**
     *  restTemplate 调用
     *  @param url:地址
     * @param deployServer: 服务配置
     * @return
     */
    public JSONObject restTemplate(String url, DeployServer deployServer) throws UnsupportedEncodingException {

        //活动调用接口的Authorization  认证
        String binary = DatatypeConverter.printBase64Binary((deployServer.getUserName() + ":" + deployServer.getPassWord()).getBytes("UTF-8"));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Basic " + binary);
        // 请求
        HttpEntity<String> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> result = restTemplate.exchange(url, HttpMethod.GET, request, JSONObject.class);
        JSONObject resultBody = result.getBody();
        return resultBody;
    }

    /**
     *  拼接扫描日志
     * @param  scanRecord 扫描记录
     *  @param log 日志
     */
    public void joinScanLog(ScanRecord scanRecord,String log ){
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
    }



    /**
     *  restTemplate 调用
     *  @param process:process
     */
    public void  readFile(ScanRecord scanRecord,Process process){
      //  CodeScanInstance instance = codeScanLog.get(repositoryId);
        try {
            // 获取命令行输出
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            // 读取命令行输出
            StringBuilder excOutput = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                logger.info("执行命令日志:"+line);
                joinScanLog(scanRecord, line);
                excOutput.append(line);
            }
        }catch (Exception e){
            throw new ApplicationException(e.getMessage());
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
}
