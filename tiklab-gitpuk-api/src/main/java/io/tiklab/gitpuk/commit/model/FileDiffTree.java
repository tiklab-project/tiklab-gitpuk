package io.tiklab.gitpuk.commit.model;

import java.util.List;

public class FileDiffTree {

    //文件、文件夹路径
    private String folderPath;

    //名称
    private String fileName;

    //文件类型 tree 、file
    private String fileType;

    //类型 ADD、DELETE
    private String type;

    //父级别的folder
    private String prentFolder;


    //文件数量
    private Integer fileNum;

    private List<FileDiffTree> fileDiffTreeList;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public List<FileDiffTree> getFileDiffTreeList() {
        return fileDiffTreeList;
    }

    public void setFileDiffTreeList(List<FileDiffTree> fileDiffTreeList) {
        this.fileDiffTreeList = fileDiffTreeList;
    }

    public String getPrentFolder() {
        return prentFolder;
    }

    public void setPrentFolder(String prentFolder) {
        this.prentFolder = prentFolder;
    }

    public Integer getFileNum() {
        return fileNum;
    }

    public void setFileNum(Integer fileNum) {
        this.fileNum = fileNum;
    }
}
