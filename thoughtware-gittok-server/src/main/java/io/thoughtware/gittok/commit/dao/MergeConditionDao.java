package io.thoughtware.gittok.commit.dao;

import io.thoughtware.core.page.Pagination;
import io.thoughtware.dal.jpa.JpaTemplate;
import io.thoughtware.dal.jpa.criterial.condition.DeleteCondition;
import io.thoughtware.dal.jpa.criterial.condition.QueryCondition;
import io.thoughtware.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.thoughtware.gittok.commit.entity.MergeConditionEntity;
import io.thoughtware.gittok.commit.model.MergeConditionQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MergeConditionDao-合并请求动态数据库访问
 */
@Repository
public class MergeConditionDao {

    private static Logger logger = LoggerFactory.getLogger(MergeConditionDao.class);

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建
     * @param mergeConditionEntity
     * @return
     */
    public String createMergeCondition(MergeConditionEntity mergeConditionEntity) {
        return jpaTemplate.save(mergeConditionEntity,String.class);
    }

    /**
     * 更新
     * @param mergeConditionEntity
     */
    public void updateMergeCondition(MergeConditionEntity mergeConditionEntity){
        jpaTemplate.update(mergeConditionEntity);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteMergeCondition(String id){
        jpaTemplate.delete(MergeConditionEntity.class,id);
    }

    /**
     * 条件删除合并请求动态
     * @param deleteCondition
     */
    public void deleteMergeCondition(DeleteCondition deleteCondition){
        jpaTemplate.delete(deleteCondition);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public MergeConditionEntity findMergeCondition(String id){
        return jpaTemplate.findOne(MergeConditionEntity.class,id);
    }

    /**
    * 查询所有合并请求动态
    * @return
    */
    public List<MergeConditionEntity> findAllMergeCondition() {
        return jpaTemplate.findAll(MergeConditionEntity.class);
    }

    /**
     * 通过ids查询合并请求动态
     * @param idList
     * @return List <MergeConditionEntity>
     */
    public List<MergeConditionEntity> findMergeConditionList(List<String> idList) {
        return jpaTemplate.findList(MergeConditionEntity.class,idList);
    }

    /**
     * 条件查询合并请求动态
     * @param mergeConditionQuery
     * @return List <MergeConditionEntity>
     */
    public List<MergeConditionEntity> findMergeConditionList(MergeConditionQuery mergeConditionQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(MergeConditionEntity.class)
                .eq("repositoryId", mergeConditionQuery.getRepositoryId())
                .eq("mergeRequestId",mergeConditionQuery.getMergeRequestId())
                .eq("type", mergeConditionQuery.getType())
                .orders(mergeConditionQuery.getOrderParams())
                .get();
        return jpaTemplate.findList(queryCondition,MergeConditionEntity.class);
    }

    /**
     * 条件分页查询合并请求动态
     * @param mergeConditionQuery
     * @return Pagination <MergeConditionEntity>
     */
    public Pagination<MergeConditionEntity> findMergeConditionPage(MergeConditionQuery mergeConditionQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(MergeConditionEntity.class)
                .eq("repositoryId", mergeConditionQuery.getRepositoryId())
                .eq("mergeRequestId",mergeConditionQuery.getMergeRequestId())
                .eq("type", mergeConditionQuery.getType())
                .orders(mergeConditionQuery.getOrderParams())
                .pagination(mergeConditionQuery.getPageParam())
                .get();


        return jpaTemplate.findPage(queryCondition,MergeConditionEntity.class);
    }

}