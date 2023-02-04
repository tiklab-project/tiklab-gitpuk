package net.tiklab.xcode.git;

import net.tiklab.xcode.branch.model.CodeBranch;
import net.tiklab.xcode.commit.model.CommitMessage;
import net.tiklab.xcode.until.CodeFinal;
import net.tiklab.xcode.until.CodeUntil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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
     * 获取仓库所有分支
     * @param repositoryAddress 仓库地址
     * @return 分支集合
     * @throws IOException 仓库不存在
     */
    public static List<CodeBranch> findAllBranch(String repositoryAddress) throws IOException {
        Git git = Git.open(new File(repositoryAddress));

        Repository repository = git.getRepository();
        List<CodeBranch> list = new ArrayList<>();
        List<Ref> refs1 = repository.getRefDatabase().getRefs();
        String defaultBranch = " ";
        for (Ref ref : refs1) {
            String name = ref.getName();
            if (name.equals("HEAD")){
                Ref target = ref.getTarget();
                defaultBranch = target.getName();
                continue;
            }
            CodeBranch codeBranch = new CodeBranch();
            String Id = ref.getObjectId().getName();
            codeBranch.setBranchId(Id);
            if (defaultBranch.equals(name)){
                codeBranch.setDefaultBranch(true);
            }
            String s = name.replace("refs/heads/", "");
            codeBranch.setBranchName(s);
            list.add(codeBranch);
        }
        git.close();

        if (list.isEmpty()){
            return Collections.emptyList();
        }

        return list;
    }

    /**
     * 返回项目默认分支
     * @param repositoryAddress 仓库地址
     * @return 默认分支 无返回 DEFAULT_MASTER
     * @throws IOException 仓库不存在
     */
    public   static String findDefaultBranch(String repositoryAddress) throws IOException {
        List<CodeBranch> codeBranches = findAllBranch(repositoryAddress);

        for (CodeBranch codeBranch : codeBranches) {
            if (codeBranch.isDefaultBranch()){
                return codeBranch.getBranchName();
            }
        }
        return CodeFinal.DEFAULT_MASTER;
    }






}















































