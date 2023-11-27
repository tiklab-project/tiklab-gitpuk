package io.tiklab.xcode.repository.service;

import io.tiklab.beans.BeanMapper;
import io.tiklab.core.context.AppHomeContext;
import io.tiklab.core.exception.ApplicationException;
import io.tiklab.core.exception.SystemException;
import io.tiklab.core.page.Pagination;
import io.tiklab.core.page.PaginationBuilder;
import io.tiklab.eam.common.context.LoginContext;
import io.tiklab.eam.common.model.EamTicket;
import io.tiklab.join.JoinTemplate;
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
import io.tiklab.xcode.authority.ValidUsrPwdServer;
import io.tiklab.xcode.branch.model.Branch;
import io.tiklab.xcode.common.XcodeYamlDataMaService;
import io.tiklab.xcode.file.model.FileTree;
import io.tiklab.xcode.file.model.FileTreeMessage;
import io.tiklab.xcode.common.git.GitBranchUntil;
import io.tiklab.xcode.common.git.GitUntil;
import io.tiklab.xcode.repository.dao.RepositoryDao;
import io.tiklab.xcode.repository.entity.RepositoryEntity;
import io.tiklab.xcode.repository.model.*;
import io.tiklab.xcode.common.RepositoryFileUtil;
import io.tiklab.xcode.common.RepositoryUtil;
import io.tiklab.xcode.scan.service.ScanPlayService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    XcodeYamlDataMaService yamlDataMaService;

    @Autowired
    private UserService userService;

    @Autowired
    LeadRecordService leadRecordService;

    @Autowired
    ValidUsrPwdServer validUsrPwdServer;

    @Autowired
    XcodeYamlDataMaService xcodeYamlDataMaService;

    @Autowired
    ScanPlayService scanPlayService;


    /**
     * 创建仓库
     * @param repository 信息
     * @return 仓库id
     */
    @Override
    public String createRpyData(Repository repository){
        //判断是否有剩余内存
        boolean resMemory = memoryManService.findResMemory();
        if (resMemory){

            String repositoryId = createRpy(repository);

            //git文件存放位置
            String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),repositoryId);

            String appHome = AppHomeContext.getAppHome();
            String ignoreFilePath = appHome+"/file/.gitignore";
            String mdFilePath =appHome+"file/README.md";
            logger.info("创建仓库ignoreFilePath:"+ignoreFilePath);

            GitUntil.createRepository(repositoryAddress,ignoreFilePath,mdFilePath,repository.getUser());


            return repositoryId;
        } else {
            throw  new ApplicationException(4006,"内存不足");
        }
    }

    @Override
    public String createRpy(Repository repository) {
        repository.setCreateTime(RepositoryUtil.date(1,new Date()));
        repository.setUpdateTime(RepositoryUtil.date(1,new Date()));
        RepositoryEntity groupEntity = BeanMapper.map(repository, RepositoryEntity.class);


        String repositoryId = repositoryDao.createRpy(groupEntity);

        //创建私有仓库 给创建人设置管理员权限
        dmRoleService.initDmRoles(repositoryId, LoginContext.getLoginId(), "xcode");
        return repositoryId;
    }


    /**
     * 删除仓库
     * @param rpyId 仓库id
     */
    @Override
    public void deleteRpy(String rpyId) {
        repositoryDao.deleteRpy(rpyId);
        //删除打开记录
        recordOpenService.deleteRecordOpenByRecord(rpyId);
        //删除提交记录
        recordCommitService.deleteRecordCommitByRepository(rpyId);
        //删除项目成员
        dmUserService.deleteDmUserByDomainId(rpyId);
        //删除导入(如果存在)
        leadRecordService.deleteLeadRecord("rpyId",rpyId);
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),rpyId);
        File file = new File(repositoryAddress);
        if (!file.exists()){
            return;
        }

        scanPlayService.deleteScanPlayByCondition("repositoryId",rpyId);
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
        List<RepositoryEntity> groupEntities = repositoryDao.findAllRpyList(idList);
        List<RepositoryEntity> entities = groupEntities.stream().filter(a -> !ObjectUtils.isEmpty(a)).collect(Collectors.toList());
        List<Repository> list = BeanMapper.mapList(entities, Repository.class);

        joinTemplate.joinQuery(list);
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
             http = yamlDataMaService.visitAddress() + "/xcode/"+ path + ".git";
        }else {
             http = "http://" + ip + ":" + yamlDataMaService.serverPort() + "/xcode/"+ path + ".git";
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
    public Pagination<Repository> findRepositoryPage(RepositoryQuery repositoryQuery) {
        List<RepositoryEntity> groupEntityList = repositoryDao.findRepositoryListLike(repositoryQuery);
        List<Repository> allRpy = BeanMapper.mapList(groupEntityList, Repository.class);

        if (!ObjectUtils.isEmpty(allRpy)&&CollectionUtils.isNotEmpty(allRpy)) {
            List<String> accessRepositoryId = findHaveAccessRepository(allRpy, repositoryQuery.getUserId(),"all");
            String[] canViewRpyIdList = accessRepositoryId.toArray(new String[accessRepositoryId.size()]);

           if (canViewRpyIdList.length>0){
               Pagination<RepositoryEntity> pagination = repositoryDao.findRepositoryPage(repositoryQuery, canViewRpyIdList);
               List<Repository> repositoryList = BeanMapper.mapList(pagination.getDataList(),Repository.class);
               joinTemplate.joinQuery(repositoryList);
               return PaginationBuilder.build(pagination,repositoryList);
           }
        }
        return PaginationBuilder.build(new Pagination<>(),null);
    }

    @Override
    public Repository findRepository(String id) {
        Repository repository = this.findOneRpy(id);

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
        }
        return repository;
    }

    @Override
    public List<Repository> findRepositoryByGroupName(String groupName) {
        List<RepositoryEntity> repositoryEntityList= repositoryDao.findRepositoryByGroupName(groupName);
        List<Repository> repositoryList = BeanMapper.mapList(repositoryEntityList,Repository.class);
        joinTemplate.joinQuery(repositoryList);
        return repositoryList;
    }

    @Override
    public void deleteRpyByAddress(String address) {
        List<RepositoryEntity> repositoryEntityList = repositoryDao.findRepositoryByAddress(address);

        List<Repository> repositoryList = BeanMapper.mapList(repositoryEntityList,Repository.class);
        if(CollectionUtils.isNotEmpty(repositoryList)){
            if (repositoryList.size()==1){
                this.deleteRpy(repositoryList.get(0).getRpyId());
            }else {
                throw new SystemException(9000,"出现相同路径");
            }
        }

    }

    @Override
    public String findRepositoryAp(String address) {
        Repository repository = this.findRepositoryByAddress(address);
        String absolutePath = yamlDataMaService.repositoryAddress() + "/" + repository.getRpyId()+ ".git";
        return absolutePath;
    }

    @Override
    public List<Repository> findRepositoryByUser(String account, String password,String DirId) {
        try {
            validUsrPwdServer.validUser(account, password, DirId);
            List<RepositoryEntity> repositoryEntityList = repositoryDao.findAllRpy();
            List<Repository> repositoryList = BeanMapper.mapList(repositoryEntityList,Repository.class);
            if (CollectionUtils.isNotEmpty(repositoryList)){
                String address = this.getAddress();
                List<String> accessRepositoryId = findHaveAccessRepository(repositoryList, LoginContext.getLoginId(),"all");

                List<RepositoryEntity> repositoryEntitys = repositoryDao.findRepositoryListByIds(accessRepositoryId);
                 repositoryList = BeanMapper.mapList(repositoryEntitys,Repository.class);
                if (CollectionUtils.isNotEmpty(repositoryList)){
                    for (Repository repository:repositoryList){
                        String path = address + "/code/" + repository.getAddress() + ".git";
                        repository.setFullPath(path);
                    }
                }
            }

            return repositoryList;
        }catch (Exception e){
            throw  new SystemException("用户校验失败");
        }
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

        return   xcodeYamlDataMaService.repositoryAddress();
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
            org.eclipse.jgit.lib.Repository repository1 = git.getRepository();
            String fullBranch = repository1.getFullBranch().replace(Constants.R_HEADS,"");
          
            git.close();
            return fullBranch;
        } catch (IOException e) {
            throw new SystemException(9000,"仓库不存在"+e);
        }
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
}










































