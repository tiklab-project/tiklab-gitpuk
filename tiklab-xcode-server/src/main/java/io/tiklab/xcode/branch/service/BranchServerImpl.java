package io.tiklab.xcode.branch.service;

import io.tiklab.core.exception.ApplicationException;
import io.tiklab.rpc.annotation.Exporter;
import io.tiklab.xcode.branch.model.BranchMessage;
import io.tiklab.xcode.branch.model.Branch;
import io.tiklab.xcode.branch.service.BranchServer;
import io.tiklab.xcode.repository.model.Repository;
import io.tiklab.xcode.repository.service.RepositoryServer;
import io.tiklab.xcode.git.GitBranchUntil;
import io.tiklab.xcode.util.RepositoryUtil;
import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Exporter
public class BranchServerImpl implements BranchServer {


    @Autowired
    RepositoryServer repositoryServer;

    @Value("${repository.address}")
    private String repositoryMemoryAddress;

    @Override
    public Branch findBranch(String rpyId,String commitId) {
        List<Branch> allBranch = findAllBranch(rpyId);
        if (CollectionUtils.isNotEmpty(allBranch)){
            List<Branch> branches = allBranch.stream().filter(a -> commitId.equals(a.getBranchId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(branches)){
                return branches.get(0);
            }
        }
        return null;
    }

    /**
     * 查询所有分支
     * @param rpyId 仓库id
     * @return 分支信息列表
     */
    @Override
    public List<Branch> findAllBranch(String rpyId) {

        String repositoryAddress = RepositoryUtil.findRepositoryAddress(repositoryMemoryAddress,rpyId);
        List<Branch> branches;
        try {
            branches = GitBranchUntil.findAllBranch(repositoryAddress);
        } catch (IOException e) {
            throw new ApplicationException("分支信息获取失败："+e);
        }

        return branches;
    }

    /**
     * 创建分支
     * @param branchMessage 分支信息
     */
    @Override
    public void createBranch(BranchMessage branchMessage) {
        String rpyId = branchMessage.getRpyId();
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(repositoryMemoryAddress,rpyId);
        try {
            GitBranchUntil.createRepositoryBranch(repositoryAddress , branchMessage.getBranchName(), branchMessage.getPoint());
        } catch (IOException | GitAPIException e) {
            throw new ApplicationException("分支创建失败"+e);
        }
    }




    /**
     * 删除分支
     * @param branchMessage 分支信息
     */
    @Override
    public void deleteBranch(BranchMessage branchMessage){
        String rpyId = branchMessage.getRpyId();
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(repositoryMemoryAddress,rpyId);
        try {
            GitBranchUntil.deleteRepositoryBranch(repositoryAddress, branchMessage.getBranchName());
        } catch (IOException | GitAPIException e) {
            throw new ApplicationException("分支删除失败"+e);
        }

    }



}







































