package io.tiklab.xcode.repository.service;

import io.tiklab.core.exception.SystemException;
import io.tiklab.user.user.model.User;
import io.tiklab.xcode.git.GitUntil;
import io.tiklab.xcode.repository.model.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * LeadToServiceImpl-执行导入
 */
@Service
public class LeadToServiceImpl implements LeadToService {

    @Autowired
    LeadAuthService authService;

    @Autowired
    RepositoryGroupServer groupServer;

    @Autowired
    RepositoryServer repositoryServer;

    @Autowired
    XcodeYamlDataMaService xcodeYamlDataMaService;

    @Autowired
    LeadRecordService leadRecordService;

    //导入结果
    public static Map<String , String> toLeadResult = new HashMap<>();

    @Override
    public List findThirdRepositoryList(String importAuthId,String page) {
        LeadAuth importAuth = authService.findLeadAuth(importAuthId);
        String address=null;
        switch (importAuth.getType()){
            case "priGitlab":
                address=importAuth.getAddress()+"/api/v4/projects?page="+page;
                break;
        }
        String path=address+"&private_token="+importAuth.getAccessToken();
        return getRestTemplate(path);
    }


    @Override
    public String toLeadRepository(LeadTo leadTo) {
        String result = toLeadResult.remove(leadTo.getThirdRepositoryId());
   /*     if (StringUtils.isNotEmpty(result)){
            throw new SystemException(7000,"该仓库正在导入");
        }*/
        User user = new User();
        user.setId(leadTo.getUserId());


        //创建仓库组
        String groupId;
        RepositoryGroup group = groupServer.findGroupByName(leadTo.getGroupName());
        if (ObjectUtils.isEmpty(group)){
            RepositoryGroup repositoryGroup = new RepositoryGroup();
            repositoryGroup.setName(leadTo.getGroupName());
            repositoryGroup.setRules(leadTo.getRule());
            repositoryGroup.setUser(user);
            groupId = groupServer.createCodeGroup(repositoryGroup);
        }else {
            groupId = group.getGroupId();
        }

        //创建仓库
        List<Repository> repositoryList = repositoryServer.findRepositoryList(new RepositoryQuery()
                .setGroupId(groupId).setName(leadTo.getRepositoryName()));
        if (!CollectionUtils.isEmpty(repositoryList)){
            toLeadResult.put(leadTo.getThirdRepositoryId(),"仓库已经导入");
            return null;
        }
        //创建仓库
        Repository repository = new Repository();
        repository.setName(leadTo.getRepositoryName());
        repository.setAddress(leadTo.getRepositoryUrl());
        RepositoryGroup repositoryGroup = new RepositoryGroup();
        repositoryGroup.setGroupId(groupId);
        repository.setGroup(repositoryGroup);
        repository.setUser(user);
        repository.setRules(leadTo.getRule());
        String rpyId = repositoryServer.createRpy(repository);
        repository.setRpyId(rpyId);

        //导入认证
        LeadAuth importAuth = authService.findLeadAuth(leadTo.getImportAuthId());

        //创建导入记录
        LeadRecord leadRecord = new LeadRecord();
        leadRecord.setRepository(repository);
        leadRecord.setLeadWay(importAuth.getType());
        leadRecord.setRelevanceId(leadTo.getThirdRepositoryId());

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(new Runnable(){
            @Override
            public void run() {
                try {
                    String repositoryAddress = xcodeYamlDataMaService.repositoryAddress()+"/"+rpyId+".git";
                    //从第三方复制裸仓库
                    GitUntil.copyRepository(repositoryAddress,leadTo.getHttpRepositoryUrl(),importAuth);
                    toLeadResult.put(leadTo.getThirdRepositoryId(),"success");

                    leadRecord.setLeadState("success");
                    leadRecordService.createLeadRecord(leadRecord);
                } catch (Exception e) {
                    toLeadResult.put(leadTo.getThirdRepositoryId(),"error,推送失败");
                    leadRecord.setLeadState("error");
                    leadRecordService.createLeadRecord(leadRecord);
                    throw new SystemException(9000,e.getMessage());
                }
            }});
        return "OK";
    }

    @Override
    public String findToLeadResult(String thirdRepositoryId) {
        String result = toLeadResult.get(thirdRepositoryId);
        if (StringUtils.isNotEmpty(result)&&result.contains("OK")&&result.contains("error")){
            toLeadResult.remove(thirdRepositoryId);
        }
        return result;
    }


    /**
     *restTemplate调用第三方接口
     * @param path 路径
     */
    public List getRestTemplate(String path){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8));
        ResponseEntity<List> responseEntity = restTemplate.getForEntity(path, List.class);
        if (responseEntity.getStatusCode().value()==200){
            List body = responseEntity.getBody();
            return body;
        }
        return null;
    }
}
