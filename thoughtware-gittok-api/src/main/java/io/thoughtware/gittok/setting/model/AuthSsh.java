package io.thoughtware.gittok.setting.model;

import io.thoughtware.gittok.repository.model.Repository;
import io.thoughtware.postin.annotation.ApiModel;
import io.thoughtware.postin.annotation.ApiProperty;
import io.thoughtware.toolkit.beans.annotation.Mapper;
import io.thoughtware.toolkit.beans.annotation.Mapping;
import io.thoughtware.toolkit.beans.annotation.Mappings;
import io.thoughtware.toolkit.join.annotation.Join;
import io.thoughtware.toolkit.join.annotation.JoinQuery;
import io.thoughtware.user.user.model.User;

/*
* ssh 认证
* */
@ApiModel
@Join
@Mapper
public class AuthSsh {


    @ApiProperty(name="authId",desc="id")
    private String id;

    @Mappings({
            @Mapping(source = "repository.rpyId",target = "rpyId")
    })
    @JoinQuery(key = "rpyId")
    private Repository repository;

    @ApiProperty(name="title",desc="标题")
    private String title;

    @ApiProperty(name="createTime",desc="创建时间")
    private String createTime;

    @Mappings({
            @Mapping(source = "user.id",target = "userId")
    })
    @JoinQuery(key = "id")
    private User user;


    @ApiProperty(name="value",desc="公钥")
    private String value;

    /**
     * 类型 public,全局 private,项目私有
     */
    @ApiProperty(name="type",desc="类型 public:全局、private:项目私有")
    private String type;

    @ApiProperty(name="userTime",desc="上次使用时间")
    private String userTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserTime() {
        return userTime;
    }

    public void setUserTime(String userTime) {
        this.userTime = userTime;
    }
}




















































