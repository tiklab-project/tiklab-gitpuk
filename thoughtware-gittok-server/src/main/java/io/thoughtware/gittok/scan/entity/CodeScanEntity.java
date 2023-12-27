package io.thoughtware.gittok.scan.entity;

import io.thoughtware.dal.jpa.annotation.*;

import java.sql.Timestamp;

@Entity
@Table(name="rpy_code_scan")
public class CodeScanEntity {

    @Id
    @GeneratorValue(length=12)
    @Column(name = "id")
    private String id;

    @Column(name = "task_name" ,notNull = true)
    private String taskName;

    @Column(name = "repository_id" ,notNull = true)
    private String repositoryId;

    @Column(name = "deploy_env_id")
    private String deployEnvId;

    @Column(name = "deploy_server_id")
    private String deployServerId;

    @Column(name = "scan_status" )
    private String scanStatus;


    @Column(name = "bugs" )
    private String bugs;

    @Column(name = "code_smells" )
    private String codeSmells;

    @Column(name = "vulnerabilities" )
    private String vulnerabilities;

    @Column(name = "update_time" )
    private Timestamp updateTime;

    @Column(name = "create_time" )
    private Timestamp createTime;

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

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getDeployEnvId() {
        return deployEnvId;
    }

    public void setDeployEnvId(String deployEnvId) {
        this.deployEnvId = deployEnvId;
    }

    public String getDeployServerId() {
        return deployServerId;
    }

    public void setDeployServerId(String deployServerId) {
        this.deployServerId = deployServerId;
    }

    public String getScanStatus() {
        return scanStatus;
    }

    public void setScanStatus(String scanStatus) {
        this.scanStatus = scanStatus;
    }

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
}
