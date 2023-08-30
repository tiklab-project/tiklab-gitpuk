package io.tiklab.xcode.repository.model;

import io.tiklab.core.order.Order;
import io.tiklab.core.order.OrderBuilders;
import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel
public class LeadRecordQuery implements Serializable {

    @ApiProperty(name ="orderParams",desc = "排序参数")
    private List<Order> orderParams = OrderBuilders.instance().asc("id").get();
    @ApiProperty(name ="RpyId",desc = "仓库id")
    private String  RpyId;

    @ApiProperty(name ="leadWay",desc = "导入方式")
    private String  leadWay;



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

    public String getLeadWay() {
        return leadWay;
    }

    public LeadRecordQuery setLeadWay(String leadWay) {
        this.leadWay = leadWay;
        return this;
    }
}
