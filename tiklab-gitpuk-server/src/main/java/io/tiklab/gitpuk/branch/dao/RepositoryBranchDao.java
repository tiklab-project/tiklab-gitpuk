package io.tiklab.gitpuk.branch.dao;

import io.tiklab.core.page.Pagination;
import io.tiklab.dal.jpa.JpaTemplate;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.condition.QueryCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.tiklab.gitpuk.branch.entity.RepositoryBranchEntity;
import io.tiklab.gitpuk.branch.model.RepositoryBranchQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RepositoryBranchDao-仓库分支
 */
@Repository
public class RepositoryBranchDao {

    private static Logger logger = LoggerFactory.getLogger(RepositoryBranchDao.class);

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建
     * @param repositoryBranchEntity
     * @return
     */
    public String createRepositoryBranch(RepositoryBranchEntity repositoryBranchEntity) {
        return jpaTemplate.save(repositoryBranchEntity,String.class);
    }

    /**
     * 更新
     * @param repositoryBranchEntity
     */
    public void updateRepositoryBranch(RepositoryBranchEntity repositoryBranchEntity){
        jpaTemplate.update(repositoryBranchEntity);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteRepositoryBranch(String id){
        jpaTemplate.delete(RepositoryBranchEntity.class,id);
    }

    /**
     * 条件删除仓库分支
     * @param deleteCondition
     */
    public void deleteRepositoryBranch(DeleteCondition deleteCondition){
        jpaTemplate.delete(deleteCondition);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public RepositoryBranchEntity findRepositoryBranch(String id){
        return jpaTemplate.findOne(RepositoryBranchEntity.class,id);
    }

    /**
    * 查询所有仓库分支
    * @return
    */
    public List<RepositoryBranchEntity> findAllRepositoryBranch() {
        return jpaTemplate.findAll(RepositoryBranchEntity.class);
    }

    /**
     * 通过ids查询仓库分支
     * @param idList
     * @return List <RepositoryBranchEntity>
     */
    public List<RepositoryBranchEntity> findRepositoryBranchList(List<String> idList) {
        return jpaTemplate.findList(RepositoryBranchEntity.class,idList);
    }

    /**
     * 条件查询仓库分支
     * @param repositoryBranchQuery
     * @return List <RepositoryBranchEntity>
     */
    public List<RepositoryBranchEntity> findRepositoryBranchList(RepositoryBranchQuery repositoryBranchQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(RepositoryBranchEntity.class)
                .eq("repositoryId", repositoryBranchQuery.getRepositoryId())
                .eq("createUser",repositoryBranchQuery.getCreateUser())
                .orders(repositoryBranchQuery.getOrderParams())
                .get();
        return jpaTemplate.findList(queryCondition,RepositoryBranchEntity.class);
    }

    /**
     * 条件分页查询仓库分支
     * @param repositoryBranchQuery
     * @return Pagination <RepositoryBranchEntity>
     */
    public Pagination<RepositoryBranchEntity> findRepositoryBranchPage(RepositoryBranchQuery repositoryBranchQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(RepositoryBranchEntity.class)
                .eq("repositoryId", repositoryBranchQuery.getRepositoryId())
                .eq("createUser",repositoryBranchQuery.getCreateUser())
                .orders(repositoryBranchQuery.getOrderParams())
                .pagination(repositoryBranchQuery.getPageParam())
                .get();


        return jpaTemplate.findPage(queryCondition,RepositoryBranchEntity.class);
    }

}