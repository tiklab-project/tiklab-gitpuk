package net.tiklab.xcode.file.service;

import net.tiklab.core.exception.ApplicationException;
import net.tiklab.xcode.code.model.Code;
import net.tiklab.xcode.code.service.CodeServer;
import net.tiklab.xcode.file.model.CodeFile;
import net.tiklab.xcode.file.model.CodeFileMessage;
import net.tiklab.xcode.git.GitCommitUntil;
import net.tiklab.xcode.git.GitUntil;
import net.tiklab.xcode.until.CodeFileUntil;
import net.tiklab.xcode.until.CodeFinal;
import net.tiklab.xcode.until.CodeUntil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

@Service
public class CodeFileServerImpl implements  CodeFileServer {

    @Autowired
    CodeServer codeServer;


    /**
     * 创建文件
     * @param file 文件信息
     */
    @Override
    public void createFile(CodeFile file) {

    }

    /**
     * 删除文件
     * @param file 文件信息
     */
    @Override
    public void deleteFile(CodeFile file) {

    }

    /**
     * 读取文件
     * @param codeFile 文件信息
     * @return 文件信息
     */
    @Override
    public CodeFileMessage readFile(CodeFile codeFile) {
        String codeId = codeFile.getCodeId();
        String fileAddress = codeFile.getFileAddress();
        String branch = codeFile.getCommitBranch();

        Code code = codeServer.findOneCode(codeId);
        String repositoryAddress = CodeUntil.findRepositoryAddress(code, CodeFinal.TRUE);
        CodeFileMessage fileMessage = null;
        try {
            Git git = Git.open(new File(repositoryAddress));
            fileMessage =  CodeFileUntil.readBranchFile(git.getRepository(), branch, fileAddress.substring(1), codeFile.isFindCommitId());
            git.close();
            // float fileSize = (float)file1.length() / 1024 ;
            // String str = String.format("%.2f", fileSize)+"KB";
            // fileMessage.setFileSize(str);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileMessage;
    }

    /**
     * 写入文件
     * @param codeFile 文件信息
     */
    @Override
    public void writeFile(CodeFile codeFile) {
        String codeId = codeFile.getCodeId();
        Code code = codeServer.findOneCode(codeId);

        String fileAddress = codeFile.getFileAddress();

        String repositoryAddress = CodeUntil.findRepositoryAddress(code,CodeFinal.FALSE);

        File file = new File(repositoryAddress + fileAddress);
        String newFileName = codeFile.getNewFileName();
        String oldFileName = codeFile.getOldFileName();

        try {
            //判断文件名称是否更改
            if (!oldFileName.equals(newFileName)){
                boolean b = CodeFileUntil.updateFileName(file.getParent(), newFileName, oldFileName);
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
        String fileContent = codeFile.getFileContent();
        CodeFileUntil.writeFile(fileContent,file.getParent()+"/"+newFileName);

        try {
            GitUntil.repositoryCommit(repositoryAddress,codeFile.getCommitBranch(),
                    codeFile.getCommitMessage(), newFileName);
        } catch (IOException | GitAPIException | URISyntaxException e) {
            throw new ApplicationException("提交失败："+e);
        }


    }










}






















































