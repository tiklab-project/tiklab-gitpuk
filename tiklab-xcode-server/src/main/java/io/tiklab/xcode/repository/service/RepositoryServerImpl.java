package io.tiklab.xcode.repository.service;

import io.tiklab.beans.BeanMapper;
import io.tiklab.core.exception.ApplicationException;
import io.tiklab.eam.common.context.LoginContext;
import io.tiklab.join.JoinTemplate;
import io.tiklab.rpc.annotation.Exporter;
import io.tiklab.user.user.model.User;
import io.tiklab.user.user.service.UserService;
import io.tiklab.xcode.branch.model.Branch;
import io.tiklab.xcode.file.model.FileTree;
import io.tiklab.xcode.file.model.FileTreeMessage;
import io.tiklab.xcode.git.GitBranchUntil;
import io.tiklab.xcode.git.GitUntil;
import io.tiklab.xcode.repository.dao.RepositoryDao;
import io.tiklab.xcode.repository.entity.RepositoryEntity;
import io.tiklab.xcode.repository.model.Repository;
import io.tiklab.xcode.repository.model.RepositoryCloneAddress;
import io.tiklab.xcode.repository.model.RepositoryGroup;
import io.tiklab.xcode.util.RepositoryFileUtil;
import io.tiklab.xcode.util.RepositoryFinal;
import io.tiklab.xcode.util.RepositoryUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
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


    @Value("${xcode.file:/file}")
    private String fileAddress;

    @Value("${server.port:8080}")
    private String port;

    @Value("${xrpy.ssh.port:8082}")
    private int sshPort;

    @Autowired
    private UserService userService;

    /**
     * 创建仓库
     * @param repository 信息
     * @return 仓库id
     */
    @Override
    public String createRpy(Repository repository) {

        joinTemplate.joinQuery(repository);
        //获取用户名
        RepositoryGroup repositoryGroup = repository.getGroup();
        //存在仓库组
        if (repositoryGroup != null && repositoryGroup.getGroupId() != null){
            String groupAddress = repositoryGroup.getAddress();
            String s = RepositoryUtil.defaultPath() + "/" + groupAddress;
            //创建工作目录
            File file = new File(s);
            if (!file.exists()){
                RepositoryFileUtil.createDirectory(file.getAbsolutePath());
            }
        }

        String repositoryAddress = RepositoryUtil.findRepositoryAddress(repository);

        String property = System.getProperty("user.dir");
        if (!property.endsWith(RepositoryFinal.APP_NAME)){
            File file = new File(property);
            property= file.getParent();
        }

        Path filePath = Paths.get(property+"/"+fileAddress);

        String ignoreFilePath = filePath+"/"+".gitignore";
        String mdFilePath = filePath+"/"+"README.md";

        GitUntil.createRepository(repositoryAddress,ignoreFilePath,mdFilePath,repository.getUser());


        repository.setCreateTime(RepositoryUtil.date(1,new Date()));
        RepositoryEntity groupEntity = BeanMapper.map(repository, RepositoryEntity.class);
        return repositoryDao.createRpy(groupEntity);
    }

    /**
     * 删除仓库
     * @param rpyId 仓库id
     */
    @Override
    public void deleteRpy(String rpyId) {
        Repository repository = findOneRpy(rpyId);
        repositoryDao.deleteRpy(rpyId);
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(repository);
        File file = new File(repositoryAddress);
        if (!file.exists()){
            return;
        }
        RepositoryFileUtil.deleteFile(file);
    }

    /**
     * 更新仓库
     * @param repository 仓库信息
     */
    @Override
    public void updateRpy(Repository repository) {
        RepositoryEntity groupEntity = BeanMapper.map(repository, RepositoryEntity.class);
        repositoryDao.updateRpy(groupEntity);
    }

    /**
     * 查询单个仓库
     * @param rpyId 仓库id
     * @return 仓库信息
     */
    @Override
    public Repository findOneRpy(String rpyId) {
        RepositoryEntity groupEntity = repositoryDao.findOneRpy(rpyId);
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
    public List<Repository> findUserRpy(String userId) {
        List<Repository> allRepositories = findAllRpy();
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
    public List<Repository> findAllRpy() {
        List<RepositoryEntity> groupEntityList = repositoryDao.findAllRpy();
        List<Repository> list = BeanMapper.mapList(groupEntityList, Repository.class);
        joinTemplate.joinQuery(list);
        if (list == null){
            return Collections.emptyList();
        }
        return list;
    }


    @Override
    public List<Repository> findAllRpyList(List<String> idList) {
        List<RepositoryEntity> groupEntities = repositoryDao.findAllRpyList(idList);
        List<Repository> list = BeanMapper.mapList(groupEntities, Repository.class);
        joinTemplate.joinQuery(list);
        return list;
    }

    /**
     * 根据仓库名称查询仓库信息
     * @param rpyName 仓库名称
     * @return 仓库信息
     */
    @Override
    public Repository findNameRpy(String rpyName) {
        String loginId = LoginContext.getLoginId();
        List<Repository> userRepositories = findUserRpy(loginId);
        if (userRepositories.size() == 0 ){
            return null;
        }

        Repository repository = null;

        for (Repository rpy : userRepositories) {
            RepositoryGroup repositoryGroup = rpy.getGroup();
            if (repositoryGroup == null || repositoryGroup.getName() == null) {
                String nickname = rpy.getUser().getName();
                rpyName = rpyName.replace(nickname + "/", "");
            }
            if (rpy.getAddress().contains(rpyName)){
                repository = rpy;
                break;
            }
        }

        if (repository == null){
            throw new ApplicationException("仓库"+rpyName+"不存在...");
        }

        try {
            String repositoryAddress = RepositoryUtil.findRepositoryAddress(repository);
            File file = new File(repositoryAddress);
            Git git = Git.open(file);
            org.eclipse.jgit.lib.Repository repository1 = git.getRepository();
            String fullBranch = repository1.getFullBranch().replace(Constants.R_HEADS,"");
            repository.setDefaultBranch(fullBranch);
            repository.setNotNull(fullBranch.isEmpty());
            git.close();
            return repository;
        } catch (IOException e) {
            throw new ApplicationException("仓库不存在"+e);
        }

    }

    /**
     * 获取文件信息
     * @param message 信息
     * @return 文件集合
     */
    @Override
    public List<FileTree> findFileTree(FileTreeMessage message){

        Repository repository = findOneRpy(message.getRpyId());

        String repositoryAddress = RepositoryUtil.findRepositoryAddress(repository) ;

        List<FileTree> fileTrees ;
        try {
            List<Branch> allBranch = GitBranchUntil.findAllBranch(repositoryAddress);
            if (allBranch.isEmpty()){
                return null;
            }
            File file = new File(repositoryAddress);
            Git git = Git.open(file);
            fileTrees = RepositoryFileUtil.findFileTree(git,message);
            git.close();
        } catch (IOException e) {
            throw new ApplicationException( "仓库信息获取失败：" + e);
        } catch (GitAPIException e) {
            throw new ApplicationException( "提交信息获取失败：" + e);
        }
        return fileTrees;
    }



    /**
     * 获取克隆地址
     * @param rpyId 仓库id
     * @return 地址信息
     */
    @Override
    public RepositoryCloneAddress findCloneAddress(String rpyId){
        Repository repository = findOneRpy(rpyId);
        String path = repository.getAddress();
        String  ip ;
        //获取本机地址
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            ip = "172.0.0.1";
        }
        RepositoryCloneAddress repositoryCloneAddress = new RepositoryCloneAddress();
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(repository);
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










































