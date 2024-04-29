package io.thoughtware.gittok.commit.dao;

import io.thoughtware.core.page.Pagination;
import io.thoughtware.dal.jpa.JpaTemplate;
import io.thoughtware.dal.jpa.criterial.condition.DeleteCondition;
import io.thoughtware.dal.jpa.criterial.condition.QueryCondition;
import io.thoughtware.dal.jpa.criterial.conditionbuilder.OrQueryBuilders;
import io.thoughtware.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.thoughtware.gittok.commit.entity.MergeAuditorEntity;
import io.thoughtware.gittok.commit.model.MergeAuditorQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MergeAuditorDao-合并请求审核人
 */
@Repository
public class MergeAuditorDao {

    private static Logger logger = LoggerFactory.getLogger(MergeAuditorDao.class);

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建
     * @param mergeAuditorEntity
     * @return
     */
    public String createMergeAuditor(MergeAuditorEntity mergeAuditorEntity) {
        return jpaTemplate.save(mergeAuditorEntity,String.class);
    }

    /**
     * 更新
     * @param mergeAuditorEntity
     */
    public void updateMergeAuditor(MergeAuditorEntity mergeAuditorEntity){
        jpaTemplate.update(mergeAuditorEntity);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteMergeAuditor(String id){
        jpaTemplate.delete(MergeAuditorEntity.class,id);
    }

    /**
     * 条件删除合并请求审核人
     * @param deleteCondition
     */
    public void deleteMergeAuditor(DeleteCondition deleteCondition){
        jpaTemplate.delete(deleteCondition);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public MergeAuditorEntity findMergeAuditor(String id){
        return jpaTemplate.findOne(MergeAuditorEntity.class,id);
    }

    /**
    * 查询所有合并请求审核人
    * @return
    */
    public List<MergeAuditorEntity> findAllMergeAuditor() {
        return jpaTemplate.findAll(MergeAuditorEntity.class);
    }

    /**
     * 通过ids查询合并请求审核人
     * @param idList
     * @return List <MergeAuditorEntity>
     */
    public List<MergeAuditorEntity> findMergeAuditorList(List<String> idList) {
        return jpaTemplate.findList(MergeAuditorEntity.class,idList);
    }

    /**
     * 条件查询合并请求审核人
     * @param mergeAuditorQuery
     * @return List <MergeAuditorEntity>
     */
    public List<MergeAuditorEntity> findMergeAuditorList(MergeAuditorQuery mergeAuditorQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(MergeAuditorEntity.class)
                .eq("repositoryId", mergeAuditorQuery.getRepositoryId())
                .eq("mergeRequestId",mergeAuditorQuery.getMergeRequestId())
                .eq("userId",mergeAuditorQuery.getUserId())
                .orders(mergeAuditorQuery.getOrderParams())
                .get();
        return jpaTemplate.findList(queryCondition,MergeAuditorEntity.class);
    }

    /**
     * 条件分页查询合并请求审核人
     * @param mergeAuditorQuery
     * @return Pagination <MergeAuditorEntity>
     */
    public Pagination<MergeAuditorEntity> findMergeAuditorPage(MergeAuditorQuery mergeAuditorQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(MergeAuditorEntity.class)
                .eq("repositoryId", mergeAuditorQuery.getRepositoryId())
                .eq("mergeRequestId",mergeAuditorQuery.getMergeRequestId())
                .eq("userId",mergeAuditorQuery.getUserId())
                .orders(mergeAuditorQuery.getOrderParams())
                .pagination(mergeAuditorQuery.getPageParam())
                .get();


        return jpaTemplate.findPage(queryCondition,MergeAuditorEntity.class);
    }

}