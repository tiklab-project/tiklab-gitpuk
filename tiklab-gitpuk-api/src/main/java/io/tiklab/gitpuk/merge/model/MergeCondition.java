package io.tiklab.gitpuk.merge.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.tiklab.core.BaseModel;
import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;
import io.tiklab.toolkit.beans.annotation.Mapper;
import io.tiklab.toolkit.beans.annotation.Mapping;
import io.tiklab.toolkit.beans.annotation.Mappings;
import io.tiklab.toolkit.join.annotation.Join;
import io.tiklab.toolkit.join.annotation.JoinQuery;
import io.tiklab.user.user.model.User;

import java.sql.Timestamp;
import java.util.List;

@ApiModel
@Join
@Mapper
public class MergeCondition extends BaseModel {

    @ApiProperty(name="id",desc="id")
    private String id;

    @ApiProperty(name="mergeRequestId",desc="合并请求id")
    private String mergeRequestId;

    @ApiProperty(name="repositoryId",desc="仓库Id")
    private String repositoryId;

    @ApiProperty(name="type",desc="动态类型 创建：create、关闭合并请求：close、打开合并请求：open、评论：comment、合并完成:complete")
    private String type;


    @ApiProperty(name="user",desc="用户")
    @Mappings({
            @Mapping(source = "user.id",target = "userId")
    })
    @JoinQuery(key = "id")
    private User user;

    @ApiProperty(name="data",desc="内容")
    private String data;

    @ApiProperty(name="createTime",desc="创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp createTime;

    /*------其他字段---------*/
    private List<MergeComment> mergeCommentList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getMergeRequestId() {
        return mergeRequestId;
    }

    public void setMergeRequestId(String mergeRequestId) {
        this.mergeRequestId = mergeRequestId;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public List<MergeComment> getMergeCommentList() {
        return mergeCommentList;
    }

    public void setMergeCommentList(List<MergeComment> mergeCommentList) {
        this.mergeCommentList = mergeCommentList;
    }
}
