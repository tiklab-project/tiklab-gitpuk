package io.tiklab.gitpuk.merge.controller;

import io.tiklab.core.Result;
import io.tiklab.core.page.Pagination;
import io.tiklab.gitpuk.merge.model.MergeCommit;
import io.tiklab.gitpuk.merge.model.MergeCommitQuery;
import io.tiklab.gitpuk.merge.service.MergeCommitService;
import io.tiklab.postin.annotation.Api;
import io.tiklab.postin.annotation.ApiMethod;
import io.tiklab.postin.annotation.ApiParam;
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
 * MergeCommitController
 */
@RestController
@RequestMapping("/mergeCommit")
@Api(name = "MergeCommitController",desc = "合并请求的分支差异提交")
public class MergeCommitController {

    private static Logger logger = LoggerFactory.getLogger(MergeCommitController.class);

    @Autowired
    private MergeCommitService mergeCommitService;

    @RequestMapping(path="/createMergeCommit",method = RequestMethod.POST)
    @ApiMethod(name = "createMergeCommit",desc = "添加合并请求的分支差异提交")
    @ApiParam(name = "mergeCommit",desc = "mergeCommit",required = true)
    public Result<String> createMergeCommit(@RequestBody @Valid @NotNull MergeCommit mergeCommit){
        String mergeCommitId = mergeCommitService.createMergeCommit(mergeCommit);

        return Result.ok(mergeCommitId);
    }

    @RequestMapping(path="/updateMergeCommit",method = RequestMethod.POST)
    @ApiMethod(name = "updateMergeCommit",desc = "更新合并请求的分支差异提交")
    @ApiParam(name = "mergeCommit",desc = "mergeCommit",required = true)
    public Result<String> updateMergeCommit(@RequestBody @Valid @NotNull MergeCommit mergeCommit){
         mergeCommitService.updateMergeCommit(mergeCommit);

        return Result.ok();
    }


    @RequestMapping(path="/deleteMergeCommit",method = RequestMethod.POST)
    @ApiMethod(name = "deleteMergeCommit",desc = "删除合并请求的分支差异提交")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<Void> deleteMergeCommit(@NotNull String id){
        mergeCommitService.deleteMergeCommit(id);

        return Result.ok();
    }

    @RequestMapping(path="/findMergeCommit",method = RequestMethod.POST)
    @ApiMethod(name = "findMergeCommit",desc = "通过id查询合并请求的分支差异提交")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<MergeCommit> findMergeCommit(@NotNull String id){
        MergeCommit mergeCommit = mergeCommitService.findMergeCommit(id);

        return Result.ok(mergeCommit);
    }

    @RequestMapping(path="/findAllMergeCommit",method = RequestMethod.POST)
    @ApiMethod(name = "findAllMergeCommit",desc = "查询所有合并请求的分支差异提交")
    public Result<List<MergeCommit>> findAllMergeCommit(){
        List<MergeCommit> mergeCommitList = mergeCommitService.findAllMergeCommit();

        return Result.ok(mergeCommitList);
    }

    @RequestMapping(path = "/findMergeCommitList",method = RequestMethod.POST)
    @ApiMethod(name = "findMergeCommitList",desc = "条件查询合并请求的分支差异提交")
    @ApiParam(name = "mergeCommitQuery",desc = "mergeCommitQuery",required = true)
    public Result<List<MergeCommit>> findMergeCommitList(@RequestBody @Valid @NotNull MergeCommitQuery mergeCommitQuery){
        List<MergeCommit> mergeCommitList = mergeCommitService.findMergeCommitList(mergeCommitQuery);

        return Result.ok(mergeCommitList);
    }

    @RequestMapping(path = "/findMergeCommitPage",method = RequestMethod.POST)
    @ApiMethod(name = "findMergeCommitPage",desc = "条件分页查询合并请求的分支差异提交")
    @ApiParam(name = "mergeCommitQuery",desc = "mergeCommitQuery",required = true)
    public Result<Pagination<MergeCommit>> findMergeCommitPage(@RequestBody @Valid @NotNull MergeCommitQuery mergeCommitQuery){
        Pagination<MergeCommit> pagination = mergeCommitService.findMergeCommitPage(mergeCommitQuery);

        return Result.ok(pagination);
    }

}
