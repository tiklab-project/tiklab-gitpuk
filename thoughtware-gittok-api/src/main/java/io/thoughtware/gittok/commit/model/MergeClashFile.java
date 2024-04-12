package io.thoughtware.gittok.commit.model;

import io.thoughtware.postin.annotation.ApiModel;
import io.thoughtware.postin.annotation.ApiProperty;

/*
*合并冲突的文件
* */
@ApiModel
public class MergeClashFile {

    @ApiProperty(name="filePath",desc="文件路径")
    private String filePath;

    @ApiProperty(name="fileData",desc="文件内容")
    private String fileData;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileData() {
        return fileData;
    }

    public void setFileData(String fileData) {
        this.fileData = fileData;
    }
}
