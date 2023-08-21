package io.tiklab.xcode.repository.service;

import io.tiklab.beans.BeanMapper;
import io.tiklab.join.JoinTemplate;
import io.tiklab.rpc.annotation.Exporter;
import io.tiklab.user.dmUser.service.DmUserService;
import io.tiklab.user.user.service.UserService;
import io.tiklab.xcode.repository.dao.ImportAuthDao;
import io.tiklab.xcode.repository.entity.ImportAuthEntity;
import io.tiklab.xcode.repository.model.ImportAuth;
import io.tiklab.xcode.repository.model.ImportAuthQuery;
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
public class ImportAuthServiceImpl implements ImportAuthService {

    @Autowired
    ImportAuthDao importAuthDao;


    @Autowired
    JoinTemplate joinTemplate;

    @Autowired
    private DmUserService dmUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private RepositoryServer repositoryServer;


    @Override
    public String createImportAuth(@NotNull @Valid ImportAuth openRecord) {

        ImportAuthEntity openRecordEntity = BeanMapper.map(openRecord, ImportAuthEntity.class);
        openRecordEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        String openRecordId= importAuthDao.createImportAuth(openRecordEntity);
        return openRecordId;
    }

    @Override
    public void updateImportAuth(@NotNull @Valid ImportAuth openRecord) {
        ImportAuthEntity openRecordEntity = BeanMapper.map(openRecord, ImportAuthEntity.class);

        importAuthDao.updateImportAuth(openRecordEntity);
    }

    @Override
    public void deleteImportAuth(@NotNull String id) {
        importAuthDao.deleteImportAuth(id);
    }



    @Override
    public ImportAuth findOne(String id) {
        ImportAuthEntity openRecordEntity = importAuthDao.findImportAuth(id);

        ImportAuth openRecord = BeanMapper.map(openRecordEntity, ImportAuth.class);
        return openRecord;
    }

    @Override
    public List<ImportAuth> findList(List<String> idList) {
        List<ImportAuthEntity> openRecordEntityList =  importAuthDao.findImportAuthList(idList);

        List<ImportAuth> openRecordList =  BeanMapper.mapList(openRecordEntityList, ImportAuth.class);
        return openRecordList;
    }

    @Override
    public ImportAuth findImportAuth(@NotNull String id) {
        ImportAuth openRecord = findOne(id);

        joinTemplate.joinQuery(openRecord);

        return openRecord;
    }

    @Override
    public List<ImportAuth> findAllImportAuth() {
        List<ImportAuthEntity> openRecordEntityList =  importAuthDao.findAllImportAuth();

        List<ImportAuth> openRecordList =  BeanMapper.mapList(openRecordEntityList, ImportAuth.class);

        joinTemplate.joinQuery(openRecordList);


        return openRecordList;
    }

    @Override
    public List<ImportAuth> findImportAuthList(ImportAuthQuery ImportAuthQuery) {
        List<ImportAuthEntity> openRecordEntityList = importAuthDao.findImportAuthList(ImportAuthQuery);

        List<ImportAuth> openRecordList = BeanMapper.mapList(openRecordEntityList, ImportAuth.class);

        return openRecordList;
    }

}