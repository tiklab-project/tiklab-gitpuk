package io.tiklab.xcode.repository.model;

import io.tiklab.beans.annotation.Mapper;
import io.tiklab.beans.annotation.Mapping;
import io.tiklab.beans.annotation.Mappings;
import io.tiklab.join.annotation.Join;
import io.tiklab.join.annotation.JoinQuery;
import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;
import io.tiklab.user.user.model.User;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

@ApiModel
@Join
@Mapper
public class RemoteInfo implements Serializable {

    @ApiProperty(name="id",desc="id")
    private String id;

    @ApiProperty(name="rpyId",desc="仓库id")
    private String rpyId;

    @ApiProperty(name="name",desc="名称")
    private String name;

    @NotNull
    @ApiProperty(name="address",desc="地址")
    private String address;

    @NotNull
    @ApiProperty(name="authWay",desc="认证方式 ssh、password")
    private String authWay;

    @ApiProperty(name="account",desc="账号")
    private String account;

    @ApiProperty(name="password",desc="密码")
    private String password;


    @ApiProperty(name="secretKey",desc="密钥")
    private String secretKey;

    @ApiProperty(name="timedState",desc="定时任务状态 0:未开启 1:开启")
    private Integer timedState=0;

    @ApiProperty(name = "createTime" ,desc ="创建时间")
    private Timestamp createTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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







































