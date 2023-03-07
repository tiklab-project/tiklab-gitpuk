package io.tiklab.xcode.commit.model;

import java.util.Date;
import java.util.List;

/**
 * 提交信息
 */
public class CommitMessage {

    /**
     * commitId
     */
    private String commitId;

    /**
     * 提交信息
     */
    private String commitMessage;

    /**
     * 提交人
     */
    private String commitUser;

    /**
     * 提交时间
     */
    private String commitTime;

    /**
     * 提交时间
     */
    private Date dateTime;

    /**
     * 提交记录
     */
    private List<CommitMessage> commitMessageList;


    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public String getCommitMessage() {
        return commitMessage;
    }

    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

    public String getCommitUser() {
        return commitUser;
    }

    public void setCommitUser(String commitUser) {
        this.commitUser = commitUser;
    }

    public String getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(String commitTime) {
        this.commitTime = commitTime;
    }

    public List<CommitMessage> getCommitMessageList() {
        return commitMessageList;
    }

    public void setCommitMessageList(List<CommitMessage> commitMessageList) {
        this.commitMessageList = commitMessageList;
    }
}
