package io.tiklab.gitpuk.repository.controller;

import io.tiklab.core.Result;
import io.tiklab.gitpuk.repository.model.RepositoryLfs;
import io.tiklab.gitpuk.repository.model.RepositoryLfsQuery;
import io.tiklab.gitpuk.repository.service.RepositoryLfsService;
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
 * RepositoryLfsController
 */
@RestController
@RequestMapping("/repositoryLfs")
@Api(name = "RepositoryLfsController",desc = "仓库lfs文件")
public class RepositoryLfsController {

    private static Logger logger = LoggerFactory.getLogger(RepositoryLfsController.class);

    @Autowired
    private RepositoryLfsService repositoryLfsService;


    @RequestMapping(path="/createRepositoryLfs",method = RequestMethod.POST)
    @ApiMethod(name = "createRepositoryLfs",desc = "创建仓库lfs文件")
    @ApiParam(name = "RepositoryLfs",desc = "RepositoryLfs",required = true)
    public Result<String> createRepositoryLfs(@RequestBody @NotNull @Valid RepositoryLfs repositoryLfs){
        String id = repositoryLfsService.createRepositoryLfs(repositoryLfs);

        return Result.ok(id);
    }

    @RequestMapping(path="/updateRepositoryLfs",method = RequestMethod.POST)
    @ApiMethod(name = "updateRepositoryLfs",desc = "修改仓库lfs文件")
    @ApiParam(name = "RepositoryLfs",desc = "RepositoryLfs",required = true)
    public Result<Void> updateRepositoryLfs(@RequestBody @NotNull @Valid RepositoryLfs repositoryLfs){
        repositoryLfsService.updateRepositoryLfs(repositoryLfs);

        return Result.ok();
    }

    @RequestMapping(path="/deleteRepositoryLfs",method = RequestMethod.POST)
    @ApiMethod(name = "deleteRepositoryLfs",desc = "删除")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<Void> deleteRepositoryLfs(@NotNull String id){
        repositoryLfsService.deleteRepositoryLfs(id);

        return Result.ok();
    }

    @RequestMapping(path="/findRepositoryLfs",method = RequestMethod.POST)
    @ApiMethod(name = "findRepositoryLfs",desc = "通过id 查询")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<RepositoryLfs> findRepositoryLfs(@NotNull String id){
        RepositoryLfs RepositoryLfs = repositoryLfsService.findRepositoryLfs(id);

        return Result.ok(RepositoryLfs);
    }

    @RequestMapping(path="/findAllRepositoryLfs",method = RequestMethod.POST)
    @ApiMethod(name = "findAllRepositoryLfs",desc = "查询所有查询")
    public Result<List<RepositoryLfs>> findAllRepositoryLfs(){
        List<RepositoryLfs> artifactList = repositoryLfsService.findAllRepositoryLfs();

        return Result.ok(artifactList);
    }

    @RequestMapping(path = "/findRepositoryLfsList",method = RequestMethod.POST)
    @ApiMethod(name = "findRepositoryLfsList",desc = "通过条件查询")
    @ApiParam(name = "RepositoryLfsQuery",desc = "RepositoryLfsQuery",required = true)
    public Result<List<RepositoryLfs>> findRepositoryLfsList(@RequestBody @Valid @NotNull RepositoryLfsQuery repositoryLfsQuery){
        List<RepositoryLfs> artifactList = repositoryLfsService.findRepositoryLfsList(repositoryLfsQuery);

        return Result.ok(artifactList);
    }



}
