package io.tiklab.gitpuk.repository.service;

import io.tiklab.gitpuk.merge.service.MergeRequestService;
import io.tiklab.gitpuk.common.*;
import io.tiklab.gitpuk.repository.model.*;
import io.tiklab.gitpuk.authority.ValidUsrPwdServer;
import io.tiklab.gitpuk.branch.model.Branch;
import io.tiklab.gitpuk.branch.model.RepositoryBranch;
import io.tiklab.gitpuk.branch.model.RepositoryBranchQuery;
import io.tiklab.gitpuk.branch.service.RepositoryBranchService;
import io.tiklab.gitpuk.common.git.GitBranchUntil;
import io.tiklab.gitpuk.common.git.GitUntil;
import io.tiklab.gitpuk.repository.dao.RepositoryDao;
import io.tiklab.gitpuk.repository.entity.RepositoryEntity;
import io.tiklab.gitpuk.tag.service.TagService;
import io.tiklab.privilege.role.model.PatchUser;
import io.tiklab.privilege.role.model.RoleUser;
import io.tiklab.privilege.role.service.RoleFunctionService;
import io.tiklab.privilege.role.service.RoleUserService;
import io.tiklab.toolkit.beans.BeanMapper;
import io.tiklab.core.context.AppHomeContext;
import io.tiklab.core.exception.ApplicationException;
import io.tiklab.core.exception.SystemException;
import io.tiklab.core.page.Pagination;
import io.tiklab.core.page.PaginationBuilder;
import io.tiklab.eam.common.context.LoginContext;
import io.tiklab.toolkit.join.JoinTemplate;
import io.tiklab.privilege.dmRole.model.DmRoleUser;
import io.tiklab.privilege.dmRole.model.DmRoleUserQuery;
import io.tiklab.privilege.dmRole.service.DmRoleService;
import io.tiklab.privilege.dmRole.service.DmRoleUserService;
import io.tiklab.rpc.annotation.Exporter;
import io.tiklab.user.dmUser.model.DmUser;
import io.tiklab.user.dmUser.model.DmUserQuery;
import io.tiklab.user.dmUser.service.DmUserService;
import io.tiklab.user.user.model.User;
import io.tiklab.user.user.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
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
public class RepositoryServerImpl implements RepositoryService {

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
    GitPukYamlDataMaService yamlDataMaService;

    @Autowired
    private UserService userService;

    @Autowired
    LeadRecordService leadRecordService;

    @Autowired
    ValidUsrPwdServer validUsrPwdServer;


    @Autowired
    GitPukMessageService gitTokMessageService;

    @Autowired
    TagService tagService;

    @Autowired
    RepositoryCollectService repositoryCollectService;

    @Autowired
    RoleUserService roleUserService;

    @Autowired
    RepositoryBranchService branchService;

    @Autowired
    RoleFunctionService roleFunService;

    @Autowired
    RepositoryLfsService lfsService;

    @Autowired
    MergeRequestService mergeRequestService;


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
        RepositoryEntity repositoryEntity = BeanMapper.map(repository, RepositoryEntity.class);


        String repositoryId = repositoryDao.createRpy(repositoryEntity);

        repositoryEntity.setRpyId(repositoryId);

        String userId;
       //初始化示例仓库用户id 取Repository里面用户
        if (!ObjectUtils.isEmpty(repository.getUser())&&StringUtils.isNotEmpty(repository.getUser().getId())){
             userId = repository.getUser().getId();
        }else {
            userId =LoginContext.getLoginId();
        }


        List<PatchUser> List = new ArrayList<>();
        PatchUser patchUser = new PatchUser();
        RoleUser userRoleAdmin = roleUserService.findUserRoleAdmin();
        //给系统超级管理员设置成项目超级管理员
        patchUser.setUserId(userRoleAdmin.getUser().getId());
        patchUser.setRoleType(2);
        List.add(patchUser);

        //超级管理员和创建者不同 ，给创建者设置为管理员角色
        if (!(userId).equals(userRoleAdmin.getUser().getId())){
            PatchUser patchUser1 = new PatchUser();
            patchUser1.setUserId(userId);
            patchUser1.setRoleType(1);
            List.add(patchUser1);
        }
        dmRoleService.initPatchDmRole(repositoryId, List);


        //正式仓库才发送消息
        if (repository.getCategory()==2){
            //发送消息日志
            sendMessLog(repositoryEntity,"create",null);
        }
        return repositoryId;
    }


    /**
     * 删除仓库
     * @param rpyId 仓库id
     */
    @Override
    public void deleteRpy(String rpyId) {
        RepositoryEntity repositoryEntity =  repositoryDao.findOneRpy(rpyId);

        repositoryDao.deleteRpy(rpyId);

        Thread thread = new Thread() {
            public void run() {

                //删除关联的数据
                deleteRelevancy(rpyId,"delete");

            /*    //删除计划
                scanPlayService.deleteScanPlayByCondition("repositoryId",rpyId);*/

                //发送消息日志
                sendMessLog(repositoryEntity,"delete",null);

                //删除演示库时  删除项目内的演示文件
                if (repositoryEntity.getCategory()==1){
                    try {
                        File zipFile = new File(AppHomeContext.getAppHome()+"/file/sample.zip");
                        FileUtils.delete(zipFile);
                    }catch (Exception e){
                        logger.info("删除文件sample.zip失败");
                    }
                }
            }
        };
        thread.start();
    }

    @Override
    @Transactional
    public void resetRepository(String rpyId) {
        try {
            RepositoryEntity repositoryEntity =  repositoryDao.findOneRpy(rpyId);

            //删除关联的数据
            deleteRelevancy(rpyId,"reset");

            //创建裸仓库
            String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),rpyId);
            GitUntil.createRepository(repositoryAddress);

            Repository repository = this.findOne(rpyId);
            repository.setSize(0L);
            this.updateRepository(repository);

            //重置后发送消息
            sendMessLog(repositoryEntity,"reset",null);

        }catch (Exception e){
            logger.info("重置仓库失败"+e.getMessage());
            throw new SystemException(e);
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

        RepositoryEntity repositoryEntity = BeanMapper.map(repository, RepositoryEntity.class);
        repositoryEntity.setUpdateTime(RepositoryUtil.date(1,new Date()));


        //校验修改的仓库名字是否重复
      //  String namespace = repository.getAddress().substring(0, repository.getAddress().indexOf("/", 1));
        //存在仓库组
        List<RepositoryEntity>  repositoryEntityList = repositoryDao.findRepositoryByNamespace(repository.getAddress());

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
        repositoryDao.updateRpy(repositoryEntity);

        //更新名字后发送消息
        if (!oneRpy.getName().equals(repository.getName())){
            sendMessLog(oneRpy,"update",repository.getName());
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

        //查询有权限的仓库
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
            repository.setSshPath(cloneAddress.getSSHAddress());
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

            //查询默认分支
            String fullBranch = findDefaultBranch(repository.getRpyId());

            if (StringUtils.isNotEmpty(fullBranch)){
                repository.setDefaultBranch(fullBranch);
                repository.setNotNull(fullBranch.isEmpty());
            }


            String size = RepositoryUtil.formatSize(repository.getSize());
            repository.setRpySize(size);
        }
        return repository;
    }

    @Override
    public Repository findConciseRepositoryByAddress(String address) {
        List<RepositoryEntity> repositoryEntityList = repositoryDao.findRepositoryByAddress(address);

        List<Repository> repositoryList = BeanMapper.mapList(repositoryEntityList,Repository.class);

        if (CollectionUtils.isEmpty(repositoryList)){
            return null;
        }

        try {
            Repository repository = repositoryList.get(0);

            String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),repositoryList.get(0).getRpyId());

            //查询所有分支
            List<Branch> allBranch = GitBranchUntil.findAllBranch(repositoryAddress);
            if (CollectionUtils.isEmpty(allBranch)){
                return null;
            }

            //获取默认分支
            List<Branch> defaultBranch = allBranch.stream().filter(a -> a.isDefaultBranch()).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(defaultBranch)){
                repository.setUniqueBranch(allBranch.get(0).getBranchName());
            }

            return repository;

        }catch (Exception e){
            logger.info("查询简洁的仓库获取分支失败："+e.getMessage());
            throw new SystemException(9000,"查询仓库获取分支失败"+e.getMessage());
        }
    }

    @Override
    public Repository findOnlyRpyByAddress(String address) {
        List<RepositoryEntity> repositoryEntityList = repositoryDao.findRepositoryByAddress(address);
        List<Repository> repositoryList = BeanMapper.mapList(repositoryEntityList,Repository.class);
        if (CollectionUtils.isEmpty(repositoryList)){
            return null;
        }
        return repositoryList.get(0);
    }


    @Override
    public void deleteRpyByAddress(String address) {
        List<RepositoryEntity> repositoryEntityList = repositoryDao.findRepositoryByAddress(address);

        List<Repository> repositoryList = BeanMapper.mapList(repositoryEntityList,Repository.class);
        this.deleteRpy(repositoryList.get(0).getRpyId());
    }

    @Override
    public String findRepositoryAp(String address) {
        Repository repository = this.findRepositoryByAddress(address);
        if (ObjectUtils.isEmpty(repository)){
            return null;
        }
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
    public Pagination<Repository>  findRepositoryListByUser(RepositoryQuery repositoryQuery) {
        List<RepositoryEntity> allRpyEntity = repositoryDao.findRepositoryListLike(repositoryQuery);
        List<Repository> allRpy = BeanMapper.mapList(allRpyEntity, Repository.class);

        List<String> accessRepositoryId = findHaveAccessRepository(allRpy, repositoryQuery.getUserId(),"all");

        if (!ObjectUtils.isEmpty(accessRepositoryId)&&accessRepositoryId.size()>0){
            String[] canViewRpyIdList = accessRepositoryId.toArray(new String[accessRepositoryId.size()]);
            Pagination<RepositoryEntity> pagination = repositoryDao.findRepositoryPage(repositoryQuery, canViewRpyIdList);
            List<Repository> repositoryList = BeanMapper.mapList(pagination.getDataList(),Repository.class);

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

    /**
     *查询当前仓库的默认分支
     * @return repositoryId repositoryId
     */
    public String findDefaultBranch(String repositoryId){
        try {
            String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),repositoryId);

            //查询所有分支
            List<Branch> allBranch = GitBranchUntil.findAllBranch(repositoryAddress);
            if (CollectionUtils.isEmpty(allBranch)){
                return null;
            }

            List<Branch> defaultBranch = allBranch.stream().filter(a -> a.isDefaultBranch()).collect(Collectors.toList());
           if (CollectionUtils.isEmpty(defaultBranch)){
               return null;
           }
            return defaultBranch.get(0).getBranchName();
        } catch (Exception e) {
            throw new SystemException(9000,"仓库不存在"+e);
        }
    }



    /**
     *查询有权限的仓库
     */
    public Pagination<Repository> findViewRepository(RepositoryQuery repositoryQuery,List<Repository> AllRepository){
        List<String> accessRepositoryId = findHaveAccessRepository(AllRepository, repositoryQuery.getUserId(),"all");

        String[] canViewRpyIdList;

        //查询我的收藏
       if (("collect").equals(repositoryQuery.getFindType())){
           List<String> rpyIds = AllRepository.stream().map(Repository::getRpyId).collect(Collectors.toList());
           String[] rpyIdsArray = rpyIds.toArray(new String[rpyIds.size()]);
           List<RepositoryCollect> repositoryCollectList = repositoryCollectService.findRepositoryCollectList(rpyIdsArray,repositoryQuery.getUserId());
           if (CollectionUtils.isEmpty(repositoryCollectList)){
                return null;
            }
           List<String> stringList = repositoryCollectList.stream()
                   .map(RepositoryCollect::getRepositoryId).collect(Collectors.toList());

           //获取重复的
           accessRepositoryId=accessRepositoryId.stream()
                   .filter(stringList::contains)
                   .collect(Collectors.toList());
       }

        canViewRpyIdList = accessRepositoryId.toArray(new String[accessRepositoryId.size()]);
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
     *删除关联的仓库数据
     * @param rpyId 仓库id
     * @param type 类型 delete、reset
     */
    public void deleteRelevancy(String rpyId,String type){
        //删除打开记录
        recordOpenService.deleteRecordOpenByRecord(rpyId);
        //删除提交记录
        recordCommitService.deleteRecordCommitByRepository(rpyId);

        //删除项目成员
        if (("delete").equals(type)){
            dmUserService.deleteDmUserByDomainId(rpyId);
        }
        //删除导入(如果存在)
        leadRecordService.deleteLeadRecord("rpyId",rpyId);
        //删除数据库分支记录
        branchService.deleteRepositoryBranch(rpyId,null);
        //删除lfs大文件记录
        lfsService.deleteRepositoryLfsByRpyId(rpyId);
        //删除合并请求
        mergeRequestService.deleteMergeRequestByCondition("rpyId",rpyId);

        //删除文件
        deleteFile(rpyId);

    }

    public void deleteFile(String rpyId){
        //删除文件
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),rpyId);
        File file = new File(repositoryAddress);
        if (!file.exists()){
            return;
        }
        RepositoryFileUtil.deleteFile(file);
    }

    /**
     * push 仓库数据后编辑仓库信息
     * @param repositoryId
     */
    public void compileRepository(String repositoryId)  {

        Repository repository = findConciseRepositoryByAddress(repositoryId);
        if (!ObjectUtils.isEmpty(repository)){
            //仓库地址
            String repositoryUrl = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(), repository.getRpyId());
            File file = new File(repositoryUrl);

            //通过提交创建数据库分支记录
            try {
                org.eclipse.jgit.lib.Repository gitRpy = Git.open(file).getRepository();
                //唯一分支不为空，表示没有默认分支，则设置默认分支
                if (StringUtils.isNotEmpty(repository.getUniqueBranch())){
                    GitBranchUntil.updateFullBranch(gitRpy, repository.getUniqueBranch());
                }
                //更新仓库文件大小
                if (file.exists()){
                    long logBytes = FileUtils.sizeOfDirectory(file);
                    repository.setSize(logBytes);
                    this.updateRepository(repository);
                }



                //查询提交后裸仓库中的所有分支
                List<Branch>  allBranch = GitBranchUntil.findAllBranch(repositoryUrl);
                List<RepositoryBranch> branchList = branchService.findRepositoryBranchList(new RepositoryBranchQuery().setRepositoryId(repository.getRpyId()));
                for (Branch branch:allBranch){
                    List<RepositoryBranch> branches = branchList.stream().filter(a -> (branch.getBranchName()).equals(a.getBranchName())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(branches)){
                        continue;
                    }
                    //数据库中分支记录和裸仓库中的分支有不同的 创建分支记录
                    RepositoryBranch repositoryBranch = new RepositoryBranch();
                    repositoryBranch.setBranchName(branch.getBranchName());
                    repositoryBranch.setBranchId(branch.getBranchId());
                    repositoryBranch.setRepositoryId(repository.getRpyId());
                    repositoryBranch.setCreateUser(LoginContext.getLoginId());
                    branchService.createRepositoryBranch(repositoryBranch);
                }
            } catch (Exception e) {
                logger.error("提交"+repository.getAddress()+"代码后获取仓库分支失败："+e.getMessage() );
                throw new SystemException(e);
            }

        }
    }

    @Override
    public String findRefCodeType(String refCode,String repoId) {
        String repositoryUrl =RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(), repoId);

        try {
            Git git = Git.open(new File(repositoryUrl));
            org.eclipse.jgit.lib.Repository repository = git.getRepository();
            //分支和标签
            List<Ref> refs = repository.getRefDatabase().getRefs();
            List<Ref> collect = refs.stream().filter(a ->a.getName().endsWith (refCode)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(collect)){
                String name = collect.get(0).getName();
                if (name.startsWith("refs/tags")){
                    return "tag";
                }else if(name.startsWith("refs/heads")||name.startsWith("refs/remotes")){
                    return "branch";
                }
            }
            return "commit";
        }catch (Exception e){
            throw new SystemException("根据code查询类型报错："+e);
        }
    }


    /**
     *操作仓库发送消息
     * @param repository 操作的仓库
     * @param type  操作类型
     * @param  updateName 更新名字
     */
    public void sendMessLog(RepositoryEntity repository,String type,String updateName){
        
        HashMap<String, Object> map = gitTokMessageService.initMessageAndLogMap();

        map.put("repositoryName", repository.getName());
        map.put("repositoryId",repository.getRpyId());
        map.put("action",repository.getName());
        //删除仓库发送消息和日志
        if (("delete").equals(type)){
            map.put("message", repository.getName());
            map.put("link", GitPukFinal.LOG_RPY_DELETE);
            map.put("qywxurl",GitPukFinal.LOG_RPY_DELETE);
            gitTokMessageService.deployMessage(map, GitPukFinal.LOG_TYPE_DELETE);
            gitTokMessageService.deployLog(map, GitPukFinal.LOG_TYPE_DELETE,"repository");
        }

        //更新仓库发送消息和日志
        if (("update").equals(type)){
            map.put("message", repository.getName()+"更改为"+updateName);
            map.put("link", GitPukFinal.LOG_RPY_UPDATE);
            map.put("qywxurl",GitPukFinal.LOG_RPY_UPDATE);
            map.put("updateName",repository.getName());
            map.put("repositoryName",updateName);
            String replaceAll = repository.getAddress().replaceAll(repository.getName(), updateName);
            map.put("repositoryPath",replaceAll);
            gitTokMessageService.deployMessage(map, GitPukFinal.LOG_TYPE_UPDATE);
            gitTokMessageService.deployLog(map, GitPukFinal.LOG_TYPE_UPDATE,"repository");
        }

        //创建仓库发送消息和日志
        map.put("repositoryPath",repository.getAddress());
        if (("create").equals(type)){
            map.put("message", repository.getName());
            map.put("link", GitPukFinal.LOG_RPY_CREATE);
            map.put("qywxurl",GitPukFinal.LOG_RPY_CREATE);
            gitTokMessageService.deployMessage(map, GitPukFinal.LOG_TYPE_CREATE);
            gitTokMessageService.deployLog(map, GitPukFinal.LOG_TYPE_CREATE,"repository");
        }

        //重置仓库发送消息和日志
        if (("reset").equals(type)){
            map.put("message", repository.getName());
            map.put("link", GitPukFinal.LOG_RPY_RESET);
            map.put("qywxurl",GitPukFinal.LOG_RPY_RESET);
            gitTokMessageService.deployMessage(map, GitPukFinal.LOG_TYPE_RESET);
            gitTokMessageService.deployLog(map, GitPukFinal.LOG_TYPE_RESET,"repository");
        }
    }
}

