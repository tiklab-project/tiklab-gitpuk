package net.tiklab.xcode.setting.dao;

import net.tiklab.dal.jpa.JpaTemplate;
import net.tiklab.xcode.setting.entity.CodeAuthEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CodeAuthDao {

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建认证
     * @param codeAuthEntity 认证信息
     * @return 认证id
     */
    public String createCodeAuth(CodeAuthEntity codeAuthEntity){
        return jpaTemplate.save(codeAuthEntity, String.class);
    }

    /**
     * 删除认证
     * @param codeAuthId 认证id
     */
    public void deleteCodeAuth(String codeAuthId){
        jpaTemplate.delete(CodeAuthEntity.class,codeAuthId);
    }


    /**
     * 更新认证
     * @param codeAuthEntity 认证信息
     */
    public void updateCodeAuth(CodeAuthEntity codeAuthEntity){
        jpaTemplate.update(codeAuthEntity);
    }

    /**
     * 查询单个认证信息
     * @param codeAuthId 认证id
     * @return 认证信息
     */
    public CodeAuthEntity findOneCodeAuth(String codeAuthId){
        return jpaTemplate.findOne(CodeAuthEntity.class,codeAuthId);
    }

    /**
     * 查询所有认证
     * @return 认证列表
     */
    public List<CodeAuthEntity> findAllCodeAuth(){
        return jpaTemplate.findAll(CodeAuthEntity.class);
    }


    public List<CodeAuthEntity> findAllCodeAuthList(List<String> idList){
        return jpaTemplate.findList(CodeAuthEntity.class,idList);
    }
    
    
    
    
}














































