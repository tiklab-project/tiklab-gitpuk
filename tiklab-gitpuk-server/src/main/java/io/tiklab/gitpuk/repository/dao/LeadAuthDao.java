package io.tiklab.gitpuk.repository.dao;

import io.tiklab.gitpuk.repository.entity.LeadAuthEntity;
import io.tiklab.gitpuk.repository.model.LeadAuthQuery;
import io.tiklab.dal.jpa.JpaTemplate;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.condition.QueryCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * LeadAuthDao-导入外部仓库校验
 */
@Repository
public class LeadAuthDao {

    private static Logger logger = LoggerFactory.getLogger(LeadAuthDao.class);

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建
     * @param leadAuthEntity
     * @return
     */
    public String createLeadAuth(LeadAuthEntity leadAuthEntity) {
        return jpaTemplate.save(leadAuthEntity,String.class);
    }

    /**
     * 更新
     * @param leadAuthEntity
     */
    public void updateLeadAuth(LeadAuthEntity leadAuthEntity){
        jpaTemplate.update(leadAuthEntity);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteLeadAuth(String id){
        jpaTemplate.delete(LeadAuthEntity.class,id);
    }

    public void deleteLeadAuth(DeleteCondition deleteCondition){
        jpaTemplate.delete(deleteCondition);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public LeadAuthEntity findLeadAuth(String id){
        return jpaTemplate.findOne(LeadAuthEntity.class,id);
    }

    /**
    * findAllLeadAuth
    * @return
    */
    public List<LeadAuthEntity> findAllLeadAuth() {
        return jpaTemplate.findAll(LeadAuthEntity.class);
    }

    /**
     * 通过ids查询
     * @param idList
     * @return
     */
    public List<LeadAuthEntity> findLeadAuthList(List<String> idList) {
        return jpaTemplate.findList(LeadAuthEntity.class,idList);
    }

    /**
     * 条件查询插件
     * @param leadAuthQuery
     * @return
     */
    public List<LeadAuthEntity> findLeadAuthList(LeadAuthQuery leadAuthQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(LeadAuthEntity.class)
                .eq("type", leadAuthQuery.getType())
                .eq("userId",leadAuthQuery.getUserId())
                .get();
        return jpaTemplate.findList(queryCondition, LeadAuthEntity.class);
    }


}