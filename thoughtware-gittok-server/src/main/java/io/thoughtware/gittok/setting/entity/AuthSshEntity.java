package io.thoughtware.gittok.setting.entity;

import io.thoughtware.dal.jpa.annotation.*;

import java.sql.Timestamp;

@Entity
@Table(name="rpy_auth_ssh")
public class AuthSshEntity {


    @Id
    @GeneratorValue(length=12)
    @Column(name = "id")
    private String id;

    @Column(name = "rpy_id")
    private String rpyId;

    @Column(name = "title",notNull = true)
    private String title;


    @Column(name = "user_id",notNull = true)
    private String userId;

    @Column(name = "value",notNull = true)
    private String value;


    @Column(name = "fingerprint")
    private String fingerprint;

    /**
     * 类型 public,全局 private,项目私有
     */
    @Column(name = "type")
    private String type;


    @Column(name = "expire_time")
    private String expireTime;


    @Column(name = "create_time")
    private Timestamp createTime;

    @Column(name = "user_time",notNull = true)
    private Timestamp userTime;


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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getUserTime() {
        return userTime;
    }

    public void setUserTime(Timestamp userTime) {
        this.userTime = userTime;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }
}




















































