package io.thoughtware.gittok.repository.service;

import io.thoughtware.gittok.repository.model.RepositoryGroup;
import io.thoughtware.gittok.repository.model.RepositoryGroupQuery;
import io.thoughtware.core.page.Pagination;
import io.thoughtware.toolkit.join.annotation.FindAll;
import io.thoughtware.toolkit.join.annotation.FindList;
import io.thoughtware.toolkit.join.annotation.FindOne;
import io.thoughtware.toolkit.join.annotation.JoinProvider;


import java.util.List;

@JoinProvider(model = RepositoryGroup.class)
public interface RepositoryGroupServer {


    /**
     * 创建仓库组
     * @param repositoryGroup 信息
     * @return 仓库组id
     */
    String createCodeGroup(RepositoryGroup repositoryGroup);

    /**
     * 删除仓库组
     * @param codeGroupId 仓库组id
     */
    void deleteCodeGroup(String codeGroupId);

    /**
     * 更新仓库组
     * @param repositoryGroup 仓库组信息
     */
    void updateCodeGroup(RepositoryGroup repositoryGroup);

    /**
     * 查询单个仓库组
     * @param codeGroupId 仓库组id
     * @return 仓库组信息
     */
    @FindOne
    RepositoryGroup findOneCodeGroup(String codeGroupId);

    /**
     * 查询所有仓库组
     * @return 仓库组信息列表
     */
    @FindAll
    List<RepositoryGroup> findAllCodeGroup();


    @FindList
    List<RepositoryGroup> findAllCodeGroupList(List<String> idList);

    /**
     * 查询用户仓库组
     * @param repositoryGroupQuery repositoryGroupQuery
     * @return 仓库组集合
     */
    Pagination<RepositoryGroup> findRepositoryGroupPage(RepositoryGroupQuery repositoryGroupQuery);

    /**
     * 通过名字查询仓库组
     * @param groupName groupName
     * @return
     */
    RepositoryGroup findGroupByName(String groupName);

    /**
     * 查询所有仓库组
     * @return
     */
    List<RepositoryGroup> findAllGroup();


    /**
     * 查询自己创建的和有创建仓库权限的仓库组
     * @param userId 创建人id
     * @return 仓库组list
     */
    List<RepositoryGroup> findCanCreateRpyGroup(String userId);
}





















