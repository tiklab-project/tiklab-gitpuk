package io.thoughtware.gittork.repository.service;


import io.thoughtware.gittork.common.RepositoryUtil;
import io.thoughtware.gittork.repository.dao.RepositoryGroupDao;
import io.thoughtware.gittork.repository.entity.RepositoryGroupEntity;
import io.thoughtware.gittork.repository.model.Repository;
import io.thoughtware.gittork.repository.model.RepositoryGroup;
import io.thoughtware.gittork.repository.model.RepositoryGroupQuery;
import io.thoughtware.beans.BeanMapper;
import io.thoughtware.core.page.Pagination;
import io.thoughtware.core.page.PaginationBuilder;
import io.thoughtware.eam.common.context.LoginContext;
import io.thoughtware.join.JoinTemplate;
import io.thoughtware.privilege.dmRole.service.DmRoleService;
import io.thoughtware.rpc.annotation.Exporter;
import io.thoughtware.user.dmUser.model.DmUser;
import io.thoughtware.user.dmUser.model.DmUserQuery;
import io.thoughtware.user.dmUser.service.DmUserService;


import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Exporter
public class RepositoryGroupServerImpl implements RepositoryGroupServer {

    @Autowired
    private JoinTemplate joinTemplate;

    @Autowired
    private RepositoryGroupDao repositoryGroupDao;

    @Autowired
    private DmUserService dmUserService;

    @Autowired
    private DmRoleService dmRoleService;

    @Autowired
    private RepositoryServer repositoryServer;
    /**
     * 创建仓库组
     * @param repositoryGroup 信息
     * @return 仓库组id
     */
    @Override
    public String createCodeGroup(RepositoryGroup repositoryGroup) {
        RepositoryGroupEntity groupEntity = BeanMapper.map(repositoryGroup, RepositoryGroupEntity.class);
        Random random = new Random();
        // 生成0到4之间的随机数
        int randomNum = random.nextInt(5);
        groupEntity.setColor(randomNum);
        groupEntity.setCreateTime(RepositoryUtil.date(1,new Date()));
        String codeGroupId = repositoryGroupDao.createCodeGroup(groupEntity);
        dmRoleService.initDmRoles(codeGroupId, LoginContext.getLoginId(), "gittork");
        return codeGroupId;
    }

    /**
     * 删除仓库组
     * @param codeGroupId 仓库组id
     */
    @Override
    public void deleteCodeGroup(String codeGroupId) {
        repositoryGroupDao.deleteCodeGroup(codeGroupId);
    }

    /**
     * 更新仓库组
     * @param repositoryGroup 仓库组信息
     */
    @Override
    public void updateCodeGroup(RepositoryGroup repositoryGroup) {
        RepositoryGroupEntity groupEntity = BeanMapper.map(repositoryGroup, RepositoryGroupEntity.class);


        Thread thread = new Thread() {
            public void run() {
                RepositoryGroupEntity group = repositoryGroupDao.findRepositoryGroup(repositoryGroup.getGroupId());
                if (!ObjectUtils.isEmpty(group)){
                    if (!group.getName().equals(repositoryGroup.getName())){
                        List<Repository> repositoryList = repositoryServer.findRepositoryList(group.getGroupId());
                        if (CollectionUtils.isNotEmpty(repositoryList)){
                            for (Repository repository:repositoryList){
                                String name = repositoryGroup.getName();
                                repository.setAddress(name+"/"+repository.getName());
                                repositoryServer.updateRepository(repository);
                            }
                        }
                    }
                }
            }
        };
        thread.start();
        repositoryGroupDao.updateCodeGroup(groupEntity);

    }

    /**
     * 查询单个仓库组
     * @param codeGroupId 仓库组id
     * @return 仓库组信息
     */
    @Override
    public RepositoryGroup findOneCodeGroup(String codeGroupId) {
        RepositoryGroupEntity groupEntity = repositoryGroupDao.findOneCodeGroup(codeGroupId);
        RepositoryGroup repositoryGroup = BeanMapper.map(groupEntity, RepositoryGroup.class);
        joinTemplate.joinQuery(repositoryGroup);
        return repositoryGroup;
    }

    /**
     * 查询所有仓库组
     * @return 仓库组信息列表
     */
    @Override
    public List<RepositoryGroup> findAllCodeGroup() {
        List<RepositoryGroupEntity> groupEntityList = repositoryGroupDao.findAllCodeGroup();
        List<RepositoryGroup> list = BeanMapper.mapList(groupEntityList, RepositoryGroup.class);
        joinTemplate.joinQuery(list);
        return list;
    }


    @Override
    public List<RepositoryGroup> findAllCodeGroupList(List<String> idList) {
        List<RepositoryGroupEntity> groupEntities = repositoryGroupDao.findAllCodeGroupList(idList);
        List<RepositoryGroup> list = BeanMapper.mapList(groupEntities, RepositoryGroup.class);
        joinTemplate.joinQuery(list);
        return list;
    }

    /**
     * 查询用户仓库组
     * @param repositoryGroupQuery
     * @return 仓库组集合
     */
    @Override
    public Pagination<RepositoryGroup> findRepositoryGroupPage(RepositoryGroupQuery repositoryGroupQuery) {
        List<RepositoryGroupEntity> groupEntityList = repositoryGroupDao.findRepositoryListLike(repositoryGroupQuery);
        List<RepositoryGroup> allGroupList = BeanMapper.mapList(groupEntityList, RepositoryGroup.class);

        if (CollectionUtils.isNotEmpty(allGroupList)){

            List<RepositoryGroup> publicGroup = allGroupList.stream().filter(a -> ("public").equals(a.getRules())).collect(Collectors.toList());
            List<String> canViewId = publicGroup.stream().map(RepositoryGroup::getGroupId).collect(Collectors.toList());

            List<RepositoryGroup> privateGroup = allGroupList.stream().filter(a -> ("private").equals(a.getRules())).collect(Collectors.toList());

            if (CollectionUtils.isNotEmpty(privateGroup)){

                //查询用户id 查询关联的项目
                DmUserQuery dmUserQuery = new DmUserQuery();
                dmUserQuery.setUserId(repositoryGroupQuery.getUserId());
                List<DmUser> dmUserList = dmUserService.findDmUserList(dmUserQuery);
                //存在项目成员
                if (CollectionUtils.isNotEmpty(dmUserList)) {
                    List<String> privateGroupId = privateGroup.stream().map(RepositoryGroup::getGroupId).collect(Collectors.toList());
                    List<String> dmGroupId = dmUserList.stream().map(DmUser::getDomainId).collect(Collectors.toList());

                    //查询项目私有
                    privateGroupId = privateGroupId.stream().filter(dmGroupId::contains).collect(Collectors.toList());
                    //最终能查看的项目组id
                    canViewId = Stream.concat(privateGroupId.stream(), canViewId.stream()).collect(Collectors.toList());
                }
            }
            //String[] canViewGroupIdList = canViewId.toArray(new String[canViewId.size()]);

            if (canViewId.size()>0){
                Pagination<RepositoryGroupEntity> repositoryGroupPage = repositoryGroupDao.findRepositoryGroupPage(repositoryGroupQuery, canViewId);
                List<RepositoryGroup> repositoryGroups = BeanMapper.mapList(repositoryGroupPage.getDataList(), RepositoryGroup.class);
                joinTemplate.joinQuery(repositoryGroups);
                return PaginationBuilder.build(repositoryGroupPage,repositoryGroups);
            }
        }
        return PaginationBuilder.build(new Pagination<>(),null);
    }

    @Override
    public RepositoryGroup findGroupByName(String groupName) {
        RepositoryGroup repositoryGroup=null;

        List<RepositoryGroupEntity> repositoryByName = repositoryGroupDao.findRepositoryByName(groupName);
        List<RepositoryGroup>  list = BeanMapper.mapList(repositoryByName, RepositoryGroup.class);
        if (CollectionUtils.isNotEmpty(list)){
             repositoryGroup = list.get(0);
        }
        return repositoryGroup;
    }

    @Override
    public List<RepositoryGroup> findAllGroup() {
        List<RepositoryGroupEntity> allGroupEntity = repositoryGroupDao.findAllGroup();
        List<RepositoryGroup>  repositoryGroups = BeanMapper.mapList(allGroupEntity, RepositoryGroup.class);
        return repositoryGroups;
    }

    @Override
    public List<RepositoryGroup> findCanCreateRpyGroup(String userId) {
        List<RepositoryGroupEntity> allGroupEntity = repositoryGroupDao.findCanCreateRpyGroup(userId);
        List<RepositoryGroup>  repositoryGroups = BeanMapper.mapList(allGroupEntity, RepositoryGroup.class);
        return repositoryGroups;
    }

}
























