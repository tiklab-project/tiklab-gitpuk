package io.tiklab.gitpuk.repository.controller;

import io.tiklab.gitpuk.repository.model.Repository;
import io.tiklab.gitpuk.repository.model.RepositoryCloneAddress;
import io.tiklab.gitpuk.repository.model.RepositoryQuery;
import io.tiklab.gitpuk.repository.service.RepositoryService;
import io.tiklab.core.Result;
import io.tiklab.core.page.Pagination;
import io.tiklab.postin.annotation.Api;
import io.tiklab.postin.annotation.ApiMethod;
import io.tiklab.postin.annotation.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/rpy")
@Api(name = "RepositoryController",desc = "仓库")
public class RepositoryController {
    
    @Autowired
    private RepositoryService repositoryServer;

    @RequestMapping(path="/createRpy",method = RequestMethod.POST)
    @ApiMethod(name = "create",desc = "创建仓库")
    @ApiParam(name = "repository",desc = "repository",required = true)
    public Result<String> create(@RequestBody @NotNull @Valid Repository repository){

        String rpyId = repositoryServer.createRpyData(repository);

        return Result.ok(rpyId);
    }


    @RequestMapping(path="/deleteRpy",method = RequestMethod.POST)
    @ApiMethod(name = "delete",desc = "删除仓库")
    @ApiParam(name = "rpyId",desc = "仓库id",required = true)
    public Result<Void> delete(@NotNull String rpyId){

        repositoryServer.deleteRpy(rpyId);
        return Result.ok();
    }


    @RequestMapping(path="/updateRpy",method = RequestMethod.POST)
    @ApiMethod(name = "update",desc = "更新仓库")
    @ApiParam(name = "repository",desc = "repository",required = true)
    public Result<Void> update(@RequestBody @NotNull @Valid Repository repository){

        repositoryServer.updateRpy(repository);

        return Result.ok();
    }


    @RequestMapping(path="/findUserRpy",method = RequestMethod.POST)
    @ApiMethod(name = "findUser",desc = "查询仓库")
    @ApiParam(name = "userId",desc = "用户id",required = true)
    public Result<List<Repository>> findUser(@NotNull String userId){

        List<Repository> repositoryList = repositoryServer.findUserRpy(userId);

        return Result.ok(repositoryList);
    }

    @RequestMapping(path="/findRepositoryByUser",method = RequestMethod.POST)
    @ApiMethod(name = "findRepositoryByUser",desc = "通过用户查询有权限的仓库 Arbess使用")
    @ApiParam(name = "account",desc = "account",required = true)
    public Result<List<Repository>> findRepositoryByUser(@NotNull String account, @NotNull String password, String dirId){

        List<Repository> repositoryList = repositoryServer.findRepositoryByUser(account,password,dirId);

        return Result.ok(repositoryList);
    }



    @RequestMapping(path="/findRepositoryList",method = RequestMethod.POST)
    @ApiMethod(name = "findRepositoryList",desc = "条件查询仓库")
    @ApiParam(name = "repositoryQuery",desc = "repositoryQuery",required = true)
    public Result<List<Repository>> findRepositoryList(@RequestBody @NotNull @Valid RepositoryQuery repositoryQuery){

        List<Repository> repositoryList = repositoryServer.findRepositoryList(repositoryQuery);

        return Result.ok(repositoryList);
    }


    @RequestMapping(path="/findRepositoryPage",method = RequestMethod.POST)
    @ApiMethod(name = "findRepositoryPage",desc = "条件分页查询仓库")
    @ApiParam(name = "repositoryQuery",desc = "repositoryQuery",required = true)
    public Result<Pagination<Repository>> findRepositoryPage(@RequestBody @NotNull @Valid RepositoryQuery repositoryQuery){

        Pagination<Repository> repositoryPage = repositoryServer.findRepositoryPage(repositoryQuery);

        return Result.ok(repositoryPage);
    }


    @RequestMapping(path="/findCloneAddress",method = RequestMethod.POST)
    @ApiMethod(name = "findCloneAddress",desc = "查询仓库克隆地址")
    @ApiParam(name = "rpyId",desc = "仓库id",required = true)
    public Result<RepositoryCloneAddress> findCloneAddress(@NotNull String rpyId){

        RepositoryCloneAddress cloneAddress = repositoryServer.findCloneAddress(rpyId);

        return Result.ok(cloneAddress);
    }

    @RequestMapping(path="/findRepository",method = RequestMethod.POST)
    @ApiMethod(name = "findRepository",desc = "通过仓库id 查询")
    @ApiParam(name = "id",desc = "仓库ID",required = true)
    public Result<Repository> findRepository(@NotNull String id){

        Repository nameRepository = repositoryServer.findRepository(id);

        return Result.ok(nameRepository);
    }


    @RequestMapping(path="/findRepositoryByName",method = RequestMethod.POST)
    @ApiMethod(name = "findRepositoryByName",desc = "通过仓库名字或仓库组查询仓库是否存在")
    @ApiParam(name = "repositoryQuery",desc = "repositoryQuery",required = true)
    public Result<List<Repository>> findRepositoryByName(@RequestBody @NotNull @Valid RepositoryQuery repositoryQuery){

        List<Repository> repositoryByName = repositoryServer.findRepositoryByName(repositoryQuery);

        return Result.ok(repositoryByName);
    }


    @RequestMapping(path="/findRepositoryByAddress",method = RequestMethod.POST)
    @ApiMethod(name = "findRepositoryByAddress",desc = "通过仓库地址匹配")
    @ApiParam(name = "address",desc = "address",required = true)
    public Result<Repository> findRepositoryByAddress( @NotNull String address){

        Repository repository= repositoryServer.findRepositoryByAddress(address);

        return Result.ok(repository);
    }

    @RequestMapping(path="/findGroupRepository",method = RequestMethod.POST)
    @ApiMethod(name = "findGroupRepository",desc = "条件查询仓库组下面的仓库")
    @ApiParam(name = "repositoryQuery",desc = "repositoryQuery",required = true)
    public Result<List<Repository>> findGroupRepository(@RequestBody @NotNull @Valid RepositoryQuery repositoryQuery){

        Pagination<Repository> groupRepository = repositoryServer.findGroupRepository(repositoryQuery);

        return Result.ok(groupRepository);
    }


    @RequestMapping(path="/deleteRpyByAddress",method = RequestMethod.POST)
    @ApiMethod(name = "deleteRpyByAddress",desc = "根据路径删除")
    @ApiParam(name = "address",desc = "仓库id",required = true)
    public Result<Void> deleteRpyByAddress(@NotNull String address){

        repositoryServer.deleteRpyByAddress(address);

        return Result.ok();
    }


    @RequestMapping(path="/findCommitRepository",method = RequestMethod.POST)
    @ApiMethod(name = "findCommitRepository",desc = "查询用户推送过的仓库")
    @ApiParam(name = "userId",desc = "用户id",required = true)
    public Result<List<Repository>> findCommitRepository(@NotNull String userId){
        List<Repository> repositoryList=repositoryServer.findCommitRepository(userId);

        return Result.ok(repositoryList);
    }


    @RequestMapping(path="/findRepositoryListByUser",method = RequestMethod.POST)
    @ApiMethod(name = "findRepositoryListByUser",desc = "通过用户查看用户有可以查看的仓库")
    @ApiParam(name = "repositoryQuery",desc = "repositoryQuery",required = true)
    public Result<Pagination<Repository>> findRepositoryListByUser(@RequestBody @NotNull @Valid RepositoryQuery repositoryQuery){
        Pagination<Repository> repositoryByUser = repositoryServer.findRepositoryListByUser(repositoryQuery);

        return Result.ok(repositoryByUser);
    }


    @RequestMapping(path="/getAddress",method = RequestMethod.POST)
    @ApiMethod(name = "getAddress",desc = "获取当前服务起ip 或配置的域名")
    public Result<String> getAddress(){
        String address=repositoryServer.getAddress();

        return Result.ok(address);
    }


    @RequestMapping(path="/getRepositoryPath",method = RequestMethod.POST)
    @ApiMethod(name = "getRepositoryPath",desc = "获取仓库地址")
    public Result<String> getRepositoryPath(){
        String address=repositoryServer.getRepositoryPath();

        return Result.ok(address);
    }


    @RequestMapping(path="/findRepositoryAuth",method = RequestMethod.POST)
    @ApiMethod(name = "findRepositoryAuth",desc = "查询用户是否有当前项目权限")
    @ApiParam(name = "rpyId",desc = "rpyId",required = true)
    public Result<String> findRepositoryAuth( @NotNull String rpyId){

        String repositoryAuth = repositoryServer.findRepositoryAuth(rpyId);

        return Result.ok(repositoryAuth);
    }

    @RequestMapping(path="/resetRepository",method = RequestMethod.POST)
    @ApiMethod(name = "resetRepository",desc = "重置仓库")
    @ApiParam(name = "rpyId",desc = "rpyId",required = true)
    public Result<String> resetRepository( @NotNull String rpyId){

        repositoryServer.resetRepository(rpyId);

        return Result.ok();
    }

    @RequestMapping(path="/findRefCodeType",method = RequestMethod.POST)
    @ApiMethod(name = "findBareRepoType",desc = "查询仓库展示仓库的类型（分支、标签、提交的仓库）")
    @ApiParam(name = "rpyId",desc = "rpyId",required = true)
    public Result<String> findRefCodeType( @NotNull String refCode,@NotNull String repoId){

        String type=repositoryServer.findRefCodeType(refCode,repoId);

        return Result.ok(type);
    }


}
