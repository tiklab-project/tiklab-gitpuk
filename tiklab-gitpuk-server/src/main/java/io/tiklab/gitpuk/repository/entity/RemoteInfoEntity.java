package io.tiklab.gitpuk.repository.entity;

import io.tiklab.core.BaseModel;
import io.tiklab.dal.jpa.annotation.*;

import java.sql.Timestamp;

@Entity
@Table(name="rpy_remote_info")
public class RemoteInfoEntity extends BaseModel {

    @Id
    @GeneratorValue(length=12)
    @Column(name = "id")
    private String id;

    @Column(name = "rpy_id",notNull = true)
    private String rpyId;

    @Column(name = "name",notNull = true)
    private String name;

    @Column(name = "address",notNull = true)
    private String address;

    @Column(name = "auth_way",notNull = true)
    private String authWay;

    @Column(name = "account")
    private String account;

    @Column(name = "password")
    private String password;

    @Column(name = "secret_key")
    private String secretKey;

    @Column(name = "timed_state")
    private Integer timedState;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAuthWay() {
        return authWay;
    }

    public void setAuthWay(String authWay) {
        this.authWay = authWay;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getRpyId() {
        return rpyId;
    }

    public void setRpyId(String rpyId) {
        this.rpyId = rpyId;
    }

    public Integer getTimedState() {
        return timedState;
    }

    public void setTimedState(Integer timedState) {
        this.timedState = timedState;
    }
}












































