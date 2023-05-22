package io.tiklab.xcode.detection.controller;

import io.tiklab.core.Result;
import io.tiklab.core.page.Pagination;
import io.tiklab.postin.annotation.Api;
import io.tiklab.postin.annotation.ApiMethod;
import io.tiklab.postin.annotation.ApiParam;
import io.tiklab.xcode.detection.model.AuthThird;
import io.tiklab.xcode.detection.model.AuthThirdQuery;
import io.tiklab.xcode.detection.service.AuthThirdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * AuthThirdController
 */
@RestController
@RequestMapping("/authThird")
@Api(name = "AuthThirdController",desc = "第三方认证服务")
public class AuthThirdController {

    private static Logger logger = LoggerFactory.getLogger(AuthThirdController.class);

    @Autowired
    private AuthThirdService authThirdService;

    @RequestMapping(path="/createAuthThird",method = RequestMethod.POST)
    @ApiMethod(name = "createAuthThird",desc = "创建第三方认证服务")
    @ApiParam(name = "AuthThird",desc = "AuthThird",required = true)
    public Result<String> createAuthThird(@RequestBody @NotNull @Valid AuthThird AuthThird){
        String id = authThirdService.createAuthThird(AuthThird);

        return Result.ok(id);
    }

    @RequestMapping(path="/updateAuthThird",method = RequestMethod.POST)
    @ApiMethod(name = "updateAuthThird",desc = "修改第三方认证服务")
    @ApiParam(name = "AuthThird",desc = "AuthThird",required = true)
    public Result<Void> updateAuthThird(@RequestBody @NotNull @Valid AuthThird AuthThird){
        authThirdService.updateAuthThird(AuthThird);

        return Result.ok();
    }

    @RequestMapping(path="/deleteAuthThird",method = RequestMethod.POST)
    @ApiMethod(name = "deleteAuthThird",desc = "删除")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<Void> deleteAuthThird(@NotNull String id){
        authThirdService.deleteAuthThird(id);

        return Result.ok();
    }

    @RequestMapping(path="/findAuthThird",method = RequestMethod.POST)
    @ApiMethod(name = "findAuthThird",desc = "通过id 查询")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<AuthThird> findAuthThird(@NotNull String id){
        AuthThird AuthThird = authThirdService.findAuthThird(id);

        return Result.ok(AuthThird);
    }

    @RequestMapping(path="/findAllAuthThird",method = RequestMethod.POST)
    @ApiMethod(name = "findAllAuthThird",desc = "查询所有查询")
    public Result<List<AuthThird>> findAllAuthThird(){
        List<AuthThird> artifactList = authThirdService.findAllAuthThird();

        return Result.ok(artifactList);
    }

    @RequestMapping(path = "/findAuthThirdList",method = RequestMethod.POST)
    @ApiMethod(name = "findAuthThirdList",desc = "通过条件查询")
    @ApiParam(name = "AuthThirdQuery",desc = "AuthThirdQuery",required = true)
    public Result<List<AuthThird>> findAuthThirdList(@RequestBody @Valid @NotNull AuthThirdQuery authThirdQuery){
        List<AuthThird> artifactList = authThirdService.findAuthThirdList(authThirdQuery);

        return Result.ok(artifactList);
    }

    @RequestMapping(path = "/findAuthThirdPage",method = RequestMethod.POST)
    @ApiMethod(name = "findAuthThirdPage",desc = "通过条件分页查询")
    @ApiParam(name = "AuthThirdQuery",desc = "AuthThirdQuery",required = true)
    public Result<Pagination<AuthThird>> findAuthThirdPage(@RequestBody @Valid @NotNull AuthThirdQuery authThirdQuery){
        Pagination<AuthThird> pagination = authThirdService.findAuthThirdPage(authThirdQuery);

        return Result.ok(pagination);
    }


}
