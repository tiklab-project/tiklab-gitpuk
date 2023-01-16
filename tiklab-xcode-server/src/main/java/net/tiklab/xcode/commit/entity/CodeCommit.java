package net.tiklab.xcode.commit.entity;

import net.tiklab.dal.jpa.annotation.*;

@Entity
@Table(name="code_branch")
public class CodeCommit {

    @Id
    @GeneratorValue
    @Column(name = "commit_id")
    private String commitId;

    @Column(name = "branch_id")
    private String branchId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "time")
    private String time;

    @Column(name = "message")
    private String message;

    @Column(name = "git_id")
    private String gitId;


    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGitId() {
        return gitId;
    }

    public void setGitId(String gitId) {
        this.gitId = gitId;
    }
}












































