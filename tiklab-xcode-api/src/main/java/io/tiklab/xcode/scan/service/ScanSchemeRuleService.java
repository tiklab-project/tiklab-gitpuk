package io.tiklab.xcode.scan.service;


import io.tiklab.core.page.Pagination;
import io.tiklab.join.annotation.FindAll;
import io.tiklab.join.annotation.FindList;
import io.tiklab.join.annotation.FindOne;
import io.tiklab.join.annotation.JoinProvider;
import io.tiklab.xcode.scan.model.ScanSchemeRule;
import io.tiklab.xcode.scan.model.ScanSchemeRuleQuery;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* ScanSchemeRuleService-扫描方案规则关系
*/
@JoinProvider(model = ScanSchemeRule.class)
public interface ScanSchemeRuleService {

    /**
    * 创建
    * @param scanSchemeRule
    * @return
    */
    String createScanSchemeRule(@NotNull @Valid ScanSchemeRule scanSchemeRule);

    /**
    * 更新
    * @param scanSchemeRule
    */
    void updateScanSchemeRule(@NotNull @Valid ScanSchemeRule scanSchemeRule);

    /**
    * 删除
    * @param id
    */
    void deleteScanSchemeRule(@NotNull String id);

    /**
     * 条件删除扫描结果
     * @param  key  删除条件字段
     * @param value
     */
    void deleteScanSchemeRuleByCondition(@NotNull String key,@NotNull String value);

    @FindOne
    ScanSchemeRule findOne(@NotNull String id);

    @FindList
    List<ScanSchemeRule> findList(List<String> idList);

    /**
    * 查找
    * @param id
    * @return
    */

    ScanSchemeRule findScanSchemeRule(@NotNull String id);

    /**
    * 查找所有
    * @return
    */
    @FindAll
    List<ScanSchemeRule> findAllScanSchemeRule();

    /**
    * 查询列表
    * @param scanSchemeRuleQuery
    * @return
    */
    List<ScanSchemeRule> findScanSchemeRuleList(ScanSchemeRuleQuery scanSchemeRuleQuery);

    /**
    * 按分页查询
    * @param scanSchemeRuleQuery
    * @return
    */
    Pagination<ScanSchemeRule> findScanSchemeRulePage(ScanSchemeRuleQuery scanSchemeRuleQuery);

}