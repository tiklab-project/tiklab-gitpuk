package io.tiklab.xcode.setting.controller;

import io.tiklab.core.Result;
import io.tiklab.postin.annotation.Api;
import io.tiklab.postin.annotation.ApiMethod;
import io.tiklab.postin.annotation.ApiParam;
import io.tiklab.xcode.setting.model.XcodeUser;
import io.tiklab.xcode.setting.service.XcodeUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/xcodeUser")
@Api(name = "XcodeUserController",desc = "xpack 项目的用户")
public class XcodeUserController {

    @Autowired
    XcodeUserService xcodeUserService;
    @RequestMapping(path="/findUserAndRpy",method = RequestMethod.POST)
    @ApiMethod(name = "backupsExec",desc = "查询所有用户以及仓库数量")
    public Result<String> findUserAndRpy(){

        List<XcodeUser> userAndRpy = xcodeUserService.findUserAndRpy();

        return Result.ok(userAndRpy);
    }

}
