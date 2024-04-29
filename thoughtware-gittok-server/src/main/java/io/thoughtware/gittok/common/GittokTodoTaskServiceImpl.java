package io.thoughtware.gittok.common;

import com.alibaba.fastjson.JSON;
import io.thoughtware.eam.common.context.LoginContext;
import io.thoughtware.gittok.commit.model.MergeAuditor;
import io.thoughtware.gittok.commit.model.MergeRequest;
import io.thoughtware.gittok.commit.service.MergeRequestService;
import io.thoughtware.gittok.repository.model.Repository;
import io.thoughtware.gittok.repository.service.RepositoryService;
import io.thoughtware.todotask.todo.model.Task;
import io.thoughtware.todotask.todo.model.TaskQuery;
import io.thoughtware.todotask.todo.model.TaskType;
import io.thoughtware.todotask.todo.service.TaskByTempService;
import io.thoughtware.todotask.todo.service.TaskService;
import io.thoughtware.user.user.model.User;
import io.thoughtware.user.user.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GittokTodoTaskServiceImpl implements GittokTodoTaskService {

    @Autowired
    UserService userService;

    @Autowired
    RepositoryService repositoryServer;


    @Autowired
    MergeRequestService mergeRequestService;


    @Autowired
    TaskByTempService taskByTempService;

    @Autowired
    TaskService taskService;

    @Value("${base.url:null}")
    String baseUrl;

    @Override
    public void addBacklog(MergeAuditor mergeAuditor){
        Task task = new Task();
        task.setBgroup("gittok");
        task.setTitle(mergeAuditor.getMergeRequestId());

        task.setAssignUser(mergeAuditor.getUser());

        TaskType taskType = new TaskType();
        taskType.setId("gittok_auditor");
        task.setTodoType(taskType);

        String createUserId = LoginContext.getLoginId();
        User user = userService.findOne(createUserId);
        task.setCreateUser(user);

        Repository repository = repositoryServer.findOne(mergeAuditor.getRepositoryId());

        MergeRequest mergeRequest = mergeRequestService.findOne(mergeAuditor.getMergeRequestId());

        Map<String, Object> content = new HashMap<>();
        content.put("mergeId", mergeAuditor.getMergeRequestId());
        content.put("repositoryAddress", repository.getAddress());
        content.put("data", mergeRequest.getTitle());

        content.put("createUser", user);
        content.put("createUserIcon",user.getNickname().substring( 0, 1).toUpperCase());
        content.put("receiveTime", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        task.setData(JSON.toJSONString(content));
        task.setBaseUrl(baseUrl);

        task.setAction(mergeAuditor.getMergeRequestId());
        task.setLink("/repository/${repositoryAddress}/mergeAdd/${mergeId}");
        taskByTempService.createTask(task);
    }




    @Override
    public void updateBacklog(LinkedHashMap<String, Object> content,String type,String assignUserId) {
        //通过合并请求的id 和用户ID 查询出待办
        TaskQuery taskQuery = new TaskQuery();
        taskQuery.setData(content);

        if (("auditor").equals(type)){
            taskQuery.setAssignUserId(assignUserId);
        }
        List<Task> taskList = taskService.findTaskList(taskQuery);
        if (CollectionUtils.isNotEmpty(taskList)){
            for (Task task:taskList){
                task.setStatus(2);
                taskService.updateTask(task);
            }
        }
    }


    @Override
    public void removedBacklog(LinkedHashMap<String, Object> content, String assignUserId) {
        TaskQuery taskQuery = new TaskQuery();
        taskQuery.setBgroup("gittok");
        TaskType taskType = new TaskType();
        taskType.setId("gittok_auditor");
        taskQuery.setTodoType(taskType);
        taskQuery.setAssignUserId(assignUserId);
        taskQuery.setData(content);

        taskService.deleteAllTask(taskQuery);
    }
}
