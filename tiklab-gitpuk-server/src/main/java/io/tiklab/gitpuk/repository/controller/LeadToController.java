package io.tiklab.gitpuk.repository.controller;

import io.tiklab.core.page.Pagination;
import io.tiklab.gitpuk.repository.model.LeadTo;
import io.tiklab.gitpuk.repository.model.LeadToQuery;
import io.tiklab.gitpuk.repository.service.LeadToService;
import io.tiklab.gitpuk.repository.model.LeadToResult;
import io.tiklab.core.Result;
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
@RequestMapping("/toLead")
@Api(name = "RepositoryToLeadController",desc = "执行导入仓库")
public class LeadToController {

    @Autowired
    LeadToService toLeadService;

    @RequestMapping(path="/findThirdRepositoryList",method = RequestMethod.POST)
    @ApiMethod(name = "findThirdRepositoryList",desc = "查询第三方仓库应用的仓库")
    @ApiParam(name = "importAuthId",desc = "importAuthId",required = true)
    public Result<List> findThirdRepositoryList(@RequestBody @NotNull @Valid LeadToQuery leadToQuery){

        Pagination<LeadTo> thirdRepositoryList = toLeadService.findThirdRepositoryList(leadToQuery);

        return Result.ok(thirdRepositoryList);
    }

    @RequestMapping(path="/toLeadRepository",method = RequestMethod.POST)
    @ApiMethod(name = "toLeadRepository",desc = " 导入仓库")
    @ApiParam(name = "repositoryAddress",desc = "仓库路径",required = true)
    public Result<String> toLeadRepository(@RequestBody @NotNull @Valid LeadToQuery leadToQuery){

        String repository = toLeadService.toLeadRepository(leadToQuery);

        return Result.ok(repository);
    }

    @RequestMapping(path="/findToLeadResult",method = RequestMethod.POST)
    @ApiMethod(name = "findToLeadResult",desc = " 查询导入仓库结果")
    @ApiParam(name = "key",desc = "key",required = true)
    public Result<LeadToResult> findToLeadResult( @NotNull String  key){

        LeadToResult result=toLeadService.findToLeadResult(key);

        return Result.ok(result);
    }
}
