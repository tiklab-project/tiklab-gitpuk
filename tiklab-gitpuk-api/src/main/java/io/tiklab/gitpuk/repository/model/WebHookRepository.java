package io.tiklab.gitpuk.repository.model;

import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;
import io.tiklab.toolkit.beans.annotation.Mapper;
import io.tiklab.toolkit.beans.annotation.Mapping;
import io.tiklab.toolkit.beans.annotation.Mappings;
import io.tiklab.toolkit.join.annotation.Join;
import io.tiklab.toolkit.join.annotation.JoinQuery;
import io.tiklab.user.user.model.User;

@ApiModel
@Join
@Mapper
public class WebHookRepository {

    @ApiProperty(name="id",desc="id")
    private String id;

    @ApiProperty(name="name",desc="仓库名字")
    private String name;

    @ApiProperty(name="createTime",desc="创建时间")
    private String createTime;

    @ApiProperty(name="httpPath",desc="仓库http路径")
    private String httpPath;

    @ApiProperty(name="sshPath",desc="仓库ssh路径")
    private String sshPath;

    @ApiProperty(name="defaultBranch",desc="默认分支")
    private String defaultBranch;

    @ApiProperty(name = "rules" ,desc =" 仓库权限 public、private")
    private String rules;

    @ApiProperty(name = "groupName" ,desc =" 仓库组名字")
    private String groupName;


    @Mappings({
            @Mapping(source = "user.id",target = "userId")
    })
    @JoinQuery(key = "id")
    private User user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getHttpPath() {
        return httpPath;
    }

    public void setHttpPath(String httpPath) {
        this.httpPath = httpPath;
    }

    public String getSshPath() {
        return sshPath;
    }

    public void setSshPath(String sshPath) {
        this.sshPath = sshPath;
    }

    public String getDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
