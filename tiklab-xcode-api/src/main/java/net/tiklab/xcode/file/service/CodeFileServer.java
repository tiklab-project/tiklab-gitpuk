package net.tiklab.xcode.file.service;

import net.tiklab.xcode.file.model.CodeFile;
import net.tiklab.xcode.file.model.CodeFileMessage;

public interface CodeFileServer {

    /**
     * 创建文件
     * @param file 文件信息
     */
    void createFile(CodeFile file);


    /**
     * 删除文件
     * @param file 文件信息
     */
    void deleteFile(CodeFile file);


    /**
     * 读取文件
     * @param codeId 仓库id
     * @param fileAddress 文件地址
     * @return 文件信息
     */
    CodeFileMessage readFile(String codeId, String fileAddress);

    /**
     * 写入文件
     * @param file 文件信息
     */
    void writeFile(CodeFile file);


    // void uploadFile();




}



































