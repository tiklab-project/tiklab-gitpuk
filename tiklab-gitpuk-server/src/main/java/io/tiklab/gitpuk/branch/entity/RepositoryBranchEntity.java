package io.tiklab.gitpuk.branch.entity;

import io.tiklab.dal.jpa.annotation.*;

import java.sql.Timestamp;

/*
* 仓库分支
* */
@Entity
@Table(name="rpy_repository_branch")
public class RepositoryBranchEntity {
    @Id
    @GeneratorValue(length=12)
    @Column(name = "id")
    private String id;

    @Column(name = "repository_id")
    private String repositoryId;

    @Column(name = "branch_name")
    private String branchName;

    @Column(name = "branch_id")
    private String branchId;

    @Column(name = "create_user")
    private String createUser;

    @Column(name = "create_time")
    private Timestamp createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
