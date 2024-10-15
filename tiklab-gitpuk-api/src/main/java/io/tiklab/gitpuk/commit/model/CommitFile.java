package io.tiklab.gitpuk.commit.model;


/**
 * 读取具体文件行信息
 */
public class CommitFile {


    /**
     * 仓库id
     */
    private String rpyId;

    /**
     * 提交记录id
     */
    private String commitId;

    /**
     * 分支
     */
    private String branch;

    /**
     * 文件地址
     */
    private String path;

    /**
     * 获取文件行数
     */
    private int count;

    /**
     * 旧文件行数
     */
    private int oldStn;

    /**
     * 新文件行数
     */
    private int newStn;

    /**
     * 获取类型 up:向上 down:向下获取
     */
    private String direction;


    /**
     *查询类型 通过提交id查询：commit、通过分支查询：branch
     */
    private String queryType;

    public String getCommitId() {
        return commitId;
    }

    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    public String getRpyId() {
        return rpyId;
    }

    public void setRpyId(String rpyId) {
        this.rpyId = rpyId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getOldStn() {
        return oldStn;
    }

    public void setOldStn(int oldStn) {
        this.oldStn = oldStn;
    }

    public int getNewStn() {
        return newStn;
    }

    public void setNewStn(int newStn) {
        this.newStn = newStn;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }
}
