package io.tiklab.xcode.repository.controller;

import io.tiklab.core.Result;
import io.tiklab.core.page.Pagination;
import io.tiklab.postin.annotation.Api;
import io.tiklab.postin.annotation.ApiMethod;
import io.tiklab.postin.annotation.ApiParam;
import io.tiklab.xcode.repository.model.RecordCommit;
import io.tiklab.xcode.repository.model.RecordCommitQuery;
import io.tiklab.xcode.repository.service.RecordCommitService;
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
 * RecordCommitController
 */
@RestController
@RequestMapping("/recordCommit")
@Api(name = "RecordCommitController",desc = "提交仓库的记录管理")
public class RecordCommitController {

    private static Logger logger = LoggerFactory.getLogger(RecordCommitController.class);

    @Autowired
    private RecordCommitService recordCommitService;

    @RequestMapping(path="/createRecordCommit",method = RequestMethod.POST)
    @ApiMethod(name = "createRecordCommit",desc = "创建打开仓库记录管理")
    @ApiParam(name = "RecordCommit",desc = "RecordCommit",required = true)
    public Result<String> createRecordCommit(@RequestBody @NotNull @Valid RecordCommit RecordCommit){
        String id = recordCommitService.createRecordCommit(RecordCommit);

        return Result.ok(id);
    }

    @RequestMapping(path="/updateRecordCommit",method = RequestMethod.POST)
    @ApiMethod(name = "updateRecordCommit",desc = "修改打开仓库记录管理")
    @ApiParam(name = "RecordCommit",desc = "RecordCommit",required = true)
    public Result<Void> updateRecordCommit(@RequestBody @NotNull @Valid RecordCommit RecordCommit){
        recordCommitService.updateRecordCommit(RecordCommit);

        return Result.ok();
    }

    @RequestMapping(path="/deleteRecordCommit",method = RequestMethod.POST)
    @ApiMethod(name = "deleteRecordCommit",desc = "删除")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<Void> deleteRecordCommit(@NotNull String id){
        recordCommitService.deleteRecordCommit(id);

        return Result.ok();
    }

    @RequestMapping(path="/findRecordCommit",method = RequestMethod.POST)
    @ApiMethod(name = "findRecordCommit",desc = "通过id 查询")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<RecordCommit> findRecordCommit(@NotNull String id){
        RecordCommit RecordCommit = recordCommitService.findRecordCommit(id);

        return Result.ok(RecordCommit);
    }

    @RequestMapping(path="/findAllRecordCommit",method = RequestMethod.POST)
    @ApiMethod(name = "findAllRecordCommit",desc = "查询所有查询")
    public Result<List<RecordCommit>> findAllRecordCommit(){
        List<RecordCommit> artifactList = recordCommitService.findAllRecordCommit();

        return Result.ok(artifactList);
    }

    @RequestMapping(path = "/findRecordCommitList",method = RequestMethod.POST)
    @ApiMethod(name = "findRecordCommitList",desc = "通过条件查询")
    @ApiParam(name = "RecordCommitQuery",desc = "RecordCommitQuery",required = true)
    public Result<List<RecordCommit>> findRecordCommitList(@RequestBody @Valid @NotNull RecordCommitQuery recordCommitQuery){
        List<RecordCommit> artifactList = recordCommitService.findRecordCommitList(recordCommitQuery);

        return Result.ok(artifactList);
    }

    @RequestMapping(path = "/findRecordCommitPage",method = RequestMethod.POST)
    @ApiMethod(name = "findRecordCommitPage",desc = "通过条件分页查询")
    @ApiParam(name = "RecordCommitQuery",desc = "RecordCommitQuery",required = true)
    public Result<Pagination<RecordCommit>> findRecordCommitPage(@RequestBody @Valid @NotNull RecordCommitQuery recordCommitQuery){
        Pagination<RecordCommit> pagination = recordCommitService.findRecordCommitPage(recordCommitQuery);

        return Result.ok(pagination);
    }


}
