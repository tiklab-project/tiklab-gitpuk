package net.tiklab.xcode.repository.controller;

import net.tiklab.core.Result;
import net.tiklab.postin.annotation.Api;
import net.tiklab.postin.annotation.ApiMethod;
import net.tiklab.postin.annotation.ApiParam;
import net.tiklab.xcode.file.model.FileTree;
import net.tiklab.xcode.file.model.FileTreeMessage;
import net.tiklab.xcode.repository.model.Repository;
import net.tiklab.xcode.repository.model.RepositoryCloneAddress;
import net.tiklab.xcode.repository.service.RepositoryServer;
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

        String rpyId = repositoryServer.createRpy(repository);

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


    @RequestMapping(path="/findNameRpy",method = RequestMethod.POST)
    @ApiMethod(name = "findName",desc = "查询仓库名称")
    @ApiParam(name = "Name",desc = "仓库名称",required = true)
    public Result<Repository> findName(@NotNull String rpyName){

        Repository nameRepository = repositoryServer.findNameRpy(rpyName);

        return Result.ok(nameRepository);
    }
    
    
    
    
}













































