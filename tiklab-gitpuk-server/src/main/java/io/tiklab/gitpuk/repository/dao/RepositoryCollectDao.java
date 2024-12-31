package io.tiklab.gitpuk.repository.dao;

import io.tiklab.core.page.Pagination;
import io.tiklab.dal.jpa.JpaTemplate;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.condition.QueryCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.tiklab.gitpuk.repository.entity.RepositoryCollectEntity;
import io.tiklab.gitpuk.repository.model.RepositoryCollectQuery;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
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
        QueryBuilders queryBuilders = QueryBuilders.createQuery(RepositoryCollectEntity.class)
                .eq("userId", userId);
        if (ObjectUtils.isNotEmpty(repositoryIds)){
            queryBuilders.in("repositoryId", repositoryIds);
        }
        QueryCondition queryCondition = queryBuilders.get();
        return jpaTemplate.findList(queryCondition, RepositoryCollectEntity.class);
    }


}