package io.tiklab.gitpuk.repository.service;

import io.tiklab.gitpuk.repository.model.RepositoryClean;
import io.tiklab.gitpuk.repository.model.RepositoryCleanQuery;

import java.util.List;
import java.util.Map;

/**
 * RepositoryCleanService-仓库清理
 */
public interface RepositoryCleanService {


    /**
     * 查询大文件
     * @param repositoryCleanQuery 信息
     * @return 仓库组id
     */
    String findLargeFile(RepositoryCleanQuery repositoryCleanQuery);

    /**
     * 查询大文件结果
     * @param repositoryCleanQuery
     * @return 仓库组id
     */
    List<RepositoryClean> findLargeFileResult(RepositoryCleanQuery repositoryCleanQuery);

    /**
     * 执行清理裸仓库中你的无效文件
     * @param rpyId
     * @return 仓库组id
     */
    String execCleanFile(String rpyId);

    /**
     * 删除大文件
     * @param repositoryCleanQuery
     * @return 仓库组id
     */
    String clearLargeFile(RepositoryCleanQuery repositoryCleanQuery);

    /**
     * 获取清除结果
     * @param repositoryId
     */
    Map<String, String> findClearResult(String repositoryId) ;


      void deleteFile(String filName);


}
