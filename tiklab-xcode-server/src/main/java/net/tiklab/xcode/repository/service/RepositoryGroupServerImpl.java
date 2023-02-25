package net.tiklab.xcode.repository.service;

import net.tiklab.beans.BeanMapper;
import net.tiklab.join.JoinTemplate;
import net.tiklab.rpc.annotation.Exporter;
import net.tiklab.xcode.repository.dao.RepositoryGroupDao;
import net.tiklab.xcode.repository.entity.RepositoryGroupEntity;
import net.tiklab.xcode.repository.model.RepositoryGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Exporter
public class RepositoryGroupServerImpl implements RepositoryGroupServer {

    @Autowired
    private JoinTemplate joinTemplate;

    @Autowired
    private RepositoryGroupDao repositoryGroupDao;


    /**
     * 创建仓库组
     * @param repositoryGroup 信息
     * @return 仓库组id
     */
    @Override
    public String createCodeGroup(RepositoryGroup repositoryGroup) {
        RepositoryGroupEntity groupEntity = BeanMapper.map(repositoryGroup, RepositoryGroupEntity.class);
        return repositoryGroupDao.createCodeGroup(groupEntity);
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
     * @param userId 用户id
     * @return 仓库组集合
     */
    @Override
    public List<RepositoryGroup> findUserGroup(String userId) {
        return findAllCodeGroup();
    }
}
























