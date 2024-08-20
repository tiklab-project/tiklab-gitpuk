package io.thoughtware.gittok.commit.service;

import io.thoughtware.core.page.Pagination;
import io.thoughtware.core.page.PaginationBuilder;
import io.thoughtware.dal.jpa.criterial.condition.DeleteCondition;
import io.thoughtware.dal.jpa.criterial.conditionbuilder.DeleteBuilders;
import io.thoughtware.gittok.commit.dao.MergeConditionDao;
import io.thoughtware.gittok.commit.entity.MergeConditionEntity;
import io.thoughtware.gittok.commit.model.*;

import io.thoughtware.toolkit.beans.BeanMapper;
import io.thoughtware.toolkit.join.JoinTemplate;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* MergeConditionServiceImpl-合并请求动态
*/
@Service
public class MergeConditionServiceImpl implements MergeConditionService {

    @Autowired
    MergeConditionDao mergeConditionDao;

    @Autowired
    JoinTemplate joinTemplate;

    @Autowired
    private MergeCommentService  mergeCommentService;


    @Override
    public String createMergeCondition(@NotNull @Valid MergeCondition mergeCondition) {
        MergeConditionEntity mergeConditionEntity = BeanMapper.map(mergeCondition, MergeConditionEntity.class);
        mergeConditionEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return mergeConditionDao.createMergeCondition(mergeConditionEntity);
    }

    @Override
    public void updateMergeCondition(@NotNull @Valid MergeCondition mergeCondition) {
        MergeConditionEntity mergeConditionEntity = BeanMapper.map(mergeCondition, MergeConditionEntity.class);
        mergeConditionDao.updateMergeCondition(mergeConditionEntity);
    }

    @Override
    public void deleteMergeCondition(@NotNull String id) {
        mergeConditionDao.deleteMergeCondition(id);
    }

    @Override
    public void deleteMergeConditionByCondition(String key,String value) {
        DeleteCondition deleteCondition = DeleteBuilders.createDelete(MergeConditionEntity.class)
                .eq(key, value)
                .get();
        mergeConditionDao.deleteMergeCondition(deleteCondition);
    }

    @Override
    public MergeCondition findOne(String id) {
        MergeConditionEntity mergeConditionEntity = mergeConditionDao.findMergeCondition(id);

        MergeCondition mergeCondition = BeanMapper.map(mergeConditionEntity, MergeCondition.class);
        return mergeCondition;
    }

    @Override
    public List<MergeCondition> findList(List<String> idList) {
        List<MergeConditionEntity> mergeConditionEntityList =  mergeConditionDao.findMergeConditionList(idList);

        List<MergeCondition> mergeConditionList =  BeanMapper.mapList(mergeConditionEntityList,MergeCondition.class);
        return mergeConditionList;
    }

    @Override
    public MergeCondition findMergeCondition(@NotNull String id) {
        MergeCondition mergeCondition = findOne(id);

        joinTemplate.joinQuery(mergeCondition);

        return mergeCondition;
    }

    @Override
    public List<MergeCondition> findAllMergeCondition() {
        List<MergeConditionEntity> mergeConditionEntityList =  mergeConditionDao.findAllMergeCondition();

        List<MergeCondition> mergeConditionList =  BeanMapper.mapList(mergeConditionEntityList,MergeCondition.class);

        joinTemplate.joinQuery(mergeConditionList);

        return mergeConditionList;
    }

    @Override
    public List<MergeCondition> findMergeConditionList(MergeConditionQuery mergeConditionQuery) {
        List<MergeConditionEntity> mergeConditionEntityList = mergeConditionDao.findMergeConditionList(mergeConditionQuery);

        List<MergeCondition> mergeConditionList = BeanMapper.mapList(mergeConditionEntityList,MergeCondition.class);

        joinTemplate.joinQuery(mergeConditionList);

        List<MergeCondition> collect = mergeConditionList.stream().filter(a -> ("comment").equals(a.getType())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)&&!("exec").equals(mergeConditionQuery.getFindType())){
            //通过合并请求查询评论
            List<MergeComment> mergeCommentList = mergeCommentService.findMergeCommentList(new MergeCommentQuery().setMergeRequestId(mergeConditionQuery.getMergeRequestId()));
            if (CollectionUtils.isNotEmpty(mergeCommentList)){
                for (MergeCondition mergeCondition:collect){
                    List<MergeComment> commentList = mergeCommentList.stream().filter(b -> mergeCondition.getId().equals(b.getMergeConditionId())).collect(Collectors.toList());
                    mergeCondition.setMergeCommentList(commentList);
                }
            }
        }
        //查询执行的动态
        if (("exec").equals(mergeConditionQuery.getFindType())){
             mergeConditionList = mergeConditionList.stream().filter(a -> !("comment").equals(a.getType())).collect(Collectors.toList());
        }

        List<MergeCondition> mergeConditions = mergeConditionList.stream().sorted(Comparator.comparing(MergeCondition::getCreateTime)).collect(Collectors.toList());

        return mergeConditions;
    }

    @Override
    public Pagination<MergeCondition> findMergeConditionPage(MergeConditionQuery mergeConditionQuery) {

        Pagination<MergeConditionEntity>  pagination = mergeConditionDao.findMergeConditionPage(mergeConditionQuery);

        List<MergeCondition> mergeConditionList = BeanMapper.mapList(pagination.getDataList(),MergeCondition.class);

        joinTemplate.joinQuery(mergeConditionList);

        return PaginationBuilder.build(pagination,mergeConditionList);
    }
}