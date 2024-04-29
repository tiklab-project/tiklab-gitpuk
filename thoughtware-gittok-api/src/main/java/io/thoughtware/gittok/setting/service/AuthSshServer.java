package io.thoughtware.gittok.setting.service;

import io.thoughtware.gittok.setting.model.AuthSsh;
import io.thoughtware.gittok.setting.model.AuthSshQuery;
import io.thoughtware.toolkit.join.annotation.FindAll;
import io.thoughtware.toolkit.join.annotation.FindList;
import io.thoughtware.toolkit.join.annotation.FindOne;
import io.thoughtware.toolkit.join.annotation.JoinProvider;

import java.util.List;

@JoinProvider(model = AuthSsh.class)
public interface AuthSshServer {


    /**
     * 创建认证
     * @param authSsh 信息
     * @return 认证id
     */
    String createAuthSsh(AuthSsh authSsh);

    /**
     * 删除认证
     * @param AuthSshId 认证id
     */
    void deleteAuthSsh(String AuthSshId);

    /**
     * 更新认证
     * @param authSsh 认证信息
     */
    void updateAuthSsh(AuthSsh authSsh);

    /**
     * 查询单个认证
     * @param AuthSshId 认证id
     * @return 认证信息
     */
    @FindOne
    AuthSsh findOneAuthSsh(String AuthSshId);

    /**
     * 查询所有认证
     * @return 认证信息列表
     */
    @FindAll
    List<AuthSsh> findAllAuthSsh();


    @FindList
    List<AuthSsh> findAllAuthSshList(List<String> idList);


    /**
     * t条件查询认证信息
     * @return 认证集合
     */
    List<AuthSsh> findAuthSshList(AuthSshQuery authSshQuery);

}









































