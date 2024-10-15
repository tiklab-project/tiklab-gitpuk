package io.tiklab.gitpuk.file.service;

import com.alibaba.fastjson.JSONObject;
import io.tiklab.gitpuk.file.model.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
     * 下载裸仓库中的文件
     * @param jsonObject jsonObject
     */
    byte[] downLoadBareFile(JSONObject jsonObject);


    /**
     * 下载裸仓库
     * @param response response
     * @param  queryString 客户端请求数据
     */
   void downLoadBareRepo(HttpServletResponse response,String queryString);


    /**
     * 读取裸仓库文件
     * @param fileQuery 文件信息
     * @return 文件信息
     */
    FileMessage readBareFile(FileQuery fileQuery);


    /**
     * lfs文件下载文件
     * @param jsonObject jsonObject
     */
    byte[] downloadLfsFile(JSONObject jsonObject);


    /**
     * 获取下载的 loadQuery
     * @param queryString jsonObject
     */
     JSONObject getDownLoadQuery(String queryString);

    /**
     * 获取裸仓库文件信息
     * @param message 信息
     * @return 文件集合
     */
    List<FileTree> findFileTree(FileFindQuery message);

    /**
     * 查询裸仓库中的所有文件
     * @param  fileFindQuery fileFindQuery
     */
    List<String> findBareAllFile(FileFindQuery fileFindQuery);
}



































