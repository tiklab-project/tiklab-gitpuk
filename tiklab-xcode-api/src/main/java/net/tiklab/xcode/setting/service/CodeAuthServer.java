package net.tiklab.xcode.setting.service;

import net.tiklab.join.annotation.FindAll;
import net.tiklab.join.annotation.FindList;
import net.tiklab.join.annotation.FindOne;
import net.tiklab.join.annotation.JoinProvider;
import net.tiklab.xcode.setting.model.CodeAuth;

import java.util.List;

@JoinProvider(model = CodeAuth.class)
public interface CodeAuthServer {


    /**
     * 创建认证
     * @param codeAuth 信息
     * @return 认证id
     */
    String createCodeAuth(CodeAuth codeAuth);

    /**
     * 删除认证
     * @param codeAuthId 认证id
     */
    void deleteCodeAuth(String codeAuthId);

    /**
     * 更新认证
     * @param codeAuth 认证信息
     */
    void updateCodeAuth(CodeAuth codeAuth);

    /**
     * 查询单个认证
     * @param codeAuthId 认证id
     * @return 认证信息
     */
    @FindOne
    CodeAuth findOneCodeAuth(String codeAuthId);

    /**
     * 查询所有认证
     * @return 认证信息列表
     */
    @FindAll
    List<CodeAuth> findAllCodeAuth();


    @FindList
    List<CodeAuth> findAllCodeAuthList(List<String> idList);

    /**
     * 查询用户认证
     * @return 认证集合
     */
    List<CodeAuth> findUserAuth();
    
    
    
    
    
    
    
    
}









































