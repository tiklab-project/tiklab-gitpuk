package io.tiklab.gitpuk.common;

import com.alibaba.fastjson.JSON;
import io.tiklab.eam.common.context.LoginContext;
import io.tiklab.gitpuk.merge.model.MergeAuditor;
import io.tiklab.gitpuk.merge.model.MergeRequest;
import io.tiklab.gitpuk.merge.service.MergeRequestService;
import io.tiklab.gitpuk.repository.model.Repository;
import io.tiklab.gitpuk.repository.service.RepositoryService;
import io.tiklab.todotask.todo.model.Task;
import io.tiklab.todotask.todo.model.TaskQuery;
import io.tiklab.todotask.todo.model.TaskType;
import io.tiklab.todotask.todo.service.TaskByTempService;
import io.tiklab.todotask.todo.service.TaskService;
import io.tiklab.user.user.model.User;
import io.tiklab.user.user.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GitPukTodoTaskServiceImpl implements GitPukTodoTaskService {

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
        task.setBgroup("gitpuk");
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
        taskQuery.setBgroup("gitpuk");
        TaskType taskType = new TaskType();
        taskType.setId("gittok_auditor");
        taskQuery.setTodoType(taskType);
        taskQuery.setAssignUserId(assignUserId);
        taskQuery.setData(content);

        taskService.deleteAllTask(taskQuery);
    }
}
