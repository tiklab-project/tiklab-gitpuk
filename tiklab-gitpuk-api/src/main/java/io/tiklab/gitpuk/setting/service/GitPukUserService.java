package io.tiklab.gitpuk.setting.service;



import io.tiklab.core.page.Pagination;
import io.tiklab.gitpuk.setting.model.GitPukQuery;
import io.tiklab.gitpuk.setting.model.GitPukUser;

public interface GitPukUserService {

    /**
     * 查询用户以及仓库
     */
    Pagination<GitPukUser> findUserAndRpy(GitPukQuery gitPukQuery);

    /**
     * 根据用户啊查询仓库和仓库组数量
     */
    GitPukUser findNumByUser(GitPukQuery gitPukQuery);
}
