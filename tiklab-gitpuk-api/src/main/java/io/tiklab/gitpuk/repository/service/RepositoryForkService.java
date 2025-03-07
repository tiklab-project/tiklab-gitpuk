package io.tiklab.gitpuk.repository.service;

import io.tiklab.core.page.Pagination;
import io.tiklab.gitpuk.repository.model.RepositoryFork;
import io.tiklab.gitpuk.repository.model.RepositoryForkQuery;
import io.tiklab.toolkit.join.annotation.FindAll;
import io.tiklab.toolkit.join.annotation.FindList;
import io.tiklab.toolkit.join.annotation.FindOne;
import io.tiklab.toolkit.join.annotation.JoinProvider;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* RepositoryForkService-仓库fork
*/
@JoinProvider(model = RepositoryFork.class)
public interface RepositoryForkService {

    /**
    * 创建
    * @param repositoryFork
    * @return
    */
    String createRepositoryFork(@NotNull @Valid RepositoryFork repositoryFork);

    /**
    * 更新
    * @param repositoryFork
    */
    void updateRepositoryFork(@NotNull @Valid RepositoryFork repositoryFork);

    /**
    * 删除
    * @param id
    */
    void deleteRepositoryFork(@NotNull String id);

    /**
     * 删除fork仓库，也会清除掉fork记录
     * @param forkRepId fork仓库id
     */
    void deleteRepForkByRpyId(@NotNull String forkRepId);


    @FindOne
    RepositoryFork findOne(@NotNull String id);
    @FindList
    List<RepositoryFork> findList(List<String> idList);

    /**
    * 查找
    * @param id
    * @return
    */
    RepositoryFork findRepositoryFork(@NotNull String id);

    /**
    * 查找所有
    * @return
    */
    @FindAll
    List<RepositoryFork> findAllRepositoryFork();

    /**
    * 查询列表
    * @param repositoryForkQuery
    * @return
    */
    List<RepositoryFork> findRepositoryForkList(RepositoryForkQuery repositoryForkQuery);

    /**
     * 按分页查询
     * @param repositoryForkQuery
     * @return
     */
    Pagination<RepositoryFork> findRepositoryForkPage(RepositoryForkQuery repositoryForkQuery);

    /**
     * 执行仓库fork
     * @param repositoryFork
     */
    String execRepositoryFork(RepositoryFork repositoryFork);

    /**
     * 查询fork结果
     * @param repositoryId 仓库id
     */
    Integer findForkResult(String repositoryId);
}