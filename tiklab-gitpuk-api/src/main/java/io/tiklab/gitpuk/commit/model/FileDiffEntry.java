package io.tiklab.gitpuk.commit.model;

import java.util.List;

public class FileDiffEntry {

    /**
     * 提交文件
     */
    List<CommitFileDiffList> diffList;

    /**
     * 提交时间
     */
    private String commitTime;

    /**
     * 提交人
     */
    private String commitUser;

    /**
     * 提交信息
     */
    private String commitMessage;

    /**
     * 父commitId
     */
    private List<String> parentCommitIds;

    /**
     * 添加行
     */
    private int addLine;

    /**
     * 删除行
     */
    private  int deleteLine;




    public String getCommitMessage() {
        return commitMessage;
    }

    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

    public List<CommitFileDiffList> getDiffList() {
        return diffList;
    }

    public void setDiffList(List<CommitFileDiffList> diffList) {
        this.diffList = diffList;
    }

    public String getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(String commitTime) {
        this.commitTime = commitTime;
    }

    public String getCommitUser() {
        return commitUser;
    }

    public void setCommitUser(String commitUser) {
        this.commitUser = commitUser;
    }

    public int getAddLine() {
        return addLine;
    }

    public void setAddLine(int addLine) {
        this.addLine = addLine;
    }

    public int getDeleteLine() {
        return deleteLine;
    }

    public void setDeleteLine(int deleteLine) {
        this.deleteLine = deleteLine;
    }

    public List<String> getParentCommitIds() {
        return parentCommitIds;
    }

    public void setParentCommitIds(List<String> parentCommitIds) {
        this.parentCommitIds = parentCommitIds;
    }

}
