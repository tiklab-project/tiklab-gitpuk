package net.tiklab.xcode.git;

import net.tiklab.core.exception.ApplicationException;
import net.tiklab.xcode.commit.model.CommitMessage;
import net.tiklab.xcode.until.CodeUntil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;
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
    public static List<CommitMessage> findBranchCommit(String repositoryAddress, String branch, boolean isCommitId) throws IOException , ApplicationException {

        Git git = Git.open(new File(repositoryAddress));
        Repository repository = git.getRepository();

        ObjectId objectId;
        if (!isCommitId){
            //分支为空设置为默认
            if (branch == null) {
                branch = repository.getBranch();
            }

            //分支是否存在
            Ref head = repository.findRef(branch);
            if (head == null) {
                git.close();
                return Collections.emptyList();
            }
            objectId = head.getObjectId();
        }else {
            objectId = ObjectId.fromString(branch);
        }
        RevWalk revWalk = new RevWalk(repository);
        revWalk.markStart(revWalk.parseCommit(objectId));

        List<CommitMessage> list = new ArrayList<>();
        for (RevCommit revCommit : revWalk) {
            TreeWalk treeWalk = new TreeWalk(repository);
            treeWalk.reset(revCommit.getTree());

            Date date = revCommit.getAuthorIdent().getWhen();//时间
            String name = revCommit.getAuthorIdent().getName();//提交人

            CommitMessage commitMessage = new CommitMessage();
            commitMessage.setCommitId(revCommit.getId().getName());//commitId
            commitMessage.setCommitMessage(revCommit.getShortMessage());//提交信息
            commitMessage.setCommitUser(name);
            commitMessage.setDateTime(date);
            commitMessage.setCommitTime(CodeUntil.time(date)+"前");//转换时间
            list.add(commitMessage);
            treeWalk.close();
            revCommit.disposeBody();

        }
        revWalk.close();
        git.close();

        list.sort(Comparator.comparing(CommitMessage::getDateTime).reversed());
        return list;
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

    /**
     * 获取单个文件的提交历史
     * @param git git实例
     * @param file 文件名称
     * @return 提交历史
     * @throws GitAPIException 信息获取失败
     */
    public static List<Map<String,String>> gitFileCommitLog(Git git,String commitId,String file) throws GitAPIException, IOException {
        List<Map<String,String>> list = new ArrayList<>();

        ObjectId objectId = ObjectId.fromString(commitId);

        RevCommit revCommits = git.getRepository().parseCommit(objectId);

        Iterable<RevCommit> log = git.log()
                .add(revCommits)
                .addPath(file)
                .call();
        for (RevCommit revCommit : log) {
            Map<String,String> map = new HashMap<>();
            Date date = revCommit.getAuthorIdent().getWhen();
            String message = revCommit.getShortMessage();
            map.put("message",message);//转换时间
            map.put("time",CodeUntil.time(date)+"前");
            list.add(map);
        }
        return list;
    }


}










































