package io.tiklab.xcode.repository.service;

import io.tiklab.beans.BeanMapper;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.DeleteBuilders;
import io.tiklab.join.JoinTemplate;
import io.tiklab.rpc.annotation.Exporter;
import io.tiklab.xcode.repository.dao.LeadRecordDao;
import io.tiklab.xcode.repository.entity.LeadRecordEntity;
import io.tiklab.xcode.repository.model.LeadRecord;
import io.tiklab.xcode.repository.model.LeadRecordQuery;
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

        return leadRecordList;
    }

}