package net.tiklab.xcode.code.service;

import net.tiklab.beans.BeanMapper;
import net.tiklab.core.exception.ApplicationException;
import net.tiklab.join.JoinTemplate;
import net.tiklab.rpc.annotation.Exporter;
import net.tiklab.user.user.model.User;
import net.tiklab.user.user.service.UserService;
import net.tiklab.utils.context.LoginContext;
import net.tiklab.xcode.branch.model.CodeBranch;
import net.tiklab.xcode.code.dao.CodeDao;
import net.tiklab.xcode.code.entity.CodeEntity;
import net.tiklab.xcode.code.model.Code;
import net.tiklab.xcode.code.model.CodeCloneAddress;
import net.tiklab.xcode.code.model.CodeGroup;
import net.tiklab.xcode.code.model.CodeMessage;
import net.tiklab.xcode.git.GitBranchUntil;
import net.tiklab.xcode.git.GitCommitUntil;
import net.tiklab.xcode.git.GitUntil;
import net.tiklab.xcode.until.CodeFileUntil;
import net.tiklab.xcode.until.CodeFinal;
import net.tiklab.xcode.until.CodeUntil;
import net.tiklab.xcode.until.FileTree;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Exporter
public class CodeServerImpl implements CodeServer{

    @Autowired
    private JoinTemplate joinTemplate;

    @Autowired
    private CodeDao codeDao;


    /**
     * 创建仓库
     * @param code 信息
     * @return 仓库id
     */
    @Override
    public String createCode(Code code) {
        Code initCode = initCode(code);
        initCode.setCreateTime(CodeUntil.date(1,new Date()));
        CodeEntity groupEntity = BeanMapper.map(initCode, CodeEntity.class);
        return codeDao.createCode(groupEntity);
    }

    /**
     * 删除仓库
     * @param codeId 仓库id
     */
    @Override
    public void deleteCode(String codeId) {
        Code code = findOneCode(codeId);
        codeDao.deleteCode(codeId);
        String repositoryAddress = CodeUntil.findRepositoryAddress(code,CodeFinal.FALSE);
        try {
            List<CodeBranch> allBranch = GitBranchUntil.findAllBranch(repositoryAddress + ".git");
            if (allBranch.isEmpty()){
                File file = new File(repositoryAddress+".git");
                CodeFileUntil.deleteFile(file);
                return;
            }
            //删除源文件信息
            for (CodeBranch branch : allBranch) {
                String branchName = branch.getBranchName();
                File file = new File(repositoryAddress + "_" + branchName);
                if (!file.exists()){
                    continue;
                }
                CodeFileUntil.deleteFile(file);
            }
            File file = new File(repositoryAddress+".git");
            CodeFileUntil.deleteFile(file);
        } catch (IOException e) {
            throw new ApplicationException("仓库不存在："+e);
        }
    }

    /**
     * 更新仓库
     * @param code 仓库信息
     */
    @Override
    public void updateCode(Code code) {
        CodeEntity groupEntity = BeanMapper.map(code, CodeEntity.class);
        codeDao.updateCode(groupEntity);
    }

    /**
     * 查询单个仓库
     * @param codeId 仓库id
     * @return 仓库信息
     */
    @Override
    public Code findOneCode(String codeId) {
        CodeEntity groupEntity = codeDao.findOneCode(codeId);
        Code code = BeanMapper.map(groupEntity, Code.class);
        joinTemplate.joinQuery(code);
        return code;
    }

    /**
     * 查询用户仓库
     * @param userId 用户id
     * @return 用户仓库
     */
    @Override
    public List<Code> findUserCode(String userId) {
        List<Code> allCode = findAllCode();
        if (allCode == null || allCode.size() == 0){
            return Collections.emptyList();
        }
        return allCode;
    }

    /**
     * 查询所有仓库
     * @return 仓库信息列表
     */
    @Override
    public List<Code> findAllCode() {
        List<CodeEntity> groupEntityList = codeDao.findAllCode();
        List<Code> list = BeanMapper.mapList(groupEntityList, Code.class);
        joinTemplate.joinQuery(list);
        if (list == null){
            return Collections.emptyList();
        }
        return list;
    }


    @Override
    public List<Code> findAllCodeList(List<String> idList) {
        List<CodeEntity> groupEntities = codeDao.findAllCodeList(idList);
        List<Code> list = BeanMapper.mapList(groupEntities, Code.class);
        joinTemplate.joinQuery(list);
        return list;
    }

    /**
     * 根据仓库名称查询仓库信息
     * @param codeName 仓库名称
     * @return 仓库信息
     */
    @Override
    public Code findNameCode(String codeName) {
        String loginId = LoginContext.getLoginId();
        List<Code> userCode = findUserCode(loginId);
        if (userCode.size() == 0 ){
            return null;
        }
        try {
            for (Code code : userCode) {
                CodeGroup codeGroup = code.getCodeGroup();
                if (codeGroup == null || codeGroup.getName() == null){
                    String nickname = code.getUser().getName();
                    codeName = codeName.replace(nickname+"/","");
                }
                String address = code.getAddress();

                if (!address.equals(codeName)){
                    continue;
                }
                String repositoryAddress = CodeUntil.findRepositoryAddress(code, CodeFinal.TRUE);
                String defaultBranch = GitBranchUntil.findDefaultBranch(repositoryAddress);
                boolean isNotNull = GitCommitUntil.findRepositoryIsNotNull(repositoryAddress, defaultBranch);
                code.setDefaultBranch(defaultBranch);
                code.setNotNull(isNotNull);
                return code;
            }
        } catch (IOException e) {
            throw new ApplicationException("仓库不存在"+e);
        }
        return null;
    }


    /**
     * 初始化仓库文件信息
     * @param code 仓库信息
     * @return 信息
     * @throws ApplicationException 初始化失败
     */
    private Code initCode(Code code) throws ApplicationException {

        joinTemplate.joinQuery(code);
        //获取用户名
        CodeGroup codeGroup = code.getCodeGroup();
        //不存在仓库组
        if (codeGroup != null && codeGroup.getGroupId() != null){
            String groupName = codeGroup.getName();
            String s = CodeUntil.defaultPath() + "/" + groupName;
            //创建工作目录
            CodeFileUntil.createDirectory(new File(s).getAbsolutePath());
            code.setAddress(groupName+"/"+code.getAddress());
        }
        GitUntil.createRepository(CodeUntil.defaultPath(),code.getAddress());
        return code;
    }

    /**
     * 获取文件信息
     * @param codeMessage 信息
     * @return 文件集合
     */
    @Override
    public List<FileTree> findFileTree(CodeMessage codeMessage){

        Code code = findOneCode(codeMessage.getCodeId());
        String name = code.getName();

        String repositoryAddress = CodeUntil.findRepositoryAddress(code, CodeFinal.FALSE) ;

        List<FileTree> fileTrees ;

        codeMessage.setRepositoryAddress(repositoryAddress);
        codeMessage.setRepositoryName(name);
        codeMessage.setAddress(code.getAddress());
        try {
            fileTrees = CodeFileUntil.fileTree(codeMessage);
        } catch (GitAPIException | IOException e) {
            throw new ApplicationException( "仓库信息获取失败：" + e);
        }
        return fileTrees;

    }


    @Value("${server.port:8080}")
    private String port;

    @Value("${xcode.ssh.port:8082}")
    private int sshPort;


    /**
     * 获取克隆地址
     * @param codeId 仓库id
     * @return 地址信息
     */
    @Override
    public CodeCloneAddress findCloneAddress(String codeId){
        Code code = findOneCode(codeId);

        String path = code.getAddress();

        String  ip ;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            ip = "172.0.0.1";
        }

        CodeCloneAddress codeCloneAddress = new CodeCloneAddress();
        String repositoryAddress = CodeUntil.findRepositoryAddress(code,CodeFinal.TRUE);
        codeCloneAddress.setFileAddress(repositoryAddress);

        String http = "http://" + ip + ":" + port + "/xcode/"+ path + ".git";

        String SSH = "ssh://"+ip + ":" + sshPort +"/" + path + ".git";

        codeCloneAddress.setHttpAddress(http);
        codeCloneAddress.setSSHAddress(SSH);

        return codeCloneAddress;

    }









    
    
}










































