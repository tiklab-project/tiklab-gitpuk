package io.thoughtware.gittok.commit.service;


import io.thoughtware.core.page.Pagination;
import io.thoughtware.gittok.commit.model.MergeCommit;
import io.thoughtware.gittok.commit.model.MergeCommitQuery;
import io.thoughtware.toolkit.join.annotation.FindAll;
import io.thoughtware.toolkit.join.annotation.FindList;
import io.thoughtware.toolkit.join.annotation.FindOne;
import io.thoughtware.toolkit.join.annotation.JoinProvider;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* MergeCommitService-合并请求的分支差异提交
*/
@JoinProvider(model = MergeCommit.class)
public interface MergeCommitService {

    /**
    * 创建
    * @param mergeCommit
    * @return
    */
    String createMergeCommit(@NotNull @Valid MergeCommit mergeCommit);

    /**
    * 更新
    * @param mergeCommit
    */
    void updateMergeCommit(@NotNull @Valid MergeCommit mergeCommit);

    /**
    * 删除
    * @param id
    */
    void deleteMergeCommit(@NotNull String id);

    /**
     * 条件删除扫描结果
     * @param  key  删除条件字段
     * @param value
     */
    void deleteMergeCommitByCondition(@NotNull String key,@NotNull String value);

    @FindOne
    MergeCommit findOne(@NotNull String id);

    @FindList
    List<MergeCommit> findList(List<String> idList);

    /**
    * 查找
    * @param id
    * @return
    */

    MergeCommit findMergeCommit(@NotNull String id);

    /**
    * 查找所有
    * @return
    */
    @FindAll
    List<MergeCommit> findAllMergeCommit();

    /**
    * 查询列表
    * @param mergeCommitQuery
    * @return
    */
    List<MergeCommit> findMergeCommitList(MergeCommitQuery mergeCommitQuery);

    /**
    * 按分页查询
    * @param mergeCommitQuery
    * @return
    */
    Pagination<MergeCommit> findMergeCommitPage(MergeCommitQuery mergeCommitQuery);


}