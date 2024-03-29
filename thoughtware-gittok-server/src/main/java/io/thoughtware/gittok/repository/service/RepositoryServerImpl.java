package io.thoughtware.gittok.repository.service;

import io.thoughtware.gittok.authority.ValidUsrPwdServer;
import io.thoughtware.gittok.branch.model.Branch;
import io.thoughtware.gittok.common.*;
import io.thoughtware.gittok.common.git.GitBranchUntil;
import io.thoughtware.gittok.common.git.GitUntil;
import io.thoughtware.gittok.file.model.FileTree;
import io.thoughtware.gittok.file.model.FileTreeMessage;
import io.thoughtware.gittok.repository.dao.RepositoryDao;
import io.thoughtware.gittok.repository.entity.RepositoryEntity;
import io.thoughtware.gittok.repository.model.*;
import io.thoughtware.gittok.scan.service.ScanPlayService;
import io.thoughtware.gittok.tag.service.TagService;
import io.thoughtware.toolkit.beans.BeanMapper;
import io.thoughtware.core.context.AppHomeContext;
import io.thoughtware.core.exception.ApplicationException;
import io.thoughtware.core.exception.SystemException;
import io.thoughtware.core.page.Pagination;
import io.thoughtware.core.page.PaginationBuilder;
import io.thoughtware.eam.common.context.LoginContext;
import io.thoughtware.toolkit.join.JoinTemplate;
import io.thoughtware.privilege.dmRole.model.DmRoleUser;
import io.thoughtware.privilege.dmRole.model.DmRoleUserQuery;
import io.thoughtware.privilege.dmRole.service.DmRoleService;
import io.thoughtware.privilege.dmRole.service.DmRoleUserService;
import io.thoughtware.rpc.annotation.Exporter;
import io.thoughtware.user.dmUser.model.DmUser;
import io.thoughtware.user.dmUser.model.DmUserQuery;
import io.thoughtware.user.dmUser.service.DmUserService;
import io.thoughtware.user.user.model.User;
import io.thoughtware.user.user.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
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
    private DmRoleUserService dmRoleUserService;

    @Autowired
    private DmUserService dmUserService;

    @Autowired
    private RecordOpenService recordOpenService;

    @Autowired
    private RecordCommitService recordCommitService;

    @Autowired
    private MemoryManService memoryManService;

    @Autowired
    GitTokYamlDataMaService yamlDataMaService;

    @Autowired
    private UserService userService;

    @Autowired
    LeadRecordService leadRecordService;

    @Autowired
    ValidUsrPwdServer validUsrPwdServer;


    @Autowired
    ScanPlayService scanPlayService;

    @Autowired
    GitTokMessageService gitTorkMessageService;

    @Autowired
    TagService tagService;



    /**
     * 创建仓库
     * @param repository 信息
     * @return 仓库id
     */
    @Override
    @Transactional
    public String createRpyData(Repository repository){
        //判断是否有剩余内存
        boolean resMemory = memoryManService.findResMemory();
        if (resMemory){

            String repositoryId = createRpy(repository);

            //git文件存放位置
            String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),repositoryId);

            String appHome = AppHomeContext.getAppHome();


            String ignoreFilePath = null;

            //logger.info("创建仓库README.md:"+mdFilePath);
           // logger.info("创建仓库ignoreFilePath:"+ignoreFilePath);

            //Gitignore存在
            if (StringUtils.isNotEmpty(repository.getGitignoreValue())){
               /* List<GitignoreFile> gitignoreFileList = gitignoreFileService.findGitignoreFileList(new GitignoreFileQuery().setValue(repository.getGitignoreValue()));
                if (CollectionUtils.isNotEmpty(gitignoreFileList)){
                     ignoreFileData = gitignoreFileList.get(0).getData();
                }*/
                ignoreFilePath= appHome+"/gitignore/"+repository.getGitignoreValue();
            }

            //创建仓库
            Git git = GitUntil.createRepository(repositoryAddress);

            //创建初始化README.md、.gitignore文件
            if (ignoreFilePath != null || repository.getIsReadme()==1){
                //初始化数据
                GitUntil.addRepositoryInitFile(git,repository,ignoreFilePath);
            }

            return repositoryId;

        } else {
            throw  new ApplicationException(4006,"内存不足");
        }
    }

    @Override
    public String createRpy(Repository repository) {
        repository.setCreateTime(RepositoryUtil.date(1,new Date()));
        repository.setUpdateTime(RepositoryUtil.date(1,new Date()));
        Random random = new Random();
        // 生成0到4之间的随机数
        int randomNum = random.nextInt(5);
        repository.setColor(randomNum);
        RepositoryEntity groupEntity = BeanMapper.map(repository, RepositoryEntity.class);


        String repositoryId = repositoryDao.createRpy(groupEntity);

        if (!ObjectUtils.isEmpty(repository.getUser())&&StringUtils.isNotEmpty(repository.getUser().getId())){
            //创建仓库 给创建人设置管理员权限
            dmRoleService.initDmRoles(repositoryId, repository.getUser().getId(), "gittok");
        }else {
            //创建仓库 给创建人设置管理员权限
            dmRoleService.initDmRoles(repositoryId, LoginContext.getLoginId(), "gittok");
        }

        //正式仓库才发送消息
        if (repository.getCategory()==2){
            //发送消息
            initRepositoryMap(groupEntity,"create",null);
        }
        return repositoryId;
    }


    /**
     * 删除仓库
     * @param rpyId 仓库id
     */
    @Override
    public void deleteRpy(String rpyId) {
        RepositoryEntity oneRpy = repositoryDao.findOneRpy(rpyId);

        repositoryDao.deleteRpy(rpyId);

        Thread thread = new Thread() {
            public void run() {
                //删除打开记录
                recordOpenService.deleteRecordOpenByRecord(rpyId);
                //删除提交记录
                recordCommitService.deleteRecordCommitByRepository(rpyId);
                //删除项目成员
                dmUserService.deleteDmUserByDomainId(rpyId);
                //删除导入(如果存在)
                leadRecordService.deleteLeadRecord("rpyId",rpyId);

                //删除文件
                String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),rpyId);
                File file = new File(repositoryAddress);
                if (!file.exists()){
                    return;
                }
                RepositoryFileUtil.deleteFile(file);

                //删除计划
                scanPlayService.deleteScanPlayByCondition("repositoryId",rpyId);

                //发送消息
                initRepositoryMap(oneRpy, "delete",null);
            }
        };
        thread.start();
    }

    @Override
    @Transactional
    public void resetRepository(String rpyId) {
        try {
            deleterRpyRelatedData(rpyId);

            //创建裸仓库
            String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),rpyId);
            GitUntil.createRepository(repositoryAddress);
        }catch (Exception e){
            throw new SystemException(500,"重置失败");
        }
    }

    /**
     * 更新仓库
     * @param repository 仓库信息
     */
    @Override
    public void updateRpy(Repository repository) {
        //更新名称
        RepositoryEntity oneRpy = repositoryDao.findOneRpy(repository.getRpyId());

        RepositoryEntity groupEntity = BeanMapper.map(repository, RepositoryEntity.class);
        groupEntity.setUpdateTime(RepositoryUtil.date(1,new Date()));


        //校验修改的仓库名字是否重复
        String namespace = repository.getAddress().substring(0, repository.getAddress().indexOf("/", 1));
        //存在仓库组
        List<RepositoryEntity>  repositoryEntityList = repositoryDao.findRepositoryByNamespace(namespace);

        if (CollectionUtils.isNotEmpty(repositoryEntityList)){
            List<RepositoryEntity> entities = repositoryEntityList.stream().filter(a -> a.getName().equals(repository.getName())&&
                    !a.getRpyId().equals(repository.getRpyId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(entities)){
                throw  new SystemException(9000,"仓库名字重复");
            }

            List<RepositoryEntity> path = repositoryEntityList.stream().filter(a -> a.getAddress().equals(repository.getAddress())&&
                    !a.getRpyId().equals(repository.getRpyId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(path)){
                throw  new SystemException(9001,"仓库地址重复");
            }
        }
        repositoryDao.updateRpy(groupEntity);

        //更新名字后发送消息
        if (!oneRpy.getName().equals(repository.getName())){
            initRepositoryMap(oneRpy,"update",repository.getName());
        }
    }


    /**
     * 更新仓库
     * @param repository 仓库信息
     */
    @Override
    public void updateRepository(Repository repository) {
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

        return repository;
    }

    public Repository findOne(String rpyId){
        RepositoryEntity groupEntity = repositoryDao.findOneRpy(rpyId);
        Repository repository = BeanMapper.map(groupEntity, Repository.class);
        joinTemplate.joinQuery(repository);
        return repository;
    }

    @Override
    public Repository findOneOnlyRpy(String rpyId) {
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
    public List<Repository> findAllRpyList() {
        List<RepositoryEntity> groupEntityList = repositoryDao.findAllRpy();
        List<Repository> list = BeanMapper.mapList(groupEntityList, Repository.class);

        joinTemplate.joinQuery(list);

        return list;
    }


    /**
     * 查询所有仓库
     * @return 仓库信息列表
     */
    @Override
    public List<Repository> findRpyList() {
        List<RepositoryEntity> groupEntityList = repositoryDao.findAllRpy();
        List<Repository> list = BeanMapper.mapList(groupEntityList, Repository.class);
        return list;
    }

    @Override
    public List<Repository> findList(List<String> idList) {
        List<RepositoryEntity> groupEntities = repositoryDao.findRepositoryList(idList);
      //  List<RepositoryEntity> entities = groupEntities.stream().filter(a -> !ObjectUtils.isEmpty(a)).collect(Collectors.toList());
        List<Repository> list = BeanMapper.mapList(groupEntities, Repository.class);

        return list;
    }


    /**
     * 获取文件信息
     * @param message 信息
     * @return 文件集合
     */
    @Override
    public List<FileTree> findFileTree(FileTreeMessage message){


        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),message.getRpyId()) ;

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
        String  ip=null ;
        //获取本机地址
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isLoopback() || networkInterface.isVirtual()) {
                    continue;  // 跳过回环和虚拟网络接口
                }
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    if (address.isLoopbackAddress()) {
                        continue;  // 跳过回环地址
                    }
                    if (address.getHostAddress().contains(":")) {
                        continue;  // 跳过IPv6地址
                    }
                     ip = address.getHostAddress();
                }
            }
        } catch (Exception e) {
            ip = "172.0.0.1";
        }
        RepositoryCloneAddress repositoryCloneAddress = new RepositoryCloneAddress();
        String address = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),rpyId);
        repositoryCloneAddress.setFileAddress(address);
        // String username = System.getProperty("user.name");
        String loginId = LoginContext.getLoginId();
        User user = userService.findOne(loginId);

        String http;
        if (StringUtils.isNotEmpty(yamlDataMaService.visitAddress())){
             http = yamlDataMaService.visitAddress() + "/"+ path + ".git";
        }else {
             http = "http://" + ip + ":" + yamlDataMaService.serverPort() + "/"+ path + ".git";
        }
        String SSH=null;

        if (!ObjectUtils.isEmpty(user)){
             SSH = "ssh://"+ user.getName() +"@"+ip + ":" + yamlDataMaService.sshPort() +"/" + path + ".git";
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
    public List<Repository> findRepositoryList(String groupId) {
        List<RepositoryEntity> repositoryEntityList = repositoryDao.findRepositoryList(new RepositoryQuery().setGroupId(groupId));
        List<Repository> repositoryList = BeanMapper.mapList(repositoryEntityList,Repository.class);
        return repositoryList;
    }

    @Override
    public Pagination<Repository> findGroupRepository(RepositoryQuery repositoryQuery) {
        List<RepositoryEntity> repositoryEntityList= repositoryDao.findGroupRepository(repositoryQuery);
        List<Repository> repositoryList = BeanMapper.mapList(repositoryEntityList,Repository.class);
        if (CollectionUtils.isNotEmpty(repositoryList)) {
            return findViewRepository(repositoryQuery,repositoryList);
        }
        return PaginationBuilder.build(new Pagination<>(),null);
    }

    @Override
    public Pagination<Repository> findRepositoryPage(RepositoryQuery repositoryQuery) {
        List<RepositoryEntity> groupEntityList = repositoryDao.findRepositoryListLike(repositoryQuery);
        List<Repository> allRpy = BeanMapper.mapList(groupEntityList, Repository.class);

        if (CollectionUtils.isNotEmpty(allRpy)) {
            return findViewRepository(repositoryQuery,allRpy);
        }
        return PaginationBuilder.build(new Pagination<>(),null);
    }

    @Override
    public Repository findRepository(String id) {
        Repository repository = this.findOneRpy(id);
        if (!ObjectUtils.isEmpty(repository)){
            RepositoryCloneAddress cloneAddress = findCloneAddress(id);
            repository.setFullPath(cloneAddress.getHttpAddress());
        }
        try {
            String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),id);
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

    @Override
    public Repository findRepositoryByAddress(String address) {
        Repository repository=null;

        List<RepositoryEntity> repositoryEntityList = repositoryDao.findRepositoryByAddress(address);

        List<Repository> repositoryList = BeanMapper.mapList(repositoryEntityList,Repository.class);


        joinTemplate.joinQuery(repositoryList);

        if (CollectionUtils.isNotEmpty(repositoryList)){
            repository = repositoryList.get(0);
            RepositoryCloneAddress cloneAddress = findCloneAddress(repository.getRpyId());
            repository.setFullPath(cloneAddress.getHttpAddress());

            String fullBranch = findDefaultBranch(repository.getRpyId());


            repository.setDefaultBranch(fullBranch);

            repository.setNotNull(fullBranch.isEmpty());
            String size = RepositoryUtil.formatSize(repository.getSize());
            repository.setRpySize(size);
        }
        return repository;
    }



    @Override
    public void deleteRpyByAddress(String address) {
        List<RepositoryEntity> repositoryEntityList = repositoryDao.findRepositoryByAddress(address);

        List<Repository> repositoryList = BeanMapper.mapList(repositoryEntityList,Repository.class);
        this.deleteRpy(repositoryList.get(0).getRpyId());
     /*   if(CollectionUtils.isNotEmpty(repositoryList)){
            if (repositoryList.size()==1){
                this.deleteRpy(repositoryList.get(0).getRpyId());
            }else {
                throw new SystemException(9000,"出现相同路径");
            }
        }*/

    }

    @Override
    public String findRepositoryAp(String address) {
        Repository repository = this.findRepositoryByAddress(address);
        String absolutePath = yamlDataMaService.repositoryAddress() + "/" + repository.getRpyId()+ ".git";
        return absolutePath;
    }

    @Override
    public List<Repository> findRepositoryByUser(String account, String password,String DirId) {

            User user = userService.findUserByUsernameByPassWard(account, password);
            if (ObjectUtils.isEmpty(user)){
                throw new SystemException(5000,"当前用户:"+account+"不存在");
            }
            List<RepositoryEntity> repositoryEntityList = repositoryDao.findAllRpy();
            List<Repository> repositoryList = BeanMapper.mapList(repositoryEntityList,Repository.class);
            if (CollectionUtils.isNotEmpty(repositoryList)){
                String address = this.getAddress();
                List<String> accessRepositoryId = findHaveAccessRepository(repositoryList, user.getId(),"all");

                List<RepositoryEntity> repositoryEntity = repositoryDao.findRepositoryListByIds(accessRepositoryId);
                 repositoryList = BeanMapper.mapList(repositoryEntity,Repository.class);
                if (CollectionUtils.isNotEmpty(repositoryList)){
                    for (Repository repository:repositoryList){
                        String path = address + "/" + repository.getAddress() + ".git";
                        repository.setFullPath(path);
                        repository.setDefaultBranch(findDefaultBranch(repository.getRpyId()));
                    }
                }
            }
            return repositoryList;
    }


    @Override
    public String getAddress() {

        String visitAddress = yamlDataMaService.visitAddress();
        if (StringUtils.isEmpty(visitAddress)){
            String ip=null;
            try {
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = interfaces.nextElement();
                    if (networkInterface.isLoopback() || networkInterface.isVirtual()) {
                        continue;  // 跳过回环和虚拟网络接口
                    }
                    Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress address = addresses.nextElement();
                        if (address.isLoopbackAddress()) {
                            continue;  // 跳过回环地址
                        }
                        if (address.getHostAddress().contains(":")) {
                            continue;  // 跳过IPv6地址
                        }
                        ip = address.getHostAddress();
                    }
                }
            } catch (Exception e) {
                ip = "172.0.0.1";
            }
            visitAddress = "http://" + ip + ":" + yamlDataMaService.serverPort();
        }
        return visitAddress;
    }

    @Override
    public String getRepositoryPath() {

        return   yamlDataMaService.repositoryAddress();
    }

    @Override
    public List<Repository> findCommitRepository(String userId) {
        List<Repository> repositoryList=null;

        List<RecordCommit> commitList = recordCommitService.findRecordCommitList(userId);
        if (CollectionUtils.isNotEmpty(commitList)){
            List<Repository> privateRepositoryList = new ArrayList<>();
            List<Repository> publicRepositories = commitList.stream().filter(a -> ("public").equals(a.getRepository().getRules())).map(RecordCommit::getRepository).collect(Collectors.toList());

            List<RecordCommit> recordCommits = commitList.stream().filter(a -> ("private").equals(a.getRepository().getRules())).collect(Collectors.toList());

            //根据用户id 查询关联的项目
            DmUserQuery dmUserQuery = new DmUserQuery();
            dmUserQuery.setUserId(userId);
            List<DmUser> dmUserList = dmUserService.findDmUserList(dmUserQuery);
            if ( CollectionUtils.isNotEmpty(dmUserList)&&CollectionUtils.isNotEmpty(recordCommits)) {
                for (RecordCommit recordCommit:recordCommits){
                   List<DmUser> collect = dmUserList.stream().filter(a -> recordCommit.getRepository().getRpyId().equals(a.getDomainId())).collect(Collectors.toList());
                   if (CollectionUtils.isNotEmpty(collect)){
                       privateRepositoryList.add(recordCommit.getRepository());
                   }
                }
            }
            repositoryList = Stream.concat(publicRepositories.stream(), privateRepositoryList.stream()).collect(Collectors.toList());
        }
        return repositoryList;
    }

    @Override
    public Pagination<Repository>  findPrivateRepositoryByUser(RepositoryQuery repositoryQuery) {
        List<RepositoryEntity> allRpyEntity = repositoryDao.findAllRpy();
        List<Repository> allRpy = BeanMapper.mapList(allRpyEntity, Repository.class);

        List<String> accessRepositoryId = findHaveAccessRepository(allRpy, repositoryQuery.getUserId(),"private");

        if (!ObjectUtils.isEmpty(accessRepositoryId)&&accessRepositoryId.size()>0){
            String[] canViewRpyIdList = accessRepositoryId.toArray(new String[accessRepositoryId.size()]);
            Pagination<RepositoryEntity> pagination = repositoryDao.findRepositoryPage(repositoryQuery, canViewRpyIdList);
            List<Repository> repositoryList = BeanMapper.mapList(pagination.getDataList(),Repository.class);
            joinTemplate.joinQuery(repositoryList);

            for (Repository repository:repositoryList){
                DmRoleUserQuery dmRoleUserQuery = new DmRoleUserQuery();
                dmRoleUserQuery.setUserId(repositoryQuery.getUserId());
                dmRoleUserQuery.setDomainId(repository.getRpyId());
                List<DmRoleUser> dmRoleUserList = dmRoleUserService.findDmRoleUserList(dmRoleUserQuery);
                if (CollectionUtils.isNotEmpty(dmRoleUserList)){
                    repository.setRole(dmRoleUserList.get(0).getRole().getName());
                }else {
                    repository.setRole("普通成员");
                }
            }

            return PaginationBuilder.build(pagination,repositoryList);
        }

        return PaginationBuilder.build(new Pagination<>(),null);
    }

    @Override
    public String findRepositoryAuth(String rpyId) {
        RepositoryEntity oneRpy = repositoryDao.findOneRpy(rpyId);
        Repository repository = BeanMapper.map(oneRpy, Repository.class);
        if (!ObjectUtils.isEmpty(repository)){
            if (("public").equals(repository.getRules())){
                return "true";
            }
            String userId = LoginContext.getLoginId();
            DmUserQuery dmUserQuery = new DmUserQuery();
            dmUserQuery.setUserId(userId);
            dmUserQuery.setDomainId(rpyId);
            List<DmUser> dmUserList = dmUserService.findDmUserList(dmUserQuery);
            if (CollectionUtils.isNotEmpty(dmUserList)){
                return "true";
            }
        }
        return "false";
    }


    public String findDefaultBranch(String repositoryId){
        try {
            String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),repositoryId);
            File file = new File(repositoryAddress);
            Git git = Git.open(file);

        /*    List<Branch> allBranch = GitBranchUntil.findAllBranch(repositoryAddress);
            if (CollectionUtils.isEmpty(allBranch)){
                return null;
            }*/

            org.eclipse.jgit.lib.Repository repository1 = git.getRepository();
            String fullBranch = repository1.getFullBranch().replace(Constants.R_HEADS,"");
          
            git.close();
            return fullBranch;
        } catch (Exception e) {
            throw new SystemException(9000,"仓库不存在"+e);
        }
    }



    /**
     *查询有权限的仓库
     */
    public Pagination<Repository> findViewRepository(RepositoryQuery repositoryQuery,List<Repository> AllRepository){
        List<String> accessRepositoryId = findHaveAccessRepository(AllRepository, repositoryQuery.getUserId(),"all");
        String[] canViewRpyIdList = accessRepositoryId.toArray(new String[accessRepositoryId.size()]);

        if (canViewRpyIdList.length>0){
            Pagination<RepositoryEntity> pagination = repositoryDao.findRepositoryPage(repositoryQuery, canViewRpyIdList);
            List<Repository> repositoryList = BeanMapper.mapList(pagination.getDataList(),Repository.class);
            joinTemplate.joinQuery(repositoryList);
            for (Repository repository:repositoryList){
                if (!ObjectUtils.isEmpty(repository.getSize())){
                    String size = RepositoryUtil.formatSize(repository.getSize());
                    repository.setRpySize(size);
                }
            }

            return PaginationBuilder.build(pagination,repositoryList);
        }
        return PaginationBuilder.build(new Pagination<>(),null);
    }


    /**
     *查询公共和有权限的仓库
     */
    public List<String> findHaveAccessRepository(List<Repository> allRpy,String userId,String type){//公共的仓库

        List<String> privateRpyId=null;
        List<Repository> publicRpy = allRpy.stream().filter(a -> ("public").equals(a.getRules())).collect(Collectors.toList());
        List<String> canViewRpyId = publicRpy.stream().map(Repository::getRpyId).collect(Collectors.toList());

        //私有仓库
        List<Repository> privateRpy = allRpy.stream().filter(a -> ("private").equals(a.getRules())).collect(Collectors.toList());

        //存在私有库
        if (CollectionUtils.isNotEmpty(privateRpy)){
            //根据用户id 查询关联的项目
            DmUserQuery dmUserQuery = new DmUserQuery();
            dmUserQuery.setUserId(userId);
            List<DmUser> dmUserList = dmUserService.findDmUserList(dmUserQuery);
            //存在项目成员
            if ( CollectionUtils.isNotEmpty(dmUserList)) {
                 privateRpyId = privateRpy.stream().map(Repository::getRpyId).collect(Collectors.toList());
                List<String> dmRpyId = dmUserList.stream().map(DmUser::getDomainId).collect(Collectors.toList());

                //查询私有相同的仓库id
                privateRpyId = privateRpyId.stream().filter(dmRpyId::contains).collect(Collectors.toList());

                canViewRpyId = Stream.concat(privateRpyId.stream(), canViewRpyId.stream()).collect(Collectors.toList());
            }
        }
        if (("private").equals(type)){
            return privateRpyId;
        }
        return canViewRpyId;
    }


    /**
     *操作仓库发送消息
     * @param oldRepository 操作的仓库
     * @param type  操作类型
     * @param  updateName 更新名字
     */
    public void initRepositoryMap(RepositoryEntity oldRepository,String type,String updateName){

        HashMap<String, Object> map = gitTorkMessageService.initMap();

        map.put("repositoryId",oldRepository.getRpyId());
        map.put("action",oldRepository.getName());
        if (("delete").equals(type)){
            map.put("message", "删除了仓库"+oldRepository.getName());
            map.put("link",GitTokFinal.LOG_RPY_DELETE);
            gitTorkMessageService.settingMessage(map,GitTokFinal.LOG_TYPE_DELETE);
            gitTorkMessageService.settingLog(map,GitTokFinal.LOG_TYPE_DELETE,"repository");
        }

        if (("update").equals(type)){
            map.put("message", oldRepository.getName()+"更改为"+updateName);
            map.put("link",GitTokFinal.LOG_RPY_UPDATE);
            map.put("repositoryPath",oldRepository.getAddress());
            gitTorkMessageService.settingMessage(map,GitTokFinal.LOG_TYPE_UPDATE);
            gitTorkMessageService.settingLog(map,GitTokFinal.LOG_TYPE_UPDATE,"repository");
        }

        if (("create").equals(type)){
            map.put("message", "创建了仓库"+oldRepository.getName());
            map.put("link",GitTokFinal.LOG_RPY_CREATE);
            map.put("repositoryPath",oldRepository.getAddress());
            gitTorkMessageService.settingMessage(map,GitTokFinal.LOG_TYPE_CREATE);
            gitTorkMessageService.settingLog(map,GitTokFinal.LOG_TYPE_CREATE,"repository");
        }
    }

    /**
     *重置的时候删除仓库关联数据
     * @param rpyId 仓库id
     */
    public void deleterRpyRelatedData(String rpyId){

        //删除文件
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),rpyId);
        File file = new File(repositoryAddress);
        if (!file.exists()){
            return;
        }
        RepositoryFileUtil.deleteFile(file);

        //删除计划
        scanPlayService.deleteScanPlayByCondition("repositoryId",rpyId);
    }

}










































