package io.tiklab.xcode.scan.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.tiklab.beans.annotation.Mapper;
import io.tiklab.beans.annotation.Mapping;
import io.tiklab.beans.annotation.Mappings;
import io.tiklab.join.annotation.Join;
import io.tiklab.join.annotation.JoinQuery;
import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;
import io.tiklab.user.user.model.User;
import io.tiklab.xcode.repository.model.Repository;

import java.io.Serializable;
import java.sql.Timestamp;

@ApiModel
@Join
@Mapper
public class ScanRecord implements Serializable {

    @ApiProperty(name="id",desc="id")
    private String id;

    @ApiProperty(name="scanPlayId",desc="扫描计划id")
    private String scanPlayId;

    @ApiProperty(name="scanObject",desc="扫描对象 （git 提交的id）")
    private String scanObject;

    @ApiProperty(name="repositoryId",desc="仓库")
    private String repositoryId;

    @ApiProperty(name="scanUser",desc="扫描用户")
    @Mappings({
            @Mapping(source = "scanUser.id",target = "scanUserId")
    })
    @JoinQuery(key = "id")
    private User scanUser;

    @ApiProperty(name="scanResult",desc="扫描结果")
    private String scanResult;

    @ApiProperty(name="scanWay",desc="扫描方式 hand：手动")
    private String scanWay;

    @ApiProperty(name="severityTrouble",desc="严重问题")
    private Integer severityTrouble;

    @ApiProperty(name="severityTrouble",desc="错误问题")
    private Integer errorTrouble;

    @ApiProperty(name="warnTrouble",desc="警告问题")
    private Integer warnTrouble;

    @ApiProperty(name="suggestTrouble",desc="建议问题")
    private Integer suggestTrouble;

    @ApiProperty(name="createTime",desc="创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Timestamp createTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScanPlayId() {
        return scanPlayId;
    }

    public void setScanPlayId(String scanPlayId) {
        this.scanPlayId = scanPlayId;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public User getScanUser() {
        return scanUser;
    }

    public void setScanUser(User scanUser) {
        this.scanUser = scanUser;
    }

    public String getScanResult() {
        return scanResult;
    }

    public void setScanResult(String scanResult) {
        this.scanResult = scanResult;
    }

    public Integer getSeverityTrouble() {
        return severityTrouble;
    }

    public void setSeverityTrouble(Integer severityTrouble) {
        this.severityTrouble = severityTrouble;
    }

    public Integer getWarnTrouble() {
        return warnTrouble;
    }

    public void setWarnTrouble(Integer warnTrouble) {
        this.warnTrouble = warnTrouble;
    }

    public Integer getSuggestTrouble() {
        return suggestTrouble;
    }

    public void setSuggestTrouble(Integer suggestTrouble) {
        this.suggestTrouble = suggestTrouble;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getScanWay() {
        return scanWay;
    }

    public void setScanWay(String scanWay) {
        this.scanWay = scanWay;
    }

    public String getScanObject() {
        return scanObject;
    }

    public void setScanObject(String scanObject) {
        this.scanObject = scanObject;
    }

    public Integer getErrorTrouble() {
        return errorTrouble;
    }

    public void setErrorTrouble(Integer errorTrouble) {
        this.errorTrouble = errorTrouble;
    }
}
