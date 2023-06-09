package io.tiklab.xcode.detection.model;

import io.tiklab.beans.annotation.Mapper;
import io.tiklab.join.annotation.Join;
import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;

import java.sql.Timestamp;

@ApiModel
@Join
@Mapper(targetAlias = "CodeScanInstanceEntity")
public class CodeScanInstance  {

    @ApiProperty(name="id",desc="id")
    private String id;


    @ApiProperty(name="repositoryId",desc="仓库id")
    private String repositoryId;

    @ApiProperty(name="taskName",desc="任务名称")
    private String taskName;

    @ApiProperty(name="runState",desc="执行状态  success、false")
    private String runState;

    @ApiProperty(name="logAddress",desc="日志地址")
    private String logAddress;

    @ApiProperty(name="createTime ",desc="创建时间")
    private Timestamp createTime ;


    /*---------其他字段--------*/
    @ApiProperty(name="runLog",desc="运行日志")
    private String runLog;

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

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getRunState() {
        return runState;
    }

    public void setRunState(String runState) {
        this.runState = runState;
    }

    public String getLogAddress() {
        return logAddress;
    }

    public void setLogAddress(String logAddress) {
        this.logAddress = logAddress;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getRunLog() {
        return runLog;
    }

    public void setRunLog(String runLog) {
        this.runLog = runLog;
    }
}
