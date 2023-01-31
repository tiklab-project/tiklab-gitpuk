package net.tiklab.xcode.branch.model;

public class BranchMessage {

    /**
     * 新建分支名称
     */
    private String branchName;

    /**
     * 分支来源
     */
    private String point;

    /**
     * 仓库id
     */
    private String codeId;


    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }
}
