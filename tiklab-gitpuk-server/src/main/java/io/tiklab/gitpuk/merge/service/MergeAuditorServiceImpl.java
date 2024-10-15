package io.tiklab.gitpuk.merge.service;

import io.tiklab.core.page.Pagination;
import io.tiklab.core.page.PaginationBuilder;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.DeleteBuilders;
import io.tiklab.eam.common.context.LoginContext;
import io.tiklab.gitpuk.merge.model.MergeAuditor;
import io.tiklab.gitpuk.merge.model.MergeAuditorQuery;
import io.tiklab.gitpuk.merge.model.MergeCondition;
import io.tiklab.gitpuk.common.GitPukFinal;
import io.tiklab.gitpuk.common.GitPukTodoTaskService;
import io.tiklab.gitpuk.merge.dao.MergeAuditorDao;
import io.tiklab.gitpuk.merge.entity.MergeAuditorEntity;
import io.tiklab.toolkit.beans.BeanMapper;
import io.tiklab.toolkit.join.JoinTemplate;
import io.tiklab.user.user.model.User;
import io.tiklab.user.user.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.*;

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
    GitPukTodoTaskService todoTaskService;



    @Override
    @Transactional
    public String createMergeAuditor(@NotNull @Valid MergeAuditor mergeAuditor) {
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
            mergeCondition.setType(GitPukFinal.MERGE_AUDITOR_ADD);
            mergeCondition.setData(GitPukFinal.MERGE_AUDITOR_ADD_DESC+auditorUser.getName());
        }

        //移除了评审人
        if (("delete").equals(type)){
            mergeCondition.setType(GitPukFinal.MERGE_AUDITOR_REMOVE);
            mergeCondition.setData(GitPukFinal.MERGE_AUDITOR_REMOVE_DESC+auditorUser.getName());
        }

        //认证通过了评审
        if (mergeAuditor.getAuditStatus()==1){
            mergeCondition.setType(GitPukFinal.MERGE_AUDITOR_PASS);
            mergeCondition.setData(GitPukFinal.MERGE_AUDITOR_PASS_DESC);
        }

        //认证未通过评审
        if (mergeAuditor.getAuditStatus()==2){
            mergeCondition.setType(GitPukFinal.MERGE_AUDITOR_NO);
            mergeCondition.setData(GitPukFinal.MERGE_AUDITOR_NO_DESC);
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