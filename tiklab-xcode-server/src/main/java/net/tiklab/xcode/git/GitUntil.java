package net.tiklab.xcode.git;

import net.tiklab.core.exception.ApplicationException;
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

    //默认路径
    private static final String defaultPath = CodeUntil.defaultPath();

    /**
     * 创建仓库
     * @param repositoryName 仓库名称
     * @throws ApplicationException 仓库创建失败
     */
    public static void createRepository(String repositoryAddress,String repositoryName) throws ApplicationException {
        File file = new File(repositoryAddress, repositoryName + ".git");
        try (Git ignored = Git.init().setDirectory(file).setBare(true).setInitialBranch("master").call()) {
        } catch (GitAPIException e) {
            throw new ApplicationException("仓库创建失败。" + e);
        }
    }

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
        CreateBranchCommand branch = git.branchCreate();
        branch.setName(branchName);
        //起点
        branch.setStartPoint(point);
        branch.call();
    }

    /**
     * 提交文件
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

    }

    /**
     * 获取仓库所有分支
     * @param repositoryAddress 仓库地址
     * @return 分支集合
     * @throws IOException 仓库不存在
     * @throws GitAPIException 读取失败
     */
    public static List<String> repositoryBranch(String repositoryAddress) throws IOException, GitAPIException {
        Git git = Git.open(new File(repositoryAddress));
        ListBranchCommand listBranchCommand = git.branchList();
        List<Ref> call = listBranchCommand.call();
        List<String> list = new ArrayList<>();
        for (Ref ref : call) {
            String name = ref.getName();
            list.add(name);
        }
        return list;
    }

    /**
     * 克隆仓库
     * @param repositoryAddress 仓库地址
     * @param branch 分支
     * @throws GitAPIException 克隆失败
     */
    public static void cloneRepository(String repositoryAddress,String branch) throws GitAPIException {
        if (branch == null){
            branch = "master";
        }
        Git call = Git.cloneRepository()
                .setURI(repositoryAddress + ".git")
                .setDirectory(new File(repositoryAddress))
                .setBranch(branch)
                .call();
        call.fetch();
        call.close();
    }

    /**
     * 拉取最新代码
     * @param repositoryAddress 仓库地址
     */
    public static void pullRepository(String repositoryAddress) throws IOException, GitAPIException {
        Git git = Git.open(new File(repositoryAddress));
        git.pull().call();
        git.fetch();
        git.close();
    }

    /**
     * 获取仓库实例
     * @param repositoryAddress 仓库地址
     * @param branch 分支
     * @return 提交实例
     * @throws IOException 仓库不存在
     */
    public static RevWalk findRepositoryCommit(String repositoryAddress, String branch) throws IOException {
        Git git = Git.open(new File(repositoryAddress));
        Repository repository = git.getRepository();

        if (branch == null) {
            branch = repository.getBranch();
        }
        Ref head = repository.findRef("refs/heads/" + branch);
        if (head == null) {
           return null;
        }

        RevWalk revWalk = new RevWalk(repository);
        revWalk.markStart(revWalk.parseCommit(head.getObjectId()));
        return revWalk;
    }

    /**
     * 获取仓库分支所有提交信息
     * @param repositoryAddress 仓库地址
     * @param branch 分支
     * @return 提交实例
     * @throws IOException 仓库不存在
     */
    public static  List<CommitMessage> findBranchCommit(String repositoryAddress, String branch) throws IOException {
        RevWalk revWalk = findRepositoryCommit(repositoryAddress, branch);
        if (revWalk == null){
            return null;
        }
        List<CommitMessage> list = new ArrayList<>();
        for (RevCommit revCommit : revWalk) {
            CommitMessage commitMessages = new CommitMessage();
            String id = revCommit.getId().getName();
            commitMessages.setCommitId(id);
            PersonIdent message = revCommit.getAuthorIdent();
            String name = message.getName();
            commitMessages.setCommitUser(name);
            String time = CodeUntil.date(1,message.getWhen());
            String times = CodeUntil.date(2,message.getWhen());
            commitMessages.setDateTime(times);
            commitMessages.setCommitTime(time);
            String commitMessage = revCommit.getShortMessage();
            commitMessages.setCommitMessage(commitMessage);
            list.add(commitMessages);
            revWalk.dispose();
        }
        return list;
    }

    /**
     * 获取文件的提交信息
     * @param repositoryAddress 仓库地址
     * @param branch 分钟
     * @param fileName 文件名称
     * @return 提交信息
     * @throws IOException 仓库不存在
     */
    public static Map<String, String> findFileCommit(String repositoryAddress, String branch,String fileName) throws IOException {
        RevWalk revWalk = findRepositoryCommit(repositoryAddress, branch);
        if (revWalk == null){
            return new HashMap<>();
        }
        Git git = Git.open(new File(repositoryAddress));
        Repository repository = git.getRepository();
        Map<String, String> map = new HashMap<>();

        for (RevCommit revCommit : revWalk) {
            TreeWalk treeWalk = new TreeWalk(repository);
            treeWalk.reset(revCommit.getTree());
            while (treeWalk.next()) {
                String nameString = treeWalk.getNameString();
                if (!nameString.equals(fileName)){
                    continue;
                }

                PersonIdent message = revCommit.getAuthorIdent();
                map.put("message" ,revCommit.getShortMessage());

                String time = CodeUntil.time(message.getWhen());
                map.put("time" ,time+"前");
            }
            revCommit.disposeBody();
        }
        revWalk.dispose();
        git.close();
        return map;
    }

    /**
     * 获取仓库路径下的文件
     * @param codeMessage 仓库地址
     * @return 文件信息
     * @throws GitAPIException 获取仓库文件失败
     */
    public static List<FileTree> fileTree(CodeMessage codeMessage,String name) throws GitAPIException, ApplicationException, IOException {

        String repositoryAddress = codeMessage.getRepositoryAddress();
        String path = codeMessage.getPath();

        String branch = codeMessage.getBranch();

        if (!CodeUntil.isNoNull(branch)){
            branch = "master";
        }

        //处理地址
        if (!CodeUntil.isNoNull(path)){
            path = repositoryAddress;
        }else {
            String replace = path.replace(name+"/blob/"+branch, "").replace(name+"/tree/" + branch, "");
            path = repositoryAddress + "/" + replace;
        }

        //判断仓库是否存在文件
        RevWalk revWalk = findRepositoryCommit(repositoryAddress, branch);
        if (revWalk == null){
            throw new  ApplicationException(100,"空仓库");
        }

        //判断仓库文件目录是否存在
        File files = new File(repositoryAddress);
        if (!files.exists()){
            cloneRepository(repositoryAddress,null);
        }else {
            pullRepository(repositoryAddress);
        }

        File pathFile = new File(path);
        if (!pathFile.exists()){
            throw new  ApplicationException(50001,"路径不存在。");
        }

        File file = new File(path);
        File[] fa = file.listFiles();
        if (fa == null){
            return null;
        }

        List<FileTree> fileTrees = new ArrayList<>();

        for (File f : fa) {
            if (f.getName().equals(".git")){
                continue;
            }
            FileTree fileTree = new FileTree();
            fileTree.setFileName(f.getName());
            fileTree.setBranch(branch);

            String parent = CodeUntil.updateUrl(defaultPath,f.getParent());
            fileTree.setFileParent(CodeUntil.updateUrl(defaultPath,parent));

            String fileAddress =CodeUntil.updateUrl(defaultPath,f.getAbsolutePath());
            fileTree.setFileAddress(fileAddress);
            fileTree.setFileType("blob");
            if (f.isDirectory() ){
                fileTree.setFileType("tree");
            }

            Map<String, String> fileCommit = findFileCommit(repositoryAddress, branch, f.getName());

            String s1 = CodeUntil.updateUrl("/" + name, parent);
            String s = "/" + name +"/" + fileTree.getFileType() + "/" + branch + s1 + "/";

            fileTree.setPath(s + f.getName());
            fileTree.setCommitMessage(fileCommit.get("message"));
            fileTree.setCommitTime(fileCommit.get("time"));
            fileTrees.add(fileTree);

        }
        fileTrees.sort(Comparator.comparing(FileTree::getFileType).reversed());

        return fileTrees;
    }







}






























































