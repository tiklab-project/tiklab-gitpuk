package net.tiklab.xcode.commit.model;

import java.util.List;

public class CommitMessage {

    private String commitId;

    private String commitMessage;

    private String commitUser;

    private String commitTime;

    private String dateTime;

    List<CommitMessage> list;

    public List<CommitMessage> getList() {
        return list;
    }

    public void setList(List<CommitMessage> list) {
        this.list = list;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
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
}
