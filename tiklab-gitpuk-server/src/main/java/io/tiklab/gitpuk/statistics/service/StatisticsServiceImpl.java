package io.tiklab.gitpuk.statistics.service;

import io.tiklab.core.exception.SystemException;
import io.tiklab.core.page.Pagination;
import io.tiklab.eam.common.context.LoginContext;
import io.tiklab.gitpuk.commit.model.CommitMessage;
import io.tiklab.gitpuk.merge.model.MergeRequest;
import io.tiklab.gitpuk.merge.model.MergeRequestQuery;
import io.tiklab.gitpuk.merge.service.MergeRequestService;
import io.tiklab.gitpuk.common.GitPukYamlDataMaService;
import io.tiklab.gitpuk.common.RepositoryFinal;
import io.tiklab.gitpuk.common.RepositoryUtil;
import io.tiklab.gitpuk.common.git.GitCommitUntil;
import io.tiklab.gitpuk.repository.model.RecordCommit;
import io.tiklab.gitpuk.repository.model.RecordCommitQuery;
import io.tiklab.gitpuk.repository.service.RecordCommitService;
import io.tiklab.gitpuk.repository.service.RepositoryService;
import io.tiklab.gitpuk.statistics.model.RepositoryStatistics;
import io.tiklab.gitpuk.statistics.model.Statistics;
import io.tiklab.gitpuk.statistics.model.StatisticsQuery;
import io.tiklab.gitpuk.statistics.model.UserStatistics;
import io.tiklab.todotask.todo.model.Task;
import io.tiklab.todotask.todo.model.TaskQuery;
import io.tiklab.todotask.todo.service.TaskService;
import io.tiklab.user.dmUser.model.DmUser;
import io.tiklab.user.dmUser.model.DmUserQuery;
import io.tiklab.user.dmUser.service.DmUserService;
import io.tiklab.user.user.model.User;
import io.tiklab.user.user.model.UserQuery;
import io.tiklab.user.user.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.util.io.DisabledOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * CommitStatisticsServiceImpl 提交统计
 */
@Service
public class StatisticsServiceImpl implements StatisticsService{
    private static Logger logger = LoggerFactory.getLogger(StatisticsServiceImpl.class);

    @Autowired
    private GitPukYamlDataMaService yamlDataMaService;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    UserService userService;

    @Autowired
    DmUserService dmUserService;

    @Autowired
    MergeRequestService mergeRequestService;

    @Autowired
    RecordCommitService recordCommitService;

    @Autowired
    TaskService taskService;

    @Override
    public Statistics commitStatics(StatisticsQuery statisticsQuery) {
        try {
            if (!("all").equals(statisticsQuery.getBranchType())&&StringUtils.isEmpty(statisticsQuery.getBranch())){
                logger.info("查询单个仓库提交没有选择分支");
                throw new SystemException(5000,"没有分支");
            }

            Statistics statistics = new Statistics();
            List<Integer> numList = new ArrayList<>();

            //通过仓库id 查询提交数量
            List<RecordCommit> commitList = recordCommitService.findRecordCommits(new RecordCommitQuery().setRepositoryId(statisticsQuery.getRepositoryId()));
            List<RecordCommit> collect = commitList.stream().sorted(Comparator.comparing(RecordCommit::getCommitTime)).collect(Collectors.toList());
            String commitStartDay=null;
            if (CollectionUtils.isNotEmpty(collect)){
                commitStartDay = collect.get(0).getCommitTime().toString();
            }
         /*   //提交数量
            List<Map.Entry<String, Integer>> commitCountByDay = getCommitCountByDay(statisticsQuery);
            String commitStartDay=null;
            if (CollectionUtils.isNotEmpty(commitCountByDay)){
                 commitStartDay = commitCountByDay.get(0).getKey();
            }*/
            //获取时间
            List<String> dayList = getDayList(statisticsQuery, commitStartDay);

            int count=0;
            for (String data:dayList){
                List<RecordCommit> entryList = commitList.stream().filter(a -> a.getCommitTime().toString().startsWith(data)).collect(Collectors.toList());
                //List<Map.Entry<String, Integer>> entryList = commitCountByDay.stream().filter(a -> (data).equals(a.getKey())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(entryList)){
                    //numList.add(entryList.get(0).getValue());
                    //count+=entryList.get(0).getValue();

                    numList.add(entryList.size());
                    count+=entryList.size();
                }else {
                    numList.add(0);
                }
            }
            statistics.setCount(count);
            statistics.setDateList(dayList);
            statistics.setCommitNumList(numList);
            return statistics;
        } catch (Exception e) {
            logger.info("查询单个仓库提交失败："+e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Statistics commitRpyUserStatistics(StatisticsQuery statisticsQuery) {
        Statistics statistics = new Statistics();
        List<Integer> commitCounts;
        List<String> userList;

        int findNum = statisticsQuery.getFindNum();

        List<User> allUser = userService.findUserList(new UserQuery());
        if (CollectionUtils.isEmpty(allUser)){
            logger.info("时间段内所有用户总提交数量统计->没有用户");
           return null;
        }


        //查询时间段内的提交
        List<String> dayList = getDayList(statisticsQuery);
        List<RecordCommit> commitList = recordCommitService.findTimeRecordCommitList(dayList.get(0), dayList.get(dayList.size() - 1),null);
        //时间段内没有提交
        if (CollectionUtils.isEmpty(commitList)) {

            //查询时间段内没有提交的合并请求
            List<User> users = findUserList(allUser, findNum);
            userList = users.stream().map(a -> a.getNickname()).collect(Collectors.toList());

            //添加提交请求数 0
            commitCounts = Stream.generate(() -> 0)
                    .limit(users.size())
                    .collect(Collectors.toList());
        }else {
            //时间段内用户存在 提交
            List<RepositoryStatistics> userStatisticsList = new ArrayList<>();
            for (User user:allUser){
                RepositoryStatistics repositoryStatistics = new RepositoryStatistics();
                repositoryStatistics.setUserName(user.getNickname());

                List<RecordCommit> commits = commitList.stream().filter(a -> user.getId().equals(a.getUserId())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(commitList)){
                    repositoryStatistics.setCommitCount(commits.size());
                }else {
                    repositoryStatistics.setCommitCount(0);
                }
                userStatisticsList.add(repositoryStatistics) ;
            }

            //通过提交数排序
            List<RepositoryStatistics> collected = userStatisticsList.stream()
                    .sorted(Comparator.comparing(RepositoryStatistics::getCommitCount).reversed())
                    .collect(Collectors.toList());

            if (collected.size()>findNum){
                collected = collected.subList(0, findNum);
            }

            userList = collected.stream().map(RepositoryStatistics::getUserName).collect(Collectors.toList());
            commitCounts = collected.stream().map(RepositoryStatistics::getCommitCount).collect(Collectors.toList());

        }
      /*  //获取用户对所有仓库的提交
        Map<String, Integer> userCommitCounts = new HashMap<>();
        for (io.thoughtware.gittok.repository.model.Repository rpy :rpyList){
            try {
                Map<String,Integer> allCommitCount = getAllCommitCount(rpy.getRpyId());
                if (ObjectUtils.isEmpty(allCommitCount)){
                    continue;
                }

                Set<String> commitUserList = allCommitCount.keySet();
                for (String commitUser :commitUserList){
                    List<User> collect = allUser.stream().filter(a -> commitUser.equals(a.getName()) || commitUser.equals(a.getPhone())
                            || commitUser.equals(a.getEmail())||commitUser.equals(a.getNickname())).collect(Collectors.toList());

                    if (CollectionUtils.isEmpty(collect)){
                        continue;
                    }
                    Integer integer = allCommitCount.get(commitUser);
                    Integer integer1 = userCommitCounts.get(commitUser);
                    if (ObjectUtils.isEmpty(integer1)){
                        userCommitCounts.put(commitUser, integer);
                    }else {
                        userCommitCounts.put(commitUser, integer+integer1);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


        List<String> authorNames;
        List<Integer> commitCounts;
        //没有查询到最近7天的提交  直接取6个用户
        if (ObjectUtils.isEmpty(userCommitCounts)){
            authorNames = allUser.stream().map(User::getNickname).collect(Collectors.toList());
            if (allUser.size()>6) {
                authorNames = authorNames.subList(0, 6);
            }
            commitCounts = Stream.generate(() -> 0)
                    .limit(6)
                    .collect(Collectors.toList());
        }else {
            // 根据 value 排序
            List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(userCommitCounts.entrySet());
            sortedEntries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));


            if (sortedEntries.size()>6){
                sortedEntries = sortedEntries.subList(0, 6);
            }

            authorNames=new ArrayList<>();
            commitCounts=new ArrayList<>();
            for (int i=0;i<sortedEntries.size() ;i++) {
                Map.Entry<String, Integer> entry = sortedEntries.get(i);
                authorNames.add(entry.getKey());
                commitCounts.add(entry.getValue());
            }

            if (sortedEntries.size()<6) {
                for (User user : allUser){
                    List<String> stringList = authorNames.stream().filter(a -> a.equals(user.getName()) || a.equals(user.getPhone())
                            || a.equals(user.getEmail())||a.equals(user.getNickname())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(stringList)){
                        authorNames.add(user.getNickname());
                        commitCounts.add(0);
                        if (authorNames.size()>6){
                            break;
                        }
                    }
                }
            }
        }*/
        statistics.setUserList(userList);
        statistics.setCommitNumList(commitCounts);
        return statistics;
    }

    @Override
    public Statistics commitUserStatistics(StatisticsQuery statisticsQuery) {
        Statistics statistics = new Statistics();

        int findNum = statisticsQuery.getFindNum();

        DmUserQuery dmUserQuery = new DmUserQuery();
        dmUserQuery.setDomainId(statisticsQuery.getRepositoryId());
        List<DmUser> dmUserList = dmUserService.findDmUserList(dmUserQuery);
        if (CollectionUtils.isEmpty(dmUserList)){
            logger.info("时间段内所有用户总提交数量统计->没有用户");
            return null;
        }

        List<UserStatistics> statisticsArrayList = new ArrayList<>();
        //查询时间段内的提交
        List<String> dayList = getDayList(statisticsQuery,null);
        List<RecordCommit> commitList = recordCommitService.findTimeRecordCommitList(dayList.get(0), dayList.get(dayList.size() - 1),statisticsQuery.getRepositoryId());
        if (CollectionUtils.isEmpty(commitList)){
            //查询时间段内没有提交的合并请求
            List<DmUser> users = findDmUserList(dmUserList, findNum);
            for (DmUser dmUser:users){
                UserStatistics userStatistics = new UserStatistics();
                userStatistics.setName(dmUser.getUser().getNickname());

                List<Integer> arrayList = new ArrayList<>();
                for (String day:dayList){
                    List<RecordCommit> commits = commitList.stream()
                            .filter(a -> a.getUserId().equals(dmUser.getUser().getId())&&a.getCommitTime().toString().startsWith(day))
                            .collect(Collectors.toList());

                    if (CollectionUtils.isNotEmpty(commits)){
                        arrayList.add(commits.size());
                    }else {
                        arrayList.add(0);
                    }
                }
                userStatistics.setData(arrayList);
                statisticsArrayList.add(userStatistics);
            }

        }else {
            for (DmUser dmUser:dmUserList){
                UserStatistics userStatistics = new UserStatistics();
                userStatistics.setName(dmUser.getUser().getNickname());

                List<Integer> arrayList = new ArrayList<>();
                for (String day:dayList){
                    List<RecordCommit> commits = commitList.stream()
                            .filter(a -> a.getUserId().equals(dmUser.getUser().getId())&&a.getCommitTime().toString().startsWith(day))
                            .collect(Collectors.toList());

                    if (CollectionUtils.isNotEmpty(commits)){
                        arrayList.add(commits.size());
                    }else {
                        arrayList.add(0);
                    }
                }
                userStatistics.setData(arrayList);
                statisticsArrayList.add(userStatistics);
            }
        }
        statistics.setDateList(dayList);
        statistics.setUserStatisticsList(statisticsArrayList);
        return statistics;
    }

    @Override
    public Statistics codesStatistics(StatisticsQuery statisticsQuery) {
        try {
            if (!("all").equals(statisticsQuery.getBranchType())&&StringUtils.isEmpty(statisticsQuery.getBranch())){
                return null;
            }
            Statistics codeCountByDay = getCodeCountByDay(statisticsQuery);
            return codeCountByDay;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Statistics commitRpyStatistics(StatisticsQuery statisticsQuery) {
        Statistics statisticsRes = new Statistics();
        List<String> repositoryNameList;
        List<Integer> commitCounts;

        int findNum = statisticsQuery.getFindNum();

        //查询所有仓库
        List<io.tiklab.gitpuk.repository.model.Repository> rpyList = repositoryService.findRpyList();

        if (CollectionUtils.isEmpty(rpyList)){
            logger.info("时间段内所有仓库提交数量统计没有仓库");
            return null;
        }

        //查询时间段内的提交
        List<String> dayList = getDayList(statisticsQuery);
        List<RecordCommit> commitList = recordCommitService.findTimeRecordCommitList(dayList.get(0), dayList.get(dayList.size() - 1),null);
        //时间段内没有提交
        if (CollectionUtils.isEmpty(commitList)){
            //截取需要展示数量的仓库
            List<io.tiklab.gitpuk.repository.model.Repository> repositoryList = findRepositoryList(rpyList, findNum);
            repositoryNameList=repositoryList.stream().map(a->a.getName()).collect(Collectors.toList());

            //添加提交请求数 0
            commitCounts = Stream.generate(() -> 0)
                    .limit(repositoryList.size())
                    .collect(Collectors.toList());
        }else {
             repositoryNameList=new ArrayList<>();
            commitCounts=new ArrayList<>();
            //时间段内存在 提交
            Map<String, List<RecordCommit>> listMap = commitList.stream().collect(Collectors.groupingBy(a -> a.getRepository().getRpyId()));
            Set<String> rpyIds = listMap.keySet();
            List<RepositoryStatistics> rpyStatisticsList = new ArrayList<>();
            for (String rpyId:rpyIds){
                RepositoryStatistics repositoryStatistics = new RepositoryStatistics();
                List<RecordCommit> recordCommits = listMap.get(rpyId);
                List<io.tiklab.gitpuk.repository.model.Repository> repositories = rpyList.stream().filter(a -> (recordCommits.get(0).getRepository().getRpyId()).equals(a.getRpyId())).collect(Collectors.toList());
                repositoryStatistics.setRepositoryName(repositories.get(0).getName());
                repositoryStatistics.setCommitCount(recordCommits.size());
                rpyStatisticsList.add(repositoryStatistics);
            }


          /*  List<RepositoryStatistics> rpyStatisticsList = new ArrayList<>();
            for (io.tiklab.gitpuk.repository.model.Repository rpy :rpyList){
                RepositoryStatistics repositoryStatistics = new RepositoryStatistics();
                try {
                    repositoryStatistics.setRepositoryName(rpy.getName());
                    //获取仓库提交
                    String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),rpy.getRpyId());
                    List<String> rpyCommitIds = GitCommit.getRpyCommitCount(repositoryAddress,   dayList);

                    if (CollectionUtils.isNotEmpty(rpyCommitIds)){
                        repositoryStatistics.setCommitCount(rpyCommitIds.size());
                    }else {
                        repositoryStatistics.setCommitCount(0);
                    }
                    rpyStatisticsList.add(repositoryStatistics);
                }catch (Exception e){
                    logger.info("时间段内所有仓库提交数量统计失败："+e.getMessage());
                    throw new RuntimeException(e);
                }
            }*/

            //提交的仓库数量大于界面展示数量，截取提交最多的前findNum 条
            if (rpyStatisticsList.size()>findNum){
                rpyStatisticsList = rpyStatisticsList.subList(0, findNum);
            }else {
                //提交的仓库数量不够界面展示的数量，且所有仓库数量大于提交数量
                if (rpyList.size()>rpyStatisticsList.size()){
                    int gapNum = findNum - rpyStatisticsList.size();
                    for (int a=0;a<rpyList.size();a++){
                        if (a>=gapNum){
                            break;
                        }
                        io.tiklab.gitpuk.repository.model.Repository repository = rpyList.get(a);
                        List<RepositoryStatistics> statistics = rpyStatisticsList.stream().filter(b -> repository.getName().equals(b.getRepositoryName())).collect(Collectors.toList());
                        if (CollectionUtils.isEmpty(statistics)){
                            RepositoryStatistics repositoryStatistics = new RepositoryStatistics();
                            repositoryStatistics.setRepositoryName(repository.getName());
                            repositoryStatistics.setCommitCount(0);
                            rpyStatisticsList.add(repositoryStatistics);
                        }
                    }
                }
            }

            //通过提交数排序
            List<RepositoryStatistics> statistics = rpyStatisticsList.stream()
                    .sorted(Comparator.comparing(RepositoryStatistics::getCommitCount).reversed())
                    .collect(Collectors.toList());

            repositoryNameList = statistics.stream().map(RepositoryStatistics::getRepositoryName).collect(Collectors.toList());
            commitCounts = statistics.stream().map(RepositoryStatistics::getCommitCount).collect(Collectors.toList());
        }

        statisticsRes.setCommitNumList(commitCounts);
        statisticsRes.setRepositoryList(repositoryNameList);
        return statisticsRes;
    }

    @Override
    public Statistics mergeReqRpyStatistics(StatisticsQuery statisticsQuery) {
        Statistics statistics = new Statistics();
        List<Integer> mergeCounts;

        int findNum = statisticsQuery.getFindNum();

        //获取时间
        List<String> dayList = getDayList(statisticsQuery);

        //查询所有仓库
        List<io.tiklab.gitpuk.repository.model.Repository> rpyList = repositoryService.findRpyList();
        if (CollectionUtils.isEmpty(rpyList)){
            logger.info("时间段内所有仓库为空");
            return null;
        }
        List<String> repositoryNameList;
        //查询时间段的合并请求
        List<MergeRequest> mergeRequestList = this.findTimeMergeReq(statisticsQuery,dayList);
        if (CollectionUtils.isEmpty(mergeRequestList)){
            //查询时间段内没有提交的合并请求
            List<io.tiklab.gitpuk.repository.model.Repository> repositoryList = findRepositoryList(rpyList, findNum);
            repositoryNameList=repositoryList.stream().map(a->a.getName()).collect(Collectors.toList());

            //添加合并请求数 0
            mergeCounts = Stream.generate(() -> 0)
                    .limit(repositoryList.size())
                    .collect(Collectors.toList());
        }else {
            List<RepositoryStatistics> rpyMergeList = new ArrayList<>();
            //查询时间段内有提交的合并请求
            for (io.tiklab.gitpuk.repository.model.Repository repo : rpyList){
                RepositoryStatistics rpyStatistics = new RepositoryStatistics();

                rpyStatistics.setRepositoryName(repo.getName());
                //过滤出仓库的合并请求数
                List<MergeRequest> mergeRequests = mergeRequestList.stream().
                        filter(a -> repo.getRpyId().equals(a.getRepository().getRpyId())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(mergeRequests)){
                    rpyStatistics.setMergeCount(mergeRequests.size());
                }else {
                    rpyStatistics.setMergeCount(0);
                }
                rpyMergeList.add(rpyStatistics);
            }
            List<RepositoryStatistics> repositoryStatistics = rpyMergeList.stream().sorted(Comparator.comparing(RepositoryStatistics::getMergeCount).reversed()).collect(Collectors.toList());
            if (repositoryStatistics.size()>findNum){
                repositoryStatistics=repositoryStatistics.subList(0,findNum-1);
            }

            repositoryNameList=repositoryStatistics.stream().map(RepositoryStatistics::getRepositoryName).collect(Collectors.toList());
            mergeCounts=repositoryStatistics.stream().map(RepositoryStatistics::getMergeCount).collect(Collectors.toList());

        }


        statistics.setRepositoryList(repositoryNameList);
        statistics.setMergeRequestList(mergeCounts);
        return statistics;
    }

    @Override
    public Statistics mergeReqStatistics(StatisticsQuery statisticsQuery) {
        Statistics statistics = new Statistics();
        //获取时间
        List<String> dayList = getDayList(statisticsQuery, null);

        //查询时间段
        List<MergeRequest> mergeRequestList = this.findTimeMergeReq(statisticsQuery,dayList);
        List<Integer> numList = new ArrayList<>();
        for (String data:dayList){
            List<MergeRequest> mergeRequests = mergeRequestList.stream().filter(a -> a.getCreateTime().toString().startsWith(data)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(mergeRequests)){
                numList.add(mergeRequests.size());
            }else {
                numList.add(0);
            }
        }
        statistics.setMergeRequestList(numList);
        statistics.setDateList(dayList);
        return statistics;
    }

    @Override
    public Statistics mergeReqRpyUserStatistics(StatisticsQuery statisticsQuery) {
        Statistics statistics = new Statistics();
        List<String> userList;
        List<Integer> mergeCounts;

        int findNum = statisticsQuery.getFindNum();

        List<User> allUser = userService.findUserList(new UserQuery());
        if (CollectionUtils.isEmpty(allUser)){
            logger.info("时间段内所有仓库用户的合并请求数->没有用户");
            return null;
        }

        //获取时间
        List<String> dayList = getDayList(statisticsQuery);

        //查询时间段的合并请求
        List<MergeRequest> mergeRequestList = this.findTimeMergeReq(statisticsQuery,dayList);
        if (CollectionUtils.isEmpty(mergeRequestList)){

            //查询时间段内没有提交的合并请求
            List<User> users = findUserList(allUser, findNum);
            userList = users.stream().map(a -> a.getNickname()).collect(Collectors.toList());

            //添加提交请求数 0
            mergeCounts = Stream.generate(() -> 0)
                    .limit(users.size())
                    .collect(Collectors.toList());
        }else {
            //时间段内用户存在 提交
            List<RepositoryStatistics> userStatisticsList = new ArrayList<>();
            for (User user:allUser){
                RepositoryStatistics repositoryStatistics = new RepositoryStatistics();
                repositoryStatistics.setUserName(user.getNickname());

                List<MergeRequest> mergeRequests = mergeRequestList.stream().filter(a -> user.getId().equals(a.getUser().getId())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(mergeRequests)){
                    repositoryStatistics.setMergeCount(mergeRequests.size());
                }else {
                    repositoryStatistics.setMergeCount(0);
                }
                userStatisticsList.add(repositoryStatistics) ;
            }

            //通过提交数排序
            List<RepositoryStatistics> collected = userStatisticsList.stream()
                    .sorted(Comparator.comparing(RepositoryStatistics::getMergeCount).reversed())
                    .collect(Collectors.toList());

            if (collected.size()>findNum){
                collected = collected.subList(0, findNum);
            }

            userList = collected.stream().map(RepositoryStatistics::getUserName).collect(Collectors.toList());
            mergeCounts = collected.stream().map(RepositoryStatistics::getMergeCount).collect(Collectors.toList());
        }

        statistics.setUserList(userList);
        statistics.setMergeRequestList(mergeCounts);
        return statistics;
    }

    @Override
    public Statistics mergeReviewStatistics(StatisticsQuery statisticsQuery) {
        Statistics statistics = new Statistics();

        //获取时间
        List<String> dayList = getDayList(statisticsQuery, null);


        return null;
    }

    @Override
    public Map<String, Integer> statisticsTodoWorkByStatus(HashMap<String, String> params) {
        Map<String, Integer> todoCount = new HashMap<>();
        LinkedHashMap data = new LinkedHashMap();

        String repositoryId = params.get("repositoryId");
        String projectSetId = params.get("projectSetId");
        String sprintId = params.get("sprintId");
        String versionId = params.get("versionId");

        if(!org.springframework.util.StringUtils.isEmpty(repositoryId)){
            data.put("repositoryId", repositoryId);
            todoCount = getTodoStatistics(data);
        }
        if(!org.springframework.util.StringUtils.isEmpty(sprintId)){
            data.put("sprintId", sprintId);
            todoCount = getTodoStatistics(data);
        }
        if(!org.springframework.util.StringUtils.isEmpty(versionId)){
            data.put("versionId", versionId);
            todoCount = getTodoStatistics(data);
        }

        if(org.springframework.util.StringUtils.isEmpty(projectSetId) && org.springframework.util.StringUtils.isEmpty(repositoryId)
                && org.springframework.util.StringUtils.isEmpty(sprintId) && org.springframework.util.StringUtils.isEmpty(versionId)){
            todoCount = getTodoStatistics(data);
        }

 /*       if(!org.springframework.util.StringUtils.isEmpty(projectSetId)){
            ProjectQuery projectQuery = new ProjectQuery();
            projectQuery.setProjectSetId(projectSetId);
            List<Project> projectList = projectService.findProjectList(projectQuery);
            int total = 0;
            int progress = 0;
            int end = 0;
            int overdue = 0;
            for (Project project : projectList) {
                String projectId1 = project.getId();
                data.put("projectId", projectId1);
                todoCount = getTodoStatistics(data);

                Integer total1 = todoCount.get("total");
                total = total + total1;

                Integer progress1 = todoCount.get("progress");
                progress = progress + progress1;

                Integer end1 = todoCount.get("end");
                end = end + end1;

                Integer overdue1 = todoCount.get("overdue");
                overdue = overdue + overdue1;
            }
            todoCount.put("total", total);
            todoCount.put("progress", progress);
            todoCount.put("end", end);
            todoCount.put("overdue", overdue);
        }

       */
        return todoCount;
    }




    /**
     * 获取项目对应分支的提交数量
     * @param statisticsQuery statisticsQuery
     */
    private  List<Map.Entry<String, Integer>> getCommitCountByDay(StatisticsQuery statisticsQuery) throws IOException, GitAPIException {

        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),statisticsQuery.getRepositoryId());
        Git git = Git.open(new File(repositoryAddress));

        Map<String, Integer> commitCountByDay = new HashMap<>();
        RevWalk revWalk = new RevWalk(git.getRepository());
        //所有分支
        if (("all").equals(statisticsQuery.getBranchType())){
            List<Ref> refs = git.branchList().call();
            for (Ref ref : refs) {
                revWalk.markStart(revWalk.parseCommit(ref.getObjectId()));
            }
        }else {
            ObjectId objectId = git.getRepository().findRef(Constants.R_HEADS + statisticsQuery.getBranch()).getObjectId();
            revWalk.markStart(revWalk.parseCommit(objectId));
        }

        for (RevCommit commit : revWalk) {
            PersonIdent author = commit.getAuthorIdent();
            Date date = commit.getAuthorIdent().getWhen();
            if (("all").equals(statisticsQuery.getCommitUser())||(author.getName()).equals(statisticsQuery.getCommitUser())){
                LocalDate commitDate = commit.getAuthorIdent().getWhen().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                String dateStr = commitDate.format(DateTimeFormatter.ISO_LOCAL_DATE);

                commitCountByDay.merge(dateStr, 1, Integer::sum);
            }

        }
        List<Map.Entry<String, Integer>> entryList = commitCountByDay.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()).collect(Collectors.toList());

        return entryList;
    }

    /**
     * 查询用户所有分支的提交数
     * @param rpyId rpyId
     */
    private  Map getAllCommitCount(String rpyId) throws IOException {
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),rpyId);
        Git git = Git.open(new File(repositoryAddress));
        Repository repository = git.getRepository();

        // 获取所有分支引用
        Map<String, Ref> allRefs = repository.getAllRefs();
        // 获取当前时间的 7 天前的时间戳
        long sevenDaysAgo = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(7);

        // 遍历所有分支并统计最近 7 天内每个用户的提交数
        Map<String, Integer> userCommitCounts = new HashMap<>();
        for (Map.Entry<String, Ref> entry : allRefs.entrySet()) {
            if (!entry.getKey().startsWith("refs/heads/")) {
                continue;
            }
            RevWalk revWalk = new RevWalk(repository);
            RevCommit commit = revWalk.parseCommit(entry.getValue().getObjectId());
            Map<String, Integer> commitCountByUser = new HashMap<>();
            while (commit != null && commit.getCommitterIdent().getWhen().getTime() >= sevenDaysAgo) {
                String userName = commit.getAuthorIdent().getName();
                if (!commitCountByUser.containsKey(userName)) {
                    commitCountByUser.put(userName, 0);
                }
                commitCountByUser.put(userName, commitCountByUser.get(userName) + 1);
                if (commit.getParentCount() > 0) {
                    commit = revWalk.parseCommit(commit.getParent(0));
                } else {
                    commit = null;
                }
            }

            for (Map.Entry<String, Integer> userCommit : commitCountByUser.entrySet()) {
                String userName = userCommit.getKey();
                int commitCount = userCommit.getValue();
                if (!userCommitCounts.containsKey(userName)) {
                    userCommitCounts.put(userName, 0);
                }
                userCommitCounts.put(userName, userCommitCounts.get(userName)+commitCount);
            }
        }
        return userCommitCounts;
    }

    /**
     * 查询仓库的代码提交
     * @param statisticsQuery statisticsQuery
     */
    private  Statistics getCodeCountByDay(StatisticsQuery statisticsQuery) throws IOException {
        Statistics statistics = new Statistics();

        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),statisticsQuery.getRepositoryId());
        Git git = Git.open(new File(repositoryAddress));
        Repository repository = git.getRepository();
        // 获取该分支第一次提交信息
        CommitMessage commitMessage = GitCommitUntil.findFistCommit(repository, statisticsQuery.getBranch(), "branch");


        //计算时间
        List<String> dataList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate  endDate = LocalDate.now();
        LocalDate startDate =null;
        if (statisticsQuery.getCellTime()!=0){
            // 减去 天数
            startDate = endDate.minusDays(statisticsQuery.getCellTime());
        }else {
            LocalDate parse = commitMessage.getDateTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            startDate=parse;
        }

        LocalDate startListDate=startDate;
        while (startListDate.isBefore(endDate.plusDays(1))) {
            dataList.add(startListDate.format(formatter));
            startListDate = startListDate.plusDays(1);
        }



        Map<LocalDate, int[]> codeChanges = new HashMap<>();
        ObjectId objectId = git.getRepository().findRef(Constants.R_HEADS + statisticsQuery.getBranch()).getObjectId();
        RevWalk revWalk = new RevWalk(git.getRepository());
        revWalk.markStart(revWalk.parseCommit(objectId));

        int allAddLine=0;
        int allDeleteLine = 0;

        //遍历分支提交
        for (RevCommit commit : revWalk) {
            PersonIdent author = commit.getAuthorIdent();
            if (("all").equals(statisticsQuery.getCommitUser())||(author.getName()).equals(statisticsQuery.getCommitUser())){
                LocalDate commitDate = Instant.ofEpochSecond(commit.getCommitTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                int addLine=0;
                int deleteLine = 0;
                if (commitDate.isEqual(startDate) ||commitDate.isEqual(endDate) ||( commitDate.isAfter(startDate) && commitDate.isBefore(endDate))) {
                    int[] changes = codeChanges.getOrDefault(commitDate, new int[2]);
                    try (DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE)) {
                        diffFormatter.setRepository(repository);
                        diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
                        diffFormatter.setDetectRenames(true);
                        //当前提交的commit和父级的commit的差异文件
                        if (commit.getParentCount()>0){
                            RevTree tree = commit.getTree();
                            List<DiffEntry> diffEntries = diffFormatter.scan(commit.getParent(0).getTree(), commit.getTree());
                            if (CollectionUtils.isNotEmpty(diffEntries)){
                                for (DiffEntry diffEntry :diffEntries) {
                                    DiffEntry.ChangeType changeType = diffEntry.getChangeType();

                                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                                    diffFormatter.format(diffEntry);
                                    String diffContent = out.toString("UTF-8");

                                    // 获取差异的文本
                                    String diffText = formatter.toString();

                                   /* if (changeType.equals(DiffEntry.ChangeType.ADD)){
                                        addLine+=1;
                                    }
                                    if (changeType.equals(DiffEntry.ChangeType.DELETE)){
                                        deleteLine+=1;
                                    }*/


                                    String newPath = diffEntry.getNewPath();
                                    String path ;
                                    if (!newPath.equals(RepositoryFinal.FILE_PATH_NULL)){
                                        path = newPath;
                                    }else {
                                        path = diffEntry.getOldPath();
                                    }

                                    //差异的文件内容
                                    String[] diffLines = GitCommitUntil.findFileChangedList(repository, commit, commit.getParent(0), path);
                                    for (String line : diffLines) {
                                        if (line.startsWith("+++") || line.startsWith("---")){
                                            continue;
                                        }
                                        if (line.startsWith("-")  ){
                                            deleteLine+=1;
                                        }
                                        if (line.startsWith("+")  ){
                                            addLine+=1;
                                        }
                                    }
                                }
                            }
                            changes[0] += addLine;
                            changes[1] += deleteLine;
                        }
                    }
                    codeChanges.put(commitDate, changes);

                    allAddLine+=addLine;
                    allDeleteLine+=deleteLine;
                }
            }
       }

        List<Integer> addList = new ArrayList<>();
        List<Integer> deleteList = new ArrayList<>();
        for (String data: dataList){
            List<LocalDate> localDates = codeChanges.keySet().stream().filter(a -> data.equals(a.toString())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(localDates)){
                int addLine = codeChanges.get(localDates.get(0))[0];
                int deleteLine = codeChanges.get(localDates.get(0))[1];
                addList.add(addLine);
                deleteList.add(deleteLine);
            }else {
                addList.add(0);
                deleteList.add(0);
            }
        }
        statistics.setAddCodeCount(allAddLine);
        statistics.setDeleteCodeCount(allDeleteLine);
        statistics.setDateList(dataList);
        statistics.setAddLine(addList);
        statistics.setDeleteLine(deleteList);
        return statistics;

}



    /**
     * 获取时间list
     * @param statisticsQuery statisticsQuery
     * @param commitStartDay 提交开始时间
     */
    public List<String> getDayList(StatisticsQuery statisticsQuery,String commitStartDay) {
        List<String> dataList = new ArrayList<>();
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();

        // 格式化输出
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDate date;
        if (statisticsQuery.getCellTime()==0){
            //DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
             date = LocalDate.parse(commitStartDay, formatter);
        }else {
            // 减去 天数
             date = currentDate.minusDays(statisticsQuery.getCellTime()-1);
        }

        while (date.isBefore(currentDate.plusDays(1))) {
            dataList.add(date.format(formatter));
            date = date.plusDays(1);
        }
        return dataList;
    }

    /**
     * 获取时间list
     * @param statisticsQuery statisticsQuery
     */
    public List<String> getDayList(StatisticsQuery statisticsQuery) {
        List<String> dataList = new ArrayList<>();
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();

        // 格式化输出
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 减去 天数
        LocalDate date = currentDate.minusDays(statisticsQuery.getFindTime()-1);

        //查询时间为7  查询的为7天内的总和
        if (statisticsQuery.getFindTime()==7){
            while (date.isBefore(currentDate.plusDays(1))) {
                dataList.add(date.format(formatter));
                date = date.plusDays(1);
            }
        }else {
            dataList.add(date.format(formatter));
        }

        //sql 查询的时候 是前闭后开
        LocalDate localDate = date.plusDays(1);
        dataList.add(localDate.format(formatter));
        return dataList;
    }


    /**
     * 获取时间段的提交commitId
     * @param statisticsQuery statisticsQuery
     */
    private  List<String> getCommitIdsInDateRange(StatisticsQuery statisticsQuery) throws IOException, GitAPIException {
        List<String> commitIds = new ArrayList<>();

        LocalDate  endDate = LocalDate.now();
        LocalDate startDate =null;
        if (statisticsQuery.getCellTime()!=0){
            // 减去 天数
            startDate = endDate.minusDays(statisticsQuery.getCellTime());
        }

        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),statisticsQuery.getRepositoryId());
        Git git = Git.open(new File(repositoryAddress));
        Iterable<RevCommit> commits = git.log().call();
        for (RevCommit commit : commits) {
            LocalDate commitDate = Instant.ofEpochSecond(commit.getCommitTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            if (commitDate.isEqual(startDate) ||(commitDate.isAfter(startDate) && commitDate.isBefore(endDate))){
                String name = commit.getName();
                commitIds.add(name);
            }
        }

        return commitIds;
    }

    /**
     * 查询时间段内的合并请求
     * @param statisticsQuery statisticsQuery
     * @param dayList 时间段
     */
    public List<MergeRequest> findTimeMergeReq(StatisticsQuery statisticsQuery,List<String> dayList){
        //查询时间段内的合并请求
        MergeRequestQuery requestQuery = new MergeRequestQuery();
        requestQuery.setRpyId(statisticsQuery.getRepositoryId());
        requestQuery.setStartTime(dayList.get(0));
        requestQuery.setEndTime(dayList.get(dayList.size()-1));
        List<MergeRequest> mergeRequestList = mergeRequestService.findTimeMergeRequestList(requestQuery);
        return mergeRequestList;
    }

    /**
     * 截取对应数量的仓库
     * @param repositoryList repositoryList
     * @param cutOutNum 截取数
     */
    public List<io.tiklab.gitpuk.repository.model.Repository> findRepositoryList(List<io.tiklab.gitpuk.repository.model.Repository> repositoryList, Integer cutOutNum ){
        List<io.tiklab.gitpuk.repository.model.Repository> repositorys=repositoryList;
        if (repositoryList.size()>cutOutNum){
            repositorys=repositoryList.subList(0,cutOutNum-1);
        }
        return repositorys;
    }


    /**
     * 截取对应数量的用户
     * @param allUser allUser
     * @param cutOutNum 截取数
     */
    public List<User> findUserList(List<User> allUser,Integer cutOutNum ){
        List<User> userList=allUser;
        if (userList.size()>cutOutNum){
            userList=userList.subList(0,cutOutNum-1);
        }
        return userList;
    }

    /**
     * 截取对应数量的用户
     * @param dmUserList dmUserList
     * @param cutOutNum 截取数
     */
    public List<DmUser> findDmUserList(List<DmUser> dmUserList,Integer cutOutNum ){
        List<DmUser> dmUsers=dmUserList;
        if (dmUsers.size()>cutOutNum){
            dmUsers=dmUsers.subList(0,cutOutNum-1);
        }
        return dmUsers;
    }

    public Map<String, Integer> getTodoStatistics(LinkedHashMap data){
        Map<String, Integer> todoCount = new HashMap<>();

        String loginId = LoginContext.getLoginId();
        TaskQuery taskQuery = new TaskQuery();
        taskQuery.setData(data);

        taskQuery.setBgroup("gitpuk");
        taskQuery.setAssignUserId(loginId);

        Pagination<Task> taskPage = taskService.findTaskPage(taskQuery);
        todoCount.put("total", taskPage.getTotalRecord());

        taskQuery.setStatus(1);
        taskPage = taskService.findTaskPage(taskQuery);
        todoCount.put("progress", taskPage.getTotalRecord());

        taskQuery.setStatus(2);
        taskPage = taskService.findTaskPage(taskQuery);
        todoCount.put("end", taskPage.getTotalRecord());

        taskQuery.setStatus(1);
        taskQuery.setIsExpire(3);
        taskPage = taskService.findTaskPage(taskQuery);
        todoCount.put("overdue", taskPage.getTotalRecord());

        return todoCount;
    }
}
