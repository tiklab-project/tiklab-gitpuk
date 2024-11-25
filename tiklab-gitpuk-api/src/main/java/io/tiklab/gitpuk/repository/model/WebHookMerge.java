package io.tiklab.gitpuk.repository.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.tiklab.postin.annotation.ApiProperty;

import java.sql.Timestamp;

public class WebHookMerge {

    @ApiProperty(name="id",desc="id")
    private String id;

    @ApiProperty(name="title",desc="合并请求标题")
    private String title;

    @ApiProperty(name="mergeOrigin",desc="合并源")
    private String mergeOrigin;

    @ApiProperty(name="mergeTarget",desc="目标源")
    private String mergeTarget;

    @ApiProperty(name="mergeState",desc="合并状态 1.已开启、2.已合并、3.已关闭 ")
    private Integer mergeState;

    @ApiProperty(name="createTime",desc="创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private java.sql.Timestamp createTime;

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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
