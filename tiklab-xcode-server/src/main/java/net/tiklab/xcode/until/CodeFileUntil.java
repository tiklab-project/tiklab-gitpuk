package net.tiklab.xcode.until;

import net.tiklab.core.exception.ApplicationException;
import net.tiklab.xcode.code.model.CodeMessage;
import net.tiklab.xcode.git.GitCommitUntil;
import net.tiklab.xcode.git.GitUntil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
     * 获取仓库路径下的文件树
     * @param codeMessage 仓库地址
     * @return 文件信息
     * @throws GitAPIException 获取仓库文件失败
     */
    public static List<FileTree> fileTree(CodeMessage codeMessage) throws GitAPIException, ApplicationException, IOException {

        String repositoryAddress = codeMessage.getRepositoryAddress();

        String name = codeMessage.getRepositoryName();
        String branch = codeMessage.getBranch();
        String address = codeMessage.getAddress();

        String defaultAddress =CodeUntil.defaultPath().replace("\\","/");

        //判断仓库文件目录是否存在
        File files = new File(repositoryAddress+"_"+branch);
        if (files.exists() && !CodeUntil.isNoNull(codeMessage.getPath())){
            CodeFileUntil.deleteFile(files);
        }

        //克隆最新的仓库
        String path = repositoryAddress+"_"+branch;
        String messagePath = codeMessage.getPath();
        if (CodeUntil.isNoNull(messagePath)){
            path = path + codeMessage.getPath();;
        }else {
            GitUntil.cloneRepository(repositoryAddress,branch);
        }

        //判断仓库文件是否存在
        File pathFile = new File(path);
        if (!pathFile.exists()){
            throw new  ApplicationException(50001,"路径不存在。");
        }

        //是否文空文件夹
        File[] fa = pathFile.listFiles();
        if (fa == null){
            return null;
        }

        List<FileTree> fileTrees = new ArrayList<>();


        Git git = Git.open(new File(repositoryAddress+".git"));
        Repository repo = git.getRepository();

        for (File f : fa) {

            FileTree fileTree = new FileTree();
            fileTree.setFileName(f.getName());
            fileTree.setBranch(branch);

            String s2 = defaultAddress + "/" + name + "_" + branch;

            //忽略.git文件夹
            if (f.getName().equals(".git") && new File(s2).getName().equals(new File(f.getParent()).getName())){
                continue;
            }

            String replace = f.getAbsolutePath().replace("\\", "/");
            String parentReplace = f.getParent().replace("\\", "/");

            String parent = parentReplace.replace(defaultAddress,"");
            String fileAddress =replace.replace(defaultAddress,"");

            fileTree.setFileParent(parent);
            fileTree.setFileAddress(fileAddress);

            //文件类型
            String suffix = null;
            fileTree.setType("tree");
            if (!f.isDirectory() ){
                String fileName = f.getName();
                if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
                    suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                }
                fileTree.setType("blob");
            }
            fileTree.setFileType(suffix);

            String s1 = "/" + address+"_"+branch;

            String s = fileAddress.substring(fileAddress.indexOf(s1)+s1.length()) ;

            s = "/" + name +"/" + fileTree.getType() + "/" + branch + s ;

            List<Map<String, String>> list = GitCommitUntil.gitFileCommitLog(git, f.getName());

            Map<String, String> fileCommit = list.get(0);

            fileTree.setPath(s);
            fileTree.setCommitMessage(fileCommit.get("message"));
            fileTree.setCommitTime(fileCommit.get("time"));
            fileTrees.add(fileTree);

        }
        fileTrees.sort(Comparator.comparing(FileTree::getType).reversed());
        repo.close();
        git.close();

        return fileTrees;
    }








}



































