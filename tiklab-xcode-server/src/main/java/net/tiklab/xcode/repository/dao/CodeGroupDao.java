package net.tiklab.xcode.repository.dao;

import net.tiklab.dal.jpa.JpaTemplate;
import net.tiklab.xcode.repository.entity.CodeGroupEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CodeGroupDao {

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建仓库组
     * @param codeGroupEntity 仓库组信息
     * @return 仓库组id
     */
    public String createCodeGroup(CodeGroupEntity codeGroupEntity){
        return jpaTemplate.save(codeGroupEntity, String.class);
    }

    /**
     * 删除仓库组
     * @param codeGroupId 仓库组id
     */
    public void deleteCodeGroup(String codeGroupId){
        jpaTemplate.delete(CodeGroupEntity.class,codeGroupId);
    }


    /**
     * 更新仓库组
     * @param codeGroupEntity 仓库组信息
     */
    public void updateCodeGroup(CodeGroupEntity codeGroupEntity){
        jpaTemplate.update(codeGroupEntity);
    }

    /**
     * 查询单个仓库组信息
     * @param codeGroupId 仓库组id
     * @return 仓库组信息
     */
    public CodeGroupEntity findOneCodeGroup(String codeGroupId){
        return jpaTemplate.findOne(CodeGroupEntity.class,codeGroupId);
    }

    /**
     * 查询所有仓库组
     * @return 仓库组列表
     */
    public List<CodeGroupEntity> findAllCodeGroup(){
        return jpaTemplate.findAll(CodeGroupEntity.class);
    }


    public List<CodeGroupEntity> findAllCodeGroupList(List<String> idList){
        return jpaTemplate.findList(CodeGroupEntity.class,idList);
    }
}
