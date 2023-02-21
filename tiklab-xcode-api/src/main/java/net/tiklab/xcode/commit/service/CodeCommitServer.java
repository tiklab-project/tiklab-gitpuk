package net.tiklab.xcode.commit.service;

import net.tiklab.xcode.commit.model.*;

import java.util.List;

public interface CodeCommitServer {

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
     * 获取提交的文件信息
     * @param commit commitId
     * @return 文件列表
     */
    FileDiffEntry findCommitDiffFileList(Commit commit);

    /**
     * 获取提交的具体文件的文件内容
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

}






































