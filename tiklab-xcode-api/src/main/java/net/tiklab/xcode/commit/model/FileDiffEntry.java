package net.tiklab.xcode.commit.model;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class FileDiffEntry {

    List<CommitFileDiffList> diffList;

    /**
     * 提交时间
     */
    private String commitTime;

    /**
     * 提交人
     */
    private String commitUser;

    /**
     * 添加行
     */
    private int addLine;

    /**
     * 删除行
     */
    private  int deleteLine;

    public List<CommitFileDiffList> getDiffList() {
        return diffList;
    }

    public void setDiffList(List<CommitFileDiffList> diffList) {
        this.diffList = diffList;
    }

    public String getCommitTime() {
        return commitTime;
    }

    public void setCommitTime(String commitTime) {
        this.commitTime = commitTime;
    }

    public String getCommitUser() {
        return commitUser;
    }

    public void setCommitUser(String commitUser) {
        this.commitUser = commitUser;
    }

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
}
