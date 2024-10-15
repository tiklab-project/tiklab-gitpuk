package io.tiklab.gitpuk.commit.model;

import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;

/*
* 不同分支、提交的差异数据统计
* */
@ApiModel
public class CommitDiffData {

    @ApiProperty(name="commitNum",desc="不同提交数量")
    private Integer commitNum;

    @ApiProperty(name="fileNum",desc="不同文件数量")
    private Integer fileNum;

    @ApiProperty(name="fileNum",desc="是否有冲突 0:没有 1有冲突")
    private  Integer clash;

    public Integer getCommitNum() {
        return commitNum;
    }

    public void setCommitNum(Integer commitNum) {
        this.commitNum = commitNum;
    }

    public Integer getFileNum() {
        return fileNum;
    }

    public void setFileNum(Integer fileNum) {
        this.fileNum = fileNum;
    }

    public int getClash() {
        return clash;
    }

    public void setClash(int clash) {
        this.clash = clash;
    }
}
