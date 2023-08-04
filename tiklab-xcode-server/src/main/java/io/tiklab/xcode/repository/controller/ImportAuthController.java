package io.tiklab.xcode.repository.controller;

import io.tiklab.core.Result;
import io.tiklab.core.page.Pagination;
import io.tiklab.postin.annotation.Api;
import io.tiklab.postin.annotation.ApiMethod;
import io.tiklab.postin.annotation.ApiParam;
import io.tiklab.xcode.repository.model.ImportAuth;
import io.tiklab.xcode.repository.model.ImportAuthQuery;
import io.tiklab.xcode.repository.service.ImportAuthService;
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
 * ImportAuthController
 */
@RestController
@RequestMapping("/importAuth")
@Api(name = "ImportAuthController",desc = "导入第三方仓库的认证")
public class ImportAuthController {

    private static Logger logger = LoggerFactory.getLogger(ImportAuthController.class);

    @Autowired
    private ImportAuthService importAuthService;

    @RequestMapping(path="/createImportAuth",method = RequestMethod.POST)
    @ApiMethod(name = "createImportAuth",desc = "创建第三方仓库认证")
    @ApiParam(name = "ImportAuth",desc = "ImportAuth",required = true)
    public Result<String> createImportAuth(@RequestBody @NotNull @Valid ImportAuth ImportAuth){
        String id = importAuthService.createImportAuth(ImportAuth);

        return Result.ok(id);
    }

    @RequestMapping(path="/updateImportAuth",method = RequestMethod.POST)
    @ApiMethod(name = "updateImportAuth",desc = "修改第三方仓库认证")
    @ApiParam(name = "ImportAuth",desc = "ImportAuth",required = true)
    public Result<Void> updateImportAuth(@RequestBody @NotNull @Valid ImportAuth ImportAuth){
        importAuthService.updateImportAuth(ImportAuth);

        return Result.ok();
    }

    @RequestMapping(path="/deleteImportAuth",method = RequestMethod.POST)
    @ApiMethod(name = "deleteImportAuth",desc = "删除")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<Void> deleteImportAuth(@NotNull String id){
        importAuthService.deleteImportAuth(id);

        return Result.ok();
    }

    @RequestMapping(path="/findImportAuth",method = RequestMethod.POST)
    @ApiMethod(name = "findImportAuth",desc = "通过id 查询")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<ImportAuth> findImportAuth(@NotNull String id){
        ImportAuth ImportAuth = importAuthService.findImportAuth(id);

        return Result.ok(ImportAuth);
    }

    @RequestMapping(path="/findAllImportAuth",method = RequestMethod.POST)
    @ApiMethod(name = "findAllImportAuth",desc = "查询所有查询")
    public Result<List<ImportAuth>> findAllImportAuth(){
        List<ImportAuth> artifactList = importAuthService.findAllImportAuth();

        return Result.ok(artifactList);
    }

    @RequestMapping(path = "/findImportAuthList",method = RequestMethod.POST)
    @ApiMethod(name = "findImportAuthList",desc = "通过条件查询")
    @ApiParam(name = "ImportAuthQuery",desc = "ImportAuthQuery",required = true)
    public Result<List<ImportAuth>> findImportAuthList(@RequestBody @Valid @NotNull ImportAuthQuery importAuthQuery){
        List<ImportAuth> artifactList = importAuthService.findImportAuthList(importAuthQuery);

        return Result.ok(artifactList);
    }


}
