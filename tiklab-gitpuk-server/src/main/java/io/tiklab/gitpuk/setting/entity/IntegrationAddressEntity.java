package io.tiklab.gitpuk.setting.entity;

import io.tiklab.dal.jpa.annotation.*;

import java.sql.Timestamp;

@Entity
@Table(name="rpy_integration_address")
public class IntegrationAddressEntity {


    @Id
    @GeneratorValue(length=12)
    @Column(name = "id")
    private String id;

    @Column(name = "code")
    private String code;

    @Column(name = "integration_address",notNull = true)
    private String integrationAddress;

    @Column(name = "auth_type",notNull = true)
    private String authType;

    @Column(name = "account")
    private String account;


    @Column(name = "password")
    private String password;

    @Column(name = "create_time")
    private Timestamp createTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIntegrationAddress() {
        return integrationAddress;
    }

    public void setIntegrationAddress(String integrationAddress) {
        this.integrationAddress = integrationAddress;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
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

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }
}
