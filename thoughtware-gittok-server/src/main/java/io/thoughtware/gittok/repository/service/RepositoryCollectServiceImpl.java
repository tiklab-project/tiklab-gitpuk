package io.thoughtware.gittok.repository.service;

import io.thoughtware.core.page.Pagination;
import io.thoughtware.core.page.PaginationBuilder;
import io.thoughtware.dal.jpa.criterial.condition.DeleteCondition;
import io.thoughtware.dal.jpa.criterial.conditionbuilder.DeleteBuilders;
import io.thoughtware.gittok.commit.model.Commit;
import io.thoughtware.gittok.commit.model.CommitMessage;
import io.thoughtware.gittok.commit.service.CommitServer;
import io.thoughtware.gittok.common.RepositoryUtil;
import io.thoughtware.gittok.repository.dao.RepositoryCollectDao;
import io.thoughtware.gittok.repository.entity.RepositoryCollectEntity;
import io.thoughtware.gittok.repository.model.RepositoryCollect;
import io.thoughtware.gittok.repository.model.RepositoryCollectQuery;
import io.thoughtware.rpc.annotation.Exporter;
import io.thoughtware.toolkit.beans.BeanMapper;
import io.thoughtware.toolkit.join.JoinTemplate;
import io.thoughtware.user.dmUser.model.DmUser;
import io.thoughtware.user.dmUser.model.DmUserQuery;
import io.thoughtware.user.dmUser.service.DmUserService;
import io.thoughtware.user.user.model.User;
import io.thoughtware.user.user.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
* RepositoryCollectServiceImpl-提交仓库的记录接口实现
*/
@Service
@Exporter
public class RepositoryCollectServiceImpl implements RepositoryCollectService {

    @Autowired
    RepositoryCollectDao repositoryCollectDao;


    @Autowired
    JoinTemplate joinTemplate;

    @Autowired
    private DmUserService dmUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private RepositoryService repositoryServer;

    @Autowired
    CommitServer commitServer;


    @Override
    public String createRepositoryCollect(@NotNull @Valid RepositoryCollect openRecord) {

        RepositoryCollectEntity openRecordEntity = BeanMapper.map(openRecord, RepositoryCollectEntity.class);
        openRecordEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        List<RepositoryCollect> collectList = this.findRepositoryCollectList(new RepositoryCollectQuery().setRepositoryId(openRecord.getRepositoryId()).setUserId(openRecord.getUser().getId()));

        if (CollectionUtils.isNotEmpty(collectList)){
            return null;
        }
        String openRecordId= repositoryCollectDao.createRepositoryCollect(openRecordEntity);
        return openRecordId;
    }

    @Override
    public void updateRepositoryCollect(@NotNull @Valid RepositoryCollect openRecord) {
        RepositoryCollectEntity openRecordEntity = BeanMapper.map(openRecord, RepositoryCollectEntity.class);

        repositoryCollectDao.updateRepositoryCollect(openRecordEntity);
    }

    @Override
    public void deleteRepositoryCollect(@NotNull String id) {
        repositoryCollectDao.deleteRepositoryCollect(id);
    }

    @Override
    public void deleteRepositoryCollectByRepository(String repositoryId) {
        DeleteCondition deleteCondition = DeleteBuilders.createDelete(RepositoryCollectEntity.class)
                .eq("repositoryId", repositoryId)
                .get();
        repositoryCollectDao.deleteRepositoryCollect(deleteCondition);
    }

    @Override
    public void deleteRepositoryCollectByRecord(String repositoryId) {
        DeleteCondition deleteCondition = DeleteBuilders.createDelete(RepositoryCollectEntity.class)
                .eq("repositoryId", repositoryId)
                .get();
        repositoryCollectDao.deleteRepositoryCollect(deleteCondition);
    }

    @Override
    public RepositoryCollect findOne(String id) {
        RepositoryCollectEntity openRecordEntity = repositoryCollectDao.findRepositoryCollect(id);

        RepositoryCollect openRecord = BeanMapper.map(openRecordEntity, RepositoryCollect.class);
        return openRecord;
    }

    @Override
    public List<RepositoryCollect> findList(List<String> idList) {
        List<RepositoryCollectEntity> openRecordEntityList =  repositoryCollectDao.findRepositoryCollectList(idList);

        List<RepositoryCollect> openRecordList =  BeanMapper.mapList(openRecordEntityList, RepositoryCollect.class);
        return openRecordList;
    }

    @Override
    public RepositoryCollect findRepositoryCollect(@NotNull String id) {
        RepositoryCollect openRecord = findOne(id);

        joinTemplate.joinQuery(openRecord);

        return openRecord;
    }

    @Override
    public List<RepositoryCollect> findAllRepositoryCollect() {
        List<RepositoryCollectEntity> openRecordEntityList =  repositoryCollectDao.findAllRepositoryCollect();

        List<RepositoryCollect> openRecordList =  BeanMapper.mapList(openRecordEntityList, RepositoryCollect.class);

        joinTemplate.joinQuery(openRecordList);


        return openRecordList;
    }

    @Override
    public List<RepositoryCollect> findRepositoryCollectList(RepositoryCollectQuery RepositoryCollectQuery) {
        List<RepositoryCollectEntity> openRecordEntityList = repositoryCollectDao.findRepositoryCollectList(RepositoryCollectQuery);

        List<RepositoryCollect> openRecordList = BeanMapper.mapList(openRecordEntityList, RepositoryCollect.class);

        return openRecordList;
    }

    @Override
    public List<RepositoryCollect> findRepositoryCollectList(String[] repositoryIds,String userId) {
        List<RepositoryCollectEntity> openRecordEntityList = repositoryCollectDao.findRepositoryCollectList(repositoryIds,userId);
        List<RepositoryCollect> openRecordList = BeanMapper.mapList(openRecordEntityList, RepositoryCollect.class);

        return openRecordList;
    }


    @Override
    public Pagination<RepositoryCollect> findRepositoryCollectPage(RepositoryCollectQuery RepositoryCollectQuery) {
        Pagination<RepositoryCollectEntity>  pagination = repositoryCollectDao.findRepositoryCollectPage(RepositoryCollectQuery);

        List<RepositoryCollect> openRecordList = BeanMapper.mapList(pagination.getDataList(), RepositoryCollect.class);
        joinTemplate.joinQuery(pagination.getDataList());

        return PaginationBuilder.build(pagination,openRecordList);
    }

    @Override
    public void deleteCollectByRpyId(String repositoryId) {
        DeleteCondition deleteCondition = DeleteBuilders.createDelete(RepositoryCollectEntity.class)
                .eq("repositoryId", repositoryId)
                .get();
        repositoryCollectDao.deleteRepositoryCollect(deleteCondition);
    }

}