package io.tiklab.gitpuk.repository.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;
import io.tiklab.toolkit.beans.annotation.Mapper;
import io.tiklab.toolkit.join.annotation.Join;

import java.sql.Timestamp;
import java.util.List;

/*
* 集成关联
* */
@ApiModel
@Join
@Mapper
public class IntRelevancy {


    @ApiProperty(name="id",desc="id")
    private String id;


    @ApiProperty(name="repositoryId",desc="仓库id")
    private String repositoryId;


    @ApiProperty(name="relevancyId",desc="关联的id")
    private String relevancyId;



    @ApiProperty(name="createTime",desc="创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Timestamp createTime;


    @ApiProperty(name="relevancyIdList",desc="关联的id集合")
    private List<String> relevancyIdList;

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

    public String getRelevancyId() {
        return relevancyId;
    }

    public void setRelevancyId(String relevancyId) {
        this.relevancyId = relevancyId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public List<String> getRelevancyIdList() {
        return relevancyIdList;
    }

    public void setRelevancyIdList(List<String> relevancyIdList) {
        this.relevancyIdList = relevancyIdList;
    }
}




















































