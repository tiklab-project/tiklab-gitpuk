package net.tiklab.xcode.until;

public class FileTree {

    //文件名
    private String fileName;

    //文件类型
    private String fileType;

    //文件地址
    private String fileAddress;

    //提交id
    private String commitId;

    //提交信息
    private String commitMessage;

    //提交时间
    private String commitTime;


    private String path;


    private String fileParent;


    private String branch;


    public String getFileParent() {
        return fileParent;
    }

    public void setFileParent(String fileParent) {
        this.fileParent = fileParent;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileAddress() {
        return fileAddress;
    }

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
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

    public String getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(String commitTime) {
        this.commitTime = commitTime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}




























