package io.tiklab.xcode.file.model;

/**
 * 更改文件信息
 */

public class FileQuery {

    /**
     * 仓库id
     */
    private String rpyId;

    /**
     * 文件新名称
     */
    private String newFileName;

    /**
     * 文件旧名称
     */
    private String oldFileName;

    /**
     * 文件地址
     */
    private String fileAddress;

    /**
     * 文件内容
     */
    private String fileContent;

    /**
     * 提交信息
     */
    private String commitMessage;

    /**
     * 提交分支
     */
    private String commitBranch;

    /**
     * 是否为commitId
     */
    public boolean findCommitId;


    public String getNewFileName() {
        return newFileName;
    }

    public void setNewFileName(String newFileName) {
        this.newFileName = newFileName;
    }

    public String getOldFileName() {
        return oldFileName;
    }

    public void setOldFileName(String oldFileName) {
        this.oldFileName = oldFileName;
    }

    public String getFileAddress() {
        return fileAddress;
    }

    public void setFileAddress(String fileAddress) {
        this.fileAddress = fileAddress;
    }

    public String getCommitMessage() {
        return commitMessage;
    }

    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

    public String getCommitBranch() {
        return commitBranch;
    }

    public void setCommitBranch(String commitBranch) {
        this.commitBranch = commitBranch;
    }

    public String getRpyId() {
        return rpyId;
    }

    public void setRpyId(String rpyId) {
        this.rpyId = rpyId;
    }

    public String getFileContent() {
        return fileContent;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public boolean isFindCommitId() {
        return findCommitId;
    }

    public void setFindCommitId(boolean findCommitId) {
        this.findCommitId = findCommitId;
    }
}
