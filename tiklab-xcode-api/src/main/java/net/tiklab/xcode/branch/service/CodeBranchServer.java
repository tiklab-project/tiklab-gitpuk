package net.tiklab.xcode.branch.service;

import net.tiklab.join.annotation.FindAll;
import net.tiklab.join.annotation.FindList;
import net.tiklab.join.annotation.FindOne;
import net.tiklab.join.annotation.JoinProvider;
import net.tiklab.xcode.branch.model.BranchMessage;
import net.tiklab.xcode.branch.model.CodeBranch;
import net.tiklab.xcode.code.model.Code;

import java.util.List;

@JoinProvider(model = CodeBranch.class)
public interface CodeBranchServer {



    /**
     * 查询所有分支
     * @param codeId 仓库id
     * @return 分支信息列表
     */
    List<CodeBranch> findAllBranch(String codeId);

    /**
     * 创建分支
     * @param branchMessage 分支信息
     */
    void createBranch(BranchMessage branchMessage);

    /**
     * 删除分支
     * @param branchMessage 分支信息
     */
    void deleteBranch(BranchMessage branchMessage);





}
