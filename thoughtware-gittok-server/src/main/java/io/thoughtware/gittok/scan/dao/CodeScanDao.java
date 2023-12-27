package io.thoughtware.gittok.scan.dao;

import io.thoughtware.gittok.scan.model.CodeScanQuery;
import io.thoughtware.core.page.Pagination;
import io.thoughtware.dal.jpa.JpaTemplate;
import io.thoughtware.dal.jpa.criterial.condition.DeleteCondition;
import io.thoughtware.dal.jpa.criterial.condition.QueryCondition;
import io.thoughtware.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.thoughtware.gittok.scan.entity.CodeScanEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CodeScanDao-插件数据访问
 */
@Repository
public class CodeScanDao {

    private static Logger logger = LoggerFactory.getLogger(CodeScanDao.class);

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建
     * @param codeScanEntity
     * @return
     */
    public String createCodeScan(CodeScanEntity codeScanEntity) {
        return jpaTemplate.save(codeScanEntity,String.class);
    }

    /**
     * 更新
     * @param codeScanEntity
     */
    public void updateCodeScan(CodeScanEntity codeScanEntity){
        jpaTemplate.update(codeScanEntity);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteCodeScan(String id){
        jpaTemplate.delete(CodeScanEntity.class,id);
    }

    public void deleteCodeScan(DeleteCondition deleteCondition){
        jpaTemplate.delete(deleteCondition);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public CodeScanEntity findCodeScan(String id){
        return jpaTemplate.findOne(CodeScanEntity.class,id);
    }

    /**
    * findAllCodeScan
    * @return
    */
    public List<CodeScanEntity> findAllCodeScan() {
        return jpaTemplate.findAll(CodeScanEntity.class);
    }

    /**
     * 通过ids查询
     * @param idList
     * @return
     */
    public List<CodeScanEntity> findCodeScanList(List<String> idList) {
        return jpaTemplate.findList(CodeScanEntity.class,idList);
    }

    /**
     * 条件查询插件
     * @param codeScanQuery
     */
    public List<CodeScanEntity> findCodeScanList(CodeScanQuery codeScanQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(CodeScanEntity.class)
                .eq("repositoryId", codeScanQuery.getRepositoryId())
                .get();
        return jpaTemplate.findList(queryCondition, CodeScanEntity.class);
    }

    /**
     * 条件分页查询插件
     * @param codeScanQuery
     */
    public Pagination<CodeScanEntity> findCodeScanPage(CodeScanQuery codeScanQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(CodeScanEntity.class)
                .eq("repositoryId", codeScanQuery.getRepositoryId())
                .get();
        return jpaTemplate.findPage(queryCondition, CodeScanEntity.class);
    }

}