package io.thoughtware.gittork.scan.service;

import io.thoughtware.gittork.scan.dao.ScanShemeDao;
import io.thoughtware.gittork.scan.entity.ScanSchemeEntity;
import io.thoughtware.gittork.scan.model.ScanScheme;
import io.thoughtware.gittork.scan.model.ScanSchemeQuery;
import io.thoughtware.beans.BeanMapper;
import io.thoughtware.core.page.Pagination;
import io.thoughtware.core.page.PaginationBuilder;
import io.thoughtware.join.JoinTemplate;
import io.thoughtware.rpc.annotation.Exporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
* ScanSchemeServiceImpl-扫描方案
*/
@Service
@Exporter
public class ScanSchemeServiceImpl implements ScanSchemeService {

    @Autowired
    ScanShemeDao scanSchemeDao;

    @Autowired
    JoinTemplate joinTemplate;

    @Override
    public String createScanScheme(@NotNull @Valid ScanScheme openRecord) {

        ScanSchemeEntity openRecordEntity = BeanMapper.map(openRecord, ScanSchemeEntity.class);
        openRecordEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        String openRecordId= scanSchemeDao.createScanScheme(openRecordEntity);

        return openRecordId;
    }

    @Override
    public void updateScanScheme(@NotNull @Valid ScanScheme openRecord) {
        ScanSchemeEntity openRecordEntity = BeanMapper.map(openRecord, ScanSchemeEntity.class);

        scanSchemeDao.updateScanScheme(openRecordEntity);
    }

    @Override
    public void deleteScanScheme(@NotNull String id) {
        scanSchemeDao.deleteScanScheme(id);
    }

    @Override
    public void deleteScanSchemeByCondition(String key, String value) {

    }

    @Override
    public ScanScheme findOne(String id) {
        ScanSchemeEntity openRecordEntity = scanSchemeDao.findScanScheme(id);

        ScanScheme openRecord = BeanMapper.map(openRecordEntity, ScanScheme.class);
        return openRecord;
    }

    @Override
    public List<ScanScheme> findList(List<String> idList) {
        List<ScanSchemeEntity> openRecordEntityList =  scanSchemeDao.findScanSchemeList(idList);

        List<ScanScheme> openRecordList =  BeanMapper.mapList(openRecordEntityList, ScanScheme.class);
        return openRecordList;
    }

    @Override
    public ScanScheme findScanScheme(@NotNull String id) {
        ScanScheme openRecord = findOne(id);

        joinTemplate.joinQuery(openRecord);

        return openRecord;
    }

    @Override
    public List<ScanScheme> findAllScanScheme() {
        List<ScanSchemeEntity> openRecordEntityList =  scanSchemeDao.findAllScanScheme();

        List<ScanScheme> openRecordList =  BeanMapper.mapList(openRecordEntityList, ScanScheme.class);

        joinTemplate.joinQuery(openRecordList);

        return openRecordList;
    }

    @Override
    public List<ScanScheme> findScanSchemeList(ScanSchemeQuery ScanSchemeQuery) {
        List<ScanSchemeEntity> openRecordEntityList = scanSchemeDao.findScanSchemeList(ScanSchemeQuery);

        List<ScanScheme> openRecordList = BeanMapper.mapList(openRecordEntityList, ScanScheme.class);
        joinTemplate.joinQuery(openRecordList);

        return openRecordList;
    }

    @Override
    public Pagination<ScanScheme> findScanSchemePage(ScanSchemeQuery ScanSchemeQuery) {
        Pagination<ScanSchemeEntity>  pagination = scanSchemeDao.findScanSchemePage(ScanSchemeQuery);

        List<ScanScheme> openRecordList = BeanMapper.mapList(pagination.getDataList(), ScanScheme.class);
        joinTemplate.joinQuery(pagination.getDataList());

       openRecordList = openRecordList.stream().sorted(Comparator.comparing(ScanScheme::getCreateTime).reversed()).collect(Collectors.toList());
        return PaginationBuilder.build(pagination,openRecordList);
    }
}