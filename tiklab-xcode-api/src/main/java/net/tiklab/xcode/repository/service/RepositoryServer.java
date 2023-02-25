package net.tiklab.xcode.repository.service;

import net.tiklab.join.annotation.FindAll;
import net.tiklab.join.annotation.FindList;
import net.tiklab.join.annotation.FindOne;
import net.tiklab.join.annotation.JoinProvider;
import net.tiklab.xcode.repository.model.Repository;
import net.tiklab.xcode.repository.model.RepositoryCloneAddress;
import net.tiklab.xcode.file.model.FileTree;
import net.tiklab.xcode.file.model.FileTreeMessage;

import java.util.List;

@JoinProvider(model = Repository.class)
public interface RepositoryServer {

    /**
     * 创建代码库
     * @param repository 信息
     * @return 代码库id
     */
    String createCode(Repository repository);

    /**
     * 删除代码库
     * @param codeId 代码库id
     */
    void deleteCode(String codeId);

    /**
     * 更新代码库
     * @param repository 代码库信息
     */
    void updateCode(Repository repository);

    /**
     * 查询单个代码库
     * @param codeId 代码库id
     * @return 代码库信息
     */
    @FindOne
    Repository findOneCode(String codeId);

    /**
     * 查询用户仓库
     * @param userId 用户id
     * @return 用户仓库
     */
    List<Repository> findUserCode(String userId);

    /**
     * 查询所有代码库
     * @return 代码库信息列表
     */
    @FindAll
    List<Repository> findAllCode();


    @FindList
    List<Repository> findAllCodeList(List<String> idList);

    /**
     * 根据仓库名称查询仓库信息
     * @param codeName 仓库名称
     * @return 仓库信息
     */
    Repository findNameCode(String codeName);


    /**
     * 获取文件信息
     * @param message 信息
     * @return 文件集合
     */
    List<FileTree> findFileTree(FileTreeMessage message);


    /**
     * 获取克隆地址
     * @param codeId 仓库id
     * @return 地址信息
     */
    RepositoryCloneAddress findCloneAddress(String codeId);

















}




















































