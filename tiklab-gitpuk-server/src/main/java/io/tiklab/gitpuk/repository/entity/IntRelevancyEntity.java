package io.tiklab.gitpuk.repository.entity;

import io.tiklab.dal.jpa.annotation.*;

import java.sql.Timestamp;

@Entity
@Table(name="rpy_integration_relevancy")
public class IntRelevancyEntity {


    @Id
    @GeneratorValue(length=12)
    @Column(name = "id")
    private String id;

    @Column(name = "repository_id")
    private String repositoryId;

    @Column(name = "relevancy_id",notNull = true)
    private String relevancyId;

    @Column(name = "type")
    private String type;

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

    public String getRelevancyId() {
        return relevancyId;
    }

    public void setRelevancyId(String relevancyId) {
        this.relevancyId = relevancyId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
