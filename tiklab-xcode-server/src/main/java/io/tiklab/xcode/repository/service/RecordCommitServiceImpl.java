package io.tiklab.xcode.repository.service;

import io.tiklab.beans.BeanMapper;
import io.tiklab.core.page.Pagination;
import io.tiklab.core.page.PaginationBuilder;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.DeleteBuilders;
import io.tiklab.join.JoinTemplate;
import io.tiklab.rpc.annotation.Exporter;
import io.tiklab.user.dmUser.model.DmUser;
import io.tiklab.user.dmUser.model.DmUserQuery;
import io.tiklab.user.dmUser.service.DmUserService;
import io.tiklab.user.user.model.User;
import io.tiklab.user.user.service.UserService;
import io.tiklab.xcode.common.RepositoryUtil;
import io.tiklab.xcode.repository.dao.RecordCommitDao;
import io.tiklab.xcode.repository.entity.RecordCommitEntity;
import io.tiklab.xcode.repository.entity.RecordOpenEntity;
import io.tiklab.xcode.repository.model.RecordCommit;
import io.tiklab.xcode.repository.model.RecordCommitQuery;
import io.tiklab.xcode.repository.model.RecordOpen;
import io.tiklab.xcode.repository.model.Repository;
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

    @Autowired
    private DmUserService dmUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private RepositoryServer repositoryServer;


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
    public void deleteRecordCommitByRepository(String repositoryId) {
        DeleteCondition deleteCondition = DeleteBuilders.createDelete(RecordCommitEntity.class)
                .eq("repositoryId", repositoryId)
                .get();
        recordCommitDao.deleteRecordCommit(deleteCondition);
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
        joinTemplate.joinQuery(recordCommits);
        List<RecordCommit> publicRep = recordCommits.stream().filter(a -> ("public").equals(a.getRepository().getRules())).collect(Collectors.toList());

        //查询私有库
        List<RecordCommit> privateRep = recordCommits.stream().filter(a -> ("private").equals(a.getRepository().getRules())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(privateRep)){
            //查询用户id 查询关联的项目
            DmUserQuery dmUserQuery = new DmUserQuery();
            dmUserQuery.setUserId(RecordCommitQuery.getUserId());
            List<DmUser> dmUserList = dmUserService.findDmUserList(dmUserQuery);
            List<RecordCommit> arrayList = new ArrayList<>();
            for (DmUser dmUser:dmUserList){
                List<RecordCommit> repositories = privateRep.stream().filter(a -> dmUser.getDomainId().equals(a.getRepository().getRpyId())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(repositories)){
                    arrayList.add(repositories.get(0));
                }
            }
            publicRep.addAll(arrayList);
        }


        List<RecordCommit> RecordCommitList = publicRep.stream().limit(4).collect(Collectors.toList());

        for (RecordCommit recordCommit:RecordCommitList){

            //计算时间差
            String timedBad = RepositoryUtil.timeBad(recordCommit.getCommitTime().getTime());
            recordCommit.setCommitTimeBad(timedBad);

            String address = recordCommit.getRepository().getAddress();
            String[] split = address.split("/");
            String groupName;
            if (split.length>2){
                 groupName = split[1];
            }else {
                groupName = split[0];
            }
            recordCommit.setGroupName(groupName);
        }

        return RecordCommitList;
    }

    @Override
    public List<RecordCommit> findRecordCommitList(String userId) {
        List<RecordCommitEntity> openRecordEntityList = recordCommitDao.findRecordCommitList(new RecordCommitQuery().setUserId(userId));

        List<RecordCommit> openRecordList = BeanMapper.mapList(openRecordEntityList, RecordCommit.class);

        if (CollectionUtils.isNotEmpty(openRecordList)){
           openRecordList = openRecordList.stream()
                    .distinct()
                    .collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(a->a.getRepository().getRpyId()))), ArrayList::new));

            joinTemplate.joinQuery(openRecordList);
        }

        return openRecordList;
    }

    @Override
    public Pagination<RecordCommit> findRecordCommitPage(RecordCommitQuery RecordCommitQuery) {
        Pagination<RecordCommitEntity>  pagination = recordCommitDao.findRecordCommitPage(RecordCommitQuery);

        List<RecordCommit> openRecordList = BeanMapper.mapList(pagination.getDataList(), RecordCommit.class);
        joinTemplate.joinQuery(pagination.getDataList());

        return PaginationBuilder.build(pagination,openRecordList);
    }

    @Override
    public void updateCommitRecord(String requestURI,String userName) {
        User user = userService.findUserByUsername(userName,null);

        String[] split = requestURI.split("/");
        String groupName=split[2];
        String name=split[3].substring(0,split[3].indexOf(".git"));
        Repository repository = repositoryServer.findRepositoryByAddress(groupName + "/" + name);
        //更新仓库提交时间
        repositoryServer.updateRpy(repository);


        RecordCommit recordCommit = new RecordCommit();
        recordCommit.setRepository(repository);
        recordCommit.setCommitTime(new Timestamp(System.currentTimeMillis()));
        recordCommit.setUserId(user.getId());
        this.createRecordCommit(recordCommit);
        }


}