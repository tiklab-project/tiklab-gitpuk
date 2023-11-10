package io.tiklab.xcode.scan.dao;

import io.tiklab.core.page.Pagination;
import io.tiklab.dal.jpa.JpaTemplate;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.condition.QueryCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.tiklab.xcode.scan.entity.ScanSchemeSonarEntity;
import io.tiklab.xcode.scan.model.ScanSchemeSonarQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ScanSchemeSonarDao-扫描方案sonar关系
 */
@Repository
public class ScanSchemeSonarDao {

    private static Logger logger = LoggerFactory.getLogger(ScanSchemeSonarDao.class);

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建
     * @param scanSchemeSonarEntity
     * @return
     */
    public String createScanSchemeSonar(ScanSchemeSonarEntity scanSchemeSonarEntity) {
        return jpaTemplate.save(scanSchemeSonarEntity,String.class);
    }

    /**
     * 更新
     * @param scanSchemeSonarEntity
     */
    public void updateScanSchemeSonar(ScanSchemeSonarEntity scanSchemeSonarEntity){
        jpaTemplate.update(scanSchemeSonarEntity);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteScanSchemeSonar(String id){
        jpaTemplate.delete(ScanSchemeSonarEntity.class,id);
    }

    /**
     * 条件删除扫描方案sonar关系
     * @param deleteCondition
     */
    public void deleteScanSchemeSonar(DeleteCondition deleteCondition){
        jpaTemplate.delete(deleteCondition);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public ScanSchemeSonarEntity findScanSchemeSonar(String id){
        return jpaTemplate.findOne(ScanSchemeSonarEntity.class,id);
    }

    /**
    * 查询所有扫描方案sonar关系
    * @return
    */
    public List<ScanSchemeSonarEntity> findAllScanSchemeSonar() {
        return jpaTemplate.findAll(ScanSchemeSonarEntity.class);
    }

    /**
     * 通过ids查询扫描方案sonar关系
     * @param idList
     * @return List <ScanSchemeSonarEntity>
     */
    public List<ScanSchemeSonarEntity> findScanSchemeSonarList(List<String> idList) {
        return jpaTemplate.findList(ScanSchemeSonarEntity.class,idList);
    }

    /**
     * 条件查询扫描方案sonar关系
     * @param scanSchemeSonarQuery
     * @return List <ScanSchemeSonarEntity>
     */
    public List<ScanSchemeSonarEntity> findScanSchemeSonarList(ScanSchemeSonarQuery scanSchemeSonarQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(ScanSchemeSonarEntity.class)
                .eq("scanSchemeId",scanSchemeSonarQuery.getScanSchemeId())
                .orders(scanSchemeSonarQuery.getOrderParams())
                .get();
        return jpaTemplate.findList(queryCondition,ScanSchemeSonarEntity.class);
    }

    /**
     * 条件分页查询扫描方案sonar关系
     * @param scanSchemeSonarQuery
     * @return Pagination <ScanSchemeSonarEntity>
     */
    public Pagination<ScanSchemeSonarEntity> findScanSchemeSonarPage(ScanSchemeSonarQuery scanSchemeSonarQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(ScanSchemeSonarEntity.class)
                .eq("scanSchemeId",scanSchemeSonarQuery.getScanSchemeId())
                .orders(scanSchemeSonarQuery.getOrderParams())
                .pagination(scanSchemeSonarQuery.getPageParam())
                .get();
        return jpaTemplate.findPage(queryCondition,ScanSchemeSonarEntity.class);
    }
}