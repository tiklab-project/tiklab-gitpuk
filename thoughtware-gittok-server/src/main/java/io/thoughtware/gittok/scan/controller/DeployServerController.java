package io.thoughtware.gittok.scan.controller;

import io.thoughtware.gittok.scan.model.DeployServer;
import io.thoughtware.gittok.scan.model.DeployServerQuery;
import io.thoughtware.gittok.scan.service.DeployServerService;
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
 * DeployServerController
 */
@RestController
@RequestMapping("/deployServer")
@Api(name = "DeployServerController",desc = "第三方认证服务")
public class DeployServerController {

    private static Logger logger = LoggerFactory.getLogger(DeployServerController.class);

    @Autowired
    private DeployServerService deployServerService;

    @RequestMapping(path="/createDeployServer",method = RequestMethod.POST)
    @ApiMethod(name = "createDeployServer",desc = "创建第三方认证服务")
    @ApiParam(name = "DeployServer",desc = "DeployServer",required = true)
    public Result<String> createDeployServer(@RequestBody @NotNull @Valid DeployServer DeployServer){
        String id = deployServerService.createDeployServer(DeployServer);

        return Result.ok(id);
    }

    @RequestMapping(path="/updateDeployServer",method = RequestMethod.POST)
    @ApiMethod(name = "updateDeployServer",desc = "修改第三方认证服务")
    @ApiParam(name = "DeployServer",desc = "DeployServer",required = true)
    public Result<Void> updateDeployServer(@RequestBody @NotNull @Valid DeployServer DeployServer){
        deployServerService.updateDeployServer(DeployServer);

        return Result.ok();
    }

    @RequestMapping(path="/deleteDeployServer",method = RequestMethod.POST)
    @ApiMethod(name = "deleteDeployServer",desc = "删除")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<Void> deleteDeployServer(@NotNull String id){
        deployServerService.deleteDeployServer(id);

        return Result.ok();
    }

    @RequestMapping(path="/findDeployServer",method = RequestMethod.POST)
    @ApiMethod(name = "findDeployServer",desc = "通过id 查询")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<DeployServer> findDeployServer(@NotNull String id){
        DeployServer DeployServer = deployServerService.findDeployServer(id);

        return Result.ok(DeployServer);
    }

    @RequestMapping(path="/findAllDeployServer",method = RequestMethod.POST)
    @ApiMethod(name = "findAllDeployServer",desc = "查询所有查询")
    public Result<List<DeployServer>> findAllDeployServer(){
        List<DeployServer> artifactList = deployServerService.findAllDeployServer();

        return Result.ok(artifactList);
    }

    @RequestMapping(path = "/findDeployServerList",method = RequestMethod.POST)
    @ApiMethod(name = "findDeployServerList",desc = "通过条件查询")
    @ApiParam(name = "DeployServerQuery",desc = "DeployServerQuery",required = true)
    public Result<List<DeployServer>> findDeployServerList(@RequestBody @Valid @NotNull DeployServerQuery deployServerQuery){
        List<DeployServer> artifactList = deployServerService.findDeployServerList(deployServerQuery);

        return Result.ok(artifactList);
    }

    @RequestMapping(path = "/findDeployServerPage",method = RequestMethod.POST)
    @ApiMethod(name = "findDeployServerPage",desc = "通过条件分页查询")
    @ApiParam(name = "DeployServerQuery",desc = "DeployServerQuery",required = true)
    public Result<Pagination<DeployServer>> findDeployServerPage(@RequestBody @Valid @NotNull DeployServerQuery deployServerQuery){
        Pagination<DeployServer> pagination = deployServerService.findDeployServerPage(deployServerQuery);

        return Result.ok(pagination);
    }


}
