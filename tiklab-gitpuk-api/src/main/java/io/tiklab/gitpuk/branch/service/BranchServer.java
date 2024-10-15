package io.tiklab.gitpuk.branch.service;

import io.tiklab.gitpuk.branch.model.Branch;
import io.tiklab.gitpuk.branch.model.BranchMessage;
import io.tiklab.gitpuk.branch.model.BranchQuery;
import io.tiklab.toolkit.join.annotation.JoinProvider;

import java.util.List;

@JoinProvider(model = Branch.class)
public interface BranchServer {

    /**
     * 查询单个分支
     * @param rpyId 仓库id
     * @return 分支信息列表
     */
     Branch findBranch(String rpyId,String commitId);


    /**
     * 查询仓库所有分支
     * @param rpyId 仓库id
     * @return 分支信息列表
     */
    List<Branch> findAllBranchByRpyId(String rpyId);

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

    /**
     * 条件查询分支
     * @param branchQuery
     */
    List<Branch> findBranchList(BranchQuery branchQuery);

    /**
     * 修改默认分支
     * @param branchQuery
     */
    void updateDefaultBranch( BranchQuery branchQuery);

    /**
     * 合并分支
     * @param branchQuery
     */
    void mergeBranch(BranchQuery branchQuery);
}
