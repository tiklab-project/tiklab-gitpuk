package net.tiklab.xcode.commit.model;

/**
 * 获取指定branch的提交信息
 */
public class Commit {

    /**
     * 仓库id
     */
    private String codeId;

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


    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
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
}






















