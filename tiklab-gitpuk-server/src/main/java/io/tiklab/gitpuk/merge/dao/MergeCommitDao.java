package io.tiklab.gitpuk.merge.dao;

import io.tiklab.core.page.Pagination;
import io.tiklab.dal.jpa.JpaTemplate;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.condition.QueryCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.tiklab.gitpuk.merge.entity.MergeCommitEntity;
import io.tiklab.gitpuk.merge.model.MergeCommitQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MergeCommitDao-合并请求的分支差异提交
 */
@Repository
public class MergeCommitDao {

    private static Logger logger = LoggerFactory.getLogger(MergeCommitDao.class);

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建
     * @param mergeCommitEntity
     * @return
     */
    public String createMergeCommit(MergeCommitEntity mergeCommitEntity) {
        return jpaTemplate.save(mergeCommitEntity,String.class);
    }

    /**
     * 更新
     * @param mergeCommitEntity
     */
    public void updateMergeCommit(MergeCommitEntity mergeCommitEntity){
        jpaTemplate.update(mergeCommitEntity);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteMergeCommit(String id){
        jpaTemplate.delete(MergeCommitEntity.class,id);
    }

    /**
     * 条件删除合并请求的分支差异提交
     * @param deleteCondition
     */
    public void deleteMergeCommit(DeleteCondition deleteCondition){
        jpaTemplate.delete(deleteCondition);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public MergeCommitEntity findMergeCommit(String id){
        return jpaTemplate.findOne(MergeCommitEntity.class,id);
    }

    /**
    * 查询所有合并请求的分支差异提交
    * @return
    */
    public List<MergeCommitEntity> findAllMergeCommit() {
        return jpaTemplate.findAll(MergeCommitEntity.class);
    }

    /**
     * 通过ids查询合并请求的分支差异提交
     * @param idList
     * @return List <MergeCommitEntity>
     */
    public List<MergeCommitEntity> findMergeCommitList(List<String> idList) {
        return jpaTemplate.findList(MergeCommitEntity.class,idList);
    }

    /**
     * 条件查询合并请求的分支差异提交
     * @param mergeCommitQuery
     * @return List <MergeCommitEntity>
     */
    public List<MergeCommitEntity> findMergeCommitList(MergeCommitQuery mergeCommitQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(MergeCommitEntity.class)
                .eq("repositoryId", mergeCommitQuery.getRepositoryId())
                .eq("mergeRequestId",mergeCommitQuery.getMergeRequestId())
                .orders(mergeCommitQuery.getOrderParams())
                .get();
        return jpaTemplate.findList(queryCondition,MergeCommitEntity.class);
    }

    /**
     * 条件分页查询合并请求的分支差异提交
     * @param mergeCommitQuery
     * @return Pagination <MergeCommitEntity>
     */
    public Pagination<MergeCommitEntity> findMergeCommitPage(MergeCommitQuery mergeCommitQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(MergeCommitEntity.class)
                .eq("repositoryId", mergeCommitQuery.getRepositoryId())
                .eq("mergeRequestId",mergeCommitQuery.getMergeRequestId())
                .orders(mergeCommitQuery.getOrderParams())
                .pagination(mergeCommitQuery.getPageParam())
                .get();


        return jpaTemplate.findPage(queryCondition,MergeCommitEntity.class);
    }

}