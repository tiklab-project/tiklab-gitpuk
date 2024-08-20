package io.thoughtware.gittok.repository.service;

import io.thoughtware.core.page.Pagination;
import io.thoughtware.gittok.repository.model.RepositoryCollect;
import io.thoughtware.gittok.repository.model.RepositoryCollectQuery;
import io.thoughtware.toolkit.join.annotation.FindAll;
import io.thoughtware.toolkit.join.annotation.FindList;
import io.thoughtware.toolkit.join.annotation.FindOne;
import io.thoughtware.toolkit.join.annotation.JoinProvider;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* RepositoryCollectService-插件接口
*/
@JoinProvider(model = RepositoryCollect.class)
public interface RepositoryCollectService {

    /**
    * 创建
    * @param repositoryCollect
    * @return
    */
    String createRepositoryCollect(@NotNull @Valid RepositoryCollect repositoryCollect);

    /**
    * 更新
    * @param repositoryCollect
    */
    void updateRepositoryCollect(@NotNull @Valid RepositoryCollect repositoryCollect);

    /**
    * 删除
    * @param id
    */
    void deleteRepositoryCollect(@NotNull String id);

    /**
     * 通过仓库删除
     * @param repositoryId 仓库id
     */
    void deleteRepositoryCollectByRepository(@NotNull String repositoryId);

    /**
     * 条件删除
     * @param repositoryId
     */
    void deleteRepositoryCollectByRecord(@NotNull String repositoryId);

    @FindOne
    RepositoryCollect findOne(@NotNull String id);
    @FindList
    List<RepositoryCollect> findList(List<String> idList);

    /**
    * 查找
    * @param id
    * @return
    */
    RepositoryCollect findRepositoryCollect(@NotNull String id);

    /**
    * 查找所有
    * @return
    */
    @FindAll
    List<RepositoryCollect> findAllRepositoryCollect();

    /**
    * 查询列表
    * @param repositoryCollectQuery
    * @return
    */
    List<RepositoryCollect> findRepositoryCollectList(RepositoryCollectQuery repositoryCollectQuery);

    /**
     * 通过仓库的ids查询收藏
     * @param repositoryIds
     * @param  userId 用户
     * @return
     */
    List<RepositoryCollect> findRepositoryCollectList(String[] repositoryIds,String userId);

    /**
    * 按分页查询
    * @param repositoryCollectQuery
    * @return
    */
    Pagination<RepositoryCollect> findRepositoryCollectPage(RepositoryCollectQuery repositoryCollectQuery);


    /**
     * 通过仓库id 删除
     * @param repositoryId
     * @return
     */
    void deleteCollectByRpyId(String repositoryId);
}