package io.thoughtware.gittok.repository.model;

import java.util.List;

public class LeadToResult {

    //总结果
    private String grossResult;

    //错误信息
    private String msg;

    //导入list
    private List<LeadTo> leadToList;


    public String getGrossResult() {
        return grossResult;
    }

    public void setGrossResult(String grossResult) {
        this.grossResult = grossResult;
    }

    public List<LeadTo> getLeadToList() {
        return leadToList;
    }

    public void setLeadToList(List<LeadTo> leadToList) {
        this.leadToList = leadToList;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
