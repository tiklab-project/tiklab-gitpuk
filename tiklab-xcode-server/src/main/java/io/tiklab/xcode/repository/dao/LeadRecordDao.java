package io.tiklab.xcode.repository.dao;

import io.tiklab.dal.jpa.JpaTemplate;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.condition.QueryCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.tiklab.xcode.repository.entity.LeadAuthEntity;
import io.tiklab.xcode.repository.model.LeadAuthQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ImportAuthDao-导入外部仓库校验
 */
@Repository
public class LeadAuthDao {

    private static Logger logger = LoggerFactory.getLogger(LeadAuthDao.class);

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建
     * @param importAuthEntity
     * @return
     */
    public String createImportAuth(LeadAuthEntity importAuthEntity) {
        return jpaTemplate.save(importAuthEntity,String.class);
    }

    /**
     * 更新
     * @param importAuthEntity
     */
    public void updateImportAuth(LeadAuthEntity importAuthEntity){
        jpaTemplate.update(importAuthEntity);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteImportAuth(String id){
        jpaTemplate.delete(LeadAuthEntity.class,id);
    }

    public void deleteImportAuth(DeleteCondition deleteCondition){
        jpaTemplate.delete(deleteCondition);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public LeadAuthEntity findImportAuth(String id){
        return jpaTemplate.findOne(LeadAuthEntity.class,id);
    }

    /**
    * findAllImportAuth
    * @return
    */
    public List<LeadAuthEntity> findAllImportAuth() {
        return jpaTemplate.findAll(LeadAuthEntity.class);
    }

    /**
     * 通过ids查询
     * @param idList
     * @return
     */
    public List<LeadAuthEntity> findImportAuthList(List<String> idList) {
        return jpaTemplate.findList(LeadAuthEntity.class,idList);
    }

    /**
     * 条件查询插件
     * @param importAuthQuery
     * @return
     */
    public List<LeadAuthEntity> findImportAuthList(LeadAuthQuery importAuthQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(LeadAuthEntity.class)
                .eq("type", importAuthQuery.getType())
                .eq("userId",importAuthQuery.getUserId())
                .get();
        return jpaTemplate.findList(queryCondition, LeadAuthEntity.class);
    }


}