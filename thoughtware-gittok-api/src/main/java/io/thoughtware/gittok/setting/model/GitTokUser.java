package io.thoughtware.gittok.setting.model;

import io.thoughtware.postin.annotation.ApiModel;
import io.thoughtware.postin.annotation.ApiProperty;

import java.io.Serializable;

@ApiModel
public class GitTokUser implements Serializable {

    @ApiProperty(name="userId",desc="用户id")
    private  String userId;

    @ApiProperty(name="nickName",desc="用户昵称")
    private  String nickName;

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
