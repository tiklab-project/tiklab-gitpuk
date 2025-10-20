package io.tiklab.gitpuk.repository.controller;

import io.tiklab.core.Result;
import io.tiklab.core.page.Pagination;
import io.tiklab.gitpuk.repository.model.IntegratedInQuery;
import io.tiklab.gitpuk.repository.service.IntegratedInService;
import io.tiklab.gitpuk.setting.model.IntegrationAddress;
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

@RestController
@RequestMapping("/integratedIn")
//系统集成的接口管理
//@Api(name = "IntRelevancyController",desc = "集成关联")
public class IntegratedInController {

    @Autowired
    IntegratedInService integratedInService;

    //查询流水线
    @RequestMapping(path="/findPipelinePage",method = RequestMethod.POST)
    public Result<Object> findPipelinePage(@RequestBody @NotNull @Valid IntegratedInQuery integratedInQuery){

        Object pipelineList = integratedInService.findPipelinePage(integratedInQuery);

        return Result.ok(pipelineList);
    }


    //查询代码库关联的流水线
    @RequestMapping(path="/findRelevancePipelinePage",method = RequestMethod.POST)
    public Result<Object> findRelevancePipelinePage(@RequestBody @NotNull @Valid IntegratedInQuery integratedInQuery){

        Object pipelineList = integratedInService.findRelevancePipelinePage(integratedInQuery);

        return Result.ok(pipelineList);
    }



    //查询所有服务端未关联的扫描的扫描方案
    @RequestMapping(path="/findScanPlayPage",method = RequestMethod.POST)
    public Result<Object> findScanPlayPage(@RequestBody @NotNull @Valid IntegratedInQuery integratedInQuery){
        Object scanPlayPage = integratedInService.findScanPlayPage(integratedInQuery);

        return Result.ok(scanPlayPage);
    }

    //查询关联的扫描计划
    @RequestMapping(path="/findRelevanceScanPlay",method = RequestMethod.POST)
    public Result<Object> findRelevanceScanPlay(@RequestBody @NotNull @Valid IntegratedInQuery integratedInQuery){

        Object scanPlayPage = integratedInService.findRelevanceScanPlay(integratedInQuery);

        return Result.ok(scanPlayPage);
    }
}
