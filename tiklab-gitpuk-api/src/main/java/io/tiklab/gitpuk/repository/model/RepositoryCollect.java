package io.tiklab.gitpuk.repository.model;

import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;
import io.tiklab.toolkit.beans.annotation.Mapper;
import io.tiklab.toolkit.beans.annotation.Mapping;
import io.tiklab.toolkit.beans.annotation.Mappings;
import io.tiklab.toolkit.join.annotation.Join;
import io.tiklab.toolkit.join.annotation.JoinField;
import io.tiklab.user.user.model.User;

import java.io.Serializable;

/**
 *  仓库收藏
 */
@ApiModel
@Join
@Mapper
public class RepositoryCollect implements Serializable {

    @ApiProperty(name="id",desc="id")
    private String id;


    @ApiProperty(name="repositoryId",desc="仓库id")
    private String repositoryId;

    /**
     * 用户
     */
    @ApiProperty(name="user",desc="收藏人")
    @Mappings({
            @Mapping(source = "user.id",target = "userId")
    })
    @JoinField(key = "user")
    private User user;


    @ApiProperty(name="createTime",desc="创建时间")
    private String createTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}







































