package io.tiklab.xcode.repository.service;

import io.tiklab.beans.BeanMapper;
import io.tiklab.core.page.Pagination;
import io.tiklab.core.page.PaginationBuilder;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.DeleteBuilders;
import io.tiklab.join.JoinTemplate;
import io.tiklab.rpc.annotation.Exporter;
import io.tiklab.xcode.repository.dao.RecordCommitDao;
import io.tiklab.xcode.repository.entity.RecordCommitEntity;
import io.tiklab.xcode.repository.model.RecordCommit;
import io.tiklab.xcode.repository.model.RecordCommitQuery;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.xml.crypto.Data;
import java.sql.Array;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
* RecordCommitServiceImpl-提交仓库的记录接口实现
*/
@Service
@Exporter
public class RecordCommitServiceImpl implements RecordCommitService {

    @Autowired
    RecordCommitDao recordCommitDao;


    @Autowired
    JoinTemplate joinTemplate;


    @Override
    public String createRecordCommit(@NotNull @Valid RecordCommit openRecord) {

        RecordCommitEntity openRecordEntity = BeanMapper.map(openRecord, RecordCommitEntity.class);
        openRecordEntity.setCommitTime(new Timestamp(System.currentTimeMillis()));
        String openRecordId= recordCommitDao.createRecordCommit(openRecordEntity);
        return openRecordId;
    }

    @Override
    public void updateRecordCommit(@NotNull @Valid RecordCommit openRecord) {
        RecordCommitEntity openRecordEntity = BeanMapper.map(openRecord, RecordCommitEntity.class);

        recordCommitDao.updateRecordCommit(openRecordEntity);
    }

    @Override
    public void deleteRecordCommit(@NotNull String id) {
        recordCommitDao.deleteRecordCommit(id);
    }

    @Override
    public void deleteRecordCommitByRecord(String repositoryId) {
        DeleteCondition deleteCondition = DeleteBuilders.createDelete(RecordCommitEntity.class)
                .eq("repositoryId", repositoryId)
                .get();
        recordCommitDao.deleteRecordCommit(deleteCondition);
    }

    @Override
    public RecordCommit findOne(String id) {
        RecordCommitEntity openRecordEntity = recordCommitDao.findRecordCommit(id);

        RecordCommit openRecord = BeanMapper.map(openRecordEntity, RecordCommit.class);
        return openRecord;
    }

    @Override
    public List<RecordCommit> findList(List<String> idList) {
        List<RecordCommitEntity> openRecordEntityList =  recordCommitDao.findRecordCommitList(idList);

        List<RecordCommit> openRecordList =  BeanMapper.mapList(openRecordEntityList, RecordCommit.class);
        return openRecordList;
    }

    @Override
    public RecordCommit findRecordCommit(@NotNull String id) {
        RecordCommit openRecord = findOne(id);

        joinTemplate.joinQuery(openRecord);

        return openRecord;
    }

    @Override
    public List<RecordCommit> findAllRecordCommit() {
        List<RecordCommitEntity> openRecordEntityList =  recordCommitDao.findAllRecordCommit();

        List<RecordCommit> openRecordList =  BeanMapper.mapList(openRecordEntityList, RecordCommit.class);

        joinTemplate.joinQuery(openRecordList);

        return openRecordList;
    }

    @Override
    public List<RecordCommit> findRecordCommitList(RecordCommitQuery RecordCommitQuery) {
        List<RecordCommitEntity> openRecordEntityList = recordCommitDao.findRecordCommitList(RecordCommitQuery);

        List<RecordCommit> openRecordList = BeanMapper.mapList(openRecordEntityList, RecordCommit.class);

        //排序后根据仓库去重
        List<RecordCommit> recordCommits = openRecordList.stream().sorted(Comparator.comparing(RecordCommit::getCommitTime).reversed()).
                collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(a -> a.getRepository().getRpyId()))), ArrayList::new));

        List<RecordCommit> RecordCommitList = recordCommits.stream().limit(5).collect(Collectors.toList());
        for (RecordCommit recordCommit:RecordCommitList){
            long longTime = System.currentTimeMillis() - recordCommit.getCommitTime().getTime();
            long days = longTime / (24 * 60 * 60 * 1000); // 计算天数
            long hours = (longTime % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000); // 计算小时数
            long minutes = (longTime % (60 * 60 * 1000)) / (60 * 1000); // 计算分钟数

            String badTime=null;
            if (days!=0){
              badTime= days + "天"+ hours + "时前";
            }
            if (days==0&&hours!=0){
                badTime= hours + "时"+ minutes + "分前";
            }
            if (days==0&&hours==0){
                badTime= minutes + "分前";
            }
            recordCommit.setCommitTimeBad(badTime);
        }
        joinTemplate.joinQuery(RecordCommitList);
        return RecordCommitList;
    }

    @Override
    public Pagination<RecordCommit> findRecordCommitPage(RecordCommitQuery RecordCommitQuery) {
        Pagination<RecordCommitEntity>  pagination = recordCommitDao.findRecordCommitPage(RecordCommitQuery);

        List<RecordCommit> openRecordList = BeanMapper.mapList(pagination.getDataList(), RecordCommit.class);
        joinTemplate.joinQuery(pagination.getDataList());

        return PaginationBuilder.build(pagination,openRecordList);
    }
}