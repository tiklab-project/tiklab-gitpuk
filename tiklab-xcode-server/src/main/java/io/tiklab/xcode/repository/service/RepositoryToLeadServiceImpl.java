package io.tiklab.xcode.repository.service;

import io.tiklab.core.exception.SystemException;
import io.tiklab.user.user.model.User;
import io.tiklab.xcode.git.GitUntil;
import io.tiklab.xcode.repository.model.*;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class RepositoryToLeadServiceImpl implements RepositoryToLeadService {

    @Autowired
    ImportAuthService authService;

    @Autowired
    RepositoryGroupServer groupServer;

    @Autowired
    RepositoryServer repositoryServer;

    @Autowired
    XcodeYamlDataMaService xcodeYamlDataMaService;

    //导入结果
    public static Map<String , String> toLeadResult = new HashMap<>();

    @Override
    public List findThirdRepositoryList(String importAuthId,String page) {
        ImportAuth importAuth = authService.findImportAuth(importAuthId);
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
    public String toLeadRepository(RepositoryToLead repositoryToLead) {
        String result = toLeadResult.remove(repositoryToLead.getThirdRepositoryId());
   /*     if (StringUtils.isNotEmpty(result)){
            throw new SystemException(7000,"该仓库正在导入");
        }*/
        User user = new User();
        user.setId(repositoryToLead.getUserId());


        //创建仓库组
        String groupId;
        RepositoryGroup group = groupServer.findGroupByName(repositoryToLead.getGroupName());
        if (ObjectUtils.isEmpty(group)){
            RepositoryGroup repositoryGroup = new RepositoryGroup();
            repositoryGroup.setName(repositoryToLead.getGroupName());
            repositoryGroup.setRules(repositoryToLead.getRule());
            repositoryGroup.setUser(user);
            groupId = groupServer.createCodeGroup(repositoryGroup);
        }else {
            groupId = group.getGroupId();
        }

        //创建仓库
        String rpyId;
        List<Repository> repositoryList = repositoryServer.findRepositoryList(new RepositoryQuery()
                .setGroupId(groupId).setName(repositoryToLead.getRepositoryName()));
        if (CollectionUtils.isEmpty(repositoryList)){
            //创建仓库
            Repository repository = new Repository();
            repository.setName(repositoryToLead.getRepositoryName());
            repository.setAddress(repositoryToLead.getRepositoryUrl());
            RepositoryGroup repositoryGroup = new RepositoryGroup();
            repositoryGroup.setGroupId(groupId);
            repository.setGroup(repositoryGroup);
            repository.setUser(user);
            repository.setRules(repositoryToLead.getRule());
             rpyId = repositoryServer.createRpy(repository);
        }else {
             rpyId = repositoryList.get(0).getRpyId();
        }
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(new Runnable(){
            @Override
            public void run() {
                try {
                    ImportAuth importAuth = authService.findImportAuth(repositoryToLead.getImportAuthId());
                    String repositoryAddress = xcodeYamlDataMaService.repositoryAddress()+"/"+rpyId+".git";
                    GitUntil.copyRepository(repositoryAddress,repositoryToLead.getHttpRepositoryUrl(),importAuth);
                    toLeadResult.put(repositoryToLead.getThirdRepositoryId(),"success");
                } catch (Exception e) {
                    toLeadResult.put(repositoryToLead.getThirdRepositoryId(),"error,推送失败");
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
     *restTemplate  get
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
