package io.tiklab.xcode.repository.dao;

import io.tiklab.dal.jpa.JpaTemplate;
import io.tiklab.xcode.repository.entity.RepositoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositoryDao {

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建仓库
     * @param repositoryEntity 仓库信息
     * @return 仓库id
     */
    public String createRpy(RepositoryEntity repositoryEntity){
        return jpaTemplate.save(repositoryEntity, String.class);
    }

    /**
     * 删除仓库
     * @param rpyId 仓库id
     */
    public void deleteRpy(String rpyId){
        jpaTemplate.delete(RepositoryEntity.class,rpyId);
    }


    /**
     * 更新仓库
     * @param repositoryEntity 仓库信息
     */
    public void updateRpy(RepositoryEntity repositoryEntity){
        jpaTemplate.update(repositoryEntity);
    }

    /**
     * 查询单个仓库信息
     * @param rpyId 仓库id
     * @return 仓库信息
     */
    public RepositoryEntity findOneRpy(String rpyId){
        return jpaTemplate.findOne(RepositoryEntity.class,rpyId);
    }

    /**
     * 查询所有仓库
     * @return 仓库列表
     */
    public List<RepositoryEntity> findAllRpy(){
        return jpaTemplate.findAll(RepositoryEntity.class);
    }


    public List<RepositoryEntity> findAllRpyList(List<String> idList){
        return jpaTemplate.findList(RepositoryEntity.class,idList);
    }



}




























































