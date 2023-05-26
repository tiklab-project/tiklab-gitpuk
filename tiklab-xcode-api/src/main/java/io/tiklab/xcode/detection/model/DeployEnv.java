package io.tiklab.xcode.detection.model;

import io.tiklab.beans.annotation.Mapper;
import io.tiklab.join.annotation.Join;
import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;

import java.io.Serializable;
import java.sql.Timestamp;


@ApiModel
@Join
@Mapper(targetAlias = "DeployEnvEntity")
public class DeployEnv implements Serializable {


    @ApiProperty(name="id",desc="id")
    private String id;


    @ApiProperty(name="envType",desc="环境类型")
    private String envType;

    @ApiProperty(name="envName",desc="环境名称")
    private String envName;

    @ApiProperty(name="envAddress",desc="环境应用地址")
    private String envAddress;

    @ApiProperty(name="createTime",desc="创建时间")
    private Timestamp createTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnvType() {
        return envType;
    }

    public void setEnvType(String envType) {
        this.envType = envType;
    }

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public String getEnvAddress() {
        return envAddress;
    }

    public void setEnvAddress(String envAddress) {
        this.envAddress = envAddress;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}




















































