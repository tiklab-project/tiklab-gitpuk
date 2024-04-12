package io.thoughtware.gittok.commit.model;

import io.thoughtware.core.BaseModel;
import io.thoughtware.core.order.Order;
import io.thoughtware.core.order.OrderBuilders;
import io.thoughtware.core.page.Page;
import io.thoughtware.postin.annotation.ApiProperty;

import java.util.List;

public class MergeCommentQuery extends BaseModel {

    @ApiProperty(name ="pageParam",desc = "分页参数")
    private Page pageParam = new Page();

    @ApiProperty(name ="orderParams",desc = "排序参数")
    private List<Order> orderParams = OrderBuilders.instance().asc("createTime").get();
    @ApiProperty(name ="repositoryId",desc = "仓库id")
    private String  repositoryId;

    @ApiProperty(name ="mergeRequestId",desc = "合并请求Id")
    private String  mergeRequestId;


    @ApiProperty(name ="mergeConditionId",desc = "合并请求动态Id")
    private String  mergeConditionId;

    public Page getPageParam() {
        return pageParam;
    }

    public void setPageParam(Page pageParam) {
        this.pageParam = pageParam;
    }

    public List<Order> getOrderParams() {
        return orderParams;
    }

    public void setOrderParams(List<Order> orderParams) {
        this.orderParams = orderParams;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getMergeRequestId() {
        return mergeRequestId;
    }

    public MergeCommentQuery setMergeRequestId(String mergeRequestId) {
        this.mergeRequestId = mergeRequestId;
        return this;
    }

    public String getMergeConditionId() {
        return mergeConditionId;
    }

    public MergeCommentQuery setMergeConditionId(String mergeConditionId) {
        this.mergeConditionId = mergeConditionId;
        return  this;
    }
}
