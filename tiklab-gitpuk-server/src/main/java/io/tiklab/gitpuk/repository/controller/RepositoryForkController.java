package io.tiklab.gitpuk.repository.controller;

import io.tiklab.core.Result;
import io.tiklab.core.page.Pagination;
import io.tiklab.gitpuk.repository.model.RepositoryFork;
import io.tiklab.gitpuk.repository.model.RepositoryForkQuery;
import io.tiklab.gitpuk.repository.service.RepositoryForkService;
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
 * RepositoryForkController
 */
@RestController
@RequestMapping("/repositoryFork")
//@Api(name = "RepositoryForkController",desc = "仓库fork")
public class RepositoryForkController {

    private static Logger logger = LoggerFactory.getLogger(RepositoryForkController.class);

    @Autowired
    private RepositoryForkService repositoryForkService;


    @RequestMapping(path="/createRepositoryFork",method = RequestMethod.POST)
    @ApiMethod(name = "createRepositoryFork",desc = "创建仓库fork")
    @ApiParam(name = "repositoryFork",desc = "repositoryFork",required = true)
    public Result<String> createRepositoryFork(@RequestBody @NotNull @Valid RepositoryFork repositoryFork){
        String id = repositoryForkService.createRepositoryFork(repositoryFork);

        return Result.ok(id);
    }




    @RequestMapping(path="/updateRepositoryFork",method = RequestMethod.POST)
    @ApiMethod(name = "updateRepositoryFork",desc = "修改仓库fork")
    @ApiParam(name = "repositoryFork",desc = "repositoryFork",required = true)
    public Result<Void> updateRepositoryFork(@RequestBody @NotNull @Valid RepositoryFork repositoryFork){
        repositoryForkService.updateRepositoryFork(repositoryFork);

        return Result.ok();
    }

    @RequestMapping(path="/deleteRepositoryFork",method = RequestMethod.POST)
    @ApiMethod(name = "deleteRepositoryFork",desc = "删除")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<Void> deleteRepositoryFork(@NotNull String id){
        repositoryForkService.deleteRepositoryFork(id);

        return Result.ok();
    }

    @RequestMapping(path="/findRepositoryFork",method = RequestMethod.POST)
    @ApiMethod(name = "findRepositoryFork",desc = "通过id 查询")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<RepositoryFork> findRepositoryFork(@NotNull String id){
        RepositoryFork RepositoryFork = repositoryForkService.findRepositoryFork(id);

        return Result.ok(RepositoryFork);
    }

    @RequestMapping(path="/findAllRepositoryFork",method = RequestMethod.POST)
    @ApiMethod(name = "findAllRepositoryFork",desc = "查询所有查询")
    public Result<List<RepositoryFork>> findAllRepositoryFork(){
        List<RepositoryFork> artifactList = repositoryForkService.findAllRepositoryFork();

        return Result.ok(artifactList);
    }

    @RequestMapping(path = "/findRepositoryForkList",method = RequestMethod.POST)
    @ApiMethod(name = "findRepositoryForkList",desc = "通过条件查询")
    @ApiParam(name = "repositoryForkQuery",desc = "repositoryForkQuery",required = true)
    public Result<List<RepositoryFork>> findRepositoryForkList(@RequestBody @Valid @NotNull RepositoryForkQuery repositoryForkQuery){
        List<RepositoryFork> artifactList = repositoryForkService.findRepositoryForkList(repositoryForkQuery);

        return Result.ok(artifactList);
    }

    @RequestMapping(path = "/findRepositoryForkPage",method = RequestMethod.POST)
    @ApiMethod(name = "findRepositoryForkPage",desc = "通过条件分页查询")
    @ApiParam(name = "repositoryForkQuery",desc = "repositoryForkQuery",required = true)
    public Result<List<Pagination<RepositoryFork>>> findRepositoryForkPage(@RequestBody @Valid @NotNull RepositoryForkQuery repositoryForkQuery){
        Pagination<RepositoryFork> repositoryForkPage = repositoryForkService.findRepositoryForkPage(repositoryForkQuery);

        return Result.ok(repositoryForkPage);
    }

    @RequestMapping(path="/execRepositoryFork",method = RequestMethod.POST)
    @ApiMethod(name = "execRepositoryFork",desc = "执行仓库fork")
    @ApiParam(name = "repositoryFork",desc = "repositoryFork",required = true)
    public Result<String> execRepositoryFork(@RequestBody @Valid @NotNull RepositoryFork repositoryFork){
        String result = repositoryForkService.execRepositoryFork(repositoryFork);

        return Result.ok(result);
    }

    @RequestMapping(path="/findForkResult",method = RequestMethod.POST)
    @ApiMethod(name = "findForkResult",desc = "查询fork结果")
    @ApiParam(name = "repositoryId",desc = "仓库id",required = true)
    public Result<Integer> findForkResult( @NotNull String repositoryId){
        Integer result = repositoryForkService.findForkResult(repositoryId);

        return Result.ok(result);
    }
}
