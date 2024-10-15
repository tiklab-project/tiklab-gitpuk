package io.tiklab.gitpuk.repository.service;


import io.tiklab.gitpuk.repository.model.Repository;
import io.tiklab.gitpuk.repository.model.RepositoryGroup;
import io.tiklab.gitpuk.repository.model.RepositoryGroupQuery;
import io.tiklab.gitpuk.common.GitPukFinal;
import io.tiklab.gitpuk.common.GitPukMessageService;
import io.tiklab.gitpuk.common.RepositoryUtil;
import io.tiklab.gitpuk.repository.dao.RepositoryGroupDao;
import io.tiklab.gitpuk.repository.entity.RepositoryGroupEntity;
import io.tiklab.core.page.Pagination;
import io.tiklab.core.page.PaginationBuilder;
import io.tiklab.eam.common.context.LoginContext;
import io.tiklab.privilege.dmRole.model.DmRoleUser;
import io.tiklab.privilege.dmRole.model.DmRoleUserQuery;
import io.tiklab.privilege.dmRole.service.DmRoleService;
import io.tiklab.privilege.dmRole.service.DmRoleUserService;
import io.tiklab.privilege.role.model.PatchUser;
import io.tiklab.privilege.role.model.RoleUser;
import io.tiklab.privilege.role.service.RoleUserService;
import io.tiklab.rpc.annotation.Exporter;
import io.tiklab.toolkit.beans.BeanMapper;
import io.tiklab.toolkit.join.JoinTemplate;
import io.tiklab.user.dmUser.model.DmUser;
import io.tiklab.user.dmUser.model.DmUserQuery;
import io.tiklab.user.dmUser.service.DmUserService;


import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
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
    private RepositoryService repositoryServer;

    @Autowired
    GitPukMessageService gitTokMessageService;

    @Autowired
    RoleUserService roleUserService;

    @Autowired
    private DmRoleUserService dmRoleUserService;

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

        String userId = LoginContext.getLoginId();
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
        dmRoleService.initPatchDmRole(codeGroupId, List);


        //发送消息
        sendMessLog(groupEntity,"create",null);
        return codeGroupId;
    }

    /**
     * 删除仓库组
     * @param codeGroupId 仓库组id
     */
    @Override
    public void deleteCodeGroup(String codeGroupId) {
        RepositoryGroupEntity repositoryGroup = repositoryGroupDao.findRepositoryGroup(codeGroupId);
        repositoryGroupDao.deleteCodeGroup(codeGroupId);

        //发送消息
        sendMessLog(repositoryGroup,"delete",null);
    }

    /**
     * 更新仓库组
     * @param repositoryGroup 仓库组信息
     */
    @Override
    public void updateCodeGroup(RepositoryGroup repositoryGroup) {
        RepositoryGroupEntity group = repositoryGroupDao.findRepositoryGroup(repositoryGroup.getGroupId());
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

        //发送消息
        if (!group.getName().equals(repositoryGroup.getName())){
            sendMessLog(group,"update",repositoryGroup.getName());
        }
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
            //查询可以查看的仓库组
            List<String> canViewIds = findHaveAccessGroup(allGroupList, repositoryGroupQuery.getUserId());
            if (canViewIds.size()>0){
                Pagination<RepositoryGroupEntity> repositoryGroupPage = repositoryGroupDao.findRepositoryGroupPage(repositoryGroupQuery, canViewIds);
                List<RepositoryGroup> repositoryGroups = BeanMapper.mapList(repositoryGroupPage.getDataList(), RepositoryGroup.class);
                //需要查询角色的
                if (("viewableRole").equals(repositoryGroupQuery.getFindType())){
                    for (RepositoryGroup group:repositoryGroups){
                        DmRoleUserQuery dmRoleUserQuery = new DmRoleUserQuery();
                        dmRoleUserQuery.setUserId(repositoryGroupQuery.getUserId());
                        dmRoleUserQuery.setDomainId(group.getGroupId());
                        List<DmRoleUser> dmRoleUserList = dmRoleUserService.findDmRoleUserList(dmRoleUserQuery);
                        if (CollectionUtils.isNotEmpty(dmRoleUserList)){
                            group.setRole(dmRoleUserList.get(0).getRole().getName());
                        }else {
                            group.setRole("普通成员");
                        }
                    }
                }else {
                    joinTemplate.joinQuery(repositoryGroups);
                }
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
        List<RepositoryGroupEntity> groupEntityList = repositoryGroupDao.findAllGroup();
        List<RepositoryGroup> allGroupList = BeanMapper.mapList(groupEntityList, RepositoryGroup.class);

        //查询可以查看的仓库组
        List<String> canViewIds = findHaveAccessGroup(allGroupList, userId);
        String[] strings = new String[canViewIds.size()];
        String[] array = canViewIds.toArray(strings);
        List<RepositoryGroupEntity> groupBuyGroupIds = repositoryGroupDao.findGroupBuyGroupIds(array);
        List<RepositoryGroup> groupList = BeanMapper.mapList(groupBuyGroupIds, RepositoryGroup.class);

        return groupList;
    }



    /**
     * 查询可以访问的仓库组
     * @param allGroupList 操作的仓库组
     * @param userId  用户id
     */
    public List<String> findHaveAccessGroup(List<RepositoryGroup> allGroupList,String userId){
        List<RepositoryGroup> publicGroup = allGroupList.stream().filter(a -> ("public").equals(a.getRules())).collect(Collectors.toList());
        List<String> canViewId = publicGroup.stream().map(RepositoryGroup::getGroupId).collect(Collectors.toList());

        List<RepositoryGroup> privateGroup = allGroupList.stream().filter(a -> ("private").equals(a.getRules())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(privateGroup)){

            //查询用户id 查询关联的项目
            DmUserQuery dmUserQuery = new DmUserQuery();
            dmUserQuery.setUserId(userId);
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
        return canViewId;
    }

    /**
     *操作仓库组发送消息
     * @param groupRepository 操作的仓库组
     * @param type  操作类型
     * @param  updateName 更新名字
     */
    public void sendMessLog(RepositoryGroupEntity groupRepository, String type, String updateName){

        HashMap<String, Object> map = gitTokMessageService.initMessageAndLogMap();

        map.put("groupId",groupRepository.getGroupId());
        map.put("action",groupRepository.getName());
        if (("delete").equals(type)){
            map.put("message", groupRepository.getName());
            map.put("link", GitPukFinal.GROUP_RPY_DELETE);
            gitTokMessageService.deployMessage(map, GitPukFinal.LOG_TYPE_GROUP_DELETE);
            gitTokMessageService.deployLog(map, GitPukFinal.LOG_TYPE_GROUP_DELETE,"repositoryGroup");
        }

        if (("update").equals(type)){
            map.put("message", groupRepository.getName()+"更改为"+updateName);
            map.put("link", GitPukFinal.GROUP_RPY_UPDATE);
            map.put("groupName",groupRepository.getName());
            gitTokMessageService.deployMessage(map, GitPukFinal.LOG_TYPE_GROUP_UPDATE);
            gitTokMessageService.deployLog(map, GitPukFinal.LOG_TYPE_GROUP_UPDATE,"repositoryGroup");
        }

        if (("create").equals(type)){
            map.put("message", groupRepository.getName());
            map.put("link", GitPukFinal.GROUP_RPY_CREATE);
            map.put("groupName",groupRepository.getName());
            gitTokMessageService.deployMessage(map, GitPukFinal.LOG_TYPE_GROUP_CREATE);
            gitTokMessageService.deployLog(map, GitPukFinal.LOG_TYPE_GROUP_CREATE,"repositoryGroup");
        }
    }

}
























