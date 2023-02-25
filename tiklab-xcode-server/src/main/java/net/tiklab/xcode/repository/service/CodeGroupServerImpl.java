package net.tiklab.xcode.repository.service;

import net.tiklab.beans.BeanMapper;
import net.tiklab.join.JoinTemplate;
import net.tiklab.rpc.annotation.Exporter;
import net.tiklab.xcode.repository.dao.CodeGroupDao;
import net.tiklab.xcode.repository.entity.CodeGroupEntity;
import net.tiklab.xcode.repository.model.CodeGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Exporter
public class CodeGroupServerImpl implements CodeGroupServer {

    @Autowired
    private JoinTemplate joinTemplate;

    @Autowired
    private CodeGroupDao codeGroupDao;


    /**
     * 创建仓库组
     * @param codeGroup 信息
     * @return 仓库组id
     */
    @Override
    public String createCodeGroup(CodeGroup codeGroup) {
        CodeGroupEntity groupEntity = BeanMapper.map(codeGroup, CodeGroupEntity.class);
        return codeGroupDao.createCodeGroup(groupEntity);
    }

    /**
     * 删除仓库组
     * @param codeGroupId 仓库组id
     */
    @Override
    public void deleteCodeGroup(String codeGroupId) {
        codeGroupDao.deleteCodeGroup(codeGroupId);
    }

    /**
     * 更新仓库组
     * @param codeGroup 仓库组信息
     */
    @Override
    public void updateCodeGroup(CodeGroup codeGroup) {
        CodeGroupEntity groupEntity = BeanMapper.map(codeGroup, CodeGroupEntity.class);
        codeGroupDao.updateCodeGroup(groupEntity);
    }

    /**
     * 查询单个仓库组
     * @param codeGroupId 仓库组id
     * @return 仓库组信息
     */
    @Override
    public CodeGroup findOneCodeGroup(String codeGroupId) {
        CodeGroupEntity groupEntity = codeGroupDao.findOneCodeGroup(codeGroupId);
        CodeGroup codeGroup = BeanMapper.map(groupEntity, CodeGroup.class);
        joinTemplate.joinQuery(codeGroup);
        return codeGroup;
    }

    /**
     * 查询所有仓库组
     * @return 仓库组信息列表
     */
    @Override
    public List<CodeGroup> findAllCodeGroup() {
        List<CodeGroupEntity> groupEntityList = codeGroupDao.findAllCodeGroup();
        List<CodeGroup> list = BeanMapper.mapList(groupEntityList, CodeGroup.class);
        joinTemplate.joinQuery(list);
        return list;
    }


    @Override
    public List<CodeGroup> findAllCodeGroupList(List<String> idList) {
        List<CodeGroupEntity> groupEntities = codeGroupDao.findAllCodeGroupList(idList);
        List<CodeGroup> list = BeanMapper.mapList(groupEntities, CodeGroup.class);
        joinTemplate.joinQuery(list);
        return list;
    }

    /**
     * 查询用户仓库组
     * @param userId 用户id
     * @return 仓库组集合
     */
    @Override
    public List<CodeGroup> findUserGroup(String userId) {
        return findAllCodeGroup();
    }
}























