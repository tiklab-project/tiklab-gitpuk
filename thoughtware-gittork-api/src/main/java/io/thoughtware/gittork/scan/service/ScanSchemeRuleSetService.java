package io.thoughtware.gittork.scan.service;


import io.thoughtware.gittork.scan.model.ScanSchemeRuleSet;
import io.thoughtware.gittork.scan.model.ScanSchemeRuleSetQuery;
import io.thoughtware.core.page.Pagination;
import io.thoughtware.join.annotation.FindAll;
import io.thoughtware.join.annotation.FindList;
import io.thoughtware.join.annotation.FindOne;
import io.thoughtware.join.annotation.JoinProvider;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* ScanSchemeRuleSetService-扫描方案规则集关系
*/
@JoinProvider(model = ScanSchemeRuleSet.class)
public interface ScanSchemeRuleSetService {

    /**
    * 创建
    * @param scanSchemeRuleSet
    * @return
    */
    String createScanSchemeRuleSet(@NotNull @Valid ScanSchemeRuleSet scanSchemeRuleSet);

    /**
    * 更新
    * @param scanSchemeRuleSet
    */
    void updateScanSchemeRuleSet(@NotNull @Valid ScanSchemeRuleSet scanSchemeRuleSet);

    /**
    * 删除
    * @param id
    */
    void deleteScanSchemeRuleSet(@NotNull String id);

    /**
     * 条件删除扫描结果
     * @param  key  删除条件字段
     * @param value
     */
    void deleteScanSchemeRuleSetByCondition(@NotNull String key,@NotNull String value);

    @FindOne
    ScanSchemeRuleSet findOne(@NotNull String id);

    @FindList
    List<ScanSchemeRuleSet> findList(List<String> idList);

    /**
    * 查找
    * @param id
    * @return
    */

    ScanSchemeRuleSet findScanSchemeRuleSet(@NotNull String id);

    /**
    * 查找所有
    * @return
    */
    @FindAll
    List<ScanSchemeRuleSet> findAllScanSchemeRuleSet();

    /**
    * 查询列表
    * @param scanSchemeRuleSetQuery
    * @return
    */
    List<ScanSchemeRuleSet> findScanSchemeRuleSetList(ScanSchemeRuleSetQuery scanSchemeRuleSetQuery);

    /**
    * 按分页查询
    * @param scanSchemeRuleSetQuery
    * @return
    */
    Pagination<ScanSchemeRuleSet> findScanSchemeRuleSetPage(ScanSchemeRuleSetQuery scanSchemeRuleSetQuery);

}