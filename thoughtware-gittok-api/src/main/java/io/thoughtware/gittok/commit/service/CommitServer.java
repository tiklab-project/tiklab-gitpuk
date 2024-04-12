package io.thoughtware.gittok.commit.service;

import io.thoughtware.gittok.commit.model.*;

import java.util.List;

public interface CommitServer {

    /**
     * 获取分支提交记录
     * @param commit 仓库id
     * @return 提交记录
     */
    List<CommitMessage> findBranchCommit(Commit commit);


    /**
     * 获取最近一次的提交记录
     * @param commit 仓库id
     * @return 提交记录
     */
    CommitMessage findLatelyBranchCommit(Commit commit);

    /**
     * 通过当前commitId和父级commitId查询文件提交对比 (获取两个commitId的提交差异文件对比  )
     * @param commit commitId
     * @return 文件列表
     */
    FileDiffEntry findCommitDiffFileList(Commit commit);

    /**
     * 提交文件模糊查询
     * @param commit 查询信息
     * @return 查询结果
     */
    FileDiffEntry findLikeCommitDiffFileList(Commit commit);

    /**
     * 查询具体文件两次提交的差异
     * @param commit commitId
     * @return 文件列表
     */
    List<CommitFileDiff> findCommitFileDiff(Commit commit);

    /**
     * 读取指定提交下的指定文件的指定行数
     * @param commit 提交信息
     * @return 文件内容
     */
    List<CommitFileDiff> findCommitLineFile(CommitFile commit);

    /**
     * 查询仓库不同分支差异的提交
     * @param commit 提交信息
     * @return 文件内容
     */
    List<CommitMessage> findCommitDiffBranch(Commit commit);

    /**
     * 查询仓库不同分支差异的文件
     * @param commit 提交信息
     * @return 文件内容
     */
    FileDiffEntry findDiffBranchFile(Commit commit);

    /**
     * 查询仓库不同分支差异的文件的详情
     * @param commit 提交信息
     * @return 文件内容
     */
    List<CommitFileDiff> findDiffBranchFileDetails(Commit commit);

    /**
     * 查询不同分支或者不同commitId的提交差异的统计
     * @param commit 提交信息
     * @return 文件内容
     */
    CommitDiffData findDiffCommitStatistics(Commit commit);
}






































