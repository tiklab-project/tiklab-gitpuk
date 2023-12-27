package io.thoughtware.gittok.setting.controller;

import io.thoughtware.gittok.setting.model.GitTokUser;
import io.thoughtware.gittok.setting.service.GitTokUserService;
import io.thoughtware.core.Result;
import io.thoughtware.postin.annotation.Api;
import io.thoughtware.postin.annotation.ApiMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/xcodeUser")
@Api(name = "GitTokUserController",desc = "xpack 项目的用户")
public class GitTokUserController {

    @Autowired
    GitTokUserService gitTorkUserService;

    @RequestMapping(path="/findUserAndRpy",method = RequestMethod.POST)
    @ApiMethod(name = "backupsExec",desc = "查询所有用户以及仓库数量")
    public Result<String> findUserAndRpy(){

        List<GitTokUser> userAndRpy = gitTorkUserService.findUserAndRpy();

        return Result.ok(userAndRpy);
    }

}
