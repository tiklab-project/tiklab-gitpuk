package io.thoughtware.gittok.scan.dao;

import io.thoughtware.gittok.scan.entity.CodeScanInstanceEntity;
import io.thoughtware.gittok.scan.model.CodeScanInstanceQuery;
import io.thoughtware.core.page.Pagination;
import io.thoughtware.dal.jpa.JpaTemplate;
import io.thoughtware.dal.jpa.criterial.condition.DeleteCondition;
import io.thoughtware.dal.jpa.criterial.condition.QueryCondition;
import io.thoughtware.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CodeScanInstanceDao-插件数据访问
 */
@Repository
public class CodeScanInstanceDao {

    private static Logger logger = LoggerFactory.getLogger(CodeScanInstanceDao.class);

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建
     * @param codeScanInstanceEntity
     * @return
     */
    public String createCodeScanInstance(CodeScanInstanceEntity codeScanInstanceEntity) {
        return jpaTemplate.save(codeScanInstanceEntity,String.class);
    }

    /**
     * 更新
     * @param codeScanInstanceEntity
     */
    public void updateCodeScanInstance(CodeScanInstanceEntity codeScanInstanceEntity){
        jpaTemplate.update(codeScanInstanceEntity);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteCodeScanInstance(String id){
        jpaTemplate.delete(CodeScanInstanceEntity.class,id);
    }

    public void deleteCodeScanInstance(DeleteCondition deleteCondition){
        jpaTemplate.delete(deleteCondition);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public CodeScanInstanceEntity findCodeScanInstance(String id){
        return jpaTemplate.findOne(CodeScanInstanceEntity.class,id);
    }

    /**
    * findAllCodeScanInstance
    * @return
    */
    public List<CodeScanInstanceEntity> findAllCodeScanInstance() {
        return jpaTemplate.findAll(CodeScanInstanceEntity.class);
    }

    /**
     * 通过ids查询
     * @param idList
     * @return
     */
    public List<CodeScanInstanceEntity> findCodeScanInstanceList(List<String> idList) {
        return jpaTemplate.findList(CodeScanInstanceEntity.class,idList);
    }

    /**
     * 条件查询插件
     * @param codeScanInstanceQuery
     * @return
     */
    public List<CodeScanInstanceEntity> findCodeScanInstanceList(CodeScanInstanceQuery codeScanInstanceQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(CodeScanInstanceEntity.class)
                .eq("repositoryId", codeScanInstanceQuery.getRepositoryId())
                .get();
        return jpaTemplate.findList(queryCondition, CodeScanInstanceEntity.class);
    }

    /**
     * 条件分页查询插件
     * @param codeScanInstanceQuery
     * @return
     */
    public Pagination<CodeScanInstanceEntity> findCodeScanInstancePage(CodeScanInstanceQuery codeScanInstanceQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(CodeScanInstanceEntity.class)
                .eq("repositoryId", codeScanInstanceQuery.getRepositoryId())
                .get();
        return jpaTemplate.findPage(queryCondition, CodeScanInstanceEntity.class);
    }

}