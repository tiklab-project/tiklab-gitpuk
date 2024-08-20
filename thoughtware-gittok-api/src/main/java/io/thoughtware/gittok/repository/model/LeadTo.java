package io.thoughtware.gittok.repository.model;

import io.thoughtware.postin.annotation.ApiModel;
import io.thoughtware.postin.annotation.ApiProperty;

import java.util.List;

@ApiModel
public class LeadTo {

    @ApiProperty(name="repositoryName",desc="仓库名字")
    private String repositoryName;

    @ApiProperty(name="groupName",desc="仓库组名字")
    private String groupName;

    @ApiProperty(name="rule",desc="权限")
    private String rule;

    @ApiProperty(name="repositoryUrl",desc="仓库路径")
    private String repositoryUrl;

    @ApiProperty(name="httpRepositoryUrl",desc="远程仓库地址clone地址")
    private String httpRepositoryUrl;

    @ApiProperty(name="userId",desc="执行导入人")
    private String userId;

    @ApiProperty(name="thirdRepositoryId",desc="第三方仓库的id")
    private String thirdRepositoryId;

    @ApiProperty(name="importAuthId",desc="导入认证的id")
    private String importAuthId;

    @ApiProperty(name="execResult",desc="执行结果")
    private String execResult;


    @ApiProperty(name="log",desc="日志")
    private String log;

    @ApiProperty(name="pageStartList",desc="pageStartList")
    private List<Integer> pageStartList;

    @ApiProperty(name="currentPageStart",desc="bitbucket 当前的start")
    private Integer currentPageStart;

    @ApiProperty(name="nextPageStart",desc="bitbucket 的下一页查询")
    private Integer nextPageStart;

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getHttpRepositoryUrl() {
        return httpRepositoryUrl;
    }

    public void setHttpRepositoryUrl(String httpRepositoryUrl) {
        this.httpRepositoryUrl = httpRepositoryUrl;
    }

    public String getThirdRepositoryId() {
        return thirdRepositoryId;
    }

    public void setThirdRepositoryId(String thirdRepositoryId) {
        this.thirdRepositoryId = thirdRepositoryId;
    }

    public String getImportAuthId() {
        return importAuthId;
    }

    public void setImportAuthId(String importAuthId) {
        this.importAuthId = importAuthId;
    }

    public String getExecResult() {
        return execResult;
    }

    public void setExecResult(String execResult) {
        this.execResult = execResult;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Integer getNextPageStart() {
        return nextPageStart;
    }

    public void setNextPageStart(Integer nextPageStart) {
        this.nextPageStart = nextPageStart;
    }

    public List<Integer> getPageStartList() {
        return pageStartList;
    }

    public void setPageStartList(List<Integer> pageStartList) {
        this.pageStartList = pageStartList;
    }

    public Integer getCurrentPageStart() {
        return currentPageStart;
    }

    public void setCurrentPageStart(Integer currentPageStart) {
        this.currentPageStart = currentPageStart;
    }
}
