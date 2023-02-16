package net.tiklab.xcode.commit.service;

import net.tiklab.xcode.commit.model.Commit;
import net.tiklab.xcode.commit.model.CommitMessage;

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




}
