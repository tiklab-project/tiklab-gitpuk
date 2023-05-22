package io.tiklab.xcode.detection.model;

import io.tiklab.beans.annotation.Mapper;
import io.tiklab.join.annotation.Join;
import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;

import java.io.Serializable;
import java.sql.Timestamp;

@ApiModel
@Join
@Mapper(targetAlias = "AuthThirdEntity")
public class AuthThird implements Serializable {

    @ApiProperty(name="id",desc="id")
    private String id;


    @ApiProperty(name="authName",desc="认证名称")
    private String authName;

    @ApiProperty(name="authServer",desc="服务权限 public、private")
    private String authServer;


    @ApiProperty(name="authType",desc="认证类型 account、privateKey")
    private String authType;

    @ApiProperty(name="serverAddress",desc="应用地址")
    private String serverAddress;

    @ApiProperty(name="mavenAddress",desc="maven 地址")
    private String mavenAddress;

    @ApiProperty(name="userName",desc="账号")
    private String userName;

    @ApiProperty(name="passWord",desc="密码")
    private String passWord;

    @ApiProperty(name="privateKey",desc="私钥")
    private String privateKey;

    @ApiProperty(name="createTime",desc="创建时间")
    private Timestamp createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public String getAuthServer() {
        return authServer;
    }

    public void setAuthServer(String authServer) {
        this.authServer = authServer;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getMavenAddress() {
        return mavenAddress;
    }

    public void setMavenAddress(String mavenAddress) {
        this.mavenAddress = mavenAddress;
    }
}
