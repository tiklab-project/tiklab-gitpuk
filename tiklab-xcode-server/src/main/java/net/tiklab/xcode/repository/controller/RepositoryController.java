package net.tiklab.xcode.repository.controller;

import net.tiklab.core.Result;
import net.tiklab.postin.annotation.Api;
import net.tiklab.postin.annotation.ApiMethod;
import net.tiklab.postin.annotation.ApiParam;
import net.tiklab.xcode.repository.model.Repository;
import net.tiklab.xcode.repository.model.RepositoryCloneAddress;
import net.tiklab.xcode.repository.service.RepositoryServer;
import net.tiklab.xcode.file.model.FileTree;
import net.tiklab.xcode.file.model.FileTreeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/code")
@Api(name = "RepositoryController",desc = "仓库")
public class RepositoryController {
    
    @Autowired
    private RepositoryServer repositoryServer;


    @RequestMapping(path="/createCode",method = RequestMethod.POST)
    @ApiMethod(name = "createCode",desc = "创建仓库")
    @ApiParam(name = "repository",desc = "repository",required = true)
    public Result<String> createCode(@RequestBody @NotNull @Valid Repository repository){

        String codeId = repositoryServer.createCode(repository);

        return Result.ok(codeId);
    }


    @RequestMapping(path="/deleteCode",method = RequestMethod.POST)
    @ApiMethod(name = "deleteCode",desc = "删除仓库")
    @ApiParam(name = "codeId",desc = "仓库id",required = true)
    public Result<Void> deleteCode(@NotNull String codeId){

        repositoryServer.deleteCode(codeId);

        return Result.ok();
    }


    @RequestMapping(path="/updateCode",method = RequestMethod.POST)
    @ApiMethod(name = "updateCode",desc = "更新仓库")
    @ApiParam(name = "repository",desc = "repository",required = true)
    public Result<Void> updateCode(@RequestBody @NotNull @Valid Repository repository){

        repositoryServer.updateCode(repository);

        return Result.ok();
    }

    @RequestMapping(path="/findUserCode",method = RequestMethod.POST)
    @ApiMethod(name = "findUserCode",desc = "查询仓库")
    @ApiParam(name = "userId",desc = "用户id",required = true)
    public Result<List<Repository>> findUserCode(@NotNull String userId){

        List<Repository> repositoryList = repositoryServer.findUserCode(userId);

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
    @ApiParam(name = "codeId",desc = "仓库id",required = true)
    public Result<RepositoryCloneAddress> findCloneAddress(@NotNull String codeId){

        RepositoryCloneAddress cloneAddress = repositoryServer.findCloneAddress(codeId);

        return Result.ok(cloneAddress);
    }


    @RequestMapping(path="/findNameCode",method = RequestMethod.POST)
    @ApiMethod(name = "findNameCode",desc = "查询仓库名称")
    @ApiParam(name = "codeName",desc = "仓库名称",required = true)
    public Result<Repository> findNameCode(@NotNull String codeName){

        Repository nameRepository = repositoryServer.findNameCode(codeName);

        return Result.ok(nameRepository);
    }
    
    
    
    
}













































