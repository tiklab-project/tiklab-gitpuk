package net.tiklab.xcode.commit.service;

import net.tiklab.xcode.commit.model.CommitMessage;

import java.util.List;

public interface CodeCommitServer {

    /**
     * 获取分支提交记录
     * @param codeId 仓库id
     * @param branchName 分支
     * @return 提交记录
     */
    List<CommitMessage> findBranchCommit(String codeId ,String branchName);


    /**
     * 获取最近一次的提交记录
     * @param codeId 仓库id
     * @param branchName 分支
     * @return 提交记录
     */
    CommitMessage findLatelyBranchCommit(String codeId, String branchName);




}
