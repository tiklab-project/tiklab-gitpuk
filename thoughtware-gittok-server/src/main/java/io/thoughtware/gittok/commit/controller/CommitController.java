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
    @ApiMethod(name = "findBranchCommit",desc = "查询分支的提交记录")
    @ApiParam(name = "rpyId",desc = "rpyId",required = true)
    public Result<List<CommitMessage>> findBranchCommit(@RequestBody @Valid @NotNull Commit commit){

        List<CommitMessage> allBranch = commitServer.findBranchCommit(commit);

        return Result.ok(allBranch);
    }

    @RequestMapping(path="/findLatelyBranchCommit",method = RequestMethod.POST)
    @ApiMethod(name = "findLatelyBranchCommit",desc = "查询分支最近一次提交记录")
    @ApiParam(name = "rpyId",desc = "rpyId",required = true)
    public Result<CommitMessage> findLatelyBranchCommit(@RequestBody @Valid @NotNull Commit commit){

        CommitMessage allBranch = commitServer.findLatelyBranchCommit(commit);

        return Result.ok(allBranch);
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




    @RequestMapping(path="/findDiffBranchFileDetails",method = RequestMethod.POST)
    @ApiMethod(name = "findDiffBranchFile",desc = "查询仓库不同分支的提交差异文件详情")
    @ApiParam(name = "commit",desc = "commit",required = true)
    public Result<List<CommitFileDiff>> findDiffBranchFileDetails(@RequestBody @Valid @NotNull Commit commit){

        List<CommitFileDiff> fileDiff  = commitServer.findDiffBranchFileDetails(commit);

        return Result.ok(fileDiff);
    }

    @RequestMapping(path="/findStatisticsByBranchs",method = RequestMethod.POST)
    @ApiMethod(name = "findStatisticsByBranchs",desc = "查询不同分支或者不同commitId的提交差异的统计")
    @ApiParam(name = "commit",desc = "commit",required = true)
    public Result<CommitDiffData> findStatisticsByBranchs(@RequestBody @Valid @NotNull Commit commit){

        CommitDiffData commitDiffData  = commitServer.findStatisticsByBranchs(commit);

        return Result.ok(commitDiffData);
    }

    @RequestMapping(path="/findStatisticsByMergeId",method = RequestMethod.POST)
    @ApiMethod(name = "findStatisticsByMergeId",desc = "通过合并请求id 查询统计")
    @ApiParam(name = "mergeId",desc = "合并请求id",required = true)
    public Result<CommitDiffData> findStatisticsByMergeId(@Valid @NotNull String mergeId){

        CommitDiffData commitDiffData  = commitServer.findStatisticsByMergeId(mergeId);

        return Result.ok(commitDiffData);
    }

    @RequestMapping(path="/findDiffCommitByMergeId",method = RequestMethod.POST)
    @ApiMethod(name = "findCommitDiffByMergeId",desc = "通过合并请求id 查询差异提交")
    @ApiParam(name = "mergeId",desc = "合并请求Id",required = true)
    public Result<List<CommitMessage>> findDiffCommitByMergeId(@Valid @NotNull String mergeId){

        List<CommitMessage> allBranch = commitServer.findDiffCommitByMergeId(mergeId);

        return Result.ok(allBranch);
    }



    @RequestMapping(path="/findDiffFileByBranchs",method = RequestMethod.POST)
    @ApiMethod(name = "findDiffFileByBranchs",desc = "查询不同分支的提交差异文件")
    @ApiParam(name = "commit",desc = "commit",required = true)
    public Result<FileDiffEntry> findDiffFileByBranchs(@RequestBody @Valid @NotNull Commit commit){

        FileDiffEntry fileDiffList = commitServer.findDiffFileByBranchs(commit);

        return Result.ok(fileDiffList);
    }
    @RequestMapping(path="/findDiffFileByCommitId",method = RequestMethod.POST)
    @ApiMethod(name = "findDiffFileByCommitId",desc = "通过提交commitId 查询与父级的差异文件")
    @ApiParam(name = "commit",desc = "commitId",required = true)
    public Result<FileDiffEntry> findDiffFileByCommitId(@RequestBody @Valid @NotNull Commit commit){

        FileDiffEntry commitFileDiffList = commitServer.findDiffFileByCommitId(commit);

        return Result.ok(commitFileDiffList);
    }

    @RequestMapping(path="/findDiffFileByMergeId",method = RequestMethod.POST)
    @ApiMethod(name = "findDiffFileByMergeId",desc = "通过合并请求id 查询差异文件")
    @ApiParam(name = "commit",desc = "commit",required = true)
    public Result<FileDiffEntry> findDiffFileByMergeId(@Valid @NotNull String mergeId){

        FileDiffEntry fileDiffList = commitServer.findDiffFileByMergeId(mergeId);

        return Result.ok(fileDiffList);
    }

    @RequestMapping(path="/findCommitUserList",method = RequestMethod.POST)
    @ApiMethod(name = "findCommitUserList",desc = "通过仓库id查询提交的用户")
    @ApiParam(name = "repositoryId",desc = "repositoryId",required = true)
    public Result<String> findCommitUserList(@Valid @NotNull String repositoryId){

        List<String> commitUserList = commitServer.findCommitUserList(repositoryId);

        return Result.ok(commitUserList);
    }


    @RequestMapping(path="/findLatelyCommit",method = RequestMethod.POST)
    @ApiMethod(name = "findLatelyCommit",desc = "查询仓库最近提交")
    @ApiParam(name = "RepositoryId",desc = "RepositoryId、time",required = true)
    public Result<List<CommitMessage>> findLatelyCommit(@Valid @NotNull String repositoryId,Integer number){

        List<CommitMessage> commitMessageList = commitServer.findLatelyCommit(repositoryId,number);

        return Result.ok(commitMessageList);
    }
}
