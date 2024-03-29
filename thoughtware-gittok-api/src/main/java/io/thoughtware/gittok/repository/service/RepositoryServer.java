package io.thoughtware.gittok.repository.service;

import io.thoughtware.gittok.file.model.FileTree;
import io.thoughtware.gittok.file.model.FileTreeMessage;
import io.thoughtware.gittok.repository.model.Repository;
import io.thoughtware.gittok.repository.model.RepositoryCloneAddress;
import io.thoughtware.gittok.repository.model.RepositoryQuery;
import io.thoughtware.core.page.Pagination;
import io.thoughtware.toolkit.join.annotation.FindAll;
import io.thoughtware.toolkit.join.annotation.FindList;
import io.thoughtware.toolkit.join.annotation.FindOne;
import io.thoughtware.toolkit.join.annotation.JoinProvider;

import java.util.List;

@JoinProvider(model = Repository.class)
public interface RepositoryServer {

    /**
     * 创建代码库以及相关数据
     * @param repository 信息
     * @return 代码库id
     */
    String createRpyData(Repository repository);

    /**
     * 创建代码库
     * @param repository 信息
     * @return 代码库id
     */
    String createRpy(Repository repository);

    /**
     * 删除代码库
     * @param rpyId 代码库id
     */
    void deleteRpy(String rpyId);

    /**
     * 更新代码库
     * @param repository 代码库信息
     */
    void updateRpy(Repository repository);


    /**
     * 更新代码库
     * @param repository 代码库信息
     */
    void updateRepository(Repository repository);

    /**
     * 查询单个代码库
     * @param rpyId 代码库id
     * @return 代码库信息
     */
    Repository findOneRpy(String rpyId);

    @FindOne
    Repository findOne(String rpyId);

    Repository findOneOnlyRpy(String rpyId);

    /**
     * 查询用户仓库
     * @param userId 用户id
     * @return 用户仓库
     */
    List<Repository> findUserRpy(String userId);

    /**
     * 查询所有代码库
     * @return 代码库信息列表
     */
    @FindAll
    List<Repository> findAllRpy();

    /**
     * 查询所有代码库
     * @return 代码库信息列表
     */

    List<Repository> findAllRpyList();


    @FindList
    List<Repository> findList(List<String> idList);

    /**
     * 查询所有仓库
     * @return 文件集合
     */
    List<Repository> findRpyList();

    /**
     * 获取文件信息
     * @param message 信息
     * @return 文件集合
     */
    List<FileTree> findFileTree(FileTreeMessage message);


    /**
     * 获取克隆地址
     * @param rpyId 仓库id
     * @return 地址信息
     */
    RepositoryCloneAddress findCloneAddress(String rpyId);

    /**
     * 条件查询仓库
     * @param repositoryQuery repositoryQuery
     */
    List<Repository> findRepositoryList(RepositoryQuery repositoryQuery);

    /**
     * 通过仓库组id查询仓库
     * @param groupId groupId
     */
    List<Repository> findRepositoryList(String groupId);

    /**
     * 分页条件查询仓库
     * @param repositoryQuery repositoryQuery
     */
     Pagination<Repository> findRepositoryPage(RepositoryQuery repositoryQuery);

    /**
     * 查询仓库组的仓库
     * @param repositoryQuery
     * @return 仓库
     */
    Pagination<Repository> findGroupRepository(RepositoryQuery repositoryQuery);

    /**
     * 通过仓库id 查询仓库
     * @param id
     */
    Repository findRepository(String id);

    /**
     * 通过仓库名字或 仓库组查询仓库是否存在
     * @param repositoryQuery
     * @return 仓库
     */
    List<Repository> findRepositoryByName(RepositoryQuery repositoryQuery);



    /**
     * 通过仓库地址查询
     * @param address
     * @return 仓库
     */
    Repository findRepositoryByAddress(String address);




    /**
     * 根据路径删除仓库
     * @param address
     */
    void deleteRpyByAddress(String address);

    /**
     * 通过仓库address 查询绝对路径
     * @param address
     * @return 绝对路径
     */
    String findRepositoryAp(String address);


    /**
     * 通过用户查询有权限的仓库
     * @param account  用户账号
     * @param  password 密码
     * @return List<Repository>
     */
    List<Repository> findRepositoryByUser(String account,String password,String DirId);


    /**
     * 获取域名或者ip
     * @return 绝对路径
     */
    String getAddress();

    /**
     * 获取仓库地址
     * @return 绝对路径
     */
    String getRepositoryPath();

    /**
     * 查询用户推送过的仓库
     * @return userId 用户ID
     */
    List<Repository> findCommitRepository(String userId);

    /**
     * 查询用户有权限的私有仓库
     * @return userId 用户ID
     */
    Pagination<Repository> findPrivateRepositoryByUser(RepositoryQuery repositoryQuery);

    /**
     *查询用户是否有当前项目权限
     * @return userId 用户ID
     */

    String findRepositoryAuth(String rpyId);

    /**
     *查询当前仓库的默认分支
     * @return userId 用户ID
     */
    String findDefaultBranch(String repositoryId);

    /**
     *重置仓库
     * @param  rpyId 仓库id
     */
    void resetRepository(String rpyId);
}




















































