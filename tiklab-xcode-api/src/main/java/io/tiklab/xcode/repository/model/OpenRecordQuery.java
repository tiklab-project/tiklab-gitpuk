package io.tiklab.xcode.repository.model;

import io.tiklab.postin.annotation.ApiModel;
import io.tiklab.postin.annotation.ApiProperty;

import java.io.Serializable;

@ApiModel
public class OpenRecordQuery implements Serializable {

    @ApiProperty(name ="userId",desc = "登录用户")
    private String  userId;

    @ApiProperty(name ="repositoryId",desc = "仓库id")
    private String  repositoryId;


    public String getUserId() {
        return userId;
    }

    public OpenRecordQuery setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public OpenRecordQuery setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
        return this;
    }
}
