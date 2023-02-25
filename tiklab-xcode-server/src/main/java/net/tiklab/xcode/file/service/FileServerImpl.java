package net.tiklab.xcode.file.service;

import net.tiklab.core.exception.ApplicationException;
import net.tiklab.xcode.file.model.FileQuery;
import net.tiklab.xcode.repository.model.Repository;
import net.tiklab.xcode.repository.service.RepositoryServer;
import net.tiklab.xcode.file.model.FileMessage;
import net.tiklab.xcode.git.GitUntil;
import net.tiklab.xcode.until.RepositoryUntilFileUntil;
import net.tiklab.xcode.until.RepositoryUntil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class FileServerImpl implements FileServer {

    @Autowired
    RepositoryServer repositoryServer;


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
        String codeId = fileQuery.getCodeId();
        String fileAddress = fileQuery.getFileAddress();
        String branch = fileQuery.getCommitBranch();

        Repository code = repositoryServer.findOneCode(codeId);
        String repositoryAddress = RepositoryUntil.findRepositoryAddress(code);
        FileMessage fileMessage ;
        try {
            Git git = Git.open(new java.io.File(repositoryAddress));
            org.eclipse.jgit.lib.Repository repository = git.getRepository();
            boolean findCommitId = fileQuery.isFindCommitId();
            String substring = fileAddress.substring(1);
            fileMessage =  RepositoryUntilFileUntil.readBranchFile(repository, branch, substring, findCommitId);
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
        String codeId = repositoryFileQuery.getCodeId();
        Repository repository = repositoryServer.findOneCode(codeId);

        String fileAddress = repositoryFileQuery.getFileAddress();

        String repositoryAddress = RepositoryUntil.findRepositoryAddress(repository);

        java.io.File file = new java.io.File(repositoryAddress + fileAddress);
        String newFileName = repositoryFileQuery.getNewFileName();
        String oldFileName = repositoryFileQuery.getOldFileName();

        try {
            //判断文件名称是否更改
            if (!oldFileName.equals(newFileName)){
                boolean b = RepositoryUntilFileUntil.updateFileName(file.getParent(), newFileName, oldFileName);
                if (!b){
                    throw new ApplicationException("文件名称更改失败");
                }
                //在储存库中删除文件
                GitUntil.deleteRepositoryFile(repositoryAddress, oldFileName);
            }

        } catch (IOException | GitAPIException | URISyntaxException e) {
            throw new ApplicationException("仓库删除文件失败"+e);
        }

        //写入文件信息
        String fileContent = repositoryFileQuery.getFileContent();
        RepositoryUntilFileUntil.writeFile(fileContent,file.getParent()+"/"+newFileName);

        try {
            GitUntil.repositoryCommit(repositoryAddress, repositoryFileQuery.getCommitBranch(),
                    repositoryFileQuery.getCommitMessage(), newFileName);
        } catch (IOException | GitAPIException | URISyntaxException e) {
            throw new ApplicationException("提交失败："+e);
        }


    }










}






















































