package io.tiklab.gitpuk.merge.model;

import io.tiklab.gitpuk.commit.model.CommitMessage;
import io.tiklab.postin.annotation.ApiProperty;

import java.util.List;

public class MergeData {

    @ApiProperty(name="mergeRequestId",desc="合并请求的id")
    private String mergeRequestId;

    @ApiProperty(name="rpyId",desc="仓库id")
    private String rpyId;

    @ApiProperty(name="mergeOrigin",desc="合并源")
    private String mergeOrigin;

    @ApiProperty(name="mergeTarget",desc="目标源")
    private String mergeTarget;

    @ApiProperty(name="mergeMessage",desc="合并消息")
    private String mergeMessage;

    @ApiProperty(name="mergeType",desc="合并类型 branch、commit")
    private String mergeType;

    /*
    * createNode 创建一个合并分支记录
    * squash  将合并请求中的提交记录压缩成一条， 然后添加到目标分支。
    * rebase  变基合并  将评审分支提交逐一编辑到目标分支
    * fast   直接修改合并分支的指向
    * */
    @ApiProperty(name="mergeWay",desc="合并方式  createNode、squash、rebase、fast")
    private String mergeWay;
    @ApiProperty(name="deleteOrigin",desc="是否删除源分支")
    private boolean deleteOrigin;

    @ApiProperty(name="filePath",desc="文件路径、冲突文件的路径")
    private String filePath;

    /*-------不同分支差异commitId--------*/

    @ApiProperty(name="execType",desc="提交信息list")
    private List<CommitMessage> commitMessageList;


    @ApiProperty(name="dataList",desc="提交信息list")
    private List<String> dataList;

    public String getRpyId() {
        return rpyId;
    }

    public void setRpyId(String rpyId) {
        this.rpyId = rpyId;
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

    public String getMergeMessage() {
        return mergeMessage;
    }

    public void setMergeMessage(String mergeMessage) {
        this.mergeMessage = mergeMessage;
    }

    public String getMergeType() {
        return mergeType;
    }

    public void setMergeType(String mergeType) {
        this.mergeType = mergeType;
    }

    public String getMergeWay() {
        return mergeWay;
    }

    public void setMergeWay(String mergeWay) {
        this.mergeWay = mergeWay;
    }

    public boolean isDeleteOrigin() {
        return deleteOrigin;
    }

    public void setDeleteOrigin(boolean deleteOrigin) {
        this.deleteOrigin = deleteOrigin;
    }

    public String getMergeRequestId() {
        return mergeRequestId;
    }

    public void setMergeRequestId(String mergeRequestId) {
        this.mergeRequestId = mergeRequestId;
    }

    public List<CommitMessage> getCommitMessageList() {
        return commitMessageList;
    }

    public void setCommitMessageList(List<CommitMessage> commitMessageList) {
        this.commitMessageList = commitMessageList;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<String> getDataList() {
        return dataList;
    }

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
    }
}
