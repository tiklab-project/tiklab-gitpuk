package io.tiklab.xcode.scan.service;

import io.tiklab.beans.BeanMapper;
import io.tiklab.core.page.Pagination;
import io.tiklab.core.page.PaginationBuilder;
import io.tiklab.join.JoinTemplate;
import io.tiklab.rpc.annotation.Exporter;
import io.tiklab.xcode.scan.dao.ScanSchemeRuleDao;
import io.tiklab.xcode.scan.entity.ScanSchemeRuleEntity;
import io.tiklab.xcode.scan.model.ScanSchemeRule;
import io.tiklab.xcode.scan.model.ScanSchemeRuleQuery;
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
* ScanSchemeRuleServiceImpl-扫描方案规则集关系
*/
@Service
@Exporter
public class ScanSchemeRuleServiceImpl implements ScanSchemeRuleService {

    @Autowired
    ScanSchemeRuleDao scanSchemeRuleDao;

    @Autowired
    JoinTemplate joinTemplate;

    @Autowired
    ScanRecordService scanRecordService;

    @Override
    public String createScanSchemeRule(@NotNull @Valid ScanSchemeRule openRecord) {

        ScanSchemeRuleEntity openRecordEntity = BeanMapper.map(openRecord, ScanSchemeRuleEntity.class);
        openRecordEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        String openRecordId= scanSchemeRuleDao.createScanSchemeRule(openRecordEntity);

        return openRecordId;
    }

    @Override
    public void updateScanSchemeRule(@NotNull @Valid ScanSchemeRule openRecord) {
        ScanSchemeRuleEntity openRecordEntity = BeanMapper.map(openRecord, ScanSchemeRuleEntity.class);

        scanSchemeRuleDao.updateScanSchemeRule(openRecordEntity);
    }

    @Override
    public void deleteScanSchemeRule(@NotNull String id) {
        scanSchemeRuleDao.deleteScanSchemeRule(id);
    }

    @Override
    public void deleteScanSchemeRuleByCondition(String key, String value) {

    }

    @Override
    public ScanSchemeRule findOne(String id) {
        ScanSchemeRuleEntity openRecordEntity = scanSchemeRuleDao.findScanSchemeRule(id);

        ScanSchemeRule openRecord = BeanMapper.map(openRecordEntity, ScanSchemeRule.class);
        return openRecord;
    }

    @Override
    public List<ScanSchemeRule> findList(List<String> idList) {
        List<ScanSchemeRuleEntity> openRecordEntityList =  scanSchemeRuleDao.findScanSchemeRuleList(idList);

        List<ScanSchemeRule> openRecordList =  BeanMapper.mapList(openRecordEntityList, ScanSchemeRule.class);
        return openRecordList;
    }

    @Override
    public ScanSchemeRule findScanSchemeRule(@NotNull String id) {
        ScanSchemeRule openRecord = findOne(id);

        joinTemplate.joinQuery(openRecord);

        return openRecord;
    }

    @Override
    public List<ScanSchemeRule> findAllScanSchemeRule() {
        List<ScanSchemeRuleEntity> openRecordEntityList =  scanSchemeRuleDao.findAllScanSchemeRule();

        List<ScanSchemeRule> openRecordList =  BeanMapper.mapList(openRecordEntityList, ScanSchemeRule.class);

        joinTemplate.joinQuery(openRecordList);

        return openRecordList;
    }

    @Override
    public List<ScanSchemeRule> findScanSchemeRuleList(ScanSchemeRuleQuery ScanSchemeRuleQuery) {
        List<ScanSchemeRuleEntity> openRecordEntityList = scanSchemeRuleDao.findScanSchemeRuleList(ScanSchemeRuleQuery);

        List<ScanSchemeRule> openRecordList = BeanMapper.mapList(openRecordEntityList, ScanSchemeRule.class);
        joinTemplate.joinQuery(openRecordList);

        return openRecordList;
    }

    @Override
    public Pagination<ScanSchemeRule> findScanSchemeRulePage(ScanSchemeRuleQuery ScanSchemeRuleQuery) {
        Pagination<ScanSchemeRuleEntity>  pagination = scanSchemeRuleDao.findScanSchemeRulePage(ScanSchemeRuleQuery);

        List<ScanSchemeRule> openRecordList = BeanMapper.mapList(pagination.getDataList(), ScanSchemeRule.class);
        joinTemplate.joinQuery(pagination.getDataList());

        return PaginationBuilder.build(pagination,openRecordList);
    }
}