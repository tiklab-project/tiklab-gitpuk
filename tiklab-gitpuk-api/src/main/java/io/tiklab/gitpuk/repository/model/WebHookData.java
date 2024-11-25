package io.tiklab.gitpuk.repository.model;

import io.tiklab.gitpuk.branch.model.Branch;
import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;
import io.tiklab.toolkit.beans.annotation.Mapper;
import io.tiklab.toolkit.join.annotation.Join;

@ApiModel
@Join
@Mapper
public class WebHookData {

    @ApiProperty(name="hookName",desc="hook名字")
    private String hookName;

    @ApiProperty(name="hookType",desc="执行事件的具体类型")
    private String hookType;

    @ApiProperty(name="tagName",desc="标签名字")
    private String tagName;

    @ApiProperty(name="branchName",desc="分支名字")
    private String branchName;

    @ApiProperty(name="webHookRepository",desc="仓库")
    private WebHookRepository webHookRepository;


    @ApiProperty(name="webHookMerge",desc="合并")
    private WebHookMerge webHookMerge;


    public String getHookName() {
        return hookName;
    }

    public void setHookName(String hookName) {
        this.hookName = hookName;
    }

    public String getHookType() {
        return hookType;
    }

    public void setHookType(String hookType) {
        this.hookType = hookType;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public WebHookRepository getWebHookRepository() {
        return webHookRepository;
    }

    public void setWebHookRepository(WebHookRepository webHookRepository) {
        this.webHookRepository = webHookRepository;
    }

    public WebHookMerge getWebHookMerge() {
        return webHookMerge;
    }

    public void setWebHookMerge(WebHookMerge webHookMerge) {
        this.webHookMerge = webHookMerge;
    }
}
