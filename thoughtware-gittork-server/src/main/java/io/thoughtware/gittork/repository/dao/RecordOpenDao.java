package io.thoughtware.gittork.repository.dao;

import io.thoughtware.gittork.repository.model.RecordOpenQuery;
import io.thoughtware.core.page.Pagination;
import io.thoughtware.dal.jpa.JpaTemplate;
import io.thoughtware.dal.jpa.criterial.condition.DeleteCondition;
import io.thoughtware.dal.jpa.criterial.condition.QueryCondition;
import io.thoughtware.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.thoughtware.gittork.repository.entity.RecordOpenEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RecordOpenDao-插件数据访问
 */
@Repository
public class RecordOpenDao {

    private static Logger logger = LoggerFactory.getLogger(RecordOpenDao.class);

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建
     * @param recordOpenEntity
     * @return
     */
    public String createRecordOpen(RecordOpenEntity recordOpenEntity) {
        return jpaTemplate.save(recordOpenEntity,String.class);
    }

    /**
     * 更新
     * @param recordOpenEntity
     */
    public void updateRecordOpen(RecordOpenEntity recordOpenEntity){
        jpaTemplate.update(recordOpenEntity);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteRecordOpen(String id){
        jpaTemplate.delete(RecordOpenEntity.class,id);
    }

    public void deleteRecordOpen(DeleteCondition deleteCondition){
        jpaTemplate.delete(deleteCondition);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public RecordOpenEntity findRecordOpen(String id){
        return jpaTemplate.findOne(RecordOpenEntity.class,id);
    }

    /**
    * findAllRecordOpen
    * @return
    */
    public List<RecordOpenEntity> findAllRecordOpen() {
        return jpaTemplate.findAll(RecordOpenEntity.class);
    }

    /**
     * 通过ids查询
     * @param idList
     * @return
     */
    public List<RecordOpenEntity> findRecordOpenList(List<String> idList) {
        return jpaTemplate.findList(RecordOpenEntity.class,idList);
    }

    /**
     * 条件查询插件
     * @param recordOpenQuery
     * @return
     */
    public List<RecordOpenEntity> findRecordOpenList(RecordOpenQuery recordOpenQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(RecordOpenEntity.class)
                .eq("userId", recordOpenQuery.getUserId())
                .eq("repositoryId", recordOpenQuery.getRepositoryId())
                .get();
        return jpaTemplate.findList(queryCondition, RecordOpenEntity.class);
    }

    /**
     * 条件分页查询插件
     * @param recordOpenQuery
     * @return
     */
    public Pagination<RecordOpenEntity> findRecordOpenPage(RecordOpenQuery recordOpenQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(RecordOpenEntity.class)
                .eq("userId", recordOpenQuery.getUserId())
                .eq("repositoryId", recordOpenQuery.getRepositoryId())
                .get();
        return jpaTemplate.findPage(queryCondition, RecordOpenEntity.class);
    }

}