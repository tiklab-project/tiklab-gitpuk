package io.thoughtware.gittork.common.git;

import io.thoughtware.gittork.branch.model.Branch;
import io.thoughtware.gittork.branch.model.BranchQuery;
import io.thoughtware.gittork.commit.model.CommitMessage;
import io.thoughtware.core.exception.ApplicationException;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * jgit操作仓库分支
 */
public class GitBranchUntil {

    /**
     * 创建仓库分支
     * @param repositoryAddress 仓库地址
     * @param branchName 分支名称
     * @param point 分支起点
     * @throws IOException 仓库不存在
     * @throws GitAPIException 创建失败
     */
    public static void createRepositoryBranch(String repositoryAddress,String branchName, String point) throws IOException, GitAPIException {
        Git git = Git.open(new File(repositoryAddress));
        git.branchCreate()
                .setName(branchName)
                .setStartPoint(point) //起点
                .call();
        git.close();
    }


    /**
     * 删除分支
     * @param repositoryAddress 仓库地址
     * @param branchName 分支名称
     * @throws IOException 仓库不存在
     * @throws GitAPIException 仓库删除失败
     */
    public static void deleteRepositoryBranch(String repositoryAddress,String branchName) throws IOException, GitAPIException {
        Git git = Git.open(new File(repositoryAddress));
        git.branchDelete()
                .setBranchNames(branchName)
                .setForce(true) //是否强制删除分支
                .call();
        git.close();
    }

    /**
     * 更新分支名称
     * @param repositoryAddress 仓库地址
     * @param newName 分支新名称
     * @param oldName 分支旧名称
     * @throws IOException 仓库不存在
     * @throws GitAPIException 名称更新失败
     */
    public static void updateRepositoryBranch(String repositoryAddress,String newName,String oldName) throws IOException, GitAPIException {
        Git git = Git.open(new File(repositoryAddress));
        git.branchRename()
                .setNewName(newName)
                .setOldName(oldName)
                .call();
        git.close();
    }

    /**
     * 获取仓库所有分支以及标签
     * @param repositoryAddress 仓库地址
     * @return 分支集合
     * @throws IOException 仓库不存在
     */
    public static List<Branch> findAllBranch(String repositoryAddress) throws IOException, GitAPIException {

        Git git = Git.open(new File(repositoryAddress));
        Repository repository = git.getRepository();

        List<Branch> list = new ArrayList<>();
        //分支和标签
        List<Ref> refs = repository.getRefDatabase().getRefs();
        String defaultBranch = " ";
        for (Ref ref : refs) {
            Branch branch = new Branch();
            String name = ref.getName();
            //排除标签
            if (name.contains(Constants.R_TAGS)){
                continue;
            }

            if (name.equals("HEAD")){
                Ref target = ref.getTarget();
                defaultBranch = target.getName();
                continue;
            }

            String Id = ref.getObjectId().getName();
            //分支名字
            String branchName = name.replace(Constants.R_HEADS, "");
            branch.setBranchId(Id);
            //判断是否为默认分支
            if (defaultBranch.equals(name)){
                branch.setDefaultBranch(true);
            }
            branch.setBranchName(branchName);
            list.add(branch);
        }

        // repository.close();
        git.close();

        if (list.isEmpty()){
            return Collections.emptyList();
        }

        return list;
    }


    /**
     * 条件获取获取仓库分支
     * @param branchQuery branchQuery
     * @return 分支集合
     * @throws IOException 仓库不存在
     */
    public static List<Branch> findBranchList(BranchQuery branchQuery) throws IOException {

        Git git = Git.open(new File(branchQuery.getRepositoryAddress()));
        Repository repository = git.getRepository();

        List<Branch> list = new ArrayList<>();
        List<Ref> refs = repository.getRefDatabase().getRefs();
        if (StringUtils.isNotEmpty(branchQuery.getName())){
            refs=refs.stream().filter(a->a.getName().contains(branchQuery.getName())||a.getName().equals("HEAD")).collect(Collectors.toList());
        }

        String defaultBranch = " ";
        for (Ref ref : refs) {
            Branch branch = new Branch();
            String name = ref.getName();
            //排除标签
            if (name.contains(Constants.R_TAGS)){
                continue;
            }
            if (name.equals("HEAD")){
                Ref target = ref.getTarget();
                defaultBranch = target.getName();
                continue;
            }
            //判断是否为默认分支
            if (defaultBranch.equals(name)){
                branch.setDefaultBranch(true);
            }
            String Id = ref.getObjectId().getName();
            String s = name.replace(Constants.R_HEADS, "");

            CommitMessage oneBranchCommit = GitCommitUntil.findOneBranchCommit(repository, s,false);

            branch.setUpdateUser(oneBranchCommit.getCommitUser());
            branch.setUpdateTime(oneBranchCommit.getCommitTime());
            //根据活跃状态查询
            if (StringUtils.isNotEmpty(branchQuery.getState())){
                if (("active").equals(branchQuery.getState())){
                    branch.setBranchId(Id);
                    branch.setBranchName(s);
                    list.add(branch);
                    continue;
                }
                if (("noActive").equals(branchQuery.getState())){
                    continue;
                }
            }

            branch.setBranchId(Id);

            branch.setBranchName(s);
            list.add(branch);
        }

        // repository.close();
        git.close();

        if (list.isEmpty()){
            return Collections.emptyList();
        }

        return list;
    }




    /**
     * 返回仓库默认分支
     * @param repositoryAddress 仓库地址
     * @return 默认分支 无返回 Constants.MASTER
     * @throws IOException 仓库不存在
     */
    public static String findDefaultBranch(String repositoryAddress) throws Exception {
        List<Branch> branches = findAllBranch(repositoryAddress);
        for (Branch branch : branches) {
            if (branch.isDefaultBranch()){
                return branch.getBranchName();
            }
        }
        return Constants.MASTER;
    }

    /**
     * 获取指定commitId的提交树
     * @param repository 仓库
     * @param branch 分支
     * @return commitId
     * @throws IOException 仓库不存在
     */
    public static RevTree findBarthCommitRevTree(Repository repository,String branch,boolean b) throws IOException {
        ObjectId objectId;
        if (!b){
            objectId = repository.resolve(Constants.R_HEADS + branch);
//            objectId = repository.resolve( branch);
        }else {
            objectId = ObjectId.fromString(branch);
        }
        RevWalk walk = new RevWalk(repository);
        RevTree tree = walk.parseCommit(objectId).getTree();
        walk.close();
        return tree;
    }

    /**
     * 获取指定commitId的提交树
     * @param repository 仓库
     * @param branch 分支
     * @return findType 查询类型
     * @throws IOException 仓库不存在
     */
    public static RevTree findBarthCommitRevTree(Repository repository,String branch,String findType) throws IOException {
        ObjectId objectId = findObjectId(repository, branch, findType);
        RevWalk walk = new RevWalk(repository);
        RevTree tree = walk.parseCommit(objectId).getTree();
        walk.close();
        return tree;
    }

    /**
     * 获取objectId
     * @param repository 仓库
     * @param branch 分支
     * @return findType 查询类型
     * @throws IOException 仓库不存在
     */
    public static ObjectId findObjectId(Repository repository,String branch,String findType) throws IOException {
        ObjectId objectId =null;
        if (("tag").equals(findType)){
            objectId = repository.resolve(Constants.R_TAGS + branch);
        }
        if (("branch").equals(findType)){
            objectId = repository.resolve(Constants.R_HEADS + branch);
        }
        if (("commit").equals(findType)){
            objectId = ObjectId.fromString(branch);
        }
        return objectId;
    }

    /**
     * 切换默认分支
     * @param repository 仓库
     * @param branch 分支
     * @throws ApplicationException 切换失败
     */
    public static void updateFullBranch(Repository repository,String branch) throws ApplicationException {
        String fullBranch = Constants.R_HEADS + branch;
        try {
            RefUpdate  updateRef = repository.updateRef("HEAD");
            updateRef.link(fullBranch);
            updateRef.update();
        } catch (IOException e) {
            throw new ApplicationException("切换默认分支失败:"+fullBranch);
        }
    }

    /**
     * 合并分支
     * @param repository repository
     * @param branch 源分支
     * @throws ApplicationException 切换失败
     */
    public static void mergeBranch( Repository repository,String branch) {

        try {
            Git git = new Git(repository);

            //默认分支
            String deBranch = git.getRepository().getBranch();

            Ref sourceRef = repository.exactRef("refs/heads/" + deBranch);
            Ref targetRef = repository.exactRef("refs/heads/" + branch);

            // 执行分支合并操作
            MergeCommand mergeCommand = git.merge();
            mergeCommand.include(sourceRef.getObjectId());
            mergeCommand.setFastForward(MergeCommand.FastForwardMode.FF);
            MergeResult called = mergeCommand.call();

            if (called.getMergeStatus().isSuccessful()) {

                // 更新目标分支引用
                RefUpdate refUpdate = repository.updateRef(targetRef.getName());
                refUpdate.setNewObjectId(called.getNewHead());
                refUpdate.setForceUpdate(true);
                RefUpdate.Result updateResult = refUpdate.update();
                String name = updateResult.name();

                /*
                *   NEW：表示引用是新创建的。
                *   FORCED：表示引用已被强制更新。
                *   FAST_FORWARD：表示引用是通过快进方式更新的。
                *   NO_CHANGE：表示引用没有发生变化，无需更新。
                *   LOCK_FAILURE：表示在更新引用时遇到了锁定失败。
                *   REJECTED：表示更新引用被拒绝。
                *   IO_FAILURE：表示在更新引用时遇到了I/O错误。
                * */
                if (("NEW").equals(name)||("FORCED").equals(name)||("FAST_FORWARD").equals(name)){

                }
                if (("NO_CHANGE").equals(name)){

                }

            } else {
                System.out.println("分支合并失败！");
            }

        } catch (Exception e) {
            throw new ApplicationException("切换默认分支失败:"+e.getMessage());
        }
    }


}















































