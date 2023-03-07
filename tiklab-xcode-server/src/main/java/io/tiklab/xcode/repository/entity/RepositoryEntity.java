package io.tiklab.xcode.repository.entity;

import io.tiklab.dal.jpa.annotation.*;

@Entity
@Table(name="rpy_repository")
public class RepositoryEntity {

    @Id
    @GeneratorValue
    @Column(name = "rpy_id")
    private String rpyId;

    @Column(name = "group_id")
    private String groupId;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "type")
    private int type;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "language")
    private String language;

    @Column(name = "state")
    private int state;


    public String getRpyId() {
        return rpyId;
    }

    public void setRpyId(String rpyId) {
        this.rpyId = rpyId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}












































