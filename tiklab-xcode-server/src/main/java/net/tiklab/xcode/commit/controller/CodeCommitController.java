package net.tiklab.xcode.commit.controller;

import net.tiklab.core.Result;
import net.tiklab.postin.annotation.Api;
import net.tiklab.postin.annotation.ApiMethod;
import net.tiklab.postin.annotation.ApiParam;
import net.tiklab.xcode.commit.model.CommitMessage;
import net.tiklab.xcode.commit.service.CodeCommitServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/codeCommit")
@Api(name = "CodeCommitController",desc = "提交记录")
public class CodeCommitController {

    @Autowired
    CodeCommitServer commitServer;

    @RequestMapping(path="/findBranchCommit",method = RequestMethod.POST)
    @ApiMethod(name = "findBranchCommit",desc = "查询提交记录")
    @ApiParam(name = "codeId",desc = "codeId",required = true)
    public Result<List<CommitMessage>> findBranchCommit(@NotNull String codeId,String branchName){

        List<CommitMessage> allBranch = commitServer.findBranchCommit(codeId,branchName);

        return Result.ok(allBranch);
    }

    @RequestMapping(path="/findLatelyBranchCommit",method = RequestMethod.POST)
    @ApiMethod(name = "findLatelyBranchCommit",desc = "最近一次提交记录")
    @ApiParam(name = "codeId",desc = "codeId",required = true)
    public Result<CommitMessage> findLatelyBranchCommit(@NotNull String codeId,String branchName){

        CommitMessage allBranch = commitServer.findLatelyBranchCommit(codeId,branchName);

        return Result.ok(allBranch);
    }



}









































