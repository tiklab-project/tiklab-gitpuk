package io.thoughtware.gittok.repository.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.thoughtware.gittok.common.GitTokYamlDataMaService;
import io.thoughtware.gittok.common.git.GitUntil;
import io.thoughtware.gittok.repository.model.*;
import io.thoughtware.core.exception.SystemException;
import io.thoughtware.user.user.model.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

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
    GitTokYamlDataMaService yamlDataMaService;

    @Autowired
    LeadRecordService leadRecordService;

    //导入结果
    public static Map<String , String> toLeadResult = new HashMap<>();

    @Override
    public List findThirdRepositoryList(String importAuthId,String page) {
        List<LeadTo> resultList = new ArrayList<>();

        LeadAuth importAuth = authService.findLeadAuth(importAuthId);
        String address=null;
        switch (importAuth.getType()){
            case "priGitlab":
                address=importAuth.getAddress()+"/api/v4/projects?page="+page;
                break;
        }
        String path=address+"&private_token="+importAuth.getAccessToken();
        List restTemplate = getRestTemplate(path);
        List<LeadRecord> leadRecordList = leadRecordService.findLeadRecordList(new LeadRecordQuery().setLeadWay(importAuth.getType()));
        if (!CollectionUtils.isEmpty(restTemplate)){
            for (Object value:restTemplate){
                LeadTo leadTo = GitLabData(value, leadRecordList);

                resultList.add(leadTo);
            }
        }
        return resultList;
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
            return "OK";
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
                    String repositoryAddress = yamlDataMaService.repositoryAddress()+"/"+rpyId+".git";
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
     *gitLab 仓库参数
     * @param value gitlab 仓库值
     * @param leadRecordList 导入记录
     */
    public LeadTo GitLabData(Object value,List<LeadRecord> leadRecordList){
        LeadTo leadTo = new LeadTo();
        JSONObject jsonObject = (JSONObject) JSON.toJSON(value);

        leadTo.setRepositoryName(jsonObject.get("name").toString());
        //仓库路径
        leadTo.setRepositoryUrl(jsonObject.get("path_with_namespace").toString());
        //仓库组名字
        JSONObject namespace = (JSONObject) jsonObject.get("namespace");
        String groupName = namespace.get("path").toString();
        leadTo.setGroupName(groupName);

        leadTo.setHttpRepositoryUrl(jsonObject.get("http_url_to_repo").toString());
        leadTo.setThirdRepositoryId(jsonObject.get("id").toString());
        if (!CollectionUtils.isEmpty(leadRecordList)){
            String id = jsonObject.get("id").toString();
            List<LeadRecord> records = leadRecordList.stream().filter(a -> a.getRelevanceId().equals(id)).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(records)) {
                //执行结果
                leadTo.setExecResult(records.get(0).getLeadState());
                //权限
                leadTo.setRule(records.get(0).getRepository().getRules());
            }
        }
        return leadTo;
    }


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
