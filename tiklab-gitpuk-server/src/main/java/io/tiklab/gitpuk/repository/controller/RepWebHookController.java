package io.tiklab.gitpuk.repository.controller;

import io.tiklab.core.Result;
import io.tiklab.core.page.Pagination;
import io.tiklab.gitpuk.repository.model.RepWebHook;
import io.tiklab.gitpuk.repository.model.RepWebHookQuery;
import io.tiklab.gitpuk.repository.model.Repository;
import io.tiklab.gitpuk.repository.model.RepositoryQuery;
import io.tiklab.gitpuk.repository.service.RepWebHookService;
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
 * RepWebHookController
 */
@RestController
@RequestMapping("/repRepWebHook")
//@Api(name = "RepWebHookController",desc = "repRepWebHook的认证")
public class RepWebHookController {

    private static Logger logger = LoggerFactory.getLogger(RepWebHookController.class);

    @Autowired
    private RepWebHookService repRepWebHookService;


    @RequestMapping(path="/createRepWebHook",method = RequestMethod.POST)
    @ApiMethod(name = "createRepWebHook",desc = "创建repRepWebHook")
    @ApiParam(name = "repRepWebHook",desc = "repRepWebHook",required = true)
    public Result<String> createRepWebHook(@RequestBody @NotNull @Valid RepWebHook repRepWebHook){
        String id = repRepWebHookService.createRepWebHook(repRepWebHook);

        return Result.ok(id);
    }

    @RequestMapping(path="/updateRepWebHook",method = RequestMethod.POST)
    @ApiMethod(name = "updateRepWebHook",desc = "修改repRepWebHook")
    @ApiParam(name = "repRepWebHook",desc = "repRepWebHook",required = true)
    public Result<Void> updateRepWebHook(@RequestBody @NotNull @Valid RepWebHook repRepWebHook){
        repRepWebHookService.updateRepWebHook(repRepWebHook);

        return Result.ok();
    }

    @RequestMapping(path="/deleteRepWebHook",method = RequestMethod.POST)
    @ApiMethod(name = "deleteRepWebHook",desc = "删除")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<Void> deleteRepWebHook(@NotNull String id){
        repRepWebHookService.deleteRepWebHook(id);

        return Result.ok();
    }

    @RequestMapping(path="/findRepWebHook",method = RequestMethod.POST)
    @ApiMethod(name = "findRepWebHook",desc = "通过id 查询")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<RepWebHook> findRepWebHook(@NotNull String id){
        RepWebHook RepWebHook = repRepWebHookService.findRepWebHook(id);

        return Result.ok(RepWebHook);
    }

    @RequestMapping(path="/findAllRepWebHook",method = RequestMethod.POST)
    @ApiMethod(name = "findAllRepWebHook",desc = "查询所有查询")
    public Result<List<RepWebHook>> findAllRepWebHook(){
        List<RepWebHook> artifactList = repRepWebHookService.findAllRepWebHook();

        return Result.ok(artifactList);
    }

    @RequestMapping(path = "/findRepWebHookList",method = RequestMethod.POST)
    @ApiMethod(name = "findRepWebHookList",desc = "通过条件查询")
    @ApiParam(name = "repRepWebHookQuery",desc = "repRepWebHookQuery",required = true)
    public Result<List<RepWebHook>> findRepWebHookList(@RequestBody @Valid @NotNull RepWebHookQuery repRepWebHookQuery){
        List<RepWebHook> artifactList = repRepWebHookService.findRepWebHookList(repRepWebHookQuery);

        return Result.ok(artifactList);
    }

    @RequestMapping(path="/findRepWebHookPag",method = RequestMethod.POST)
    @ApiMethod(name = "findRepWebHookPag",desc = "条件分页查询")
    @ApiParam(name = "repRepWebHookQuery",desc = "repositoryQuery",required = true)
    public Result<Pagination<RepWebHook>> findRepWebHookPag(@RequestBody @NotNull @Valid RepWebHookQuery repRepWebHookQuery){

        Pagination<RepWebHook> artifactList = repRepWebHookService.findRepWebHookPag(repRepWebHookQuery);

        return Result.ok(artifactList);
    }


}
