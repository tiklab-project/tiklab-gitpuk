package io.thoughtware.gittok.commit.controller;

import io.thoughtware.gittok.commit.model.*;
import io.thoughtware.gittok.commit.service.CommitServer;
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

@RestController
@RequestMapping("/commit")
@Api(name = "CommitController",desc = "提交记录")
public class CommitController {

    @Autowired
    CommitServer commitServer;

    @RequestMapping(path="/findBranchCommit",method = RequestMethod.POST)
    @ApiMethod(name = "findBranchCommit",desc = "查询提交记录")
    @ApiParam(name = "rpyId",desc = "rpyId",required = true)
    public Result<List<CommitMessage>> findBranchCommit(@RequestBody @Valid @NotNull Commit commit){

        List<CommitMessage> allBranch = commitServer.findBranchCommit(commit);

        return Result.ok(allBranch);
    }

    @RequestMapping(path="/findLatelyBranchCommit",method = RequestMethod.POST)
    @ApiMethod(name = "findLatelyBranchCommit",desc = "最近一次提交记录")
    @ApiParam(name = "rpyId",desc = "rpyId",required = true)
    public Result<CommitMessage> findLatelyBranchCommit(@RequestBody @Valid @NotNull Commit commit){

        CommitMessage allBranch = commitServer.findLatelyBranchCommit(commit);

        return Result.ok(allBranch);
    }


    @RequestMapping(path="/findCommitFileDiffList",method = RequestMethod.POST)
    @ApiMethod(name = "findCommitDiffFileList",desc = "获取两个commitId的提交差异文件对比")
    @ApiParam(name = "commit",desc = "commitId",required = true)
    public Result<FileDiffEntry> findCommitDiffFileList(@RequestBody @Valid @NotNull Commit commit){

        FileDiffEntry commitFileDiffList = commitServer.findCommitDiffFileList(commit);

        return Result.ok(commitFileDiffList);
    }

    @RequestMapping(path="/findLikeCommitDiffFileList",method = RequestMethod.POST)
    @ApiMethod(name = "findLikeCommitDiffFileList",desc = "模糊查询两个提交之前的差异文件")
    @ApiParam(name = "commit",desc = "commitId",required = true)
    public Result<FileDiffEntry> findLikeCommitDiffFileList(@RequestBody @Valid @NotNull Commit commit){

        FileDiffEntry commitFileDiffList = commitServer.findLikeCommitDiffFileList(commit);

        return Result.ok(commitFileDiffList);
    }


    @RequestMapping(path="/findCommitFileDiff",method = RequestMethod.POST)
    @ApiMethod(name = "findCommitFileDiff",desc = "查询具体文件两次提交的差异")
    @ApiParam(name = "commit",desc = "commitId",required = true)
    public Result< List<CommitFileDiff>> findCommitDiffFile(@RequestBody @Valid @NotNull Commit commit){

        List<CommitFileDiff> fileDiff = commitServer.findCommitFileDiff(commit);

        return Result.ok(fileDiff);
    }




    @RequestMapping(path="/findCommitLineFile",method = RequestMethod.POST)
    @ApiMethod(name = "findCommitLineFile",desc = "查询提交文件具体内容信息")
    @ApiParam(name = "commit",desc = "commitId",required = true)
    public Result< List<CommitFileDiff>> findCommitDiffFile( @RequestBody @Valid @NotNull CommitFile commit){

        List<CommitFileDiff> fileDiff = commitServer.findCommitLineFile(commit);

        return Result.ok(fileDiff);
    }



    @RequestMapping(path="/findCommitDiffBranch",method = RequestMethod.POST)
    @ApiMethod(name = "findCommitNoBranch",desc = "查询仓库不同分支差异提交")
    @ApiParam(name = "commit",desc = "commit",required = true)
    public Result<List<CommitMessage>> findCommitDiffBranch(@RequestBody @Valid @NotNull Commit commit){

        List<CommitMessage> allBranch = commitServer.findCommitDiffBranch(commit);

        return Result.ok(allBranch);
    }

    @RequestMapping(path="/findDiffBranchFile",method = RequestMethod.POST)
    @ApiMethod(name = "findDiffBranchFile",desc = "查询仓库不同分支的提交差异文件")
    @ApiParam(name = "commit",desc = "commit",required = true)
    public Result<FileDiffEntry> findDiffBranchFile(@RequestBody @Valid @NotNull Commit commit){

        FileDiffEntry fileDiffList = commitServer.findDiffBranchFile(commit);

        return Result.ok(fileDiffList);
    }

    @RequestMapping(path="/findDiffBranchFileDetails",method = RequestMethod.POST)
    @ApiMethod(name = "findDiffBranchFile",desc = "查询仓库不同分支的提交差异文件详情")
    @ApiParam(name = "commit",desc = "commit",required = true)
    public Result<List<CommitFileDiff>> findDiffBranchFileDetails(@RequestBody @Valid @NotNull Commit commit){

        List<CommitFileDiff> fileDiff  = commitServer.findDiffBranchFileDetails(commit);

        return Result.ok(fileDiff);
    }

    @RequestMapping(path="/findDiffCommitStatistics",method = RequestMethod.POST)
    @ApiMethod(name = "findDiffCommitStatistics",desc = "查询不同分支或者不同commitId的提交差异的统计")
    @ApiParam(name = "commit",desc = "commit",required = true)
    public Result<CommitDiffData> findDiffCommitStatistics(@RequestBody @Valid @NotNull Commit commit){

        CommitDiffData commitDiffData  = commitServer.findDiffCommitStatistics(commit);

        return Result.ok(commitDiffData);
    }

}










































