package io.thoughtware.gittok.commit.controller;

import io.thoughtware.core.Result;
import io.thoughtware.core.page.Pagination;
import io.thoughtware.gittok.commit.model.MergeData;
import io.thoughtware.gittok.commit.model.MergeRequest;
import io.thoughtware.gittok.commit.model.MergeRequestQuery;
import io.thoughtware.gittok.commit.service.MergeRequestService;
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
import java.util.Map;

/**
 * MergeRequestController
 */
@RestController
@RequestMapping("/mergeRequest")
@Api(name = "MergeRequestController",desc = "合并请求")
public class MergeRequestController {

    private static Logger logger = LoggerFactory.getLogger(MergeRequestController.class);

    @Autowired
    private MergeRequestService mergeRequestService;

    @RequestMapping(path="/createMergeRequest",method = RequestMethod.POST)
    @ApiMethod(name = "createMergeRequest",desc = "添加合并请求")
    @ApiParam(name = "mergeRequest",desc = "mergeRequest",required = true)
    public Result<String> createMergeRequest(@RequestBody @Valid @NotNull MergeRequest mergeRequest){
        String mergeRequestId = mergeRequestService.createMergeRequest(mergeRequest);

        return Result.ok(mergeRequestId);
    }

    @RequestMapping(path="/updateMergeRequest",method = RequestMethod.POST)
    @ApiMethod(name = "updateMergeRequest",desc = "更新合并请求")
    @ApiParam(name = "mergeRequest",desc = "mergeRequest",required = true)
    public Result<String> updateMergeRequest(@RequestBody @Valid @NotNull MergeRequest mergeRequest){
         mergeRequestService.updateMergeRequest(mergeRequest);

        return Result.ok();
    }


    @RequestMapping(path="/deleteMergeRequest",method = RequestMethod.POST)
    @ApiMethod(name = "deleteMergeRequest",desc = "删除合并请求")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<Void> deleteMergeRequest(@NotNull String id){
        mergeRequestService.deleteMergeRequest(id);

        return Result.ok();
    }

    @RequestMapping(path="/findMergeRequest",method = RequestMethod.POST)
    @ApiMethod(name = "findMergeRequest",desc = "通过id查询合并请求")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<MergeRequest> findMergeRequest(@NotNull String id){
        MergeRequest mergeRequest = mergeRequestService.findMergeRequest(id);

        return Result.ok(mergeRequest);
    }

    @RequestMapping(path="/findAllMergeRequest",method = RequestMethod.POST)
    @ApiMethod(name = "findAllMergeRequest",desc = "查询所有合并请求")
    public Result<List<MergeRequest>> findAllMergeRequest(){
        List<MergeRequest> mergeRequestList = mergeRequestService.findAllMergeRequest();

        return Result.ok(mergeRequestList);
    }

    @RequestMapping(path = "/findMergeRequestList",method = RequestMethod.POST)
    @ApiMethod(name = "findMergeRequestList",desc = "条件查询合并请求")
    @ApiParam(name = "mergeRequestQuery",desc = "mergeRequestQuery",required = true)
    public Result<List<MergeRequest>> findMergeRequestList(@RequestBody @Valid @NotNull MergeRequestQuery mergeRequestQuery){
        List<MergeRequest> mergeRequestList = mergeRequestService.findMergeRequestList(mergeRequestQuery);

        return Result.ok(mergeRequestList);
    }

    @RequestMapping(path = "/findMergeRequestPage",method = RequestMethod.POST)
    @ApiMethod(name = "findMergeRequestPage",desc = "条件分页查询合并请求")
    @ApiParam(name = "mergeRequestQuery",desc = "mergeRequestQuery",required = true)
    public Result<Pagination<MergeRequest>> findMergeRequestPage(@RequestBody @Valid @NotNull MergeRequestQuery mergeRequestQuery){
        Pagination<MergeRequest> pagination = mergeRequestService.findMergeRequestPage(mergeRequestQuery);

        return Result.ok(pagination);
    }

    @RequestMapping(path = "/findMergeStateNum",method = RequestMethod.POST)
    @ApiMethod(name = "findMergeStateNum",desc = "查询各状态的合并请求数量")
    @ApiParam(name = "mergeRequestQuery",desc = "mergeRequestQuery",required = true)
    public Result<Map> findMergeStateNum(@RequestBody @Valid @NotNull MergeRequestQuery mergeRequestQuery){
        Map s = mergeRequestService.findMergeStateNum(mergeRequestQuery);

        return Result.ok(s);
    }

    @RequestMapping(path = "/execMerge",method = RequestMethod.POST)
    @ApiMethod(name = "execMerge",desc = "执行合并")
    @ApiParam(name = "mergeRequestQuery",desc = "mergeRequestQuery",required = true)
    public Result<Void> execMerge(@RequestBody @Valid @NotNull MergeData mergeData){
        String s = mergeRequestService.execMerge(mergeData);

        return Result.ok(s);
    }

}
