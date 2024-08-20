package io.thoughtware.gittok.file.service;

import io.thoughtware.core.exception.SystemException;
import io.thoughtware.gittok.common.RepositoryFileUtil;
import io.thoughtware.gittok.common.RepositoryUtil;
import io.thoughtware.gittok.common.GitTokYamlDataMaService;
import io.thoughtware.gittok.common.git.GitUntil;
import io.thoughtware.gittok.file.model.FileMessage;
import io.thoughtware.gittok.file.model.FileQuery;
import io.thoughtware.gittok.file.model.RepositoryFile;
import io.thoughtware.gittok.repository.service.RepositoryService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

@Service
public class RepositoryFileServerImpl implements RepositoryFileServer {

    @Autowired
    RepositoryService repositoryServer;

    @Autowired
    GitTokYamlDataMaService yamlDataMaService;

    @Override
    public void createBareFolder(RepositoryFile repositoryFile) {
        String rpyPath = yamlDataMaService.repositoryAddress();
        //裸仓库地址
        String address = RepositoryUtil.findRepositoryAddress(rpyPath, repositoryFile.getRepositoryId());

        //临时文件夹
        String temporaryPath = RepositoryUtil.getRpyTemporaryPath(rpyPath, repositoryFile.getRepositoryId());

        try {
            //clone 仓库
            GitUntil.cloneRepositoryByFile(address,temporaryPath,repositoryFile.getBranch());

            File repoDir = new File(temporaryPath);
            Git git = Git.open(repoDir);
            String folderPath = repositoryFile.getFolderPath();  // 文件夹路径
            String fileName = repositoryFile.getFileName();    // 文件名

            //添加仓库的文件夹
            File folder = new File(repoDir, folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File file = new File(repoDir, folderPath + "/" + fileName);
            // 创建文件及写入内容
            FileWriter writer = new FileWriter(file);
           // writer.write("This is the content of the new file.");

            // 将新文件添加到 Git 暂存区
            git.add().addFilepattern(".").call();
            git.commit().setMessage("Add new file: " + fileName).call();
            //推送到裸仓库
            GitUntil. pushAllBranchRepository(temporaryPath,address);

            //删除临时文件数据
            FileUtils.deleteDirectory(new File(temporaryPath));
        }catch (Exception e){
            throw new SystemException(e);
        }
    }

    @Override
    public void deleteBareFile(RepositoryFile repositoryFile) {
        String rpyPath = yamlDataMaService.repositoryAddress();
        //裸仓库地址
        String address = RepositoryUtil.findRepositoryAddress(rpyPath, repositoryFile.getRepositoryId());

        //临时文件夹
        String temporaryPath = RepositoryUtil.getRpyTemporaryPath(rpyPath, repositoryFile.getRepositoryId());

        File file = new File(temporaryPath);
        try {
            if (file.exists()){
                FileUtils.deleteDirectory(file);
            }
            //clone 仓库
            GitUntil.cloneRepositoryByFile(address,temporaryPath,repositoryFile.getBranch());

            File repoDir = new File(temporaryPath);
            Git git = Git.open(repoDir);

            String filePath = repositoryFile.getFilePath();
            if (repositoryFile.getFilePath().startsWith("/")){
                 filePath = filePath.substring(1);
            }
            git.rm()
                    .addFilepattern(filePath)
                    .call();

            // 将新文件添加到 Git 暂存区
            git.add().addFilepattern(".").call();
            git.commit().setMessage(repositoryFile.getCommitMessage()).call();
            //推送到裸仓库
            GitUntil. pushAllBranchRepository(temporaryPath,address);

            //删除临时文件数据
            FileUtils.deleteDirectory(new File(temporaryPath));

        }catch (Exception e){
            throw new SystemException(e);
        }

    }

    @Override
    public void updateBareFile(RepositoryFile repositoryFile) {
        String rpyPath = yamlDataMaService.repositoryAddress();
        //裸仓库地址
        String address = RepositoryUtil.findRepositoryAddress(rpyPath, repositoryFile.getRepositoryId());

        //临时文件夹
        String temporaryPath = RepositoryUtil.getRpyTemporaryPath(rpyPath, repositoryFile.getRepositoryId());

        File file = new File(temporaryPath);
        try {
            if (file.exists()) {
                FileUtils.deleteDirectory(file);
            }
            System.out.println("开始："+System.currentTimeMillis());
            //clone 仓库
            GitUntil.cloneRepositoryByFile(address, temporaryPath, repositoryFile.getBranch());
            System.out.println("结束："+System.currentTimeMillis());
            File repoDir = new File(temporaryPath);
            Git git = Git.open(repoDir);

            Repository repository = git.getRepository();

            String filePath = temporaryPath + repositoryFile.getFilePath();
            FileUtils.writeStringToFile(new File(filePath),repositoryFile.getFileData(), StandardCharsets.UTF_8);

            // 将新文件添加到 Git 暂存区
            git.add().addFilepattern(".").call();
            git.commit().setMessage(repositoryFile.getCommitMessage()).call();
            //推送到裸仓库
            GitUntil. pushAllBranchRepository(temporaryPath,address);

            //删除临时文件数据
            FileUtils.deleteDirectory(new File(temporaryPath));

        }catch (Exception e){
            throw new SystemException(e);
        }
    }

    /**
     * 创建文件
     * @param fileQuery 文件信息
     */
    @Override
    public void createFile(FileQuery fileQuery) {

    }

    /**
     * 删除文件
     * @param fileQuery 文件信息
     */
    @Override
    public void deleteFile(FileQuery fileQuery) {

    }

    /**
     * 读取文件
     * @param fileQuery 文件信息
     * @return 文件信息
     */
    @Override
    public FileMessage readFile(FileQuery fileQuery) {
        String rpyId = fileQuery.getRpyId();
        String fileAddress = fileQuery.getFileAddress();
        String branch = fileQuery.getCommitBranch();

        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),rpyId);
        FileMessage fileMessage ;
        try {
            Git git = Git.open(new java.io.File(repositoryAddress));
            org.eclipse.jgit.lib.Repository repository = git.getRepository();
            boolean findCommitId = fileQuery.isFindCommitId();
            if (fileAddress.startsWith("/")){
                 fileAddress = fileAddress.substring(1);
            }
            fileMessage =  RepositoryFileUtil.readBranchFile(repository, branch, fileAddress, findCommitId);
            git.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileMessage;
    }

    /**
     * 写入文件
     * @param repositoryFileQuery 文件信息
     */
    @Override
    public void writeFile(FileQuery repositoryFileQuery) {



    }

}






















































