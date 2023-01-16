package net.tiklab.xcode.code.service;

import net.tiklab.beans.BeanMapper;
import net.tiklab.core.exception.ApplicationException;
import net.tiklab.join.JoinTemplate;
import net.tiklab.rpc.annotation.Exporter;
import net.tiklab.xcode.code.dao.CodeDao;
import net.tiklab.xcode.code.entity.CodeEntity;
import net.tiklab.xcode.code.model.Code;
import net.tiklab.xcode.code.model.CodeGroup;
import net.tiklab.xcode.code.model.CodeMessage;
import net.tiklab.xcode.git.GitUntil;
import net.tiklab.xcode.until.CodeUntil;
import net.tiklab.xcode.until.FileTree;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
        CodeEntity groupEntity = BeanMapper.map(initCode, CodeEntity.class);
        return codeDao.createCode(groupEntity);
    }

    /**
     * 删除仓库
     * @param codeId 仓库id
     */
    @Override
    public void deleteCode(String codeId) {
        codeDao.deleteCode(codeId);
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
        return findAllCode();
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
        return list;
    }


    @Override
    public List<Code> findAllCodeList(List<String> idList) {
        List<CodeEntity> groupEntities = codeDao.findAllCodeList(idList);
        List<Code> list = BeanMapper.mapList(groupEntities, Code.class);
        joinTemplate.joinQuery(list);
        return list;
    }


    //初始化仓库信息
    private Code initCode(Code code) throws ApplicationException{
        joinTemplate.joinQuery(code);

        CodeGroup codeGroup = code.getCodeGroup();
        String s  = CodeUntil.defaultPath() ;
        //是否存在仓库组
        if (codeGroup != null){
            String groupName = codeGroup.getName();
            code.setName(groupName+"/"+ code.getName());
            s = CodeUntil.defaultPath() + "/" + groupName;
            //创建工作目录
            File file = new File(s);
            try {
                CodeUntil.createDirectory(file.getAbsolutePath());
            } catch (ApplicationException e) {
                throw new ApplicationException(e);
            }
        }

        GitUntil.createRepository(s,code.getName());
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
        String s = CodeUntil.defaultPath() + "/" + name;

        CodeGroup codeGroup = code.getCodeGroup();
        if (codeGroup != null){
            s =  CodeUntil.defaultPath() + "/" + codeGroup.getName() + "/" + name;
        }
        List<FileTree> fileTrees = null;

        codeMessage.setRepositoryAddress(s);
        try {
            fileTrees = GitUntil.fileTree(codeMessage,name);
        } catch (GitAPIException | IOException e) {
            throw new ApplicationException("仓库信息获取失败："+e);
        }
        return fileTrees;

    }


    
    
}










































