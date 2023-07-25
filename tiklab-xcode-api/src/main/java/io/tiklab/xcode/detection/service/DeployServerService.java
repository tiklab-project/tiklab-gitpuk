package io.tiklab.xcode.detection.service;

import io.tiklab.core.page.Pagination;
import io.tiklab.join.annotation.FindAll;
import io.tiklab.join.annotation.FindList;
import io.tiklab.join.annotation.FindOne;
import io.tiklab.join.annotation.JoinProvider;
import io.tiklab.xcode.detection.model.DeployServer;
import io.tiklab.xcode.detection.model.DeployServerQuery;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* DeployServerService-插件接口
*/
@JoinProvider(model = DeployServer.class)
public interface DeployServerService {

    /**
    * 创建
    * @param recordCommit
    * @return
    */
    String createDeployServer(@NotNull @Valid DeployServer recordCommit);

    /**
    * 更新
    * @param recordCommit
    */
    void updateDeployServer(@NotNull @Valid DeployServer recordCommit);

    /**
    * 删除
    * @param id
    */
    void deleteDeployServer(@NotNull String id);

    /**
     * 条件删除
     * @param repositoryId
     */
    void deleteDeployServerByRecord(@NotNull String repositoryId);

    @FindOne
    DeployServer findOne(@NotNull String id);
    @FindList
    List<DeployServer> findList(List<String> idList);

    /**
    * 查找
    * @param id
    * @return
    */
    DeployServer findDeployServer(@NotNull String id);

    /**
    * 查找所有
    * @return
    */
    @FindAll
    List<DeployServer> findAllDeployServer();

    /**
    * 查询列表
    * @param deployServerQuery
    * @return
    */
    List<DeployServer> findDeployServerList(DeployServerQuery deployServerQuery);

    /**
    * 按分页查询
    * @param deployServerQuery
    * @return
    */
    Pagination<DeployServer> findDeployServerPage(DeployServerQuery deployServerQuery);


}