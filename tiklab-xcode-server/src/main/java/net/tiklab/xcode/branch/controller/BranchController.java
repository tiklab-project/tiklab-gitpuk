package net.tiklab.xcode.branch.controller;

import net.tiklab.core.Result;
import net.tiklab.postin.annotation.Api;
import net.tiklab.postin.annotation.ApiMethod;
import net.tiklab.postin.annotation.ApiParam;
import net.tiklab.xcode.branch.model.BranchMessage;
import net.tiklab.xcode.branch.model.Branch;
import net.tiklab.xcode.branch.service.BranchServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/codeBranch")
@Api(name = "BranchController",desc = "分支")
public class BranchController {

    @Autowired
    BranchServer branchServer;

    @RequestMapping(path="/findAllBranch",method = RequestMethod.POST)
    @ApiMethod(name = "findAllBranch",desc = "查询分支")
    @ApiParam(name = "codeId",desc = "codeId",required = true)
    public Result<List<Branch>> findAllBranch(@NotNull String codeId){

        List<Branch> allBranch = branchServer.findAllBranch(codeId);

        return Result.ok(allBranch);
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



}














































