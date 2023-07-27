package io.tiklab.xcode.repository.service;


import io.tiklab.beans.BeanMapper;
import io.tiklab.core.page.Pagination;
import io.tiklab.core.page.PaginationBuilder;
import io.tiklab.eam.common.context.LoginContext;
import io.tiklab.join.JoinTemplate;
import io.tiklab.privilege.dmRole.service.DmRoleService;
import io.tiklab.rpc.annotation.Exporter;
import io.tiklab.user.dmUser.model.DmUser;
import io.tiklab.user.dmUser.model.DmUserQuery;
import io.tiklab.user.dmUser.service.DmUserService;
import io.tiklab.xcode.repository.dao.RepositoryGroupDao;
import io.tiklab.xcode.repository.entity.RepositoryEntity;
import io.tiklab.xcode.repository.entity.RepositoryGroupEntity;
import io.tiklab.xcode.repository.model.Repository;
import io.tiklab.xcode.repository.model.RepositoryGroup;
import io.tiklab.xcode.repository.model.RepositoryGroupQuery;
import io.tiklab.xcode.util.RepositoryUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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
    /**
     * 创建仓库组
     * @param repositoryGroup 信息
     * @return 仓库组id
     */
    @Override
    public String createCodeGroup(RepositoryGroup repositoryGroup) {
        RepositoryGroupEntity groupEntity = BeanMapper.map(repositoryGroup, RepositoryGroupEntity.class);

        groupEntity.setCreateTime(RepositoryUtil.date(1,new Date()));
        String codeGroupId = repositoryGroupDao.createCodeGroup(groupEntity);
        dmRoleService.initDmRoles(codeGroupId, LoginContext.getLoginId(), "xcode");
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
                String[] canViewGroupIdList = canViewId.toArray(new String[canViewId.size()]);
                if (canViewGroupIdList.length>0){
                    Pagination<RepositoryGroupEntity> repositoryGroupPage = repositoryGroupDao.findRepositoryGroupPage(repositoryGroupQuery, canViewGroupIdList);
                    List<RepositoryGroup> repositoryGroupList = BeanMapper.mapList(repositoryGroupPage.getDataList(),RepositoryGroup.class);
                    joinTemplate.joinQuery(repositoryGroupList);
                    return PaginationBuilder.build(repositoryGroupPage,repositoryGroupList);
                }
            }
        }
        return null;
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
























