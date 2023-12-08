package io.thoughtware.gittork.scan.dao;

import io.thoughtware.gittork.scan.model.DeployEnvQuery;
import io.thoughtware.core.page.Pagination;
import io.thoughtware.dal.jpa.JpaTemplate;
import io.thoughtware.dal.jpa.criterial.condition.DeleteCondition;
import io.thoughtware.dal.jpa.criterial.condition.QueryCondition;
import io.thoughtware.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.thoughtware.gittork.scan.entity.DeployEnvEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DeployEnvDao-插件数据访问
 */
@Repository
public class DeployEnvDao {

    private static Logger logger = LoggerFactory.getLogger(DeployEnvDao.class);

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建
     * @param deployEnvEntity
     * @return
     */
    public String createDeployEnv(DeployEnvEntity deployEnvEntity) {
        return jpaTemplate.save(deployEnvEntity,String.class);
    }

    /**
     * 更新
     * @param deployEnvEntity
     */
    public void updateDeployEnv(DeployEnvEntity deployEnvEntity){
        jpaTemplate.update(deployEnvEntity);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteDeployEnv(String id){
        jpaTemplate.delete(DeployEnvEntity.class,id);
    }

    public void deleteDeployEnv(DeleteCondition deleteCondition){
        jpaTemplate.delete(deleteCondition);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public DeployEnvEntity findDeployEnv(String id){
        return jpaTemplate.findOne(DeployEnvEntity.class,id);
    }

    /**
    * findAllDeployEnv
    * @return
    */
    public List<DeployEnvEntity> findAllDeployEnv() {
        return jpaTemplate.findAll(DeployEnvEntity.class);
    }

    /**
     * 通过ids查询
     * @param idList
     * @return
     */
    public List<DeployEnvEntity> findDeployEnvList(List<String> idList) {
        return jpaTemplate.findList(DeployEnvEntity.class,idList);
    }

    /**
     * 条件查询插件
     * @param deployEnvQuery
     * @return
     */
    public List<DeployEnvEntity> findDeployEnvList(DeployEnvQuery deployEnvQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(DeployEnvEntity.class)
                .eq("envName", deployEnvQuery.getEnvName())
                .eq("envType", deployEnvQuery.getEnvType())
                .get();
        return jpaTemplate.findList(queryCondition, DeployEnvEntity.class);
    }

    /**
     * 条件分页查询插件
     * @param deployEnvQuery
     * @return
     */
    public Pagination<DeployEnvEntity> findDeployEnvPage(DeployEnvQuery deployEnvQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(DeployEnvEntity.class)
                .eq("envName", deployEnvQuery.getEnvName())
                .eq("envType", deployEnvQuery.getEnvType())
                .get();
        return jpaTemplate.findPage(queryCondition, DeployEnvEntity.class);
    }

}