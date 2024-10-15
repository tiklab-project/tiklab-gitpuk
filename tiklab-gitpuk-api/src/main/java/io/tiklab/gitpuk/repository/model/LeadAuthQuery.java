package io.tiklab.gitpuk.repository.model;

import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;

import java.io.Serializable;

@ApiModel
public class LeadAuthQuery implements Serializable {

    @ApiProperty(name ="type",desc = "类型")
    private String  type;

    @ApiProperty(name ="userId",desc = "用户id")
    private String  userId;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}







































