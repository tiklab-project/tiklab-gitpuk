package net.tiklab.xcode.file.model;

public class CodeFile {

    /**
     * 仓库id
     */
    private String codeId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件地址
     */
    private String fileAddress;

    /**
     * 提交信息
     */
    private String commitMessage;

    /**
     * 提交分支
     */
    private String commitBranch;


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }
}
