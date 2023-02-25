package net.tiklab.xcode.branch.model;

public class Branch {

    //分支id
    private String branchId;

    //分支名称
    private String branchName;

    //更新时间
    private String updateTime;

    //类型
    private int type;

    //状态
    private int state;

    //默认分支
    private boolean defaultBranch;

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
}
