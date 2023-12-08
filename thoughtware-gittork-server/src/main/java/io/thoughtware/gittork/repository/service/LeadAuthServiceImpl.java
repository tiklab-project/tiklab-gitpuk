package io.thoughtware.gittork.repository.service;

import io.thoughtware.gittork.repository.dao.LeadAuthDao;
import io.thoughtware.gittork.repository.entity.LeadAuthEntity;
import io.thoughtware.gittork.repository.model.LeadAuth;
import io.thoughtware.gittork.repository.model.LeadAuthQuery;
import io.thoughtware.beans.BeanMapper;
import io.thoughtware.join.JoinTemplate;
import io.thoughtware.rpc.annotation.Exporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

/**
* LeadAuthServiceImpl-导入第三方仓库的认证
*/
@Service
@Exporter
public class LeadAuthServiceImpl implements LeadAuthService {

    @Autowired
    LeadAuthDao leadAuthDao;


    @Autowired
    JoinTemplate joinTemplate;


    @Override
    public String createLeadAuth(@NotNull @Valid LeadAuth openRecord) {

        LeadAuthEntity openRecordEntity = BeanMapper.map(openRecord, LeadAuthEntity.class);
        openRecordEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        String openRecordId= leadAuthDao.createLeadAuth(openRecordEntity);
        return openRecordId;
    }

    @Override
    public void updateLeadAuth(@NotNull @Valid LeadAuth openRecord) {
        LeadAuthEntity openRecordEntity = BeanMapper.map(openRecord, LeadAuthEntity.class);

        leadAuthDao.updateLeadAuth(openRecordEntity);
    }

    @Override
    public void deleteLeadAuth(@NotNull String id) {
        leadAuthDao.deleteLeadAuth(id);
    }



    @Override
    public LeadAuth findOne(String id) {
        LeadAuthEntity openRecordEntity = leadAuthDao.findLeadAuth(id);

        LeadAuth openRecord = BeanMapper.map(openRecordEntity, LeadAuth.class);
        return openRecord;
    }

    @Override
    public List<LeadAuth> findList(List<String> idList) {
        List<LeadAuthEntity> openRecordEntityList =  leadAuthDao.findLeadAuthList(idList);

        List<LeadAuth> openRecordList =  BeanMapper.mapList(openRecordEntityList, LeadAuth.class);
        return openRecordList;
    }

    @Override
    public LeadAuth findLeadAuth(@NotNull String id) {
        LeadAuth openRecord = findOne(id);

        joinTemplate.joinQuery(openRecord);

        return openRecord;
    }

    @Override
    public List<LeadAuth> findAllLeadAuth() {
        List<LeadAuthEntity> openRecordEntityList =  leadAuthDao.findAllLeadAuth();

        List<LeadAuth> openRecordList =  BeanMapper.mapList(openRecordEntityList, LeadAuth.class);

        joinTemplate.joinQuery(openRecordList);


        return openRecordList;
    }

    @Override
    public List<LeadAuth> findLeadAuthList(LeadAuthQuery LeadAuthQuery) {
        List<LeadAuthEntity> openRecordEntityList = leadAuthDao.findLeadAuthList(LeadAuthQuery);

        List<LeadAuth> openRecordList = BeanMapper.mapList(openRecordEntityList, LeadAuth.class);

        return openRecordList;
    }

}