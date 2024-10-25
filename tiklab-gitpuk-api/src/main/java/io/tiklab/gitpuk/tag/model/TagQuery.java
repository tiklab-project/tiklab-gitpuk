package io.tiklab.gitpuk.tag.model;

import io.tiklab.postin.annotation.ApiProperty;

public class TagQuery {

    @ApiProperty(name ="rpyId",desc = "分支名称")
    private String  rpyId;

    @ApiProperty(name ="tagName",desc = "标签名字")
    private String  tagName;

    public String getRpyId() {
        return rpyId;
    }

    public void setRpyId(String rpyId) {
        this.rpyId = rpyId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
