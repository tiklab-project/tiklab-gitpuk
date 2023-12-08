package io.thoughtware.gittork.file.service;

import io.thoughtware.gittork.common.RepositoryFileUtil;
import io.thoughtware.gittork.common.RepositoryUtil;
import io.thoughtware.gittork.common.GitTorkYamlDataMaService;
import io.thoughtware.gittork.file.model.FileMessage;
import io.thoughtware.gittork.file.model.FileQuery;
import io.thoughtware.gittork.repository.service.RepositoryServer;
import org.eclipse.jgit.api.Git;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class FileServerImpl implements FileServer {

    @Autowired
    RepositoryServer repositoryServer;

    @Autowired
    GitTorkYamlDataMaService yamlDataMaService;

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






















































