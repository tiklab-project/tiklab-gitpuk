package io.tiklab.gitpuk.common.git;

import io.tiklab.core.exception.SystemException;
import io.tiklab.gitpuk.file.model.FileMessage;
import io.tiklab.gitpuk.file.model.RepositoryFile;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.dircache.DirCacheBuilder;
import org.eclipse.jgit.dircache.DirCacheEntry;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class GitFileUtil {

    public static void createBareRepoFolder1(RepositoryFile repositoryFile,String rpyPath){
        String filePath = repositoryFile.getFilePath();
        String branch = repositoryFile.getBranch();

        try {
            File bareRepoDir = new File(rpyPath);
            // 加载裸仓库
            Repository repository = new FileRepositoryBuilder()
                    .setGitDir(bareRepoDir)
                    .build();


            ObjectInserter inserter = repository.newObjectInserter();
            // 获取TreeWalk
            TreeWalk treeWalk = getTreeWalk(repository, branch,"branch");

            // 初始化DirCache，用来更新索引
            DirCache dirCache = DirCache.newInCore();
            DirCacheBuilder builder = dirCache.builder();

            // 读取已有的树结构，创建新的树结构，保留已有的文件夹和文件
            TreeFormatter newTreeFormatter = new TreeFormatter();
            // 遍历已有的树，并将其重新插入新的树结构
            while (treeWalk.next()) {
                String path = treeWalk.getPathString();
                //创建相同路径下的文件
                if (path.equals(filePath)){
                    throw new SystemException("同路径下存在相同的文件");
                }
                ObjectId objectId = treeWalk.getObjectId(0);
                FileMode fileMode = treeWalk.getFileMode(0);
                newTreeFormatter.append(path, fileMode, objectId);

                // 也添加到DirCache（索引）中，确保索引同步
                DirCacheEntry entry = new DirCacheEntry(path);
                entry.setObjectId(objectId);
                entry.setFileMode(fileMode);
                builder.add(entry);
            }

            //创建的文件内容
            byte[] fileBytes =  repositoryFile.getFileData().getBytes(StandardCharsets.UTF_8);
            ObjectId newBlobId = inserter.insert(Constants.OBJ_BLOB,fileBytes);

            // 将新文件添加到文件夹中
            newTreeFormatter.append(filePath, FileMode.REGULAR_FILE, newBlobId);

            // 同时将新文件添加到索引
            DirCacheEntry newEntry = new DirCacheEntry(filePath);
            newEntry.setObjectId(newBlobId);
            newEntry.setFileMode(FileMode.REGULAR_FILE);
            builder.add(newEntry);

            // 完成DirCache构建
            builder.finish();
            ObjectId newTreeId = inserter.insert(newTreeFormatter);

            //推送commit
            pushCommit(repositoryFile,inserter, repository,newTreeId);

            // 确保索引写入（在裸仓库中这一步尤为重要）
            dirCache.write();
            dirCache.commit();

            // 关闭仓库
            repository.close();
            inserter.flush();
            inserter.close();
        }catch (Exception e){
            throw new SystemException("报错："+e.getMessage());
        }
    }

    /**
     * 更新裸仓库的文件
     * @param repositoryFile  repositoryFile
     * @param rpyPath 裸仓库地址
     */
    public static void createBareRepoFolder(RepositoryFile repositoryFile,String rpyPath){
        String filePath = repositoryFile.getFilePath();
        String branch = repositoryFile.getBranch();
        String newFileData = repositoryFile.getFileData();
        try {
            File bareRepoDir = new File(rpyPath);
            // 加载裸仓库
            Repository repository = new FileRepositoryBuilder()
                    .setGitDir(bareRepoDir)
                    .build();

            //获取裸仓库的TreeWalk
            TreeWalk treeWalk = getTreeWalk(repository, branch,"branch");

            // 初始化 DirCache
            DirCache dirCache = DirCache.newInCore();
            DirCacheBuilder builder = dirCache.builder();

            // 创建新的树对象
            ObjectDatabase objectDatabase = repository.getObjectDatabase();
            ObjectInserter inserter = objectDatabase.newInserter();
            ObjectId newBlobId = null;

            // 读取已有的树结构，创建新的树结构，保留已有的文件夹和文件
            TreeFormatter newTreeFormatter = new TreeFormatter();

            while (treeWalk.next()) {
                String path = treeWalk.getPathString();

                //创建相同路径下的文件
                if (path.equals(filePath)){
                    throw new SystemException("同路径下存在相同的文件");
                }
                ObjectId objectId = treeWalk.getObjectId(0);
                FileMode fileMode = treeWalk.getFileMode(0);

                newTreeFormatter.append(path, fileMode, objectId);
                DirCacheEntry entry = new DirCacheEntry(path);
                entry.setObjectId(objectId);
                entry.setFileMode(fileMode);
                builder.add(entry);

            }
            //写入文件内容
            byte[] bytes = newFileData.getBytes(StandardCharsets.UTF_8);
            newBlobId = inserter.insert(Constants.OBJ_BLOB, bytes.length, new ByteArrayInputStream(bytes));


            // 将新文件添加到文件夹中
            newTreeFormatter.append(filePath, FileMode.REGULAR_FILE, newBlobId);

            // 完成DirCache构建
            builder.finish();
            ObjectId newTreeIds = inserter.insert(newTreeFormatter);

            // 创建新的树对象
            ObjectId newTreeId = dirCache.writeTree(inserter);
            inserter.flush();


            //推送commit
            pushCommit(repositoryFile,inserter, repository,newTreeIds);

            // 关闭仓库
            repository.close();
        }catch (Exception e){
            throw new SystemException("报错："+e.getMessage());
        }
    }

    /**
     * 更新裸仓库的文件
     * @param repositoryFile  repositoryFile
     * @param rpyPath 裸仓库地址
     */
    public static void updateBareRepoFile(RepositoryFile repositoryFile,String rpyPath){
        String branch = repositoryFile.getBranch();
        String newFileData = repositoryFile.getFileData();
        try {
            File bareRepoDir = new File(rpyPath);
            // 加载裸仓库
            Repository repository = new FileRepositoryBuilder()
                    .setGitDir(bareRepoDir)
                    .build();

            //获取裸仓库的TreeWalk
            TreeWalk treeWalk = getTreeWalk(repository, branch,"branch");

            // 初始化 DirCache
            DirCache dirCache = DirCache.newInCore();
            DirCacheBuilder builder = dirCache.builder();

            // 创建新的树对象
            ObjectInserter inserter = repository.newObjectInserter();
            ObjectId newBlobId = null;
            while (treeWalk.next()) {
                String path = treeWalk.getPathString();

                if (path.equals(repositoryFile.getFilePath())) {
                   /* ObjectLoader loader = repository.open(treeWalk.getObjectId(0));
                    String oldFileData = new String(loader.getBytes(), StandardCharsets.UTF_8);*/
                    ObjectId objectId = treeWalk.getObjectId(0);
                    // 创建新的 Blob 对象，包含新的文件内容
                    newBlobId = inserter.insert(Constants.OBJ_BLOB, newFileData.getBytes(StandardCharsets.UTF_8));
                    // 创建 DirCacheEntry
                    DirCacheEntry entry = new DirCacheEntry(path);
                    entry.setObjectId(newBlobId);
                    entry.setFileMode(treeWalk.getFileMode(0));
                    builder.add(entry);
                } else {
                    // 保持其他文件不变
                    DirCacheEntry entry = new DirCacheEntry(path);
                    entry.setObjectId(treeWalk.getObjectId(0));
                    entry.setFileMode(treeWalk.getFileMode(0));
                    builder.add(entry);
                }
            }
            // 完成构建新的索引
            builder.finish();

            // 创建新的树对象
            ObjectId newTreeId = dirCache.writeTree(inserter);
            inserter.flush();
            //推送commit
            pushCommit(repositoryFile,inserter, repository,newTreeId);

            // 关闭仓库
            repository.close();
        }catch (Exception e){
            throw new SystemException("报错："+e.getMessage());
        }
    }


    /**
     * 删除裸仓库的文件
     * @param repositoryFile  repositoryFile
     * @param rpyPath 裸仓库地址
     */
    public static void deleteBareRepoFile(RepositoryFile repositoryFile,String rpyPath){
        String filePath = repositoryFile.getFilePath();
        String branch = repositoryFile.getBranch();

        try {
            File bareRepoDir = new File(rpyPath);
            // 加载裸仓库
            Repository repository = new FileRepositoryBuilder()
                    .setGitDir(bareRepoDir)
                    .build();

            //获取裸仓库的TreeWalk
            TreeWalk treeWalk = getTreeWalk(repository, branch,"branch");


            // 初始化 DirCache
            DirCache dirCache = DirCache.newInCore();
            DirCacheBuilder builder = dirCache.builder();

            // 遍历所有文件，除去需要删除的文件
            while (treeWalk.next()) {
                String path = treeWalk.getPathString();

                if (!path.equals(filePath)) {
                    DirCacheEntry entry = new DirCacheEntry(path);
                    entry.setObjectId(treeWalk.getObjectId(0));
                    entry.setFileMode(treeWalk.getFileMode(0));
                    builder.add(entry);
                }
            }

            // 完成构建新的索引
            builder.finish();

            // 创建新的树对象
            ObjectInserter inserter = repository.newObjectInserter();
            ObjectId newTreeId = dirCache.writeTree(inserter);
            inserter.flush();

            //推送commit
            pushCommit(repositoryFile,inserter, repository,newTreeId);

            // 关闭仓库
            repository.close();

        }catch (Exception e){
            throw new SystemException("报错："+e.getMessage());
        }
    }



    /**
     * 下载裸仓库中的文件
     * @param filePath  filePath
     * @param bareAddress 裸仓库地址
     * @param branch 分支
     */
    public static byte[] downLoadBareRepoFile(String filePath,String bareAddress,String branch){

        try {
            File bareRepoDir = new File(bareAddress);
            // 加载裸仓库
            Repository repository = new FileRepositoryBuilder()
                    .setGitDir(bareRepoDir)
                    .build();
            //获取裸仓库的TreeWalk
            TreeWalk treeWalk = getTreeWalk(repository, branch,"branch");
            ObjectId fileBlobId = null;

            while (treeWalk.next()) {
                if (treeWalk.getPathString().equals(filePath)) {
                    fileBlobId = treeWalk.getObjectId(0);
                    break;
                }
            }
            if (fileBlobId != null) {
                // 获取文件 Blob
                ObjectLoader loader = repository.open(fileBlobId);

               /* // 将文件保存到本地
                File outputFile = new File("/path/to/local/file");
                try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                    loader.copyTo(outputStream);
                }*/
                byte[] bytes = loader.getBytes();
                // 关闭仓库
                repository.close();
                return bytes;
            }
            // 关闭仓库
            repository.close();

            return null;
        }catch (Exception e){
            throw new SystemException("报错："+e.getMessage());
        }
    }


    /**
     * 下载裸仓库 为zip
     * @param bareAddress 裸仓库地址
     * @param branch 分支
     * @param headType 类型 tag、branch、commit
     */
    public static void downLoadBareRepoZip(String bareAddress, String branch,String headType, HttpServletResponse response) {
        try {
            File bareRepoDir = new File(bareAddress);
            // 加载裸仓库
            Repository repository = new FileRepositoryBuilder()
                    .setGitDir(bareRepoDir)
                    .build();
            //获取裸仓库的TreeWalk
            TreeWalk treeWalk = getTreeWalk(repository, branch,headType);
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());

            // 遍历仓库中的所有文件并打包为 ZIP
            while (treeWalk.next()) {
                String path = treeWalk.getPathString();
                ObjectId blobId = treeWalk.getObjectId(0);

                // 获取文件 Blob
                ObjectLoader loader = repository.open(blobId);

                // 将文件添加到 ZIP
                ZipEntry zipEntry = new ZipEntry(path);
                zos.putNextEntry(zipEntry);

                // 将文件内容写入 ZIP
                loader.copyTo(zos);
                zos.closeEntry();
            }
            zos.finish(); // 完成 ZIP 文件的写入
            response.flushBuffer(); // 刷新输出流
        }catch (Exception e){
            throw new SystemException("报错："+e.getMessage());
        }
    }

    /**
     * 下载裸仓库 为tar
     * @param bareAddress 裸仓库地址
     * @param branch 分支
     * @param headType 类型 tag、branch、commit
     */
    public static void downLoadBareRepoTar(String bareAddress, String branch,String headType,
                                           HttpServletResponse response) {
        try {
            File bareRepoDir = new File(bareAddress);
            // 加载裸仓库
            Repository repository = new FileRepositoryBuilder()
                    .setGitDir(bareRepoDir)
                    .build();
            //获取裸仓库的TreeWalk
            TreeWalk treeWalk = getTreeWalk(repository, branch,headType);
            TarArchiveOutputStream tarOut = new TarArchiveOutputStream(response.getOutputStream());
            tarOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
            // 遍历仓库中的所有文件并打包为 tar
            while (treeWalk.next()) {
                String path = treeWalk.getPathString();
                ObjectId blobId = treeWalk.getObjectId(0);

                // 获取文件 Blob
                ObjectLoader loader = repository.open(blobId);

                // 创建 tar 条目
                TarArchiveEntry tarEntry = new TarArchiveEntry(path);

                tarEntry.setSize(loader.getSize());
                tarOut.putArchiveEntry(tarEntry);

                // 将文件内容写入 tar
                loader.copyTo(tarOut);
                tarOut.closeArchiveEntry();
            }
            tarOut.finish(); // 完成 tar 文件的写入
            response.flushBuffer(); // 刷新输出流
        }catch (Exception e){
            throw new SystemException("报错："+e.getMessage());
        }
    }


    /**
     * 查询裸仓库所有文件
     * @param repositoryAddress 裸仓库地址
     * @param bareHead 裸仓库查询
     * @param bareHeadType head类型 branch、tag、commit
     */
    public static List<String> findBareAllFile(String repositoryAddress,String bareHead,String bareHeadType){
        try {
            List<String> fileList = new ArrayList<>();
            File bareRepoDir = new File(repositoryAddress);
            // 加载裸仓库
            Repository repository = new FileRepositoryBuilder()
                    .setGitDir(bareRepoDir)
                    .build();

            //获取裸仓库的TreeWalk
            TreeWalk treeWalk = getTreeWalk(repository, bareHead,bareHeadType);
            // 遍历仓库中的所有文件并打包为 tar
            while (treeWalk.next()) {
                String path = treeWalk.getPathString();
                fileList.add(path);
            }
            repository.close();
            return fileList;
        }catch (Exception e){
            throw new SystemException("获取文件失败"+e.getMessage());
        }
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
    public static FileMessage readBranchFile(String repositoryAddress, String filePath, String refCode, String refCodeType) throws IOException {
        FileMessage fileMessage = new FileMessage();

        Git git = Git.open(new File(repositoryAddress));
        Repository repository = git.getRepository();

        //获取 查询refCode 的循环树对象
        RevCommit headCommit = getHeadCommit(repository, refCode, refCodeType);
        RevTree tree = headCommit.getTree();
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
     * 获取裸仓库的TreeWalk
     * @param bareHead  分支
     * @param repository 打开的裸仓库
     */
    public static TreeWalk getTreeWalk( Repository repository,String bareHead,String bareHeadType) throws IOException {
        RevCommit headCommit = getHeadCommit(repository, bareHead,bareHeadType);
        RevTree tree = headCommit.getTree();

        // 用 TreeWalk 遍历当前的树
        TreeWalk treeWalk = new TreeWalk(repository);
        treeWalk.addTree(tree);
        treeWalk.setRecursive(true);
        return treeWalk;
    }

    /**
     * 获取裸仓库的HeadCommit
     * @param refCode  refCode
     * @param repository 打开的裸仓库
     */
    public static RevCommit getHeadCommit( Repository repository,String refCode,String refCodeType) throws IOException {
        ObjectId headId;
        if (("branch").equals(refCodeType)){
            // 找到 HEAD commit
             headId = repository.resolve(Constants.R_HEADS +refCode);
            //headId=  repository.findRef(Constants.R_HEADS +branch).getObjectId();
        }else if (("tag").equals(refCodeType)){
            headId = repository.resolve(Constants.R_TAGS +refCode);
        }else {
            //提交commitId
            headId = ObjectId.fromString(refCode);
        }
        RevWalk revWalk = new RevWalk(repository);
        RevCommit headCommit = revWalk.parseCommit(headId);
        return headCommit;
    }


    /**
     * 推送提交
     * @param repositoryFile    repositoryFile
     * @param inserter          新数对象
     * @param  repository       裸仓库对象
     * @param  newTreeId        新树对象
     */
    public static void pushCommit(RepositoryFile repositoryFile,ObjectInserter inserter,
                                  Repository repository,ObjectId newTreeId) throws IOException {


        RevCommit headCommit = getHeadCommit(repository, repositoryFile.getBranch(),"branch");

        // 创建新的提交
        CommitBuilder commitBuilder = new CommitBuilder();
        commitBuilder.setTreeId(newTreeId);
        commitBuilder.setParentId(headCommit);
        commitBuilder.setAuthor(new PersonIdent("Author", "author@example.com"));
        commitBuilder.setCommitter(new PersonIdent("Committer", "committer@example.com"));
        commitBuilder.setMessage(repositoryFile.getCommitMessage());

        // 插入新的提交对象
        ObjectId newCommitId = inserter.insert(commitBuilder);
        inserter.flush();
        inserter.close();

        // 更新引用 (如 master)
        RefUpdate refUpdate = repository.updateRef("refs/heads/"+repositoryFile.getBranch());
        refUpdate.setNewObjectId(newCommitId);
        //refUpdate.setExpectedOldObjectId(ObjectId.zeroId());
        refUpdate.setForceUpdate(true);
        refUpdate.update();

    }

}
