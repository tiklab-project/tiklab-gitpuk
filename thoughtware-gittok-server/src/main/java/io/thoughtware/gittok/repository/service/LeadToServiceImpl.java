package io.thoughtware.gittok.repository.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.thoughtware.core.page.Pagination;
import io.thoughtware.core.page.PaginationBuilder;
import io.thoughtware.eam.common.context.LoginContext;
import io.thoughtware.gittok.common.GitTokFinal;
import io.thoughtware.gittok.common.GitTokYamlDataMaService;
import io.thoughtware.gittok.common.git.GitUntil;
import io.thoughtware.gittok.repository.model.*;
import io.thoughtware.core.exception.SystemException;
import io.thoughtware.user.user.model.User;
import okhttp3.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * LeadToServiceImpl-执行导入
 */
@Service
public class LeadToServiceImpl implements LeadToService {
    private static Logger logger = LoggerFactory.getLogger(LeadToServiceImpl.class);

    @Autowired
    LeadAuthService authService;

    @Autowired
    RepositoryGroupServer groupServer;

    @Autowired
    RepositoryService repositoryServer;

    @Autowired
    GitTokYamlDataMaService yamlDataMaService;

    @Autowired
    LeadRecordService leadRecordService;

    //导入结果
    public static Map<String , LeadToResult> toLeadResult = new HashMap<>();

    @Override
    public Pagination<LeadTo> findThirdRepositoryList(String importAuthId,String page) {
        //返回结果
        Pagination<LeadTo> resultPagination = new Pagination<>();

        try {
            //第三方的认证信息
            LeadAuth importAuth = authService.findLeadAuth(importAuthId);
            switch (importAuth.getType()){
                case "priGitlab":
                     resultPagination = findGitlabRpy(importAuth, resultPagination, page);
                    break;
                case "gitlab":
                    resultPagination= findGitlabRpy(importAuth,resultPagination, page);
                    break;
                case "github":
                    resultPagination= findGithubRpy(importAuth,resultPagination,page);
                    break;
                case "gitee":
                    resultPagination=findGiteeRpy(importAuth,resultPagination,page);
                    break;
            }
            return resultPagination;
        }catch (Exception e){
            throw new SystemException(e);
        }
    }


    /**
     *  findGitlabRpy 获取gitlab 仓库
     * @param leadAuth 认证信息
     * @param resultPagination 返回结果
     * @param  page 分页数
     */
    public Pagination<LeadTo> findGitlabRpy(LeadAuth leadAuth,Pagination<LeadTo> resultPagination,String page){
        List<LeadTo> resultList = new ArrayList();

        String address= GitTokFinal.GITLAB_API_URL;
        if (("priGitlab").equals(leadAuth.getType())){
            address=leadAuth.getAddress()+"/api/v4/projects";
        }
       // owned=true 自己创建的; simple=true 获取简化的数据
       String path=  address+"?owned=true&page="+page+"&private_token="+leadAuth.getAccessToken();

        //RestTemplate 调用接口
        ResponseEntity<List> restTemplate = getRestTemplate(path);
        if (restTemplate.getStatusCode().value()==200){
            //仓库总数、总页
            HttpHeaders headers = restTemplate.getHeaders();
            String total = headers.get("X-Total").get(0);
            String totalPage = headers.get("X-Total-Pages").get(0);

            resultPagination.setTotalRecord(Integer.valueOf(total));
            resultPagination.setTotalPage(Integer.valueOf(totalPage));
            resultPagination.setCurrentPage(Integer.valueOf(page));

            //仓库列表数据
            List body = restTemplate.getBody();
            //查询导入数据
            List<LeadRecord> leadRecordList = leadRecordService.findLeadRecordList(new LeadRecordQuery().setLeadWay(leadAuth.getType()));
            if (!CollectionUtils.isEmpty(body)){
                for (Object value:body){
                    LeadTo leadTo = new LeadTo();
                    JSONObject jsonObject = (JSONObject) JSON.toJSON(value);

                    leadTo.setRepositoryName(jsonObject.get("name").toString());
                    //仓库路径
                    leadTo.setRepositoryUrl(jsonObject.get("path_with_namespace").toString());
                    //仓库组名字
                    JSONObject namespace = (JSONObject) jsonObject.get("namespace");
                    String groupName = namespace.get("path").toString();
                    leadTo.setGroupName(groupName);

                    //权限
                    leadTo.setRule(jsonObject.get("visibility").toString());

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
                    resultList.add(leadTo);
                }
            }
            return PaginationBuilder.build(resultPagination,resultList);
        }
        return null;
    }


    /**
     *  findGithubRpy 获取github 仓库
     * @param leadAuth 认证信息
     * @param resultPagination 返回结果
     * @param  page 分页数
     */
    public Pagination<LeadTo> findGithubRpy(LeadAuth leadAuth,Pagination<LeadTo> resultPagination,String page) throws IOException {
        List<LeadTo> resultList = new ArrayList();

       // postRestTemplate(GitTokFinal.GITHUB_API_URL + "?owned=true&simple=true&per_page=20&page="+page,leadAuth);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(GitTokFinal.GITHUB_API_URL + "?owned=true&simple=true&per_page=20&page="+page)
                .header("Authorization", "token "+leadAuth.getAccessToken()) //认证accToken
                .header("accept","application/vnd.github+json")
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            Headers headers = response.headers();
            String responseBody = response.body().string();
            Object parse = JSONArray.parse(responseBody);
            List<Object> jsonObjects = (List<Object>) parse;

            List<LeadRecord> leadRecordList = leadRecordService.findLeadRecordList(new LeadRecordQuery().setLeadWay(leadAuth.getType()));
            if (!CollectionUtils.isEmpty(jsonObjects)){
                for (Object value:jsonObjects){
                    //拼接结果
                    LeadTo leadTo = new LeadTo();

                    JSONObject jsonObject = (JSONObject) JSON.toJSON(value);
                    leadTo.setRepositoryName(jsonObject.get("name").toString());
                    //仓库路径
                    leadTo.setRepositoryUrl(jsonObject.get("full_name").toString());
                    //仓库组名字
                    String groupName = StringUtils.substringBefore(jsonObject.get("full_name").toString(), "/");
                    leadTo.setGroupName(groupName);
                    leadTo.setHttpRepositoryUrl(jsonObject.get("clone_url").toString());
                    leadTo.setThirdRepositoryId(jsonObject.get("id").toString());

                    //权限

                    leadTo.setRule(jsonObject.get("visibility").toString());

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
                    resultList.add(leadTo);
                }
            }
            return PaginationBuilder.build(resultPagination,resultList);
        }
        return null;
    }

    /**
     *  findGiteeRpy 获取gitee 仓库
     * @param leadAuth 认证信息
     * @param resultPagination 返回结果
     * @param  page 分页数
     */
    public Pagination<LeadTo> findGiteeRpy(LeadAuth leadAuth,Pagination<LeadTo> resultPagination,String page){
        String path=  GitTokFinal.GITEE_API_URL+"?access_token="+leadAuth.getAccessToken()+
                "&sort=full_name&per_page=20&page="+page;

        List<LeadTo> resultList = new ArrayList<>();

        //RestTemplate 调用接口
        ResponseEntity<List> restTemplate = getRestTemplate(path);
        if (restTemplate.getStatusCode().value()==200){
            //仓库总数、总页
            HttpHeaders headers = restTemplate.getHeaders();
            String total = headers.get("total_count").get(0);
            String totalPage = headers.get("total_page").get(0);

            resultPagination.setTotalRecord(Integer.valueOf(total));
            resultPagination.setTotalPage(Integer.valueOf(totalPage));
            resultPagination.setCurrentPage(Integer.valueOf(page));

            //仓库列表数据
            List body = restTemplate.getBody();
            String string = body.toString();
            //查询导入数据
            List<LeadRecord> leadRecordList = leadRecordService.findLeadRecordList(new LeadRecordQuery().setLeadWay(leadAuth.getType()));
            if (!CollectionUtils.isEmpty(body)){
                for (Object value:body){
                    //拼接结果
                    LeadTo leadTo = new LeadTo();

                    JSONObject jsonObject = (JSONObject) JSON.toJSON(value);
                    leadTo.setRepositoryName(jsonObject.get("name").toString());
                    //仓库路径
                    leadTo.setRepositoryUrl(jsonObject.get("human_name").toString());
                    //仓库组名字
                    JSONObject namespace = (JSONObject) jsonObject.get("namespace");
                    String groupName = namespace.get("name").toString();
                    leadTo.setGroupName(groupName);

                    //权限
                    String aPublic = jsonObject.get("public").toString();
                    if (("true").equals(aPublic)){
                        leadTo.setRule("public");
                    }
                    String aPrivate = jsonObject.get("private").toString();
                    if (("true").equals(aPrivate)){
                        leadTo.setRule("private");
                    }

                    leadTo.setHttpRepositoryUrl(jsonObject.get("html_url").toString());
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
                    resultList.add(leadTo);
                }
            }
            return PaginationBuilder.build(resultPagination,resultList);
        }
        return null;
    }

    @Override
    public String toLeadRepository(LeadToQuery leadToQuery) {
        String loginId = LoginContext.getLoginId();
        List<LeadTo> leadToList = leadToQuery.getLeadToList();
        toLeadResult.remove(loginId);
   /*     if (StringUtils.isNotEmpty(result)){
            throw new SystemException(7000,"该仓库正在导入");
        }*/


        //添加执行结果
        putResult(leadToList,"run");

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(new Runnable(){
            @Override
            public void run() {
                for (LeadTo leadTo:leadToList){
                    LeadRecord leadRecord = new LeadRecord();
                    leadRecord.setRelevanceId(leadTo.getThirdRepositoryId());
                    leadRecord.setLeadState("run");

                    addExecResult(leadRecord,leadTo,"run,开始导入仓库");
                    try {
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
                            //返回结果
                            logger.info(leadTo.getRepositoryUrl()+"，推送失败，仓库已经导入");
                            addExecResult(leadRecord,leadTo,"fail,仓库已经导入");
                            putResult(leadToList,"fail");
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
                        leadRecord.setRepository(repository);
                        leadRecord.setLeadWay(importAuth.getType());
                        String leadRecordId = leadRecordService.createLeadRecord(leadRecord);
                        leadRecord.setId(leadRecordId);


                        String repositoryAddress = yamlDataMaService.repositoryAddress()+"/"+rpyId+".git";
                        //从第三方复制裸仓库
                        GitUntil.copyRepository(repositoryAddress,leadTo.getHttpRepositoryUrl(),importAuth);

                        //添加返回结果
                        logger.info(leadTo.getRepositoryUrl()+"success，推送成功");
                        addExecResult(leadRecord,leadTo,"success，推送成功");


                        leadRecordService.updateLeadRecord(leadRecord);
                    } catch (Exception e) {
                        //添加返回结果
                        logger.info(leadTo.getRepositoryUrl()+"，推送失败："+e.getMessage());
                        addExecResult(leadRecord,leadTo,"fail,推送失败："+e.getMessage());
                        putResult(leadToList,"fail");

                        leadRecordService.updateLeadRecord(leadRecord);
                        throw new SystemException(e);
                    }
                }
                putResult(leadToList,"success");
            }});
        return "OK";
    }

    @Override
    public LeadToResult findToLeadResult(String key) {
        LeadToResult leadToResult = toLeadResult.get(key);
   /*     if (StringUtils.isNotEmpty(result)&&result.contains("OK")&&result.contains("fail")){
            toLeadResult.remove(thirdRepositoryId);
        }*/
        return leadToResult;
    }


    /**
     *  getRestTemplate 通过RestTemplate 查询
     * @param path 查询路径
     */
    public ResponseEntity<List>  getRestTemplate(String path){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8));
        ResponseEntity<List> response = restTemplate.getForEntity(path, List.class);
        return response;
    }



    public void postRestTemplate(String path,LeadAuth leadAuth){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","token "+leadAuth.getAccessToken());
        headers.add("accept","application/vnd.github+json");

        // 请求
        HttpEntity<String> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List> exchange = restTemplate.exchange(path, HttpMethod.GET, request, List.class);

        System.out.println("");
    }

    /**
     *  getResult 获取结果
     * @param leadToList leadToList
     * @param grossResult 总结果
     */
    public void putResult(List<LeadTo> leadToList,String grossResult){
        LeadToResult leadToResult = new LeadToResult();
        leadToResult.setLeadToList(leadToList);
        leadToResult.setGrossResult(grossResult);
        String loginId = LoginContext.getLoginId();

        toLeadResult.put(loginId,leadToResult);
    }

    /**
     *  addExecResult 添加执行的结果数据
     * @param leadRecord 执行结果
     * @param  leadTo leadTo
     * @param msg 消息
     */
    public void addExecResult(LeadRecord leadRecord,LeadTo leadTo,String msg){
        if (msg.startsWith("fail")){
            leadRecord.setLeadState("fail");
            leadTo.setExecResult("fail");
        }
        if (msg.startsWith("success")){
            leadRecord.setLeadState("success");
            leadTo.setExecResult("success");
        }
        if (msg.startsWith("run")){
            leadRecord.setLeadState("run");
            leadTo.setExecResult("run");
        }
        leadRecord.setLog(msg);
        leadTo.setLog(msg);
    }
}
