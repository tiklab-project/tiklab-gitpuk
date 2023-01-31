package net.tiklab.xcode.git;

import net.tiklab.core.exception.ApplicationException;
import net.tiklab.xcode.branch.model.CodeBranch;
import net.tiklab.xcode.code.model.CodeMessage;
import net.tiklab.xcode.commit.model.CommitMessage;
import net.tiklab.xcode.until.CodeUntil;
import net.tiklab.xcode.until.FileTree;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.RefNotAdvertisedException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.TreeFilter;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

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
                    // .setInitialBranch("master")
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
        Git call = Git.cloneRepository()
                .setURI(repositoryAddress + ".git")
                .setDirectory(new File(repositoryAddress))
                .setBranch(branch)
                .call();
        call.fetch();
        call.close();
    }

    /**
     * 更新仓库
     * @param repositoryAddress 仓库地址
     */
    public static void pullRepository(String repositoryAddress) throws IOException, GitAPIException {
        Git git = Git.open(new File(repositoryAddress));
        git.pull().call();
        git.fetch();
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
    public static void repositoryCommit(String repositoryAddress,String commitMessage,String fileAddress) throws IOException, GitAPIException {
        Git git = Git.open(new File(repositoryAddress));
        git.add().addFilepattern(fileAddress).call(); //添加文件
        git.commit().setMessage(commitMessage).call(); // 提交信息
        git.push().setRemote(repositoryAddress+".git") //地址
                .setRefSpecs(new RefSpec("master")) //推送分支 没有则创建
                .call();
        git.close();
    }








}






























































