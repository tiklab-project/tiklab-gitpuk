package io.tiklab.xcode.repository.service;

import io.tiklab.beans.BeanMapper;
import io.tiklab.core.exception.ApplicationException;
import io.tiklab.core.page.Pagination;
import io.tiklab.core.page.PaginationBuilder;
import io.tiklab.eam.common.context.LoginContext;
import io.tiklab.join.JoinTemplate;
import io.tiklab.privilege.dmRole.service.DmRoleService;
import io.tiklab.rpc.annotation.Exporter;
import io.tiklab.user.dmUser.model.DmUser;
import io.tiklab.user.dmUser.model.DmUserQuery;
import io.tiklab.user.dmUser.service.DmUserService;
import io.tiklab.user.user.model.User;
import io.tiklab.user.user.service.UserService;
import io.tiklab.xcode.branch.model.Branch;
import io.tiklab.xcode.branch.service.BranchServer;
import io.tiklab.xcode.file.model.FileTree;
import io.tiklab.xcode.file.model.FileTreeMessage;
import io.tiklab.xcode.git.GitBranchUntil;
import io.tiklab.xcode.git.GitUntil;
import io.tiklab.xcode.repository.dao.RepositoryDao;
import io.tiklab.xcode.repository.entity.RepositoryEntity;
import io.tiklab.xcode.repository.model.*;
import io.tiklab.xcode.setting.service.BackupsServerImpl;
import io.tiklab.xcode.util.RepositoryFileUtil;
import io.tiklab.xcode.util.RepositoryUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Exporter
public class RepositoryServerImpl implements RepositoryServer {

    private static Logger logger = LoggerFactory.getLogger(RepositoryServerImpl.class);

    @Autowired
    private JoinTemplate joinTemplate;

    @Autowired
    private RepositoryDao repositoryDao;

    @Autowired
    private DmRoleService dmRoleService;

    @Autowired
    private DmUserService dmUserService;

    @Autowired
    private RecordOpenService recordOpenService;

    @Autowired
    private RecordCommitService recordCommitService;

    @Autowired
    private BranchServer branchServer;

    @Autowired
    private MemoryManService memoryManService;

    @Value("${xcode.file:/file}")
    private String fileAddress;

    @Value("${server.port:8080}")
    private String port;

    @Value("${xcode.ssh.port:8082}")
    private int sshPort;

    @Value("${APP_HOME:null}")
    String appHome;

    @Autowired
    private UserService userService;

    @Value("${repository.code:null}")
    private String repositoryCode;

    @Value("${repository.address:null}")
    private String repositoryAddress;

    /**
     * 创建仓库
     * @param repository 信息
     * @return 仓库id
     */
    @Override
    public String createRpy(Repository repository){
        //判断是否有剩余内存
        boolean resMemory = memoryManService.findResMemory();
        if (resMemory){
            //git文件存放位置
            String repositoryAddress = RepositoryUtil.findRepositoryAddress(repository);

            String ignoreFilePath = appHome+"/file/.gitignore";
            String mdFilePath =appHome+"file/README.md";
            logger.info("创建仓库ignoreFilePath:"+ignoreFilePath);

            GitUntil.createRepository(repositoryAddress,ignoreFilePath,mdFilePath,repository.getUser());

            repository.setCreateTime(RepositoryUtil.date(1,new Date()));
            repository.setUpdateTime(RepositoryUtil.date(1,new Date()));
            RepositoryEntity groupEntity = BeanMapper.map(repository, RepositoryEntity.class);

            if (repository.getAddress().contains("/")){
                String address = repository.getAddress().substring(0, repository.getAddress().indexOf("/"));
                String allAddress = RepositoryUtil.defaultPath() + "/" + address;
                File file = new File(allAddress);
                if (!file.exists() && !file.isDirectory()) {
                    file.mkdirs();
                }
            }
            String repositoryId = repositoryDao.createRpy(groupEntity);
            dmRoleService.initDmRoles(repositoryId, LoginContext.getLoginId(), "xcode");
            return repositoryId;
        }
        else {
            throw  new ApplicationException(4006,"内存不足");
        }
    }


    /**
     * 删除仓库
     * @param rpyId 仓库id
     */
    @Override
    public void deleteRpy(String rpyId) {
        Repository repository = findOneRpy(rpyId);
        repositoryDao.deleteRpy(rpyId);

        recordOpenService.deleteRecordOpenByRecord(rpyId);
        recordCommitService.deleteRecordCommitByRepository(rpyId);

        dmUserService.deleteDmUserByDomainId(rpyId);
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
        groupEntity.setUpdateTime(RepositoryUtil.date(1,new Date()));
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
        if (!ObjectUtils.isEmpty(repository)){
            RepositoryCloneAddress cloneAddress = findCloneAddress(rpyId);
            repository.setFullPath(cloneAddress.getHttpAddress());
        }
        return repository;
    }

    public Repository findOne(String rpyId){
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
        for (Repository repository:list){
            RepositoryCloneAddress cloneAddress = findCloneAddress(repository.getRpyId());
            repository.setFullPath(cloneAddress.getHttpAddress());
        }
        joinTemplate.joinQuery(list);
        return list;
    }


    @Override
    public List<Repository> findAllRpyList(List<String> idList) {
        List<RepositoryEntity> groupEntities = repositoryDao.findAllRpyList(idList);
        List<RepositoryEntity> entities = groupEntities.stream().filter(a -> !ObjectUtils.isEmpty(a)).collect(Collectors.toList());
        List<Repository> list = BeanMapper.mapList(entities, Repository.class);

        joinTemplate.joinQuery(list);
        return list;
    }


    /**
     * 根据仓库名称查询仓库信息
     * @param rpyName 仓库名称
     * @return 仓库信息
     */
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
                System.out.println("");
            }
            if (rpy.getAddress().equals(rpyName)){
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
        Repository repository = findOne(rpyId);
        String path = repository.getAddress();
        String  ip ;
        //获取本机地址
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            ip = "172.0.0.1";
        }
        RepositoryCloneAddress repositoryCloneAddress = new RepositoryCloneAddress();
        String address = RepositoryUtil.findRepositoryAddress(repository);
        repositoryCloneAddress.setFileAddress(address);
        // String username = System.getProperty("user.name");
        String loginId = LoginContext.getLoginId();
        User user = userService.findOne(loginId);

        String http=null;
        if (StringUtils.isNotEmpty(repositoryAddress)){
             http = repositoryAddress + "/"+repositoryCode+"/"+ path + ".git";
        }else {
             http = "http://" + ip + ":" + port + "/"+repositoryCode+"/"+ path + ".git";
        }
        String SSH=null;

        if (!ObjectUtils.isEmpty(user)){
             SSH = "ssh://"+ user.getName() +"@"+ip + ":" + sshPort +"/" + path + ".git";
        }

        repositoryCloneAddress.setHttpAddress(http);
        repositoryCloneAddress.setSSHAddress(SSH);
        return repositoryCloneAddress;

    }


    public List<Repository> findRepositoryList(RepositoryQuery repositoryQuery){
        List<RepositoryEntity> repositoryEntityList = repositoryDao.findRepositoryList(repositoryQuery);

        List<Repository> repositoryList = BeanMapper.mapList(repositoryEntityList,Repository.class);


        joinTemplate.joinQuery(repositoryList);
        return repositoryList;
    }


    @Override
    public Pagination<Repository> findRepositoryPage(RepositoryQuery repositoryQuery) {
        List<Repository> allRpy = this.findRepositoryList(repositoryQuery);
        if (CollectionUtils.isNotEmpty(allRpy)) {
            //公共的仓库
            List<Repository> publicRpy = allRpy.stream().filter(a -> ("public").equals(a.getRules())).collect(Collectors.toList());

            List<String> canViewRpyId = publicRpy.stream().map(Repository::getRpyId).collect(Collectors.toList());

            List<Repository> privateRpy = allRpy.stream().filter(a -> ("private").equals(a.getRules())).collect(Collectors.toList());

            //查询用户id 查询关联的项目
            DmUserQuery dmUserQuery = new DmUserQuery();
            dmUserQuery.setUserId(repositoryQuery.getUserId());
            List<DmUser> dmUserList = dmUserService.findDmUserList(dmUserQuery);

            //存在私有的和权限成员
            if (CollectionUtils.isNotEmpty(privateRpy) && CollectionUtils.isNotEmpty(dmUserList)) {
                List<String> privateRpyId = privateRpy.stream().map(Repository::getRpyId).collect(Collectors.toList());
                List<String> dmRpyId = dmUserList.stream().map(DmUser::getDomainId).collect(Collectors.toList());

                //查询私有相同的仓库id
                privateRpyId = privateRpyId.stream().filter(dmRpyId::contains).collect(Collectors.toList());
                canViewRpyId = Stream.concat(privateRpyId.stream(), canViewRpyId.stream()).collect(Collectors.toList());
            }
            String[] canViewRpyIdList = canViewRpyId.toArray(new String[canViewRpyId.size()]);
            Pagination<RepositoryEntity> pagination = repositoryDao.findRepositoryPage(repositoryQuery, canViewRpyIdList);
            List<Repository> repositoryList = BeanMapper.mapList(pagination.getDataList(),Repository.class);
            joinTemplate.joinQuery(repositoryList);
            return PaginationBuilder.build(pagination,repositoryList);
        }
        return null;
    }

    @Override
    public Repository findRepository(String id) {
        Repository repository = this.findOneRpy(id);

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

    @Override
    public  List<Repository> findRepositoryByName(RepositoryQuery repositoryQuery) {
        List<Repository> repositoryList = this.findRepositoryList(repositoryQuery);

        List<Repository> repositorys=null;
        if (CollectionUtils.isNotEmpty(repositoryList)){
            if (StringUtils.isEmpty(repositoryQuery.getGroupId())){
                List<Repository> repositories = repositoryList.stream().filter(a -> ObjectUtils.isEmpty(a.getGroup())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(repositories)){
                    repositorys= repositories;
                }
            }else {
                repositorys=repositoryList;
            }
        }
        return repositorys;
    }

}










































