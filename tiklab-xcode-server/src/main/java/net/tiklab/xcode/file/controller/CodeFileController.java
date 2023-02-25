package net.tiklab.xcode.file.controller;

import net.tiklab.core.Result;
import net.tiklab.postin.annotation.Api;
import net.tiklab.postin.annotation.ApiMethod;
import net.tiklab.postin.annotation.ApiParam;
import net.tiklab.xcode.file.model.FileQuery;
import net.tiklab.xcode.file.model.FileMessage;
import net.tiklab.xcode.file.service.FileServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@RestController
@RequestMapping("/codeFile")
@Api(name = "CodeFileController",desc = "文件")
public class CodeFileController {

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
    @ApiParam(name = "codeId",desc = "codeId",required = true)
    public Result<Void> writeFile(@NotNull @RequestBody @Valid FileQuery fileQuery){

      fileServer.writeFile(fileQuery);

        return Result.ok();
    }





}

























































