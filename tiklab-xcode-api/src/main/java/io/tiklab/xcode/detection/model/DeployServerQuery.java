package io.tiklab.xcode.detection.model;

import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;

import java.io.Serializable;

@ApiModel
public class DeployServerQuery implements Serializable {

    @ApiProperty(name ="serverName",desc = "服务名称")
    private String  serverName;


    public String getServerName() {
        return serverName;
    }

    public DeployServerQuery setServerName(String serverName) {
        this.serverName = serverName;
        return this;
    }
}







































