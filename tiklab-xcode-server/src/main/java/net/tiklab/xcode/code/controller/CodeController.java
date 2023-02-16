package net.tiklab.xcode.code.controller;

import net.tiklab.core.Result;
import net.tiklab.postin.annotation.Api;
import net.tiklab.postin.annotation.ApiMethod;
import net.tiklab.postin.annotation.ApiParam;
import net.tiklab.xcode.code.model.Code;
import net.tiklab.xcode.code.model.CodeCloneAddress;
import net.tiklab.xcode.code.model.CodeMessage;
import net.tiklab.xcode.code.service.CodeServer;
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
@Api(name = "CodeController",desc = "仓库")
public class CodeController {
    
    @Autowired
    private CodeServer codeServer;


    @RequestMapping(path="/createCode",method = RequestMethod.POST)
    @ApiMethod(name = "createCode",desc = "创建仓库")
    @ApiParam(name = "code",desc = "code",required = true)
    public Result<String> createCode(@RequestBody @NotNull @Valid Code code){

        String codeId = codeServer.createCode(code);

        return Result.ok(codeId);
    }


    @RequestMapping(path="/deleteCode",method = RequestMethod.POST)
    @ApiMethod(name = "deleteCode",desc = "删除仓库")
    @ApiParam(name = "codeId",desc = "仓库id",required = true)
    public Result<Void> deleteCode(@NotNull String codeId){

        codeServer.deleteCode(codeId);

        return Result.ok();
    }


    @RequestMapping(path="/updateCode",method = RequestMethod.POST)
    @ApiMethod(name = "updateCode",desc = "更新仓库")
    @ApiParam(name = "code",desc = "code",required = true)
    public Result<Void> updateCode(@RequestBody @NotNull @Valid Code code){

        codeServer.updateCode(code);

        return Result.ok();
    }

    @RequestMapping(path="/findUserCode",method = RequestMethod.POST)
    @ApiMethod(name = "findUserCode",desc = "查询仓库")
    @ApiParam(name = "userId",desc = "用户id",required = true)
    public Result<List<Code>> findUserCode(@NotNull String userId){

        List<Code> codeList = codeServer.findUserCode(userId);

        return Result.ok(codeList);
    }


    @RequestMapping(path="/findFileTree",method = RequestMethod.POST)
    @ApiMethod(name = "findFileTree",desc = "查询仓库")
    @ApiParam(name = "message",desc = "文件信息",required = true)
    public Result< List<FileTree>> findFileTree(@RequestBody @NotNull @Valid FileTreeMessage message){

        List<FileTree> fileTree = codeServer.findFileTree(message);

        return Result.ok(fileTree);
    }


    @RequestMapping(path="/findCloneAddress",method = RequestMethod.POST)
    @ApiMethod(name = "findCloneAddress",desc = "查询仓库克隆地址")
    @ApiParam(name = "codeId",desc = "仓库id",required = true)
    public Result<CodeCloneAddress> findCloneAddress(@NotNull String codeId){

        CodeCloneAddress cloneAddress = codeServer.findCloneAddress(codeId);

        return Result.ok(cloneAddress);
    }


    @RequestMapping(path="/findNameCode",method = RequestMethod.POST)
    @ApiMethod(name = "findNameCode",desc = "查询仓库名称")
    @ApiParam(name = "codeName",desc = "仓库名称",required = true)
    public Result<Code> findNameCode(@NotNull String codeName){

        Code nameCode = codeServer.findNameCode(codeName);

        return Result.ok(nameCode);
    }
    
    
    
    
}













































