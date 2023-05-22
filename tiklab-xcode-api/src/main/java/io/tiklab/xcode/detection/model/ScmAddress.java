package io.tiklab.xcode.detection.model;

import io.tiklab.beans.annotation.Mapper;
import io.tiklab.join.annotation.Join;
import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;

import java.io.Serializable;
import java.sql.Timestamp;


@ApiModel
@Join
@Mapper(targetAlias = "ScmAddressEntity")
public class ScmAddress implements Serializable {


    @ApiProperty(name="id",desc="id")
    private String id;


    @ApiProperty(name="scmType",desc="供应应用类型")
    private String scmType;

    @ApiProperty(name="scmName",desc="供应应用名称")
    private String scmName;

    @ApiProperty(name="scmAddress",desc="供应应用地址")
    private String scmAddress;

    @ApiProperty(name="createTime",desc="创建时间")
    private Timestamp createTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScmType() {
        return scmType;
    }

    public void setScmType(String scmType) {
        this.scmType = scmType;
    }

    public String getScmName() {
        return scmName;
    }

    public void setScmName(String scmName) {
        this.scmName = scmName;
    }

    public String getScmAddress() {
        return scmAddress;
    }

    public void setScmAddress(String scmAddress) {
        this.scmAddress = scmAddress;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}




















































