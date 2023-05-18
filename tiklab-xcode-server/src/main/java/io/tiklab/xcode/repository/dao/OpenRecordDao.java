package io.tiklab.xcode.repository.dao;

import io.tiklab.core.page.Pagination;
import io.tiklab.dal.jpa.JpaTemplate;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.condition.QueryCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.tiklab.xcode.repository.entity.OpenRecordEntity;
import io.tiklab.xcode.repository.model.OpenRecordQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * OpenRecordDao-插件数据访问
 */
@Repository
public class OpenRecordDao {

    private static Logger logger = LoggerFactory.getLogger(OpenRecordDao.class);

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建
     * @param openRecordEntity
     * @return
     */
    public String createOpenRecord(OpenRecordEntity openRecordEntity) {
        return jpaTemplate.save(openRecordEntity,String.class);
    }

    /**
     * 更新
     * @param openRecordEntity
     */
    public void updateOpenRecord(OpenRecordEntity openRecordEntity){
        jpaTemplate.update(openRecordEntity);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteOpenRecord(String id){
        jpaTemplate.delete(OpenRecordEntity.class,id);
    }

    public void deleteOpenRecord(DeleteCondition deleteCondition){
        jpaTemplate.delete(deleteCondition);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public OpenRecordEntity findOpenRecord(String id){
        return jpaTemplate.findOne(OpenRecordEntity.class,id);
    }

    /**
    * findAllOpenRecord
    * @return
    */
    public List<OpenRecordEntity> findAllOpenRecord() {
        return jpaTemplate.findAll(OpenRecordEntity.class);
    }

    /**
     * 通过ids查询
     * @param idList
     * @return
     */
    public List<OpenRecordEntity> findOpenRecordList(List<String> idList) {
        return jpaTemplate.findList(OpenRecordEntity.class,idList);
    }

    /**
     * 条件查询插件
     * @param openRecordQuery
     * @return
     */
    public List<OpenRecordEntity> findOpenRecordList(OpenRecordQuery openRecordQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(OpenRecordEntity.class)
                .eq("userId",openRecordQuery.getUserId())
                .eq("repositoryId",openRecordQuery.getRepositoryId())
                .get();
        return jpaTemplate.findList(queryCondition,OpenRecordEntity.class);
    }

    /**
     * 条件分页查询插件
     * @param openRecordQuery
     * @return
     */
    public Pagination<OpenRecordEntity> findOpenRecordPage(OpenRecordQuery openRecordQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(OpenRecordEntity.class)
                .eq("userId",openRecordQuery.getUserId())
                .eq("repositoryId",openRecordQuery.getRepositoryId())
                .get();
        return jpaTemplate.findPage(queryCondition,OpenRecordEntity.class);
    }

}