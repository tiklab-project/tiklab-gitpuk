package io.tiklab.gitpuk.repository.model;

import io.tiklab.core.page.Page;
import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel
public class RepositoryGroupQuery implements Serializable {

    @ApiProperty(name ="pageParam",desc = "分页参数")
    private Page pageParam = new Page();

    @ApiProperty(name ="userId",desc = "登录用户")
    private String  userId;

    @ApiProperty(name ="findType",desc = "查询类型 自己创建的：oneself、有权限看的：viewable、需要查看角色的：viewableRole")
    private String  findType;

    @ApiProperty(name ="userId",desc = "仓库名称")
    private String  name;

    @ApiProperty(name ="rules",desc = "仓库权限 public、private")
    private String  rules;



    @ApiProperty(name ="repositoryId",desc = "用于fork仓库id 用于fork")
    private String  repositoryId;

    @ApiProperty(name ="repositoryName",desc = "仓库名称 用于fork")
    private String  repositoryName;

    @ApiProperty(name ="repositoryAddress",desc = "仓库路径 用于fork")
    private String  repositoryAddress;

    @ApiProperty(name ="groupId",desc = "仓库组id 用于fork")
    private String  groupId;

    @ApiProperty(name ="userName",desc = "用户名字 用于fork")
    private String  userName;


    public Page getPageParam() {
        return pageParam;
    }

    public void setPageParam(Page pageParam) {
        this.pageParam = pageParam;
    }

    public String getFindType() {
        return findType;
    }

    public void setFindType(String findType) {
        this.findType = findType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getRepositoryAddress() {
        return repositoryAddress;
    }

    public void setRepositoryAddress(String repositoryAddress) {
        this.repositoryAddress = repositoryAddress;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }
}







































