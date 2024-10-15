package io.tiklab.gitpuk.branch.model;

import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;

import java.io.Serializable;

@ApiModel
public class BranchQuery implements Serializable {

    @ApiProperty(name ="name",desc = "分支名称")
    private String  name;

    @ApiProperty(name ="state",desc = "分支状态 所有：all、 我的：oneself、 活跃：active、非活跃noActive")
    private String  state;

    @ApiProperty(name ="rpyId",desc = "仓库id")
    private String  rpyId;

    @ApiProperty(name ="repositoryAddress",desc = "仓库地址")
    private String  repositoryAddress;

    @ApiProperty(name ="userId",desc = "用户id")
    private String  userId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRpyId() {
        return rpyId;
    }

    public void setRpyId(String rpyId) {
        this.rpyId = rpyId;
    }

    public String getRepositoryAddress() {
        return repositoryAddress;
    }

    public void setRepositoryAddress(String repositoryAddress) {
        this.repositoryAddress = repositoryAddress;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
