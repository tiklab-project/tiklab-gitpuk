package io.thoughtware.gittok.scan.controller;

import io.thoughtware.gittok.scan.model.*;
import io.thoughtware.gittok.scan.service.CodeScanService;
import io.thoughtware.gittok.scan.service.CodeScanSonarService;
import io.thoughtware.core.Result;
import io.thoughtware.gittok.scan.service.CodeScanSpotBugsService;
import io.thoughtware.postin.annotation.Api;
import io.thoughtware.postin.annotation.ApiMethod;
import io.thoughtware.postin.annotation.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/codeScan")
@Api(name = "CodeScanController",desc = "代码扫描管理")
public class CodeScanController {

    @Autowired
    CodeScanService codeScanService;

    @Autowired
    CodeScanSonarService codeScanSonarService;

    @Autowired
    CodeScanSpotBugsService codeScanSpotBugsService;

    @RequestMapping(path="/codeScanExec",method = RequestMethod.POST)
    @ApiMethod(name = "CodeScanExec",desc = "执行代码扫描")
    @ApiParam(name = "scanPlayId",desc = "scanPlayId",required = true)
    public Result<String> codeScanExec( @NotNull String  scanPlayId){
        String scanExec = codeScanService.codeScanExec(scanPlayId);

        return Result.ok(scanExec);
    }

    @RequestMapping(path="/findScanState",method = RequestMethod.POST)
    @ApiMethod(name = "findScanState",desc = "获取扫描状态")
    @ApiParam(name = "scanPlayId",desc = "scanPlayId",required = true)
    public Result<ScanRecord> findScanState(@NotNull String  scanPlayId, @NotNull String scanWay){
        ScanRecord scanResult = codeScanService.findScanState(scanPlayId,scanWay);

        return Result.ok(scanResult);
    }





    @RequestMapping(path="/findScanResult",method = RequestMethod.POST)
    @ApiMethod(name = "findScanResult",desc = "查询扫描后项目结果")
    @ApiParam(name = "repositoryId",desc = "repositoryId",required = true)
    public Result<CodeScan> findScanResult(@NotNull String  repositoryId){
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


    @RequestMapping(path="/findScanIssuesDeBySonar",method = RequestMethod.POST)
    @ApiMethod(name = "findScanIssuesDeBySonar",desc = "查询sonar执行的问题详情")
    @ApiParam(name = "scanPlayId",desc = "scanPlayId",required = true)
    public Result<List<ScanIssuesDetails>> findScanIssuesDeBySonar(@NotNull String issueKey, String component){
        List<ScanIssuesDetails> issuesDeBySonar = codeScanSonarService.findScanIssuesDeBySonar(issueKey, component);

        return Result.ok(issuesDeBySonar);
    }


    @RequestMapping(path="/findScanBySonar",method = RequestMethod.POST)
    @ApiMethod(name = "findScanBySonar",desc = "查询")
    @ApiParam(name = "scanPlayId",desc = "scanPlayId",required = true)
    public Result<String> findScanBySonar( @NotNull String  scanPlayId){
        ScanRecord scanBySonar = codeScanSonarService.findScanBySonar(scanPlayId);

        return Result.ok(scanBySonar);
    }

    @RequestMapping(path="/findLog",method = RequestMethod.POST)
    @ApiMethod(name = "findLog",desc = "查询")
    @ApiParam(name = "scanPlayId",desc = "scanPlayId",required = true)
    public Result<String> findLog( @NotNull String  scanPlayId){
        String log = codeScanSpotBugsService.findLog(scanPlayId);

        return Result.ok(log);
    }

}
