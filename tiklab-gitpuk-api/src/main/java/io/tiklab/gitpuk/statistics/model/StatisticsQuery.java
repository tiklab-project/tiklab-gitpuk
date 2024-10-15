package io.tiklab.gitpuk.statistics.model;

import io.tiklab.postin.annotation.ApiProperty;

public class StatisticsQuery {

    @ApiProperty(name ="repositoryId",desc = "仓库id")
    private String  repositoryId;

    @ApiProperty(name ="branch",desc = "分支")
    private String  branch;

    @ApiProperty(name ="commitUser",desc = "提交用户")
    private String  commitUser;


    @ApiProperty(name ="cellTime",desc = "统计时间单位 最近7、15、30、90、0  0代表所有时间")
    private int cellTime;

    @ApiProperty(name ="findNum",desc = "查询数量")
    private int findNum=5;

    @ApiProperty(name ="branchType",desc = "查询的分支类型 all、one")
    private String branchType;

    @ApiProperty(name ="findTime",desc = "查询时间  今天 1、昨天 2、前天 3、7天内 7")
    private int findTime;

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCommitUser() {
        return commitUser;
    }

    public void setCommitUser(String commitUser) {
        this.commitUser = commitUser;
    }


    public int getCellTime() {
        return cellTime;
    }

    public void setCellTime(int cellTime) {
        this.cellTime = cellTime;
    }

    public String getBranchType() {
        return branchType;
    }

    public void setBranchType(String branchType) {
        this.branchType = branchType;
    }

    public int getFindNum() {
        return findNum;
    }

    public void setFindNum(int findNum) {
        this.findNum = findNum;
    }

    public int getFindTime() {
        return findTime;
    }

    public void setFindTime(int findTime) {
        this.findTime = findTime;
    }
}
