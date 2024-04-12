package io.thoughtware.gittok.commit.service;


import io.thoughtware.core.page.Pagination;
import io.thoughtware.gittok.commit.model.MergeData;
import io.thoughtware.gittok.commit.model.MergeCondition;
import io.thoughtware.gittok.commit.model.MergeConditionQuery;
import io.thoughtware.toolkit.join.annotation.FindAll;
import io.thoughtware.toolkit.join.annotation.FindList;
import io.thoughtware.toolkit.join.annotation.FindOne;
import io.thoughtware.toolkit.join.annotation.JoinProvider;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
* MergeConditionService-合并请求动态
*/
@JoinProvider(model = MergeCondition.class)
public interface MergeConditionService {

    /**
    * 创建
    * @param mergeCondition
    * @return
    */
    String createMergeCondition(@NotNull @Valid MergeCondition mergeCondition);

    /**
    * 更新
    * @param mergeCondition
    */
    void updateMergeCondition(@NotNull @Valid MergeCondition mergeCondition);

    /**
    * 删除
    * @param id
    */
    void deleteMergeCondition(@NotNull String id);

    /**
     * 条件删除扫描结果
     * @param  key  删除条件字段
     * @param value
     */
    void deleteMergeConditionByCondition(@NotNull String key,@NotNull String value);

    @FindOne
    MergeCondition findOne(@NotNull String id);

    @FindList
    List<MergeCondition> findList(List<String> idList);

    /**
    * 查找
    * @param id
    * @return
    */

    MergeCondition findMergeCondition(@NotNull String id);

    /**
    * 查找所有
    * @return
    */
    @FindAll
    List<MergeCondition> findAllMergeCondition();

    /**
    * 查询列表
    * @param mergeConditionQuery
    * @return
    */
    List<MergeCondition> findMergeConditionList(MergeConditionQuery mergeConditionQuery);

    /**
    * 按分页查询
    * @param mergeConditionQuery
    * @return
    */
    Pagination<MergeCondition> findMergeConditionPage(MergeConditionQuery mergeConditionQuery);


}