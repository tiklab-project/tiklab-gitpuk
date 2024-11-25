package io.tiklab.gitpuk.repository.dao;

import io.tiklab.core.page.Page;
import io.tiklab.core.page.Pagination;
import io.tiklab.dal.jpa.JpaTemplate;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.condition.QueryCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.tiklab.gitpuk.repository.entity.RepWebHookEntity;
import io.tiklab.gitpuk.repository.model.RepWebHookQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RepWebHookDao
 */
@Repository
public class RepWebHookDao {

    private static Logger logger = LoggerFactory.getLogger(RepWebHookDao.class);

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建
     * @param repRepWebHookEntity
     * @return
     */
    public String createRepWebHook(RepWebHookEntity repRepWebHookEntity) {
        return jpaTemplate.save(repRepWebHookEntity,String.class);
    }

    /**
     * 更新
     * @param repRepWebHookEntity
     */
    public void updateRepWebHook(RepWebHookEntity repRepWebHookEntity){
        jpaTemplate.update(repRepWebHookEntity);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteRepWebHook(String id){
        jpaTemplate.delete(RepWebHookEntity.class,id);
    }

    public void deleteRepWebHook(DeleteCondition deleteCondition){
        jpaTemplate.delete(deleteCondition);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public RepWebHookEntity findRepWebHook(String id){
        return jpaTemplate.findOne(RepWebHookEntity.class,id);
    }

    /**
    * findAllRepWebHook
    * @return
    */
    public List<RepWebHookEntity> findAllRepWebHook() {
        return jpaTemplate.findAll(RepWebHookEntity.class);
    }

    /**
     * 通过ids查询
     * @param idList
     * @return
     */
    public List<RepWebHookEntity> findRepWebHookList(List<String> idList) {
        return jpaTemplate.findList(RepWebHookEntity.class,idList);
    }

    /**
     * 条件查询RepWebHook
     * @param repRepWebHookQuery
     * @return
     */
    public List<RepWebHookEntity> findRepWebHookList(RepWebHookQuery repRepWebHookQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(RepWebHookEntity.class)
                .eq("repositoryId", repRepWebHookQuery.getRepositoryId())
                .eq("enable",repRepWebHookQuery.getEnable())
                .like("events",repRepWebHookQuery.getEvent())
                .get();
        return jpaTemplate.findList(queryCondition, RepWebHookEntity.class);
    }

    /**
     * 条件查询RepWebHook
     * @param repRepWebHookQuery
     * @return
     */
    public Pagination<RepWebHookEntity> findRepWebHookPage(RepWebHookQuery repRepWebHookQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(RepWebHookEntity.class)
                .eq("repositoryId", repRepWebHookQuery.getRepositoryId())
                .orders(repRepWebHookQuery.getOrderParams())
                .pagination(repRepWebHookQuery.getPageParam())
                .get();
        return jpaTemplate.findPage(queryCondition, RepWebHookEntity.class);
    }
}