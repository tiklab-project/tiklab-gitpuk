package io.tiklab.gitpuk.repository.model;

import io.tiklab.core.order.Order;
import io.tiklab.core.order.OrderBuilders;
import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel
public class RepositoryCollectQuery implements Serializable {

    @ApiProperty(name ="orderParams",desc = "排序参数")
    private List<Order> orderParams = OrderBuilders.instance().asc("id").get();

    @ApiProperty(name ="repositoryId",desc = "仓库ID")
    private String  repositoryId;


    @ApiProperty(name ="userId",desc = "用户id")
    private String  userId;

    public List<Order> getOrderParams() {
        return orderParams;
    }

    public void setOrderParams(List<Order> orderParams) {
        this.orderParams = orderParams;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public RepositoryCollectQuery setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public RepositoryCollectQuery setUserId(String userId) {
        this.userId = userId;
        return this;
    }
}
