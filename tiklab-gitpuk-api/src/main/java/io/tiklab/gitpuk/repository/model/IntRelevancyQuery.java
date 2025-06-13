package io.tiklab.gitpuk.repository.model;

import io.tiklab.core.order.Order;
import io.tiklab.core.order.OrderBuilders;
import io.tiklab.core.page.Page;
import io.tiklab.postin.annotation.ApiProperty;

import java.util.List;

/**
 * 集成关联表
 */

public class IntRelevancyQuery {

    @ApiProperty(name ="pageParam",desc = "分页参数")
    private Page pageParam = new Page();

    @ApiProperty(name ="orderParams",desc = "排序参数")
    private List<Order> orderParams = OrderBuilders.instance().desc("createTime").get();

    @ApiProperty(name ="repositoryId",desc = "仓库id")
    private String  repositoryId;

    @ApiProperty(name ="relevancyId",desc = "关联的id")
    private String  relevancyId;

    @ApiProperty(name ="type",desc = "type")
    private String  type;

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

    public IntRelevancyQuery setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
        return this;
    }

    public String getRelevancyId() {
        return relevancyId;
    }

    public void setRelevancyId(String relevancyId) {
        this.relevancyId = relevancyId;
    }

    public  String getType() {
        return type;
    }

    public IntRelevancyQuery setType(String type) {
        this.type = type;
        return this;
    }
}
