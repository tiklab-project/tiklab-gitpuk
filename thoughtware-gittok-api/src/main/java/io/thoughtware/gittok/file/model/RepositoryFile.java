package io.thoughtware.gittok.file.model;

import io.thoughtware.postin.annotation.ApiModel;
import io.thoughtware.postin.annotation.ApiProperty;

@ApiModel
public class RepositoryFile {

    @ApiProperty(name="repositoryId",desc="仓库id")
    private String repositoryId;

    @ApiProperty(name="branch",desc="分支")
    private String branch;

    @ApiProperty(name="folderPath",desc="文件夹路径")
    private String folderPath;

    @ApiProperty(name="fileName",desc="文件名字")
    private String fileName;

    @ApiProperty(name="fileName",desc="文件全路径")
    private String filePath;

    @ApiProperty(name="commitMessage",desc="提交信息")
    private String commitMessage;

    @ApiProperty(name="fileData",desc="修改后的文件内容")
    private String fileData;

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getCommitMessage() {
        return commitMessage;
    }

    public void setCommitMessage(String commitMessage) {
        this.commitMessage = commitMessage;
    }

    public String getFileData() {
        return fileData;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
    }
}
