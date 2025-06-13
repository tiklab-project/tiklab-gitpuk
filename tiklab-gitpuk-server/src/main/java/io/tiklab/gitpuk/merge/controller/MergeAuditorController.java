package io.tiklab.gitpuk.merge.controller;

import io.tiklab.core.Result;
import io.tiklab.core.page.Pagination;
import io.tiklab.gitpuk.merge.model.MergeAuditor;
import io.tiklab.gitpuk.merge.model.MergeAuditorQuery;
import io.tiklab.gitpuk.merge.service.MergeAuditorService;
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
 * MergeAuditorController
 */
@RestController
@RequestMapping("/mergeAuditor")
//@Api(name = "MergeAuditorController",desc = "合并请求审核人")
public class MergeAuditorController {

    private static Logger logger = LoggerFactory.getLogger(MergeAuditorController.class);

    @Autowired
    private MergeAuditorService mergeAuditorService;

    @RequestMapping(path="/createMergeAuditor",method = RequestMethod.POST)
    @ApiMethod(name = "createMergeAuditor",desc = "添加合并请求审核人")
    @ApiParam(name = "mergeAuditor",desc = "mergeAuditor",required = true)
    public Result<String> createMergeAuditor(@RequestBody @Valid @NotNull MergeAuditor mergeAuditor){
        String mergeAuditorId = mergeAuditorService.createMergeAuditor(mergeAuditor);

        return Result.ok(mergeAuditorId);
    }

    @RequestMapping(path="/updateMergeAuditor",method = RequestMethod.POST)
    @ApiMethod(name = "updateMergeAuditor",desc = "更新合并请求审核人")
    @ApiParam(name = "mergeAuditor",desc = "mergeAuditor",required = true)
    public Result<String> updateMergeAuditor(@RequestBody @Valid @NotNull MergeAuditor mergeAuditor){
         mergeAuditorService.updateMergeAuditor(mergeAuditor);

        return Result.ok();
    }



    @RequestMapping(path="/deleteMergeAuditor",method = RequestMethod.POST)
    @ApiMethod(name = "deleteMergeAuditor",desc = "删除合并请求审核人")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<Void> deleteMergeAuditor(@NotNull String id){
        mergeAuditorService.deleteMergeAuditor(id);

        return Result.ok();
    }

    @RequestMapping(path="/findMergeAuditor",method = RequestMethod.POST)
    @ApiMethod(name = "findMergeAuditor",desc = "通过id查询合并请求审核人")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<MergeAuditor> findMergeAuditor(@NotNull String id){
        MergeAuditor mergeAuditor = mergeAuditorService.findMergeAuditor(id);

        return Result.ok(mergeAuditor);
    }

    @RequestMapping(path="/findAllMergeAuditor",method = RequestMethod.POST)
    @ApiMethod(name = "findAllMergeAuditor",desc = "查询所有合并请求审核人")
    public Result<List<MergeAuditor>> findAllMergeAuditor(){
        List<MergeAuditor> mergeAuditorList = mergeAuditorService.findAllMergeAuditor();

        return Result.ok(mergeAuditorList);
    }

    @RequestMapping(path = "/findMergeAuditorList",method = RequestMethod.POST)
    @ApiMethod(name = "findMergeAuditorList",desc = "条件查询合并请求审核人")
    @ApiParam(name = "mergeAuditorQuery",desc = "mergeAuditorQuery",required = true)
    public Result<List<MergeAuditor>> findMergeAuditorList(@RequestBody @Valid @NotNull MergeAuditorQuery mergeAuditorQuery){
        List<MergeAuditor> mergeAuditorList = mergeAuditorService.findMergeAuditorList(mergeAuditorQuery);

        return Result.ok(mergeAuditorList);
    }

    @RequestMapping(path = "/findMergeAuditorPage",method = RequestMethod.POST)
    @ApiMethod(name = "findMergeAuditorPage",desc = "条件分页查询合并请求审核人")
    @ApiParam(name = "mergeAuditorQuery",desc = "mergeAuditorQuery",required = true)
    public Result<Pagination<MergeAuditor>> findMergeAuditorPage(@RequestBody @Valid @NotNull MergeAuditorQuery mergeAuditorQuery){
        Pagination<MergeAuditor> pagination = mergeAuditorService.findMergeAuditorPage(mergeAuditorQuery);

        return Result.ok(pagination);
    }

}
