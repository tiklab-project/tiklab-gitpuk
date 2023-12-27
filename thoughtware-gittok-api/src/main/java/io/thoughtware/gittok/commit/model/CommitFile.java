package io.thoughtware.gittok.commit.model;


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
}
