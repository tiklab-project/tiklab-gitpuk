package net.tiklab.xcode.branch.service;

import net.tiklab.core.exception.ApplicationException;
import net.tiklab.xcode.branch.model.BranchMessage;
import net.tiklab.xcode.branch.model.Branch;
import net.tiklab.xcode.repository.model.Repository;
import net.tiklab.xcode.repository.service.RepositoryServer;
import net.tiklab.xcode.git.GitBranchUntil;
import net.tiklab.xcode.until.RepositoryUntil;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class BranchServerImpl implements BranchServer {


    @Autowired
    RepositoryServer repositoryServer;


    /**
     * 查询所有分支
     * @param codeId 仓库id
     * @return 分支信息列表
     */
    @Override
    public List<Branch> findAllBranch(String codeId) {
        Repository repository = repositoryServer.findOneCode(codeId);

        String repositoryAddress = RepositoryUntil.findRepositoryAddress(repository);
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
        String codeId = branchMessage.getCodeId();
        Repository repository = repositoryServer.findOneCode(codeId);
        String repositoryAddress = RepositoryUntil.findRepositoryAddress(repository);
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
        String codeId = branchMessage.getCodeId();
        Repository repository = repositoryServer.findOneCode(codeId);
        String repositoryAddress = RepositoryUntil.findRepositoryAddress(repository);
        try {
            GitBranchUntil.deleteRepositoryBranch(repositoryAddress, branchMessage.getBranchName());
        } catch (IOException | GitAPIException e) {
            throw new ApplicationException("分支删除失败"+e);
        }

    }



}







































