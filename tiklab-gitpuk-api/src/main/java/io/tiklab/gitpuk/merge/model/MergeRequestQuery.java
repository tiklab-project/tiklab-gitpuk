package io.tiklab.gitpuk.merge.model;

import io.tiklab.core.order.Order;
import io.tiklab.core.order.OrderBuilders;
import io.tiklab.core.page.Page;
import io.tiklab.postin.annotation.ApiProperty;

import java.util.List;

public class MergeRequestQuery {

    @ApiProperty(name ="pageParam",desc = "分页参数")
    private Page pageParam = new Page();

    @ApiProperty(name ="orderParams",desc = "排序参数")
    private List<Order> orderParams = OrderBuilders.instance().desc("createTime").get();
    @ApiProperty(name ="RpyId",desc = "仓库id")
    private String  rpyId;

    @ApiProperty(name ="mergeState",desc = "合并请求状态")
    private Integer  mergeState;

    @ApiProperty(name ="title",desc = "标题")
    private String  title;

    @ApiProperty(name ="mergeOrigin",desc = "合并源")
    private String  mergeOrigin;

    @ApiProperty(name ="mergeTarget",desc = "目标源")
    private String  mergeTarget;


    @ApiProperty(name ="startTime",desc = "查询开始时间")
    private String  startTime;

    @ApiProperty(name ="endTime",desc = "查询结束时间")
    private String  endTime;

    public List<Order> getOrderParams() {
        return orderParams;
    }

    public void setOrderParams(List<Order> orderParams) {
        this.orderParams = orderParams;
    }

    public String getRpyId() {
        return rpyId;
    }

    public MergeRequestQuery setRpyId(String rpyId) {
        this.rpyId = rpyId;
        return this;
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

    public MergeRequestQuery setMergeState(Integer mergeState) {
        this.mergeState = mergeState;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMergeOrigin() {
        return mergeOrigin;
    }

    public void setMergeOrigin(String mergeOrigin) {
        this.mergeOrigin = mergeOrigin;
    }

    public String getMergeTarget() {
        return mergeTarget;
    }

    public void setMergeTarget(String mergeTarget) {
        this.mergeTarget = mergeTarget;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
