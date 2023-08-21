package io.tiklab.xcode.repository.controller;

import io.tiklab.core.Result;
import io.tiklab.postin.annotation.Api;
import io.tiklab.postin.annotation.ApiMethod;
import io.tiklab.postin.annotation.ApiParam;
import io.tiklab.xcode.repository.model.LeadRecord;
import io.tiklab.xcode.repository.model.LeadRecordQuery;
import io.tiklab.xcode.repository.service.LeadRecordService;
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
 * LeadRecordController
 */
@RestController
@RequestMapping("/leadRecord")
@Api(name = "LeadRecordController",desc = "导入第三方仓库的记录")
public class LeadRecordController {

    private static Logger logger = LoggerFactory.getLogger(LeadRecordController.class);

    @Autowired
    private LeadRecordService leadRecordService;

    @RequestMapping(path="/createLeadRecord",method = RequestMethod.POST)
    @ApiMethod(name = "createLeadRecord",desc = "创建第三方仓库记录")
    @ApiParam(name = "LeadRecord",desc = "LeadRecord",required = true)
    public Result<String> createLeadRecord(@RequestBody @NotNull @Valid LeadRecord leadRecord){
        String id = leadRecordService.createLeadRecord(leadRecord);

        return Result.ok(id);
    }
    

    @RequestMapping(path="/deleteLeadRecord",method = RequestMethod.POST)
    @ApiMethod(name = "deleteLeadRecord",desc = "删除")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<Void> deleteLeadRecord(@NotNull String id){
        leadRecordService.deleteLeadRecord(id);

        return Result.ok();
    }

    @RequestMapping(path="/findLeadRecord",method = RequestMethod.POST)
    @ApiMethod(name = "findLeadRecord",desc = "通过id 查询")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<LeadRecord> findLeadRecord(@NotNull String id){
        LeadRecord LeadRecord = leadRecordService.findLeadRecord(id);

        return Result.ok(LeadRecord);
    }

    @RequestMapping(path="/findAllLeadRecord",method = RequestMethod.POST)
    @ApiMethod(name = "findAllLeadRecord",desc = "查询所有查询")
    public Result<List<LeadRecord>> findAllLeadRecord(){
        List<LeadRecord> artifactList = leadRecordService.findAllLeadRecord();

        return Result.ok(artifactList);
    }

    @RequestMapping(path = "/findLeadRecordList",method = RequestMethod.POST)
    @ApiMethod(name = "findLeadRecordList",desc = "通过条件查询")
    @ApiParam(name = "LeadRecordQuery",desc = "LeadRecordQuery",required = true)
    public Result<List<LeadRecord>> findLeadRecordList(@RequestBody @Valid @NotNull LeadRecordQuery leadRecordQuery){
        List<LeadRecord> artifactList = leadRecordService.findLeadRecordList(leadRecordQuery);

        return Result.ok(artifactList);
    }


}
