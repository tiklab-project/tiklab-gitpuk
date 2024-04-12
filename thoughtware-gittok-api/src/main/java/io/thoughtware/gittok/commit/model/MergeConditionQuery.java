package io.thoughtware.gittok.commit.model;

import io.thoughtware.core.order.Order;
import io.thoughtware.core.order.OrderBuilders;
import io.thoughtware.core.page.Page;
import io.thoughtware.postin.annotation.ApiProperty;

import java.util.List;

public class MergeConditionQuery {

    @ApiProperty(name ="pageParam",desc = "分页参数")
    private Page pageParam = new Page();

    @ApiProperty(name ="orderParams",desc = "排序参数")
    private List<Order> orderParams = OrderBuilders.instance().asc("createTime").get();
    @ApiProperty(name ="repositoryId",desc = "仓库id")
    private String  repositoryId;

    @ApiProperty(name ="mergeRequestId",desc = "合并请求Id")
    private String  mergeRequestId;

    @ApiProperty(name ="type",desc = "类型")
    private String  type;


    @ApiProperty(name ="findType",desc = "查询类型  all（所有）、exec（执行动态,除去评论的动态）")
    private String  findType;

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

    public void setMergeRequestId(String mergeRequestId) {
        this.mergeRequestId = mergeRequestId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFindType() {
        return findType;
    }

    public void setFindType(String findType) {
        this.findType = findType;
    }
}
