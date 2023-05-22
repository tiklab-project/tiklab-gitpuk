package io.tiklab.xcode.detection.model;

import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;

import java.io.Serializable;

@ApiModel
public class ScmAddressQuery implements Serializable {

    @ApiProperty(name ="scmType",desc = "类型")
    private String  scmType;

    @ApiProperty(name ="scmName",desc = "名称")
    private String  scmName;


    public String getScmType() {
        return scmType;
    }

    public ScmAddressQuery setScmType(String scmType) {
        this.scmType = scmType;
        return this;
    }

    public String getScmName() {
        return scmName;
    }

    public void setScmName(String scmName) {
        this.scmName = scmName;
    }
}







































