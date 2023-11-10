package io.tiklab.xcode.scan.service;

import io.tiklab.core.page.Pagination;
import io.tiklab.join.annotation.FindAll;
import io.tiklab.join.annotation.FindList;
import io.tiklab.join.annotation.FindOne;
import io.tiklab.join.annotation.JoinProvider;
import io.tiklab.xcode.scan.model.DeployEnv;
import io.tiklab.xcode.scan.model.DeployEnvQuery;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* DeployEnvService-部署的环境
*/
@JoinProvider(model = DeployEnv.class)
public interface DeployEnvService {

    /**
    * 创建
    * @param deployEnv
    * @return
    */
    String createDeployEnv(@NotNull @Valid DeployEnv deployEnv);

    /**
    * 更新
    * @param deployEnv
    */
    void updateDeployEnv(@NotNull @Valid DeployEnv deployEnv);

    /**
    * 删除
    * @param id
    */
    void deleteDeployEnv(@NotNull String id);

    /**
     * 条件删除
     * @param repositoryId
     */
    void deleteDeployEnvByRecord(@NotNull String repositoryId);

    @FindOne
    DeployEnv findOne(@NotNull String id);
    @FindList
    List<DeployEnv> findList(List<String> idList);

    /**
    * 查找
    * @param id
    * @return
    */
    DeployEnv findDeployEnv(@NotNull String id);

    /**
    * 查找所有
    * @return
    */
    @FindAll
    List<DeployEnv> findAllDeployEnv();

    /**
    * 查询列表
    * @param deployEnvQuery
    * @return
    */
    List<DeployEnv> findDeployEnvList(DeployEnvQuery deployEnvQuery);

    /**
    * 按分页查询
    * @param deployEnvQuery
    * @return
    */
    Pagination<DeployEnv> findDeployEnvPage(DeployEnvQuery deployEnvQuery);


}