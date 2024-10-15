package io.tiklab.gitpuk.repository.model;

import io.tiklab.postin.annotation.ApiProperty;

import java.util.List;

public class RepositoryCleanQuery {

    @ApiProperty(name ="repositoryId",desc = "仓库id")
    private String  repositoryId;

    @ApiProperty(name ="fileSize",desc = "文件大小")
    private Integer  fileSize;

    @ApiProperty(name ="sort",desc = "排序")
    private String  sort;

    @ApiProperty(name ="fileName",desc = "文件名字")
    private String fileName;

    @ApiProperty(name ="fileNameList",desc = "文件名字list")
    private List<String> fileNameList;

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<String> getFileNameList() {
        return fileNameList;
    }

    public void setFileNameList(List<String> fileNameList) {
        this.fileNameList = fileNameList;
    }
}
