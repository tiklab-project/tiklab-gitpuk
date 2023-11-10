package io.tiklab.xcode.scan.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.tiklab.beans.annotation.Mapper;
import io.tiklab.beans.annotation.Mapping;
import io.tiklab.beans.annotation.Mappings;
import io.tiklab.join.annotation.Join;
import io.tiklab.join.annotation.JoinQuery;
import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;

import java.io.Serializable;
import java.sql.Timestamp;

@ApiModel
@Join
@Mapper
public class ScanSchemeSonar implements Serializable {

    @ApiProperty(name="id",desc="id")
    private String id;

    @ApiProperty(name="scanSchemeId",desc="扫描方案id")
    private String scanSchemeId;


    @ApiProperty(name="deployEnv",desc="部署环境")
    @Mappings({
            @Mapping(source = "deployEnv.id",target = "deployEnvId")
    })
    @JoinQuery(key = "id")
    private DeployEnv deployEnv;

    @ApiProperty(name="deployServer",desc="部署服务")
    @Mappings({
            @Mapping(source = "deployServer.id",target = "deployServer")
    })
    @JoinQuery(key = "id")
    private DeployServer deployServer;


    @ApiProperty(name="createTime",desc="创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Timestamp createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScanSchemeId() {
        return scanSchemeId;
    }

    public void setScanSchemeId(String scanSchemeId) {
        this.scanSchemeId = scanSchemeId;
    }

    public DeployEnv getDeployEnv() {
        return deployEnv;
    }

    public void setDeployEnv(DeployEnv deployEnv) {
        this.deployEnv = deployEnv;
    }

    public DeployServer getDeployServer() {
        return deployServer;
    }

    public void setDeployServer(DeployServer deployServer) {
        this.deployServer = deployServer;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
