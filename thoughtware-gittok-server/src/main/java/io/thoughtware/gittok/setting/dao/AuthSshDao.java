package io.thoughtware.gittok.setting.dao;

import io.thoughtware.dal.jpa.JpaTemplate;
import io.thoughtware.dal.jpa.criterial.condition.QueryCondition;
import io.thoughtware.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.thoughtware.gittok.setting.entity.AuthSshEntity;
import io.thoughtware.gittok.setting.model.AuthSshQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthSshDao {

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建认证
     * @param authSshEntity 认证信息
     * @return 认证id
     */
    public String createAuthSsh(AuthSshEntity authSshEntity){
        return jpaTemplate.save(authSshEntity, String.class);
    }

    /**
     * 删除认证
     * @param AuthSshId 认证id
     */
    public void deleteAuthSsh(String AuthSshId){
        jpaTemplate.delete(AuthSshEntity.class,AuthSshId);
    }


    /**
     * 更新认证
     * @param authSshEntity 认证信息
     */
    public void updateAuthSsh(AuthSshEntity authSshEntity){
        jpaTemplate.update(authSshEntity);
    }

    /**
     * 查询单个认证信息
     * @param AuthSshId 认证id
     * @return 认证信息
     */
    public AuthSshEntity findOneAuthSsh(String AuthSshId){
        return jpaTemplate.findOne(AuthSshEntity.class,AuthSshId);
    }

    /**
     * 查询所有认证
     * @return 认证列表
     */
    public List<AuthSshEntity> findAllAuthSsh(){
        return jpaTemplate.findAll(AuthSshEntity.class);
    }


    public List<AuthSshEntity> findAllAuthSshList(List<String> idList){
        return jpaTemplate.findList(AuthSshEntity.class,idList);
    }

    public List<AuthSshEntity> findAuthSshList(AuthSshQuery authSshQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(AuthSshEntity.class)
                .eq("userId", authSshQuery.getUserId())
                .eq("type", authSshQuery.getType())
                .eq("rpyId",authSshQuery.getRpyId())
                .orders(authSshQuery.getOrderParams())
                .get();
        return jpaTemplate.findList(queryCondition, AuthSshEntity.class);
    }
}














































