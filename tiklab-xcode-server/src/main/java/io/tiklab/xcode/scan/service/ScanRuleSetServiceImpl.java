package io.tiklab.xcode.scan.service;

import io.tiklab.beans.BeanMapper;
import io.tiklab.core.page.Pagination;
import io.tiklab.core.page.PaginationBuilder;
import io.tiklab.join.JoinTemplate;
import io.tiklab.rpc.annotation.Exporter;
import io.tiklab.xcode.scan.dao.ScanRuleSetDao;
import io.tiklab.xcode.scan.entity.ScanRuleSetEntity;
import io.tiklab.xcode.scan.model.ScanRuleSet;
import io.tiklab.xcode.scan.model.ScanRuleSetQuery;
import io.tiklab.xcode.scan.model.ScanRecord;
import io.tiklab.xcode.scan.model.ScanRecordQuery;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

/**
* ScanRuleSetServiceImpl-扫描计划集接口实现
*/
@Service
@Exporter
public class ScanRuleSetServiceImpl implements ScanRuleSetService {

    @Autowired
    ScanRuleSetDao scanRuleSetDao;

    @Autowired
    JoinTemplate joinTemplate;


    @Override
    public String createScanRuleSet(@NotNull @Valid ScanRuleSet openRecord) {

        ScanRuleSetEntity openRecordEntity = BeanMapper.map(openRecord, ScanRuleSetEntity.class);
        openRecordEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        String openRecordId= scanRuleSetDao.createScanRuleSet(openRecordEntity);

        return openRecordId;
    }

    @Override
    public void updateScanRuleSet(@NotNull @Valid ScanRuleSet openRecord) {
        ScanRuleSetEntity openRecordEntity = BeanMapper.map(openRecord, ScanRuleSetEntity.class);

        scanRuleSetDao.updateScanRuleSet(openRecordEntity);
    }

    @Override
    public void deleteScanRuleSet(@NotNull String id) {
        scanRuleSetDao.deleteScanRuleSet(id);
    }

    @Override
    public void deleteScanRuleSetByCondition(String key, String value) {

    }

    @Override
    public ScanRuleSet findOne(String id) {
        ScanRuleSetEntity openRecordEntity = scanRuleSetDao.findScanRuleSet(id);

        ScanRuleSet openRecord = BeanMapper.map(openRecordEntity, ScanRuleSet.class);
        return openRecord;
    }

    @Override
    public List<ScanRuleSet> findList(List<String> idList) {
        List<ScanRuleSetEntity> openRecordEntityList =  scanRuleSetDao.findScanRuleSetList(idList);

        List<ScanRuleSet> openRecordList =  BeanMapper.mapList(openRecordEntityList, ScanRuleSet.class);
        return openRecordList;
    }

    @Override
    public ScanRuleSet findScanRuleSet(@NotNull String id) {
        ScanRuleSet openRecord = findOne(id);

        joinTemplate.joinQuery(openRecord);

        return openRecord;
    }

    @Override
    public List<ScanRuleSet> findAllScanRuleSet() {
        List<ScanRuleSetEntity> openRecordEntityList =  scanRuleSetDao.findAllScanRuleSet();

        List<ScanRuleSet> openRecordList =  BeanMapper.mapList(openRecordEntityList, ScanRuleSet.class);

        joinTemplate.joinQuery(openRecordList);

        return openRecordList;
    }

    @Override
    public List<ScanRuleSet> findScanRuleSetList(ScanRuleSetQuery ScanRuleSetQuery) {
        List<ScanRuleSetEntity> openRecordEntityList = scanRuleSetDao.findScanRuleSetList(ScanRuleSetQuery);

        List<ScanRuleSet> openRecordList = BeanMapper.mapList(openRecordEntityList, ScanRuleSet.class);
        joinTemplate.joinQuery(openRecordList);

        return openRecordList;
    }

    @Override
    public Pagination<ScanRuleSet> findScanRuleSetPage(ScanRuleSetQuery ScanRuleSetQuery) {
        Pagination<ScanRuleSetEntity>  pagination = scanRuleSetDao.findScanRuleSetPage(ScanRuleSetQuery);

        List<ScanRuleSet> openRecordList = BeanMapper.mapList(pagination.getDataList(), ScanRuleSet.class);
        joinTemplate.joinQuery(pagination.getDataList());


        return PaginationBuilder.build(pagination,openRecordList);
    }
}