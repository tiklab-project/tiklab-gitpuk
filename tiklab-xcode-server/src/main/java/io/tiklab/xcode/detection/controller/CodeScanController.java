package io.tiklab.xcode.detection.controller;

import io.tiklab.core.Result;
import io.tiklab.postin.annotation.Api;
import io.tiklab.postin.annotation.ApiMethod;
import io.tiklab.postin.annotation.ApiParam;
import io.tiklab.xcode.detection.model.CodeScan;
import io.tiklab.xcode.detection.model.DeployEnv;
import io.tiklab.xcode.detection.service.CodeScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/codeScan")
@Api(name = "CodeScanController",desc = "代码扫描管理")
public class CodeScanController {

    @Autowired
    CodeScanService codeScanService;

    @RequestMapping(path="/codeScanExec",method = RequestMethod.POST)
    @ApiMethod(name = "CodeScanExec",desc = "执行代码扫描")
    @ApiParam(name = "repositoryId",desc = "repositoryId",required = true)
    public Result<String> codeScanExec( @NotNull String  repositoryId){
        boolean scanExec = codeScanService.codeScanExec(repositoryId);

        return Result.ok(scanExec);
    }

    @RequestMapping(path="/findScanState",method = RequestMethod.POST)
    @ApiMethod(name = "findScanState",desc = "获取扫描状态")
    @ApiParam(name = "repositoryId",desc = "repositoryId",required = true)
    public Result<String> findScanState( @NotNull String  repositoryId){
        String scanResult = codeScanService.findScanState(repositoryId);

        return Result.ok(scanResult);
    }

    @RequestMapping(path="/findScanResult",method = RequestMethod.POST)
    @ApiMethod(name = "findScanResult",desc = "查询扫描后项目结果")
    @ApiParam(name = "repositoryId",desc = "repositoryId",required = true)
    public Result<CodeScan> findScanResult( @NotNull String  repositoryId){
        CodeScan scanResult = codeScanService.findScanResult(repositoryId);

        return Result.ok(scanResult);
    }

    @RequestMapping(path="/createCodeScan",method = RequestMethod.POST)
    @ApiMethod(name = "createCodeScan",desc = "创建扫描方案")
    @ApiParam(name = "CodeScan",desc = "CodeScan",required = true)
    public Result<String> createCodeScan(@RequestBody @NotNull @Valid CodeScan CodeScan){
        String id = codeScanService.createCodeScan(CodeScan);

        return Result.ok(id);
    }

    @RequestMapping(path="/findCodeScanByRpyId",method = RequestMethod.POST)
    @ApiMethod(name = "findCodeScanByRpyId",desc = "查询扫描方案")
    @ApiParam(name = "CodeScan",desc = "CodeScan",required = true)
    public Result<String> findCodeScanByRpyId( @NotNull String repositoryId){
        CodeScan codeScan = codeScanService.findCodeScanByRpyId(repositoryId);

        return Result.ok(codeScan);
    }

}
