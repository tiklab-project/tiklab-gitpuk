package io.thoughtware.gittok.scan.entity;

import io.thoughtware.dal.jpa.annotation.*;

import java.sql.Timestamp;

@Entity
@Table(name="rpy_scan_scheme_sonar")
public class ScanSchemeSonarEntity {
    @Id
    @GeneratorValue(length=12)
    @Column(name = "id")
    private String id;

    @Column(name = "scan_scheme_id" ,notNull = true)
    private String scanSchemeId;

    @Column(name = "deploy_env_id")
    private String deployEnvId;

    @Column(name = "deploy_server")
    private String deployServer;

    @Column(name = "create_time")
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

    public String getDeployEnvId() {
        return deployEnvId;
    }

    public void setDeployEnvId(String deployEnvId) {
        this.deployEnvId = deployEnvId;
    }

    public String getDeployServer() {
        return deployServer;
    }

    public void setDeployServer(String deployServer) {
        this.deployServer = deployServer;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
