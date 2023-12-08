package io.thoughtware.gittork.scan.service;

import io.thoughtware.gittork.scan.model.DeployServer;
import io.thoughtware.gittork.scan.model.DeployServerQuery;
import io.thoughtware.core.page.Pagination;
import io.thoughtware.join.annotation.FindAll;
import io.thoughtware.join.annotation.FindList;
import io.thoughtware.join.annotation.FindOne;
import io.thoughtware.join.annotation.JoinProvider;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* DeployServerService-部署的服务
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