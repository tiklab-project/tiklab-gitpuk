package io.tiklab.gitpuk.setting.controller;

import io.tiklab.core.Result;
import io.tiklab.gitpuk.setting.model.AuthSsh;
import io.tiklab.gitpuk.setting.model.AuthSshQuery;
import io.tiklab.gitpuk.setting.service.AuthSshServer;
import io.tiklab.postin.annotation.Api;
import io.tiklab.postin.annotation.ApiMethod;
import io.tiklab.postin.annotation.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/authSsh")
@Api(name = "AuthSshController",desc = "认证")
public class AuthSshController {

    @Autowired
    private AuthSshServer authSshServer;

    @RequestMapping(path="/createAuthSsh",method = RequestMethod.POST)
    @ApiMethod(name = "createAuthSsh",desc = "创建认证")
    @ApiParam(name = "authSsh",desc = "authSsh",required = true)
    public Result<String> createAuthSsh(@RequestBody @NotNull @Valid AuthSsh authSsh){

        String Id = authSshServer.createAuthSsh(authSsh);

        return Result.ok(Id);
    }

    @RequestMapping(path="/updateAuthSsh",method = RequestMethod.POST)
    @ApiMethod(name = "updateAuthSsh",desc = "更新认证")
    @ApiParam(name = "authSsh",desc = "authSsh",required = true)
    public Result<String> updateAuthSsh(@RequestBody @NotNull @Valid AuthSsh authSsh){

        authSshServer.updateAuthSsh(authSsh);

        return Result.ok();
    }


    @RequestMapping(path="/deleteAuthSsh",method = RequestMethod.POST)
    @ApiMethod(name = "deleteAuthSsh",desc = "删除认证")
    @ApiParam(name = "Id",desc = "认证id",required = true)
    public Result<Void> deleteAuthSsh(@NotNull String id){

        authSshServer.deleteAuthSsh(id);

        return Result.ok();
    }


    @RequestMapping(path="/findOneAuthSsh",method = RequestMethod.POST)
    @ApiMethod(name = "findOneAuthSsh",desc = "查询单个认证")
    @ApiParam(name = "authSshId",desc = "认证id",required = true)
    public Result<AuthSsh> findOneAuthSsh(@NotNull String id){

        AuthSsh authSsh = authSshServer.findOneAuthSsh(id);

        return Result.ok(authSsh);
    }



    @RequestMapping(path="/findAuthSshList",method = RequestMethod.POST)
    @ApiMethod(name = "findUserAuthSsh",desc = "查询认证")
    public Result<List<AuthSsh>> findAuthSshList(@RequestBody @NotNull @Valid AuthSshQuery authSshQuery){

        List<AuthSsh> List = authSshServer.findAuthSshList(authSshQuery);

        return Result.ok(List);
    }
}

























