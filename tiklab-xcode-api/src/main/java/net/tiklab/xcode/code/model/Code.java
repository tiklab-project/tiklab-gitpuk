package net.tiklab.xcode.code.model;

import net.tiklab.beans.annotation.Mapper;
import net.tiklab.beans.annotation.Mapping;
import net.tiklab.beans.annotation.Mappings;
import net.tiklab.join.annotation.Join;
import net.tiklab.join.annotation.JoinQuery;
import net.tiklab.postin.annotation.ApiModel;
import net.tiklab.postin.annotation.ApiProperty;
import net.tiklab.user.user.model.User;

@ApiModel
@Join
@Mapper(targetAlias = "CodeEntity")
public class Code {

    @ApiProperty(name="codeId",desc="仓库id")
    private String codeId;

    //仓库组id
    @Mappings({
            @Mapping(source = "codeId",target = "groupId")
    })
    @JoinQuery(key = "codeId")
    private CodeGroup codeGroup;

    @ApiProperty(name="name",desc="仓库名称")
    private String name;

    @ApiProperty(name="address",desc="仓库地址")
    private String address;

    @ApiProperty(name="createTime",desc="创建时间")
    private String createTime;

    @ApiProperty(name="type",desc="类型")
    private int type;

    //创建人
    @Mappings({
            @Mapping(source = "user.id",target = "userId")
    })
    @JoinQuery(key = "id")
    private User user;

    @ApiProperty(name="remarks",desc="描述")
    private String remarks;

    @ApiProperty(name="language",desc="语言")
    private String language;

    @ApiProperty(name="state",desc="仓库状态")
    private int state;


    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public CodeGroup getCodeGroup() {
        return codeGroup;
    }

    public void setCodeGroup(CodeGroup codeGroup) {
        this.codeGroup = codeGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


}







































