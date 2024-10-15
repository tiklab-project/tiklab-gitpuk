package io.tiklab.gitpuk.repository.service;

import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.DeleteBuilders;
import io.tiklab.gitpuk.common.GitPukYamlDataMaService;
import io.tiklab.gitpuk.common.RepositoryFileUtil;
import io.tiklab.gitpuk.common.RepositoryUtil;
import io.tiklab.gitpuk.repository.dao.RepositoryLfsDao;
import io.tiklab.gitpuk.repository.entity.RepositoryLfsEntity;
import io.tiklab.gitpuk.repository.model.RepositoryLfs;
import io.tiklab.gitpuk.repository.model.RepositoryLfsQuery;
import io.tiklab.rpc.annotation.Exporter;
import io.tiklab.toolkit.beans.BeanMapper;
import io.tiklab.toolkit.join.JoinTemplate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.sql.Timestamp;
import java.util.List;

/**
* RepositoryLfsServiceImpl 仓库lfs文件
*/
@Service
@Exporter
public class RepositoryLfsServiceImpl implements RepositoryLfsService {

    @Autowired
    RepositoryLfsDao repositoryLfsDao;


    @Autowired
    JoinTemplate joinTemplate;

    @Autowired
    GitPukYamlDataMaService yamlDataMaService;

    @Override
    public String createRepositoryLfs(@NotNull @Valid RepositoryLfs openRecord) {

        RepositoryLfsEntity openRecordEntity = BeanMapper.map(openRecord, RepositoryLfsEntity.class);
        openRecordEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        openRecordEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        String openRecordId= repositoryLfsDao.createRepositoryLfs(openRecordEntity);
        return openRecordId;
    }

    @Override
    public void updateRepositoryLfs(@NotNull @Valid RepositoryLfs openRecord) {
        RepositoryLfsEntity openRecordEntity = BeanMapper.map(openRecord, RepositoryLfsEntity.class);
        openRecordEntity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        repositoryLfsDao.updateRepositoryLfs(openRecordEntity);
    }

    @Override
    public void deleteRepositoryLfs(@NotNull String id) {
        RepositoryLfs repositoryLfs = this.findOne(id);

        //修改lfs文件状态未 删除
        if (ObjectUtils.isNotEmpty(repositoryLfs)){
            repositoryLfs.setIsDelete(0);
        }
        updateRepositoryLfs(repositoryLfs);
        //repositoryLfsDao.deleteRepositoryLfs(id);
        //删除lfs文件
        deleteLfsFile(repositoryLfs.getRepositoryId(),repositoryLfs.getOid());
    }

    @Override
    public void deleteRepositoryLfsByRpyId(String repositoryId) {
        DeleteCondition deleteCondition = DeleteBuilders.createDelete(RepositoryLfsEntity.class)
                .eq("repositoryId", repositoryId)
                .get();

        repositoryLfsDao.deleteRepositoryLfs(deleteCondition);

        //删除lfs文件
        deleteLfsFile(repositoryId,null);
    }






    @Override
    public RepositoryLfs findOne(String id) {
        RepositoryLfsEntity openRecordEntity = repositoryLfsDao.findRepositoryLfs(id);

        RepositoryLfs openRecord = BeanMapper.map(openRecordEntity, RepositoryLfs.class);
        return openRecord;
    }

    @Override
    public List<RepositoryLfs> findList(List<String> idList) {
        List<RepositoryLfsEntity> openRecordEntityList =  repositoryLfsDao.findRepositoryLfsList(idList);

        List<RepositoryLfs> openRecordList =  BeanMapper.mapList(openRecordEntityList, RepositoryLfs.class);
        return openRecordList;
    }

    @Override
    public RepositoryLfs findRepositoryLfs(@NotNull String id) {
        RepositoryLfs openRecord = findOne(id);

        joinTemplate.joinQuery(openRecord);

        return openRecord;
    }

    @Override
    public List<RepositoryLfs> findAllRepositoryLfs() {
        List<RepositoryLfsEntity> openRecordEntityList =  repositoryLfsDao.findAllRepositoryLfs();

        List<RepositoryLfs> openRecordList =  BeanMapper.mapList(openRecordEntityList, RepositoryLfs.class);

        joinTemplate.joinQuery(openRecordList);


        return openRecordList;
    }

    @Override
    public List<RepositoryLfs> findRepositoryLfsList(RepositoryLfsQuery RepositoryLfsQuery) {
        List<RepositoryLfsEntity> openRecordEntityList = repositoryLfsDao.findRepositoryLfsList(RepositoryLfsQuery);

        List<RepositoryLfs> openRecordList = BeanMapper.mapList(openRecordEntityList, RepositoryLfs.class);

        return openRecordList;
    }

    public void editRepositoryLfs(RepositoryLfs repositoryLfs){
        RepositoryLfsQuery lfsQuery = new RepositoryLfsQuery();
        lfsQuery.setRepositoryId(repositoryLfs.getRepositoryId());
        lfsQuery.setOid(repositoryLfs.getOid());
        List<RepositoryLfsEntity> openRecordEntityList = repositoryLfsDao.findRepositoryLfsList(lfsQuery);

        if (CollectionUtils.isNotEmpty(openRecordEntityList)){
            RepositoryLfsEntity repositoryLfsEntity = openRecordEntityList.get(0);
            repositoryLfs.setId(repositoryLfsEntity.getId());
            this.updateRepositoryLfs(repositoryLfs);
        }else {
            this.createRepositoryLfs(repositoryLfs);
        }
    }

    /**
     * 删除lfs 文件
     * @param rpyId 仓库id
     */
    public void deleteLfsFile(String rpyId,String oid){
        //删除lfs文件
        String rpyLfsPath = RepositoryUtil.getRpyLfsPath(yamlDataMaService.repositoryAddress(), rpyId);
        File file;
        if (StringUtils.isNotEmpty(oid)){
            file = new File(rpyLfsPath, oid);
        }else {
            file = new File(rpyLfsPath);
        }
        if (!file.exists()){
            return;
        }
        RepositoryFileUtil.deleteFile(file);
    }

}