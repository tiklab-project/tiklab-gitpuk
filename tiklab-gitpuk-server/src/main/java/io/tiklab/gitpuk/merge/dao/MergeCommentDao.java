package io.tiklab.gitpuk.merge.dao;

import io.tiklab.core.page.Pagination;
import io.tiklab.dal.jpa.JpaTemplate;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.condition.QueryCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.tiklab.gitpuk.merge.entity.MergeCommentEntity;
import io.tiklab.gitpuk.merge.model.MergeCommentQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MergeCommentDao-合并请求动态评论评论数据库访问
 */
@Repository
public class MergeCommentDao {

    private static Logger logger = LoggerFactory.getLogger(MergeCommentDao.class);

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建
     * @param mergeCommentEntity
     * @return
     */
    public String createMergeComment(MergeCommentEntity mergeCommentEntity) {
        return jpaTemplate.save(mergeCommentEntity,String.class);
    }

    /**
     * 更新
     * @param mergeCommentEntity
     */
    public void updateMergeComment(MergeCommentEntity mergeCommentEntity){
        jpaTemplate.update(mergeCommentEntity);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteMergeComment(String id){
        jpaTemplate.delete(MergeCommentEntity.class,id);
    }

    /**
     * 条件删除合并请求动态评论
     * @param deleteCondition
     */
    public void deleteMergeComment(DeleteCondition deleteCondition){
        jpaTemplate.delete(deleteCondition);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public MergeCommentEntity findMergeComment(String id){
        return jpaTemplate.findOne(MergeCommentEntity.class,id);
    }

    /**
    * 查询所有合并请求动态评论
    * @return
    */
    public List<MergeCommentEntity> findAllMergeComment() {
        return jpaTemplate.findAll(MergeCommentEntity.class);
    }

    /**
     * 通过ids查询合并请求动态评论
     * @param idList
     * @return List <MergeCommentEntity>
     */
    public List<MergeCommentEntity> findMergeCommentList(List<String> idList) {
        return jpaTemplate.findList(MergeCommentEntity.class,idList);
    }

    /**
     * 条件查询合并请求动态评论
     * @param mergeCommentQuery
     * @return List <MergeCommentEntity>
     */
    public List<MergeCommentEntity> findMergeCommentList(MergeCommentQuery mergeCommentQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(MergeCommentEntity.class)
                .eq("repositoryId", mergeCommentQuery.getRepositoryId())
                .eq("mergeRequestId",mergeCommentQuery.getMergeRequestId())
                .eq("mergeConditionId", mergeCommentQuery.getMergeConditionId())
                .orders(mergeCommentQuery.getOrderParams())
                .get();
        return jpaTemplate.findList(queryCondition,MergeCommentEntity.class);
    }

    /**
     * 条件分页查询合并请求动态评论
     * @param mergeCommentQuery
     * @return Pagination <MergeCommentEntity>
     */
    public Pagination<MergeCommentEntity> findMergeCommentPage(MergeCommentQuery mergeCommentQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(MergeCommentEntity.class)
                .eq("repositoryId", mergeCommentQuery.getRepositoryId())
                .eq("mergeRequestId",mergeCommentQuery.getMergeRequestId())
                .eq("mergeConditionId", mergeCommentQuery.getMergeConditionId())
                .orders(mergeCommentQuery.getOrderParams())
                .pagination(mergeCommentQuery.getPageParam())
                .get();


        return jpaTemplate.findPage(queryCondition,MergeCommentEntity.class);
    }

}