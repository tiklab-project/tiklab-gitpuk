package net.tiklab.xcode.git;

import net.tiklab.core.exception.ApplicationException;
import net.tiklab.xcode.branch.model.CodeBranch;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.URIish;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;


/**
 * git仓库操作
 */

public class GitUntil {


    /**
     * 创建仓库
     * @param repositoryName 仓库名称
     * @throws ApplicationException 仓库创建失败
     */
    public static void createRepository(String repositoryAddress,String repositoryName) throws ApplicationException {
        File file = new File(repositoryAddress, repositoryName + ".git");
        Git git;
        try {
            git = Git.init()
                    .setDirectory(file)
                    .setBare(true) //裸仓库
                    .call();
        } catch (GitAPIException e) {
            throw new ApplicationException("仓库创建失败。" + e);
        }
       git.close();
    }

    /**
     * 克隆仓库
     * @param repositoryAddress 仓库地址
     * @param branch 分支
     * @throws GitAPIException 克隆失败
     */
    public static void cloneRepository(String repositoryAddress,String branch) throws GitAPIException {
        Git git = Git.cloneRepository()
                .setURI(repositoryAddress + ".git")
                .setDirectory(new File(repositoryAddress+"_"+branch))
                .setBranch(branch)
                .call();
        git.close();
    }

    /**
     * 更新仓库
     * @param repositoryAddress 仓库地址
     */
    public static void pullRepository(String repositoryAddress,String branch) throws IOException, GitAPIException {
        Git git = Git.open(new File(repositoryAddress+"_"+branch));
        git.pull()
                .setRebase(false)
                .setRemoteBranchName(branch) //拉取分支
                .call();
        git.close();
    }

    /**
     * 仓库提交
     * @param repositoryAddress 仓库地址
     * @param commitMessage 提交信息
     * @param fileAddress 提交文件
     * @throws IOException 找不到仓库
     * @throws GitAPIException 提交失败
     */
    public static void repositoryCommit(String repositoryAddress,String branch,String commitMessage,String fileAddress) throws IOException, GitAPIException, URISyntaxException {

        //判断分支是否存在
        List<CodeBranch> branches = GitBranchUntil.findAllBranch(repositoryAddress);
        boolean isBranch = false;
        for (CodeBranch codeBranch : branches) {
            String branchName = codeBranch.getBranchName();
            if (branch.equals(branchName)) {
                isBranch = true;
                break;
            }
        }

        if (!isBranch){
            //默认分支
            String defaultBranch = GitBranchUntil.findDefaultBranch(repositoryAddress) ;
            GitBranchUntil.createRepositoryBranch(repositoryAddress+".git",branch,defaultBranch);
        }

        cloneRepository(repositoryAddress,branch);

        Git git = Git.open(new File(repositoryAddress+"_"+branch));

        git.add() //添加文件
                .setUpdate(true)
                .addFilepattern(fileAddress)
                .call();
        git.commit() // 提交信息
                .setMessage(commitMessage)
                .call();
        git.push()
                .setRemote(repositoryAddress)
                .setRefSpecs(new RefSpec(branch))
                .call();
        git.close();

        remoteRepositoryFile(repositoryAddress,branch);

    }

    /**
     * 推送到远程
     * @param repositoryAddress 仓库地址
     * @param branch 分支
     * @throws IOException 仓库不存在
     * @throws URISyntaxException 远程地址错误
     * @throws GitAPIException 推送失败
     */
    public static void remoteRepositoryFile(String repositoryAddress,String branch) throws IOException, URISyntaxException, GitAPIException {
        Git git = Git.open(new File(repositoryAddress));
        git.remoteAdd() //远程仓库
                .setName("origin")
                .setUri(new URIish(repositoryAddress+".git"))
                .call();
        git.push()
                // .setRefSpecs(new RefSpec(branch))
                .setPushAll()
                .call();
        git.close();
    }

    /**
     * 删除远程文件
     * @param repositoryAddress 仓库地址
     * @param fileAddress 文件地址
     * @throws IOException 仓库不存在
     * @throws URISyntaxException 远程地址错误
     * @throws GitAPIException 推送失败
     */
    public static void deleteRepositoryFile(String repositoryAddress,String fileAddress) throws IOException, GitAPIException, URISyntaxException {
        Git git = Git.open(new File(repositoryAddress));
        git.remoteAdd() //添加远程仓库
                .setName("origin")
                .setUri(new URIish(repositoryAddress+".git"))
                .call();
        git.rm()
                .addFilepattern(fileAddress)
                .setCached(false) //true 仅从索引中删除 false 索引文件都删除
                .call();
        git.close();
    }






}






























































