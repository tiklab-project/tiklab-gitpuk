package io.thoughtware.gittok.repository.controller;

import io.thoughtware.core.Result;
import io.thoughtware.gittok.repository.model.RepositoryClean;
import io.thoughtware.gittok.repository.model.RepositoryCleanQuery;
import io.thoughtware.gittok.repository.service.RepositoryCleanService;
import io.thoughtware.postin.annotation.Api;
import io.thoughtware.postin.annotation.ApiMethod;
import io.thoughtware.postin.annotation.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/repositoryClean")
@Api(name = "RepositoryCleanController",desc = "清理仓库")
public class RepositoryCleanController {

    @Autowired
    RepositoryCleanService cleanService;

    @RequestMapping(path="/findLargeFile",method = RequestMethod.POST)
    @ApiMethod(name = "findLargeFile",desc = "条件查询大文件")
    @ApiParam(name = "repository",desc = "repository",required = true)
    public Result<String> findLargeFile(@RequestBody @NotNull @Valid RepositoryCleanQuery repositoryCleanQuery){
        String file = cleanService.findLargeFile(repositoryCleanQuery);

        return Result.ok(file);
    }

    @RequestMapping(path="/findLargeFileResult",method = RequestMethod.POST)
    @ApiMethod(name = "findLargeFileResult",desc = "条件查询大文件结果")
    @ApiParam(name = "repository",desc = "repository",required = true)
    public Result<List<RepositoryClean>> findLargeFileResult(@RequestBody @NotNull @Valid RepositoryCleanQuery repositoryCleanQuery){
        List<RepositoryClean> repositoryClean  =  cleanService.findLargeFileResult(repositoryCleanQuery);

        return Result.ok(repositoryClean);
    }

    @RequestMapping(path="/execCleanFile",method = RequestMethod.POST)
    @ApiMethod(name = "execCleanFile",desc = "执行清理裸仓库中你的无效文件")
    @ApiParam(name = "execCleanFile",desc = "repositoryCleanQuery",required = true)
    public Result<String> execCleanFile(String rpyId){
        String largeFile = cleanService.execCleanFile(rpyId);

        return Result.ok(largeFile);
    }


    @RequestMapping(path="/clearLargeFile",method = RequestMethod.POST)
    @ApiMethod(name = "clearLargeFile",desc = "删除大文件")
    @ApiParam(name = "repositoryCleanQuery",desc = "repositoryCleanQuery",required = true)
    public Result<String> clearLargeFile(@RequestBody @NotNull @Valid RepositoryCleanQuery repositoryCleanQuery){
        String largeFile = cleanService.clearLargeFile(repositoryCleanQuery);

        return Result.ok(largeFile);
    }

    @RequestMapping(path="/findClearResult",method = RequestMethod.POST)
    @ApiMethod(name = "findClearResult",desc = "获取清除结果")
    @ApiParam(name = "repositoryId",desc = "repositoryId",required = true)
    public Result<Map> findClearResult(@NotNull String repositoryId){
        Map<String,String> result=cleanService.findClearResult(repositoryId);

        return Result.ok(result);
    }


}
