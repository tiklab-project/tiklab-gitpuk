package io.thoughtware.gittok.repository.service;

import io.thoughtware.gittok.repository.dao.LeadRecordDao;
import io.thoughtware.gittok.repository.entity.LeadRecordEntity;
import io.thoughtware.gittok.repository.model.LeadRecord;
import io.thoughtware.gittok.repository.model.LeadRecordQuery;
import io.thoughtware.toolkit.beans.BeanMapper;
import io.thoughtware.dal.jpa.criterial.condition.DeleteCondition;
import io.thoughtware.dal.jpa.criterial.conditionbuilder.DeleteBuilders;
import io.thoughtware.toolkit.join.JoinTemplate;
import io.thoughtware.rpc.annotation.Exporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

/**
* LeadRecordServiceImpl-导入记录
*/
@Service
@Exporter
public class LeadRecordServiceImpl implements LeadRecordService {

    @Autowired
    LeadRecordDao importAuthDao;


    @Autowired
    JoinTemplate joinTemplate;


    @Override
    public String createLeadRecord(@NotNull @Valid LeadRecord leadRecord) {

        LeadRecordEntity leadRecordEntity = BeanMapper.map(leadRecord, LeadRecordEntity.class);
        leadRecordEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        String leadRecordId= importAuthDao.createLeadRecord(leadRecordEntity);
        return leadRecordId;
    }

    @Override
    public void updateLeadRecord(LeadRecord leadRecord) {
        LeadRecordEntity leadRecordEntity = BeanMapper.map(leadRecord, LeadRecordEntity.class);

        importAuthDao.updateLeadRecord(leadRecordEntity);
    }


    @Override
    public void deleteLeadRecord(@NotNull String id) {
        importAuthDao.deleteLeadRecord(id);
    }

    @Override
    public void deleteLeadRecord(String field, String value) {
        DeleteCondition deleteCondition = DeleteBuilders.createDelete(LeadRecordEntity.class)
                .eq(field, value)
                .get();
        importAuthDao.deleteLeadRecord(deleteCondition);
    }


    @Override
    public LeadRecord findOne(String id) {
        LeadRecordEntity leadRecordEntity = importAuthDao.findLeadRecord(id);

        LeadRecord leadRecord = BeanMapper.map(leadRecordEntity, LeadRecord.class);
        return leadRecord;
    }

    @Override
    public List<LeadRecord> findList(List<String> idList) {
        List<LeadRecordEntity> leadRecordEntityList =  importAuthDao.findLeadRecordList(idList);

        List<LeadRecord> leadRecordList =  BeanMapper.mapList(leadRecordEntityList, LeadRecord.class);
        return leadRecordList;
    }

    @Override
    public LeadRecord findLeadRecord(@NotNull String id) {
        LeadRecord leadRecord = findOne(id);

        joinTemplate.joinQuery(leadRecord);

        return leadRecord;
    }

    @Override
    public List<LeadRecord> findAllLeadRecord() {
        List<LeadRecordEntity> leadRecordEntityList =  importAuthDao.findAllLeadRecord();

        List<LeadRecord> leadRecordList =  BeanMapper.mapList(leadRecordEntityList, LeadRecord.class);

        joinTemplate.joinQuery(leadRecordList);


        return leadRecordList;
    }

    @Override
    public List<LeadRecord> findLeadRecordList(LeadRecordQuery LeadRecordQuery) {
        List<LeadRecordEntity> leadRecordEntityList = importAuthDao.findLeadRecordList(LeadRecordQuery);

        List<LeadRecord> leadRecordList = BeanMapper.mapList(leadRecordEntityList, LeadRecord.class);
        joinTemplate.joinQuery(leadRecordList);

        return leadRecordList;
    }

}