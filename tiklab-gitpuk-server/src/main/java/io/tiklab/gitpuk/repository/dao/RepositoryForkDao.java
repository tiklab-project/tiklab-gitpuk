package io.tiklab.gitpuk.repository.dao;

import io.tiklab.core.page.Pagination;
import io.tiklab.dal.jpa.JpaTemplate;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.condition.QueryCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.tiklab.gitpuk.repository.entity.RecordCommitEntity;
import io.tiklab.gitpuk.repository.entity.RepositoryEntity;
import io.tiklab.gitpuk.repository.entity.RepositoryForkEntity;
import io.tiklab.gitpuk.repository.model.RecordCommitQuery;
import io.tiklab.gitpuk.repository.model.RepositoryForkQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RepositoryForkDao-仓库fork
 */
@Repository
public class RepositoryForkDao {

    private static Logger logger = LoggerFactory.getLogger(RepositoryForkDao.class);

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建
     * @param repositoryForkEntity
     * @return
     */
    public String createRepositoryFork(RepositoryForkEntity repositoryForkEntity) {
        return jpaTemplate.save(repositoryForkEntity,String.class);
    }

    /**
     * 更新
     * @param repositoryForkEntity
     */
    public void updateRepositoryFork(RepositoryForkEntity repositoryForkEntity){
        jpaTemplate.update(repositoryForkEntity);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteRepositoryFork(String id){
        jpaTemplate.delete(RepositoryForkEntity.class,id);
    }

    public void deleteRepositoryFork(DeleteCondition deleteCondition){
        jpaTemplate.delete(deleteCondition);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public RepositoryForkEntity findRepositoryFork(String id){
        return jpaTemplate.findOne(RepositoryForkEntity.class,id);
    }

    /**
    * findAllRepositoryFork
    * @return
    */
    public List<RepositoryForkEntity> findAllRepositoryFork() {
        return jpaTemplate.findAll(RepositoryForkEntity.class);
    }

    /**
     * 通过ids查询
     * @param idList
     * @return
     */
    public List<RepositoryForkEntity> findRepositoryForkList(List<String> idList) {
        return jpaTemplate.findList(RepositoryForkEntity.class,idList);
    }

    /**
     * 条件查询插件
     * @param repositoryForkQuery
     * @return
     */
    public List<RepositoryForkEntity> findRepositoryForkList(RepositoryForkQuery repositoryForkQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(RepositoryForkEntity.class)
                .eq("repositoryId", repositoryForkQuery.getRepositoryId())
                .get();
        return jpaTemplate.findList(queryCondition, RepositoryForkEntity.class);
    }
    /**
     * 条件分页查询插件
     * @param repositoryForkQuery
     * @return
     */
    public Pagination<RepositoryForkEntity> findRepositoryForkPage(RepositoryForkQuery repositoryForkQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(RepositoryForkEntity.class,"fork")
                .leftJoin(RepositoryEntity.class,"rep","rep.rpyId=fork.forkRepositoryId")
                .like("rep.name",repositoryForkQuery.getRepositoryName())
                .eq("rep.rules",repositoryForkQuery.getRules())
                .eq("fork.repositoryId", repositoryForkQuery.getRepositoryId())
                .pagination(repositoryForkQuery.getPageParam())
                .get();
        return jpaTemplate.findPage(queryCondition, RepositoryForkEntity.class);
    }


}