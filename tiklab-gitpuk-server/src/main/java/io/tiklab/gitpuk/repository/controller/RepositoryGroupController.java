package io.tiklab.gitpuk.repository.controller;

import io.tiklab.gitpuk.repository.model.RepositoryGroup;
import io.tiklab.gitpuk.repository.model.RepositoryGroupQuery;
import io.tiklab.gitpuk.repository.service.RepositoryGroupServer;
import io.tiklab.core.Result;
import io.tiklab.core.page.Pagination;
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
@RequestMapping("/rpyGroup")
@Api(name = "RepositoryGroupController",desc = "仓库组")
public class RepositoryGroupController {

    @Autowired
    private RepositoryGroupServer groupServer;


    @RequestMapping(path="/createGroup",method = RequestMethod.POST)
    @ApiMethod(name = "createGroup",desc = "创建仓库组")
    @ApiParam(name = "group",desc = "group",required = true)
    public Result<String> createGroup(@RequestBody @NotNull @Valid RepositoryGroup group){

        String rpyId = groupServer.createCodeGroup(group);

        return Result.ok(rpyId);
    }


    @RequestMapping(path="/deleteGroup",method = RequestMethod.POST)
    @ApiMethod(name = "deleteGroup",desc = "删除仓库组")
    @ApiParam(name = "groupId",desc = "仓库组id",required = true)
    public Result<Void> deleteGroup(@NotNull String groupId){

        groupServer.deleteCodeGroup(groupId);

        return Result.ok();
    }


    @RequestMapping(path="/updateGroup",method = RequestMethod.POST)
    @ApiMethod(name = "updateGroup",desc = "更新仓库组")
    @ApiParam(name = "group",desc = "group",required = true)
    public Result<Void> updateGroup(@RequestBody @NotNull @Valid RepositoryGroup group){

        groupServer.updateCodeGroup(group);

        return Result.ok();
    }

    @RequestMapping(path="/findRepositoryGroupPage",method = RequestMethod.POST)
    @ApiMethod(name = "findRepositoryGroupPage",desc = "查询仓库组")
    @ApiParam(name = "repositoryGroupQuery",desc = "repositoryGroupQuery",required = true)
    public Result<Pagination<RepositoryGroup>> findRepositoryGroupPage(@RequestBody @NotNull @Valid RepositoryGroupQuery repositoryGroupQuery){

        Pagination<RepositoryGroup> codeList = groupServer.findRepositoryGroupPage(repositoryGroupQuery);

        return Result.ok(codeList);
    }

    @RequestMapping(path="/findGroupByName",method = RequestMethod.POST)
    @ApiMethod(name = "findGroupByName",desc = "通过名字查询仓库组")
    @ApiParam(name = "groupName",desc = "仓库名字",required = true)
    public Result<List<RepositoryGroup>> findGroupByName(@NotNull String groupName){

        RepositoryGroup repositoryGroup = groupServer.findGroupByName(groupName);

        return Result.ok(repositoryGroup);
    }

    @RequestMapping(path="/findAllGroup",method = RequestMethod.POST)
    @ApiMethod(name = "findAllGroup",desc = "查询所有")
    public Result<List<RepositoryGroup>> findAllGroup(){
        List<RepositoryGroup> repositoryGroupList = groupServer.findAllGroup();

        return Result.ok(repositoryGroupList);
    }


    @RequestMapping(path="/findCanCreateRpyGroup",method = RequestMethod.POST)
    @ApiMethod(name = "findCanCreateRpyGroup",desc = "查询自己创建的和有权限查看的仓库组")
    @ApiParam(name = "userId",desc = "userId",required = true)
    public Result<List<RepositoryGroup>> findCanCreateRpyGroup(@NotNull  String userId){
        List<RepositoryGroup> repositoryGroupList = groupServer.findCanCreateRpyGroup(userId);

        return Result.ok(repositoryGroupList);
    }

    @RequestMapping(path="/findCanForkGroup",method = RequestMethod.POST)
    @ApiMethod(name = "findCanCreateRpyGroup",desc = "查询可以Fork的仓库组")
    @ApiParam(name = "repositoryGroupQuery",desc = "repositoryGroupQuery",required = true)
    public Result<List<RepositoryGroup>> findCanForkGroup(@RequestBody @NotNull @Valid RepositoryGroupQuery repositoryGroupQuery){
        List<RepositoryGroup> repositoryGroupList = groupServer.findCanForkGroup(repositoryGroupQuery);

        return Result.ok(repositoryGroupList);
    }

}
