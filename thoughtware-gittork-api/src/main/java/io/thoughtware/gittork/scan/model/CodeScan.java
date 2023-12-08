package io.thoughtware.gittork.scan.model;

import io.thoughtware.beans.annotation.Mapper;
import io.thoughtware.beans.annotation.Mapping;
import io.thoughtware.beans.annotation.Mappings;
import io.thoughtware.join.annotation.Join;
import io.thoughtware.join.annotation.JoinQuery;
import io.thoughtware.postin.annotation.ApiModel;
import io.thoughtware.postin.annotation.ApiProperty;
import io.thoughtware.gittork.repository.model.Repository;

import java.io.Serializable;
import java.sql.Timestamp;

@ApiModel
@Join
@Mapper
public class CodeScan implements Serializable {

    @ApiProperty(name="id",desc="id")
    private String id;


    @ApiProperty(name="taskName",desc="扫描任务名称")
    private String taskName;


    @ApiProperty(name="repository",desc="仓库")
    @Mappings({
            @Mapping(source = "repository.rpyId",target = "repositoryId")
    })
    @JoinQuery(key = "rpyId")
    private Repository repository;

    @ApiProperty(name="deployEnvId",desc="环境id")
    private String deployEnvId;


    @ApiProperty(name="deployServer",desc="服务")
    @Mappings({
            @Mapping(source = "deployServer.id",target = "deployServerId")
    })
    @JoinQuery(key = "id")
    private DeployServer deployServer;

    @ApiProperty(name="scanStatus",desc="scanStatus 扫描通过 OK  ERROR")
    private String scanStatus;

    @ApiProperty(name="bugs",desc="bugs数量")
    private String bugs;

    @ApiProperty(name="codeSmells",desc="代码异常数")
    private String codeSmells;

    @ApiProperty(name="vulnerabilities ",desc="漏洞")
    private String vulnerabilities ;

    @ApiProperty(name="updateTime ",desc="更新时间")
    private Timestamp updateTime ;

    @ApiProperty(name="createTime ",desc="更新时间")
    private Timestamp createTime ;



    public String getBugs() {
        return bugs;
    }

    public void setBugs(String bugs) {
        this.bugs = bugs;
    }

    public String getCodeSmells() {
        return codeSmells;
    }

    public void setCodeSmells(String codeSmells) {
        this.codeSmells = codeSmells;
    }

    public String getVulnerabilities() {
        return vulnerabilities;
    }

    public void setVulnerabilities(String vulnerabilities) {
        this.vulnerabilities = vulnerabilities;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public String getDeployEnvId() {
        return deployEnvId;
    }

    public void setDeployEnvId(String deployEnvId) {
        this.deployEnvId = deployEnvId;
    }

    public DeployServer getDeployServer() {
        return deployServer;
    }

    public void setDeployServer(DeployServer deployServer) {
        this.deployServer = deployServer;
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

    public String getScanStatus() {
        return scanStatus;
    }

    public void setScanStatus(String scanStatus) {
        this.scanStatus = scanStatus;
    }

}
