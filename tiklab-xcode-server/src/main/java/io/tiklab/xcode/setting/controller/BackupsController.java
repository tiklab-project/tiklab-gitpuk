package io.tiklab.xcode.setting.controller;

import io.tiklab.core.Result;
import io.tiklab.postin.annotation.Api;
import io.tiklab.postin.annotation.ApiMethod;
import io.tiklab.postin.annotation.ApiParam;
import io.tiklab.xcode.setting.model.Backups;
import io.tiklab.xcode.setting.service.BackupsServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;


@RestController
@RequestMapping("/backups")
@Api(name = "BackupsController",desc = "数据备份")
public class BackupsController {

    @Autowired
    BackupsServer backupsServer;

    @RequestMapping(path="/backupsExec",method = RequestMethod.POST)
    @ApiMethod(name = "backupsExec",desc = "执行备份")
    @ApiParam(name = "auth",desc = "auth",required = true)
    public Result<String> backupsExec(){

        String exec = backupsServer.backupsExec();

        return Result.ok(exec);
    }

    @RequestMapping(path="/findBackups",method = RequestMethod.POST)
    @ApiMethod(name = "findBackups",desc = "查询备份相关数据")
    public Result<Backups> findBackups(){

        Backups backups=  backupsServer.findBackups();

        return Result.ok(backups);
    }

    @RequestMapping(path="/updateBackups",method = RequestMethod.POST)
    @ApiMethod(name = "updateBackups",desc = "修改备份相关数据")
    @ApiParam(name = "auth",desc = "auth",required = true)
    public Result<String> updateBackups(@RequestBody @NotNull @Valid Backups backups){

        backupsServer.updateBackups(backups);

        return Result.ok();
    }


    @RequestMapping(path="/recoveryData",method = RequestMethod.POST)
    @ApiMethod(name = "recoveryData",desc = "数据恢复")
    @ApiParam(name = "fileName",desc = "恢复的文件名称",required = true)
    public Result<String> recoveryData(@NotNull String userId,String fileName){

        backupsServer.recoveryData(userId,fileName);

        return Result.ok();
    }

    @RequestMapping(path="/gainBackupsRes",method = RequestMethod.POST)
    @ApiMethod(name = "gainBackupsRes",desc = "获取备份或者数据恢复结果")
    @ApiParam(name = "type",desc = "backups、recovery",required = true)
    public Result<String> gainBackupsRes(@NotNull String type){

        String gainBackupsRes = backupsServer.gainBackupsRes(type);

        return Result.ok(gainBackupsRes);
    }

    @RequestMapping(path="/uploadBackups",method = RequestMethod.POST)
    @ApiMethod(name = "uploadBackups",desc = "上传备份数据")
    @ApiParam(name = "uploadFile",desc = "uploadFile",required = true)
    public Result<String> uploadBackups(@RequestParam("uploadFile") MultipartFile uploadFile,String userId){
        try {
            String fileName = uploadFile.getOriginalFilename();   //获取文件名字
            InputStream inputStream = uploadFile.getInputStream();
            backupsServer.uploadBackups(inputStream,fileName,userId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.ok();
    }


}
