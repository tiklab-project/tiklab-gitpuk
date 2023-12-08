package io.thoughtware.gittork.branch.model;

import java.io.Serializable;

/**
 *  @pi.model: io.thoughtware.gittork.branch.model.Branch
 */
public class Branch implements Serializable {

    /**
     * @pi.name: branchId
     * @pi.dataType:string
     * @pi.desc: 分支id
     * @pi.value: branchId
     */
    private String branchId;


    /**
     * @pi.name: branchName
     * @pi.dataType:string
     * @pi.desc: 分支名称
     * @pi.value: branchName
     */
    private String branchName;

    /**
     * @pi.name: updateUser
     * @pi.dataType:string
     * @pi.desc: 更新人
     * @pi.value: updateUser
     */

    private String updateUser;

    /**
     * @pi.name: updateTime
     * @pi.dataType:string
     * @pi.desc: 更新时间
     * @pi.value: updateTime
     */

    private String updateTime;

    /**
     * @pi.name: type
     * @pi.dataType:int
     * @pi.desc: 类型
     * @pi.value: type
     */
    private int type;

    /**
     * @pi.name: state
     * @pi.dataType:int
     * @pi.desc: 状态
     * @pi.value: state
     */
    private int state;

    /**
     * @pi.name: defaultBranch
     * @pi.dataType:boolean
     * @pi.desc: 默认分支
     * @pi.value: defaultBranch
     */
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

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
