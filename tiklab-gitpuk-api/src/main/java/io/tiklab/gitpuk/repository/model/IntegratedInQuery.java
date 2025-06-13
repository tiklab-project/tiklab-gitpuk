package io.tiklab.gitpuk.repository.model;

import io.tiklab.core.page.Page;
import io.tiklab.postin.annotation.ApiProperty;

import java.util.List;

public class IntegratedInQuery {

    @ApiProperty(name ="pageParam",desc = "分页参数")
    private Page pageParam = new Page();

    @ApiProperty(name ="pipelineName",desc = "流水线名字")
    private String  pipelineName;

    @ApiProperty(name ="address",desc = "地址")
    private String  address;

    @ApiProperty(name ="relevancyIdS",desc = "关联的ids")
    private List<String> relevancyIdS;


    @ApiProperty(name ="repositoryId",desc = "仓库id")
    private String repositoryId;


    @ApiProperty(name ="userId",desc = "用户id")
    private String  userId;


    @ApiProperty(name ="userName",desc = "用户名、账号")
    private String  userName;

    @ApiProperty(name ="password",desc = "密码")
    private String  password;


    public Page getPageParam() {
        return pageParam;
    }

    public void setPageParam(Page pageParam) {
        this.pageParam = pageParam;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPipelineName() {
        return pipelineName;
    }

    public void setPipelineName(String pipelineName) {
        this.pipelineName = pipelineName;
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public List<String> getRelevancyIdS() {
        return relevancyIdS;
    }

    public void setRelevancyIdS(List<String> relevancyIdS) {
        this.relevancyIdS = relevancyIdS;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
