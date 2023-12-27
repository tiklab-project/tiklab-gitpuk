package io.thoughtware.gittok.scan.entity;

import io.thoughtware.dal.jpa.annotation.*;

import java.sql.Timestamp;

@Entity
@Table(name="rpy_scan_play")
public class ScanPlayEntity {
    @Id
    @GeneratorValue(length=12)
    @Column(name = "id")
    private String id;

    @Column(name = "play_name" ,notNull = true)
    private String playName;

    @Column(name = "repository_id",notNull = true)
    private String repositoryId;

    @Column(name = "branch",notNull = true)
    private String branch;

    @Column(name = "scan_scheme_id",notNull = true)
    private String scanSchemeId;

    @Column(name = "scan_time")
    private Timestamp scanTime;

    @Column(name = "create_time")
    private Timestamp createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlayName() {
        return playName;
    }

    public void setPlayName(String playName) {
        this.playName = playName;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public Timestamp getScanTime() {
        return scanTime;
    }

    public void setScanTime(Timestamp scanTime) {
        this.scanTime = scanTime;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getScanSchemeId() {
        return scanSchemeId;
    }

    public void setScanSchemeId(String scanSchemeId) {
        this.scanSchemeId = scanSchemeId;
    }
}
