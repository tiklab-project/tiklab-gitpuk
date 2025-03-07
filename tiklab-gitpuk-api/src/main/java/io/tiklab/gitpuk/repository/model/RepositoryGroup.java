package io.tiklab.gitpuk.repository.model;

import io.tiklab.toolkit.beans.annotation.Mapper;
import io.tiklab.toolkit.beans.annotation.Mapping;
import io.tiklab.toolkit.beans.annotation.Mappings;
import io.tiklab.toolkit.join.annotation.Join;
import io.tiklab.toolkit.join.annotation.JoinQuery;
import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;
import io.tiklab.user.user.model.User;

import java.io.Serializable;

@ApiModel
@Join
@Mapper
public class RepositoryGroup implements Serializable {

    @ApiProperty(name="groupId",desc="仓库组id")
    private String groupId;

    @ApiProperty(name="name",desc="仓库组名称")
    private String name;

    @ApiProperty(name="address",desc="仓库地址")
    private String address;

    @ApiProperty(name="createTime",desc="创建时间")
    private String createTime;

    @ApiProperty(name="type",desc="类型")
    private int type;

    //创建人
    @Mappings({
            @Mapping(source = "user.id",target = "userId")
    })
    @JoinQuery(key = "id")
    private User user;

    @ApiProperty(name = "rules" ,desc =" 仓库权限 public、private")
    private String rules;

    @ApiProperty(name="remarks",desc="描述")
    private String remarks;

    @ApiProperty(name="color",desc="仓库图标颜色 0-4")
    private Integer color;

    /*-----------其他字段----------*/

    @ApiProperty(name="repositoryNum",desc="仓库数")
    private String repositoryNum;

    @ApiProperty(name="role",desc="角色")
    private String role;

    @ApiProperty(name="fork",desc="是否可以fork")
    private boolean fork;

    @ApiProperty(name="desc",desc="描述")
    private String desc;

    @ApiProperty(name="dataType",desc="数据类型 个人、仓库组")
    private String dataType;

    public String getGroupId() {
        return groupId;
    }

    public RepositoryGroup setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getRepositoryNum() {
        return repositoryNum;
    }

    public void setRepositoryNum(String repositoryNum) {
        this.repositoryNum = repositoryNum;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isFork() {
        return fork;

    }

    public void setFork(boolean fork) {
        this.fork = fork;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
























































