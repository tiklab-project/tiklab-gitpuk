package io.tiklab.xcode.repository.controller;

import io.tiklab.core.Result;
import io.tiklab.postin.annotation.Api;
import io.tiklab.postin.annotation.ApiMethod;
import io.tiklab.postin.annotation.ApiParam;
import io.tiklab.xcode.repository.model.RepositoryGroup;
import io.tiklab.xcode.repository.service.RepositoryGroupServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/rpyGroup")
@Api(name = "RepositoryGroupController",desc = "仓库组")
public class RepositoryGroupController {

    @Autowired
    private RepositoryGroupServer groupServer;


    @RequestMapping(path="/createGroup",method = RequestMethod.POST)
    @ApiMethod(name = "createGroup",desc = "创建仓库组")
    @ApiParam(name = "code",desc = "code",required = true)
    public Result<String> createGroup(@RequestBody @NotNull @Valid RepositoryGroup group){

        String rpyId = groupServer.createCodeGroup(group);

        return Result.ok(rpyId);
    }


    @RequestMapping(path="/deleteGroup",method = RequestMethod.POST)
    @ApiMethod(name = "deleteGroup",desc = "删除仓库组")
    @ApiParam(name = "rpyId",desc = "仓库组id",required = true)
    public Result<Void> deleteGroup(@NotNull String groupId){

        groupServer.deleteCodeGroup(groupId);

        return Result.ok();
    }


    @RequestMapping(path="/updateGroup",method = RequestMethod.POST)
    @ApiMethod(name = "updateGroup",desc = "更新仓库组")
    @ApiParam(name = "code",desc = "code",required = true)
    public Result<Void> updateGroup(@RequestBody @NotNull @Valid RepositoryGroup group){

        groupServer.updateCodeGroup(group);

        return Result.ok();
    }

    @RequestMapping(path="/findUserGroup",method = RequestMethod.POST)
    @ApiMethod(name = "findUserGroup",desc = "查询仓库组")
    @ApiParam(name = "userId",desc = "用户id",required = true)
    public Result<List<RepositoryGroup>> findUserGroup(@NotNull String userId){

        List<RepositoryGroup> codeList = groupServer.findUserGroup(userId);

        return Result.ok(codeList);
    }
    
    
}
