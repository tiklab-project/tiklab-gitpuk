package io.tiklab.xcode.commit.model;

/**
 * 获取指定branch的提交信息
 */
public class Commit {

    /**
     * 仓库id
     */
    private String rpyId;

    /**
     * 分支
     */
    private String branch;

    /**
     * 分支是否为commitId
     */
    private boolean findCommitId;

    /**
     * 文件地址
     */
    private String filePath;

    /**
     * 模糊查询地址
     */
    private String likePath;

    /**
     * 开始数量
     */
    private int begin;

    /**
     * 结束数量
     */
    private int end;

    /**
     * 查询所有
     */
    private String number;


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
}






















