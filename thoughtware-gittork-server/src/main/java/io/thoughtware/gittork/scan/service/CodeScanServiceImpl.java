package io.thoughtware.gittork.scan.service;

import com.alibaba.fastjson.JSONObject;
import io.thoughtware.gittork.common.GitTorkYamlDataMaService;
import io.thoughtware.gittork.repository.model.Repository;
import io.thoughtware.gittork.repository.service.RepositoryServer;
import io.thoughtware.gittork.scan.model.*;
import io.thoughtware.beans.BeanMapper;
import io.thoughtware.core.exception.ApplicationException;
import io.thoughtware.core.exception.SystemException;
import io.thoughtware.join.JoinTemplate;
import io.thoughtware.gittork.scan.dao.CodeScanDao;
import io.thoughtware.gittork.scan.entity.CodeScanEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class CodeScanServiceImpl implements CodeScanService {
    private static Logger logger = LoggerFactory.getLogger(CodeScanServiceImpl.class);

    @Autowired
    CodeScanDao codeScanDao;

    @Autowired
    DeployServerService deployServerService;

    @Autowired
    DeployEnvService deployEnvService;
    @Autowired
    RepositoryServer repositoryServer;

    @Autowired
    JoinTemplate joinTemplate;

    @Autowired
    CodeScanInstanceService instanceService;

    @Autowired
    GitTorkYamlDataMaService yamlDataMaService;


    @Autowired
    ScanPlayService scanPlayService;

    @Autowired
    ScanSchemeRuleSetService scanSchemeRuleSetService;

    @Autowired
    CodeScanSpotBugsService scanSpotBugsService;

    @Autowired
    CodeScanSonarService scanSonarService;
    
    public static Map<String , String> codeScanState = new HashMap<>();

    public static Map<String , CodeScanInstance> codeScanLog = new HashMap<>();


    @Override
    public String codeScanExec(String scanPlayId) {
   /*     String state = codeScanState.get(scanPlayId);
        //扫描状态不为空且正在执行中
        if (StringUtils.isNotEmpty(state)&&("true").equals(state)){
            throw new SystemException(918,"该扫描计划正在执行中");
        }*/

        ScanPlay scanPlay = scanPlayService.findOne(scanPlayId);
        //扫描计划中的扫描方案
        ScanScheme scanScheme = scanPlay.getScanScheme();

        //启动线程执行代码扫描
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(new Runnable(){
            @Override
            public void run() {
                codeScanState.put(scanPlayId,"true");
                if (("rule").equals(scanScheme.getScanWay())){
                    //查询出规则集
                    List<ScanSchemeRuleSet> schemeRuleList = scanSchemeRuleSetService.findScanSchemeRuleSetList(new ScanSchemeRuleSetQuery().setScanSchemeId(scanScheme.getId()));
                    if (CollectionUtils.isEmpty(schemeRuleList)){
                        throw new SystemException(900,"关联的方案中没有添加规则");
                    }
                    //通过spotBugs 扫描
                    scanSpotBugsService.codeScanBySpotBugs(scanPlay);
                }
                //通过sonar扫描
                if (("sonar").equals(scanScheme.getScanWay())){
                     scanSonarService.codeScanBySonar(scanPlay);
                }
            }});
        return "ok";
    }

    @Override
    public ScanRecord findScanState(String scanPlayId,String scanWay) {
        if (("sonar").equals(scanWay)){
            return scanSonarService.findScanBySonar(scanPlayId);
        }else {
            return scanSpotBugsService.findScanBySpotBugs(scanPlayId);
        }
    }


    /**
     *  执行日志
     *  @param process:process
     */
    public void  readFile(Process process) throws IOException {

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

    }






    @Override
    public CodeScan findScanResult(String repositoryId) {
        CodeScan codeScan = this.findCodeScanByRpyId(repositoryId);
        try {
            Repository repository = codeScan.getRepository();

            DeployServer deployServer = codeScan.getDeployServer();

            //查询项目
            String findRepositoryUrl=deployServer.getServerAddress()+"/api/projects/search?projects="+repository.getName();
            JSONObject findResultBody = restTemplate(findRepositoryUrl, deployServer);
            Object components = findResultBody.get("components");
            List<Object> resultList = new ArrayList<>();
            for (Object o : (List<Object>) components){
                resultList.add(o);
            }
            LinkedHashMap hashMap = (LinkedHashMap) resultList.get(0);
            String key = hashMap.get("key").toString();


            //查询项目扫描状态
            String stateUrl=deployServer.getServerAddress()+"/api/qualitygates/project_status?projectKey="+repository.getName();
            JSONObject statResultBody = restTemplate(stateUrl, deployServer);
            LinkedHashMap projectStatus = (LinkedHashMap) statResultBody.get("projectStatus");
            String status = projectStatus.get("status").toString();
            codeScan.setScanStatus(status);


            //查询项目扫描结果
            String codeScanUrl=deployServer.getServerAddress()+"/api/measures/component?component="+key+"&metricKeys=bugs,coverage,vulnerabilities,code_smells";
            JSONObject scanResultBody = restTemplate(codeScanUrl, deployServer);
            LinkedHashMap component =(LinkedHashMap) scanResultBody.get("component");
            Object measures = component.get("measures");
            for (Object result : (List<Object>) measures){
                LinkedHashMap jsonObject = (LinkedHashMap) result;
                if ("bugs".equals(jsonObject.get("metric"))) {
                    codeScan.setBugs(jsonObject.get("value").toString());
                }
                if ("code_smells".equals(jsonObject.get("metric"))) {
                    codeScan.setCodeSmells(jsonObject.get("value").toString());
                }
                if ("vulnerabilities".equals(jsonObject.get("metric"))) {
                    codeScan.setVulnerabilities(jsonObject.get("value").toString());
                }
            }

        } catch (Exception e) {
            if (e.getMessage().contains("Connection refused")){
                logger.info(e.getMessage());
                codeScan.setScanStatus("not");
                return codeScan;
            }
            throw new SystemException(e);
        }
        return codeScan;
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
     * @param repositoryId: repositoryId
     */
    public void  readFile( Process process,String repositoryId,CodeScanInstance scanInstance){
        CodeScanInstance instance = codeScanLog.get(repositoryId);
        try {
            InputStream inputStream = process.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream(inputStream.available());
            BufferedInputStream in = new BufferedInputStream(inputStream);
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;

            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                Date data = new Date();
                if (ObjectUtils.isEmpty(instance)){
                    scanInstance.setRunLog( "["+data+"]"+bos);
                }else {
                    scanInstance.setRunLog( "["+data+"]"+instance.getRunLog()+"\n"+bos);
                }
                codeScanLog.put(repositoryId,scanInstance);
                logger.info(bos.toString());
                bos.write(buffer, 0, len);
               codeScanState.put(repositoryId,bos.toString());
            }
        }catch (Exception e){
            throw new ApplicationException(e.getMessage());
        }
    }

    /**
     * 创建代码扫描
     * @param codeScan
     * @return
     */
    public String createCodeScan(CodeScan codeScan){

        CodeScan scanByRpy = this.findCodeScanByRpyId(codeScan.getRepository().getRpyId());

        if (ObjectUtils.isEmpty(scanByRpy)){
            CodeScanEntity codeScanEntity = BeanMapper.map(codeScan, CodeScanEntity.class);
            codeScanDao.createCodeScan(codeScanEntity);
        }else {
            codeScan.setId(scanByRpy.getId());
            CodeScanEntity codeScanEntity = BeanMapper.map(codeScan, CodeScanEntity.class);
             codeScanDao.updateCodeScan(codeScanEntity);
        }
        return null;
    }

    /**
     * 通过仓库id查询
     * @param rpyId
     * @return
     */
    public CodeScan findCodeScanByRpyId(String rpyId){
        CodeScan codeScan=null;
        List<CodeScanEntity> codeScanList = codeScanDao.findCodeScanList(new CodeScanQuery().setRepositoryId(rpyId));
        if (CollectionUtils.isNotEmpty(codeScanList)){
            CodeScanEntity codeScanEntity = codeScanList.get(0);
             codeScan = BeanMapper.map(codeScanEntity, CodeScan.class);
            joinTemplate.joinQuery(codeScan);
        }
        return codeScan;
    }



}
