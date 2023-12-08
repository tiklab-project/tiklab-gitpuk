package io.thoughtware.gittork.scan.controller;

import io.thoughtware.gittork.scan.model.CodeScanInstance;
import io.thoughtware.gittork.scan.model.CodeScanInstanceQuery;
import io.thoughtware.gittork.scan.service.CodeScanInstanceService;
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
 * CodeScanInstanceController
 */
@RestController
@RequestMapping("/codeScanInstance")
@Api(name = "CodeScanInstanceController",desc = "查询扫描实例")
public class CodeScanInstanceController {

    private static Logger logger = LoggerFactory.getLogger(CodeScanInstanceController.class);

    @Autowired
    CodeScanInstanceService codeScanInstanceService;

    @RequestMapping(path="/createCodeScanInstance",method = RequestMethod.POST)
    @ApiMethod(name = "createCodeScanInstance",desc = "创建扫描实例")
    @ApiParam(name = "CodeScanInstance",desc = "CodeScanInstance",required = true)
    public Result<String> createCodeScanInstance(@RequestBody @NotNull @Valid CodeScanInstance CodeScanInstance){
        String id = codeScanInstanceService.createCodeScanInstance(CodeScanInstance);

        return Result.ok(id);
    }

    @RequestMapping(path="/updateCodeScanInstance",method = RequestMethod.POST)
    @ApiMethod(name = "updateCodeScanInstance",desc = "修改扫描实例")
    @ApiParam(name = "CodeScanInstance",desc = "CodeScanInstance",required = true)
    public Result<Void> updateCodeScanInstance(@RequestBody @NotNull @Valid CodeScanInstance CodeScanInstance){
        codeScanInstanceService.updateCodeScanInstance(CodeScanInstance);

        return Result.ok();
    }

    @RequestMapping(path="/deleteCodeScanInstance",method = RequestMethod.POST)
    @ApiMethod(name = "deleteCodeScanInstance",desc = "删除")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<Void> deleteCodeScanInstance(@NotNull String id){
        codeScanInstanceService.deleteCodeScanInstance(id);

        return Result.ok();
    }

    @RequestMapping(path="/findCodeScanInstance",method = RequestMethod.POST)
    @ApiMethod(name = "findCodeScanInstance",desc = "通过id 查询")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<CodeScanInstance> findCodeScanInstance(@NotNull String id){
        CodeScanInstance CodeScanInstance = codeScanInstanceService.findCodeScanInstance(id);

        return Result.ok(CodeScanInstance);
    }

    @RequestMapping(path="/findAllCodeScanInstance",method = RequestMethod.POST)
    @ApiMethod(name = "findAllCodeScanInstance",desc = "查询所有查询")
    public Result<List<CodeScanInstance>> findAllCodeScanInstance(){
        List<CodeScanInstance> artifactList = codeScanInstanceService.findAllCodeScanInstance();

        return Result.ok(artifactList);
    }

    @RequestMapping(path = "/findCodeScanInstanceList",method = RequestMethod.POST)
    @ApiMethod(name = "findCodeScanInstanceList",desc = "通过条件查询")
    @ApiParam(name = "CodeScanInstanceQuery",desc = "CodeScanInstanceQuery",required = true)
    public Result<List<CodeScanInstance>> findCodeScanInstanceList(@RequestBody @Valid @NotNull CodeScanInstanceQuery codeScanInstanceQuery){
        List<CodeScanInstance> artifactList = codeScanInstanceService.findCodeScanInstanceList(codeScanInstanceQuery);

        return Result.ok(artifactList);
    }

    @RequestMapping(path = "/findCodeScanInstancePage",method = RequestMethod.POST)
    @ApiMethod(name = "findCodeScanInstancePage",desc = "通过条件分页查询")
    @ApiParam(name = "CodeScanInstanceQuery",desc = "CodeScanInstanceQuery",required = true)
    public Result<Pagination<CodeScanInstance>> findCodeScanInstancePage(@RequestBody @Valid @NotNull CodeScanInstanceQuery codeScanInstanceQuery){
        Pagination<CodeScanInstance> pagination = codeScanInstanceService.findCodeScanInstancePage(codeScanInstanceQuery);

        return Result.ok(pagination);
    }


}
