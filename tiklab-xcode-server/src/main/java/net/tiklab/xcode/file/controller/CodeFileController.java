package net.tiklab.xcode.file.controller;

import net.tiklab.core.Result;
import net.tiklab.postin.annotation.Api;
import net.tiklab.postin.annotation.ApiMethod;
import net.tiklab.postin.annotation.ApiParam;
import net.tiklab.xcode.commit.model.CommitMessage;
import net.tiklab.xcode.commit.service.CodeCommitServer;
import net.tiklab.xcode.file.model.CodeFile;
import net.tiklab.xcode.file.model.CodeFileMessage;
import net.tiklab.xcode.file.service.CodeFileServer;
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
    CodeFileServer fileServer;

    @RequestMapping(path="/readFile",method = RequestMethod.POST)
    @ApiMethod(name = "readFile",desc = "读取文件")
    @ApiParam(name = "codeFile",desc = "文件信息",required = true)
    public Result<CodeFileMessage> readFile(@NotNull @RequestBody @Valid CodeFile codeFile){

        CodeFileMessage file = fileServer.readFile(codeFile);

        return Result.ok(file);
    }

    @RequestMapping(path="/writeFile",method = RequestMethod.POST)
    @ApiMethod(name = "writeFile",desc = "读取文件")
    @ApiParam(name = "codeId",desc = "codeId",required = true)
    public Result<Void> writeFile(@NotNull @RequestBody @Valid CodeFile codeFile){

      fileServer.writeFile(codeFile);

        return Result.ok();
    }





}

























































