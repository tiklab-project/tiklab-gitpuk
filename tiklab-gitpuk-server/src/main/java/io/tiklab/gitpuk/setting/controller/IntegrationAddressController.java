package io.tiklab.gitpuk.setting.controller;

import io.tiklab.core.Result;
import io.tiklab.gitpuk.setting.model.IntegrationAddress;
import io.tiklab.gitpuk.setting.model.IntegrationAddressQuery;
import io.tiklab.gitpuk.setting.service.IntegrationAddressServer;
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
@RequestMapping("/integrationAddress")
@Api(name = "IntegrationAddressController",desc = "系统集成地址")
public class IntegrationAddressController {

    @Autowired
    private IntegrationAddressServer integrationAddressServer;

    @RequestMapping(path="/createIntegrationAddress",method = RequestMethod.POST)
    @ApiMethod(name = "createIntegrationAddress",desc = "创建系统集成地址")
    @ApiParam(name = "integrationAddress",desc = "integrationAddress",required = true)
    public Result<String> createIntegrationAddress(@RequestBody @NotNull @Valid IntegrationAddress integrationAddress){

        String Id = integrationAddressServer.createIntegrationAddress(integrationAddress);

        return Result.ok(Id);
    }

    @RequestMapping(path="/updateIntegrationAddress",method = RequestMethod.POST)
    @ApiMethod(name = "updateIntegrationAddress",desc = "更新系统集成地址")
    @ApiParam(name = "integrationAddress",desc = "integrationAddress",required = true)
    public Result<String> updateIntegrationAddress(@RequestBody @NotNull @Valid IntegrationAddress integrationAddress){

        integrationAddressServer.updateIntegrationAddress(integrationAddress);

        return Result.ok();
    }


    @RequestMapping(path="/deleteIntegrationAddress",method = RequestMethod.POST)
    @ApiMethod(name = "deleteIntegrationAddress",desc = "删除系统集成地址")
    @ApiParam(name = "id",desc = "系统集成地址id",required = true)
    public Result<Void> deleteIntegrationAddress(@NotNull String id){

        integrationAddressServer.deleteIntegrationAddress(id);

        return Result.ok();
    }


    @RequestMapping(path="/findOneIntegrationAddress",method = RequestMethod.POST)
    @ApiMethod(name = "findOneIntegrationAddress",desc = "查询单个系统集成地址")
    @ApiParam(name = "id",desc = "系统集成地址id",required = true)
    public Result<IntegrationAddress> findOneIntegrationAddress(@NotNull String id){

        IntegrationAddress integrationAddress = integrationAddressServer.findOneIntegrationAddress(id);

        return Result.ok(integrationAddress);
    }



    @RequestMapping(path="/findIntegrationAddressList",method = RequestMethod.POST)
    @ApiMethod(name = "findUserIntegrationAddress",desc = "查询系统集成地址")
    @ApiParam(name = "integrationAddressQuery",desc = "integrationAddressQuery",required = true)
    public Result<List<IntegrationAddress>> findIntegrationAddressList(@RequestBody @NotNull @Valid IntegrationAddressQuery integrationAddressQuery){

        List<IntegrationAddress> List = integrationAddressServer.findIntegrationAddressList(integrationAddressQuery);

        return Result.ok(List);
    }

    @RequestMapping(path="/findIntegrationAddress",method = RequestMethod.POST)
    @ApiMethod(name = "findIntegrationAddress",desc = "通过code查询系统集成地址")
    @ApiParam(name = "code",desc = "code",required = true)
    public Result<IntegrationAddress> findIntegrationAddress(@NotNull String code){

        IntegrationAddress integrationAddress = integrationAddressServer.findIntegrationAddress(code);

        return Result.ok(integrationAddress);
    }
}

























