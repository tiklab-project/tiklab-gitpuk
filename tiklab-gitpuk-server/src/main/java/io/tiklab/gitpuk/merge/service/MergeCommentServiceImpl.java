package io.tiklab.gitpuk.merge.service;

import io.tiklab.core.page.Pagination;
import io.tiklab.core.page.PaginationBuilder;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.DeleteBuilders;
import io.tiklab.gitpuk.merge.model.MergeComment;
import io.tiklab.gitpuk.merge.model.MergeCommentQuery;
import io.tiklab.gitpuk.merge.model.MergeCondition;
import io.tiklab.gitpuk.common.GitPukFinal;
import io.tiklab.gitpuk.merge.dao.MergeCommentDao;
import io.tiklab.gitpuk.merge.entity.MergeCommentEntity;
import io.tiklab.toolkit.beans.BeanMapper;
import io.tiklab.toolkit.join.JoinTemplate;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

/**
* MergeCommentServiceImpl-合并请求动态评论
*/
@Service
public class MergeCommentServiceImpl implements MergeCommentService {

    @Autowired
    MergeCommentDao mergeCommentDao;

    @Autowired
    JoinTemplate joinTemplate;

    @Autowired
    MergeConditionService mergeConditionService;




    @Override
    @Transactional
    public String createMergeComment(@NotNull @Valid MergeComment mergeComment) {
        MergeCommentEntity mergeCommentEntity = BeanMapper.map(mergeComment, MergeCommentEntity.class);
        mergeCommentEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));

        //在动态里面创建新的评论 需要先创建一条动态
        if (("condition").equals(mergeComment.getCreateType())){
            MergeCondition mergeCondition = new MergeCondition();
            mergeCondition.setType(GitPukFinal.MERGE_COMMENT);
            mergeCondition.setData(GitPukFinal.MERGE_COMMENT_DESC);
            mergeCondition.setMergeRequestId(mergeComment.getMergeRequestId());
            mergeCondition.setRepositoryId(mergeComment.getRepositoryId());
            mergeCondition.setUser(mergeComment.getCommentUser());
            String conditionId = mergeConditionService.createMergeCondition(mergeCondition);
            mergeCommentEntity.setMergeConditionId(conditionId);
        }

        return mergeCommentDao.createMergeComment(mergeCommentEntity);
    }

    @Override
    public void updateMergeComment(@NotNull @Valid MergeComment mergeComment) {
        MergeCommentEntity mergeCommentEntity = BeanMapper.map(mergeComment, MergeCommentEntity.class);
        mergeCommentDao.updateMergeComment(mergeCommentEntity);
    }

    @Override
    public void deleteMergeComment(@NotNull String id) {
        MergeComment mergeComment = findOne(id);

        //删除动态里面的最后一条评论也删除该动态
        if (ObjectUtils.isNotEmpty(mergeComment)){
            List<MergeCommentEntity> mergeCommentList = mergeCommentDao.findMergeCommentList(new MergeCommentQuery().setMergeConditionId(mergeComment.getMergeConditionId()));
            if (mergeCommentList.size()==1){
                mergeConditionService.deleteMergeCondition(mergeComment.getMergeConditionId());
            }
        }
        mergeCommentDao.deleteMergeComment(id);
    }

    @Override
    public void deleteMergeCommentByCondition(String key,String value) {
        DeleteCondition deleteCondition = DeleteBuilders.createDelete(MergeCommentEntity.class)
                .eq(key, value)
                .get();
        mergeCommentDao.deleteMergeComment(deleteCondition);
    }

    @Override
    public MergeComment findOne(String id) {
        MergeCommentEntity mergeCommentEntity = mergeCommentDao.findMergeComment(id);

        MergeComment mergeComment = BeanMapper.map(mergeCommentEntity, MergeComment.class);
        return mergeComment;
    }

    @Override
    public List<MergeComment> findList(List<String> idList) {
        List<MergeCommentEntity> mergeCommentEntityList =  mergeCommentDao.findMergeCommentList(idList);

        List<MergeComment> mergeCommentList =  BeanMapper.mapList(mergeCommentEntityList,MergeComment.class);
        return mergeCommentList;
    }

    @Override
    public MergeComment findMergeComment(@NotNull String id) {
        MergeComment mergeComment = findOne(id);

        joinTemplate.joinQuery(mergeComment);

        return mergeComment;
    }

    @Override
    public List<MergeComment> findAllMergeComment() {
        List<MergeCommentEntity> mergeCommentEntityList =  mergeCommentDao.findAllMergeComment();

        List<MergeComment> mergeCommentList =  BeanMapper.mapList(mergeCommentEntityList,MergeComment.class);

        joinTemplate.joinQuery(mergeCommentList);

        return mergeCommentList;
    }

    @Override
    public List<MergeComment> findMergeCommentList(MergeCommentQuery mergeCommentQuery) {
        List<MergeCommentEntity> mergeCommentEntityList = mergeCommentDao.findMergeCommentList(mergeCommentQuery);

        List<MergeComment> mergeCommentList = BeanMapper.mapList(mergeCommentEntityList,MergeComment.class);

        joinTemplate.joinQuery(mergeCommentList ,new String[]{"commentUser","replyUser"});

        return mergeCommentList;
    }

    @Override
    public Pagination<MergeComment> findMergeCommentPage(MergeCommentQuery mergeCommentQuery) {

        Pagination<MergeCommentEntity>  pagination = mergeCommentDao.findMergeCommentPage(mergeCommentQuery);

        List<MergeComment> mergeCommentList = BeanMapper.mapList(pagination.getDataList(),MergeComment.class);

        joinTemplate.joinQuery(mergeCommentList);

        return PaginationBuilder.build(pagination,mergeCommentList);
    }
}