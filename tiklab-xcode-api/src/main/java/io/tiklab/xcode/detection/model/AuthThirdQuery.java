package io.tiklab.xcode.detection.model;

import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;

import java.io.Serializable;

@ApiModel
public class AuthThirdQuery implements Serializable {

    @ApiProperty(name ="authName",desc = "认证名称")
    private String  authName;


    public String getAuthName() {
        return authName;
    }

    public AuthThirdQuery setAuthName(String authName) {
        this.authName = authName;
        return  this;
    }

}







































