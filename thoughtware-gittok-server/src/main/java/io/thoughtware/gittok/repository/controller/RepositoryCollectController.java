package io.thoughtware.gittok.repository.controller;

import io.thoughtware.core.Result;
import io.thoughtware.core.page.Pagination;
import io.thoughtware.gittok.repository.model.RepositoryCollect;
import io.thoughtware.gittok.repository.model.RepositoryCollectQuery;
import io.thoughtware.gittok.repository.service.RepositoryCollectService;
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
 * RepositoryCollectController
 */
@RestController
@RequestMapping("/repositoryCollect")
@Api(name = "RepositoryCollectController",desc = "收藏仓库数据")
public class RepositoryCollectController {

    private static Logger logger = LoggerFactory.getLogger(RepositoryCollectController.class);

    @Autowired
    private RepositoryCollectService repositoryCollectService;

    @RequestMapping(path="/createRepositoryCollect",method = RequestMethod.POST)
    @ApiMethod(name = "createRepositoryCollect",desc = "创建收藏仓库")
    @ApiParam(name = "RepositoryCollect",desc = "RepositoryCollect",required = true)
    public Result<String> createRepositoryCollect(@RequestBody @NotNull @Valid RepositoryCollect RepositoryCollect){
        String id = repositoryCollectService.createRepositoryCollect(RepositoryCollect);

        return Result.ok(id);
    }

    @RequestMapping(path="/updateRepositoryCollect",method = RequestMethod.POST)
    @ApiMethod(name = "updateRepositoryCollect",desc = "修改收藏仓库")
    @ApiParam(name = "RepositoryCollect",desc = "RepositoryCollect",required = true)
    public Result<Void> updateRepositoryCollect(@RequestBody @NotNull @Valid RepositoryCollect RepositoryCollect){
        repositoryCollectService.updateRepositoryCollect(RepositoryCollect);

        return Result.ok();
    }

    @RequestMapping(path="/deleteRepositoryCollect",method = RequestMethod.POST)
    @ApiMethod(name = "deleteRepositoryCollect",desc = "删除")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<Void> deleteRepositoryCollect(@NotNull String id){
        repositoryCollectService.deleteRepositoryCollect(id);

        return Result.ok();
    }

    @RequestMapping(path="/deleteCollectByRpyId",method = RequestMethod.POST)
    @ApiMethod(name = "deleteCollectByCondition",desc = "通过仓库id 删除")
    @ApiParam(name = "repositoryId",desc = "仓库id",required = true)
    public Result<Void> deleteCollectByRpyId(@NotNull String repositoryId){
        repositoryCollectService.deleteCollectByRpyId(repositoryId);

        return Result.ok();
    }

    @RequestMapping(path="/findRepositoryCollect",method = RequestMethod.POST)
    @ApiMethod(name = "findRepositoryCollect",desc = "通过id 查询")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<RepositoryCollect> findRepositoryCollect(@NotNull String id){
        RepositoryCollect RepositoryCollect = repositoryCollectService.findRepositoryCollect(id);

        return Result.ok(RepositoryCollect);
    }

    @RequestMapping(path="/findAllRepositoryCollect",method = RequestMethod.POST)
    @ApiMethod(name = "findAllRepositoryCollect",desc = "查询所有查询")
    public Result<List<RepositoryCollect>> findAllRepositoryCollect(){
        List<RepositoryCollect> artifactList = repositoryCollectService.findAllRepositoryCollect();

        return Result.ok(artifactList);
    }

    @RequestMapping(path = "/findRepositoryCollectList",method = RequestMethod.POST)
    @ApiMethod(name = "findRepositoryCollectList",desc = "通过条件查询")
    @ApiParam(name = "RepositoryCollectQuery",desc = "RepositoryCollectQuery",required = true)
    public Result<List<RepositoryCollect>> findRepositoryCollectList(@RequestBody @Valid @NotNull RepositoryCollectQuery repositoryCollectQuery){
        List<RepositoryCollect> artifactList = repositoryCollectService.findRepositoryCollectList(repositoryCollectQuery);

        return Result.ok(artifactList);
    }

    @RequestMapping(path = "/findRepositoryCollectPage",method = RequestMethod.POST)
    @ApiMethod(name = "findRepositoryCollectPage",desc = "通过条件分页查询")
    @ApiParam(name = "RepositoryCollectQuery",desc = "RepositoryCollectQuery",required = true)
    public Result<Pagination<RepositoryCollect>> findRepositoryCollectPage(@RequestBody @Valid @NotNull RepositoryCollectQuery repositoryCollectQuery){
        Pagination<RepositoryCollect> pagination = repositoryCollectService.findRepositoryCollectPage(repositoryCollectQuery);

        return Result.ok(pagination);
    }


}
