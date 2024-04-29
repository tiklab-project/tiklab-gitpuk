package io.thoughtware.gittok.commit.service;

import io.thoughtware.core.page.Pagination;
import io.thoughtware.core.page.PaginationBuilder;
import io.thoughtware.dal.jpa.criterial.condition.DeleteCondition;
import io.thoughtware.dal.jpa.criterial.conditionbuilder.DeleteBuilders;
import io.thoughtware.gittok.commit.dao.MergeCommitDao;
import io.thoughtware.gittok.commit.entity.MergeCommitEntity;
import io.thoughtware.gittok.commit.model.MergeCommit;
import io.thoughtware.gittok.commit.model.MergeCommitQuery;
import io.thoughtware.gittok.commit.model.MergeCondition;
import io.thoughtware.gittok.common.GitTokFinal;
import io.thoughtware.toolkit.beans.BeanMapper;
import io.thoughtware.toolkit.join.JoinTemplate;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

/**
* MergeCommitServiceImpl-合并请求的分支差异提交
*/
@Service
public class MergeCommitServiceImpl implements MergeCommitService {

    @Autowired
    MergeCommitDao mergeCommitDao;

    @Autowired
    JoinTemplate joinTemplate;

    @Autowired
     MergeConditionService mergeConditionService;




    @Override
    @Transactional
    public String createMergeCommit(@NotNull @Valid  MergeCommit mergeCommit) {
        MergeCommitEntity mergeCommitEntity = BeanMapper.map(mergeCommit, MergeCommitEntity.class);
        mergeCommitEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return mergeCommitDao.createMergeCommit(mergeCommitEntity);
    }

    @Override
    public void updateMergeCommit(@NotNull @Valid MergeCommit mergeCommit) {
        MergeCommitEntity mergeCommitEntity = BeanMapper.map(mergeCommit, MergeCommitEntity.class);
        mergeCommitDao.updateMergeCommit(mergeCommitEntity);
    }

    @Override
    public void deleteMergeCommit(@NotNull String id) {

        mergeCommitDao.deleteMergeCommit(id);
    }

    @Override
    public void deleteMergeCommitByCondition(String key,String value) {
        DeleteCondition deleteCondition = DeleteBuilders.createDelete(MergeCommitEntity.class)
                .eq(key, value)
                .get();
        mergeCommitDao.deleteMergeCommit(deleteCondition);
    }

    @Override
    public MergeCommit findOne(String id) {
        MergeCommitEntity mergeCommitEntity = mergeCommitDao.findMergeCommit(id);

        MergeCommit mergeCommit = BeanMapper.map(mergeCommitEntity, MergeCommit.class);
        return mergeCommit;
    }

    @Override
    public List<MergeCommit> findList(List<String> idList) {
        List<MergeCommitEntity> mergeCommitEntityList =  mergeCommitDao.findMergeCommitList(idList);

        List<MergeCommit> mergeCommitList =  BeanMapper.mapList(mergeCommitEntityList,MergeCommit.class);
        return mergeCommitList;
    }

    @Override
    public MergeCommit findMergeCommit(@NotNull String id) {
        MergeCommit mergeCommit = findOne(id);

        joinTemplate.joinQuery(mergeCommit);

        return mergeCommit;
    }

    @Override
    public List<MergeCommit> findAllMergeCommit() {
        List<MergeCommitEntity> mergeCommitEntityList =  mergeCommitDao.findAllMergeCommit();

        List<MergeCommit> mergeCommitList =  BeanMapper.mapList(mergeCommitEntityList,MergeCommit.class);

        joinTemplate.joinQuery(mergeCommitList);

        return mergeCommitList;
    }

    @Override
    public List<MergeCommit> findMergeCommitList(MergeCommitQuery mergeCommitQuery) {
        List<MergeCommitEntity> mergeCommitEntityList = mergeCommitDao.findMergeCommitList(mergeCommitQuery);

        List<MergeCommit> mergeCommitList = BeanMapper.mapList(mergeCommitEntityList,MergeCommit.class);

        joinTemplate.joinQuery(mergeCommitList);

        return mergeCommitList;
    }

    @Override
    public Pagination<MergeCommit> findMergeCommitPage(MergeCommitQuery mergeCommitQuery) {

        Pagination<MergeCommitEntity>  pagination = mergeCommitDao.findMergeCommitPage(mergeCommitQuery);

        List<MergeCommit> mergeCommitList = BeanMapper.mapList(pagination.getDataList(),MergeCommit.class);

        joinTemplate.joinQuery(mergeCommitList);

        return PaginationBuilder.build(pagination,mergeCommitList);
    }
}