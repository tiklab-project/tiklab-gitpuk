package io.thoughtware.gittok.repository.dao;

import io.thoughtware.gittok.repository.model.LeadRecordQuery;
import io.thoughtware.dal.jpa.JpaTemplate;
import io.thoughtware.dal.jpa.criterial.condition.DeleteCondition;
import io.thoughtware.dal.jpa.criterial.condition.QueryCondition;
import io.thoughtware.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.thoughtware.gittok.repository.entity.LeadRecordEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * LeadRecordDao-导入外部仓库记录
 */
@Repository
public class LeadRecordDao {

    private static Logger logger = LoggerFactory.getLogger(LeadRecordDao.class);

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建
     * @param leadRecordEntity
     * @return
     */
    public String createLeadRecord(LeadRecordEntity leadRecordEntity) {
        return jpaTemplate.save(leadRecordEntity,String.class);
    }

    /**
     * 更新
     * @param leadRecordEntity
     */
    public void updateLeadRecord(LeadRecordEntity leadRecordEntity){
        jpaTemplate.update(leadRecordEntity);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteLeadRecord(String id){
        jpaTemplate.delete(LeadRecordEntity.class,id);
    }

    public void deleteLeadRecord(DeleteCondition deleteCondition){
        jpaTemplate.delete(deleteCondition);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public LeadRecordEntity findLeadRecord(String id){
        return jpaTemplate.findOne(LeadRecordEntity.class,id);
    }

    /**
    * findAllLeadRecord
    * @return
    */
    public List<LeadRecordEntity> findAllLeadRecord() {
        return jpaTemplate.findAll(LeadRecordEntity.class);
    }

    /**
     * 通过ids查询
     * @param idList
     * @return
     */
    public List<LeadRecordEntity> findLeadRecordList(List<String> idList) {
        return jpaTemplate.findList(LeadRecordEntity.class,idList);
    }

    /**
     * 条件查询插件
     * @param leadRecordQuery
     * @return
     */
    public List<LeadRecordEntity> findLeadRecordList(LeadRecordQuery leadRecordQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(LeadRecordEntity.class)
                .eq("rpyId",leadRecordQuery.getRpyId())
                .get();
        return jpaTemplate.findList(queryCondition, LeadRecordEntity.class);
    }


}