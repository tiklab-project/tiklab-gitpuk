package net.tiklab.xcode.file.model;


/**
 * 文件树信息
 */

public class FileTreeMessage {

    private String codeId;


    private String branch;


    private boolean findCommitId;


    private String path;


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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
