package net.tiklab.xcode.branch.entity;

import net.tiklab.dal.jpa.annotation.*;

@Entity
@Table(name="code_branch")
public class CodeBranchEntity {

    @Id
    @GeneratorValue
    @Column(name = "branch_id")
    private String branchId;

    @Column(name = "branch_name")
    private String branchName;

    @Column(name = "code_id")
    private String codeId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "type")
    private int type;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "state")
    private int state;


    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
}












































