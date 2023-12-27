package io.thoughtware.gittok.repository.controller;

import io.thoughtware.gittok.repository.model.LeadAuth;
import io.thoughtware.gittok.repository.model.LeadAuthQuery;
import io.thoughtware.gittok.repository.service.LeadAuthService;
import io.thoughtware.core.Result;
import io.thoughtware.postin.annotation.Api;
import io.thoughtware.postin.annotation.ApiMethod;
import io.thoughtware.postin.annotation.ApiParam;

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
 * LeadAuthController
 */
@RestController
@RequestMapping("/leadAuth")
@Api(name = "LeadAuthController",desc = "导入第三方仓库的认证")
public class LeadAuthController {

    private static Logger logger = LoggerFactory.getLogger(LeadAuthController.class);

    @Autowired
    private LeadAuthService leadAuthService;


    @RequestMapping(path="/createLeadAuth",method = RequestMethod.POST)
    @ApiMethod(name = "createLeadAuth",desc = "创建第三方仓库认证")
    @ApiParam(name = "LeadAuth",desc = "LeadAuth",required = true)
    public Result<String> createLeadAuth(@RequestBody @NotNull @Valid LeadAuth leadAuth){
        String id = leadAuthService.createLeadAuth(leadAuth);

        return Result.ok(id);
    }

    @RequestMapping(path="/updateLeadAuth",method = RequestMethod.POST)
    @ApiMethod(name = "updateLeadAuth",desc = "修改第三方仓库认证")
    @ApiParam(name = "LeadAuth",desc = "LeadAuth",required = true)
    public Result<Void> updateLeadAuth(@RequestBody @NotNull @Valid LeadAuth leadAuth){
        leadAuthService.updateLeadAuth(leadAuth);

        return Result.ok();
    }

    @RequestMapping(path="/deleteLeadAuth",method = RequestMethod.POST)
    @ApiMethod(name = "deleteLeadAuth",desc = "删除")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<Void> deleteLeadAuth(@NotNull String id){
        leadAuthService.deleteLeadAuth(id);

        return Result.ok();
    }

    @RequestMapping(path="/findLeadAuth",method = RequestMethod.POST)
    @ApiMethod(name = "findLeadAuth",desc = "通过id 查询")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<LeadAuth> findLeadAuth(@NotNull String id){
        LeadAuth LeadAuth = leadAuthService.findLeadAuth(id);

        return Result.ok(LeadAuth);
    }

    @RequestMapping(path="/findAllLeadAuth",method = RequestMethod.POST)
    @ApiMethod(name = "findAllLeadAuth",desc = "查询所有查询")
    public Result<List<LeadAuth>> findAllLeadAuth(){
        List<LeadAuth> artifactList = leadAuthService.findAllLeadAuth();

        return Result.ok(artifactList);
    }

    @RequestMapping(path = "/findLeadAuthList",method = RequestMethod.POST)
    @ApiMethod(name = "findLeadAuthList",desc = "通过条件查询")
    @ApiParam(name = "LeadAuthQuery",desc = "LeadAuthQuery",required = true)
    public Result<List<LeadAuth>> findLeadAuthList(@RequestBody @Valid @NotNull LeadAuthQuery leadAuthQuery){
        List<LeadAuth> artifactList = leadAuthService.findLeadAuthList(leadAuthQuery);

        return Result.ok(artifactList);
    }



}
