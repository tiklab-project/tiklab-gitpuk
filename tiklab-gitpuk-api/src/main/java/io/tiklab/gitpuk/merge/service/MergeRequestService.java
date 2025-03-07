package io.tiklab.gitpuk.merge.service;


import io.tiklab.core.page.Pagination;
import io.tiklab.gitpuk.merge.model.*;
import io.tiklab.toolkit.join.annotation.FindAll;
import io.tiklab.toolkit.join.annotation.FindList;
import io.tiklab.toolkit.join.annotation.FindOne;
import io.tiklab.toolkit.join.annotation.JoinProvider;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
* MergeRequestService-合并请求提交
*/
@JoinProvider(model = MergeRequest.class)
public interface MergeRequestService {

    /**
    * 创建
    * @param mergeRequest
    * @return
    */
    String createMergeRequest(@NotNull @Valid MergeRequest mergeRequest);

    /**
    * 更新
    * @param mergeRequest
    */
    void updateMergeRequest(@NotNull @Valid MergeRequest mergeRequest);

    /**
    * 删除
    * @param id
    */
    void deleteMergeRequest(@NotNull String id);

    /**
     * 条件删除合并请求
     * @param  key  删除条件字段
     * @param value
     */
    void deleteMergeRequestByCondition(@NotNull String key,@NotNull String value);

    @FindOne
    MergeRequest findOne(@NotNull String id);

    @FindList
    List<MergeRequest> findList(List<String> idList);

    /**
    * 查找
    * @param id
    * @return
    */

    MergeRequest findMergeRequest(@NotNull String id);

    /**
    * 查找所有
    * @return
    */
    @FindAll
    List<MergeRequest> findAllMergeRequest();

    /**
    * 查询列表
    * @param mergeRequestQuery
    * @return
    */
    List<MergeRequest> findMergeRequestList(MergeRequestQuery mergeRequestQuery);

    /**
    * 按分页查询
    * @param mergeRequestQuery
    * @return
    */
    Pagination<MergeRequest> findMergeRequestPage(MergeRequestQuery mergeRequestQuery);


    /**
     * 执行合并
     * @param mergeData
     * @return
     */
    String execMerge(MergeData mergeData);

    /**
     * 查询各状态的合并请求数量
     * @param mergeRequestQuery
     * @return
     */
    Map findMergeStateNum(MergeRequestQuery mergeRequestQuery);

    /**
     * 查询合并冲突文件
     * @param mergeData
     * @return
     */
    List<String> findConflictingFile(MergeData mergeData);

    /**
     * 查询合并冲突文件详情
     * @param mergeData mergeData
     */
    MergeClashFile findConflictingFileDetails(MergeData mergeData);


    /**
     * 查询时间段内的合并请求
     * @param mergeRequestQuery mergeRequestQuery
     */
    List<MergeRequest> findTimeMergeRequestList(MergeRequestQuery mergeRequestQuery);

    /**
     * 在线解决冲突文件
     * @param mergeClashFileSet 修改后的冲突文件集合
     */
    void conflictResolutionFile(MergeClashFileSet mergeClashFileSet);
}