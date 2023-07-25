package io.tiklab.xcode.detection.model;

import io.tiklab.beans.annotation.Mapper;
import io.tiklab.join.annotation.Join;
import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;

import java.io.Serializable;
import java.sql.Timestamp;

@ApiModel
@Join
@Mapper
public class DeployServer implements Serializable {

    @ApiProperty(name="id",desc="id")
    private String id;

    @ApiProperty(name="taskName",desc="任务名称")
    private String taskName;

    @ApiProperty(name="serverName",desc="服务名称")
    private String serverName;

    @ApiProperty(name="authType",desc="认证类型 account、privateKey")
    private String authType;

    @ApiProperty(name="serverAddress",desc="应用地址")
    private String serverAddress;

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

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
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
}
