package io.thoughtware.gittok.repository.controller;

import io.thoughtware.core.exception.SystemException;
import io.thoughtware.gittok.file.model.FileTree;
import io.thoughtware.gittok.file.model.FileTreeMessage;
import io.thoughtware.gittok.repository.model.Repository;
import io.thoughtware.gittok.repository.model.RepositoryCloneAddress;
import io.thoughtware.gittok.repository.model.RepositoryQuery;
import io.thoughtware.gittok.repository.service.RepositoryService;
import io.thoughtware.core.Result;
import io.thoughtware.core.page.Pagination;
import io.thoughtware.postin.annotation.Api;
import io.thoughtware.postin.annotation.ApiMethod;
import io.thoughtware.postin.annotation.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.List;
/**
 * @pi.protocol: http
 * @pi.groupName: repository
 */
@RestController
@RequestMapping("/rpy")
@Api(name = "RepositoryController",desc = "仓库")
public class RepositoryController {
    
    @Autowired
    private RepositoryService repositoryServer;

    /**
     * @pi.name: create repository
     * @pi.path:/rpy/createRpy
     * @pi.methodType:post
     * @pi.request-type:json
     * @pi.param: model=Repository ;
     */
    @RequestMapping(path="/createRpy",method = RequestMethod.POST)
    @ApiMethod(name = "create",desc = "创建仓库")
    @ApiParam(name = "repository",desc = "repository",required = true)
    public Result<String> create(@RequestBody @NotNull @Valid Repository repository){

        String rpyId = repositoryServer.createRpyData(repository);

        return Result.ok(rpyId);
    }


    /**
     * @pi.name:删除仓库
     * @pi.path:/rpy/deleteRpy
     * @pi.methodType:post
     * @pi.request-type:formdata
     * @pi.param:  name=rpyId;dataType=string;value=rpyId;
     */
    @RequestMapping(path="/deleteRpy",method = RequestMethod.POST)
    @ApiMethod(name = "delete",desc = "删除仓库")
    @ApiParam(name = "rpyId",desc = "仓库id",required = true)
    public Result<Void> delete(@NotNull String rpyId){

        repositoryServer.deleteRpy(rpyId);
        return Result.ok();
    }


    /**
     * @pi.name:更新仓库
     * @pi.path:/rpy/updateRpy
     * @pi.methodType:post
     * @pi.request-type:json
     * @pi.param: model=Repository ;
     */
    @RequestMapping(path="/updateRpy",method = RequestMethod.POST)
    @ApiMethod(name = "update",desc = "更新仓库")
    @ApiParam(name = "repository",desc = "repository",required = true)
    public Result<Void> update(@RequestBody @NotNull @Valid Repository repository){

        repositoryServer.updateRpy(repository);

        return Result.ok();
    }

    /**
     * @pi.name:通过用户id查询仓库
     * @pi.path:/rpy/findUserRpy
     * @pi.methodType:post
     * @pi.request-type:formdata
     * @pi.param:  name=userId;dataType=string;value=userId;
     */
    @RequestMapping(path="/findUserRpy",method = RequestMethod.POST)
    @ApiMethod(name = "findUser",desc = "查询仓库")
    @ApiParam(name = "userId",desc = "用户id",required = true)
    public Result<List<Repository>> findUser(@NotNull String userId){

        List<Repository> repositoryList = repositoryServer.findUserRpy(userId);

        return Result.ok(repositoryList);
    }

    @RequestMapping(path="/findRepositoryByUser",method = RequestMethod.POST)
    @ApiMethod(name = "findRepositoryByUser",desc = "通过用户查询有权限的仓库")
    @ApiParam(name = "account",desc = "account",required = true)
    public Result<List<Repository>> findRepositoryByUser(@NotNull String account, @NotNull String password, String dirId){

        List<Repository> repositoryList = repositoryServer.findRepositoryByUser(account,password,dirId);

        return Result.ok(repositoryList);
    }


    /**
     * @pi.name:条件查询仓库
     * @pi.path:/rpy/findRepositoryList
     * @pi.methodType:post
     * @pi.request-type:json
     * @pi.param: model=Repository ;
     */

    @RequestMapping(path="/findRepositoryList",method = RequestMethod.POST)
    @ApiMethod(name = "findRepositoryList",desc = "条件查询仓库")
    @ApiParam(name = "repositoryQuery",desc = "repositoryQuery",required = true)
    public Result<List<Repository>> findRepositoryList(@RequestBody @NotNull @Valid RepositoryQuery repositoryQuery){

        List<Repository> repositoryList = repositoryServer.findRepositoryList(repositoryQuery);

        return Result.ok(repositoryList);
    }

    /**
     * @pi.name:条件分页查询仓库
     * @pi.path:/rpy/findRepositoryPage
     * @pi.methodType:post
     * @pi.request-type:json
     * @pi.param: model=Repository ;
     */
    @RequestMapping(path="/findRepositoryPage",method = RequestMethod.POST)
    @ApiMethod(name = "findRepositoryPage",desc = "条件分页查询仓库")
    @ApiParam(name = "repositoryQuery",desc = "repositoryQuery",required = true)
    public Result<Pagination<Repository>> findRepositoryPage(@RequestBody @NotNull @Valid RepositoryQuery repositoryQuery){

        Pagination<Repository> repositoryPage = repositoryServer.findRepositoryPage(repositoryQuery);

        return Result.ok(repositoryPage);
    }



    /**
     * @pi.name:查询仓库的文件树
     * @pi.path:/rpy/findFileTree
     * @pi.methodType:post
     * @pi.request-type:json
     * @pi.param: model=FileTreeMessage ;
     */
    @RequestMapping(path="/findFileTree",method = RequestMethod.POST)
    @ApiMethod(name = "findFileTree",desc = "查询仓库")
    @ApiParam(name = "message",desc = "文件信息",required = true)
    public Result<List<FileTree>> findFileTree(@RequestBody @NotNull @Valid FileTreeMessage message){

        List<FileTree> fileTree = repositoryServer.findFileTree(message);

        return Result.ok(fileTree);
    }

    /**
     * @pi.name:查询仓库克隆地址
     * @pi.path:/rpy/findCloneAddress
     * @pi.methodType:post
     * @pi.request-type:formdata
     * @pi.param:  name=rpyId;dataType=string;value=rpyId;
     */
    @RequestMapping(path="/findCloneAddress",method = RequestMethod.POST)
    @ApiMethod(name = "findCloneAddress",desc = "查询仓库克隆地址")
    @ApiParam(name = "rpyId",desc = "仓库id",required = true)
    public Result<RepositoryCloneAddress> findCloneAddress(@NotNull String rpyId){

        RepositoryCloneAddress cloneAddress = repositoryServer.findCloneAddress(rpyId);

        return Result.ok(cloneAddress);
    }
    /**
     * @pi.name:通过仓库id 查询
     * @pi.path:/rpy/findRepository
     * @pi.methodType:post
     * @pi.request-type:formdata
     * @pi.param:  name=id;dataType=string;value=id;
     */
    @RequestMapping(path="/findRepository",method = RequestMethod.POST)
    @ApiMethod(name = "findRepository",desc = "通过仓库id 查询")
    @ApiParam(name = "id",desc = "仓库ID",required = true)
    public Result<Repository> findRepository(@NotNull String id){

        Repository nameRepository = repositoryServer.findRepository(id);

        return Result.ok(nameRepository);
    }


    /**
     * @pi.name:通过仓库名字或仓库组查询仓库是否存在
     * @pi.path:/rpy/findRepositoryByName
     * @pi.methodType:post
     * @pi.request-type:json
     * @pi.param: model=RepositoryQuery ;
     */
    @RequestMapping(path="/findRepositoryByName",method = RequestMethod.POST)
    @ApiMethod(name = "findRepositoryByName",desc = "通过仓库名字或仓库组查询仓库是否存在")
    @ApiParam(name = "repositoryQuery",desc = "repositoryQuery",required = true)
    public Result<List<Repository>> findRepositoryByName(@RequestBody @NotNull @Valid RepositoryQuery repositoryQuery){

        List<Repository> repositoryByName = repositoryServer.findRepositoryByName(repositoryQuery);

        return Result.ok(repositoryByName);
    }

    /**
     * @pi.name:通过仓库地址匹配
     * @pi.path:/rpy/findRepositoryByAddress
     * @pi.methodType:post
     * @pi.request-type:formdata
     * @pi.param:  name=address;dataType=string;value=address;
     */
    @RequestMapping(path="/findRepositoryByAddress",method = RequestMethod.POST)
    @ApiMethod(name = "findRepositoryByAddress",desc = "通过仓库地址匹配")
    @ApiParam(name = "address",desc = "address",required = true)
    public Result<Repository> findRepositoryByAddress( @NotNull String address){

        Repository repository= repositoryServer.findRepositoryByAddress(address);

        return Result.ok(repository);
    }

    /**
     * @pi.name:条件查询仓库组下面的仓库
     * @pi.path:/rpy/findGroupRepository
     * @pi.methodType:post
     * @pi.request-type:formdata
     * @pi.param:  name=groupName;dataType=string;value=groupName;
     */
    @RequestMapping(path="/findGroupRepository",method = RequestMethod.POST)
    @ApiMethod(name = "findGroupRepository",desc = "条件查询仓库组下面的仓库")
    @ApiParam(name = "repositoryQuery",desc = "repositoryQuery",required = true)
    public Result<List<Repository>> findGroupRepository(@RequestBody @NotNull @Valid RepositoryQuery repositoryQuery){

        Pagination<Repository> groupRepository = repositoryServer.findGroupRepository(repositoryQuery);

        return Result.ok(groupRepository);
    }

    /**
     * @pi.name:根据路径删除
     * @pi.path:/rpy/deleteRpyByAddress
     * @pi.methodType:post
     * @pi.request-type:formdata
     * @pi.param:  name=address;dataType=string;value=address;
     */
    @RequestMapping(path="/deleteRpyByAddress",method = RequestMethod.POST)
    @ApiMethod(name = "deleteRpyByAddress",desc = "根据路径删除")
    @ApiParam(name = "address",desc = "仓库id",required = true)
    public Result<Void> deleteRpyByAddress(@NotNull String address){

        repositoryServer.deleteRpyByAddress(address);

        return Result.ok();
    }

    /**
     * @pi.name:查询用户推送过的仓库
     * @pi.path:/rpy/findCommitRepository
     * @pi.methodType:post
     * @pi.request-type:formdata
     * @pi.param:  name=userId;dataType=string;value=userId;
     */

    @RequestMapping(path="/findCommitRepository",method = RequestMethod.POST)
    @ApiMethod(name = "findCommitRepository",desc = "查询用户推送过的仓库")
    @ApiParam(name = "userId",desc = "用户id",required = true)
    public Result<List<Repository>> findCommitRepository(@NotNull String userId){
        List<Repository> repositoryList=repositoryServer.findCommitRepository(userId);

        return Result.ok(repositoryList);
    }

    /**
     * @pi.name:查询用户有权限的私有仓库
     * @pi.path:/rpy/findPrivateRepositoryByUser
     * @pi.methodType:post
     * @pi.request-type:json
     * @pi.param: model=RepositoryQuery ;
     */
    @RequestMapping(path="/findPrivateRepositoryByUser",method = RequestMethod.POST)
    @ApiMethod(name = "findPrivateRepositoryByUser",desc = "查询用户有权限的私有仓库")
    @ApiParam(name = "repositoryQuery",desc = "repositoryQuery",required = true)
    public Result<Pagination<Repository>> findPrivateRepositoryByUser(@RequestBody @NotNull @Valid RepositoryQuery repositoryQuery){
        Pagination<Repository> repositoryByUser = repositoryServer.findPrivateRepositoryByUser(repositoryQuery);

        return Result.ok(repositoryByUser);
    }

    /**
     * @pi.name:获取当前服务起ip 或配置的域名
     * @pi.path:/rpy/getAddress
     * @pi.methodType:post
     * @pi.request-type:none
     */
    @RequestMapping(path="/getAddress",method = RequestMethod.POST)
    @ApiMethod(name = "getAddress",desc = "获取当前服务起ip 或配置的域名")
    public Result<String> getAddress(){
        String address=repositoryServer.getAddress();

        return Result.ok(address);
    }


    @RequestMapping(path="/getRepositoryPath",method = RequestMethod.POST)
    @ApiMethod(name = "getRepositoryPath",desc = "获取仓库地址")
    public Result<String> getRepositoryPath(){
        String address=repositoryServer.getRepositoryPath();

        return Result.ok(address);
    }

    /**
     * @pi.name:查询用户是否有当前项目权限
     * @pi.path:/rpy/findRepositoryAuth
     * @pi.methodType:post
     * @pi.request-type:formdata
     * @pi.param:  name=rpyId;dataType=string;value=rpyId;
     */
    @RequestMapping(path="/findRepositoryAuth",method = RequestMethod.POST)
    @ApiMethod(name = "findRepositoryAuth",desc = "查询用户是否有当前项目权限")
    @ApiParam(name = "rpyId",desc = "rpyId",required = true)
    public Result<String> findRepositoryAuth( @NotNull String rpyId){

        String repositoryAuth = repositoryServer.findRepositoryAuth(rpyId);

        return Result.ok(repositoryAuth);
    }

    @RequestMapping(path="/resetRepository",method = RequestMethod.POST)
    @ApiMethod(name = "resetRepository",desc = "重置仓库")
    @ApiParam(name = "rpyId",desc = "rpyId",required = true)
    public Result<String> resetRepository( @NotNull String rpyId){

        repositoryServer.resetRepository(rpyId);

        return Result.ok();
    }
}
