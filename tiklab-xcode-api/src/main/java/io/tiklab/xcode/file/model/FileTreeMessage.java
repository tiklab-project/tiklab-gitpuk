package io.tiklab.xcode.file.model;


/**
 * 文件树信息
 */

public class FileTreeMessage {

    private String rpyId;


    private String branch;


    private boolean findCommitId;


    private String path;


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
}
