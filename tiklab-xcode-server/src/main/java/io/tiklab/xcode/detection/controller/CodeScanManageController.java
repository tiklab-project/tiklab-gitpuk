package io.tiklab.xcode.detection.controller;

import io.tiklab.core.Result;
import io.tiklab.postin.annotation.Api;
import io.tiklab.postin.annotation.ApiMethod;
import io.tiklab.postin.annotation.ApiParam;
import io.tiklab.xcode.detection.model.ScmAddress;
import io.tiklab.xcode.detection.service.CodeScanManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/codeScanManage")
@Api(name = "CodeScanManageController",desc = "代码扫描管理")
public class CodeScanManageController {

    @Autowired
    CodeScanManageService codeScanManageService;

    @RequestMapping(path="/CodeScanExec",method = RequestMethod.POST)
    @ApiMethod(name = "CodeScanExec",desc = "执行代码扫描")
    @ApiParam(name = "repository",desc = "repository",required = true)
    public Result<String> CodeScanExec( @NotNull String  repository){
         codeScanManageService.codeScanExec(repository);

        return Result.ok();
    }

}
