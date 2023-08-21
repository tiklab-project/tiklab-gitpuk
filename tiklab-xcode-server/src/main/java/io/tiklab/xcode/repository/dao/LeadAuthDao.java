package io.tiklab.xcode.repository.dao;

import io.tiklab.core.page.Pagination;
import io.tiklab.dal.jpa.JpaTemplate;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.condition.QueryCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.tiklab.xcode.repository.entity.ImportAuthEntity;
import io.tiklab.xcode.repository.model.ImportAuthQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ImportAuthDao-导入外部仓库校验
 */
@Repository
public class ImportAuthDao {

    private static Logger logger = LoggerFactory.getLogger(ImportAuthDao.class);

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建
     * @param importAuthEntity
     * @return
     */
    public String createImportAuth(ImportAuthEntity importAuthEntity) {
        return jpaTemplate.save(importAuthEntity,String.class);
    }

    /**
     * 更新
     * @param importAuthEntity
     */
    public void updateImportAuth(ImportAuthEntity importAuthEntity){
        jpaTemplate.update(importAuthEntity);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteImportAuth(String id){
        jpaTemplate.delete(ImportAuthEntity.class,id);
    }

    public void deleteImportAuth(DeleteCondition deleteCondition){
        jpaTemplate.delete(deleteCondition);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public ImportAuthEntity findImportAuth(String id){
        return jpaTemplate.findOne(ImportAuthEntity.class,id);
    }

    /**
    * findAllImportAuth
    * @return
    */
    public List<ImportAuthEntity> findAllImportAuth() {
        return jpaTemplate.findAll(ImportAuthEntity.class);
    }

    /**
     * 通过ids查询
     * @param idList
     * @return
     */
    public List<ImportAuthEntity> findImportAuthList(List<String> idList) {
        return jpaTemplate.findList(ImportAuthEntity.class,idList);
    }

    /**
     * 条件查询插件
     * @param importAuthQuery
     * @return
     */
    public List<ImportAuthEntity> findImportAuthList(ImportAuthQuery importAuthQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(ImportAuthEntity.class)
                .eq("type", importAuthQuery.getType())
                .eq("userId",importAuthQuery.getUserId())
                .get();
        return jpaTemplate.findList(queryCondition, ImportAuthEntity.class);
    }


}