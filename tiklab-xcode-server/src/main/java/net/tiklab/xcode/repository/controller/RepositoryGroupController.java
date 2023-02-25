package net.tiklab.xcode.repository.controller;

import net.tiklab.core.Result;
import net.tiklab.postin.annotation.Api;
import net.tiklab.postin.annotation.ApiMethod;
import net.tiklab.postin.annotation.ApiParam;
import net.tiklab.xcode.repository.model.RepositoryGroup;
import net.tiklab.xcode.repository.service.RepositoryGroupServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/codeGroup")
@Api(name = "RepositoryGroupController",desc = "仓库组")
public class RepositoryGroupController {

    @Autowired
    private RepositoryGroupServer groupServer;


    @RequestMapping(path="/createGroup",method = RequestMethod.POST)
    @ApiMethod(name = "createGroup",desc = "创建仓库组")
    @ApiParam(name = "code",desc = "code",required = true)
    public Result<String> createGroup(@RequestBody @NotNull @Valid RepositoryGroup group){

        String codeId = groupServer.createCodeGroup(group);

        return Result.ok(codeId);
    }


    @RequestMapping(path="/deleteGroup",method = RequestMethod.POST)
    @ApiMethod(name = "deleteGroup",desc = "删除仓库组")
    @ApiParam(name = "codeId",desc = "仓库组id",required = true)
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
