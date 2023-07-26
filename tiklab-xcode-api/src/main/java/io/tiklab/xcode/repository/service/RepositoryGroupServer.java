package io.tiklab.xcode.repository.service;

import io.tiklab.join.annotation.FindAll;
import io.tiklab.join.annotation.FindList;
import io.tiklab.join.annotation.FindOne;
import io.tiklab.join.annotation.JoinProvider;
import io.tiklab.xcode.repository.model.RepositoryGroup;


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
     * @param userId 用户id
     * @return 仓库组集合
     */
    List<RepositoryGroup> findUserGroup(String userId);

    /**
     * 通过名字查询仓库组
     * @param groupName groupName
     * @return
     */
    RepositoryGroup findGroupByName(String groupName);
}





















