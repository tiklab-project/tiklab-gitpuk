package net.tiklab.xcode.commit.service;

import net.tiklab.core.exception.ApplicationException;
import net.tiklab.xcode.code.model.Code;
import net.tiklab.xcode.code.service.CodeServer;
import net.tiklab.xcode.code.service.CodeServerImpl;
import net.tiklab.xcode.commit.model.CommitMessage;
import net.tiklab.xcode.git.GitCommitUntil;
import net.tiklab.xcode.until.CodeUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class CodeCommitServerImpl implements CodeCommitServer {

    @Autowired
    CodeServer codeServer;

    /**
     * 获取分支提交记录
     * @param codeId 仓库id
     * @param branchName 分支
     * @return 提交记录
     */
    @Override
    public List<CommitMessage> findBranchCommit(String codeId, String branchName) {
        Code code = codeServer.findOneCode(codeId);
        String repositoryAddress = CodeUntil.findRepositoryAddress(code.getAddress(), code.getCodeGroup());
        List<CommitMessage> branchCommit;
        try {
            branchCommit = GitCommitUntil.findBranchCommit(repositoryAddress+".git", branchName);
        } catch (IOException e) {
            throw new ApplicationException("提交记录获取失败："+e);
        }
        return commitSort(branchCommit, new ArrayList<>());
    }

    /**
     * 获取最近一次的提交记录
     * @param codeId 仓库id
     * @param branchName 分支
     * @return 提交记录
     */
    @Override
    public CommitMessage findLatelyBranchCommit(String codeId, String branchName) {
        List<CommitMessage> branchCommit = findBranchCommit(codeId, branchName);
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









































