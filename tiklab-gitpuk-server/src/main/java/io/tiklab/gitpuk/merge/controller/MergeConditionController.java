package io.tiklab.gitpuk.merge.controller;

import io.tiklab.core.Result;
import io.tiklab.core.page.Pagination;
import io.tiklab.gitpuk.merge.model.MergeCondition;
import io.tiklab.gitpuk.merge.model.MergeConditionQuery;
import io.tiklab.gitpuk.merge.service.MergeConditionService;
import io.tiklab.postin.annotation.Api;
import io.tiklab.postin.annotation.ApiMethod;
import io.tiklab.postin.annotation.ApiParam;
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
 * MergeConditionController
 */
@RestController
@RequestMapping("/mergeCondition")
//@Api(name = "MergeConditionController",desc = "合并请求动态")
public class MergeConditionController {

    private static Logger logger = LoggerFactory.getLogger(MergeConditionController.class);

    @Autowired
    private MergeConditionService mergeConditionService;

    @RequestMapping(path="/createMergeCondition",method = RequestMethod.POST)
    @ApiMethod(name = "createMergeCondition",desc = "添加合并请求动态")
    @ApiParam(name = "mergeCondition",desc = "mergeCondition",required = true)
    public Result<String> createMergeCondition(@RequestBody @Valid @NotNull MergeCondition mergeCondition){
        String mergeConditionId = mergeConditionService.createMergeCondition(mergeCondition);

        return Result.ok(mergeConditionId);
    }

    @RequestMapping(path="/updateMergeCondition",method = RequestMethod.POST)
    @ApiMethod(name = "updateMergeCondition",desc = "更新合并请求动态")
    @ApiParam(name = "mergeCondition",desc = "mergeCondition",required = true)
    public Result<String> updateMergeCondition(@RequestBody @Valid @NotNull MergeCondition mergeCondition){
         mergeConditionService.updateMergeCondition(mergeCondition);

        return Result.ok();
    }


    @RequestMapping(path="/deleteMergeCondition",method = RequestMethod.POST)
    @ApiMethod(name = "deleteMergeCondition",desc = "删除合并请求动态")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<Void> deleteMergeCondition(@NotNull String id){
        mergeConditionService.deleteMergeCondition(id);

        return Result.ok();
    }

    @RequestMapping(path="/findMergeCondition",method = RequestMethod.POST)
    @ApiMethod(name = "findMergeCondition",desc = "通过id查询合并请求动态")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<MergeCondition> findMergeCondition(@NotNull String id){
        MergeCondition mergeCondition = mergeConditionService.findMergeCondition(id);

        return Result.ok(mergeCondition);
    }

    @RequestMapping(path="/findAllMergeCondition",method = RequestMethod.POST)
    @ApiMethod(name = "findAllMergeCondition",desc = "查询所有合并请求动态")
    public Result<List<MergeCondition>> findAllMergeCondition(){
        List<MergeCondition> mergeConditionList = mergeConditionService.findAllMergeCondition();

        return Result.ok(mergeConditionList);
    }

    @RequestMapping(path = "/findMergeConditionList",method = RequestMethod.POST)
    @ApiMethod(name = "findMergeConditionList",desc = "条件查询合并请求动态")
    @ApiParam(name = "mergeConditionQuery",desc = "mergeConditionQuery",required = true)
    public Result<List<MergeCondition>> findMergeConditionList(@RequestBody @Valid @NotNull MergeConditionQuery mergeConditionQuery){
        List<MergeCondition> mergeConditionList = mergeConditionService.findMergeConditionList(mergeConditionQuery);

        return Result.ok(mergeConditionList);
    }

    @RequestMapping(path = "/findMergeConditionPage",method = RequestMethod.POST)
    @ApiMethod(name = "findMergeConditionPage",desc = "条件分页查询合并请求动态")
    @ApiParam(name = "mergeConditionQuery",desc = "mergeConditionQuery",required = true)
    public Result<Pagination<MergeCondition>> findMergeConditionPage(@RequestBody @Valid @NotNull MergeConditionQuery mergeConditionQuery){
        Pagination<MergeCondition> pagination = mergeConditionService.findMergeConditionPage(mergeConditionQuery);

        return Result.ok(pagination);
    }

}
