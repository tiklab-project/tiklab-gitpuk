package io.tiklab.gitpuk.setting.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.tiklab.gitpuk.repository.model.Repository;
import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;
import io.tiklab.toolkit.beans.annotation.Mapper;
import io.tiklab.toolkit.beans.annotation.Mapping;
import io.tiklab.toolkit.beans.annotation.Mappings;
import io.tiklab.toolkit.join.annotation.Join;
import io.tiklab.toolkit.join.annotation.JoinQuery;
import io.tiklab.user.user.model.User;

import java.sql.Timestamp;

/*
* 系统集成地址
* */
@ApiModel
@Join
@Mapper
public class IntegrationAddress {


    @ApiProperty(name="id",desc="id")
    private String id;


    @ApiProperty(name="code",desc="集成的类型code")
    private String code;


    @ApiProperty(name="integrationAddress",desc="集成地址")
    private String integrationAddress;

    @ApiProperty(name="authType",desc="认证类型 私钥：key、密码：password")
    private String authType="password";

    @ApiProperty(name="account",desc="集成地址")
    private String account;

    @ApiProperty(name="password",desc="集成地址")
    private String password;


    @ApiProperty(name="createTime",desc="创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
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

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
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
}

