package io.tiklab.gitpuk.common;

import io.tiklab.gitpuk.merge.model.MergeAuditor;

import java.util.LinkedHashMap;

public interface GitPukTodoTaskService {

    /**
     * 添加合并请求审核待办
     * @param mergeAuditor mergeAuditor
     */
    void addBacklog(MergeAuditor mergeAuditor);

    /**
     * 更新待办
     * @param content 待办内容
     * @param type auditor、merge
     * @param assignUserId assignUserId
     */
    void updateBacklog(LinkedHashMap<String, Object> content,String type,String assignUserId);


    /**
     * 移除待办
     * @param content 待办内容
     * @param assignUserId assignUserId
     */
    void removedBacklog(LinkedHashMap<String, Object> content, String assignUserId);


}
