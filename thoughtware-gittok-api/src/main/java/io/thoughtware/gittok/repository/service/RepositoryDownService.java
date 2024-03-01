package io.thoughtware.gittok.repository.service;

import java.io.IOException;
import java.io.InputStream;

public interface RepositoryDownService {


    /**
     *下载仓库
     * @param  reqData 请求数据
     * @param name 名字
     * @return userId 用户ID
     */
    String downloadRpy(String reqData, int name) ;
}
