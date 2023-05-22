package io.tiklab.xcode.detection.dao;

import io.tiklab.core.page.Pagination;
import io.tiklab.dal.jpa.JpaTemplate;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.condition.QueryCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.tiklab.xcode.detection.entity.ScmAddressEntity;
import io.tiklab.xcode.detection.model.ScmAddressQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ScmAddressDao-插件数据访问
 */
@Repository
public class ScmAddressDao {

    private static Logger logger = LoggerFactory.getLogger(ScmAddressDao.class);

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建
     * @param scmAddressEntity
     * @return
     */
    public String createScmAddress(ScmAddressEntity scmAddressEntity) {
        return jpaTemplate.save(scmAddressEntity,String.class);
    }

    /**
     * 更新
     * @param scmAddressEntity
     */
    public void updateScmAddress(ScmAddressEntity scmAddressEntity){
        jpaTemplate.update(scmAddressEntity);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteScmAddress(String id){
        jpaTemplate.delete(ScmAddressEntity.class,id);
    }

    public void deleteScmAddress(DeleteCondition deleteCondition){
        jpaTemplate.delete(deleteCondition);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public ScmAddressEntity findScmAddress(String id){
        return jpaTemplate.findOne(ScmAddressEntity.class,id);
    }

    /**
    * findAllScmAddress
    * @return
    */
    public List<ScmAddressEntity> findAllScmAddress() {
        return jpaTemplate.findAll(ScmAddressEntity.class);
    }

    /**
     * 通过ids查询
     * @param idList
     * @return
     */
    public List<ScmAddressEntity> findScmAddressList(List<String> idList) {
        return jpaTemplate.findList(ScmAddressEntity.class,idList);
    }

    /**
     * 条件查询插件
     * @param scmAddressQuery
     * @return
     */
    public List<ScmAddressEntity> findScmAddressList(ScmAddressQuery scmAddressQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(ScmAddressEntity.class)
                .eq("scmName", scmAddressQuery.getScmName())
                .eq("scmType", scmAddressQuery.getScmType())
                .get();
        return jpaTemplate.findList(queryCondition, ScmAddressEntity.class);
    }

    /**
     * 条件分页查询插件
     * @param scmAddressQuery
     * @return
     */
    public Pagination<ScmAddressEntity> findScmAddressPage(ScmAddressQuery scmAddressQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(ScmAddressEntity.class)
                .eq("scmName", scmAddressQuery.getScmName())
                .eq("scmType", scmAddressQuery.getScmType())
                .get();
        return jpaTemplate.findPage(queryCondition, ScmAddressEntity.class);
    }

}