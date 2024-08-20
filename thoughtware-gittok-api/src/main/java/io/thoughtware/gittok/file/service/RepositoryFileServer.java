package io.thoughtware.gittok.file.service;

import io.thoughtware.gittok.file.model.FileQuery;
import io.thoughtware.gittok.file.model.FileMessage;
import io.thoughtware.gittok.file.model.RepositoryFile;

public interface RepositoryFileServer {

    /**
     * 裸仓库中创建文件夹
     * @param repositoryFile 文件信息
     */
    void createBareFolder(RepositoryFile repositoryFile);

    /**
     * 删除裸仓库中的文件
     * @param repositoryFile 文件信息
     */
    void deleteBareFile(RepositoryFile repositoryFile);

    /**
     * 更新裸仓库中的文件内容
     * @param repositoryFile 文件信息
     */
    void updateBareFile(RepositoryFile repositoryFile);

    /**
     * 创建文件
     * @param fileQuery 文件信息
     */
    void createFile(FileQuery fileQuery);


    /**
     * 删除文件
     * @param fileQuery 文件信息
     */
    void deleteFile(FileQuery fileQuery);



    /**
     * 读取文件
     * @param fileQuery 文件信息
     * @return 文件信息
     */
    FileMessage readFile(FileQuery fileQuery);

    /**
     * 写入文件
     * @param fileQuery 文件信息
     */
    void writeFile(FileQuery fileQuery);




    // void uploadFile();




}



































