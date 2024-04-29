package io.thoughtware.gittok.branch.model;

import io.thoughtware.gittok.commit.model.MergeRequest;

import java.io.Serializable;
import java.util.List;

/**
 *  @pi.model: io.thoughtware.gittok.branch.model.Branch
 *  裸仓库中的分支
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
     * @pi.desc: 创建人
     * @pi.value: updateUser
     */
    private String createUser;

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

    /**
     * @pi.name: lagNum
     * @pi.dataType:int
     * @pi.desc: 滞后数量
     * @pi.value: lagNum
     */
    private int lagNum;


    /**
     * @pi.name: advanceNum
     * @pi.dataType:int
     * @pi.desc: 超前数量
     * @pi.value: advanceNum
     */
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
