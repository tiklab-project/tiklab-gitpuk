package net.tiklab.xcode.repository.service;

import net.tiklab.beans.BeanMapper;
import net.tiklab.core.exception.ApplicationException;
import net.tiklab.join.JoinTemplate;
import net.tiklab.rpc.annotation.Exporter;
import net.tiklab.user.user.model.User;
import net.tiklab.user.user.service.UserService;
import net.tiklab.utils.context.LoginContext;
import net.tiklab.xcode.branch.model.Branch;
import net.tiklab.xcode.repository.dao.RepositoryDao;
import net.tiklab.xcode.repository.entity.RepositoryEntity;
import net.tiklab.xcode.repository.model.Repository;
import net.tiklab.xcode.repository.model.RepositoryCloneAddress;
import net.tiklab.xcode.repository.model.RepositoryGroup;
import net.tiklab.xcode.file.model.FileTreeMessage;
import net.tiklab.xcode.git.GitBranchUntil;
import net.tiklab.xcode.git.GitCommitUntil;
import net.tiklab.xcode.git.GitUntil;
import net.tiklab.xcode.until.RepositoryUntilFileUntil;
import net.tiklab.xcode.until.RepositoryUntil;
import net.tiklab.xcode.file.model.FileTree;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Exporter
public class RepositoryServerImpl implements RepositoryServer {

    @Autowired
    private JoinTemplate joinTemplate;

    @Autowired
    private RepositoryDao repositoryDao;


    /**
     * 创建仓库
     * @param repository 信息
     * @return 仓库id
     */
    @Override
    public String createCode(Repository repository) {
        Repository initRepository = initCode(repository);
        initRepository.setCreateTime(RepositoryUntil.date(1,new Date()));
        RepositoryEntity groupEntity = BeanMapper.map(initRepository, RepositoryEntity.class);
        return repositoryDao.createCode(groupEntity);
    }

    /**
     * 删除仓库
     * @param codeId 仓库id
     */
    @Override
    public void deleteCode(String codeId) {
        Repository repository = findOneCode(codeId);
        repositoryDao.deleteCode(codeId);
        String repositoryAddress = RepositoryUntil.findRepositoryAddress(repository);
        File file = new File(repositoryAddress);
        if (!file.exists()){
            return;
        }
        RepositoryUntilFileUntil.deleteFile(file);
    }

    /**
     * 更新仓库
     * @param repository 仓库信息
     */
    @Override
    public void updateCode(Repository repository) {
        RepositoryEntity groupEntity = BeanMapper.map(repository, RepositoryEntity.class);
        repositoryDao.updateCode(groupEntity);
    }

    /**
     * 查询单个仓库
     * @param codeId 仓库id
     * @return 仓库信息
     */
    @Override
    public Repository findOneCode(String codeId) {
        RepositoryEntity groupEntity = repositoryDao.findOneCode(codeId);
        Repository repository = BeanMapper.map(groupEntity, Repository.class);
        joinTemplate.joinQuery(repository);
        return repository;
    }

    /**
     * 查询用户仓库
     * @param userId 用户id
     * @return 用户仓库
     */
    @Override
    public List<Repository> findUserCode(String userId) {
        List<Repository> allRepositories = findAllCode();
        if (allRepositories == null || allRepositories.size() == 0){
            return Collections.emptyList();
        }
        return allRepositories;
    }

    /**
     * 查询所有仓库
     * @return 仓库信息列表
     */
    @Override
    public List<Repository> findAllCode() {
        List<RepositoryEntity> groupEntityList = repositoryDao.findAllCode();
        List<Repository> list = BeanMapper.mapList(groupEntityList, Repository.class);
        joinTemplate.joinQuery(list);
        if (list == null){
            return Collections.emptyList();
        }
        return list;
    }


    @Override
    public List<Repository> findAllCodeList(List<String> idList) {
        List<RepositoryEntity> groupEntities = repositoryDao.findAllCodeList(idList);
        List<Repository> list = BeanMapper.mapList(groupEntities, Repository.class);
        joinTemplate.joinQuery(list);
        return list;
    }

    /**
     * 根据仓库名称查询仓库信息
     * @param codeName 仓库名称
     * @return 仓库信息
     */
    @Override
    public Repository findNameCode(String codeName) {
        String loginId = LoginContext.getLoginId();
        List<Repository> userRepositories = findUserCode(loginId);
        if (userRepositories.size() == 0 ){
            return null;
        }
        try {
            for (Repository repository : userRepositories) {
                RepositoryGroup repositoryGroup = repository.getGroup();
                if (repositoryGroup == null || repositoryGroup.getName() == null){
                    String nickname = repository.getUser().getName();
                    codeName = codeName.replace(nickname+"/","");
                }
                String address = repository.getAddress();

                if (!address.equals(codeName)){
                    continue;
                }
                String repositoryAddress = RepositoryUntil.findRepositoryAddress(repository);
                String defaultBranch = GitBranchUntil.findDefaultBranch(repositoryAddress);
                boolean isNotNull = GitCommitUntil.findRepositoryIsNotNull(repositoryAddress, defaultBranch);
                repository.setDefaultBranch(defaultBranch);
                repository.setNotNull(isNotNull);
                return repository;
            }
        } catch (IOException e) {
            throw new ApplicationException("仓库不存在"+e);
        }
        return null;
    }


    /**
     * 初始化仓库文件信息
     * @param repository 仓库信息
     * @return 信息
     * @throws ApplicationException 初始化失败
     */
    private Repository initCode(Repository repository) throws ApplicationException {
        joinTemplate.joinQuery(repository);
        //获取用户名
        RepositoryGroup repositoryGroup = repository.getGroup();
        //不存在仓库组
        if (repositoryGroup != null && repositoryGroup.getGroupId() != null){
            String groupAddress = repositoryGroup.getAddress();
            String s = RepositoryUntil.defaultPath() + "/" + groupAddress;
            //创建工作目录
            File file = new File(s);
            if (!file.exists()){
                RepositoryUntilFileUntil.createDirectory(file.getAbsolutePath());
            }
        }
        String repositoryAddress = RepositoryUntil.findRepositoryAddress(repository);
        GitUntil.createRepository(repositoryAddress);
        return repository;
    }

    /**
     * 获取文件信息
     * @param message 信息
     * @return 文件集合
     */
    @Override
    public List<FileTree> findFileTree(FileTreeMessage message){

        Repository repository = findOneCode(message.getCodeId());

        String repositoryAddress = RepositoryUntil.findRepositoryAddress(repository) ;

        List<FileTree> fileTrees ;
        try {
            List<Branch> allBranch = GitBranchUntil.findAllBranch(repositoryAddress);
            if (allBranch.isEmpty()){
                return null;
            }
            File file = new File(repositoryAddress);
            Git git = Git.open(file);
            fileTrees = RepositoryUntilFileUntil.findFileTree(git,message);
            git.close();
        } catch (IOException e) {
            throw new ApplicationException( "仓库信息获取失败：" + e);
        } catch (GitAPIException e) {
            throw new ApplicationException( "提交信息获取失败：" + e);
        }
        return fileTrees;
    }

    @Value("${server.port:8080}")
    private String port;

    @Value("${xcode.ssh.port:8082}")
    private int sshPort;

    @Autowired
    private UserService userService;

    /**
     * 获取克隆地址
     * @param codeId 仓库id
     * @return 地址信息
     */
    @Override
    public RepositoryCloneAddress findCloneAddress(String codeId){
        Repository repository = findOneCode(codeId);
        String path = repository.getAddress();
        String  ip ;
        //获取本机地址
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            ip = "172.0.0.1";
        }
        RepositoryCloneAddress repositoryCloneAddress = new RepositoryCloneAddress();
        String repositoryAddress = RepositoryUntil.findRepositoryAddress(repository);
        repositoryCloneAddress.setFileAddress(repositoryAddress);
        // String username = System.getProperty("user.name");
        String loginId = LoginContext.getLoginId();
        User user = userService.findOne(loginId);
        String http = "http://" + ip + ":" + port + "/xcode/"+ path + ".git";
        String SSH = "ssh://"+ user.getName() +"@"+ip + ":" + sshPort +"/" + path + ".git";
        repositoryCloneAddress.setHttpAddress(http);
        repositoryCloneAddress.setSSHAddress(SSH);
        return repositoryCloneAddress;

    }









    
    
}










































