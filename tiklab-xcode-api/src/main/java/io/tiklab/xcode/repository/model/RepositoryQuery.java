package io.tiklab.xcode.repository.model;

import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;

import java.io.Serializable;

@ApiModel
public class RepositoryQuery implements Serializable {

    @ApiProperty(name ="userId",desc = "登录用户")
    private String  userId;

    @ApiProperty(name ="sort",desc = "排序方式")
    private String  sort;

    @ApiProperty(name ="userId",desc = "仓库名称")
    private String  name;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
}







































