package io.tiklab.gitpuk.merge.service;


import io.tiklab.core.page.Pagination;
import io.tiklab.gitpuk.merge.model.MergeAuditor;
import io.tiklab.gitpuk.merge.model.MergeAuditorQuery;
import io.tiklab.toolkit.join.annotation.FindAll;
import io.tiklab.toolkit.join.annotation.FindList;
import io.tiklab.toolkit.join.annotation.FindOne;
import io.tiklab.toolkit.join.annotation.JoinProvider;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* MergeAuditorService-合并请求审核人
*/
@JoinProvider(model = MergeAuditor.class)
public interface MergeAuditorService {

    /**
    * 创建
    * @param mergeAuditor
    * @return
    */
    String createMergeAuditor(@NotNull @Valid MergeAuditor mergeAuditor);

    /**
    * 更新
    * @param mergeAuditor
    */
    void updateMergeAuditor(@NotNull @Valid MergeAuditor mergeAuditor);

    /**
    * 删除
    * @param id
    */
    void deleteMergeAuditor(@NotNull String id);

    /**
     * 条件删除扫描结果
     * @param  key  删除条件字段
     * @param value
     */
    void deleteMergeAuditorByCondition(@NotNull String key,@NotNull String value);

    @FindOne
    MergeAuditor findOne(@NotNull String id);

    @FindList
    List<MergeAuditor> findList(List<String> idList);

    /**
    * 查找
    * @param id
    * @return
    */

    MergeAuditor findMergeAuditor(@NotNull String id);

    /**
    * 查找所有
    * @return
    */
    @FindAll
    List<MergeAuditor> findAllMergeAuditor();

    /**
    * 查询列表
    * @param mergeAuditorQuery
    * @return
    */
    List<MergeAuditor> findMergeAuditorList(MergeAuditorQuery mergeAuditorQuery);

    /**
    * 按分页查询
    * @param mergeAuditorQuery
    * @return
    */
    Pagination<MergeAuditor> findMergeAuditorPage(MergeAuditorQuery mergeAuditorQuery);


}