package io.thoughtware.gittok.file.controller;

import io.thoughtware.gittok.file.model.FileMessage;
import io.thoughtware.gittok.file.model.FileQuery;
import io.thoughtware.core.Result;
import io.thoughtware.gittok.file.model.RepositoryFile;
import io.thoughtware.gittok.file.service.RepositoryFileServer;
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


@RestController
@RequestMapping("/repositoryFile")
@Api(name = "RepositoryFileController",desc = "仓库文件文件")
public class RepositoryFileController {

    @Autowired
    RepositoryFileServer repositoryFileServer;

    @RequestMapping(path="/createBareFolder",method = RequestMethod.POST)
    @ApiMethod(name = "createBareFolder",desc = "创建裸仓库文件夹")
    @ApiParam(name = "repositoryFile",desc = "repositoryFile",required = true)
    public Result<Void> createBareFolder(@NotNull @RequestBody @Valid RepositoryFile repositoryFile){

        repositoryFileServer.createBareFolder(repositoryFile);

        return Result.ok();
    }

    @RequestMapping(path="/deleteBareFile",method = RequestMethod.POST)
    @ApiMethod(name = "deleteBareFile",desc = "删除裸仓库文件")
    @ApiParam(name = "repositoryFile",desc = "repositoryFile",required = true)
    public Result<Void> deleteBareFile(@NotNull @RequestBody @Valid RepositoryFile repositoryFile){

        repositoryFileServer.deleteBareFile(repositoryFile);

        return Result.ok();
    }

    @RequestMapping(path="/updateBareFile",method = RequestMethod.POST)
    @ApiMethod(name = "updateBareFile",desc = "更新裸仓库文件内容")
    @ApiParam(name = "repositoryFile",desc = "repositoryFile",required = true)
    public Result<Void> updateBareFile(@NotNull @RequestBody @Valid RepositoryFile repositoryFile){

        repositoryFileServer.updateBareFile(repositoryFile);

        return Result.ok();
    }


    @RequestMapping(path="/readFile",method = RequestMethod.POST)
    @ApiMethod(name = "readFile",desc = "读取文件")
    @ApiParam(name = "repositoryFileQuery",desc = "文件信息",required = true)
    public Result<FileMessage> readFile(@NotNull @RequestBody @Valid FileQuery repositoryFileQuery){

        FileMessage file = repositoryFileServer.readFile(repositoryFileQuery);

        return Result.ok(file);
    }

    @RequestMapping(path="/writeFile",method = RequestMethod.POST)
    @ApiMethod(name = "writeFile",desc = "写入文件")
    @ApiParam(name = "rpyId",desc = "rpyId",required = true)
    public Result<Void> writeFile(@NotNull @RequestBody @Valid FileQuery fileQuery){

        repositoryFileServer.writeFile(fileQuery);

        return Result.ok();
    }

}

























































