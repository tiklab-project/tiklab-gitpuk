package io.tiklab.xcode.scan.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.tiklab.beans.annotation.Mapper;
import io.tiklab.beans.annotation.Mapping;
import io.tiklab.beans.annotation.Mappings;
import io.tiklab.join.annotation.Join;
import io.tiklab.join.annotation.JoinQuery;
import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;
import io.tiklab.xcode.repository.model.Repository;

import java.io.Serializable;
import java.sql.Timestamp;

@ApiModel
@Join
@Mapper
public class ScanRule implements Serializable {

    @ApiProperty(name="id",desc="id")
    private String id;

    @ApiProperty(name="ruleSetId",desc="扫描规则集id")
    private String ruleSetId;


    @ApiProperty(name="ruleName",desc="规则名称")
    private String ruleName;

    @ApiProperty(name="scanTool",desc="扫描工具")
    private String scanTool;

    @ApiProperty(name="ruleOverview",desc="规则概述")
    private String ruleOverview;

    @ApiProperty(name="problemLevel",desc="规则等级 严重：1、警告：2、建议：3")
    private Integer problemLevel;

    @ApiProperty(name="describe",desc="规则描述")
    private String describe;

    @ApiProperty(name="createTime",desc="创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Timestamp createTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRuleSetId() {
        return ruleSetId;
    }

    public void setRuleSetId(String ruleSetId) {
        this.ruleSetId = ruleSetId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getScanTool() {
        return scanTool;
    }

    public void setScanTool(String scanTool) {
        this.scanTool = scanTool;
    }

    public String getRuleOverview() {
        return ruleOverview;
    }

    public void setRuleOverview(String ruleOverview) {
        this.ruleOverview = ruleOverview;
    }

    public Integer getProblemLevel() {
        return problemLevel;
    }

    public void setProblemLevel(Integer problemLevel) {
        this.problemLevel = problemLevel;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
