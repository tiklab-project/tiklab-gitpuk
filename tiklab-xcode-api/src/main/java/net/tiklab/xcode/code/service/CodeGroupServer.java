package net.tiklab.xcode.code.service;

import net.tiklab.join.annotation.FindAll;
import net.tiklab.join.annotation.FindList;
import net.tiklab.join.annotation.FindOne;
import net.tiklab.join.annotation.JoinProvider;
import net.tiklab.xcode.code.model.CodeGroup;


import java.util.List;

@JoinProvider(model = CodeGroup.class)
public interface CodeGroupServer {


    /**
     * 创建仓库组
     * @param codeGroup 信息
     * @return 仓库组id
     */
    String createCodeGroup(CodeGroup codeGroup);

    /**
     * 删除仓库组
     * @param codeGroupId 仓库组id
     */
    void deleteCodeGroup(String codeGroupId);

    /**
     * 更新仓库组
     * @param codeGroup 仓库组信息
     */
    void updateGroup(CodeGroup codeGroup);

    /**
     * 查询单个仓库组
     * @param codeGroupId 仓库组id
     * @return 仓库组信息
     */
    @FindOne
    CodeGroup findOneCodeGroup(String codeGroupId);

    /**
     * 查询所有仓库组
     * @return 仓库组信息列表
     */
    @FindAll
    List<CodeGroup> findAllCodeGroup();


    @FindList
    List<CodeGroup> findAllCodeGroupList(List<String> idList);
    
}
