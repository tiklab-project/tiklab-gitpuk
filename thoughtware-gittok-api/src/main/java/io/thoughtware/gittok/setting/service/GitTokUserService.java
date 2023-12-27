package io.thoughtware.gittok.setting.service;



import io.thoughtware.gittok.setting.model.GitTokUser;

import java.util.List;

public interface GitTokUserService {

    /**
     * 查询用户以及仓库
     */
    List<GitTokUser> findUserAndRpy();
}
