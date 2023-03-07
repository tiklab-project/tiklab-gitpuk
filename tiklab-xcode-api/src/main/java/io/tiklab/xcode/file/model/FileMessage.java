package io.tiklab.xcode.file.model;

/**
 * 返回前端文件信息
 */
public class FileMessage {


    /**
     * 文件类型
     */
    private String fileType;

    /**
     * 文件信息
     */
    private String fileMessage;

    /**
     * 文件名称
     */
    private String fileName;


    /**
     * 文件路径
     */
    private String filePath;


    /**
     * 文件大小
     */
    private String fileSize;


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileMessage() {
        return fileMessage;
    }

    public void setFileMessage(String fileMessage) {
        this.fileMessage = fileMessage;
    }




}























