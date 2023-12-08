package io.thoughtware.gittork.setting.service;



import io.thoughtware.gittork.setting.model.GitTorkUser;

import java.util.List;

public interface GitTorkUserService {

    /**
     * 查询用户以及仓库
     */
    List<GitTorkUser> findUserAndRpy();
}
