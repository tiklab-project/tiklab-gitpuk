package io.tiklab.gitpuk.repository.service;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.HostKey;
import io.tiklab.core.exception.ApplicationException;
import io.tiklab.core.page.Pagination;
import io.tiklab.core.page.PaginationBuilder;
import io.tiklab.gitpuk.branch.model.Branch;
import io.tiklab.gitpuk.branch.service.BranchServer;
import io.tiklab.gitpuk.merge.model.MergeRequest;
import io.tiklab.gitpuk.merge.service.MergeRequestService;
import io.tiklab.gitpuk.repository.dao.RepWebHookDao;
import io.tiklab.gitpuk.repository.entity.RepWebHookEntity;
import io.tiklab.gitpuk.repository.model.*;
import io.tiklab.rpc.annotation.Exporter;
import io.tiklab.toolkit.beans.BeanMapper;
import io.tiklab.toolkit.join.JoinTemplate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.SyncFailedException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
* RepWebHookServiceImpl 文件管理
*/
@Service
@Exporter
public class RepWebHookServiceImpl implements RepWebHookService {
    private static Logger logger = LoggerFactory.getLogger(RepWebHookServiceImpl.class);

    @Autowired
    RepWebHookDao repRepWebHookDao;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    MergeRequestService mergeRequestService;

    @Autowired
    JoinTemplate joinTemplate;


    @Override
    public String createRepWebHook(@NotNull @Valid RepWebHook openRecord) {

        RepWebHookEntity openRecordEntity = BeanMapper.map(openRecord, RepWebHookEntity.class);
        openRecordEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        openRecordEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        String openRecordId= repRepWebHookDao.createRepWebHook(openRecordEntity);
        return openRecordId;
    }

    @Override
    public void updateRepWebHook(@NotNull @Valid RepWebHook openRecord) {
        RepWebHookEntity openRecordEntity = BeanMapper.map(openRecord, RepWebHookEntity.class);

        repRepWebHookDao.updateRepWebHook(openRecordEntity);
    }

    @Override
    public void deleteRepWebHook(@NotNull String id) {
        repRepWebHookDao.deleteRepWebHook(id);
    }



    @Override
    public RepWebHook findOne(String id) {
        RepWebHookEntity openRecordEntity = repRepWebHookDao.findRepWebHook(id);

        RepWebHook openRecord = BeanMapper.map(openRecordEntity, RepWebHook.class);
        return openRecord;
    }

    @Override
    public List<RepWebHook> findList(List<String> idList) {
        List<RepWebHookEntity> openRecordEntityList =  repRepWebHookDao.findRepWebHookList(idList);

        List<RepWebHook> openRecordList =  BeanMapper.mapList(openRecordEntityList, RepWebHook.class);
        return openRecordList;
    }

    @Override
    public RepWebHook findRepWebHook(@NotNull String id) {
        RepWebHook openRecord = findOne(id);

        joinTemplate.joinQuery(openRecord);

        return openRecord;
    }

    @Override
    public List<RepWebHook> findAllRepWebHook() {
        List<RepWebHookEntity> openRecordEntityList =  repRepWebHookDao.findAllRepWebHook();

        List<RepWebHook> openRecordList =  BeanMapper.mapList(openRecordEntityList, RepWebHook.class);

        joinTemplate.joinQuery(openRecordList);


        return openRecordList;
    }

    @Override
    public List<RepWebHook> findRepWebHookList(RepWebHookQuery RepWebHookQuery) {
        List<RepWebHookEntity> openRecordEntityList = repRepWebHookDao.findRepWebHookList(RepWebHookQuery);

        List<RepWebHook> openRecordList = BeanMapper.mapList(openRecordEntityList, RepWebHook.class);

        return openRecordList;
    }

    @Override
    public Pagination<RepWebHook> findRepWebHookPag(RepWebHookQuery repRepWebHookQuery) {
        Pagination<RepWebHookEntity> repWebHookPage = repRepWebHookDao.findRepWebHookPage(repRepWebHookQuery);
        List<RepWebHook> repWebHooks = BeanMapper.mapList(repWebHookPage.getDataList(), RepWebHook.class);
        return PaginationBuilder.build(repWebHookPage,repWebHooks);
    }

    @Override
    public void execWebHook(String repositoryId,String type,String name) {
        String  event;
        if (type.endsWith("Branch")){
            event="push";
        }else if (type.endsWith("Tag")){
            event="tag";
        }else {
            event="merge";
        }
        //查询是否存在启动的对应事件的WebHook
        List<RepWebHook> repWebHookList = findRepWebHookList(new RepWebHookQuery()
                .setRepositoryId(repositoryId)
                .setEnable(1)
                .setEvent(event));
        if (CollectionUtils.isNotEmpty(repWebHookList)) {
            Thread thread = new Thread() {
                public void run() {
                    for (RepWebHook repWebHook : repWebHookList) {
                        String hookUrl = repWebHook.getUrl();
                        try {
                            URL url = new URL(hookUrl);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                            // 设置请求方法
                            connection.setRequestMethod("POST");
                            connection.setDoOutput(true); // 允许向连接输出内容

                            // 设置请求头（可选，根据需要设置）
                            connection.setRequestProperty("Content-Type", "application/json");
                            connection.setRequestProperty("X-Secret-Token", repWebHook.getSecretToken());
                            connection.setRequestProperty("X-Event", event);

                            //拼接webHook数据
                            WebHookData webHookData = conWebHookData(repositoryId, event, name);
                            webHookData.setHookType(type);
                            ObjectMapper objectMapper = new ObjectMapper();
                            String jsonString = objectMapper.writeValueAsString(webHookData);

                            // 发送请求数据
                            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                                wr.writeBytes(jsonString);
                                wr.flush();
                            }
                            // 获取响应代码
                            int responseCode = connection.getResponseCode();
                            if (responseCode!=200){
                                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                String inputLine;
                                StringBuilder response = new StringBuilder();

                                while ((inputLine = in.readLine()) != null) {
                                    response.append(inputLine);
                                }
                                in.close();
                                String string = response.toString();
                                logger.info("仓库"+webHookData.getWebHookRepository().getName()+",在执行"+type+
                                        ",推送webHook失败,原因："+string);
                            }
                        } catch (Exception e) {
                            throw new ApplicationException(e.getMessage());
                        }
                    }
                }
            };
            thread.start();
        }

    }


    /**
     * 整合WebHookData
     * @param repositoryId 仓库id
     * @param   event 事件
     * @param   name 分支名称、标签名称、合并分支的id
     */
    public WebHookData conWebHookData(String repositoryId,String event,String name ){
        WebHookData hookData = new WebHookData();

        WebHookRepository hookRepository = new WebHookRepository();
        Repository repository = repositoryService.findRepository(repositoryId);
        if (ObjectUtils.isNotEmpty(repository)){
            hookRepository.setId(repositoryId);
            hookRepository.setName(repository.getName());
            hookRepository.setDefaultBranch(repository.getDefaultBranch());
            hookRepository.setRules(repository.getRules());
            hookRepository.setHttpPath(repository.getFullPath());
            hookRepository.setSshPath(repository.getSshPath());
            hookRepository.setCreateTime(hookRepository.getCreateTime());
            hookData.setWebHookRepository(hookRepository);
        }

        if (event.equals("push")){
            hookData.setBranchName(name);
        }
        if (event.endsWith("tag")){
            hookData.setTagName(name);
        }
        if (event.endsWith("merge")){
            MergeRequest mergeRequest = mergeRequestService.findMergeRequest(name);
            WebHookMerge hookMerge = new WebHookMerge();
            hookMerge.setId(mergeRequest.getId());
            hookMerge.setTitle(mergeRequest.getTitle());
            hookMerge.setCreateTime(mergeRequest.getCreateTime());
            hookMerge.setMergeState(mergeRequest.getMergeState());
            hookMerge.setMergeTarget(mergeRequest.getMergeTarget());
            hookMerge.setMergeOrigin(mergeRequest.getMergeOrigin());
            hookData.setWebHookMerge(hookMerge);
        }
        hookData.setHookName(event);
        return hookData;
    }
}