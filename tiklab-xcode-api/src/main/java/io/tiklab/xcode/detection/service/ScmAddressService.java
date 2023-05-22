package io.tiklab.xcode.detection.service;

import io.tiklab.core.page.Pagination;
import io.tiklab.join.annotation.FindAll;
import io.tiklab.join.annotation.FindList;
import io.tiklab.join.annotation.FindOne;
import io.tiklab.join.annotation.JoinProvider;
import io.tiklab.xcode.detection.model.ScmAddress;
import io.tiklab.xcode.detection.model.ScmAddressQuery;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* ScmAddressService-插件接口
*/
@JoinProvider(model = ScmAddress.class)
public interface ScmAddressService {

    /**
    * 创建
    * @param recordCommit
    * @return
    */
    String createScmAddress(@NotNull @Valid ScmAddress recordCommit);

    /**
    * 更新
    * @param recordCommit
    */
    void updateScmAddress(@NotNull @Valid ScmAddress recordCommit);

    /**
    * 删除
    * @param id
    */
    void deleteScmAddress(@NotNull String id);

    /**
     * 条件删除
     * @param
     */
    void deleteScmAddressByRecord(@NotNull String repositoryId);

    @FindOne
    ScmAddress findOne(@NotNull String id);
    @FindList
    List<ScmAddress> findList(List<String> idList);

    /**
    * 查找
    * @param id
    * @return
    */
    ScmAddress findScmAddress(@NotNull String id);

    /**
    * 查找所有
    * @return
    */
    @FindAll
    List<ScmAddress> findAllScmAddress();

    /**
    * 查询列表
    * @param scmAddressQuery
    * @return
    */
    List<ScmAddress> findScmAddressList(ScmAddressQuery scmAddressQuery);

    /**
    * 按分页查询
    * @param scmAddressQuery
    * @return
    */
    Pagination<ScmAddress> findScmAddressPage(ScmAddressQuery scmAddressQuery);


}