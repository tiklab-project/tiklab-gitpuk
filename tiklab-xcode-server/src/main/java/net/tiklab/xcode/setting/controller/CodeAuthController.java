package net.tiklab.xcode.setting.controller;

import net.tiklab.core.Result;
import net.tiklab.postin.annotation.Api;
import net.tiklab.postin.annotation.ApiMethod;
import net.tiklab.postin.annotation.ApiParam;
import net.tiklab.xcode.setting.model.CodeAuth;
import net.tiklab.xcode.setting.service.CodeAuthServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/codeAuth")
@Api(name = "CodeAuthController",desc = "认证")
public class CodeAuthController {

    @Autowired
    private CodeAuthServer authServer;

    @RequestMapping(path="/createAuth",method = RequestMethod.POST)
    @ApiMethod(name = "createAuth",desc = "创建认证")
    @ApiParam(name = "code",desc = "code",required = true)
    public Result<String> createAuth(@RequestBody @NotNull @Valid CodeAuth auth){

        String codeId = authServer.createCodeAuth(auth);

        return Result.ok(codeId);
    }


    @RequestMapping(path="/deleteAuth",method = RequestMethod.POST)
    @ApiMethod(name = "deleteAuth",desc = "删除认证")
    @ApiParam(name = "codeId",desc = "认证id",required = true)
    public Result<Void> deleteAuth(@NotNull String authId){

        authServer.deleteCodeAuth(authId);

        return Result.ok();
    }


    @RequestMapping(path="/findOneAuth",method = RequestMethod.POST)
    @ApiMethod(name = "findOneAuth",desc = "查询单个认证")
    @ApiParam(name = "authId",desc = "认证id",required = true)
    public Result<CodeAuth> findOneAuth(@NotNull String authId){

        CodeAuth auth = authServer.findOneCodeAuth(authId);

        return Result.ok(auth);
    }



    @RequestMapping(path="/findUserAuth",method = RequestMethod.POST)
    @ApiMethod(name = "findUserAuth",desc = "查询认证")
    public Result<List<CodeAuth>> findUserAuth(){

        List<CodeAuth> codeList = authServer.findUserAuth();

        return Result.ok(codeList);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}

























