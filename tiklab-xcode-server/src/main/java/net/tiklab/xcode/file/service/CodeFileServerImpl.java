package net.tiklab.xcode.file.service;

import net.tiklab.xcode.code.model.Code;
import net.tiklab.xcode.code.service.CodeServer;
import net.tiklab.xcode.file.model.CodeFile;
import net.tiklab.xcode.file.model.CodeFileMessage;
import net.tiklab.xcode.until.CodeFileUntil;
import net.tiklab.xcode.until.CodeUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

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
     * @param codeId      仓库id
     * @param fileAddress 文件地址
     * @return 文件信息
     */
    @Override
    public CodeFileMessage readFile(String codeId, String fileAddress) {
        Code code = codeServer.findOneCode(codeId);
        String repositoryAddress = CodeUntil.findRepositoryAddress(code.getAddress(), code.getCodeGroup());
        String s = repositoryAddress + fileAddress;
        CodeFileMessage fileMessage = new CodeFileMessage();
        String file = CodeFileUntil.readFile(s);
        //获取文件类型
        File file1 = new File(s);
        String suffix = null;
        String fileName = file1.getName();
        if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        }

        float fileSize = (float)file1.length() / 1024 ;
        String str = String.format("%.2f", fileSize)+"KB";
        fileMessage.setFileName(fileName);
        fileMessage.setFilePath(file1.getAbsolutePath());
        fileMessage.setFileType(suffix);
        fileMessage.setFileMessage(file);
        fileMessage.setFileSize(str);

        return fileMessage;
    }

    /**
     * 写入文件
     * @param file 文件信息
     */
    @Override
    public void writeFile(CodeFile file) {

    }










}






















































