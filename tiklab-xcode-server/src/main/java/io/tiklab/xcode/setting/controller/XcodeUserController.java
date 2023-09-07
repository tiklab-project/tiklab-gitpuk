package io.tiklab.xcode.setting.controller;

import io.tiklab.core.Result;
import io.tiklab.postin.annotation.Api;
import io.tiklab.postin.annotation.ApiMethod;
import io.tiklab.postin.annotation.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/xcodeUser")
@Api(name = "XcodeUserController",desc = "xpack 项目的用户")
public class XcodeUserController {


    @RequestMapping(path="/backupsExec",method = RequestMethod.POST)
    @ApiMethod(name = "backupsExec",desc = "查询所有用户以及仓库数量")
    public Result<String> backupsExec(){



        return Result.ok();
    }

}
