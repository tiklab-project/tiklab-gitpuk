package net.tiklab.xcode.repository.dao;

import net.tiklab.dal.jpa.JpaTemplate;
import net.tiklab.xcode.repository.entity.RepositoryEntity;
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
    public String createCode(RepositoryEntity repositoryEntity){
        return jpaTemplate.save(repositoryEntity, String.class);
    }

    /**
     * 删除仓库
     * @param codeId 仓库id
     */
    public void deleteCode(String codeId){
        jpaTemplate.delete(RepositoryEntity.class,codeId);
    }


    /**
     * 更新仓库
     * @param repositoryEntity 仓库信息
     */
    public void updateCode(RepositoryEntity repositoryEntity){
        jpaTemplate.update(repositoryEntity);
    }

    /**
     * 查询单个仓库信息
     * @param codeId 仓库id
     * @return 仓库信息
     */
    public RepositoryEntity findOneCode(String codeId){
        return jpaTemplate.findOne(RepositoryEntity.class,codeId);
    }

    /**
     * 查询所有仓库
     * @return 仓库列表
     */
    public List<RepositoryEntity> findAllCode(){
        return jpaTemplate.findAll(RepositoryEntity.class);
    }


    public List<RepositoryEntity> findAllCodeList(List<String> idList){
        return jpaTemplate.findList(RepositoryEntity.class,idList);
    }



}




























































