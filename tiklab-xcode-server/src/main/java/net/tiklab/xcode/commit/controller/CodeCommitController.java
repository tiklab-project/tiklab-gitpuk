package net.tiklab.xcode.commit.controller;

import net.tiklab.core.Result;
import net.tiklab.postin.annotation.Api;
import net.tiklab.postin.annotation.ApiMethod;
import net.tiklab.postin.annotation.ApiParam;
import net.tiklab.xcode.commit.model.*;
import net.tiklab.xcode.commit.service.CodeCommitServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
    public Result<List<CommitMessage>> findBranchCommit(@RequestBody @Valid @NotNull Commit commit){

        List<CommitMessage> allBranch = commitServer.findBranchCommit(commit);

        return Result.ok(allBranch);
    }

    @RequestMapping(path="/findLatelyBranchCommit",method = RequestMethod.POST)
    @ApiMethod(name = "findLatelyBranchCommit",desc = "最近一次提交记录")
    @ApiParam(name = "codeId",desc = "codeId",required = true)
    public Result<CommitMessage> findLatelyBranchCommit(@RequestBody @Valid @NotNull Commit commit){

        CommitMessage allBranch = commitServer.findLatelyBranchCommit(commit);

        return Result.ok(allBranch);
    }


    @RequestMapping(path="/findCommitFileDiffList",method = RequestMethod.POST)
    @ApiMethod(name = "findCommitDiffFileList",desc = "文件提交对比")
    @ApiParam(name = "commit",desc = "commitId",required = true)
    public Result<FileDiffEntry> findCommitDiffFileList( @RequestBody @Valid @NotNull Commit commit){

        FileDiffEntry commitFileDiffList = commitServer.findCommitDiffFileList(commit);

        return Result.ok(commitFileDiffList);
    }

    @RequestMapping(path="/findCommitFileDiff",method = RequestMethod.POST)
    @ApiMethod(name = "findCommitFileDiff",desc = "文件具体内容信息")
    @ApiParam(name = "commit",desc = "commitId",required = true)
    public Result< List<CommitFileDiff>> findCommitDiffFile( @RequestBody @Valid @NotNull Commit commit){

        List<CommitFileDiff> fileDiff = commitServer.findCommitFileDiff(commit);

        return Result.ok(fileDiff);
    }


    @RequestMapping(path="/findCommitLineFile",method = RequestMethod.POST)
    @ApiMethod(name = "findCommitLineFile",desc = "文件具体内容信息")
    @ApiParam(name = "commit",desc = "commitId",required = true)
    public Result< List<CommitFileDiff>> findCommitDiffFile( @RequestBody @Valid @NotNull CommitFile commit){

        List<CommitFileDiff> fileDiff = commitServer.findCommitLineFile(commit);

        return Result.ok(fileDiff);
    }



}










































