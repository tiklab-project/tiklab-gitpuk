package io.thoughtware.gittork.scan.controller;

import io.thoughtware.gittork.scan.model.ScanSchemeRule;
import io.thoughtware.gittork.scan.model.ScanSchemeRuleQuery;
import io.thoughtware.gittork.scan.service.ScanSchemeRuleService;
import io.thoughtware.core.Result;
import io.thoughtware.core.page.Pagination;
import io.thoughtware.postin.annotation.Api;
import io.thoughtware.postin.annotation.ApiMethod;
import io.thoughtware.postin.annotation.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * ScanSchemeRuleController
 */
@RestController
@RequestMapping("/scanSchemeRule")
@Api(name = "ScanSchemeRuleController",desc = "扫描方案规则关系")
public class ScanSchemeRuleController {

    private static Logger logger = LoggerFactory.getLogger(ScanSchemeRuleController.class);

    @Autowired
    private ScanSchemeRuleService scanSchemeRuleService;

    @RequestMapping(path="/createScanSchemeRule",method = RequestMethod.POST)
    @ApiMethod(name = "createScanSchemeRule",desc = "添加扫方案规则关系")
    @ApiParam(name = "scanSchemeRule",desc = "scanSchemeRule",required = true)
    public Result<String> createScanSchemeRule(@RequestBody @Valid @NotNull ScanSchemeRule scanSchemeRule){
        String scanSchemeRuleId = scanSchemeRuleService.createScanSchemeRule(scanSchemeRule);

        return Result.ok(scanSchemeRuleId);
    }

    @RequestMapping(path="/updateScanSchemeRule",method = RequestMethod.POST)
    @ApiMethod(name = "updateScanSchemeRule",desc = "更新扫描方案规则关系")
    @ApiParam(name = "scanSchemeRule",desc = "scanSchemeRule",required = true)
    public Result<String> updateScanSchemeRule(@RequestBody @Valid @NotNull ScanSchemeRule scanSchemeRule){
         scanSchemeRuleService.updateScanSchemeRule(scanSchemeRule);

        return Result.ok();
    }


    @RequestMapping(path="/deleteScanSchemeRule",method = RequestMethod.POST)
    @ApiMethod(name = "deleteScanSchemeRule",desc = "删除扫描方案规则关系")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<Void> deleteScanSchemeRule(@NotNull String id){
        scanSchemeRuleService.deleteScanSchemeRule(id);

        return Result.ok();
    }

    @RequestMapping(path="/findScanSchemeRule",method = RequestMethod.POST)
    @ApiMethod(name = "findScanSchemeRule",desc = "通过id查询扫描方案规则关系")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<ScanSchemeRule> findScanSchemeRule(@NotNull String id){
        ScanSchemeRule scanSchemeRule = scanSchemeRuleService.findScanSchemeRule(id);

        return Result.ok(scanSchemeRule);
    }

    @RequestMapping(path="/findAllScanSchemeRule",method = RequestMethod.POST)
    @ApiMethod(name = "findAllScanSchemeRule",desc = "查询所有扫描方案规则关系")
    public Result<List<ScanSchemeRule>> findAllScanSchemeRule(){
        List<ScanSchemeRule> scanSchemeRuleList = scanSchemeRuleService.findAllScanSchemeRule();

        return Result.ok(scanSchemeRuleList);
    }

    @RequestMapping(path = "/findScanSchemeRuleList",method = RequestMethod.POST)
    @ApiMethod(name = "findScanSchemeRuleList",desc = "条件查询扫描方案规则关系")
    @ApiParam(name = "scanSchemeRuleQuery",desc = "scanSchemeRuleQuery",required = true)
    public Result<List<ScanSchemeRule>> findScanSchemeRuleList(@RequestBody @Valid @NotNull ScanSchemeRuleQuery scanSchemeRuleQuery){
        List<ScanSchemeRule> scanSchemeRuleList = scanSchemeRuleService.findScanSchemeRuleList(scanSchemeRuleQuery);

        return Result.ok(scanSchemeRuleList);
    }

    @RequestMapping(path = "/findScanSchemeRulePage",method = RequestMethod.POST)
    @ApiMethod(name = "findScanSchemeRulePage",desc = "条件分页查询扫描方案规则关系")
    @ApiParam(name = "scanSchemeRuleQuery",desc = "scanSchemeRuleQuery",required = true)
    public Result<Pagination<ScanSchemeRule>> findScanSchemeRulePage(@RequestBody @Valid @NotNull ScanSchemeRuleQuery scanSchemeRuleQuery){
        Pagination<ScanSchemeRule> pagination = scanSchemeRuleService.findScanSchemeRulePage(scanSchemeRuleQuery);

        return Result.ok(pagination);
    }

}
