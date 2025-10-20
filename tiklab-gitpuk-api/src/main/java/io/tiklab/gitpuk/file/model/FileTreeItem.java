package io.tiklab.gitpuk.file.model;

import java.util.List;

public class FileTreeItem {


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

    /**
     * 提交时间
     */
    private String commitTime;

    /**
     * 类型
     */
    private String type;


    //路径
    private String path;

    //文件夹路径
    private String borderPath;

    //界面路由路径 例：code/${path}/${path}
    private String url;


    private String fileParent;


    private String branch;

    //是否是lfs文件
    private boolean isLfs;


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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isLfs() {
        return isLfs;
    }

    public void setLfs(boolean lfs) {
        isLfs = lfs;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBorderPath() {
        return borderPath;
    }

    public void setBorderPath(String borderPath) {
        this.borderPath = borderPath;
    }
}
