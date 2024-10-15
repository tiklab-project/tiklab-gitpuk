package io.tiklab.gitpuk.file.model;

/**
 * 文件下载信息
 */
public class FileDownload {

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 仓库id
     */
    private String rpyId;

    /**
     * lfs文件的 oid
     */
    private String oid;

    /**
     * 文件分支
     */
    private String branch;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getRpyId() {
        return rpyId;
    }

    public void setRpyId(String rpyId) {
        this.rpyId = rpyId;
    }
}
