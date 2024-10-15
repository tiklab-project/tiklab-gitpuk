package io.tiklab.gitpuk.repository.dao;

import io.tiklab.dal.jpa.JpaTemplate;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.condition.QueryCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.tiklab.gitpuk.repository.entity.RepositoryLfsEntity;
import io.tiklab.gitpuk.repository.model.RepositoryLfsQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RepositoryLfsDao-仓库lfs文件
 */
@Repository
public class RepositoryLfsDao {

    private static Logger logger = LoggerFactory.getLogger(RepositoryLfsDao.class);

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建
     * @param repositoryLfsEntity
     * @return
     */
    public String createRepositoryLfs(RepositoryLfsEntity repositoryLfsEntity) {
        return jpaTemplate.save(repositoryLfsEntity,String.class);
    }

    /**
     * 更新
     * @param repositoryLfsEntity
     */
    public void updateRepositoryLfs(RepositoryLfsEntity repositoryLfsEntity){
        jpaTemplate.update(repositoryLfsEntity);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteRepositoryLfs(String id){
        jpaTemplate.delete(RepositoryLfsEntity.class,id);
    }

    public void deleteRepositoryLfs(DeleteCondition deleteCondition){
        jpaTemplate.delete(deleteCondition);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public RepositoryLfsEntity findRepositoryLfs(String id){
        return jpaTemplate.findOne(RepositoryLfsEntity.class,id);
    }

    /**
    * findAllRepositoryLfs
    * @return
    */
    public List<RepositoryLfsEntity> findAllRepositoryLfs() {
        return jpaTemplate.findAll(RepositoryLfsEntity.class);
    }

    /**
     * 通过ids查询
     * @param idList
     * @return
     */
    public List<RepositoryLfsEntity> findRepositoryLfsList(List<String> idList) {
        return jpaTemplate.findList(RepositoryLfsEntity.class,idList);
    }

    /**
     * 条件查询插件
     * @param repositoryLfsQuery
     * @return
     */
    public List<RepositoryLfsEntity> findRepositoryLfsList(RepositoryLfsQuery repositoryLfsQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(RepositoryLfsEntity.class)
                .eq("repositoryId", repositoryLfsQuery.getRepositoryId())
                .eq("isDelete", repositoryLfsQuery.getIsDelete())
                .eq("oid",repositoryLfsQuery.getOid())
                .get();
        return jpaTemplate.findList(queryCondition, RepositoryLfsEntity.class);
    }


}