package io.thoughtware.gittok.repository.model;

import java.util.List;

public class LeadToResult {

    //总结果
    private String grossResult;

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
}
