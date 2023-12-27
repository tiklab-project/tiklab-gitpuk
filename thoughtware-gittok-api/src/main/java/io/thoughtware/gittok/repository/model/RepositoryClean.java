package io.thoughtware.gittok.repository.model;

import io.thoughtware.postin.annotation.ApiModel;
import io.thoughtware.postin.annotation.ApiProperty;

@ApiModel
public class RepositoryClean {

    @ApiProperty(name="fileName",desc="文件名称")
    private String fileName;

    @ApiProperty(name="fileSize",desc="文件大小")
    private Long fileSize;

    @ApiProperty(name="size",desc="文件大小")
    private String size;


    @ApiProperty(name="msg",desc="消息")
    private String msg;
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
