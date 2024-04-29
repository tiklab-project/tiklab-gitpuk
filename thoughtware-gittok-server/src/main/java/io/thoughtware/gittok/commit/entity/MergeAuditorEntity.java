package io.thoughtware.gittok.commit.entity;

import io.thoughtware.dal.jpa.annotation.*;

import java.sql.Timestamp;

/*
* 合并请求审核人
* */
@Entity
@Table(name="rpy_merge_auditor")
public class MergeAuditorEntity {
    @Id
    @GeneratorValue(length=12)
    @Column(name = "id")
    private String id;


    @Column(name = "merge_request_id")
    private String mergeRequestId;

    @Column(name = "repository_id")
    private String repositoryId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "audit_status")
    private Integer auditStatus;

    @Column(name = "create_time")
    private Timestamp createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMergeRequestId() {
        return mergeRequestId;
    }

    public void setMergeRequestId(String mergeRequestId) {
        this.mergeRequestId = mergeRequestId;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
