package net.tiklab.xcode.commit.model;

/**
 * 更改文件列表
 */

public class CommitFileDiffList {

    private String oldFilePath;

    private String oldFileType;

    private String oldFileContent;
    private String newFilePath;

    private String newFileType;

    private String newFileContent;

    private String type;

    private String fileType;

    private int addLine;

    private int deleteLine;

    public int getAddLine() {
        return addLine;
    }

    public void setAddLine(int addLine) {
        this.addLine = addLine;
    }

    public int getDeleteLine() {
        return deleteLine;
    }

    public void setDeleteLine(int deleteLine) {
        this.deleteLine = deleteLine;
    }

    public String getOldFilePath() {
        return oldFilePath;
    }

    public void setOldFilePath(String oldFilePath) {
        this.oldFilePath = oldFilePath;
    }

    public String getNewFilePath() {
        return newFilePath;
    }

    public void setNewFilePath(String newFilePath) {
        this.newFilePath = newFilePath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOldFileType() {
        return oldFileType;
    }

    public void setOldFileType(String oldFileType) {
        this.oldFileType = oldFileType;
    }

    public String getNewFileType() {
        return newFileType;
    }

    public void setNewFileType(String newFileType) {
        this.newFileType = newFileType;
    }

    public String getOldFileContent() {
        return oldFileContent;
    }

    public void setOldFileContent(String oldFileContent) {
        this.oldFileContent = oldFileContent;
    }

    public String getNewFileContent() {
        return newFileContent;
    }

    public void setNewFileContent(String newFileContent) {
        this.newFileContent = newFileContent;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}


























