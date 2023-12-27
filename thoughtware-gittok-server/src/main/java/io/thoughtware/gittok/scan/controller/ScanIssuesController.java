package io.thoughtware.gittok.scan.controller;

import io.thoughtware.gittok.scan.model.ScanIssues;
import io.thoughtware.gittok.scan.model.ScanIssuesQuery;
import io.thoughtware.gittok.scan.service.ScanIssuesService;
import io.thoughtware.core.Result;
import io.thoughtware.core.page.Pagination;
import io.thoughtware.postin.annotation.Api;
import io.thoughtware.postin.annotation.ApiMethod;
import io.thoughtware.postin.annotation.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * ScanIssuesController
 */
@RestController
@RequestMapping("/scanIssues")
@Api(name = "ScanIssuesController",desc = "扫描问题")
public class ScanIssuesController {

    private static Logger logger = LoggerFactory.getLogger(ScanIssuesController.class);

    @Autowired
    private ScanIssuesService scanIssuesService;

    @RequestMapping(path="/createScanIssues",method = RequestMethod.POST)
    @ApiMethod(name = "createScanIssues",desc = "添加扫描制品问题")
    @ApiParam(name = "scanIssues",desc = "scanIssues",required = true)
    public Result<String> createScanIssues(@RequestBody @Valid @NotNull ScanIssues scanIssues){
        String scanIssuesId = scanIssuesService.createScanIssues(scanIssues);

        return Result.ok(scanIssuesId);
    }

    @RequestMapping(path="/updateScanIssues",method = RequestMethod.POST)
    @ApiMethod(name = "updateScanIssues",desc = "更新扫描制品问题")
    @ApiParam(name = "scanIssues",desc = "scanIssues",required = true)
    public Result<String> updateScanIssues(@RequestBody @Valid @NotNull ScanIssues scanIssues){
         scanIssuesService.updateScanIssues(scanIssues);

        return Result.ok();
    }


    @RequestMapping(path="/deleteScanIssues",method = RequestMethod.POST)
    @ApiMethod(name = "deleteScanIssues",desc = "删除扫描问题")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<Void> deleteScanIssues(@NotNull String id){
        scanIssuesService.deleteScanIssues(id);

        return Result.ok();
    }

    @RequestMapping(path="/findScanIssues",method = RequestMethod.POST)
    @ApiMethod(name = "findScanIssues",desc = "通过id查询扫描问题")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<ScanIssues> findScanIssues(@NotNull String id){
        ScanIssues scanIssues = scanIssuesService.findScanIssues(id);

        return Result.ok(scanIssues);
    }

    @RequestMapping(path="/findAllScanIssues",method = RequestMethod.POST)
    @ApiMethod(name = "findAllScanIssues",desc = "查询所有扫描问题")
    public Result<List<ScanIssues>> findAllScanIssues(){
        List<ScanIssues> scanIssuesList = scanIssuesService.findAllScanIssues();

        return Result.ok(scanIssuesList);
    }

    @RequestMapping(path = "/findScanIssuesList",method = RequestMethod.POST)
    @ApiMethod(name = "findScanIssuesList",desc = "条件查询扫描问题")
    @ApiParam(name = "scanIssuesQuery",desc = "scanIssuesQuery",required = true)
    public Result<List<ScanIssues>> findScanIssuesList(@RequestBody @Valid @NotNull ScanIssuesQuery scanIssuesQuery){
        List<ScanIssues> scanIssuesList = scanIssuesService.findScanIssuesList(scanIssuesQuery);

        return Result.ok(scanIssuesList);
    }

    @RequestMapping(path = "/findScanIssuesPage",method = RequestMethod.POST)
    @ApiMethod(name = "findScanIssuesPage",desc = "条件分页查询扫描问题")
    @ApiParam(name = "scanIssuesQuery",desc = "scanIssuesQuery",required = true)
    public Result<Pagination<ScanIssues>> findScanIssuesPage(@RequestBody @Valid @NotNull ScanIssuesQuery scanIssuesQuery){
        Pagination<ScanIssues> pagination = scanIssuesService.findScanIssuesPage(scanIssuesQuery);

        return Result.ok(pagination);
    }



}
