package io.thoughtware.gittork.repository.controller;

import io.thoughtware.gittork.repository.model.RemoteInfo;
import io.thoughtware.gittork.repository.model.RemoteInfoQuery;
import io.thoughtware.gittork.repository.service.RemoteInfoService;
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
 * RemoteInfoController
 */
@RestController
@RequestMapping("/remoteInfo")
@Api(name = "RemoteInfoController",desc = "镜像信息管理 ")
public class RemoteInfoController {

    private static Logger logger = LoggerFactory.getLogger(RemoteInfoController.class);

    @Autowired
    private RemoteInfoService remoteInfoService;

    @RequestMapping(path="/createRemoteInfo",method = RequestMethod.POST)
    @ApiMethod(name = "createRemoteInfo",desc = "创建镜像信息")
    @ApiParam(name = "remoteInfo",desc = "remoteInfo",required = true)
    public Result<String> createRemoteInfo(@RequestBody @NotNull @Valid RemoteInfo remoteInfo){

        String id = remoteInfoService.createRemoteInfo(remoteInfo);

        return Result.ok(id);
    }

    @RequestMapping(path="/updateRemoteInfo",method = RequestMethod.POST)
    @ApiMethod(name = "updateRemoteInfo",desc = "更新镜像信息")
    @ApiParam(name = "remoteInfo",desc = "remoteInfo",required = true)
    public Result<Void> updateRemoteInfo(@RequestBody @NotNull @Valid RemoteInfo remoteInfo){
        remoteInfoService.updateRemoteInfo(remoteInfo);

        return Result.ok();
    }

    @RequestMapping(path="/deleteRemoteInfo",method = RequestMethod.POST)
    @ApiMethod(name = "deleteRemoteInfo",desc = "删除镜像信息")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<Void> deleteRemoteInfo(@NotNull String id){
        remoteInfoService.deleteRemoteInfo(id);

        return Result.ok();
    }

    @RequestMapping(path="/findRemoteInfo",method = RequestMethod.POST)
    @ApiMethod(name = "findRemoteInfo",desc = "通过id查询镜像信息")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<RemoteInfo> findRemoteInfo(@NotNull String id){
        RemoteInfo remoteInfo = remoteInfoService.findRemoteInfo(id);
        System.out.println("dasd");
        return Result.ok(remoteInfo);
    }

    @RequestMapping(path="/findAllRemoteInfo",method = RequestMethod.POST)
    @ApiMethod(name = "findAllRemoteInfo",desc = "查询所有镜像信息")
    public Result<List<RemoteInfo>> findAllRemoteInfo(){
        List<RemoteInfo> remoteInfoList = remoteInfoService.findAllRemoteInfo();

        return Result.ok(remoteInfoList);
    }

    @RequestMapping(path = "/findRemoteInfoList",method = RequestMethod.POST)
    @ApiMethod(name = "findRemoteInfoList",desc = "通过条件查询镜像信息")
    @ApiParam(name = "remoteInfoQuery",desc = "remoteInfoQuery",required = true)
    public Result<List<RemoteInfo>> findRemoteInfoList(@RequestBody @Valid @NotNull RemoteInfoQuery remoteInfoQuery){
        List<RemoteInfo> remoteInfoList = remoteInfoService.findRemoteInfoList(remoteInfoQuery);

        return Result.ok(remoteInfoList);
    }

    @RequestMapping(path = "/sendRepository",method = RequestMethod.POST)
    @ApiMethod(name = "sendRepository",desc = "向第三方推送仓库数据")
    @ApiParam(name = "remoteInfoQuery",desc = "remoteInfoQuery",required = true)
    public Result<RemoteInfo> sendRepository(@RequestBody @Valid @NotNull RemoteInfoQuery remoteInfoQuery){
       RemoteInfo remoteInfoList = remoteInfoService.sendRepository(remoteInfoQuery);

        return Result.ok(remoteInfoList);
    }

    @RequestMapping(path = "/sendOneRepository",method = RequestMethod.POST)
    @ApiMethod(name = "sendOneRepository",desc = "向单个第三方推送仓库数据")
    @ApiParam(name = "remoteInfo",desc = "remoteInfo",required = true)
    public Result<RemoteInfo> sendOneRepository(@RequestBody @Valid @NotNull RemoteInfo remoteInfo){
        String oneRepository = remoteInfoService.sendOneRepository(remoteInfo);

        return Result.ok(oneRepository);
    }

    @RequestMapping(path = "/findMirrorResult",method = RequestMethod.POST)
    @ApiMethod(name = "sendOneRepository",desc = "获取推送结果")
    @ApiParam(name = "remoteInfo",desc = "remoteInfo",required = true)
    public Result<RemoteInfo> findMirrorResult( @NotNull String remoteInfoId,@NotNull  String rpyId){
        String oneRepository = remoteInfoService.findMirrorResult(remoteInfoId,rpyId);

        return Result.ok(oneRepository);
    }
}
