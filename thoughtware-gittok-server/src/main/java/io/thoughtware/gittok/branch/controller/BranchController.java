package io.thoughtware.gittok.branch.controller;

import io.thoughtware.gittok.branch.model.Branch;
import io.thoughtware.gittok.branch.model.BranchMessage;
import io.thoughtware.gittok.branch.model.BranchQuery;
import io.thoughtware.gittok.branch.service.BranchServer;
import io.thoughtware.core.Result;
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

/**
 * @pi.protocol: http
 * @pi.groupName: branch
 */
@RestController
@RequestMapping("/branch")
@Api(name = "BranchController",desc = "分支")
public class BranchController {

    @Autowired
    BranchServer branchServer;

    /**
     * @pi.name:通过仓库id查询分支
     * @pi.path:/branch/findAllBranch
     * @pi.methodType:post
     * @pi.request-type:formdata
     * @pi.param:  name=rpyId;dataType=string;value=rpyId;
     */
    @RequestMapping(path="/findAllBranch",method = RequestMethod.POST)
    @ApiMethod(name = "findAllBranch",desc = "查询分支")
    @ApiParam(name = "rpyId",desc = "rpyId",required = true)
    public Result<List<Branch>> findAllBranch(@NotNull String rpyId){

        List<Branch> allBranch = branchServer.findAllBranch(rpyId);

        return Result.ok(allBranch);
    }

    /**
     * @pi.name: 条件查询分支
     * @pi.path:/branch/findBranchList
     * @pi.methodType:post
     * @pi.request-type:json
     * @pi.param: model=BranchQuery ;
     */
    @RequestMapping(path="/findBranchList",method = RequestMethod.POST)
    @ApiMethod(name = "findAllBranch",desc = "条件查询分支")
    @ApiParam(name = "branchQuery",desc = "branchQuery",required = true)
    public Result<List<Branch>> findBranchList(@RequestBody @Valid @NotNull BranchQuery branchQuery){

        List<Branch> allBranch = branchServer.findBranchList(branchQuery);

        return Result.ok(allBranch);
    }

    /**
     * @pi.name: 条件查询分支
     * @pi.path:/branch/findBranch
     * @pi.methodType:post
     * @pi.request-type:json
     * @pi.param: model=rpyId ;
     */
    @RequestMapping(path="/findBranch",method = RequestMethod.POST)
    @ApiMethod(name = "findBranch",desc = " 查询单个分支")
    @ApiParam(name = "rpyId",desc = "rpyId",required = true)
    public Result<Branch> findBranch(@NotNull String rpyId,@NotNull String commitId){

        Branch branch = branchServer.findBranch(rpyId, commitId);

        return Result.ok(branch);
    }

    /**
     * @pi.name: 创建分支
     * @pi.path:/branch/createBranch
     * @pi.methodType:post
     * @pi.request-type:json
     * @pi.param: model=BranchMessage ;
     */
    @RequestMapping(path="/createBranch",method = RequestMethod.POST)
    @ApiMethod(name = "createBranch",desc = "创建分支")
    @ApiParam(name = "branchMessage",desc = "branchMessage",required = true)
    public Result<Void> createBranch(@RequestBody @Valid @NotNull BranchMessage branchMessage){

         branchServer.createBranch(branchMessage);

        return Result.ok();
    }

    /**
     * @pi.name: 删除分支
     * @pi.path:/branch/deleteBranch
     * @pi.methodType:post
     * @pi.request-type:json
     * @pi.param: model=BranchMessage ;
     */
    @RequestMapping(path="/deleteBranch",method = RequestMethod.POST)
    @ApiMethod(name = "deleteBranch",desc = "删除分支")
    @ApiParam(name = "branchMessage",desc = "branchMessage",required = true)
    public Result<Void> deleteBranch(@RequestBody @Valid @NotNull BranchMessage branchMessage){

        branchServer.deleteBranch(branchMessage);

        return Result.ok();
    }

    /**
     * @pi.name: 修改默认分支
     * @pi.path:/branch/updateDefaultBranch
     * @pi.methodType:post
     * @pi.request-type:json
     * @pi.param: model=BranchQuery ;
     */
    @RequestMapping(path="/updateDefaultBranch",method = RequestMethod.POST)
    @ApiMethod(name = "updateDefaultBranch",desc = "修改默认分支")
    @ApiParam(name = "branchMessage",desc = "branchMessage",required = true)
    public Result<Void> updateDefaultBranch(@RequestBody @Valid @NotNull BranchQuery branchQuery){

        branchServer.updateDefaultBranch(branchQuery);

        return Result.ok();
    }

    /**
     * @pi.name: 合并分支
     * @pi.path:/branch/mergeBranch
     * @pi.methodType:post
     * @pi.request-type:json
     * @pi.param: model=BranchQuery ;
     */
    @RequestMapping(path="/mergeBranch",method = RequestMethod.POST)
    @ApiMethod(name = "mergeBranch",desc = "合并分支")
    @ApiParam(name = "branchMessage",desc = "branchMessage",required = true)
    public Result<Void> mergeBranch(@RequestBody @Valid @NotNull BranchQuery branchQuery){

        branchServer.mergeBranch(branchQuery);

        return Result.ok();
    }


}














































