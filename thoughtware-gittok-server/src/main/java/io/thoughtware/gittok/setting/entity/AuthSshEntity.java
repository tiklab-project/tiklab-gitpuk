package io.thoughtware.gittok.setting.entity;

import io.thoughtware.dal.jpa.annotation.*;

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

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "user_id",notNull = true)
    private String userId;

    @Column(name = "value",notNull = true)
    private String value;

    /**
     * 类型 public,全局 private,项目私有
     */
    @Column(name = "type")
    private String type;

    @Column(name = "user_time",notNull = true)
    private String userTime;


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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
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

    public String getUserTime() {
        return userTime;
    }

    public void setUserTime(String userTime) {
        this.userTime = userTime;
    }
}




















































