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

@ApiModel
@Join
@Mapper
public class MergeComment extends BaseModel {

    @ApiProperty(name="id",desc="id")
    private String id;

    @ApiProperty(name="mergeConditionId",desc="合并动态的id")
    private String mergeConditionId;

    @ApiProperty(name="mergeRequestId",desc="合并请求id")
    private String mergeRequestId;

    @ApiProperty(name="repositoryId",desc="仓库Id")
    private String repositoryId;

    @ApiProperty(name="commentUser",desc="评论人")
    @Mappings({
            @Mapping(source = "commentUser.id",target = "commentUserId")
    })
    @JoinQuery(key = "id")
    private User commentUser;


    @ApiProperty(name="replyUser",desc="对谁的评论回复")
    @Mappings({
            @Mapping(source = "replyUser.id",target = "replyUserId")
    })
    @JoinQuery(key = "id")
    private User replyUser;

    @ApiProperty(name="data",desc="内容")
    private String data;

    @ApiProperty(name="createTime",desc="创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp createTime;

    /*-------其他字段----------*/
    @ApiProperty(name="createType",desc="创建评论类型 在动态里面创建新的评论：condition、回复评论：comment")
    private String createType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMergeConditionId() {
        return mergeConditionId;
    }

    public void setMergeConditionId(String mergeConditionId) {
        this.mergeConditionId = mergeConditionId;
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

    public User getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(User commentUser) {
        this.commentUser = commentUser;
    }

    public User getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(User replyUser) {
        this.replyUser = replyUser;
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

    public String getCreateType() {
        return createType;
    }

    public void setCreateType(String createType) {
        this.createType = createType;
    }
}
