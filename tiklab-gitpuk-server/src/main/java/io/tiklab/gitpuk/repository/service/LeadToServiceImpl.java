package io.tiklab.gitpuk.repository.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.tiklab.core.page.Pagination;
import io.tiklab.core.page.PaginationBuilder;
import io.tiklab.eam.common.context.LoginContext;
import io.tiklab.gitpuk.repository.model.*;
import io.tiklab.gitpuk.common.GitPukFinal;
import io.tiklab.gitpuk.common.GitPukYamlDataMaService;
import io.tiklab.gitpuk.common.RepositoryUtil;
import io.tiklab.gitpuk.common.git.GitUntil;
import io.tiklab.core.exception.SystemException;
import io.tiklab.user.user.model.User;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
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

import java.io.File;
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
    GitPukYamlDataMaService yamlDataMaService;

    @Autowired
    LeadRecordService leadRecordService;

    //导入结果
    public static Map<String , LeadToResult> toLeadResult = new HashMap<>();

    @Override
    public Pagination<LeadTo> findThirdRepositoryList(LeadToQuery leadToQuery) {
        //返回结果
        Pagination<LeadTo> resultPagination = new Pagination<>();

        try {
            //第三方的认证信息
            LeadAuth importAuth = authService.findLeadAuth(leadToQuery.getImportAuthId());
            switch (importAuth.getType()){
                case "priGitlab":
                     resultPagination = findGitlabRpy(importAuth, resultPagination, leadToQuery);
                    break;
                case "gitlab":
                    resultPagination= findGitlabRpy(importAuth,resultPagination, leadToQuery);
                    break;
                case "github":
                    resultPagination= findGithubRpy(importAuth,resultPagination,leadToQuery);
                    break;
                case "gitee":
                    resultPagination=findGiteeRpy(importAuth,resultPagination,leadToQuery);
                    break;
                case "priBitbucket":
                    resultPagination=findBitbucketRpy(importAuth,resultPagination,leadToQuery);
                    break;
            }
            return resultPagination;
        }catch (Exception e){
            logger.info("查询仓库失败"+e.getMessage());
            throw new SystemException(e);
        }
    }


    /**
     *  findGitlabRpy 获取gitlab 仓库
     * @param leadAuth 认证信息
     * @param resultPagination 返回结果
     * @param  leadToQuery leadToQuery
     */
    public Pagination<LeadTo> findGitlabRpy(LeadAuth leadAuth,Pagination<LeadTo> resultPagination,LeadToQuery leadToQuery){
        List<LeadTo> resultList = new ArrayList();

        String address= GitPukFinal.GITLAB_API_URL;
        if (("priGitlab").equals(leadAuth.getType())){
            String authAddress = leadAuth.getAddress();
            if (authAddress.endsWith("/")){
                authAddress=StringUtils.substringBeforeLast(authAddress,"/");
            }

            address=authAddress+"/api/v4/projects";
        }
       // owned=true 自己创建的; simple=true 获取简化的数据
       String path=  address+"?min_access_level=10&page="+leadToQuery.getPage()+"&private_token="+leadAuth.getAccessToken();

        //RestTemplate 调用接口
        ResponseEntity<List> restTemplate = getRestTemplate(path);
        if (restTemplate.getStatusCode().value()==200){
            //仓库总数、总页
            HttpHeaders headers = restTemplate.getHeaders();
            String total = headers.get("X-Total").get(0);
            String totalPage = headers.get("X-Total-Pages").get(0);

            resultPagination.setTotalRecord(Integer.valueOf(total));
            resultPagination.setTotalPage(Integer.valueOf(totalPage));
            resultPagination.setCurrentPage(Integer.valueOf(leadToQuery.getPage()));

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
     * @param  leadToQuery leadToQuery
     */
    public Pagination<LeadTo> findGithubRpy(LeadAuth leadAuth,Pagination<LeadTo> resultPagination,LeadToQuery leadToQuery) throws IOException {
        List<LeadTo> resultList = new ArrayList();

       // postRestTemplate(GitTokFinal.GITHUB_API_URL + "?owned=true&simple=true&per_page=20&page="+page,leadAuth);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(GitPukFinal.GITHUB_API_URL + "?per_page=20&page="+leadToQuery.getPage())
                .header("Authorization", "token "+leadAuth.getAccessToken()) //认证accToken
                .header("accept","application/vnd.github.v3+json")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            resultPagination.setCurrentPage(Integer.valueOf(leadToQuery.getPage()));
           //获取总页数
            Headers headers = response.headers();
            String link = headers.get("link");
            List<String> stringList = Arrays.asList(link.split(","));
            if (link.contains("last")){
                for (String data:stringList){
                    if (data.contains("last")){
                        String[] split = data.split(";");
                        String totalPage = split[0].substring(split[0].lastIndexOf("page=")+5,split[0].length()-1);
                        resultPagination.setTotalPage(Integer.valueOf(totalPage));
                    }
                }
            }else {
                for (String data:stringList){
                    if (data.contains("prev")){
                        String[] split = data.split(";");
                        String totalPage = split[0].substring(split[0].lastIndexOf("page=")+5,split[0].length()-1);
                        resultPagination.setTotalPage(Integer.valueOf(totalPage)+1);
                    }
                }
            }

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
     * @param  leadToQuery leadToQuery
     */
    public Pagination<LeadTo> findGiteeRpy(LeadAuth leadAuth,Pagination<LeadTo> resultPagination,LeadToQuery leadToQuery){
        String path=  GitPukFinal.GITEE_API_URL+"?access_token="+leadAuth.getAccessToken()+
                "&sort=full_name&per_page=20&page="+leadToQuery.getPage();

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
            resultPagination.setCurrentPage(Integer.valueOf(leadToQuery.getPage()));

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


    /**
     *  findBitbucketRpy 获取bucket 仓库
     * @param leadAuth 认证信息
     * @param resultPagination 返回结果
     * @param  leadToQuery leadToQuery
     */
    public Pagination<LeadTo> findBitbucketRpy(LeadAuth leadAuth,Pagination<LeadTo> resultPagination,LeadToQuery leadToQuery) throws IOException {
        List<LeadTo> resultList = new ArrayList<>();

        String authAddress = leadAuth.getAddress();
        if (leadAuth.getAddress().endsWith("/")){
            authAddress=StringUtils.substringBeforeLast(authAddress,"/");
        }

        List<Integer> pageStartList = new ArrayList<>();
        Integer limit=25;
        //查询总数据
        Integer totalPage=0;
        Integer totalRecord=0;
        Boolean isLastPage=false;
        Integer nextPageStart=0;
        while (!isLastPage){
            String address =authAddress+"/rest/api/1.0/repos?start=" + nextPageStart + "&limit=" + limit;
            ResponseEntity<JSONObject> restTemplate = getRestTemplate(address, leadAuth.getAccessToken());
            if (restTemplate.getStatusCode().value()==200){
                JSONObject body = restTemplate.getBody();
                pageStartList.add(nextPageStart);

                //是否是最后页
                isLastPage = (Boolean) body.get("isLastPage");
                if (!isLastPage){
                    nextPageStart=Integer.valueOf(body.get("nextPageStart").toString());
                }

                //数量
                totalRecord+=Integer.valueOf(body.get("size").toString());
                totalPage+=1;
            }
        }
        resultPagination.setTotalRecord(totalRecord);
        resultPagination.setTotalPage(totalPage);
        resultPagination.setCurrentPage(leadToQuery.getPage());

        //查询当前页 仓库列表
        String address =authAddress+"/rest/api/1.0/repos?start=" + leadToQuery.getNextPageStart() + "&limit=" + limit;
        ResponseEntity<JSONObject> restTemplate = getRestTemplate(address, leadAuth.getAccessToken());
        if (restTemplate.getStatusCode().value()==200){
            JSONObject body = restTemplate.getBody();
            //是否是最后页
            Boolean isLast = (Boolean) body.get("isLastPage");
            Integer pageStart=0;
            if (!isLast){
                 pageStart = Integer.valueOf(body.get("nextPageStart").toString());
            }

            //仓库内容
            List<Object> values = (List<Object>) body.get("values");
            List<LeadRecord> leadRecordList = leadRecordService.findLeadRecordList(new LeadRecordQuery().setLeadWay(leadAuth.getType()));
            for (Object value:values ){
                LeadTo leadTo = new LeadTo();
                JSONObject jsonObject = (JSONObject) JSON.toJSON(value);
                //项目
                JSONObject project = (JSONObject) JSON.toJSON(jsonObject.get("project"));
                String projectName = project.get("name").toString();
                leadTo.setGroupName(projectName);

                //存储库
                String repositoriesName = jsonObject.get("name").toString();
                leadTo.setRepositoryName(repositoriesName);
                leadTo.setRepositoryUrl(projectName+"/"+repositoriesName);

                //存储库地址
                JSONObject links = (JSONObject) JSON.toJSON(jsonObject.get("links"));
                List<JSONObject> self = (List<JSONObject>) links.get("clone");
                List<JSONObject> collect = self.stream().filter(a -> ("http").equals(a.get("name"))).collect(Collectors.toList());

                String href = collect.get(0).get("href").toString();
                leadTo.setHttpRepositoryUrl(href);

                String id = jsonObject.get("id").toString();
                leadTo.setThirdRepositoryId(id);

                String rule = jsonObject.get("public").toString();
                String s = rule.equals("false") ? "private" : "public";
                leadTo.setRule(s);

                leadTo.setPageStartList(pageStartList);
                leadTo.setNextPageStart(pageStart);
                leadTo.setCurrentPageStart(leadToQuery.getNextPageStart());

                if (!CollectionUtils.isEmpty(leadRecordList)){
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


    @Override
    public String toLeadRepository(LeadToQuery leadToQuery) {
        String loginId = LoginContext.getLoginId();

        List<LeadTo> leadToList = leadToQuery.getLeadToList();
        toLeadResult.remove(loginId);
   /*     if (StringUtils.isNotEmpty(result)){
            throw new SystemException(7000,"该仓库正在导入");
        }*/


        //添加执行结果
        putResult(leadToList,"run",null);

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(new Runnable(){
            @Override
            public void run() {
                for (LeadTo leadTo:leadToList){
                    LeadRecord leadRecord = new LeadRecord();
                    leadRecord.setRelevanceId(leadTo.getThirdRepositoryId());
                    leadRecord.setLeadState("run");

                    addExecResult(leadRecord,leadTo,"run,开始导入仓库");
                    String rpyId=null;
                    try {
                        User user = new User();
                        user.setId(leadToQuery.getUserId());


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

                        //查询仓库
                        List<Repository> repositoryList = repositoryServer.findRepositoryList(new RepositoryQuery()
                                .setGroupId(groupId).setName(leadTo.getRepositoryName()));
                        if (!CollectionUtils.isEmpty(repositoryList)){
                            //返回结果
                            logger.info(leadTo.getRepositoryUrl()+"，导入仓库失败失败，仓库已经导入");
                            addExecResult(leadRecord,leadTo,"fail,仓库已经导入");
                            putResult(leadToList,"fail","已存在相同仓库");
                            return;
                        }
                        //创建仓库
                        Repository repository = new Repository();
                        repository.setName(leadTo.getRepositoryName().trim());
                        repository.setAddress(leadTo.getRepositoryUrl());
                        RepositoryGroup repositoryGroup = new RepositoryGroup();
                        repositoryGroup.setGroupId(groupId);
                        repository.setGroup(repositoryGroup);
                        repository.setUser(user);
                        repository.setRules(leadTo.getRule());
                         rpyId = repositoryServer.createRpy(repository);
                        repository.setRpyId(rpyId);

                        //导入认证
                        LeadAuth importAuth = authService.findLeadAuth(leadToQuery.getImportAuthId());
                        //创建导入记录
                        leadRecord.setRepository(repository);
                        leadRecord.setLeadWay(importAuth.getType());
                        String leadRecordId = leadRecordService.createLeadRecord(leadRecord);
                        leadRecord.setId(leadRecordId);

                        //获取用户信息
                        String userAccount = getThreeUserInfo(importAuth);


                        String repositoryAddress = yamlDataMaService.repositoryAddress()+"/"+rpyId+".git";
                        //从第三方复制裸仓库
                        GitUntil.copyRepository(repositoryAddress,leadTo.getHttpRepositoryUrl(),importAuth,userAccount );

                        //添加返回结果
                        logger.info(leadTo.getRepositoryUrl()+"success，导入成功");
                        addExecResult(leadRecord,leadTo,"success，导入成功");

                        //更新导入记录
                        leadRecordService.updateLeadRecord(leadRecord);

                        //修改仓库大小
                        String address = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(), rpyId);
                        File file = new File(address);
                        long logBytes = FileUtils.sizeOfDirectory(file);
                        repository.setSize(logBytes);
                        repositoryServer.updateRepository(repository);

                    } catch (Exception e) {
                        //添加返回结果
                        logger.info(leadTo.getRepositoryUrl()+"，导入仓库失败："+e.getMessage());
                        addExecResult(leadRecord,leadTo,"fail,导入仓库失败："+e.getMessage());
                        putResult(leadToList,"fail","导入仓库失败:"+e.getMessage());
                        repositoryServer.deleteRpy(rpyId);
                        leadRecordService.updateLeadRecord(leadRecord);

                        throw new SystemException(e);
                    }
                }
                putResult(leadToList,"success",null);
            }});
        return "OK";
    }

    @Override
    public LeadToResult findToLeadResult(String key) {
        LeadToResult leadToResult = toLeadResult.get(key);
        return leadToResult;
    }


    /**
     *  getThreeUserInfo 获取第三方用户信息
     * @param importAuth importAuth
     */
    public String getThreeUserInfo(LeadAuth importAuth){
        if (("gitee").equals(importAuth.getType())){
            String userPath = GitPukFinal.GITEE_USER_URL + "?access_token=" + importAuth.getAccessToken();
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<JSONObject> forEntity = restTemplate.getForEntity(userPath, JSONObject.class);

            JSONObject body = forEntity.getBody();
            String account = String.valueOf(body.get("login"));
            return account;
        }
        if (("priBitbucket").equals(importAuth.getType())){
            String authAddress = importAuth.getAddress();
            if (authAddress.endsWith("/")){
                authAddress=StringUtils.substringBeforeLast(authAddress,"/");
            }
            String address = authAddress + "/rest/api/1.0/users";
            ResponseEntity<JSONObject> restTemplate = getRestTemplate(address, importAuth.getAccessToken());
            JSONObject body = restTemplate.getBody();
            System.out.println("");
        }
        return null;
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


    /**
     *  getRestTemplate 添加head头信息 通过RestTemplate 查询
     * @param path 查询路径
     * @param authorize 认证信息
     */
    public ResponseEntity<JSONObject>  getRestTemplate(String path,String authorize){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer " + authorize);
        // 请求
        HttpEntity<String> request = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8));

        ResponseEntity<JSONObject> exchange = restTemplate.exchange(path, HttpMethod.GET, request, JSONObject.class);

        return exchange;
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
     * @param  msg 消息
     */
    public void putResult(List<LeadTo> leadToList,String grossResult,String msg){
        LeadToResult leadToResult = new LeadToResult();
        leadToResult.setLeadToList(leadToList);
        leadToResult.setGrossResult(grossResult);
        leadToResult.setMsg(msg);
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
