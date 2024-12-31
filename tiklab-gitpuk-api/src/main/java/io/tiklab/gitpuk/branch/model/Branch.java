package io.tiklab.gitpuk.branch.model;

import io.tiklab.gitpuk.merge.model.MergeRequest;

import java.io.Serializable;

/**
 *  裸仓库中的分支
 */
public class Branch implements Serializable {


    private String branchId;


    private String branchName;


    private String createUser;


    private String updateUser;


    private String updateTime;


    private int type;

   //状态
    private int state;


    //默认分支
    private boolean defaultBranch;


    // 滞后数量
    private int lagNum;


    // 超前数量
    private int advanceNum;

    MergeRequest mergeRequest;


    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(boolean defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public int getLagNum() {
        return lagNum;
    }

    public void setLagNum(int lagNum) {
        this.lagNum = lagNum;
    }

    public int getAdvanceNum() {
        return advanceNum;
    }

    public void setAdvanceNum(int advanceNum) {
        this.advanceNum = advanceNum;
    }


    public MergeRequest getMergeRequest() {
        return mergeRequest;
    }

    public void setMergeRequest(MergeRequest mergeRequest) {
        this.mergeRequest = mergeRequest;
    }
}
