package io.tiklab.xcode.repository.service;

import io.tiklab.beans.BeanMapper;
import io.tiklab.core.page.Pagination;
import io.tiklab.core.page.PaginationBuilder;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.DeleteBuilders;
import io.tiklab.join.JoinTemplate;
import io.tiklab.rpc.annotation.Exporter;
import io.tiklab.xcode.repository.dao.OpenRecordDao;
import io.tiklab.xcode.repository.entity.OpenRecordEntity;
import io.tiklab.xcode.repository.model.OpenRecord;
import io.tiklab.xcode.repository.model.OpenRecordQuery;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

/**
* OpenRecordServiceImpl-打开仓库的记录接口实现
*/
@Service
@Exporter
public class OpenRecordServiceImpl implements OpenRecordService {

    @Autowired
    OpenRecordDao openRecordDao;


    @Autowired
    JoinTemplate joinTemplate;


    @Override
    public String createOpenRecord(@NotNull @Valid OpenRecord openRecord) {

        String openRecordId=null;
        OpenRecordEntity openRecordEntity = BeanMapper.map(openRecord, OpenRecordEntity.class);

        List<OpenRecordEntity> recordList = openRecordDao.findOpenRecordList(new OpenRecordQuery().setUserId(openRecord.getUserId())
                .setRepositoryId(openRecord.getRepositoryId()));
        if (CollectionUtils.isNotEmpty(recordList)){
            OpenRecordEntity record = recordList.get(0);
            record.setNewOpenTime(new Timestamp(System.currentTimeMillis()));
            openRecordDao.updateOpenRecord(record);
            openRecordId=record.getId();
        }else {
            openRecordEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
            openRecordEntity.setNewOpenTime(new Timestamp(System.currentTimeMillis()));
            openRecordId=openRecordDao.createOpenRecord(openRecordEntity);
        }

        return openRecordId;
    }

    @Override
    public void updateOpenRecord(@NotNull @Valid OpenRecord openRecord) {
        OpenRecordEntity openRecordEntity = BeanMapper.map(openRecord, OpenRecordEntity.class);

        openRecordDao.updateOpenRecord(openRecordEntity);
    }

    @Override
    public void deleteOpenRecord(@NotNull String id) {
        openRecordDao.deleteOpenRecord(id);
    }

    @Override
    public void deleteOpenRecordByRecord(String repositoryId) {
        DeleteCondition deleteCondition = DeleteBuilders.createDelete(OpenRecordEntity.class)
                .eq("repositoryId", repositoryId)
                .get();
        openRecordDao.deleteOpenRecord(deleteCondition);
    }

    @Override
    public OpenRecord findOne(String id) {
        OpenRecordEntity openRecordEntity = openRecordDao.findOpenRecord(id);

        OpenRecord openRecord = BeanMapper.map(openRecordEntity, OpenRecord.class);
        return openRecord;
    }

    @Override
    public List<OpenRecord> findList(List<String> idList) {
        List<OpenRecordEntity> openRecordEntityList =  openRecordDao.findOpenRecordList(idList);

        List<OpenRecord> openRecordList =  BeanMapper.mapList(openRecordEntityList,OpenRecord.class);
        return openRecordList;
    }

    @Override
    public OpenRecord findOpenRecord(@NotNull String id) {
        OpenRecord openRecord = findOne(id);

        joinTemplate.joinQuery(openRecord);

        return openRecord;
    }

    @Override
    public List<OpenRecord> findAllOpenRecord() {
        List<OpenRecordEntity> openRecordEntityList =  openRecordDao.findAllOpenRecord();

        List<OpenRecord> openRecordList =  BeanMapper.mapList(openRecordEntityList,OpenRecord.class);

        joinTemplate.joinQuery(openRecordList);

        return openRecordList;
    }

    @Override
    public List<OpenRecord> findOpenRecordList(OpenRecordQuery OpenRecordQuery) {
        List<OpenRecordEntity> openRecordEntityList = openRecordDao.findOpenRecordList(OpenRecordQuery);

        List<OpenRecord> openRecordList = BeanMapper.mapList(openRecordEntityList,OpenRecord.class);

        joinTemplate.joinQuery(openRecordList);

        return openRecordList;
    }

    @Override
    public Pagination<OpenRecord> findOpenRecordPage(OpenRecordQuery OpenRecordQuery) {
        Pagination<OpenRecordEntity>  pagination = openRecordDao.findOpenRecordPage(OpenRecordQuery);

        List<OpenRecord> openRecordList = BeanMapper.mapList(pagination.getDataList(),OpenRecord.class);
        joinTemplate.joinQuery(pagination.getDataList());

        return PaginationBuilder.build(pagination,openRecordList);
    }





}