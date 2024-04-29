package io.thoughtware.gittok.repository.entity;

import io.thoughtware.core.BaseModel;
import io.thoughtware.dal.jpa.annotation.*;

import java.sql.Timestamp;

/*
* 仓库收藏
* */
@Entity
@Table(name="rpy_repository_collect")
public class RepositoryCollectEntity extends BaseModel {

    @Id
    @GeneratorValue(length=12)
    @Column(name = "id")
    private String id;

    @Column(name = "repository_id")
    private String repositoryId;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}












































