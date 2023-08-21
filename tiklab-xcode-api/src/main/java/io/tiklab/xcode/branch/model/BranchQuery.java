package io.tiklab.xcode.branch.model;

import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;

import java.io.Serializable;

@ApiModel
public class BranchQuery implements Serializable {

    @ApiProperty(name ="name",desc = "分支名称")
    private String  name;

    @ApiProperty(name ="state",desc = "分支状态 活跃：active ")
    private String  state;

    @ApiProperty(name ="rpyId",desc = "仓库id")
    private String  rpyId;

    @ApiProperty(name ="repositoryAddress",desc = "仓库地址")
    private String  repositoryAddress;

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
}
