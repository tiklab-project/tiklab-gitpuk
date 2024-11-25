package io.tiklab.gitpuk.repository.entity;

import io.tiklab.dal.jpa.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name="rpy_web_hook")
public class RepWebHookEntity {

    @Id
    @GeneratorValue(length=12)
    @Column(name = "id")
    private String id;

    @Column(name = "repository_id")
    private String repositoryId;


    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "secret_token")
    private String secretToken;

    @Column(name = "events")
    private String events;

    @Column(name = "enable")
    private Integer enable;

    @Column(name = "update_time")
    private Timestamp updateTime;
    @Column(name = "create_time")
    private Timestamp createTime;




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecretToken() {
        return secretToken;
    }

    public void setSecretToken(String secretToken) {
        this.secretToken = secretToken;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }
}
