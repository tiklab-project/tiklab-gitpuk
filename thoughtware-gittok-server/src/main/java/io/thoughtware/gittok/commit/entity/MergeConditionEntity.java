package io.thoughtware.gittok.commit.entity;

import io.thoughtware.dal.jpa.annotation.*;

import java.sql.Timestamp;

/*
* 合并请求动态
* */
@Entity
@Table(name="rpy_merge_condition")
public class MergeConditionEntity {
    @Id
    @GeneratorValue(length=12)
    @Column(name = "id")
    private String id;

    @Column(name = "merge_request_id",notNull = true)
    private String mergeRequestId;

    @Column(name = "repository_id",notNull = true)
    private String repositoryId;


    @Column(name = "type",notNull = true)
    private String type;

    @Column(name = "user_id",notNull = true)
    private String userId;

    @Column(name = "data")
    private String data;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }
}
