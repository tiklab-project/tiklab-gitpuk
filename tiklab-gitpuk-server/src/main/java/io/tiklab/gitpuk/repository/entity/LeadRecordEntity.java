package io.tiklab.gitpuk.repository.entity;

import io.tiklab.dal.jpa.annotation.*;

import java.sql.Timestamp;

@Entity
@Table(name="rpy_lead_record")
public class LeadRecordEntity {

    @Id
    @GeneratorValue(length=12)
    @Column(name = "id")
    private String id;

    //仓库id
    @Column(name = "rpy_id")
    private String rpyId;

    //导入方式
    @Column(name = "lead_way")
    private String leadWay;

    //导入状态
    @Column(name = "lead_state")
    private String leadState;

    //第三方库的关联id
    @Column(name = "relevance_id")
    private String relevanceId;

    @Column(name = "create_time")
    private Timestamp createTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRpyId() {
        return rpyId;
    }

    public void setRpyId(String rpyId) {
        this.rpyId = rpyId;
    }

    public String getLeadWay() {
        return leadWay;
    }

    public void setLeadWay(String leadWay) {
        this.leadWay = leadWay;
    }

    public String getLeadState() {
        return leadState;
    }

    public void setLeadState(String leadState) {
        this.leadState = leadState;
    }

    public String getRelevanceId() {
        return relevanceId;
    }

    public void setRelevanceId(String relevanceId) {
        this.relevanceId = relevanceId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}












































