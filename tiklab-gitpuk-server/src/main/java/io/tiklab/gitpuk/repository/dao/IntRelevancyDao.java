package io.tiklab.gitpuk.repository.dao;

import io.tiklab.dal.jpa.JpaTemplate;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.condition.QueryCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.tiklab.gitpuk.repository.entity.IntRelevancyEntity;
import io.tiklab.gitpuk.repository.model.IntRelevancyQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class IntRelevancyDao {

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建集成关联
     * @param intRelevancyEntity 集成关联信息
     * @return 集成关联id
     */
    public String createIntRelevancy(IntRelevancyEntity intRelevancyEntity){
        return jpaTemplate.save(intRelevancyEntity, String.class);
    }

    /**
     * 删除集成关联
     * @param IntRelevancyId 集成关联id
     */
    public void deleteIntRelevancy(String IntRelevancyId){
        jpaTemplate.delete(IntRelevancyEntity.class,IntRelevancyId);
    }

    public void deleteIntRelevancy(DeleteCondition deleteCondition){
        jpaTemplate.delete(deleteCondition);
    }

    /**
     * 更新集成关联
     * @param intRelevancyEntity 集成关联信息
     */
    public void updateIntRelevancy(IntRelevancyEntity intRelevancyEntity){
        jpaTemplate.update(intRelevancyEntity);
    }

    /**
     * 查询单个集成关联信息
     * @param IntRelevancyId 集成关联id
     * @return 集成关联信息
     */
    public IntRelevancyEntity findOneIntRelevancy(String IntRelevancyId){
        return jpaTemplate.findOne(IntRelevancyEntity.class,IntRelevancyId);
    }

    /**
     * 查询所有集成关联
     * @return 集成关联列表
     */
    public List<IntRelevancyEntity> findAllIntRelevancy(){
        return jpaTemplate.findAll(IntRelevancyEntity.class);
    }


    public List<IntRelevancyEntity> findAllIntRelevancyList(List<String> idList){
        return jpaTemplate.findList(IntRelevancyEntity.class,idList);
    }

    public List<IntRelevancyEntity> findIntRelevancyList(IntRelevancyQuery intRelevancyQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(IntRelevancyEntity.class)
                .eq("repositoryId", intRelevancyQuery.getRepositoryId())
                .eq("relevancyId", intRelevancyQuery.getRelevancyId())
                .orders(intRelevancyQuery.getOrderParams())
                .get();
        return jpaTemplate.findList(queryCondition, IntRelevancyEntity.class);
    }
}














































