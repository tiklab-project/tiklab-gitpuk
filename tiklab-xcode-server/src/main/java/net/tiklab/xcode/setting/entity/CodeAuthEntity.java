package net.tiklab.xcode.setting.entity;

import net.tiklab.dal.jpa.annotation.*;

@Entity
@Table(name="code_auth")
public class CodeAuthEntity {


    @Id
    @GeneratorValue
    @Column(name = "auth_id")
    private String authId;

    @Column(name = "code_id")
    private String codeId;

    @Column(name = "title",notNull = true)
    private String title;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "user_id",notNull = true)
    private String userId;

    @Column(name = "value",notNull = true)
    private String value;

    /**
     * 类型 1,全局 2,项目私有
     */
    @Column(name = "type")
    private String type;

    @Column(name = "user_time",notNull = true)
    private String userTime;


    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
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




















































