package io.thoughtware.gittok.commit.dao;

import io.thoughtware.core.page.Pagination;
import io.thoughtware.dal.jpa.JpaTemplate;
import io.thoughtware.dal.jpa.criterial.condition.DeleteCondition;
import io.thoughtware.dal.jpa.criterial.condition.QueryCondition;
import io.thoughtware.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.thoughtware.gittok.commit.entity.MergeRequestEntity;
import io.thoughtware.gittok.commit.model.MergeRequestQuery;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MergeRequestDao-扫描漏洞数据库访问
 */
@Repository
public class MergeRequestDao {

    private static Logger logger = LoggerFactory.getLogger(MergeRequestDao.class);

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建
     * @param mergeRequestEntity
     * @return
     */
    public String createMergeRequest(MergeRequestEntity mergeRequestEntity) {
        return jpaTemplate.save(mergeRequestEntity,String.class);
    }

    /**
     * 更新
     * @param mergeRequestEntity
     */
    public void updateMergeRequest(MergeRequestEntity mergeRequestEntity){
        jpaTemplate.update(mergeRequestEntity);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteMergeRequest(String id){
        jpaTemplate.delete(MergeRequestEntity.class,id);
    }

    /**
     * 条件删除漏洞
     * @param deleteCondition
     */
    public void deleteMergeRequest(DeleteCondition deleteCondition){
        jpaTemplate.delete(deleteCondition);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public MergeRequestEntity findMergeRequest(String id){
        return jpaTemplate.findOne(MergeRequestEntity.class,id);
    }

    /**
    * 查询所有漏洞
    * @return
    */
    public List<MergeRequestEntity> findAllMergeRequest() {
        return jpaTemplate.findAll(MergeRequestEntity.class);
    }

    /**
     * 通过ids查询漏洞
     * @param idList
     * @return List <MergeRequestEntity>
     */
    public List<MergeRequestEntity> findMergeRequestList(List<String> idList) {
        return jpaTemplate.findList(MergeRequestEntity.class,idList);
    }

    /**
     * 条件查询漏洞
     * @param mergeRequestQuery
     * @return List <MergeRequestEntity>
     */
    public List<MergeRequestEntity> findMergeRequestList(MergeRequestQuery mergeRequestQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(MergeRequestEntity.class)
                .eq("rpyId", mergeRequestQuery.getRpyId())
                .eq("mergeState",mergeRequestQuery.getMergeState())
                .like("title",mergeRequestQuery.getTitle())
                .orders(mergeRequestQuery.getOrderParams())
                .get();
        return jpaTemplate.findList(queryCondition,MergeRequestEntity.class);
    }

    /**
     * 条件分页查询漏洞
     * @param mergeRequestQuery
     * @return Pagination <MergeRequestEntity>
     */
    public Pagination<MergeRequestEntity> findMergeRequestPage(MergeRequestQuery mergeRequestQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(MergeRequestEntity.class)
                .eq("rpyId", mergeRequestQuery.getRpyId())
                .eq("mergeState",mergeRequestQuery.getMergeState())
                .like("title",mergeRequestQuery.getTitle())
                .orders(mergeRequestQuery.getOrderParams())
                .pagination(mergeRequestQuery.getPageParam())
                .get();


        return jpaTemplate.findPage(queryCondition,MergeRequestEntity.class);
    }

}