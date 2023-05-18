package io.tiklab.xcode.repository.controller;

import io.tiklab.core.Result;
import io.tiklab.core.page.Pagination;
import io.tiklab.postin.annotation.Api;
import io.tiklab.postin.annotation.ApiMethod;
import io.tiklab.postin.annotation.ApiParam;
import io.tiklab.xcode.repository.model.OpenRecord;
import io.tiklab.xcode.repository.model.OpenRecordQuery;
import io.tiklab.xcode.repository.service.OpenRecordService;
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
 * OpenRecordController
 */
@RestController
@RequestMapping("/OpenRecord")
@Api(name = "OpenRecordController",desc = "打开仓库的记录管理")
public class OpenRecordController {

    private static Logger logger = LoggerFactory.getLogger(OpenRecordController.class);

    @Autowired
    private OpenRecordService openRecordService;

    @RequestMapping(path="/createOpenRecord",method = RequestMethod.POST)
    @ApiMethod(name = "createOpenRecord",desc = "创建打开仓库记录管理")
    @ApiParam(name = "OpenRecord",desc = "OpenRecord",required = true)
    public Result<String> createOpenRecord(@RequestBody @NotNull @Valid OpenRecord OpenRecord){
        String id = openRecordService.createOpenRecord(OpenRecord);

        return Result.ok(id);
    }

    @RequestMapping(path="/updateOpenRecord",method = RequestMethod.POST)
    @ApiMethod(name = "updateOpenRecord",desc = "修改打开仓库记录管理")
    @ApiParam(name = "OpenRecord",desc = "OpenRecord",required = true)
    public Result<Void> updateOpenRecord(@RequestBody @NotNull @Valid OpenRecord OpenRecord){
        openRecordService.updateOpenRecord(OpenRecord);

        return Result.ok();
    }

    @RequestMapping(path="/deleteOpenRecord",method = RequestMethod.POST)
    @ApiMethod(name = "deleteOpenRecord",desc = "删除")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<Void> deleteOpenRecord(@NotNull String id){
        openRecordService.deleteOpenRecord(id);

        return Result.ok();
    }

    @RequestMapping(path="/findOpenRecord",method = RequestMethod.POST)
    @ApiMethod(name = "findOpenRecord",desc = "通过id 查询")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<OpenRecord> findOpenRecord(@NotNull String id){
        OpenRecord OpenRecord = openRecordService.findOpenRecord(id);

        return Result.ok(OpenRecord);
    }

    @RequestMapping(path="/findAllOpenRecord",method = RequestMethod.POST)
    @ApiMethod(name = "findAllOpenRecord",desc = "查询所有查询")
    public Result<List<OpenRecord>> findAllOpenRecord(){
        List<OpenRecord> artifactList = openRecordService.findAllOpenRecord();

        return Result.ok(artifactList);
    }

    @RequestMapping(path = "/findOpenRecordList",method = RequestMethod.POST)
    @ApiMethod(name = "findOpenRecordList",desc = "通过条件查询")
    @ApiParam(name = "OpenRecordQuery",desc = "OpenRecordQuery",required = true)
    public Result<List<OpenRecord>> findOpenRecordList(@RequestBody @Valid @NotNull OpenRecordQuery openRecordQuery){
        List<OpenRecord> artifactList = openRecordService.findOpenRecordList(openRecordQuery);

        return Result.ok(artifactList);
    }

    @RequestMapping(path = "/findOpenRecordPage",method = RequestMethod.POST)
    @ApiMethod(name = "findOpenRecordPage",desc = "通过条件分页查询")
    @ApiParam(name = "OpenRecordQuery",desc = "OpenRecordQuery",required = true)
    public Result<Pagination<OpenRecord>> findOpenRecordPage(@RequestBody @Valid @NotNull OpenRecordQuery openRecordQuery){
        Pagination<OpenRecord> pagination = openRecordService.findOpenRecordPage(openRecordQuery);

        return Result.ok(pagination);
    }


}
