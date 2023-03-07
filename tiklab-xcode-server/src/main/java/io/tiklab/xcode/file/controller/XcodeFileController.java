package io.tiklab.xcode.file.controller;

import io.tiklab.xcode.file.service.FileServer;
import io.tiklab.core.Result;
import io.tiklab.postin.annotation.Api;
import io.tiklab.postin.annotation.ApiMethod;
import io.tiklab.postin.annotation.ApiParam;
import io.tiklab.xcode.file.model.FileQuery;
import io.tiklab.xcode.file.model.FileMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@RestController
@RequestMapping("/file")
@Api(name = "XcodeFileController",desc = "文件")
public class XcodeFileController {

    @Autowired
    FileServer fileServer;

    @RequestMapping(path="/readFile",method = RequestMethod.POST)
    @ApiMethod(name = "readFile",desc = "读取文件")
    @ApiParam(name = "repositoryFileQuery",desc = "文件信息",required = true)
    public Result<FileMessage> readFile(@NotNull @RequestBody @Valid FileQuery repositoryFileQuery){

        FileMessage file = fileServer.readFile(repositoryFileQuery);

        return Result.ok(file);
    }

    @RequestMapping(path="/writeFile",method = RequestMethod.POST)
    @ApiMethod(name = "writeFile",desc = "读取文件")
    @ApiParam(name = "rpyId",desc = "rpyId",required = true)
    public Result<Void> writeFile(@NotNull @RequestBody @Valid FileQuery fileQuery){

      fileServer.writeFile(fileQuery);

        return Result.ok();
    }





}

























































