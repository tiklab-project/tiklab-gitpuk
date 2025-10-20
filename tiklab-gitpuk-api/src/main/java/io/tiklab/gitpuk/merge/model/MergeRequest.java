package io.tiklab.gitpuk.merge.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.tiklab.gitpuk.repository.model.Repository;
import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;
import io.tiklab.toolkit.beans.annotation.Mapper;
import io.tiklab.toolkit.beans.annotation.Mapping;
import io.tiklab.toolkit.beans.annotation.Mappings;
import io.tiklab.toolkit.join.annotation.Join;
import io.tiklab.toolkit.join.annotation.JoinField;
import io.tiklab.user.user.model.User;

import java.sql.Timestamp;

@ApiModel
@Join
@Mapper
public class MergeRequest {

    @ApiProperty(name="id",desc="合并请求id")
    private String id;

    @ApiProperty(name="title",desc="合并请求标题")
    private String title;

    @ApiProperty(name="repository",desc="仓库")
    @Mappings({
            @Mapping(source = "repository.rpyId",target = "rpyId")
    })
    @JoinField(key = "rpyId")
    private Repository repository;

    @ApiProperty(name="mergeOrigin",desc="合并源")
    private String mergeOrigin;

    @ApiProperty(name="mergeTarget",desc="目标源")
    private String mergeTarget;

    @ApiProperty(name="mergeState",desc="合并状态 1.已开启、2.已合并、3.已关闭 ")
    private Integer mergeState=1;




    @ApiProperty(name="value",desc="内容")
    private String value;

    @ApiProperty(name="parentCommit",desc="合并分支的父commitId")
    private String parentCommit;

    @ApiProperty(name="currentCommit",desc="合并后当前commitId")
    private String currentCommit;

    @ApiProperty(name="createUser",desc="用户")
    @Mappings({
            @Mapping(source = "user.id",target = "createUser")
    })
    @JoinField(key = "id")
    private User user;

    @ApiProperty(name="createTime",desc="创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private java.sql.Timestamp createTime;

    /*-----其他字段-----*/

    @ApiProperty(name="execType",desc="操作类型 close、open、create、comment、complete")
    private String execType;

    @ApiProperty(name="branchExist",desc="分子是否存在")
    private boolean branchExist;

    @ApiProperty(name="isClash",desc="是否有冲突 0、没有 1、有 ")
    private Integer isClash;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public String getMergeOrigin() {
        return mergeOrigin;
    }

    public void setMergeOrigin(String mergeOrigin) {
        this.mergeOrigin = mergeOrigin;
    }

    public String getMergeTarget() {
        return mergeTarget;
    }

    public void setMergeTarget(String mergeTarget) {
        this.mergeTarget = mergeTarget;
    }

    public Integer getMergeState() {
        return mergeState;
    }

    public void setMergeState(Integer mergeState) {
        this.mergeState = mergeState;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Integer getIsClash() {
        return isClash;
    }

    public void setIsClash(Integer isClash) {
        this.isClash = isClash;
    }

    public String getExecType() {
        return execType;
    }

    public void setExecType(String execType) {
        this.execType = execType;
    }

    public String getParentCommit() {
        return parentCommit;
    }

    public void setParentCommit(String parentCommit) {
        this.parentCommit = parentCommit;
    }

    public String getCurrentCommit() {
        return currentCommit;
    }

    public void setCurrentCommit(String currentCommit) {
        this.currentCommit = currentCommit;
    }

    public boolean isBranchExist() {
        return branchExist;
    }

    public void setBranchExist(boolean branchExist) {
        this.branchExist = branchExist;
    }
}
