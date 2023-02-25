package net.tiklab.xcode.setting.model;

import net.tiklab.beans.annotation.Mapper;
import net.tiklab.beans.annotation.Mapping;
import net.tiklab.beans.annotation.Mappings;
import net.tiklab.join.annotation.Join;
import net.tiklab.join.annotation.JoinQuery;
import net.tiklab.postin.annotation.ApiModel;
import net.tiklab.postin.annotation.ApiProperty;
import net.tiklab.user.user.model.User;
import net.tiklab.xcode.repository.model.Code;


@ApiModel
@Join
@Mapper(targetAlias = "CodeAuthEntity")
public class CodeAuth {


    @ApiProperty(name="authId",desc="认证id")
    private String authId;

    @Mappings({
            @Mapping(source = "code.codeId",target = "codeId")
    })
    @JoinQuery(key = "codeId")
    private Code code;

    @ApiProperty(name="title",desc="标题")
    private String title;

    @ApiProperty(name="createTime",desc="创建时间")
    private String createTime;

    @Mappings({
            @Mapping(source = "user.id",target = "userId")
    })
    @JoinQuery(key = "id")
    private User user;


    @ApiProperty(name="value",desc="公钥")
    private String value;

    /**
     * 类型 1,全局 2,项目私有
     */
    @ApiProperty(name="type",desc="类型 1,全局 2,项目私有")
    private String type;

    @ApiProperty(name="userTime",desc="上次使用时间")
    private String userTime;


    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserTime() {
        return userTime;
    }

    public void setUserTime(String userTime) {
        this.userTime = userTime;
    }
}




















































