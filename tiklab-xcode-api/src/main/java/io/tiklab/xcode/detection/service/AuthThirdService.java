package io.tiklab.xcode.detection.service;

import io.tiklab.core.page.Pagination;
import io.tiklab.join.annotation.FindAll;
import io.tiklab.join.annotation.FindList;
import io.tiklab.join.annotation.FindOne;
import io.tiklab.join.annotation.JoinProvider;
import io.tiklab.xcode.detection.model.AuthThird;
import io.tiklab.xcode.detection.model.AuthThirdQuery;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* AuthThirdService-插件接口
*/
@JoinProvider(model = AuthThird.class)
public interface AuthThirdService {

    /**
    * 创建
    * @param recordCommit
    * @return
    */
    String createAuthThird(@NotNull @Valid AuthThird recordCommit);

    /**
    * 更新
    * @param recordCommit
    */
    void updateAuthThird(@NotNull @Valid AuthThird recordCommit);

    /**
    * 删除
    * @param id
    */
    void deleteAuthThird(@NotNull String id);

    /**
     * 条件删除
     * @param
     */
    void deleteAuthThirdByRecord(@NotNull String repositoryId);

    @FindOne
    AuthThird findOne(@NotNull String id);
    @FindList
    List<AuthThird> findList(List<String> idList);

    /**
    * 查找
    * @param id
    * @return
    */
    AuthThird findAuthThird(@NotNull String id);

    /**
    * 查找所有
    * @return
    */
    @FindAll
    List<AuthThird> findAllAuthThird();

    /**
    * 查询列表
    * @param authThirdQuery
    * @return
    */
    List<AuthThird> findAuthThirdList(AuthThirdQuery authThirdQuery);

    /**
    * 按分页查询
    * @param authThirdQuery
    * @return
    */
    Pagination<AuthThird> findAuthThirdPage(AuthThirdQuery authThirdQuery);


}