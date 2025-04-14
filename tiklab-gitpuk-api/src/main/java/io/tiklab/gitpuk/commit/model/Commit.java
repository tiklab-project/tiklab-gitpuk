package io.tiklab.gitpuk.commit.model;

import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;

/**
 * 获取指定branch的提交信息
 */
@ApiModel
public class Commit {

    @ApiProperty(name="rpyId",desc="仓库id")
    private String rpyId;


    @ApiProperty(name="branch",desc="分支")
    private String branch;

    @ApiProperty(name="refCode",desc="分支、标签名字或者 commitId")
    private String refCode;

    @ApiProperty(name="refCodeType",desc="类型 branch、tag、commit")
    private String refCodeType;


    @ApiProperty(name="filePath",desc="文件地址")
    private String filePath;


    @ApiProperty(name="likePath",desc="模糊查询地址")
    private String likePath;


    @ApiProperty(name="begin",desc="开始数量")
    private int begin;


    @ApiProperty(name="end",desc="结束数量")
    private int end;


    @ApiProperty(name="number",desc="查询所有")
    private String number;


    @ApiProperty(name="commitInfo",desc="输入的查询提交信息")
    private String commitInfo;


    @ApiProperty(name="commitUser",desc="提交人")
    private String commitUser;


    @ApiProperty(name="targetBranch",desc="目标分支")
    private String targetBranch;



    @ApiProperty(name="findCommitId",desc="查询是否是通过commitId查询(通过提交界面查询提交代码文件或者查询代码文件的详情")
    private boolean findCommitId;



    @ApiProperty(name="originCommitId",desc="源commitId （一般都是父级")
    private String originCommitId;


    @ApiProperty(name="commitId",desc="当前的commitId")
    private String commitId;


    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getRpyId() {
        return rpyId;
    }

    public void setRpyId(String rpyId) {
        this.rpyId = rpyId;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public boolean isFindCommitId() {
        return findCommitId;
    }

    public void setFindCommitId(boolean findCommitId) {
        this.findCommitId = findCommitId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getLikePath() {
        return likePath;
    }

    public void setLikePath(String likePath) {
        this.likePath = likePath;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCommitInfo() {
        return commitInfo;
    }

    public void setCommitInfo(String commitInfo) {
        this.commitInfo = commitInfo;
    }

    public String getCommitUser() {
        return commitUser;
    }

    public void setCommitUser(String commitUser) {
        this.commitUser = commitUser;
    }

    public String getTargetBranch() {
        return targetBranch;
    }

    public void setTargetBranch(String targetBranch) {
        this.targetBranch = targetBranch;
    }

    public String getOriginCommitId() {
        return originCommitId;
    }

    public void setOriginCommitId(String originCommitId) {
        this.originCommitId = originCommitId;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }


    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    public String getRefCodeType() {
        return refCodeType;
    }

    public void setRefCodeType(String refCodeType) {
        this.refCodeType = refCodeType;
    }
}






















