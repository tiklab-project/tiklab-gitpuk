package io.tiklab.xcode.detection.controller;

import io.tiklab.core.Result;
import io.tiklab.core.page.Pagination;
import io.tiklab.postin.annotation.Api;
import io.tiklab.postin.annotation.ApiMethod;
import io.tiklab.postin.annotation.ApiParam;
import io.tiklab.xcode.detection.model.ScmAddress;
import io.tiklab.xcode.detection.model.ScmAddressQuery;
import io.tiklab.xcode.detection.service.ScmAddressService;
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
 * ScmAddressController
 */
@RestController
@RequestMapping("/scmAddress")
@Api(name = "ScmAddressController",desc = "供应应用地址管理")
public class ScmAddressController {

    private static Logger logger = LoggerFactory.getLogger(ScmAddressController.class);

    @Autowired
    private ScmAddressService scmAddressService;

    @RequestMapping(path="/createScmAddress",method = RequestMethod.POST)
    @ApiMethod(name = "createScmAddress",desc = "创建供应应用地址管理")
    @ApiParam(name = "ScmAddress",desc = "ScmAddress",required = true)
    public Result<String> createScmAddress(@RequestBody @NotNull @Valid ScmAddress ScmAddress){
        String id = scmAddressService.createScmAddress(ScmAddress);

        return Result.ok(id);
    }

    @RequestMapping(path="/updateScmAddress",method = RequestMethod.POST)
    @ApiMethod(name = "updateScmAddress",desc = "修改供应应用地址管理")
    @ApiParam(name = "ScmAddress",desc = "ScmAddress",required = true)
    public Result<Void> updateScmAddress(@RequestBody @NotNull @Valid ScmAddress ScmAddress){
        scmAddressService.updateScmAddress(ScmAddress);

        return Result.ok();
    }

    @RequestMapping(path="/deleteScmAddress",method = RequestMethod.POST)
    @ApiMethod(name = "deleteScmAddress",desc = "删除")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<Void> deleteScmAddress(@NotNull String id){
        scmAddressService.deleteScmAddress(id);

        return Result.ok();
    }

    @RequestMapping(path="/findScmAddress",method = RequestMethod.POST)
    @ApiMethod(name = "findScmAddress",desc = "通过id 查询")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<ScmAddress> findScmAddress(@NotNull String id){
        ScmAddress ScmAddress = scmAddressService.findScmAddress(id);

        return Result.ok(ScmAddress);
    }

    @RequestMapping(path="/findAllScmAddress",method = RequestMethod.POST)
    @ApiMethod(name = "findAllScmAddress",desc = "查询所有查询")
    public Result<List<ScmAddress>> findAllScmAddress(){
        List<ScmAddress> artifactList = scmAddressService.findAllScmAddress();

        return Result.ok(artifactList);
    }

    @RequestMapping(path = "/findScmAddressList",method = RequestMethod.POST)
    @ApiMethod(name = "findScmAddressList",desc = "通过条件查询")
    @ApiParam(name = "ScmAddressQuery",desc = "ScmAddressQuery",required = true)
    public Result<List<ScmAddress>> findScmAddressList(@RequestBody @Valid @NotNull ScmAddressQuery scmAddressQuery){
        List<ScmAddress> artifactList = scmAddressService.findScmAddressList(scmAddressQuery);

        return Result.ok(artifactList);
    }

    @RequestMapping(path = "/findScmAddressPage",method = RequestMethod.POST)
    @ApiMethod(name = "findScmAddressPage",desc = "通过条件分页查询")
    @ApiParam(name = "ScmAddressQuery",desc = "ScmAddressQuery",required = true)
    public Result<Pagination<ScmAddress>> findScmAddressPage(@RequestBody @Valid @NotNull ScmAddressQuery scmAddressQuery){
        Pagination<ScmAddress> pagination = scmAddressService.findScmAddressPage(scmAddressQuery);

        return Result.ok(pagination);
    }


}
