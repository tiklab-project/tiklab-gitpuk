package io.tiklab.xcode.branch.service;

import io.tiklab.core.exception.ApplicationException;
import io.tiklab.core.exception.SystemException;
import io.tiklab.rpc.annotation.Exporter;
import io.tiklab.xcode.branch.model.BranchMessage;
import io.tiklab.xcode.branch.model.Branch;
import io.tiklab.xcode.branch.model.BranchQuery;
import io.tiklab.xcode.repository.service.RepositoryServer;
import io.tiklab.xcode.common.git.GitBranchUntil;
import io.tiklab.xcode.common.RepositoryUtil;
import io.tiklab.xcode.common.XcodeYamlDataMaService;
import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    XcodeYamlDataMaService yamlDataMaService;


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

        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),rpyId);
        List<Branch> branches;
        try {
            branches = GitBranchUntil.findAllBranch(repositoryAddress);
        } catch (Exception e) {
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
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),rpyId);
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
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),rpyId);
        try {
            GitBranchUntil.deleteRepositoryBranch(repositoryAddress, branchMessage.getBranchName());
        } catch (IOException | GitAPIException e) {
            throw new ApplicationException("分支删除失败"+e);
        }

    }

    @Override
    public List<Branch> findBranchList(BranchQuery branchQuery) {
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),branchQuery.getRpyId());
        try {
            branchQuery.setRepositoryAddress(repositoryAddress);
            List<Branch> branchList = GitBranchUntil.findBranchList(branchQuery);

            return branchList;
        } catch (IOException e) {
            throw new ApplicationException("分支信息获取失败："+e);
        }
    }

    @Override
    public void updateDefaultBranch(BranchQuery branchQuery) {
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),branchQuery.getRpyId()) ;

        File file = new File(repositoryAddress);
        try {
            Git git = Git.open(file);
            Repository repository = git.getRepository();
            GitBranchUntil.updateFullBranch(repository, branchQuery.getName());
        } catch (IOException e) {
            throw new SystemException(9000,"切换默认分支失败:"+branchQuery.getName());
        }
    }

    @Override
    public void mergeBranch(BranchQuery branchQuery) {
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),branchQuery.getRpyId()) ;

        File file = new File(repositoryAddress);
        try {
            Git git = Git.open(file);
            Repository repository = git.getRepository();
            GitBranchUntil.mergeBranch(repository, branchQuery.getName());
        } catch (IOException e) {
            throw new SystemException(9000,"获取仓库失败:"+branchQuery.getName());
        }
    }
}







































