package io.tiklab.gitpuk.setting.dao;

import io.tiklab.dal.jpa.JpaTemplate;
import io.tiklab.dal.jpa.criterial.condition.QueryCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.tiklab.gitpuk.setting.entity.IntegrationAddressEntity;
import io.tiklab.gitpuk.setting.model.IntegrationAddressQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class IntegrationAddressDao {

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建系统集成地址
     * @param integrationAddressEntity 系统集成地址信息
     * @return 系统集成地址id
     */
    public String createIntegrationAddress(IntegrationAddressEntity integrationAddressEntity){
        return jpaTemplate.save(integrationAddressEntity, String.class);
    }

    /**
     * 删除系统集成地址
     * @param IntegrationAddressId 系统集成地址id
     */
    public void deleteIntegrationAddress(String IntegrationAddressId){
        jpaTemplate.delete(IntegrationAddressEntity.class,IntegrationAddressId);
    }


    /**
     * 更新系统集成地址
     * @param integrationAddressEntity 系统集成地址信息
     */
    public void updateIntegrationAddress(IntegrationAddressEntity integrationAddressEntity){
        jpaTemplate.update(integrationAddressEntity);
    }

    /**
     * 查询单个系统集成地址信息
     * @param IntegrationAddressId 系统集成地址id
     * @return 系统集成地址信息
     */
    public IntegrationAddressEntity findOneIntegrationAddress(String IntegrationAddressId){
        return jpaTemplate.findOne(IntegrationAddressEntity.class,IntegrationAddressId);
    }

    /**
     * 查询所有系统集成地址
     * @return 系统集成地址列表
     */
    public List<IntegrationAddressEntity> findAllIntegrationAddress(){
        return jpaTemplate.findAll(IntegrationAddressEntity.class);
    }


    public List<IntegrationAddressEntity> findAllIntegrationAddressList(List<String> idList){
        return jpaTemplate.findList(IntegrationAddressEntity.class,idList);
    }

    public List<IntegrationAddressEntity> findIntegrationAddressList(IntegrationAddressQuery integrationAddressQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(IntegrationAddressEntity.class)
                .eq("code", integrationAddressQuery.getCode())
                .orders(integrationAddressQuery.getOrderParams())
                .get();
        return jpaTemplate.findList(queryCondition, IntegrationAddressEntity.class);
    }
}














































