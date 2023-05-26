package io.tiklab.xcode.detection.dao;

import io.tiklab.core.page.Pagination;
import io.tiklab.dal.jpa.JpaTemplate;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.condition.QueryCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.tiklab.xcode.detection.entity.CodeScanEntity;
import io.tiklab.xcode.detection.model.CodeScanQuery;
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
     * @return
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
     * @return
     */
    public Pagination<CodeScanEntity> findCodeScanPage(CodeScanQuery codeScanQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(CodeScanEntity.class)
                .eq("repositoryId", codeScanQuery.getRepositoryId())
                .get();
        return jpaTemplate.findPage(queryCondition, CodeScanEntity.class);
    }

}