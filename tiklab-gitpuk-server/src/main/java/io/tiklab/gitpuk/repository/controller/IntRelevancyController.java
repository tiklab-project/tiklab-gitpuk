package io.tiklab.gitpuk.repository.controller;

import io.tiklab.core.Result;
import io.tiklab.gitpuk.repository.model.IntRelevancy;
import io.tiklab.gitpuk.repository.model.IntRelevancyQuery;
import io.tiklab.gitpuk.repository.service.IntRelevancyService;
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
@RequestMapping("/intRelevancy")
//@Api(name = "IntRelevancyController",desc = "集成关联")
public class IntRelevancyController {

    @Autowired
    private IntRelevancyService intRelevancyServer;

    @RequestMapping(path="/createIntRelevancy",method = RequestMethod.POST)
    @ApiMethod(name = "createIntRelevancy",desc = "创建集成关联")
    @ApiParam(name = "intRelevancy",desc = "intRelevancy",required = true)
    public Result<String> createIntRelevancy(@RequestBody @NotNull @Valid IntRelevancy intRelevancy){

        String Id = intRelevancyServer.createIntRelevancy(intRelevancy);

        return Result.ok(Id);
    }

    @RequestMapping(path="/updateIntRelevancy",method = RequestMethod.POST)
    @ApiMethod(name = "updateIntRelevancy",desc = "更新集成关联")
    @ApiParam(name = "intRelevancy",desc = "intRelevancy",required = true)
    public Result<String> updateIntRelevancy(@RequestBody @NotNull @Valid IntRelevancy intRelevancy){

        intRelevancyServer.updateIntRelevancy(intRelevancy);

        return Result.ok();
    }


    @RequestMapping(path="/deleteIntRelevancy",method = RequestMethod.POST)
    @ApiMethod(name = "deleteIntRelevancy",desc = "删除集成关联")
    @ApiParam(name = "id",desc = "集成关联id",required = true)
    public Result<Void> deleteIntRelevancy(@NotNull String id){

        intRelevancyServer.deleteIntRelevancy(id);

        return Result.ok();
    }

    @RequestMapping(path="/deleteIntRelevancyByIn",method = RequestMethod.POST)
    @ApiMethod(name = "deleteIntRelevancyByIn",desc = "通过仓库id和关联id删除")
    @ApiParam(name = "intRelevancyQuery",desc = "通过仓库id和关联id删除",required = true)
    public Result<Void> deleteIntRelevancyByIn(@RequestBody @NotNull @Valid IntRelevancyQuery intRelevancyQuery){

        intRelevancyServer.deleteIntRelevancy(intRelevancyQuery);

        return Result.ok();
    }

    @RequestMapping(path="/findOneIntRelevancy",method = RequestMethod.POST)
    @ApiMethod(name = "findOneIntRelevancy",desc = "查询单个集成关联")
    @ApiParam(name = "id",desc = "集成关联id",required = true)
    public Result<IntRelevancy> findOneIntRelevancy(@NotNull String id){

        IntRelevancy intRelevancy = intRelevancyServer.findOneIntRelevancy(id);

        return Result.ok(intRelevancy);
    }




    @RequestMapping(path="/findIntRelevancyList",method = RequestMethod.POST)
    @ApiMethod(name = "findUserIntRelevancy",desc = "查询集成关联")
    @ApiParam(name = "intRelevancyQuery",desc = "intRelevancyQuery",required = true)
    public Result<List<IntRelevancy>> findIntRelevancyList(@RequestBody @NotNull @Valid IntRelevancyQuery intRelevancyQuery){

        List<IntRelevancy> List = intRelevancyServer.findIntRelevancyList(intRelevancyQuery);

        return Result.ok(List);
    }

    
}

























