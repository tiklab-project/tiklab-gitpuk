package io.thoughtware.gittok.repository.controller;


import io.thoughtware.core.Result;
import io.thoughtware.core.exception.SystemException;
import io.thoughtware.gittok.repository.service.RepositoryDownService;
import io.thoughtware.postin.annotation.Api;
import io.thoughtware.postin.annotation.ApiMethod;
import io.thoughtware.postin.annotation.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/*
* 仓库下载
* */
@RestController
@RequestMapping("/rpyDown")
@Api(name = "RepositoryDownController",desc = "仓库")
public class RepositoryDownController {

    @Autowired
    RepositoryDownService repositoryDownService;

    @RequestMapping(path = "/downloadRpy/**",method = RequestMethod.GET)
    @ApiMethod(name = "downloadRpy",desc = "仓库下载")
    @ApiParam(name = "requestParam",desc = "requestParam")
    public void downloadRpy(HttpServletRequest request, HttpServletResponse response) {
        String requestURI = request.getRequestURI();
        String reqData = request.getQueryString();
        int name = StringUtils.lastIndexOf(requestURI, "/");
        // 设置响应的内容类型为ZIP文件
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=123.zip");
        try {


            ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());

            String gitRepoPath = repositoryDownService.downloadRpy(reqData, name);

            // 添加裸仓库路径下的指定分支文件到ZIP文件中
            addFileToZip(zipOut, gitRepoPath, "master", "");

            // 刷新并关闭ZIP输出流
            zipOut.flush();
        } catch (IOException e) {
            throw new SystemException(e.getMessage());
        }
    }

    private void addFileToZip(ZipOutputStream zipOut, String folderPath, String fileName, String parentFolderPath) throws IOException {
        File file = new File(folderPath, fileName);
        byte[] buffer = new byte[1024];
        int bytesRead;

        if (file.isDirectory()) {
            File[] files = file.listFiles();

            for (File subFile : files) {
                String subFolderPath = parentFolderPath + fileName + "/";
                addFileToZip(zipOut, folderPath, subFile.getName(), subFolderPath);
            }
        } else {
            try (FileInputStream fis = new FileInputStream(file)) {
                String entryPath = parentFolderPath + fileName;
                ZipEntry zipEntry = new ZipEntry(entryPath);
                zipOut.putNextEntry(zipEntry);

                while ((bytesRead = fis.read(buffer)) != -1) {
                    zipOut.write(buffer, 0, bytesRead);
                }
            }
        }
    }
}
