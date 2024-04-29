package io.thoughtware.gittok.commit.service;

import io.thoughtware.core.exception.ApplicationException;
import io.thoughtware.core.page.Pagination;
import io.thoughtware.core.page.PaginationBuilder;
import io.thoughtware.dal.jpa.criterial.condition.DeleteCondition;
import io.thoughtware.dal.jpa.criterial.conditionbuilder.DeleteBuilders;
import io.thoughtware.gittok.branch.model.Branch;
import io.thoughtware.gittok.branch.service.BranchServer;
import io.thoughtware.gittok.commit.dao.MergeRequestDao;
import io.thoughtware.gittok.commit.entity.MergeRequestEntity;
import io.thoughtware.gittok.commit.model.*;
import io.thoughtware.gittok.common.*;
import io.thoughtware.gittok.common.git.GitBranchUntil;
import io.thoughtware.gittok.common.git.GitCommitUntil;
import io.thoughtware.gittok.repository.model.Repository;
import io.thoughtware.gittok.repository.service.RepositoryService;
import io.thoughtware.toolkit.beans.BeanMapper;
import io.thoughtware.toolkit.join.JoinTemplate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* MergeRequestServiceImpl-合并请求
*/
@Service
public class MergeRequestServiceImpl implements MergeRequestService {
    private static Logger logger = LoggerFactory.getLogger(MergeRequestServiceImpl.class);

    @Autowired
    MergeRequestDao mergeRequestDao;

    @Autowired
    JoinTemplate joinTemplate;

    @Autowired
    private GitTokYamlDataMaService yamlDataMaService;

    @Autowired
    MergeConditionService mergeConditionService;

    @Autowired
    MergeCommitService mergeCommitService;

    @Autowired
    GittokTodoTaskService todoTaskService;

    @Autowired
    MergeAuditorService auditorService;;

    @Autowired
    GitTokMessageService gitTokMessageService;

    @Autowired
    RepositoryService repoService;



    @Override
    @Transactional
    public String createMergeRequest(@NotNull @Valid MergeRequest mergeRequest) {
        MergeRequestEntity mergeRequestEntity = BeanMapper.map(mergeRequest, MergeRequestEntity.class);
        mergeRequestEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        String mergeRequestId = mergeRequestDao.createMergeRequest(mergeRequestEntity);

        mergeRequest.setId(mergeRequestId);
        //添加合并请求动态
        createCondition(mergeRequest);
        //添加消息和日志
        sendMessLog(mergeRequest);
        return mergeRequestId;
    }

    @Override
    @Transactional
    public void updateMergeRequest(@NotNull @Valid MergeRequest mergeRequest) {
        MergeRequestEntity mergeRequestEntity = BeanMapper.map(mergeRequest, MergeRequestEntity.class);

        mergeRequestDao.updateMergeRequest(mergeRequestEntity);

        //添加合并请求动态
        createCondition(mergeRequest);
    }

    @Override
    public void deleteMergeRequest(@NotNull String id) {
        mergeRequestDao.deleteMergeRequest(id);
    }

    @Override
    public void deleteMergeRequestByCondition(String key,String value) {
        DeleteCondition deleteCondition = DeleteBuilders.createDelete(MergeRequestEntity.class)
                .eq(key, value)
                .get();
        mergeRequestDao.deleteMergeRequest(deleteCondition);
    }

    @Override
    public MergeRequest findOne(String id) {
        MergeRequestEntity mergeRequestEntity = mergeRequestDao.findMergeRequest(id);

        MergeRequest mergeRequest = BeanMapper.map(mergeRequestEntity, MergeRequest.class);
        return mergeRequest;
    }

    @Override
    public List<MergeRequest> findList(List<String> idList) {
        List<MergeRequestEntity> mergeRequestEntityList =  mergeRequestDao.findMergeRequestList(idList);

        List<MergeRequest> mergeRequestList =  BeanMapper.mapList(mergeRequestEntityList,MergeRequest.class);
        return mergeRequestList;
    }

    @Override
    public MergeRequest findMergeRequest(@NotNull String id) {
        MergeRequest mergeRequest = findOne(id);

        joinTemplate.joinQuery(mergeRequest);

        //当前合并请求已关闭查询分支合并分支是否删除
        if (ObjectUtils.isNotEmpty(mergeRequest)&&mergeRequest.getMergeState()==3){
            String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),mergeRequest.getRepository().getRpyId());
            try {
                List<Branch> allBranch = GitBranchUntil.findAllBranch(repositoryAddress);
                List<Branch> targetBranch = allBranch.stream().filter(a -> mergeRequest.getMergeTarget().equals(a.getBranchName())).collect(Collectors.toList());
                List<Branch> originBranch = allBranch.stream().filter(a -> mergeRequest.getMergeOrigin().equals(a.getBranchName())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(targetBranch)&&CollectionUtils.isNotEmpty(originBranch)){
                    mergeRequest.setBranchExist(true);
                }else {
                    mergeRequest.setBranchExist(false);
                }
            } catch (Exception e) {
                logger.info("查询关闭状态的合并分支请求详情，获取仓库所有分支失败"+e );
                throw new ApplicationException("查询关闭状态的合并分支请求详情，获取仓库所有分支失败");
            }
        }

        return mergeRequest;
    }

    @Override
    public List<MergeRequest> findAllMergeRequest() {
        List<MergeRequestEntity> mergeRequestEntityList =  mergeRequestDao.findAllMergeRequest();

        List<MergeRequest> mergeRequestList =  BeanMapper.mapList(mergeRequestEntityList,MergeRequest.class);

        joinTemplate.joinQuery(mergeRequestList);

        return mergeRequestList;
    }

    @Override
    public List<MergeRequest> findMergeRequestList(MergeRequestQuery mergeRequestQuery) {
        List<MergeRequestEntity> mergeRequestEntityList = mergeRequestDao.findMergeRequestList(mergeRequestQuery);

        List<MergeRequest> mergeRequestList = BeanMapper.mapList(mergeRequestEntityList,MergeRequest.class);

        joinTemplate.joinQuery(mergeRequestList);

        return mergeRequestList;
    }

    @Override
    public Pagination<MergeRequest> findMergeRequestPage(MergeRequestQuery mergeRequestQuery) {

        Pagination<MergeRequestEntity>  pagination = mergeRequestDao.findMergeRequestPage(mergeRequestQuery);

        List<MergeRequest> mergeRequestList = BeanMapper.mapList(pagination.getDataList(),MergeRequest.class);

        joinTemplate.joinQuery(mergeRequestList);

        if (CollectionUtils.isNotEmpty(mergeRequestList)){
            //查询合并请求是否有冲突
            for (MergeRequest mergeRequest:mergeRequestList){
                if (  mergeRequest.getMergeState()==1){
                    CommitDiffData diffData  = getIsCash(mergeRequest);
                    int clash = ObjectUtils.isNotEmpty(diffData) ? diffData.getClash() : mergeRequest.getIsClash();
                    mergeRequest.setIsClash(clash);
                }
            }
        }

        return PaginationBuilder.build(pagination,mergeRequestList);
    }

    @Override
    public Map findMergeStateNum(MergeRequestQuery mergeRequestQuery) {
        Map<String, Integer> map = new HashMap<>();

        List<MergeRequestEntity> mergeRequestEntityList = mergeRequestDao.findMergeRequestList(mergeRequestQuery);
        if (CollectionUtils.isNotEmpty(mergeRequestEntityList)){
            map.put("allNum",mergeRequestEntityList.size());

            //打开的合并请求
            List<MergeRequestEntity> openMergeList = mergeRequestEntityList.stream().filter(a -> a.getMergeState() == 1).collect(Collectors.toList());
            int openNum = CollectionUtils.isNotEmpty(openMergeList) ? openMergeList.size() : 0;
            map.put("openNum",openNum);

            //已经合并数
            List<MergeRequestEntity> mergeList = mergeRequestEntityList.stream().filter(a -> a.getMergeState() == 2).collect(Collectors.toList());
            int mergeNum = CollectionUtils.isNotEmpty(mergeList) ? mergeList.size() : 0;
            map.put("mergeNum",mergeNum);

            //关闭的合并请求
            List<MergeRequestEntity> closeMergeList = mergeRequestEntityList.stream().filter(a -> a.getMergeState() == 3).collect(Collectors.toList());
            int closeNum = CollectionUtils.isNotEmpty(closeMergeList) ? closeMergeList.size() : 0;
            map.put("closeNum",closeNum);
        }
        return map;
    }


    @Override
    public String execMerge(MergeData mergeData) {
        String rpyId = mergeData.getRpyId();
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),rpyId);

        String result;
        if (("fast").equals(mergeData.getMergeWay())){
            result  = GitBranchUntil.mergeBranchByFast(mergeData, repositoryAddress);
        }else {
            result  = GitBranchUntil.mergeBranch(mergeData, repositoryAddress);
        }

        //合并成功修改合并请求的状态
        if (("ok").equals(result)){
            MergeRequest request = this.findOne(mergeData.getMergeRequestId());
            request.setMergeState(2);
            this.updateMergeRequest(request);

            //添加动态
            MergeRequest mergeRequest = this.findMergeRequest(mergeData.getMergeRequestId());
            mergeRequest.setExecType("complete");
            createCondition(mergeRequest);

            //添加差异提交记录
            addDiffCommit(mergeData );

            //添加代办
            Thread thread = new Thread() {
                public void run() {
                      LinkedHashMap<String, Object> content = new LinkedHashMap<>();
                      content.put("mergeId", mergeData.getMergeRequestId());
                      todoTaskService.updateBacklog(content,"merge",null);
                }
            };
            thread.start();
        }
        return null;
    }

    @Override
    public List<MergeClashFile> findMergeClashFile(MergeData mergeData) {
        String rpyId = mergeData.getRpyId();
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),rpyId);
        List<MergeClashFile> mergeClashFile = GitBranchUntil.getMergeClashFile(mergeData, repositoryAddress);
        return mergeClashFile;
    }

    @Override
    public String findClashFileData(String repositoryId ,String filePath) {

        String rpyPath = yamlDataMaService.repositoryAddress() + "/" + RepositoryFinal.MERGE_REPOSITORY + "/" + repositoryId;

        String path = rpyPath + "/" + filePath;
        try {
            String content = Files.readString(Path.of(path), StandardCharsets.UTF_8);
            return content;
        }catch (Exception e){
            throw new ApplicationException(5000,"文件不存在");
        }
    }



    /**
     * 合并分支后创建差异的commitId
     * @param mergeData
     * @return
     */
    public  void addDiffCommit(MergeData mergeData){
        //合并提交list
        List<CommitMessage> commitMessageList = mergeData.getCommitMessageList();
        List<CommitMessage> messageList = commitMessageList.stream()
                .flatMap(map -> map.getCommitMessageList().stream())
                .collect(Collectors.toList());

        //创建和的提交记录
        MergeCommit mergeCommit = new MergeCommit();
        mergeCommit.setRepositoryId(mergeData.getRpyId());
        mergeCommit.setMergeRequestId(mergeData.getMergeRequestId());
        if (CollectionUtils.isNotEmpty(messageList)){
            for (CommitMessage commitMessage :messageList){
                mergeCommit.setCommitId(commitMessage.getCommitId());
                mergeCommit.setCommitTime(new Timestamp(commitMessage.getDateTime().getTime()));
                mergeCommitService.createMergeCommit(mergeCommit);
            }
        }
    }

    /**
     * 获取两个分支是否有冲突
     * @param mergeRequest
     */
    public CommitDiffData getIsCash(MergeRequest mergeRequest){
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),mergeRequest.getRepository().getRpyId());
        try {
            Git  git = Git.open(new File(repositoryAddress));
            Commit commit = new Commit();
            commit.setFindCommitId(false);
            commit.setTargetBranch(mergeRequest.getMergeTarget());
            commit.setBranch(mergeRequest.getMergeOrigin());
            return   GitCommitUntil.getDiffStatistics(git,commit);
        } catch (IOException|GitAPIException e) {
            logger.error("查询合并请求中的源和目标是否有冲突失败："+e);
            throw new RuntimeException(e);
        }
    }


    /**
     * 创建合并请求的动态
     * @param mergeRequest
     * @return
     */
    public void createCondition(MergeRequest mergeRequest){
        MergeCondition mergeCondition = new MergeCondition();

        //创建合并请求
        if (("create").equals(mergeRequest.getExecType())){
            mergeCondition.setType(GitTokFinal.MERGE_CREATE);
            mergeCondition.setData(GitTokFinal.MERGE_CREATE_DESC);
        }

        //打开合并请求
        if (("open").equals(mergeRequest.getExecType())){
            mergeCondition.setType(GitTokFinal.MERGE_OPEN);
            mergeCondition.setData(GitTokFinal.MERGE_OPEN_DESC);
        }
        //关闭合并请求
        if (("close").equals(mergeRequest.getExecType())){
            mergeCondition.setType(GitTokFinal.MERGE_CLOSE);
            mergeCondition.setData(GitTokFinal.MERGE_CLOSE_DESC);
        }
        //完成合并
        if (("complete").equals(mergeRequest.getExecType())){
            mergeCondition.setType(GitTokFinal.MERGE_COMPLETE);
            mergeCondition.setData(GitTokFinal.MERGE_COMPLETE_DESC);
        }

        mergeCondition.setMergeRequestId(mergeRequest.getId());
        mergeCondition.setRepositoryId(mergeRequest.getRepository().getRpyId());
        mergeCondition.setUser(mergeRequest.getUser());

        if (StringUtils.isNotEmpty(mergeCondition.getType())){
            mergeConditionService.createMergeCondition(mergeCondition);
        }
    }


    /**
     *创建分支添加消息和日志
     * @param mergeRequest 合并请求
     */
    public void sendMessLog(MergeRequest mergeRequest){

        Repository repository = repoService.findOneRpy(mergeRequest.getRepository().getRpyId());

        HashMap<String, Object> map = gitTokMessageService.initMessageAndLogMap();

        map.put("mergeId",mergeRequest.getId());
        map.put("action",mergeRequest.getTitle());
        //创建合并请求
        if (("create").equals(mergeRequest.getExecType())){
            map.put("message", "在仓库"+repository.getName()+"中创建了合并请求"+mergeRequest.getTitle());
            map.put("link",GitTokFinal.MERGE_DATA_PATH);
            map.put("repositoryPath",repository.getAddress());
            map.put("mergeId",mergeRequest.getId());
            gitTokMessageService.deployMessage(map,GitTokFinal.LOG_TYPE_MERGE_CRATE);
            gitTokMessageService.deployLog(map,GitTokFinal.LOG_TYPE_MERGE_CRATE,"merge");
        }
    }


}