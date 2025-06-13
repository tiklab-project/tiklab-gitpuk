package io.tiklab.gitpuk.setting.controller;

import io.tiklab.core.page.Pagination;
import io.tiklab.gitpuk.setting.model.GitPukQuery;
import io.tiklab.gitpuk.setting.model.GitPukUser;
import io.tiklab.gitpuk.setting.service.GitPukUserService;
import io.tiklab.core.Result;
import io.tiklab.postin.annotation.Api;
import io.tiklab.postin.annotation.ApiMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/gitPukUser")
//@Api(name = "GitTokUserController",desc = "xpack 项目的用户")
public class GitTokUserController {

    @Autowired
    GitPukUserService gitPukUserService;

    @RequestMapping(path="/findUserAndRpy",method = RequestMethod.POST)
    @ApiMethod(name = "backupsExec",desc = "查询所有用户以及仓库数量")
    public Result<GitPukUser> findUserAndRpy(@RequestBody @NotNull @Valid GitPukQuery gitPukQuery){

        Pagination<GitPukUser> userAndRpy = gitPukUserService.findUserAndRpy(gitPukQuery);

        return Result.ok(userAndRpy);
    }

    @RequestMapping(path="/findNumByUser",method = RequestMethod.POST)
    @ApiMethod(name = "backupsExec",desc = "根据用户查询仓库数量和仓库组数量")
    public Result<GitPukUser> findNumByUser(@RequestBody @NotNull @Valid GitPukQuery gitPukQuery){

      GitPukUser userAndRpy = gitPukUserService.findNumByUser(gitPukQuery);

        return Result.ok(userAndRpy);
    }


}
