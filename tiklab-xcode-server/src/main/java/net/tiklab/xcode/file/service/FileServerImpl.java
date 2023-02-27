package net.tiklab.xcode.file.service;

import net.tiklab.xcode.file.model.FileMessage;
import net.tiklab.xcode.file.model.FileQuery;
import net.tiklab.xcode.repository.model.Repository;
import net.tiklab.xcode.repository.service.RepositoryServer;
import net.tiklab.xcode.util.RepositoryFileUtil;
import net.tiklab.xcode.util.RepositoryUtil;
import org.eclipse.jgit.api.Git;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
        String rpyId = fileQuery.getRpyId();
        String fileAddress = fileQuery.getFileAddress();
        String branch = fileQuery.getCommitBranch();

        Repository code = repositoryServer.findOneRpy(rpyId);
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(code);
        FileMessage fileMessage ;
        try {
            Git git = Git.open(new java.io.File(repositoryAddress));
            org.eclipse.jgit.lib.Repository repository = git.getRepository();
            boolean findCommitId = fileQuery.isFindCommitId();
            String substring = fileAddress.substring(1);
            fileMessage =  RepositoryFileUtil.readBranchFile(repository, branch, substring, findCommitId);
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






















































