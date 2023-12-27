package io.thoughtware.gittok.scan.entity;

import io.thoughtware.dal.jpa.annotation.*;

import java.sql.Timestamp;
/*
 * 扫描规则集实体
 * */
@Entity
@Table(name="rpy_scan_rule_set")
public class ScanRuleSetEntity {
    @Id
    @GeneratorValue(length=12)
    @Column(name = "id")
    private String id;

    @Column(name = "rule_set_name",notNull = true)
    private String ruleSetName;

    @Column(name = "rule_set_type")
    private String ruleSetType;

    @Column(name = "language")
    private String language;


    @Column(name = "describe")
    private String describe;

    @Column(name = "create_time")
    private Timestamp createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRuleSetName() {
        return ruleSetName;
    }

    public void setRuleSetName(String ruleSetName) {
        this.ruleSetName = ruleSetName;
    }

    public String getRuleSetType() {
        return ruleSetType;
    }

    public void setRuleSetType(String ruleSetType) {
        this.ruleSetType = ruleSetType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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
