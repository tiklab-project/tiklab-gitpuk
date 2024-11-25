package io.tiklab.gitpuk.repository.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.tiklab.core.BaseModel;
import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;
import io.tiklab.toolkit.beans.annotation.Mapper;
import io.tiklab.toolkit.join.annotation.Join;

import java.sql.Timestamp;
import java.util.List;

@ApiModel
@Join
@Mapper
public class RepWebHook extends BaseModel {

    @ApiProperty(name="id",desc="id")
    private String id;

    @ApiProperty(name="repositoryId",desc="仓库id")
    private String repositoryId;

    @ApiProperty(name="url",desc="地址")
    private String url;

    @ApiProperty(name="name",desc="webHook名字" ,required = true)
    private String name;

    @ApiProperty(name="secretToken",desc="令牌")
    private String secretToken;

    @ApiProperty(name="events",desc="事件 push、tag、merge")
    private String events;

    @ApiProperty(name="enable",desc="是否启用  不启用：0  启用：1")
    private Integer enable;

    @ApiProperty(name="updateTime",desc="更新事件")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Timestamp updateTime;

    @ApiProperty(name="updateTime",desc="创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Timestamp createTime;



    @ApiProperty(name="eventsList",desc="事件list")
    private List<String> eventsList;

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

    public String getSecretToken() {
        return secretToken;
    }

    public void setSecretToken(String secretToken) {
        this.secretToken = secretToken;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }


    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public List<String> getEventsList() {
        return eventsList;
    }

    public void setEventsList(List<String> eventsList) {
        this.eventsList = eventsList;
    }
}
