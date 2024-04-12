package io.thoughtware.gittok.commit.entity;

import io.thoughtware.dal.jpa.annotation.*;

import java.sql.Timestamp;

/*
* 合并请求
* */
@Entity
@Table(name="rpy_merge_request")
public class MergeRequestEntity {
    @Id
    @GeneratorValue(length=12)
    @Column(name = "id")
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "rpy_id")
    private String rpyId;

    @Column(name = "merge_origin")
    private String mergeOrigin;

    @Column(name = "merge_target")
    private String mergeTarget;

    @Column(name = "create_user")
    private String createUser;

    @Column(name = "merge_state")
    private Integer mergeState;

    @Column(name = "is_clash")
    private Integer isClash;

    @Column(name = "value")
    private String value;

    @Column(name = "parent_commit")
    private String parentCommit;

    @Column(name = "current_commit")
    private String currentCommit;

    @Column(name = "create_time")
    private Timestamp createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRpyId() {
        return rpyId;
    }

    public void setRpyId(String rpyId) {
        this.rpyId = rpyId;
    }

    public String getMergeOrigin() {
        return mergeOrigin;
    }

    public void setMergeOrigin(String mergeOrigin) {
        this.mergeOrigin = mergeOrigin;
    }

    public String getMergeTarget() {
        return mergeTarget;
    }

    public void setMergeTarget(String mergeTarget) {
        this.mergeTarget = mergeTarget;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Integer getMergeState() {
        return mergeState;
    }

    public void setMergeState(Integer mergeState) {
        this.mergeState = mergeState;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getIsClash() {
        return isClash;
    }

    public void setIsClash(Integer isClash) {
        this.isClash = isClash;
    }

    public String getParentCommit() {
        return parentCommit;
    }

    public void setParentCommit(String parentCommit) {
        this.parentCommit = parentCommit;
    }

    public String getCurrentCommit() {
        return currentCommit;
    }

    public void setCurrentCommit(String currentCommit) {
        this.currentCommit = currentCommit;
    }
}
