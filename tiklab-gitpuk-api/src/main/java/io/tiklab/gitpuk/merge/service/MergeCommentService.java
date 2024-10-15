package io.tiklab.gitpuk.merge.service;


import io.tiklab.core.page.Pagination;
import io.tiklab.gitpuk.merge.model.MergeComment;
import io.tiklab.gitpuk.merge.model.MergeCommentQuery;
import io.tiklab.toolkit.join.annotation.FindAll;
import io.tiklab.toolkit.join.annotation.FindList;
import io.tiklab.toolkit.join.annotation.FindOne;
import io.tiklab.toolkit.join.annotation.JoinProvider;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* MergeCommentService-合并请求评论
*/
@JoinProvider(model = MergeComment.class)
public interface MergeCommentService {

    /**
    * 创建
    * @param mergeComment
    * @return
    */
    String createMergeComment(@NotNull @Valid MergeComment mergeComment);

    /**
    * 更新
    * @param mergeComment
    */
    void updateMergeComment(@NotNull @Valid MergeComment mergeComment);

    /**
    * 删除
    * @param id
    */
    void deleteMergeComment(@NotNull String id);

    /**
     * 条件删除扫描结果
     * @param  key  删除条件字段
     * @param value
     */
    void deleteMergeCommentByCondition(@NotNull String key,@NotNull String value);

    @FindOne
    MergeComment findOne(@NotNull String id);

    @FindList
    List<MergeComment> findList(List<String> idList);

    /**
    * 查找
    * @param id
    * @return
    */

    MergeComment findMergeComment(@NotNull String id);

    /**
    * 查找所有
    * @return
    */
    @FindAll
    List<MergeComment> findAllMergeComment();

    /**
    * 查询列表
    * @param mergeCommentQuery
    * @return
    */
    List<MergeComment> findMergeCommentList(MergeCommentQuery mergeCommentQuery);

    /**
    * 按分页查询
    * @param mergeCommentQuery
    * @return
    */
    Pagination<MergeComment> findMergeCommentPage(MergeCommentQuery mergeCommentQuery);


}