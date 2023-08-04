package io.tiklab.xcode.detection.service;

import com.alibaba.fastjson.JSONObject;
import io.tiklab.beans.BeanMapper;
import io.tiklab.core.exception.ApplicationException;
import io.tiklab.join.JoinTemplate;
import io.tiklab.xcode.detection.dao.CodeScanDao;
import io.tiklab.xcode.detection.entity.CodeScanEntity;
import io.tiklab.xcode.detection.model.*;
import io.tiklab.xcode.git.GitUntil;
import io.tiklab.xcode.repository.model.Repository;
import io.tiklab.xcode.repository.service.RepositoryServer;
import io.tiklab.xcode.common.RepositoryUtil;
import io.tiklab.xcode.repository.service.XcodeYamlDataMaService;
import org.apache.commons.collections.CollectionUtils;
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
    XcodeYamlDataMaService yamlDataMaService;
    
    public static Map<String , String> codeScanState = new HashMap<>();

    public static Map<String , CodeScanInstance> codeScanLog = new HashMap<>();

    @Override
    public boolean codeScanExec(String repositoryId) {
        codeScanLog.remove(repositoryId);
        CodeScanInstance scanInstance = new CodeScanInstance();
        scanInstance.setRepositoryId(repositoryId);
        scanInstance.setTaskName("123");

        CodeScan codeScan = findCodeScanByRpyId(repositoryId);
        if (ObjectUtils.isEmpty(codeScan)){
            scanInstance.setRunState("false");
            codeScanLog.put(repositoryId,scanInstance);
            throw new ApplicationException(6006,"请先设置里面选择配置");
        }
        if (ObjectUtils.isEmpty(codeScan.getDeployEnvId())){
            scanInstance.setRunState("false");
            codeScanLog.put(repositoryId,scanInstance);
            throw new ApplicationException(6006,"不存在maven配置");
        }
        DeployEnv deployEnv = deployEnvService.findDeployEnv(codeScan.getDeployEnvId());
        String execOrder =  "mvn clean verify sonar:sonar ";

        if (ObjectUtils.isEmpty(codeScan.getDeployEnvId())){
            scanInstance.setRunState("false");
            codeScanLog.put(repositoryId,scanInstance);
            throw new ApplicationException(6006,"不存在sonar配置");
        }
        DeployServer deployServer = codeScan.getDeployServer();

        String mavenAddress = deployEnv.getEnvAddress();
        RepositoryUtil.validFile(mavenAddress,21);

        execOrder = execOrder +
                " -Dsonar.projectKey="+ codeScan.getRepository().getName()+
                " -Dsonar.host.url="+ deployServer.getServerAddress();
        if ( "account".equals(deployServer.getAuthType())){
            execOrder = execOrder +
                    " -Dsonar.login="+deployServer.getUserName()+
                    " -Dsonar.password="+deployServer.getPassWord();
        }else {
            execOrder = execOrder +
                    " -Dsonar.login="+deployServer.getPrivateKey();
        }

        String cloneRepositoryUrl = RepositoryUtil.SystemTypeAddress(yamlDataMaService.repositoryAddress()+"/clone/" + codeScan.getRepository().getName());
        String order = " ./" + execOrder + " " + "-f" +" " +cloneRepositoryUrl ;
        if (RepositoryUtil.findSystemType() == 1){
            order = " .\\" + execOrder + " " + "-f"+" "  +cloneRepositoryUrl;
        }

        Process process;

        try {
            //克隆项目
            String repositoryUrl = RepositoryUtil.SystemTypeAddress(yamlDataMaService.repositoryAddress() +"/"+ codeScan.getRepository().getName() + ".git");
            String cloneUrl =  RepositoryUtil.SystemTypeAddress(yamlDataMaService.repositoryAddress()+"/clone/"+codeScan.getRepository().getName());
            GitUntil.cloneRepository(repositoryUrl, "master", cloneUrl);

            process = RepositoryUtil.process(mavenAddress, order);
            readFile(process,repositoryId,scanInstance);

            scanInstance.setRunState("true");
            codeScanLog.put(repositoryId,scanInstance);
            instanceService.createCodeScanInstance(scanInstance);
            return true;
        }catch (Exception e){
            //这个异常是已经有存在的项目
            if (e.getMessage().contains("already exists and is not an empty directory")){
                scanInstance.setRunState("false");
                codeScanLog.put(repositoryId,scanInstance);
                throw  new ApplicationException(6005,"该项目正在扫描中");
            }
            scanInstance.setRunState("false");
            codeScanLog.put(repositoryId,scanInstance);
            throw new ApplicationException(e.getMessage());
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
            throw new RuntimeException(e);
        }
        return codeScan;
    }

    @Override
    public CodeScanInstance findScanState(String repositoryId) {
        CodeScanInstance scanInstance = codeScanLog.get(repositoryId);
        if (!ObjectUtils.isEmpty(scanInstance)){
            Repository repository = repositoryServer.findOneRpy(repositoryId);
            RepositoryUtil.deleteDireAndFile(yamlDataMaService.repositoryAddress()+"/clone/",repository.getName());
        }
        return   scanInstance;
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
