package net.tiklab.xcode.git;

import net.tiklab.xcode.file.model.FileTreeMessage;
import net.tiklab.xcode.until.CodeFinal;
import net.tiklab.xcode.until.CodeUntil;
import net.tiklab.xcode.file.model.FileTree;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GitTest {


    public static void main(String[] args) throws Exception {

        findFileTree();
    }

    private static void findFileTree(Repository repository, FileTreeMessage message) throws IOException {
        List<FileTree> list = new ArrayList<>();

        String commitId = message.getBranch();
        //判断是读取提交文件还是分支文件
        if (message.isFindCommitId()){
            commitId = repository.resolve("refs/heads/" + commitId).getName();
        }

        ObjectId commitIdObject = ObjectId.fromString(commitId);
        RevWalk walk = new RevWalk(repository);
        RevTree tree = walk.parseCommit(commitIdObject).getTree();
        TreeWalk treeWalk = new TreeWalk(repository);
        treeWalk.addTree(tree);
        treeWalk.setRecursive(false);
        while (treeWalk.next()) {
            // String path = message.getPath();
            // if (!CodeUntil.isNoNull(path)){
            //     FileTree fileTree = findFileTreeMessage(message.getPath(),treeWalk,commitId);
            //     list.add(fileTree);
            //     continue;
            // }
            // String[] strings = path.split("/");

            String pathString = treeWalk.getPathString();
            System.out.println(pathString);


        }
    }

    private static TreeWalk findTreeWalkFile(TreeWalk treeWalk,String path) throws IOException {
        String[] strings = path.substring(1).split("/");
        int length = strings.length;
        int i = 0;
        while (treeWalk.next()) {


        }
        return treeWalk;

    }


    private static FileTree findFileTreeMessage(String file ,TreeWalk treeWalk,String commitId ){
        FileTree fileTree = new FileTree();
        if (!CodeUntil.isNoNull(file)){
            String fileType = CodeFinal.FILE_TYPE_BLOB;
            if (treeWalk.isSubtree()){
                fileType = CodeFinal.FILE_TYPE_TREE;
            }
            fileTree.setFileName(treeWalk.getNameString());
            fileTree.setFileType(fileType);
            String path = "/" +
                    fileType +
                    "/" +
                    commitId +
                    treeWalk.getNameString();
            fileTree.setPath(path);
        }
        return fileTree;
    }




    private static void findFileTree() throws IOException {
        Git git = Git.open(new File("C:\\Users\\admin\\xcode\\repository\\aa.git"));

        Repository repository = git.getRepository();

        //获取分支id
        String branchName = "master";
        ObjectId head = repository.resolve("refs/heads/" + branchName);

        //获取提交id
        String commitId = "15cf56258d062bce0c1aa199b3230d3a70ec38eb";

        ObjectId commitIdObject = ObjectId.fromString(head.getName());

        RevWalk walk = new RevWalk(repository);
        RevTree tree = walk.parseCommit(commitIdObject).getTree();
        TreeWalk treeWalk = new TreeWalk(repository);
        treeWalk.addTree(tree);
        treeWalk.setRecursive(true);
        while (treeWalk.next()) {
            System.out.println("信息: " + treeWalk.getNameString());
            System.out.println("文件: " + treeWalk.getPathString());
        }
    }

    private static void findTree(TreeWalk treeWalk,  RevTree tree) throws IOException {


    }

    private static void readFile(Repository repository,TreeWalk treeWalk) throws IOException {
        ObjectId objectId = treeWalk.getObjectId(0);
        ObjectLoader loader = repository.open(objectId);
        String s = new String(loader.getBytes());
        System.out.println(s);
    }


























}








































