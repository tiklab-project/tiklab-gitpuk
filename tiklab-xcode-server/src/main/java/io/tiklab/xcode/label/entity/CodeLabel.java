package io.tiklab.xcode.label.entity;

import io.tiklab.dal.jpa.annotation.*;

@Entity
@Table(name="code_branch")
public class CodeLabel {

    @Id
    @GeneratorValue
    @Column(name = "label_id")
    private String labelId;

    @Column(name = "branch_id")
    private String branchId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "create_time")
    private String time;

    @Column(name = "title")
    private String title;

    @Column(name = "remarks")
    private String remarks;


    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}












































