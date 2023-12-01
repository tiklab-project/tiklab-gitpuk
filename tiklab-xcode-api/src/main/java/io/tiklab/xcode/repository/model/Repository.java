package io.tiklab.xcode.repository.model;

import io.tiklab.beans.annotation.Mapper;
import io.tiklab.beans.annotation.Mapping;
import io.tiklab.beans.annotation.Mappings;
import io.tiklab.join.annotation.Join;
import io.tiklab.join.annotation.JoinQuery;
import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;
import io.tiklab.user.user.model.User;

import java.io.Serializable;

/**
 *  @pi.model: io.tiklab.xcode.repository.model.Repository
 */
@ApiModel
@Join
@Mapper
public class Repository implements Serializable {

    /**
     * @pi.name: rpyId
     * @pi.dataType:string
     * @pi.desc: 仓库id
     * @pi.value: rpyId
     */
    @ApiProperty(name="rpyId",desc="仓库id")
    private String rpyId;

    /**
     * @pi.model: RepositoryGroup
     * @pi.desc: 所属空间
     */
    @Mappings({
            @Mapping(source = "group.groupId",target = "groupId")
    })
    @JoinQuery(key = "groupId")
    private RepositoryGroup group;

    /**
     * @pi.name: name
     * @pi.dataType:string
     * @pi.desc: 仓库名称
     * @pi.value: name
     */
    @ApiProperty(name="name",desc="仓库名称")
    private String name;

    /**
     * @pi.name: address
     * @pi.dataType:string
     * @pi.desc: 仓库地址
     * @pi.value: address
     */
    @ApiProperty(name="address",desc="仓库地址")
    private String address;

    /**
     * @pi.name: createTime
     * @pi.dataType:string
     * @pi.desc: 创建时间
     * @pi.value: createTime
     */
    @ApiProperty(name="createTime",desc="创建时间")
    private String createTime;

    /**
     * @pi.name: updateTime
     * @pi.dataType:string
     * @pi.desc: 更新时间
     * @pi.value: updateTime
     */
    @ApiProperty(name="updateTime",desc="更新时间")
    private String updateTime;


    /**
     * @pi.name: type
     * @pi.dataType:int
     * @pi.desc: 类型
     * @pi.value: type
     */
    @ApiProperty(name="type",desc="类型")
    private int type;

    /**
     * @pi.name: rules
     * @pi.dataType:string
     * @pi.desc: 仓库权限public、private
     * @pi.value: rules
     */
    @ApiProperty(name = "rules" ,desc =" 仓库权限 public、private")
    private String rules;

    /**
     * @pi.model: User
     * @pi.desc: 创建人
     */
    @Mappings({
            @Mapping(source = "user.id",target = "userId")
    })
    @JoinQuery(key = "id")
    private User user;

    /**
     * @pi.name: remarks
     * @pi.dataType:string
     * @pi.desc: 描述
     * @pi.value: remarks
     */
    @ApiProperty(name="remarks",desc="描述")
    private String remarks;

    /**
     * @pi.name: language
     * @pi.dataType:string
     * @pi.desc: 语言
     * @pi.value: language
     */
    @ApiProperty(name="language",desc="语言")
    private String language;

    /**
     * @pi.name: state
     * @pi.dataType:int
     * @pi.desc: 仓库状态 1.所有 2.只读
     * @pi.value: state
     */
    @ApiProperty(name="state",desc="仓库状态 1.所有 2.只读 ")
    private int state=1;

    /**
     * @pi.name: classifyState
     * @pi.dataType:string
     * @pi.desc: 代码归档状态 "ture" "false"
     * @pi.value: classifyState
     */
    @ApiProperty(name="classifyState",desc="代码归档状态 ture false")
    private String classifyState="false";

    /**
     * @pi.name: size
     * @pi.dataType:string
     * @pi.desc: 仓库大小 单位：字节
     * @pi.value: classifyState
     */
    @ApiProperty(name="size",desc="仓库大小 单位：字节")
    private String size;

    /*--------其他字段---------*/


    /**
     * 默认分支
     */
    private String defaultBranch;

    /**
     * 是否为空仓库
     */
    private boolean notNull;

    /**
     * 仓库全路径
     */
    private String fullPath;

    /**
     * 角色
     */
    private String role;


    public String getRpyId() {
        return rpyId;
    }

    public void setRpyId(String rpyId) {
        this.rpyId = rpyId;
    }

    public RepositoryGroup getGroup() {
        return group;
    }

    public void setGroup(RepositoryGroup group) {
        this.group = group;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


    public String getDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }


    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getClassifyState() {
        return classifyState;
    }

    public void setClassifyState(String classifyState) {
        this.classifyState = classifyState;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}







































