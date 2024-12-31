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
public class Repository implements Serializable {


    @ApiProperty(name="rpyId",desc="仓库id")
    private String rpyId;


    @Mappings({
            @Mapping(source = "group.groupId",target = "groupId")
    })
    @JoinQuery(key = "groupId")
    private RepositoryGroup group;


    @ApiProperty(name="name",desc="仓库名称")
    private String name;


    @ApiProperty(name="address",desc="仓库地址")
    private String address;


    @ApiProperty(name="category",desc="1 、演示 ；2、正式仓库")
    private Integer category=2;


    @ApiProperty(name="createTime",desc="创建时间")
    private String createTime;


    @ApiProperty(name="updateTime",desc="更新时间")
    private String updateTime;


    @ApiProperty(name="type",desc="类型")
    private int type;


    @ApiProperty(name = "rules" ,desc =" 仓库权限 public、private")
    private String rules;


    @Mappings({
            @Mapping(source = "user.id",target = "userId")
    })
    @JoinQuery(key = "id")
    private User user;


    @ApiProperty(name="remarks",desc="描述")
    private String remarks;


    @ApiProperty(name="language",desc="语言")
    private String language;


    @ApiProperty(name="state",desc="仓库状态 1.所有 2.只读 ")
    private int state=1;


    @ApiProperty(name="classifyState",desc="代码归档状态 ture false")
    private String classifyState="false";


    @ApiProperty(name="size",desc="仓库大小 单位：字节")
    private Long size;


    @ApiProperty(name="color",desc="仓库图标颜色 0-4")
    private Integer color;

    /*--------其他字段---------*/

    /**
     * 仓库大小
     */
    private String rpySize;


    /**
     * 默认分支
     */
    private String defaultBranch;

    /**
     * 是否为空仓库
     */
    private boolean notNull;

    /**
     * 仓库http拉取路径
     */
    private String fullPath;

    /**
     * 仓库ssh拉取路径
     */
    private String sshPath;

    /**
     * 角色
     */
    private String role;

    //是否创建README.md   0:false、1:true  默认不创建
    private Integer isReadme=0;

    //创建.Gitignore文件
    private String gitignoreValue;


    //第一次上传空仓库 不是默认分支且只有一个分支
    private String uniqueBranch;


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

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public String getRpySize() {
        return rpySize;
    }

    public void setRpySize(String rpySize) {
        this.rpySize = rpySize;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getIsReadme() {
        return isReadme;
    }

    public void setIsReadme(Integer isReadme) {
        this.isReadme = isReadme;
    }

    public String getGitignoreValue() {
        return gitignoreValue;
    }

    public void setGitignoreValue(String gitignoreValue) {
        this.gitignoreValue = gitignoreValue;
    }

    public String getUniqueBranch() {
        return uniqueBranch;
    }

    public void setUniqueBranch(String uniqueBranch) {
        this.uniqueBranch = uniqueBranch;
    }

    public String getSshPath() {
        return sshPath;
    }

    public void setSshPath(String sshPath) {
        this.sshPath = sshPath;
    }
}







































