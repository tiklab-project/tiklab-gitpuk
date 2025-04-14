package io.tiklab.gitpuk.branch.controller;

import io.tiklab.gitpuk.branch.model.Branch;
import io.tiklab.gitpuk.branch.model.BranchMessage;
import io.tiklab.gitpuk.branch.model.BranchQuery;
import io.tiklab.gitpuk.branch.service.BranchServer;
import io.tiklab.core.Result;
import io.tiklab.postin.annotation.Api;
import io.tiklab.postin.annotation.ApiMethod;
import io.tiklab.postin.annotation.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;


@RestController
@RequestMapping("/branch")
@Api(name = "BranchController",desc = "裸仓库中的分支管理")
public class BranchController {

    @Autowired
    BranchServer branchServer;


    @RequestMapping(path="/findAllBranch",method = RequestMethod.POST)
    @ApiMethod(name = "findAllBranch",desc = "查询分支")
    @ApiParam(name = "rpyId",desc = "rpyId",required = true)
    public Result<List<Branch>> findAllBranch(@NotNull String rpyId){

        List<Branch> allBranch = branchServer.findAllBranchByRpyId(rpyId);

        return Result.ok(allBranch);
    }


    @RequestMapping(path="/findBranchList",method = RequestMethod.POST)
    @ApiMethod(name = "findBranchList",desc = "条件查询分支")
    @ApiParam(name = "branchQuery",desc = "branchQuery",required = true)
    public Result<List<Branch>> findBranchList(@RequestBody @Valid @NotNull BranchQuery branchQuery){

        List<Branch> allBranch = branchServer.findBranchList(branchQuery);


        return Result.ok(allBranch);
    }


    @RequestMapping(path="/findBranch",method = RequestMethod.POST)
    @ApiMethod(name = "findBranch",desc = " 查询单个分支")
    @ApiParam(name = "commitId",desc = "commitId",required = true)
    public Result<Branch> findBranch(@NotNull String rpyId,@NotNull String commitId){

        Branch branch = branchServer.findBranch(rpyId, commitId);

        return Result.ok(branch);
    }


    @RequestMapping(path="/createBranch",method = RequestMethod.POST)
    @ApiMethod(name = "createBranch",desc = "创建分支")
    @ApiParam(name = "branchMessage",desc = "branchMessage",required = true)
    public Result<Void> createBranch(@RequestBody @Valid @NotNull BranchMessage branchMessage){

         branchServer.createBranch(branchMessage);

        return Result.ok();
    }


    @RequestMapping(path="/deleteBranch",method = RequestMethod.POST)
    @ApiMethod(name = "deleteBranch",desc = "删除分支")
    @ApiParam(name = "branchMessage",desc = "branchMessage",required = true)
    public Result<Void> deleteBranch(@RequestBody @Valid @NotNull BranchMessage branchMessage){

        branchServer.deleteBranch(branchMessage);

        return Result.ok();
    }


    @RequestMapping(path="/updateDefaultBranch",method = RequestMethod.POST)
    @ApiMethod(name = "updateDefaultBranch",desc = "修改默认分支")
    @ApiParam(name = "branchQuery",desc = "branchQuery",required = true)
    public Result<Void> updateDefaultBranch(@RequestBody @Valid @NotNull BranchQuery branchQuery){

        branchServer.updateDefaultBranch(branchQuery);

        return Result.ok();
    }


    @RequestMapping(path="/mergeBranch",method = RequestMethod.POST)
    @ApiMethod(name = "mergeBranch",desc = "合并分支")
    @ApiParam(name = "branchQuery",desc = "branchQuery",required = true)
    public Result<Void> mergeBranch(@RequestBody @Valid @NotNull BranchQuery branchQuery){

        branchServer.mergeBranch(branchQuery);

        return Result.ok();
    }
}