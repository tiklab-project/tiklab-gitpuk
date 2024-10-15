package io.tiklab.gitpuk.repository.model;

import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;

import java.util.List;

@ApiModel
public class LeadToQuery {

    @ApiProperty(name="leadToList",desc="需要导入的list")
    private List<LeadTo> leadToList;

    @ApiProperty(name="importAuthId",desc="导入认证的id")
    private String importAuthId;

    @ApiProperty(name="userId",desc="userId")
    private String userId;

    @ApiProperty(name="page",desc="page")
    private Integer page ;


    @ApiProperty(name="nextPageStart",desc="bitbucket 的下一页查询")
    private Integer nextPageStart ;

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

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getNextPageStart() {
        return nextPageStart;
    }

    public void setNextPageStart(Integer nextPageStart) {
        this.nextPageStart = nextPageStart;
    }
}
