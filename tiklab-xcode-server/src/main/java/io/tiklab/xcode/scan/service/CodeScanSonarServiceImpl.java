package io.tiklab.xcode.scan.service;

import com.alibaba.fastjson.JSONObject;
import io.tiklab.core.exception.ApplicationException;
import io.tiklab.core.exception.SystemException;
import io.tiklab.core.page.Pagination;
import io.tiklab.eam.common.context.LoginContext;
import io.tiklab.user.user.model.User;
import io.tiklab.xcode.commit.model.Commit;
import io.tiklab.xcode.commit.model.CommitMessage;
import io.tiklab.xcode.commit.service.CommitServer;
import io.tiklab.xcode.common.RepositoryUtil;
import io.tiklab.xcode.common.XcodeYamlDataMaService;
import io.tiklab.xcode.common.git.GitUntil;
import io.tiklab.xcode.scan.model.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class CodeScanSonarServiceImpl implements CodeScanSonarService {
    private static Logger logger = LoggerFactory.getLogger(CodeScanSonarServiceImpl.class);

    @Autowired
    ScanPlayService scanPlayService;

    @Autowired
    DeployServerService deployServerService;

    @Autowired
    XcodeYamlDataMaService yamlDataMaService;

    @Autowired
    ScanRecordService scanRecordService;

    @Autowired
    CommitServer commitServer;

    @Autowired
    ScanSchemeSonarService schemeSonarService;

    public static Map<String , String> codeScanResult = new HashMap<>();

    public static Map<String , CodeScanInstance> codeScanLog = new HashMap<>();

    @Override
    public void codeScanBySonar(String scanPlayId) {
        logger.info("sonar扫描->开始执行");
        codeScanResult.put(scanPlayId,"start");
        ScanPlay scanPlay = scanPlayService.findScanPlay(scanPlayId);
        try {
            List<ScanSchemeSonar> schemeSonarList = schemeSonarService.findScanSchemeSonarList(new ScanSchemeSonarQuery().setScanSchemeId(scanPlay.getScanScheme().getId()));
            ScanSchemeSonar scanSchemeSonar = schemeSonarList.get(0);
            DeployEnv deployEnv = scanSchemeSonar.getDeployEnv();
            DeployServer deployServer = scanSchemeSonar.getDeployServer();
            if (ObjectUtils.isEmpty(deployServer)){
                throw  new SystemException(600,"sonar没有配置");
            }

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
            GitUntil.cloneRepository(repositoryUrl, scanPlay.getBranch(), cloneRepositoryUrl);
            logger.info("sonar扫描->推送sonar");
            Process  process = RepositoryUtil.process(deployEnv.getEnvAddress(), order);
            //读取执行日志
            readFile(process);
            int waitFor = process.waitFor();
            if (waitFor==0){
                scanRecord(scanPlay,"success");
                codeScanResult.put(scanPlayId,"success");
            }else {
                scanRecord(scanPlay,"fail");
                codeScanResult.put(scanPlayId,"fail");
            }
            //执行完成后删除clone 的文件
            FileUtils.deleteDirectory(new File(cloneRepositoryUrl));

        }catch (Exception e){
            logger.info("使用sonar扫描错误日志:"+e.getMessage());
            scanRecord(scanPlay,"fail");
            codeScanResult.put(scanPlayId,"fail");
        }
    }

    @Override
    public String findScanBySonar(String scanPlayId) {
        String result = codeScanResult.get(scanPlayId);

        return result;
    }


    /**
     *  创建扫描记录
     *  @param scanPlay:扫描计划
     * @param  state 执行结果状态
     */
    public void scanRecord(ScanPlay scanPlay,String state) {
        ScanRecord scanRecord = new ScanRecord();

        Commit commit = new Commit();
        commit.setRpyId(scanPlay.getRepository().getRpyId());
        commit.setBranch(scanPlay.getBranch());
        CommitMessage branchCommit = commitServer.findLatelyBranchCommit(commit);
        scanRecord.setScanObject(branchCommit.getCommitId());
        try {
            if (("fail").equals(state)){
                scanRecord.setRepositoryId(scanPlay.getRepository().getRpyId());
                scanRecord.setScanPlayId(scanPlay.getId());

                User user = new User();
                user.setId(LoginContext.getLoginId());
                scanRecord.setScanUser(user);
                scanRecord.setScanWay("hand");
                scanRecord.setScanResult("fail");
            }else {
                List<DeployServer> deployServerList = deployServerService.findDeployServerList(new DeployServerQuery().setServerName("sonar"));
                DeployServer deployServer = deployServerList.get(0);
                String findRepositoryUrl=deployServer.getServerAddress()+"/api/projects/search?projects="+scanPlay.getRepository().getName();
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
                String stateUrl=deployServer.getServerAddress()+"/api/qualitygates/project_status?projectKey="+scanPlay.getRepository().getName();
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
                scanRecord.setRepositoryId(scanPlay.getRepository().getRpyId());
                scanRecord.setScanPlayId(scanPlay.getId());
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


    /**
     *  创建扫描问题列表
     *  @param scanIssuesQuery:扫描计划
     */
    public Pagination<ScanIssues> findScanIssuesBySonar(ScanIssuesQuery scanIssuesQuery){
        Pagination<ScanIssues> objectPagination = new Pagination<>();
        List<ScanIssues> resultList = new ArrayList<>();

        ScanPlay scanPlay = scanPlayService.findScanPlay(scanIssuesQuery.getScanPlayId());
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

            Integer pageSize = scanIssuesQuery.getPageParam().getPageSize();
            Integer currentPage = scanIssuesQuery.getPageParam().getCurrentPage();

            String findReList=deployServer.getServerAddress()+ "/api/issues/search?componentKeys="+key+"&severities=INFO,MINOR,MAJOR,CRITICAL,BLOCKER&p="+currentPage+"&ps="+pageSize;
            JSONObject statResultBody = restTemplate(findReList, deployServer);
            //问题总条数
            Integer allTotal =Integer.valueOf(statResultBody.get("total").toString()) ;

            List<Objects> objectsList = (List<Objects>) statResultBody.get("issues");
            for (Object object:objectsList){
                ScanIssues scanIssues = new ScanIssues();
                HashMap value = (HashMap) object;
                scanIssues.setIssuesMessage(value.get("message").toString());
                scanIssues.setIssuesSeverity(value.get("severity").toString());
                scanIssues.setFileName(value.get("component").toString());
                scanIssues.setRuleName(value.get("rule").toString());
                scanIssues.setLeadInTime(value.get("creationDate").toString());
                scanIssues.setIssuesLine(Integer.valueOf(value.get("line").toString()));
                scanIssues.setScanIssuesKey(value.get("key").toString());
                resultList.add(scanIssues);
            }
            objectPagination.setTotalRecord(allTotal);
            Integer size = scanIssuesQuery.getPageParam().getPageSize();
            int ceil =(int) Math.ceil((double) allTotal / size);
            objectPagination.setTotalPage(ceil);
            objectPagination.setCurrentPage(scanIssuesQuery.getPageParam().getCurrentPage());
        }catch (Exception e){
            throw new SystemException(e.getMessage());
        }
        objectPagination.setDataList(resultList);
        return objectPagination;

    }

    @Override
    public Pagination<ScanRecordInstance> findScanIssuesBySonar(ScanRecordInstanceQuery scanRecordInstanceQuery) {
        Pagination<ScanRecordInstance> objectPagination = new Pagination<>();
        List<ScanRecordInstance> resultList = new ArrayList<>();

        ScanPlay scanPlay = scanPlayService.findScanPlay(scanRecordInstanceQuery.getScanPlayId());
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
     *  restTemplate 调用
     *  @param process:process
     */
    public void  readFile(Process process){
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
                excOutput.append(line);
            }
        }catch (Exception e){
            throw new ApplicationException(e.getMessage());
        }
    }
}
