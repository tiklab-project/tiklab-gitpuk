package io.tiklab.xcode.detection.dao;

import io.tiklab.core.page.Pagination;
import io.tiklab.dal.jpa.JpaTemplate;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.condition.QueryCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.tiklab.xcode.detection.entity.AuthThirdEntity;
import io.tiklab.xcode.detection.model.AuthThirdQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * AuthThirdDao-插件数据访问
 */
@Repository
public class AuthThirdDao {

    private static Logger logger = LoggerFactory.getLogger(AuthThirdDao.class);

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建
     * @param authThirdEntity
     * @return
     */
    public String createAuthThird(AuthThirdEntity authThirdEntity) {
        return jpaTemplate.save(authThirdEntity,String.class);
    }

    /**
     * 更新
     * @param authThirdEntity
     */
    public void updateAuthThird(AuthThirdEntity authThirdEntity){
        jpaTemplate.update(authThirdEntity);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteAuthThird(String id){
        jpaTemplate.delete(AuthThirdEntity.class,id);
    }

    public void deleteAuthThird(DeleteCondition deleteCondition){
        jpaTemplate.delete(deleteCondition);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public AuthThirdEntity findAuthThird(String id){
        return jpaTemplate.findOne(AuthThirdEntity.class,id);
    }

    /**
    * findAllAuthThird
    * @return
    */
    public List<AuthThirdEntity> findAllAuthThird() {
        return jpaTemplate.findAll(AuthThirdEntity.class);
    }

    /**
     * 通过ids查询
     * @param idList
     * @return
     */
    public List<AuthThirdEntity> findAuthThirdList(List<String> idList) {
        return jpaTemplate.findList(AuthThirdEntity.class,idList);
    }

    /**
     * 条件查询插件
     * @param authThirdQuery
     * @return
     */
    public List<AuthThirdEntity> findAuthThirdList(AuthThirdQuery authThirdQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(AuthThirdEntity.class)
                .eq("authName", authThirdQuery.getAuthName())
                .get();
        return jpaTemplate.findList(queryCondition, AuthThirdEntity.class);
    }

    /**
     * 条件分页查询插件
     * @param authThirdQuery
     * @return
     */
    public Pagination<AuthThirdEntity> findAuthThirdPage(AuthThirdQuery authThirdQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(AuthThirdEntity.class)
                .eq("authName", authThirdQuery.getAuthName())
                .get();
        return jpaTemplate.findPage(queryCondition, AuthThirdEntity.class);
    }

}