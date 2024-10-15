package io.tiklab.gitpuk.repository.service;

import io.tiklab.gitpuk.repository.model.RepositoryLfs;
import io.tiklab.gitpuk.repository.model.RepositoryLfsQuery;
import io.tiklab.toolkit.join.annotation.FindAll;
import io.tiklab.toolkit.join.annotation.FindList;
import io.tiklab.toolkit.join.annotation.FindOne;
import io.tiklab.toolkit.join.annotation.JoinProvider;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* RepositoryLfsService-仓库lfs文件
*/
@JoinProvider(model = RepositoryLfs.class)
public interface RepositoryLfsService {

    /**
    * 创建
    * @param repositoryLfs
    * @return
    */
    String createRepositoryLfs(@NotNull @Valid RepositoryLfs repositoryLfs);

    /**
    * 更新
    * @param repositoryLfs
    */
    void updateRepositoryLfs(@NotNull @Valid RepositoryLfs repositoryLfs);

    /**
    * 删除
    * @param id
    */
    void deleteRepositoryLfs(@NotNull String id);

    /**
     * 通过仓库id删除
     * @param repositoryId 仓库id
     */
    void deleteRepositoryLfsByRpyId(@NotNull String repositoryId);


    @FindOne
    RepositoryLfs findOne(@NotNull String id);
    @FindList
    List<RepositoryLfs> findList(List<String> idList);

    /**
    * 查找
    * @param id
    * @return
    */
    RepositoryLfs findRepositoryLfs(@NotNull String id);

    /**
    * 查找所有
    * @return
    */
    @FindAll
    List<RepositoryLfs> findAllRepositoryLfs();

    /**
    * 查询列表
    * @param repositoryLfsQuery
    * @return
    */
    List<RepositoryLfs> findRepositoryLfsList(RepositoryLfsQuery repositoryLfsQuery);

    /**
     * 编辑lfs文件信息
     * @param repositoryLfs
     * @return
     */
    void editRepositoryLfs(RepositoryLfs repositoryLfs);
}