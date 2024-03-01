package io.thoughtware.gittok.repository.model;

import io.thoughtware.core.order.Order;
import io.thoughtware.core.order.OrderBuilders;
import io.thoughtware.core.page.Page;
import io.thoughtware.postin.annotation.ApiModel;
import io.thoughtware.postin.annotation.ApiProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel
public class RepositoryQuery implements Serializable {

    @ApiProperty(name ="pageParam",desc = "分页参数")
    private Page pageParam = new Page();

    @ApiProperty(name ="orderParams",desc = "排序参数")
    private List<Order> orderParams = OrderBuilders.instance().desc("createTime").get();

    @ApiProperty(name ="userId",desc = "登录用户")
    private String  userId;

    @ApiProperty(name ="sort",desc = "排序方式 desc、asc")
    private String  sort;

    @ApiProperty(name ="userId",desc = "仓库名称")
    private String  name;

    @ApiProperty(name ="groupId",desc = "仓库组id")
    private String  groupId;

    @ApiProperty(name ="address",desc = "仓库地址")
    private String  address;

    @ApiProperty(name ="findType",desc = "查询类型 自己创建的：oneself、有权限看的：viewable")
    private String  findType;


    @ApiProperty(name ="groupName",desc = "仓库组名字")
    private String  groupName;

    @ApiProperty(name ="category",desc = "1 、演示 ；2、正式仓库")
    private Integer  category;


    public String getUserId() {
        return userId;
    }

    public RepositoryQuery setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public RepositoryQuery setName(String name) {
        this.name = name;
        return this;
    }

    public Page getPageParam() {
        return pageParam;
    }

    public void setPageParam(Page pageParam) {
        this.pageParam = pageParam;
    }

    public String getGroupId() {
        return groupId;
    }

    public RepositoryQuery setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public RepositoryQuery setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getFindType() {
        return findType;
    }

    public void setFindType(String findType) {
        this.findType = findType;
    }

    public String getGroupName() {
        return groupName;
    }

    public RepositoryQuery setGroupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public Integer getCategory() {
        return category;
    }

    public RepositoryQuery setCategory(Integer category) {
        this.category = category;
        return this;
    }

    public List<Order> getOrderParams() {
        return orderParams;
    }

    public void setOrderParams(List<Order> orderParams) {
        this.orderParams = orderParams;
    }
}







































