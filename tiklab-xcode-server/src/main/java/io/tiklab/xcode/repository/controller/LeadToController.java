package io.tiklab.xcode.repository.controller;

import io.tiklab.core.Result;
import io.tiklab.postin.annotation.Api;
import io.tiklab.postin.annotation.ApiMethod;
import io.tiklab.postin.annotation.ApiParam;
import io.tiklab.xcode.repository.model.LeadTo;
import io.tiklab.xcode.repository.service.LeadToService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/toLead")
@Api(name = "RepositoryToLeadController",desc = "执行导入仓库")
public class LeadToController {

    @Autowired
    LeadToService toLeadService;

    @RequestMapping(path="/findThirdRepositoryList",method = RequestMethod.POST)
    @ApiMethod(name = "findThirdRepositoryList",desc = "查询第三方仓库应用的仓库")
    @ApiParam(name = "importAuthId",desc = "importAuthId",required = true)
    public Result<List> findThirdRepositoryList( @NotNull  String importAuthId,String page){

        List repositoryList = toLeadService.findThirdRepositoryList(importAuthId,page);

        return Result.ok(repositoryList);
    }

    @RequestMapping(path="/toLeadRepository",method = RequestMethod.POST)
    @ApiMethod(name = "toLeadRepository",desc = " 导入仓库")
    @ApiParam(name = "repositoryAddress",desc = "仓库路径",required = true)
    public Result<String> toLeadRepository(@RequestBody @NotNull @Valid LeadTo leadTo){

        String repository = toLeadService.toLeadRepository(leadTo);

        return Result.ok(repository);
    }

    @RequestMapping(path="/findToLeadResult",method = RequestMethod.POST)
    @ApiMethod(name = "findToLeadResult",desc = " 查询导入仓库结果")
    @ApiParam(name = "thirdRepositoryId",desc = "第三方仓库id",required = true)
    public Result<String> findToLeadResult( @NotNull String  thirdRepositoryId){

        String result=toLeadService.findToLeadResult(thirdRepositoryId);

        return Result.ok(result);
    }
}
