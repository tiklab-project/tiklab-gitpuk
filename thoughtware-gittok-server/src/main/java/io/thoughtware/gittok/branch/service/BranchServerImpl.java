package io.thoughtware.gittok.branch.service;

import io.thoughtware.core.page.Pagination;
import io.thoughtware.eam.common.context.LoginContext;
import io.thoughtware.gittok.branch.model.*;
import io.thoughtware.gittok.commit.model.MergeRequest;
import io.thoughtware.gittok.commit.model.MergeRequestQuery;
import io.thoughtware.gittok.commit.service.MergeRequestService;
import io.thoughtware.gittok.common.GitTokYamlDataMaService;
import io.thoughtware.gittok.common.git.GitCommitUntil;
import io.thoughtware.gittok.repository.service.RepositoryService;
import io.thoughtware.core.exception.ApplicationException;
import io.thoughtware.core.exception.SystemException;
import io.thoughtware.rpc.annotation.Exporter;
import io.thoughtware.gittok.common.git.GitBranchUntil;
import io.thoughtware.gittok.common.RepositoryUtil;
import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Exporter
public class BranchServerImpl implements BranchServer {
    private static Logger logger = LoggerFactory.getLogger(BranchServerImpl.class);

    @Autowired
    RepositoryService repositoryServer;

    @Autowired
    GitTokYamlDataMaService yamlDataMaService;

    @Autowired
    RepositoryBranchService repositoryBranchService;

    @Autowired
    MergeRequestService mergerRequestService;


    @Override
    public Branch findBranch(String rpyId, String commitId) {
        List<Branch> allBranch = findAllBranchByRpyId(rpyId);
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
    public List<Branch> findAllBranchByRpyId(String rpyId) {

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
            String branchId = GitBranchUntil.createRepositoryBranch(repositoryAddress, branchMessage.getBranchName(), branchMessage.getPoint());

            //创建分支记录
            RepositoryBranch branch =  new RepositoryBranch();
            branch.setBranchId(branchId);
            branch.setRepositoryId(rpyId);
            branch.setBranchName(branchMessage.getBranchName());
            branch.setCreateUser(LoginContext.getLoginId());
            repositoryBranchService.createRepositoryBranch(branch);

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

            //删除分支 移除分支记录
            repositoryBranchService.deleteRepositoryBranch(rpyId, branchMessage.getBranchName());

        } catch (IOException | GitAPIException e) {
            throw new ApplicationException("分支删除失败"+e);
        }

    }

    @Override
    public List<Branch> findBranchList(BranchQuery branchQuery) {
        //查询仓库的合并请求
        List<MergeRequest> mergeRequestList = mergerRequestService.findMergeRequestList(new MergeRequestQuery().setRpyId(branchQuery.getRpyId()));

        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),branchQuery.getRpyId());
        try {
            branchQuery.setRepositoryAddress(repositoryAddress);

            //查询仓库分支
            List<Branch> branchList = GitBranchUntil.findBranchList(branchQuery);
            //将默认分支放在首位
            branchList.sort(Comparator.comparing(Branch::isDefaultBranch).reversed());

            //查询我创建的 ，需要根据分支记录查询
            if (("oneself").equals(branchQuery.getState())) {
                List<RepositoryBranch> repositoryBranchList = repositoryBranchService.findRepositoryBranchList(new RepositoryBranchQuery()
                        .setRepositoryId(branchQuery.getRpyId())
                        .setCreateUser(branchQuery.getUserId()));

                if (CollectionUtils.isNotEmpty(repositoryBranchList)){
                    //获取裸仓库分支和数据库分支记录 相同的
                   branchList = branchList.stream()
                            .filter(p1 -> repositoryBranchList.stream().anyMatch(p2 -> p2.getBranchName().equals(p1.getBranchName())))
                            .collect(Collectors.toList());
                }
            }

            //查询该分支时候有打开的合并请求
            if (CollectionUtils.isNotEmpty(branchList)){
                for (Branch branch :branchList){
                    List<MergeRequest> mergeRequests =  mergeRequestList.stream().filter(a->a.getMergeState()!=2)
                            .filter(a -> branch.getBranchName().equals(a.getMergeOrigin()) || branch.getBranchName().equals(a.getMergeTarget()))
                            .sorted(Comparator.comparing(MergeRequest::getCreateTime).reversed())
                            .collect(Collectors.toList());

                    //获取该分支最新创建的合并请求
                    if (CollectionUtils.isNotEmpty(mergeRequests)){
                        branch.setMergeRequest(mergeRequests.get(0));
                    }
                }
            }

            return branchList;
        } catch (IOException e) {
            logger.error("获取仓库"+branchQuery.getRpyId()+"分支失败:"+e.getMessage());
            throw new ApplicationException("获取分支失败："+e);
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
           // GitBranchUntil.mergeBranch(repository, branchQuery.getName());
        } catch (IOException e) {
            throw new SystemException(9000,"获取仓库失败:"+branchQuery.getName());
        }
    }
}







































