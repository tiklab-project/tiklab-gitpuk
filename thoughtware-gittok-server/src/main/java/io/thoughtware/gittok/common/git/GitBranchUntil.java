package io.thoughtware.gittok.common.git;

import io.thoughtware.gittok.branch.model.Branch;
import io.thoughtware.gittok.branch.model.BranchQuery;
import io.thoughtware.gittok.commit.model.CommitMessage;
import io.thoughtware.core.exception.ApplicationException;
import io.thoughtware.gittok.commit.model.MergeData;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.RefSpec;

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
        Repository repository = Git.open(new File(repositoryAddress)).getRepository();
        Git git = new Git(repository);

        // 删除分支 (分支名字和标签名字相同时)
        git.push()
                .setRemote(repository.getDirectory().getAbsolutePath())
                .setRefSpecs(new RefSpec(":" + "refs/heads/" + branchName))
                .call();
        git.close();

      /*  git.branchDelete()
                .setBranchNames(branchName)
                .setForce(true) //是否强制删除分支
                .call();
        git.close();*/
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

            //排除远程分支
            if (name.contains(Constants.R_REMOTES)){
                continue;
            }

            //默认分支
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
      /*      updateRef.update();*/
        } catch (IOException e) {
            throw new ApplicationException("切换默认分支失败:"+fullBranch);
        }
    }

    /**
     * 合并分支
     * @param mergeData mergeData
     * @param repositoryPath 裸仓库地址
     * @throws ApplicationException 切换失败
     */
    public static String mergeBranch(MergeData mergeData, String repositoryPath) {
        try {
            String beforeLast = StringUtils.substringBeforeLast(repositoryPath, "/");
            String afterLast = StringUtils.substringAfterLast(repositoryPath, "/");
            //仓库存储的id
            String rpyId = afterLast.substring(0, afterLast.indexOf(".git"));

            //clone 裸仓库为普通仓库
            String clonePath = beforeLast + "/merge/"+rpyId;
            File file = new File(clonePath);
            //合并分支时候裸仓库存在对应的普通仓库 先删除
            if (file.exists()){
                FileUtils.deleteDirectory(new File(clonePath));
            }

            if (!file.exists()){
                file.mkdirs();
            }

            //克隆裸仓库的所有分子为普通仓库
            GitUntil.cloneRepositoryAllBranch(repositoryPath, clonePath);

            //普通仓库git
            Git git = Git.open(new File(clonePath));

            //切换到目标源
            git.checkout().setName(mergeData.getMergeTarget()).call();

            //获取源目标对象的id
            ObjectId mergeBase = git.getRepository().resolve(mergeData.getMergeOrigin());

            //创建和并节点的基本合并方式
            if (("createNode").equals(mergeData.getMergeWay())){
                git.merge()
                        .include(mergeBase)
                        .setFastForward(MergeCommand.FastForwardMode.NO_FF)
                        .setMessage(mergeData.getMergeMessage())
                        .setCommit(true)
                        .call();
            }


            //将合并请求中的提交记录压缩成一条， 然后添加到目标分支。
            if (("squash").equals(mergeData.getMergeWay())){
                git.merge()
                        .include(mergeBase)
                        .setCommit(false)
                        .setSquash(true)

                        .call();
                //创建提交信息
                git.commit()
                        .setMessage(mergeData.getMergeMessage())
                        //.setCommitter()
                        .call();
            }


            //变基合并  将评审分支提交逐一编辑到目标分支
            if (("rebase").equals(mergeData.getMergeWay())){
                 git.rebase()
                       .setUpstream(mergeData.getMergeOrigin())
                       .setPreserveMerges(true)
                       .call();
            }

            //推送到裸仓库
            GitUntil. pushAllBranchRepository(clonePath,repositoryPath);

            //执行完成后删除clone 的文件
            FileUtils.deleteDirectory(new File(clonePath));

        } catch (Exception e) {
            throw new ApplicationException(5000,"合并分支失败："+e.getMessage());
        }
        return "ok";
    }

    public static String mergeBranchByFast(MergeData mergeData, String repositoryPath) {
        try {
            Repository repository = Git.open(new File(repositoryPath)).getRepository();

            Git git = new Git(repository);
            //源分支
            String mergeOrigin = mergeData.getMergeOrigin();
            Ref sourceRef = repository.exactRef("refs/heads/" + mergeOrigin);

            //目标分支
            String mergeTarget = mergeData.getMergeTarget();
            Ref targetRef = repository.exactRef("refs/heads/" + mergeTarget);


            // 执行分支合并操作
            MergeCommand mergeCommand = git.merge();
            mergeCommand.include(sourceRef);
            mergeCommand.setFastForward(MergeCommand.FastForwardMode.FF_ONLY);
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
            /*if (("NEW").equals(name)||("FORCED").equals(name)||("FAST_FORWARD").equals(name)){

            }*/
         /*   if (("NO_CHANGE").equals(name)){
                throw new ApplicationException(5000,"两个分子没有不同的不需要合并");
            }*/
            return  "ok";
            } else {
                throw new ApplicationException(5000, "合并分支失败");
            }
        } catch (Exception e) {
            throw new ApplicationException(5000, "合并分支失败：" + e.getMessage());
        }

    }


    public static void mergeBranch1(MergeData mergeData, String repositoryPath) {
        String beforeLast = StringUtils.substringBeforeLast(repositoryPath, "/");
        String afterLast = StringUtils.substringAfterLast(repositoryPath, "/");
        String rpyId = afterLast.substring(0, afterLast.indexOf(".git"));

        try {
            //clone 裸仓库为普通仓库
            String clonePath = beforeLast + "/clone/"+rpyId;
            File file = new File(clonePath);
            if (file.exists()){
                FileUtils.deleteDirectory(file);
            }

            if (!file.exists()){
                file.mkdirs();
            }
            GitUntil.cloneRepositoryAllBranch(repositoryPath, clonePath);

            //普通仓库git
            Git git = Git.open(new File(clonePath));



          //  RepositoryCache.close(git.getRepository());
        } catch (Exception e) {


        }
    }

}















































