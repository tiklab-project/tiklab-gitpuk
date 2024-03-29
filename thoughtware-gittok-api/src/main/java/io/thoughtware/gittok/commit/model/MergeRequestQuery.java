package io.thoughtware.gittok.commit.model;

import io.thoughtware.core.order.Order;
import io.thoughtware.core.order.OrderBuilders;
import io.thoughtware.core.page.Page;
import io.thoughtware.postin.annotation.ApiProperty;

import java.util.List;

public class MergeRequestQuery {

    @ApiProperty(name ="pageParam",desc = "分页参数")
    private Page pageParam = new Page();

    @ApiProperty(name ="orderParams",desc = "排序参数")
    private List<Order> orderParams = OrderBuilders.instance().desc("createTime").get();
    @ApiProperty(name ="RpyId",desc = "仓库id")
    private String  RpyId;

    @ApiProperty(name ="mergeState",desc = "和平请求状态")
    private Integer  mergeState;

    @ApiProperty(name ="title",desc = "标题")
    private String  title;


    public List<Order> getOrderParams() {
        return orderParams;
    }

    public void setOrderParams(List<Order> orderParams) {
        this.orderParams = orderParams;
    }

    public String getRpyId() {
        return RpyId;
    }

    public void setRpyId(String rpyId) {
        RpyId = rpyId;
    }

    public Page getPageParam() {
        return pageParam;
    }

    public void setPageParam(Page pageParam) {
        this.pageParam = pageParam;
    }

    public Integer getMergeState() {
        return mergeState;
    }

    public void setMergeState(Integer mergeState) {
        this.mergeState = mergeState;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
