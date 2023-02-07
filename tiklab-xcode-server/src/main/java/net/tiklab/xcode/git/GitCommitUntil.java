package net.tiklab.xcode.git;

import net.tiklab.core.exception.ApplicationException;
import net.tiklab.xcode.code.model.CodeMessage;
import net.tiklab.xcode.commit.model.CommitMessage;
import net.tiklab.xcode.until.CodeUntil;
import net.tiklab.xcode.until.FileTree;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.attributes.Attributes;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * jgit获取仓库提交信息
 */
public class GitCommitUntil {

    /**
     * 判断仓库是否为空
     * @param repositoryAddress 仓库地址
     * @param branch 分支
     * @return false 为空 true 不为空
     * @throws IOException 仓库不存在
     */
    public static boolean findRepositoryIsNotNull(String repositoryAddress, String branch) throws IOException {

        Git git = Git.open(new File(repositoryAddress));
        Repository repository = git.getRepository();

        if (branch == null) {
            branch = repository.getBranch();
        }
        Ref head = repository.findRef("refs/heads/" + branch);
        if (head == null) {
            return false;
        }
        git.close();
        return true;
    }

    /**
     * 获取分支的提交记录
     * @param repositoryAddress 仓库地址
     * @param branch 分支
     * @return 提交记录
     * @throws IOException 仓库不存在
     * @throws ApplicationException 分支不存在
     */
    public static List<CommitMessage> findBranchCommit(String repositoryAddress, String branch) throws IOException , ApplicationException {
        Git git = Git.open(new File(repositoryAddress));
        Repository repository = git.getRepository();
        //分支为空设置为默认
        if (branch == null) {
            branch = repository.getBranch();
        }
        //分支是否存在
        Ref head = repository.findRef(branch);
        if (head == null) {
            throw new ApplicationException("分支"+branch+"不存在。");
        }
        ObjectId objectId = head.getObjectId();
        RevWalk revWalk = new RevWalk(repository);
        revWalk.markStart(revWalk.parseCommit(objectId));

        List<CommitMessage> list = new ArrayList<>();
        for (RevCommit revCommit : revWalk) {
            CommitMessage commitMessage = new CommitMessage();
            TreeWalk treeWalk = new TreeWalk(repository);
            treeWalk.reset(revCommit.getTree());
            commitMessage.setCommitId(revCommit.getId().getName());//commitId
            commitMessage.setCommitMessage(revCommit.getShortMessage());//提交信息
            Date date = revCommit.getAuthorIdent().getWhen();//时间
            String name = revCommit.getAuthorIdent().getName();//提交人
            commitMessage.setCommitUser(name);
            commitMessage.setDateTime(date);
            String time = CodeUntil.time(date);//转换时间
            commitMessage.setCommitTime(time+"前");
            treeWalk.close();
            revCommit.disposeBody();
            list.add(commitMessage);
        }
        revWalk.dispose();
        revWalk.close();
        git.close();
        list.sort(Comparator.comparing(CommitMessage::getDateTime).reversed());
        return list;
    }

    /**
     * 获取指定文件的提交信息
     * @param repo 仓库
     * @param branch 分支
     * @param file 指定文件
     * @return 指定文件的提交信息
     * @throws IOException 仓库不存在
     * @throws GitAPIException 提交信息获取失败
     */
    public static Map<String, String> findFileCommit(Repository repo, String branch, File file) throws IOException, GitAPIException {

        Map<String, String> map = new HashMap<>();

        File directory = repo.getDirectory();

        List<CommitMessage> branchCommit = findBranchCommit(directory.getAbsolutePath(), branch);

        map.put("message",branchCommit.get(0).getCommitMessage());
        map.put("time",branchCommit.get(0).getCommitTime());

        if (branchCommit.size() == 1){
            return map;
        }

        //获取仓库信息
        RevWalk walk = new RevWalk(repo);
        for (int i = 0; i < branchCommit.size()-1; i++) {

            //最近一次的提交记录
            String newCommit = branchCommit.get(i).getCommitId();
            ObjectId newObjectId=repo.resolve(newCommit);
            RevCommit newRevCommit=walk.parseCommit(newObjectId);

            //上一次提交记录
            String oldCommit = branchCommit.get(i+1).getCommitId();
            ObjectId oldObjectId=repo.resolve(oldCommit);
            RevCommit oldRevCommit=walk.parseCommit(oldObjectId);

            List<DiffEntry> diffFix = findChangedFileList(newRevCommit,oldRevCommit,repo);

            for (DiffEntry entry : diffFix) {
                //更改的文件名称
                String newPath = entry.getNewPath();

                String fileName = file.getName();

                String message = branchCommit.get(i).getCommitMessage();
                String time = branchCommit.get(i).getCommitTime();

                if (!file.isDirectory() && newPath.equals(fileName)){
                    map.put("message",message);
                    map.put("time",time);
                    walk.dispose();
                    walk.close();
                    return map;
                }
                if (file.isDirectory() && newPath.contains(fileName)){
                    map.put("message",message);
                    map.put("time",time);
                    walk.dispose();
                    walk.close();
                    return map;
                }
            }
        }
        walk.dispose();
        walk.close();
        return map;
    }

    /**
     * 获取上一次提交信息
     * @param commit 提交信息
     * @param repo 仓库
     * @return 上一次提交
     * @throws IOException 仓库不存在
     */
    public static RevCommit findPrevHash(RevCommit commit, Repository repo)  throws  IOException {
        RevWalk walk = new RevWalk(repo);
        walk.markStart(commit);
        int count = 0;
        for (RevCommit rev : walk) {
            if (count == 1) {
                return rev;
            }
            count++;
        }
        walk.dispose();
        return null;
    }

    /**
     * 对比两次的提交
     * @param newCommit 新的提交
     * @param oldCommit 旧的提交
     * @param repo 仓库
     * @return 新的提交的文件
     * @throws IOException 仓库不存在
     * @throws GitAPIException 对比失败
     */
    public static List<DiffEntry> findChangedFileList(RevCommit newCommit, RevCommit oldCommit, Repository repo) throws IOException, GitAPIException {

        List<DiffEntry> returnDiffs ;

        ObjectReader reader = repo.newObjectReader();
        // 上次提交
        CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
        ObjectId oldHead=oldCommit.getTree().getId();
        oldTreeIter.reset(reader, oldHead);
        // 这次提交
        CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
        ObjectId head=newCommit.getTree().getId();
        newTreeIter.reset(reader, head);

        // 获得已更改文件的列表
        Git git = new Git(repo);
        returnDiffs = git.diff()
                .setNewTree(newTreeIter)
                .setOldTree(oldTreeIter)
                .call();

        git.close();

        return returnDiffs;
    }

    private static void gitLog(Git git) throws IOException, GitAPIException {
        List<Map<String,String>> list = new ArrayList<>();

        Iterable<RevCommit> call = git.log()
                .addPath("src")
                .call();
        for (RevCommit revCommit : call) {
            int commitTime = revCommit.getCommitTime();
            String shortMessage = revCommit.getShortMessage();


            System.out.println("时间："+commitTime);
            System.out.println("消息："+shortMessage);
        }


    }


}










































