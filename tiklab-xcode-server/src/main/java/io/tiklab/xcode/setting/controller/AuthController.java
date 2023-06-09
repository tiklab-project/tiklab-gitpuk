package io.tiklab.xcode.setting.controller;

import io.tiklab.xcode.setting.service.AuthServer;
import io.tiklab.core.Result;
import io.tiklab.postin.annotation.Api;
import io.tiklab.postin.annotation.ApiMethod;
import io.tiklab.postin.annotation.ApiParam;
import io.tiklab.xcode.setting.model.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/auth")
@Api(name = "AuthController",desc = "认证")
public class AuthController {

    @Autowired
    private AuthServer authServer;

    @RequestMapping(path="/createAuth",method = RequestMethod.POST)
    @ApiMethod(name = "createAuth",desc = "创建认证")
    @ApiParam(name = "auth",desc = "auth",required = true)
    public Result<String> createAuth(@RequestBody @NotNull @Valid Auth auth){

        String Id = authServer.createAuth(auth);

        return Result.ok(Id);
    }


    @RequestMapping(path="/deleteAuth",method = RequestMethod.POST)
    @ApiMethod(name = "deleteAuth",desc = "删除认证")
    @ApiParam(name = "Id",desc = "认证id",required = true)
    public Result<Void> deleteAuth(@NotNull String authId){

        authServer.deleteAuth(authId);

        return Result.ok();
    }


    @RequestMapping(path="/findOneAuth",method = RequestMethod.POST)
    @ApiMethod(name = "findOneAuth",desc = "查询单个认证")
    @ApiParam(name = "authId",desc = "认证id",required = true)
    public Result<Auth> findOneAuth(@NotNull String authId){

        Auth auth = authServer.findOneAuth(authId);

        return Result.ok(auth);
    }



    @RequestMapping(path="/findUserAuth",method = RequestMethod.POST)
    @ApiMethod(name = "findUserAuth",desc = "查询认证")
    public Result<List<Auth>> findUserAuth(){

        List<Auth> List = authServer.findUserAuth();

        return Result.ok(List);
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}

























