package io.thoughtware.gittok.repository.model;

import io.thoughtware.postin.annotation.ApiModel;
import io.thoughtware.postin.annotation.ApiProperty;

import java.util.List;

@ApiModel
public class LeadToQuery {

    @ApiProperty(name="leadToList",desc="需要导入的list")
    private List<LeadTo> leadToList;

    @ApiProperty(name="importAuthId",desc="导入认证的id")
    private String importAuthId;

    @ApiProperty(name="userId",desc="userId")
    private String userId;

    public List<LeadTo> getLeadToList() {
        return leadToList;
    }

    public void setLeadToList(List<LeadTo> leadToList) {
        this.leadToList = leadToList;
    }

    public String getImportAuthId() {
        return importAuthId;
    }

    public void setImportAuthId(String importAuthId) {
        this.importAuthId = importAuthId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
