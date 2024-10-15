package io.tiklab.gitpuk.statistics.model;

import java.util.List;

public class Statistics {


    //提交总数
    private int count;

    //添加代码数
    private int addCodeCount;

    //减少代码数
    private int deleteCodeCount;

    // 当前日期
    private List<String> dateList;

    //提交数量
    private List<Integer> commitNumList;

    //添加代码数量
    private List<Integer> addLine;

    //减少代码数量
    private List<Integer> deleteLine;


    //用户list
    private List<String> userList;


    //仓库list
    private List<String> repositoryList;

    //合并请求list
    private List<Integer> mergeRequestList;

    private List<UserStatistics> userStatisticsList;



    public List<String> getDateList() {
        return dateList;
    }

    public void setDateList(List<String> dateList) {
        this.dateList = dateList;
    }


    public List<Integer> getCommitNumList() {
        return commitNumList;
    }

    public void setCommitNumList(List<Integer> commitNumList) {
        this.commitNumList = commitNumList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Integer> getAddLine() {
        return addLine;
    }

    public void setAddLine(List<Integer> addLine) {
        this.addLine = addLine;
    }

    public List<Integer> getDeleteLine() {
        return deleteLine;
    }

    public void setDeleteLine(List<Integer> deleteLine) {
        this.deleteLine = deleteLine;
    }

    public int getAddCodeCount() {
        return addCodeCount;
    }

    public void setAddCodeCount(int addCodeCount) {
        this.addCodeCount = addCodeCount;
    }

    public int getDeleteCodeCount() {
        return deleteCodeCount;
    }

    public void setDeleteCodeCount(int deleteCodeCount) {
        this.deleteCodeCount = deleteCodeCount;
    }

    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }

    public List<String> getRepositoryList() {
        return repositoryList;
    }

    public void setRepositoryList(List<String> repositoryList) {
        this.repositoryList = repositoryList;
    }

    public List<Integer> getMergeRequestList() {
        return mergeRequestList;
    }

    public void setMergeRequestList(List<Integer> mergeRequestList) {
        this.mergeRequestList = mergeRequestList;
    }

    public List<UserStatistics> getUserStatisticsList() {
        return userStatisticsList;
    }

    public void setUserStatisticsList(List<UserStatistics> userStatisticsList) {
        this.userStatisticsList = userStatisticsList;
    }
}
