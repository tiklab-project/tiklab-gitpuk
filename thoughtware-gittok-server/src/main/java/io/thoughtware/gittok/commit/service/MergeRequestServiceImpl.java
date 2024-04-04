package io.thoughtware.gittok.commit.service;

import io.thoughtware.core.page.Pagination;
import io.thoughtware.core.page.PaginationBuilder;
import io.thoughtware.dal.jpa.criterial.condition.DeleteCondition;
import io.thoughtware.dal.jpa.criterial.conditionbuilder.DeleteBuilders;
import io.thoughtware.gittok.commit.dao.MergeRequestDao;
import io.thoughtware.gittok.commit.entity.MergeRequestEntity;
import io.thoughtware.gittok.commit.model.MergeData;
import io.thoughtware.gittok.commit.model.MergeRequest;
import io.thoughtware.gittok.commit.model.MergeRequestQuery;
import io.thoughtware.gittok.common.GitTokYamlDataMaService;
import io.thoughtware.gittok.common.RepositoryUtil;
import io.thoughtware.gittok.common.git.GitBranchUntil;
import io.thoughtware.toolkit.beans.BeanMapper;
import io.thoughtware.toolkit.join.JoinTemplate;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* MergeRequestServiceImpl-扫描漏洞
*/
@Service
public class MergeRequestServiceImpl implements MergeRequestService {

    @Autowired
    MergeRequestDao mergeRequestDao;

    @Autowired
    JoinTemplate joinTemplate;

    @Autowired
    private GitTokYamlDataMaService yamlDataMaService;


    @Override
    public String createMergeRequest(@NotNull @Valid MergeRequest mergeRequest) {
        MergeRequestEntity mergeRequestEntity = BeanMapper.map(mergeRequest, MergeRequestEntity.class);
        mergeRequestEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return mergeRequestDao.createMergeRequest(mergeRequestEntity);
    }

    @Override
    public void updateMergeRequest(@NotNull @Valid MergeRequest mergeRequest) {
        MergeRequestEntity mergeRequestEntity = BeanMapper.map(mergeRequest, MergeRequestEntity.class);
        mergeRequestDao.updateMergeRequest(mergeRequestEntity);
    }

    @Override
    public void deleteMergeRequest(@NotNull String id) {
        mergeRequestDao.deleteMergeRequest(id);
    }

    @Override
    public void deleteMergeRequestByCondition(String key,String value) {
        DeleteCondition deleteCondition = DeleteBuilders.createDelete(MergeRequestEntity.class)
                .eq(key, value)
                .get();
        mergeRequestDao.deleteMergeRequest(deleteCondition);
    }

    @Override
    public MergeRequest findOne(String id) {
        MergeRequestEntity mergeRequestEntity = mergeRequestDao.findMergeRequest(id);

        MergeRequest mergeRequest = BeanMapper.map(mergeRequestEntity, MergeRequest.class);
        return mergeRequest;
    }

    @Override
    public List<MergeRequest> findList(List<String> idList) {
        List<MergeRequestEntity> mergeRequestEntityList =  mergeRequestDao.findMergeRequestList(idList);

        List<MergeRequest> mergeRequestList =  BeanMapper.mapList(mergeRequestEntityList,MergeRequest.class);
        return mergeRequestList;
    }

    @Override
    public MergeRequest findMergeRequest(@NotNull String id) {
        MergeRequest mergeRequest = findOne(id);

        joinTemplate.joinQuery(mergeRequest);

        return mergeRequest;
    }

    @Override
    public List<MergeRequest> findAllMergeRequest() {
        List<MergeRequestEntity> mergeRequestEntityList =  mergeRequestDao.findAllMergeRequest();

        List<MergeRequest> mergeRequestList =  BeanMapper.mapList(mergeRequestEntityList,MergeRequest.class);

        joinTemplate.joinQuery(mergeRequestList);

        return mergeRequestList;
    }

    @Override
    public List<MergeRequest> findMergeRequestList(MergeRequestQuery mergeRequestQuery) {
        List<MergeRequestEntity> mergeRequestEntityList = mergeRequestDao.findMergeRequestList(mergeRequestQuery);

        List<MergeRequest> mergeRequestList = BeanMapper.mapList(mergeRequestEntityList,MergeRequest.class);

        joinTemplate.joinQuery(mergeRequestList);

        return mergeRequestList;
    }

    @Override
    public Pagination<MergeRequest> findMergeRequestPage(MergeRequestQuery mergeRequestQuery) {

        Pagination<MergeRequestEntity>  pagination = mergeRequestDao.findMergeRequestPage(mergeRequestQuery);

        List<MergeRequest> mergeRequestList = BeanMapper.mapList(pagination.getDataList(),MergeRequest.class);

        joinTemplate.joinQuery(mergeRequestList);

        return PaginationBuilder.build(pagination,mergeRequestList);
    }

    @Override
    public Map findMergeStateNum(MergeRequestQuery mergeRequestQuery) {
        Map<String, Integer> map = new HashMap<>();

        List<MergeRequestEntity> mergeRequestEntityList = mergeRequestDao.findMergeRequestList(mergeRequestQuery);
        if (CollectionUtils.isNotEmpty(mergeRequestEntityList)){
            map.put("allNum",mergeRequestEntityList.size());

            //打开的合并请求
            List<MergeRequestEntity> openMergeList = mergeRequestEntityList.stream().filter(a -> a.getMergeState() == 1).collect(Collectors.toList());
            int openNum = CollectionUtils.isNotEmpty(openMergeList) ? openMergeList.size() : 0;
            map.put("openNum",openNum);

            //已经合并数
            List<MergeRequestEntity> mergeList = mergeRequestEntityList.stream().filter(a -> a.getMergeState() == 2).collect(Collectors.toList());
            int mergeNum = CollectionUtils.isNotEmpty(mergeList) ? mergeList.size() : 0;
            map.put("mergeNum",mergeNum);

            //关闭的合并请求
            List<MergeRequestEntity> closeMergeList = mergeRequestEntityList.stream().filter(a -> a.getMergeState() == 3).collect(Collectors.toList());
            int closeNum = CollectionUtils.isNotEmpty(closeMergeList) ? closeMergeList.size() : 0;
            map.put("closeNum",closeNum);
        }
        return map;
    }

    @Override
    public String execMerge(MergeData mergeData) {
        String rpyId = mergeData.getRpyId();
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),rpyId);

        String result;
        if (("fast").equals(mergeData.getMergeWay())){
            result  = GitBranchUntil.mergeBranchByFast(mergeData, repositoryAddress);
        }else {
            result  = GitBranchUntil.mergeBranch(mergeData, repositoryAddress);
        }

        //合并成功修改合并请求的状态
        if (("ok").equals(result)){
            MergeRequest request = this.findOne(mergeData.getMergeRequestId());
            request.setMergeState(2);
            this.updateMergeRequest(request);
        }
        return result;
    }




}