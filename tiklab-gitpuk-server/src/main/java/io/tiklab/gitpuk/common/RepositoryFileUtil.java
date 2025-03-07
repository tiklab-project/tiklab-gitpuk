package io.tiklab.gitpuk.common;

import io.tiklab.gitpuk.common.git.GitBranchUntil;
import io.tiklab.gitpuk.common.git.GitCommitUntil;
import io.tiklab.gitpuk.file.model.FileFindQuery;
import io.tiklab.gitpuk.file.model.FileMessage;
import io.tiklab.gitpuk.file.model.FileTree;
import io.tiklab.core.exception.ApplicationException;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class RepositoryFileUtil {


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
    public static void createDirectory(String address)    {
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
            throw new ApplicationException("项目工作目录创建失败。"+address);
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
     * 获取仓库的所有file
     * @param git 文件信息
     * @return branch 分支名字
     */
    public static List<String> findRepositoryAllFile(Git git,String branch) throws IOException {
        Repository repository = git.getRepository();

        RevTree tree = GitBranchUntil.findBarthCommitRevTree(repository,branch,"branch");
        TreeWalk treeWalk = new TreeWalk(repository);
        treeWalk.addTree(tree);
        treeWalk.setRecursive(true);
        List<String> arrayList = new ArrayList<>();
        while (treeWalk.next()) {
            String pathString = treeWalk.getPathString();
            arrayList.add(pathString);
        }
        return arrayList;
    }

    /**
     * 获取仓库文件
     * @param message 文件信息
     * @return 文件树
     * @throws IOException 仓库不存在
     */
    public static List<FileTree> findFileTree(String repositoryAddress, FileFindQuery message) throws IOException, GitAPIException {
        //打开裸仓库
        File rpyFile = new File(repositoryAddress);
        Git git = Git.open(rpyFile);
        Repository repository = git.getRepository();

        //获取分子的提交revTree
        List<FileTree> list = new ArrayList<>();
        String commitId = message.getRefCode();
        RevTree tree =GitBranchUntil.findBarthCommitRevTree(repository,commitId,message.getRefCodeType());

        //TreeWalk，用于遍历Git仓库中的文件和目录。通过使用TreeWalk，我们可以获取指定分支或提交中的文件内容。
        TreeWalk treeWalk = new TreeWalk(repository);
        treeWalk.addTree(tree);
        treeWalk.setRecursive(true);
        Map<String,String> map = new HashMap<>();

        while (treeWalk.next()) {
            FileTree fileTree = new FileTree();
            String pathString = treeWalk.getPathString();

            // 读取文件内容，判断是否是lfs文件
            ObjectId objectId1 = treeWalk.getObjectId(0);
            ObjectLoader loader = repository.open(objectId1);
            long size = loader.getSize();
            if (size<5000){
                //ByteArrayInputStream 最大大小理论上是约2GB，lfs文件内容大小小于5000,因此只读取小于5000字节的文件内容
                ByteArrayInputStream in = new ByteArrayInputStream(loader.getBytes());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String firstLine = reader.readLine();
                if (firstLine != null && firstLine.contains("https://git-lfs.github.com/spec/v1")){
                    fileTree.setLfs(true);
                }
            }
            //获取父级路径
            String file = message.getPath();
            if (RepositoryUtil.isNoNull(file)){
                fileTree.setFileParent(file.substring(0,file.lastIndexOf("/")));
            }
            String type = null;
            if (!RepositoryUtil.isNoNull(file)){
                int i = pathString.indexOf("/");
                if (i >= 0){
                    type = Constants.TYPE_TREE;
                    String substring = pathString.substring(0, i);
                    String s = map.get(substring);
                    if (s != null){
                        continue;
                    }
                    fileTree.setFileName(substring);
                    map.put(substring,substring);
                }else {
                    type = Constants.TYPE_BLOB;
                    fileTree.setFileName(pathString);
                }
            }

            if (RepositoryUtil.isNoNull(file) && pathString.startsWith(file.substring(1))
                    && pathString.contains("/")){

                String substring = file.substring(1)+"/";
                String replace = pathString.replace(substring,"");
                int i = replace.indexOf("/");
                if (i >= 0){
                    type = Constants.TYPE_TREE;
                    String substring1 = replace.substring(0, i);
                    fileTree.setFileName(substring1);
                    String s = map.get(substring1);
                    if (s != null){
                        continue;
                    }
                    map.put(substring1,substring1);
                }else {
                    type = Constants.TYPE_BLOB;
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

            String branch = message.getRefCode();

            ObjectId objectId = GitBranchUntil.findObjectId(repository, branch, message.getRefCodeType());
            List<Map<String, String>> commitList = GitCommitUntil.gitFileCommitLog(git,objectId.getName(),pathString);
            if (!commitList.isEmpty()){
                Map<String, String> fileCommit = commitList.get(0);
                fileTree.setCommitMessage(fileCommit.get("message"));
                fileTree.setCommitTime(fileCommit.get("time"));
            }

        /*    if (("commit").equals(message.getRefCodeType())){
                branch = branch + RepositoryFinal.COMMIT_ONLY_ID;
            }
            if (("tag").equals(message.getRefCodeType())){
                branch = branch + RepositoryFinal.TAG;
            }*/
            type =type.equals("tree")?"code":type;
            if (!RepositoryUtil.isNoNull(file)){
                path = "/" + type + "/" + branch + "/" + fileName;
            }else {
                fileAddress = file.substring(1)+"/"+ fileName;
                path = "/" + type + "/" + branch + file+"/"+ fileName;
            }
            fileTree.setPath(path);
            list.add(fileTree);
        }
        treeWalk.close();
        list.sort(Comparator.comparing(FileTree::getType).reversed());
        git.close();
        return list;
    }

    /**
     * 读取指定分支、标签、提交的指定文件的文件信息
     * @param repositoryAddress 裸仓库地址
     * @param refCode refCode
     * @param filePath 文件信息
     * @param refCodeType refCodeType
     * @return 文件信息
     * @throws IOException 仓库不存在
     */
    public static FileMessage readBranchFile(String repositoryAddress, String refCode, String filePath, String refCodeType) throws IOException {
        Git git = Git.open(new File(repositoryAddress));
        org.eclipse.jgit.lib.Repository repository = git.getRepository();

        RevTree tree = GitBranchUntil.findBarthCommitRevTree(repository, refCode, refCodeType);



        FileMessage fileMessage = new FileMessage();
        TreeWalk treeWalk = TreeWalk.forPath(repository, filePath, tree);

        if (treeWalk != null) {
            //读取文件的内容
            ObjectId objectId = treeWalk.getObjectId(0);
            String name = treeWalk.getNameString();
           if (name.contains("/")){
               name=StringUtils.substringAfterLast(name,"/");
           }
            //文件类型
            int i = name.lastIndexOf(".") + 1;
            String substring = name.substring(i);

            ObjectLoader loader = repository.open(objectId);
            byte[] bytes = loader.getBytes();
            String content = new String(bytes, StandardCharsets.UTF_8);

            //文件大小
            long objectSize = treeWalk.getObjectReader().getObjectSize(objectId, Constants.OBJ_BLOB);
            float fileSize = (float)objectSize/ 1024 ;
            String str = String.format("%.2f", fileSize)+"KB";



            //是否是lfs文件
            if (content != null && content.contains("https://git-lfs.github.com/spec/v1")){
                String[] split = content.split("\n");
                String oidSha = split[1];
                String oid = StringUtils.substringAfter(oidSha, "sha256:");
                fileMessage.setOid(oid);
            }

            fileMessage.setFileSize(str);
            fileMessage.setFileName(name);
            fileMessage.setFilePath(treeWalk.getPathString());
            fileMessage.setFileMessage(content);
            fileMessage.setFileType(substring);

            treeWalk.close();
        }
        return fileMessage;
    }






    /**
     * 递归压缩当前文件夹 tar.gz
     * @param sourceFolder 当前文件夹路径
     * @param tos 压缩文件ZipOutputStream
     */
    public static void compressFolder(String sourceFolder, String parentEntryPath, TarArchiveOutputStream tos) throws IOException {
        File folder = new File(sourceFolder);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 如果是子文件夹，创建对应的tar条目，然后递归压缩子文件夹中的内容
                    String entryName = parentEntryPath + file.getName() + "/";
                    TarArchiveEntry entry = new TarArchiveEntry(entryName);
                    tos.putArchiveEntry(entry);
                    tos.closeArchiveEntry();
                    compressFolder(file.getAbsolutePath(), entryName, tos);
                } else {
                    // 如果是文件，创建对应的tar条目，并将文件内容写入tar输出流中
                    String entryName = parentEntryPath + file.getName();
                    TarArchiveEntry entry = new TarArchiveEntry(entryName);
                    entry.setSize(file.length());
                    tos.putArchiveEntry(entry);

                    try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file))) {
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = bis.read(buffer)) != -1) {
                            tos.write(buffer, 0, bytesRead);
                        }
                    }

                    // 关闭当前条目
                    tos.closeArchiveEntry();
                }
            }
        }
    }

    /**
     * 解压tar.gz文件夹
     * @param outputFolderPath 解压路径
     * @param inputFilePath 压缩包文件路径
     */

    public static void decompression(String inputFilePath,String outputFolderPath) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(inputFilePath);
        GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream);
        TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(gzipInputStream);

        TarArchiveEntry entry;
        while ((entry = tarArchiveInputStream.getNextTarEntry()) != null) {
            String entryName = entry.getName();
            File outputFile = new File(outputFolderPath + entryName);

            if (entry.isDirectory()) {
                outputFile.mkdirs();
                continue;
            }

            File parentDir = outputFile.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = tarArchiveInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            fileOutputStream.close();
        }

        tarArchiveInputStream.close();
        gzipInputStream.close();
        fileInputStream.close();
    }

    /**
     * 解压zip文件夹
     * @param outputFolderPath 解压路径
     * @param inputFilePath 压缩包文件路径
     */

    public static void decompressionZip(String inputFilePath,String outputFolderPath) throws IOException {

        File targetFolder = new File(outputFolderPath);

        // 创建目标文件夹（如果不存在）
        if (!targetFolder.exists()) {
            targetFolder.mkdirs();
        }

        byte[] buffer = new byte[1024];

        // 创建zip文件输入流
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(inputFilePath));

        // 获取zip文件中的每个entry
        ZipEntry zipEntry = zipInputStream.getNextEntry();

        while (zipEntry != null) {
            String entryName = zipEntry.getName();

            // 构建目标文件路径
            File extractedFile = new File(targetFolder, entryName);

            // 如果entry是一个文件，则解压缩
            if (!zipEntry.isDirectory()) {
                // 创建目标文件的父目录（如果不存在）
                if (!extractedFile.getParentFile().exists()) {
                    extractedFile.getParentFile().mkdirs();
                }

                // 创建输出流，将entry解压到目标文件
                FileOutputStream outputStream = new FileOutputStream(extractedFile);
                int length;
                while ((length = zipInputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.close();
            }

            // 关闭当前entry，继续获取下一个entry
            zipInputStream.closeEntry();
            zipEntry = zipInputStream.getNextEntry();
        }

        // 关闭zip文件输入流
        zipInputStream.close();
    }
}
