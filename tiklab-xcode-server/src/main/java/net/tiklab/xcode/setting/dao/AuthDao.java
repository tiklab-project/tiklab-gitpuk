package net.tiklab.xcode.setting.dao;

import net.tiklab.dal.jpa.JpaTemplate;
import net.tiklab.xcode.setting.entity.AuthEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthDao {

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建认证
     * @param authEntity 认证信息
     * @return 认证id
     */
    public String createAuth(AuthEntity authEntity){
        return jpaTemplate.save(authEntity, String.class);
    }

    /**
     * 删除认证
     * @param AuthId 认证id
     */
    public void deleteAuth(String AuthId){
        jpaTemplate.delete(AuthEntity.class,AuthId);
    }


    /**
     * 更新认证
     * @param authEntity 认证信息
     */
    public void updateAuth(AuthEntity authEntity){
        jpaTemplate.update(authEntity);
    }

    /**
     * 查询单个认证信息
     * @param AuthId 认证id
     * @return 认证信息
     */
    public AuthEntity findOneAuth(String AuthId){
        return jpaTemplate.findOne(AuthEntity.class,AuthId);
    }

    /**
     * 查询所有认证
     * @return 认证列表
     */
    public List<AuthEntity> findAllAuth(){
        return jpaTemplate.findAll(AuthEntity.class);
    }


    public List<AuthEntity> findAllAuthList(List<String> idList){
        return jpaTemplate.findList(AuthEntity.class,idList);
    }
    
    
    
    
}














































