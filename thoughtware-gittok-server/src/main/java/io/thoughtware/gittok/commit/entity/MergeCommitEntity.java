package io.thoughtware.gittok.commit.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.thoughtware.dal.jpa.annotation.*;
import io.thoughtware.postin.annotation.ApiProperty;

import java.sql.Timestamp;

/*
* 合并完成后的差异commitId
* */
@Entity
@Table(name="rpy_merge_commit")
public class MergeCommitEntity {
    @Id
    @GeneratorValue(length=12)
    @Column(name = "id")
    private String id;


    @Column(name = "merge_request_id")
    private String mergeRequestId;

    @Column(name = "repository_id")
    private String repositoryId;

    @Column(name = "commit_id")
    private String commitId;

    @Column(name = "commit_time")
    private Timestamp commitTime;

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

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(Timestamp commitTime) {
        this.commitTime = commitTime;
    }
}
