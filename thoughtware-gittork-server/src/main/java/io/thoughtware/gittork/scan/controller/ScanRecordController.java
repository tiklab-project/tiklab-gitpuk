package io.thoughtware.gittork.scan.controller;

import io.thoughtware.gittork.scan.model.ScanRecord;
import io.thoughtware.gittork.scan.model.ScanRecordQuery;
import io.thoughtware.gittork.scan.service.ScanRecordService;
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
 * ScanRecordController
 */
@RestController
@RequestMapping("/scanRecord")
@Api(name = "ScanRecordController",desc = "扫描记录")
public class ScanRecordController {

    private static Logger logger = LoggerFactory.getLogger(ScanRecordController.class);

    @Autowired
    private ScanRecordService scanRecordService;

    @RequestMapping(path="/createScanRecord",method = RequestMethod.POST)
    @ApiMethod(name = "createScanRecord",desc = "添加扫描制品记录")
    @ApiParam(name = "scanRecord",desc = "scanRecord",required = true)
    public Result<String> createScanRecord(@RequestBody @Valid @NotNull ScanRecord scanRecord){
        String scanRecordId = scanRecordService.createScanRecord(scanRecord);

        return Result.ok(scanRecordId);
    }

    @RequestMapping(path="/updateScanRecord",method = RequestMethod.POST)
    @ApiMethod(name = "updateScanRecord",desc = "更新扫描制品记录")
    @ApiParam(name = "scanRecord",desc = "scanRecord",required = true)
    public Result<String> updateScanRecord(@RequestBody @Valid @NotNull ScanRecord scanRecord){
         scanRecordService.updateScanRecord(scanRecord);

        return Result.ok();
    }


    @RequestMapping(path="/deleteScanRecord",method = RequestMethod.POST)
    @ApiMethod(name = "deleteScanRecord",desc = "删除扫描记录")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<Void> deleteScanRecord(@NotNull String id){
        scanRecordService.deleteScanRecord(id);

        return Result.ok();
    }

    @RequestMapping(path="/findScanRecord",method = RequestMethod.POST)
    @ApiMethod(name = "findScanRecord",desc = "通过id查询扫描记录")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<ScanRecord> findScanRecord(@NotNull String id){
        ScanRecord scanRecord = scanRecordService.findScanRecord(id);

        return Result.ok(scanRecord);
    }

    @RequestMapping(path="/findAllScanRecord",method = RequestMethod.POST)
    @ApiMethod(name = "findAllScanRecord",desc = "查询所有扫描记录")
    public Result<List<ScanRecord>> findAllScanRecord(){
        List<ScanRecord> scanRecordList = scanRecordService.findAllScanRecord();

        return Result.ok(scanRecordList);
    }

    @RequestMapping(path = "/findScanRecordList",method = RequestMethod.POST)
    @ApiMethod(name = "findScanRecordList",desc = "条件查询扫描记录")
    @ApiParam(name = "scanRecordQuery",desc = "scanRecordQuery",required = true)
    public Result<List<ScanRecord>> findScanRecordList(@RequestBody @Valid @NotNull ScanRecordQuery scanRecordQuery){
        List<ScanRecord> scanRecordList = scanRecordService.findScanRecordList(scanRecordQuery);

        return Result.ok(scanRecordList);
    }

    @RequestMapping(path = "/findScanRecordPage",method = RequestMethod.POST)
    @ApiMethod(name = "findScanRecordPage",desc = "条件分页查询扫描记录")
    @ApiParam(name = "scanRecordQuery",desc = "scanRecordQuery",required = true)
    public Result<Pagination<ScanRecord>> findScanRecordPage(@RequestBody @Valid @NotNull ScanRecordQuery scanRecordQuery){
        Pagination<ScanRecord> pagination = scanRecordService.findScanRecordPage(scanRecordQuery);

        return Result.ok(pagination);
    }

}