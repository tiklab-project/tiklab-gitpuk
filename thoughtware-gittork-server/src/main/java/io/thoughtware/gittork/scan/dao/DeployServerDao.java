package io.thoughtware.gittork.scan.dao;

import io.thoughtware.gittork.scan.model.DeployServerQuery;
import io.thoughtware.core.page.Pagination;
import io.thoughtware.dal.jpa.JpaTemplate;
import io.thoughtware.dal.jpa.criterial.condition.DeleteCondition;
import io.thoughtware.dal.jpa.criterial.condition.QueryCondition;
import io.thoughtware.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.thoughtware.gittork.scan.entity.DeployServerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DeployServerDao-插件数据访问
 */
@Repository
public class DeployServerDao {

    private static Logger logger = LoggerFactory.getLogger(DeployServerDao.class);

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建
     * @param deployServerEntity
     * @return
     */
    public String createDeployServer(DeployServerEntity deployServerEntity) {
        return jpaTemplate.save(deployServerEntity,String.class);
    }

    /**
     * 更新
     * @param deployServerEntity
     */
    public void updateDeployServer(DeployServerEntity deployServerEntity){
        jpaTemplate.update(deployServerEntity);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteDeployServer(String id){
        jpaTemplate.delete(DeployServerEntity.class,id);
    }

    public void deleteDeployServer(DeleteCondition deleteCondition){
        jpaTemplate.delete(deleteCondition);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public DeployServerEntity findDeployServer(String id){
        return jpaTemplate.findOne(DeployServerEntity.class,id);
    }

    /**
    * findAllDeployServer
    * @return
    */
    public List<DeployServerEntity> findAllDeployServer() {
        return jpaTemplate.findAll(DeployServerEntity.class);
    }

    /**
     * 通过ids查询
     * @param idList
     * @return
     */
    public List<DeployServerEntity> findDeployServerList(List<String> idList) {
        return jpaTemplate.findList(DeployServerEntity.class,idList);
    }

    /**
     * 条件查询插件
     * @param deployServerQuery
     * @return
     */
    public List<DeployServerEntity> findDeployServerList(DeployServerQuery deployServerQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(DeployServerEntity.class)
                .eq("serverName", deployServerQuery.getServerName())
                .get();
        return jpaTemplate.findList(queryCondition, DeployServerEntity.class);
    }

    /**
     * 条件分页查询插件
     * @param deployServerQuery
     * @return
     */
    public Pagination<DeployServerEntity> findDeployServerPage(DeployServerQuery deployServerQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(DeployServerEntity.class)
                .eq("serverName", deployServerQuery.getServerName())
                .get();
        return jpaTemplate.findPage(queryCondition, DeployServerEntity.class);
    }

}