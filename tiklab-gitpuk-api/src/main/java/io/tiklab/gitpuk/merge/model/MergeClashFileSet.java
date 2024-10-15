package io.tiklab.gitpuk.merge.model;

import io.tiklab.postin.annotation.ApiProperty;

import java.util.List;

/*
* 冲突文件的集合
* */
public class MergeClashFileSet {


    @ApiProperty(name ="mergeOrigin",desc = "合并源")
    private String  mergeOrigin;

    @ApiProperty(name ="mergeTarget",desc = "目标源")
    private String  mergeTarget;

    @ApiProperty(name ="repositoryId",desc = "仓库id")
    private String  repositoryId;

    @ApiProperty(name ="mergeClashFileList",desc = "冲突文件修改后的内容")
    List<MergeClashFile> mergeClashFileList;


    public List<MergeClashFile> getMergeClashFileList() {
        return mergeClashFileList;
    }

    public void setMergeClashFileList(List<MergeClashFile> mergeClashFileList) {
        this.mergeClashFileList = mergeClashFileList;
    }

    public String getMergeOrigin() {
        return mergeOrigin;
    }

    public void setMergeOrigin(String mergeOrigin) {
        this.mergeOrigin = mergeOrigin;
    }

    public String getMergeTarget() {
        return mergeTarget;
    }

    public void setMergeTarget(String mergeTarget) {
        this.mergeTarget = mergeTarget;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }
}
