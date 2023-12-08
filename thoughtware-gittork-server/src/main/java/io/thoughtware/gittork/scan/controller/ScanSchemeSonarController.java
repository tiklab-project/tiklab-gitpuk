package io.thoughtware.gittork.scan.controller;

import io.thoughtware.gittork.scan.model.ScanSchemeSonar;
import io.thoughtware.gittork.scan.model.ScanSchemeSonarQuery;
import io.thoughtware.gittork.scan.service.ScanSchemeSonarService;
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
 * ScanSchemeSonarController
 */
@RestController
@RequestMapping("/scanSchemeSonar")
@Api(name = "ScanSchemeSonarController",desc = "扫描方案sonar 关系")
public class ScanSchemeSonarController {

    private static Logger logger = LoggerFactory.getLogger(ScanSchemeSonarController.class);

    @Autowired
    private ScanSchemeSonarService scanSchemeSonarService;

    @RequestMapping(path="/createScanSchemeSonar",method = RequestMethod.POST)
    @ApiMethod(name = "createScanSchemeSonar",desc = "添加扫描方案sonar 关系")
    @ApiParam(name = "scanSchemeSonar",desc = "scanSchemeSonar",required = true)
    public Result<String> createScanSchemeSonar(@RequestBody @Valid @NotNull ScanSchemeSonar scanSchemeSonar){
        String scanSchemeSonarId = scanSchemeSonarService.createScanSchemeSonar(scanSchemeSonar);

        return Result.ok(scanSchemeSonarId);
    }

    @RequestMapping(path="/updateScanSchemeSonar",method = RequestMethod.POST)
    @ApiMethod(name = "updateScanSchemeSonar",desc = "更新扫描方案sonar 关系")
    @ApiParam(name = "scanSchemeSonar",desc = "scanSchemeSonar",required = true)
    public Result<String> updateScanSchemeSonar(@RequestBody @Valid @NotNull ScanSchemeSonar scanSchemeSonar){
         scanSchemeSonarService.updateScanSchemeSonar(scanSchemeSonar);

        return Result.ok();
    }


    @RequestMapping(path="/deleteScanSchemeSonar",method = RequestMethod.POST)
    @ApiMethod(name = "deleteScanSchemeSonar",desc = "删除扫描方案sonar 关系")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<Void> deleteScanSchemeSonar(@NotNull String id){
        scanSchemeSonarService.deleteScanSchemeSonar(id);

        return Result.ok();
    }

    @RequestMapping(path="/findScanSchemeSonar",method = RequestMethod.POST)
    @ApiMethod(name = "findScanSchemeSonar",desc = "通过id查询扫描方案sonar 关系")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<ScanSchemeSonar> findScanSchemeSonar(@NotNull String id){
        ScanSchemeSonar scanSchemeSonar = scanSchemeSonarService.findScanSchemeSonar(id);

        return Result.ok(scanSchemeSonar);
    }

    @RequestMapping(path="/findAllScanSchemeSonar",method = RequestMethod.POST)
    @ApiMethod(name = "findAllScanSchemeSonar",desc = "查询所有扫描方案sonar 关系")
    public Result<List<ScanSchemeSonar>> findAllScanSchemeSonar(){
        List<ScanSchemeSonar> scanSchemeSonarList = scanSchemeSonarService.findAllScanSchemeSonar();

        return Result.ok(scanSchemeSonarList);
    }

    @RequestMapping(path = "/findScanSchemeSonarList",method = RequestMethod.POST)
    @ApiMethod(name = "findScanSchemeSonarList",desc = "条件查询扫描方案sonar 关系")
    @ApiParam(name = "scanSchemeSonarQuery",desc = "scanSchemeSonarQuery",required = true)
    public Result<List<ScanSchemeSonar>> findScanSchemeSonarList(@RequestBody @Valid @NotNull ScanSchemeSonarQuery scanSchemeSonarQuery){
        List<ScanSchemeSonar> scanSchemeSonarList = scanSchemeSonarService.findScanSchemeSonarList(scanSchemeSonarQuery);

        return Result.ok(scanSchemeSonarList);
    }

    @RequestMapping(path = "/findScanSchemeSonarPage",method = RequestMethod.POST)
    @ApiMethod(name = "findScanSchemeSonarPage",desc = "条件分页查询扫描方案sonar 关系")
    @ApiParam(name = "scanSchemeSonarQuery",desc = "scanSchemeSonarQuery",required = true)
    public Result<Pagination<ScanSchemeSonar>> findScanSchemeSonarPage(@RequestBody @Valid @NotNull ScanSchemeSonarQuery scanSchemeSonarQuery){
        Pagination<ScanSchemeSonar> pagination = scanSchemeSonarService.findScanSchemeSonarPage(scanSchemeSonarQuery);

        return Result.ok(pagination);
    }

}
