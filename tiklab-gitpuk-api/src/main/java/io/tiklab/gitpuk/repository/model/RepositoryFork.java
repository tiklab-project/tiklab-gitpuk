package io.tiklab.gitpuk.repository.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;
import io.tiklab.toolkit.beans.annotation.Mapper;
import io.tiklab.toolkit.beans.annotation.Mapping;
import io.tiklab.toolkit.beans.annotation.Mappings;
import io.tiklab.toolkit.join.annotation.Join;
import io.tiklab.toolkit.join.annotation.JoinQuery;
import io.tiklab.user.user.model.User;

import java.sql.Timestamp;

@ApiModel
@Join
@Mapper
public class RepositoryFork {

    @ApiProperty(name="id",desc="id")
    private String id;

    @ApiProperty(name="repositoryId",desc="原仓库id")
    private String repositoryId;


    @ApiProperty(name="repository",desc="fork 仓库id")
    @Mappings({
            @Mapping(source = "repository.rpyId",target = "forkRepositoryId")
    })
    @JoinQuery(key = "rpyId")
    private Repository repository;

    @ApiProperty(name="groupId",desc="仓库组id")
    private String groupId;


    @ApiProperty(name="userId",desc="用户id")
    @Mappings({
            @Mapping(source = "user.id",target = "userId")
    })
    @JoinQuery(key = "id")
    private User user;

    @ApiProperty(name="createTime",desc="创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Timestamp createTime;

    /*-----其他字段--------*/

    @ApiProperty(name="repAddress",desc="仓库地址")
    private String repAddress;

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

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getRepAddress() {
        return repAddress;
    }

    public void setRepAddress(String repAddress) {
        this.repAddress = repAddress;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}







































