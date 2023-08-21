package io.tiklab.xcode.repository.service;

import io.tiklab.beans.BeanMapper;
import io.tiklab.join.JoinTemplate;
import io.tiklab.rpc.annotation.Exporter;
import io.tiklab.user.dmUser.service.DmUserService;
import io.tiklab.user.user.service.UserService;
import io.tiklab.xcode.repository.dao.LeadAuthDao;
import io.tiklab.xcode.repository.entity.LeadAuthEntity;
import io.tiklab.xcode.repository.model.LeadAuth;
import io.tiklab.xcode.repository.model.LeadAuthQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

/**
* ImportAuthServiceImpl-导入第三方仓库的认证
*/
@Service
@Exporter
public class LeadAuthServiceImpl implements LeadAuthService {

    @Autowired
    LeadAuthDao importAuthDao;


    @Autowired
    JoinTemplate joinTemplate;

    @Autowired
    private DmUserService dmUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private RepositoryServer repositoryServer;


    @Override
    public String createImportAuth(@NotNull @Valid LeadAuth openRecord) {

        LeadAuthEntity openRecordEntity = BeanMapper.map(openRecord, LeadAuthEntity.class);
        openRecordEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        String openRecordId= importAuthDao.createImportAuth(openRecordEntity);
        return openRecordId;
    }

    @Override
    public void updateImportAuth(@NotNull @Valid LeadAuth openRecord) {
        LeadAuthEntity openRecordEntity = BeanMapper.map(openRecord, LeadAuthEntity.class);

        importAuthDao.updateImportAuth(openRecordEntity);
    }

    @Override
    public void deleteImportAuth(@NotNull String id) {
        importAuthDao.deleteImportAuth(id);
    }



    @Override
    public LeadAuth findOne(String id) {
        LeadAuthEntity openRecordEntity = importAuthDao.findImportAuth(id);

        LeadAuth openRecord = BeanMapper.map(openRecordEntity, LeadAuth.class);
        return openRecord;
    }

    @Override
    public List<LeadAuth> findList(List<String> idList) {
        List<LeadAuthEntity> openRecordEntityList =  importAuthDao.findImportAuthList(idList);

        List<LeadAuth> openRecordList =  BeanMapper.mapList(openRecordEntityList, LeadAuth.class);
        return openRecordList;
    }

    @Override
    public LeadAuth findImportAuth(@NotNull String id) {
        LeadAuth openRecord = findOne(id);

        joinTemplate.joinQuery(openRecord);

        return openRecord;
    }

    @Override
    public List<LeadAuth> findAllImportAuth() {
        List<LeadAuthEntity> openRecordEntityList =  importAuthDao.findAllImportAuth();

        List<LeadAuth> openRecordList =  BeanMapper.mapList(openRecordEntityList, LeadAuth.class);

        joinTemplate.joinQuery(openRecordList);


        return openRecordList;
    }

    @Override
    public List<LeadAuth> findImportAuthList(LeadAuthQuery ImportAuthQuery) {
        List<LeadAuthEntity> openRecordEntityList = importAuthDao.findImportAuthList(ImportAuthQuery);

        List<LeadAuth> openRecordList = BeanMapper.mapList(openRecordEntityList, LeadAuth.class);

        return openRecordList;
    }

}