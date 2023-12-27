package io.thoughtware.gittok.file.model;


/**
 * 文件树信息
 */

public class FileTreeMessage {

    //仓库id
    private String rpyId;

    //分支
    private String branch;

    //是否查询提交
    private boolean findCommitId;

    //路径
    private String path;


    //查询类型 tag、branch、commit
    private String findType;


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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFindType() {
        return findType;
    }

    public void setFindType(String findType) {
        this.findType = findType;
    }
}
