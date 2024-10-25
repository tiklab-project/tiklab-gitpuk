package io.tiklab.gitpuk.file.controller;

import com.alibaba.fastjson.JSONObject;
import io.tiklab.gitpuk.file.model.*;
import io.tiklab.core.Result;
import io.tiklab.gitpuk.file.service.RepositoryFileServer;
import io.tiklab.postin.annotation.Api;
import io.tiklab.postin.annotation.ApiMethod;
import io.tiklab.postin.annotation.ApiParam;
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
import java.io.*;
import java.util.List;


@RestController
@RequestMapping("/repositoryFile")
@Api(name = "RepositoryFileController",desc = "仓库文件文件")
public class RepositoryFileController {

    @Autowired
    RepositoryFileServer repositoryFileServer;


    @RequestMapping(path="/createBareFolder",method = RequestMethod.POST)
    @ApiMethod(name = "createBareFolder",desc = "创建裸仓库文件夹")
    @ApiParam(name = "repositoryFile",desc = "repositoryFile",required = true)
    public Result<Void> createBareFolder(@NotNull @RequestBody @Valid RepositoryFile repositoryFile){

        repositoryFileServer.createBareFolder(repositoryFile);

        return Result.ok();
    }

    @RequestMapping(path="/deleteBareFile",method = RequestMethod.POST)
    @ApiMethod(name = "deleteBareFile",desc = "删除裸仓库文件")
    @ApiParam(name = "repositoryFile",desc = "repositoryFile",required = true)
    public Result<Void> deleteBareFile(@NotNull @RequestBody @Valid RepositoryFile repositoryFile){

        repositoryFileServer.deleteBareFile(repositoryFile);

        return Result.ok();
    }

    @RequestMapping(path="/updateBareFile",method = RequestMethod.POST)
    @ApiMethod(name = "updateBareFile",desc = "更新裸仓库文件内容")
    @ApiParam(name = "repositoryFile",desc = "repositoryFile",required = true)
    public Result<Void> updateBareFile(@NotNull @RequestBody @Valid RepositoryFile repositoryFile){

        repositoryFileServer.updateBareFile(repositoryFile);

        return Result.ok();
    }


    @RequestMapping(path="/findFileTree",method = RequestMethod.POST)
    @ApiMethod(name = "findFileTree",desc = "查询裸仓库文件仓库")
    @ApiParam(name = "message",desc = "文件信息",required = true)
    public Result<List<FileTree>> findFileTree(@RequestBody @NotNull @Valid FileFindQuery message){

        List<FileTree> fileTree = repositoryFileServer.findFileTree(message);

        return Result.ok(fileTree);
    }

    @RequestMapping(path="/findBareAllFile",method = RequestMethod.POST)
    @ApiMethod(name = "findBareAllFile",desc = "条件查询裸仓库所有文件")
    @ApiParam(name = "rpyId",desc = "rpyId",required = true)
    public Result<String> findBareAllFile( @RequestBody @NotNull @Valid FileFindQuery fileFindQuery){

        List<String> bareAllFile = repositoryFileServer.findBareAllFile(fileFindQuery);

        return Result.ok(bareAllFile);
    }


    @RequestMapping(path="/readBareRepoFile",method = RequestMethod.POST)
    @ApiMethod(name = "readFile",desc = "读取裸仓库的文件")
    @ApiParam(name = "repositoryFileQuery",desc = "文件信息",required = true)
    public Result<FileMessage> readBareFile(@NotNull @RequestBody @Valid FileQuery repositoryFileQuery){

        FileMessage file = repositoryFileServer.readBareFile(repositoryFileQuery);

        return Result.ok(file);
    }

    @RequestMapping(path="/downLoadBareFile/**",method = RequestMethod.GET)
    @ApiMethod(name = "writeFile",desc = "下载裸仓库中的文件")
    @ApiParam(name = "request",desc = "request",required = true)
    public void downLoadBareFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String queryString = request.getQueryString();

        JSONObject downLoadQuery = repositoryFileServer.getDownLoadQuery(queryString);

        //下载裸仓库文件
        byte[] bytes = repositoryFileServer.downLoadBareFile(downLoadQuery);
        String filePath = downLoadQuery.get("filePath").toString();
        String fileName =  filePath.substring( filePath.lastIndexOf("/")+1);
        response.setHeader("Content-Disposition", "attachment; filename="+fileName);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
    }


    @RequestMapping(path="/downLoadBareRepo/**",method = RequestMethod.GET)
    @ApiMethod(name = "writeFile",desc = "下载裸仓库")
    @ApiParam(name = "fileQuery",desc = "fileQuery",required = true)
    public void downLoadBareRepo(HttpServletRequest request, HttpServletResponse response){
        String queryString = request.getQueryString();

        repositoryFileServer.downLoadBareRepo(response,queryString);

    }

    @RequestMapping(path="/downLoadLfsFile/**",method = RequestMethod.GET)
    @ApiMethod(name = "downloadLfsFile",desc = "下载lfs文件")
    @ApiParam(name = "fileDownload",desc = "fileDownload",required = true)
    public void downloadLfsFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String queryString = request.getQueryString();

        JSONObject downLoadQuery = repositoryFileServer.getDownLoadQuery(queryString);

        repositoryFileServer.downloadLfsFile(downLoadQuery,response);
    }
}

























































