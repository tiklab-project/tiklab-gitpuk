package io.tiklab.xcode.repository.controller;

import io.tiklab.core.page.Pagination;
import io.tiklab.xcode.file.model.FileTree;
import io.tiklab.xcode.file.model.FileTreeMessage;
import io.tiklab.xcode.repository.model.RepositoryCloneAddress;
import io.tiklab.core.Result;
import io.tiklab.postin.annotation.Api;
import io.tiklab.postin.annotation.ApiMethod;
import io.tiklab.postin.annotation.ApiParam;
import io.tiklab.xcode.repository.model.Repository;
import io.tiklab.xcode.repository.model.RepositoryQuery;
import io.tiklab.xcode.repository.service.RepositoryServer;
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
    private RepositoryServer repositoryServer;


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



    @RequestMapping(path="/findFileTree",method = RequestMethod.POST)
    @ApiMethod(name = "findFileTree",desc = "查询仓库")
    @ApiParam(name = "message",desc = "文件信息",required = true)
    public Result< List<FileTree>> findFileTree(@RequestBody @NotNull @Valid FileTreeMessage message){

        List<FileTree> fileTree = repositoryServer.findFileTree(message);

        return Result.ok(fileTree);
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

    @RequestMapping(path="/findRepositoryByGroupName",method = RequestMethod.POST)
    @ApiMethod(name = "findRepositoryByGroupName",desc = "通过仓库组名字查询仓库列表")
    @ApiParam(name = "address",desc = "address",required = true)
    public Result<List<Repository>> findRepositoryByGroupName( @NotNull String groupName){

        List<Repository> repository= repositoryServer.findRepositoryByGroupName(groupName);

        return Result.ok(repository);
    }

    @RequestMapping(path="/deleteRpyByAddress",method = RequestMethod.POST)
    @ApiMethod(name = "deleteRpyByAddress",desc = "根据路径删除")
    @ApiParam(name = "address",desc = "仓库id",required = true)
    public Result<Void> deleteRpyByAddress(@NotNull String address){

        repositoryServer.deleteRpyByAddress(address);

        return Result.ok();
    }

}













































