package net.tiklab.xcode.until;

import net.tiklab.core.exception.ApplicationException;
import net.tiklab.xcode.code.model.CodeMessage;
import net.tiklab.xcode.file.model.CodeFileMessage;
import net.tiklab.xcode.file.model.FileTree;
import net.tiklab.xcode.file.model.FileTreeMessage;
import net.tiklab.xcode.git.GitBranchUntil;
import net.tiklab.xcode.git.GitCommitUntil;
import net.tiklab.xcode.git.GitUntil;
import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.attributes.Attributes;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.junit.Ignore;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CodeFileUntil {

    /**
     * 字符串写入文件
     * @param str 字符串
     * @param path 文件地址
     * @throws ApplicationException 写入失败
     */
    public static void writeFile(String str, String path) throws ApplicationException {
        try (FileWriter writer = new FileWriter(path, StandardCharsets.UTF_8)) {
            writer.write(str);
            writer.flush();
        } catch (Exception e) {
            throw new ApplicationException("文件写入失败。"+e.getMessage());
        }
    }

    /**
     * 创建目录
     * @param address 文件地址
     * @throws ApplicationException 文件创建失败
     */
    public static void createDirectory(String address) throws ApplicationException {
        File file = new File(address);
        if (file.exists()) {
            return;
        }
        int i = 0;
        boolean b = false;
        if (!file.exists()) {
            while (!b && i <= 10) {
                b = file.mkdirs();
                i++;
            }
        }
        if (i >= 10) {
            throw new ApplicationException("项目工作目录创建失败。");
        }
    }

    /**
     * 创建文件
     * @param address 文件地址
     * @throws ApplicationException 文件创建失败
     */
    public static String createFile(String address) throws ApplicationException {
        File file = new File(address);
        String parent = file.getParent();
        File parentFile = new File(parent);
        try {
            if (!parentFile.exists()){
                createDirectory(parent);
            }
            boolean newFile = file.createNewFile();
        } catch (IOException | ApplicationException e) {
            throw new ApplicationException("文件创建失败。");
        }
        return file.getAbsolutePath();

    }

    /**
     * 读取文件
     * @param fileAddress 文件地址
     * @return 内容
     */
    public static String readFile(String fileAddress) throws ApplicationException {

        File file = new File(fileAddress);

        if (!file.exists()){
            return null;
        }

        Path path = Paths.get(fileAddress);

        StringBuilder s = new StringBuilder();
        List<String> lines = null;
        try {
            lines = Files.readAllLines(path,StandardCharsets.UTF_8);
            for (String line : lines) {
                s.append(line).append("\n");
            }
        } catch (IOException e) {
            throw new ApplicationException("读取文件信息失败"+ e.getMessage());
        }
        return s.toString();
    }

    /**
     * 修改文件名称
     * @param fileAddress 文件上级目录
     * @param oldName 旧名称
     * @param newName 新名称
     */
    public static boolean updateFileName(String fileAddress,String oldName,String newName){
        File file = new File(fileAddress + "/" + oldName);
        return file.renameTo(new File(fileAddress + "/" + newName));
    }


    /**
     * 删除文件及文件夹
     * @param file 文件地址
     * @return 是否删除 true 删除成功,false 删除失败
     */
    public static Boolean deleteFile(File file){
        if (file.isDirectory()) {
            String[] children = file.list();
            //递归删除目录中的子目录下
            if (children != null) {
                for (String child : children) {
                    boolean state = deleteFile(new File(file, child));
                    int tryCount = 0;
                    while (!state && tryCount ++ < 10) {
                        System.gc();
                        state = file.delete();
                    }
                }
            }
            // 目录此时为空，删除
        }
        return file.delete();
    }

    /**
     * 获取仓库文件
     * @param message 文件信息
     * @return 文件树
     * @throws IOException 仓库不存在
     */
    public static List<FileTree> findFileTree(Git git, FileTreeMessage message) throws IOException, GitAPIException {

        Repository repository = git.getRepository();

        List<FileTree> list = new ArrayList<>();
        String commitId = message.getBranch();

        RevTree tree = GitBranchUntil.findBarthCommitRevTree(repository, commitId, message.isFindCommitId());

        TreeWalk treeWalk = new TreeWalk(repository);
        treeWalk.addTree(tree);
        treeWalk.setRecursive(true);
        Map<String,String> map = new HashMap<>();
        while (treeWalk.next()) {
            FileTree fileTree = new FileTree();
            String file = message.getPath();
            //获取父级路径
            if (CodeUntil.isNoNull(file)){
                fileTree.setFileParent(file.substring(0,file.lastIndexOf("/")));
            }
            String pathString = treeWalk.getPathString();
            String type = null;
            if (!CodeUntil.isNoNull(file)){
                int i = pathString.indexOf("/");
                if (i >= 0){
                    type = CodeFinal.FILE_TYPE_TREE;
                    String substring = pathString.substring(0, i);
                    String s = map.get(substring);
                    if (s != null){
                        continue;
                    }
                    fileTree.setFileName(substring);
                    map.put(substring,substring);
                }else {
                    type = CodeFinal.FILE_TYPE_BLOB;
                    fileTree.setFileName(pathString);
                }
            }

            if (CodeUntil.isNoNull(file) &&
                    pathString.startsWith(file.substring(1))
                    && pathString.contains("/")){

                String substring = file.substring(1)+"/";
                String replace = pathString.replace(substring,"");
                int i = replace.indexOf("/");
                if (i >= 0){
                    type = CodeFinal.FILE_TYPE_TREE;
                    String substring1 = replace.substring(0, i);
                    fileTree.setFileName(substring1);
                    String s = map.get(substring1);
                    if (s != null){
                        continue;
                    }
                    map.put(substring1,substring1);
                }else {
                    type = CodeFinal.FILE_TYPE_BLOB;
                    fileTree.setFileName(replace);
                }
            }

            String fileName = fileTree.getFileName();
            if (fileName == null){
                continue;
            }
            int i = fileName.lastIndexOf(".");
            if (i >= 0){
                String substring = fileName.substring(i+1);
                fileTree.setFileType(substring);
            }
            fileTree.setType(type);
            String path;
            String fileAddress = fileName;

            String branch = message.getBranch();
            if (message.isFindCommitId()){
                branch = branch + CodeFinal.COMMIT_ONLY_ID;
            }

            if (!CodeUntil.isNoNull(file)){
                path = "/" + type + "/" + branch + "/" + fileName;
            }else {
                fileAddress = file.substring(1)+"/"+ fileName;
                path = "/" + type + "/" + branch + file+"/"+ fileName;
            }
            fileTree.setPath(path);

            //提交信息
            ObjectId objectId = GitBranchUntil.findBarthCommitId(repository, commitId, message.isFindCommitId());
            List<Map<String, String>> commitList = GitCommitUntil.gitFileCommitLog(git,objectId.getName(),fileAddress);
            if (!commitList.isEmpty()){
                Map<String, String> fileCommit = commitList.get(0);
                fileTree.setCommitMessage(fileCommit.get("message"));
                fileTree.setCommitTime(fileCommit.get("time"));
            }
            list.add(fileTree);
        }
        treeWalk.close();
        list.sort(Comparator.comparing(FileTree::getType).reversed());
        return list;
    }

    /**
     * 读取指定分支的指定文件的文件信息
     * @param repository 仓库
     * @param branch 分支
     * @param filePath 文件信息
     * @param b 是否为提交id
     * @return 文件信息
     * @throws IOException 仓库不存在
     */
    public static CodeFileMessage readBranchFile(Repository repository,String branch,String filePath,boolean b) throws IOException {
        RevTree tree = GitBranchUntil.findBarthCommitRevTree(repository, branch, b);
        CodeFileMessage fileMessage = new CodeFileMessage();
        TreeWalk treeWalk = TreeWalk.forPath(repository, filePath, tree);

        if (treeWalk != null) {
            ObjectId objectId = treeWalk.getObjectId(0);
            String name = treeWalk.getNameString();
            ObjectLoader loader = repository.open(objectId);
            byte[] bytes = loader.getBytes();
            String content = new String(bytes, StandardCharsets.UTF_8);
            treeWalk.close();

            long objectSize = treeWalk.getObjectReader().getObjectSize(treeWalk.getObjectId(0), Constants.OBJ_BLOB);
            float fileSize = (float)objectSize/ 1024 ;
            String str = String.format("%.2f", fileSize)+"KB";
            fileMessage.setFileSize(str);
            fileMessage.setFileName(name);
            fileMessage.setFilePath(treeWalk.getPathString());
            fileMessage.setFileType(name.substring(name.lastIndexOf(".")+1));
            fileMessage.setFileMessage(content);
        }
        return fileMessage;
    }

    // /**
    //  * 获取仓库路径下的文件树
    //  * @param codeMessage 仓库地址
    //  * @return 文件信息
    //  * @throws GitAPIException 获取仓库文件失败
    //  */
    // public static List<FileTree> fileTree(CodeMessage codeMessage) throws GitAPIException, ApplicationException, IOException {
    //
    //     String repositoryAddress = codeMessage.getRepositoryAddress();
    //
    //     String name = codeMessage.getRepositoryName();
    //     String branch = codeMessage.getBranch();
    //     String address = codeMessage.getAddress();
    //
    //     String defaultAddress =CodeUntil.defaultPath().replace("\\","/");
    //
    //     //判断仓库文件目录是否存在
    //     File files = new File(repositoryAddress+"_"+branch);
    //     if (files.exists() && !CodeUntil.isNoNull(codeMessage.getPath())){
    //         CodeFileUntil.deleteFile(files);
    //     }
    //
    //     //克隆最新的仓库
    //     String path = repositoryAddress+"_"+branch;
    //     String messagePath = codeMessage.getPath();
    //     if (CodeUntil.isNoNull(messagePath)){
    //         path = path + messagePath;
    //     }else {
    //         GitUntil.cloneRepository(repositoryAddress,branch);
    //     }
    //
    //     //判断仓库文件是否存在
    //     File pathFile = new File(path);
    //     if (!pathFile.exists()){
    //         throw new  ApplicationException(50001,"路径不存在。");
    //     }
    //
    //     //是否文空文件夹
    //     File[] fa = pathFile.listFiles();
    //     if (fa == null){
    //         return null;
    //     }
    //
    //     List<FileTree> fileTrees = new ArrayList<>();
    //
    //
    //     Git git = Git.open(new File(repositoryAddress+".git"));
    //     Repository repo = git.getRepository();
    //
    //     for (File f : fa) {
    //
    //         FileTree fileTree = new FileTree();
    //         fileTree.setFileName(f.getName());
    //         fileTree.setBranch(branch);
    //
    //         String s2 = defaultAddress + "/" + name + "_" + branch;
    //
    //         //忽略git仓库的.git文件夹
    //         if (f.getName().equals(".git") && new File(s2).getName().equals(new File(f.getParent()).getName())){
    //             continue;
    //         }
    //
    //         String replace = f.getAbsolutePath().replace("\\", "/");
    //         String parentReplace = f.getParent().replace("\\", "/");
    //
    //         String parent = parentReplace.replace(defaultAddress,"");
    //         String fileAddress =replace.replace(defaultAddress,"");
    //
    //         fileTree.setFileParent(parent);
    //         fileTree.setFileAddress(fileAddress);
    //
    //         //文件类型
    //         String suffix = null;
    //         fileTree.setType("tree");
    //         if (!f.isDirectory() ){
    //             String fileName = f.getName();
    //             if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
    //                 suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
    //             }
    //             fileTree.setType("blob");
    //         }
    //         fileTree.setFileType(suffix);
    //
    //         String s1 = "/" + address+"_"+branch;
    //
    //         String s = fileAddress.substring(fileAddress.indexOf(s1)+s1.length()) ;
    //
    //         s = "/" + name +"/" + fileTree.getType() + "/" + branch + s ;
    //
    //         List<Map<String, String>> list = GitCommitUntil.gitFileCommitLog(git, f.getName());
    //         Map<String, String> fileCommit = list.get(0);
    //         fileTree.setPath(s);
    //         fileTree.setCommitMessage(fileCommit.get("message"));
    //         fileTree.setCommitTime(fileCommit.get("time"));
    //         fileTrees.add(fileTree);
    //
    //     }
    //     fileTrees.sort(Comparator.comparing(FileTree::getType).reversed());
    //     // repo.close();
    //     git.close();
    //
    //     return fileTrees;
    // }


}



































