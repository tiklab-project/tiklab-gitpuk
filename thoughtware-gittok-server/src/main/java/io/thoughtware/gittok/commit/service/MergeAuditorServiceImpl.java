package io.thoughtware.gittok.commit.service;

import com.alibaba.fastjson.JSON;
import io.thoughtware.core.exception.ApplicationException;
import io.thoughtware.core.page.Pagination;
import io.thoughtware.core.page.PaginationBuilder;
import io.thoughtware.dal.jpa.criterial.condition.DeleteCondition;
import io.thoughtware.dal.jpa.criterial.conditionbuilder.DeleteBuilders;
import io.thoughtware.eam.common.context.LoginContext;
import io.thoughtware.gittok.commit.dao.MergeAuditorDao;
import io.thoughtware.gittok.commit.entity.MergeAuditorEntity;
import io.thoughtware.gittok.commit.model.MergeAuditor;
import io.thoughtware.gittok.commit.model.MergeAuditorQuery;
import io.thoughtware.gittok.commit.model.MergeCondition;
import io.thoughtware.gittok.commit.model.MergeRequest;
import io.thoughtware.gittok.common.GitTokFinal;
import io.thoughtware.gittok.common.GittokTodoTaskService;
import io.thoughtware.gittok.common.RepositoryFileUtil;
import io.thoughtware.gittok.common.RepositoryUtil;
import io.thoughtware.gittok.repository.model.Repository;
import io.thoughtware.gittok.repository.service.RepositoryService;
import io.thoughtware.todotask.todo.model.Task;
import io.thoughtware.todotask.todo.model.TaskQuery;
import io.thoughtware.todotask.todo.model.TaskType;
import io.thoughtware.todotask.todo.service.TaskByTempService;
import io.thoughtware.todotask.todo.service.TaskService;
import io.thoughtware.toolkit.beans.BeanMapper;
import io.thoughtware.toolkit.join.JoinTemplate;
import io.thoughtware.user.user.model.User;
import io.thoughtware.user.user.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* MergeAuditorServiceImpl-合并请求审核人
*/
@Service
public class MergeAuditorServiceImpl implements MergeAuditorService {

    @Autowired
    MergeAuditorDao mergeAuditorDao;

    @Autowired
    JoinTemplate joinTemplate;

    @Autowired
     MergeConditionService mergeConditionService;

    @Autowired
    UserService userService;

    @Autowired
    GittokTodoTaskService todoTaskService;



    @Override
    @Transactional
    public String createMergeAuditor(@NotNull @Valid  MergeAuditor mergeAuditor) {
        List<MergeAuditor> mergeAuditorList = this.findMergeAuditorList(new MergeAuditorQuery().setMergeRequestId(mergeAuditor.getMergeRequestId())
                .setUserId(mergeAuditor.getUser().getId()));
        //判断是否存在
        if (CollectionUtils.isNotEmpty(mergeAuditorList)){
            return null;
        }
        MergeAuditorEntity mergeAuditorEntity = BeanMapper.map(mergeAuditor, MergeAuditorEntity.class);
        mergeAuditorEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        String mergeAuditorId = mergeAuditorDao.createMergeAuditor(mergeAuditorEntity);

        //添加动态
        createCondition(mergeAuditor,"create");

        //添加代办
        Thread thread = new Thread() {
            public void run() {
                todoTaskService.addBacklog(mergeAuditor);
            }
        };
        thread.start();

        return mergeAuditorId;
    }

    @Override
    public void updateMergeAuditor(@NotNull @Valid MergeAuditor mergeAuditor) {
        //根据合并请求id、用户查询
        if (StringUtils.isEmpty(mergeAuditor.getId())){
            List<MergeAuditor> mergeAuditorList = this.findMergeAuditorList(new MergeAuditorQuery().setMergeRequestId(mergeAuditor.getMergeRequestId())
                    .setUserId(mergeAuditor.getUser().getId()));

            if (CollectionUtils.isNotEmpty(mergeAuditorList)){
                MergeAuditor auditor = mergeAuditorList.get(0);
                auditor.setAuditStatus(mergeAuditor.getAuditStatus());
                mergeAuditor= auditor;
            }
        }
        MergeAuditorEntity mergeAuditorEntity = BeanMapper.map(mergeAuditor, MergeAuditorEntity.class);
        mergeAuditorDao.updateMergeAuditor(mergeAuditorEntity);

        //添加动态
        createCondition(mergeAuditor,null);

        //更新待办代办
        Thread thread = new Thread() {
            public void run() {
            LinkedHashMap<String, Object> content = new LinkedHashMap<>();
            content.put("mergeId", mergeAuditorEntity.getMergeRequestId());
            todoTaskService.updateBacklog(content,"auditor",mergeAuditorEntity.getUserId());
            }
        };
        thread.start();
    }

    @Override
    public void deleteMergeAuditor(@NotNull String id) {
        MergeAuditor mergeAuditor = this.findOne(id);
        //添加动态
        createCondition(mergeAuditor,"delete");

        mergeAuditorDao.deleteMergeAuditor(id);

        //删除待办
        LinkedHashMap<String, Object> content = new LinkedHashMap<>();
        content.put("mergeId", mergeAuditor.getMergeRequestId());
        todoTaskService.removedBacklog(content,mergeAuditor.getUser().getId());

    }

    @Override
    public void deleteMergeAuditorByCondition(String key,String value) {
        DeleteCondition deleteCondition = DeleteBuilders.createDelete(MergeAuditorEntity.class)
                .eq(key, value)
                .get();
        mergeAuditorDao.deleteMergeAuditor(deleteCondition);


    }

    @Override
    public MergeAuditor findOne(String id) {
        MergeAuditorEntity mergeAuditorEntity = mergeAuditorDao.findMergeAuditor(id);

        MergeAuditor mergeAuditor = BeanMapper.map(mergeAuditorEntity, MergeAuditor.class);
        return mergeAuditor;
    }

    @Override
    public List<MergeAuditor> findList(List<String> idList) {
        List<MergeAuditorEntity> mergeAuditorEntityList =  mergeAuditorDao.findMergeAuditorList(idList);

        List<MergeAuditor> mergeAuditorList =  BeanMapper.mapList(mergeAuditorEntityList,MergeAuditor.class);
        return mergeAuditorList;
    }

    @Override
    public MergeAuditor findMergeAuditor(@NotNull String id) {
        MergeAuditor mergeAuditor = findOne(id);

        joinTemplate.joinQuery(mergeAuditor);

        return mergeAuditor;
    }

    @Override
    public List<MergeAuditor> findAllMergeAuditor() {
        List<MergeAuditorEntity> mergeAuditorEntityList =  mergeAuditorDao.findAllMergeAuditor();

        List<MergeAuditor> mergeAuditorList =  BeanMapper.mapList(mergeAuditorEntityList,MergeAuditor.class);

        joinTemplate.joinQuery(mergeAuditorList);

        return mergeAuditorList;
    }

    @Override
    public List<MergeAuditor> findMergeAuditorList(MergeAuditorQuery mergeAuditorQuery) {
        List<MergeAuditorEntity> mergeAuditorEntityList = mergeAuditorDao.findMergeAuditorList(mergeAuditorQuery);

        List<MergeAuditor> mergeAuditorList = BeanMapper.mapList(mergeAuditorEntityList,MergeAuditor.class);

        joinTemplate.joinQuery(mergeAuditorList);

        return mergeAuditorList;
    }

    @Override
    public Pagination<MergeAuditor> findMergeAuditorPage(MergeAuditorQuery mergeAuditorQuery) {

        Pagination<MergeAuditorEntity>  pagination = mergeAuditorDao.findMergeAuditorPage(mergeAuditorQuery);

        List<MergeAuditor> mergeAuditorList = BeanMapper.mapList(pagination.getDataList(),MergeAuditor.class);

        joinTemplate.joinQuery(mergeAuditorList);

        return PaginationBuilder.build(pagination,mergeAuditorList);
    }


    /**
     * 创建合并请求的动态
     * @param mergeAuditor
     * @param  type
     * @return
     */
    public void createCondition(MergeAuditor mergeAuditor,String type){
        MergeCondition mergeCondition = new MergeCondition();

        User auditorUser = userService.findOne(mergeAuditor.getUser().getId());

        //添加评审人
        if (("create").equals(type)){
            mergeCondition.setType(GitTokFinal.MERGE_AUDITOR_ADD);
            mergeCondition.setData(GitTokFinal.MERGE_AUDITOR_ADD_DESC+auditorUser.getName());
        }

        //移除了评审人
        if (("delete").equals(type)){
            mergeCondition.setType(GitTokFinal.MERGE_AUDITOR_REMOVE);
            mergeCondition.setData(GitTokFinal.MERGE_AUDITOR_REMOVE_DESC+auditorUser.getName());
        }

        //认证通过了评审
        if (mergeAuditor.getAuditStatus()==1){
            mergeCondition.setType(GitTokFinal.MERGE_AUDITOR_PASS);
            mergeCondition.setData(GitTokFinal.MERGE_AUDITOR_PASS_DESC);
        }

        //认证未通过评审
        if (mergeAuditor.getAuditStatus()==2){
            mergeCondition.setType(GitTokFinal.MERGE_AUDITOR_NO);
            mergeCondition.setData(GitTokFinal.MERGE_AUDITOR_NO_DESC);
        }

        mergeCondition.setMergeRequestId(mergeAuditor.getMergeRequestId());
        mergeCondition.setRepositoryId(mergeAuditor.getRepositoryId());
        User user = new User();
        user.setId(LoginContext.getLoginId());
        mergeCondition.setUser(user);

        if (org.apache.commons.lang3.StringUtils.isNotEmpty(mergeCondition.getType())){
            mergeConditionService.createMergeCondition(mergeCondition);
        }
    }
}