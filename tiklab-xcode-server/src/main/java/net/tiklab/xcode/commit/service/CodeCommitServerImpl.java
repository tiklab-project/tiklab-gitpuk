package net.tiklab.xcode.commit.service;

import net.tiklab.core.exception.ApplicationException;
import net.tiklab.xcode.code.model.Code;
import net.tiklab.xcode.code.service.CodeServer;
import net.tiklab.xcode.code.service.CodeServerImpl;
import net.tiklab.xcode.commit.model.Commit;
import net.tiklab.xcode.commit.model.CommitMessage;
import net.tiklab.xcode.git.GitCommitUntil;
import net.tiklab.xcode.until.CodeFinal;
import net.tiklab.xcode.until.CodeUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class CodeCommitServerImpl implements CodeCommitServer {

    @Autowired
    CodeServer codeServer;

    /**
     * 获取分支提交记录
     * @param commit 信息
     * @return 提交记录
     */
    @Override
    public List<CommitMessage> findBranchCommit(Commit commit) {
        String codeId = commit.getCodeId();
        String branch = commit.getBranch();
        Code code = codeServer.findOneCode(codeId);
        String repositoryAddress = CodeUntil.findRepositoryAddress(code, CodeFinal.TRUE);
        List<CommitMessage> branchCommit;
        try {
            branchCommit = GitCommitUntil.findBranchCommit(repositoryAddress, branch,commit.isFindCommitId());
        } catch (IOException e) {
            throw new ApplicationException("提交记录获取失败："+e);
        }
        if (branchCommit.isEmpty()){
           return Collections.emptyList();
        }
        return commitSort(branchCommit, new ArrayList<>());
    }

    /**
     * 获取最近一次的提交记录
     * @param commit 仓库id
     * @return 提交记录
     */
    @Override
    public CommitMessage findLatelyBranchCommit(Commit commit) {
        List<CommitMessage> branchCommit = findBranchCommit(commit);
        if (branchCommit.isEmpty()){
            return null;
        }
        return branchCommit.get(0).getCommitMessageList().get(0);
    }



    /**
     * 提交记录根据日期排序（按天）
     * @param branchCommit 提交记录
     * @param list 每天的提交记录
     * @return 提交记录
     */
    private List<CommitMessage> commitSort(List<CommitMessage> branchCommit, List<CommitMessage> list){
        List<CommitMessage> removeList = new ArrayList<>();
        CommitMessage commitMessage = new CommitMessage();
        Date date = branchCommit.get(0).getDateTime();
        String time= CodeUntil.date(2, date);
        commitMessage.setCommitTime(time);
        for (CommitMessage message : branchCommit) {
            Date dateTime = message.getDateTime();
            if (!time.equals(CodeUntil.date(2, dateTime))) {
                continue;
            }
            removeList.add(message);
        }
        commitMessage.setCommitMessageList(removeList);
        list.add(commitMessage);
        branchCommit.removeAll(removeList);
        if (branchCommit.size() != 0){
            commitSort(branchCommit,list);
        }
        return list;
    }



}









































