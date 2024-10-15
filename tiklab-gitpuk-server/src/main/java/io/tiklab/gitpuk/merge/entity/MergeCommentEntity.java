package io.tiklab.gitpuk.merge.entity;

import io.tiklab.dal.jpa.annotation.*;

import java.sql.Timestamp;

/*
* 合并请求
* */
@Entity
@Table(name="rpy_merge_comment")
public class MergeCommentEntity {
    @Id
    @GeneratorValue(length=12)
    @Column(name = "id")
    private String id;

    @Column(name = "merge_condition_id")
    private String mergeConditionId;

    @Column(name = "merge_request_id")
    private String mergeRequestId;

    @Column(name = "repository_id")
    private String repositoryId;

    @Column(name = "comment_user_id")
    private String commentUserId;

    @Column(name = "reply_user_id")
    private String replyUserId;

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

    public String getMergeConditionId() {
        return mergeConditionId;
    }

    public void setMergeConditionId(String mergeConditionId) {
        this.mergeConditionId = mergeConditionId;
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

    public String getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(String commentUserId) {
        this.commentUserId = commentUserId;
    }

    public String getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(String replyUserId) {
        this.replyUserId = replyUserId;
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
}
