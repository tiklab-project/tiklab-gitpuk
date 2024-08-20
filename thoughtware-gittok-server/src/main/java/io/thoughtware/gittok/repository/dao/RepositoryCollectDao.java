package io.thoughtware.gittok.repository.dao;

import io.thoughtware.core.page.Pagination;
import io.thoughtware.dal.jpa.JpaTemplate;
import io.thoughtware.dal.jpa.criterial.condition.DeleteCondition;
import io.thoughtware.dal.jpa.criterial.condition.QueryCondition;
import io.thoughtware.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.thoughtware.gittok.repository.entity.RepositoryCollectEntity;
import io.thoughtware.gittok.repository.model.RepositoryCollectQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RepositoryCollectDao-收藏仓库数据访问
 */
@Repository
public class RepositoryCollectDao {

    private static Logger logger = LoggerFactory.getLogger(RepositoryCollectDao.class);

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建
     * @param repositoryCollectEntity
     * @return
     */
    public String createRepositoryCollect(RepositoryCollectEntity repositoryCollectEntity) {
        return jpaTemplate.save(repositoryCollectEntity,String.class);
    }

    /**
     * 更新
     * @param repositoryCollectEntity
     */
    public void updateRepositoryCollect(RepositoryCollectEntity repositoryCollectEntity){
        jpaTemplate.update(repositoryCollectEntity);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteRepositoryCollect(String id){
        jpaTemplate.delete(RepositoryCollectEntity.class,id);
    }

    public void deleteRepositoryCollect(DeleteCondition deleteCondition){
        jpaTemplate.delete(deleteCondition);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public RepositoryCollectEntity findRepositoryCollect(String id){
        return jpaTemplate.findOne(RepositoryCollectEntity.class,id);
    }

    /**
    * findAllRepositoryCollect
    * @return
    */
    public List<RepositoryCollectEntity> findAllRepositoryCollect() {
        return jpaTemplate.findAll(RepositoryCollectEntity.class);
    }

    /**
     * 通过ids查询
     * @param idList
     * @return
     */
    public List<RepositoryCollectEntity> findRepositoryCollectList(List<String> idList) {
        return jpaTemplate.findList(RepositoryCollectEntity.class,idList);
    }

    /**
     * 条件查询收藏仓库
     * @param repositoryCollectQuery
     * @return
     */
    public List<RepositoryCollectEntity> findRepositoryCollectList(RepositoryCollectQuery repositoryCollectQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(RepositoryCollectEntity.class)
                .eq("userId", repositoryCollectQuery.getUserId())
                .eq("repositoryId", repositoryCollectQuery.getRepositoryId())
                .get();
        return jpaTemplate.findList(queryCondition, RepositoryCollectEntity.class);
    }

    /**
     * 条件分页查询收藏仓库
     * @param repositoryCollectQuery
     * @return
     */
    public Pagination<RepositoryCollectEntity> findRepositoryCollectPage(RepositoryCollectQuery repositoryCollectQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(RepositoryCollectEntity.class)
                .eq("userId", repositoryCollectQuery.getUserId())
                .eq("repositoryId", repositoryCollectQuery.getRepositoryId())
                .get();
        return jpaTemplate.findPage(queryCondition, RepositoryCollectEntity.class);
    }


    /**
     * 通过repositoryIds查询
     * @param repositoryIds
     * @return
     */
    public List<RepositoryCollectEntity> findRepositoryCollectList(String[] repositoryIds,String userId) {
        QueryCondition queryCondition = QueryBuilders.createQuery(RepositoryCollectEntity.class)
                .in("repositoryId", repositoryIds)
                .eq("userId", userId)
                .get();
        return jpaTemplate.findList(queryCondition, RepositoryCollectEntity.class);
    }


}