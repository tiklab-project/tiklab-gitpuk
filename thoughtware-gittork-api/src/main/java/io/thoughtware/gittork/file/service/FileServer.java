package io.thoughtware.gittork.file.service;

import io.thoughtware.gittork.file.model.FileQuery;
import io.thoughtware.gittork.file.model.FileMessage;

public interface FileServer {

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



































