package io.thoughtware.gittork.setting.model;

import io.thoughtware.postin.annotation.ApiModel;
import io.thoughtware.postin.annotation.ApiProperty;

import java.io.Serializable;

@ApiModel
public class GitTorkUser implements Serializable {

    @ApiProperty(name="userId",desc="用户id")
    private  String userId;

    @ApiProperty(name="userName",desc="用户名字")
    private  String userName;

    @ApiProperty(name="repositoryNum",desc="仓库数量")
    private Integer repositoryNum;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



    public Integer getRepositoryNum() {
        return repositoryNum;
    }

    public void setRepositoryNum(Integer repositoryNum) {
        this.repositoryNum = repositoryNum;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
