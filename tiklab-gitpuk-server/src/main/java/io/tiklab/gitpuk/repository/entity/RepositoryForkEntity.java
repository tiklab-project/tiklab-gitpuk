package io.tiklab.gitpuk.repository.entity;

import io.tiklab.core.BaseModel;
import io.tiklab.dal.jpa.annotation.*;

import java.sql.Timestamp;

@Entity
@Table(name="rpy_repository_fork")
public class RepositoryForkEntity extends BaseModel {

    @Id
    @GeneratorValue(length=12)
    @Column(name = "id")
    private String id;

    @Column(name = "repository_id")
    private String repositoryId;

    @Column(name = "fork_repository_id")
    private String forkRepositoryId;

    @Column(name = "group_id")
    private String groupId;

    @Column(name = "user_id")
    private String userId;

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

    public String getForkRepositoryId() {
        return forkRepositoryId;
    }

    public void setForkRepositoryId(String forkRepositoryId) {
        this.forkRepositoryId = forkRepositoryId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}












































