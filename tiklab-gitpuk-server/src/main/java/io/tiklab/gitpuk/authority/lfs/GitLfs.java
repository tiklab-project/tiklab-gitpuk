package io.tiklab.gitpuk.authority.lfs;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.tiklab.core.Result;
import io.tiklab.gitpuk.authority.request.LfsBatchRequest;
import io.tiklab.gitpuk.authority.request.LfsData;
import io.tiklab.gitpuk.authority.utils.ReturnResponse;
import io.tiklab.gitpuk.common.GitPukYamlDataMaService;
import io.tiklab.gitpuk.common.RepositoryUtil;
import io.tiklab.gitpuk.repository.model.Repository;
import io.tiklab.gitpuk.repository.model.RepositoryLfs;
import io.tiklab.gitpuk.repository.service.MemoryManService;
import io.tiklab.gitpuk.repository.service.RepositoryLfsService;
import io.tiklab.gitpuk.repository.service.RepositoryService;
import io.tiklab.postin.annotation.ApiMethod;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;

@RestController
@RequestMapping("/lfs/**")
public class GitLfs {

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    GitPukYamlDataMaService yamlDataMaService;

    @Autowired
    RepositoryLfsService lfsService;

    @Autowired
    MemoryManService memoryManService;

    @RequestMapping(path="/{type}",method = RequestMethod.POST)
    @ApiMethod(name = "uploadDataSsh",desc = "ssh客户端传入lfs文件信息（不是文件内容）")
    public void uploadDataSsh(@PathVariable String type,HttpServletRequest request,HttpServletResponse response){

        if (("batch").equals(type)){
            try {
                //解析 客户端上传的数据
                LfsBatchRequest lfsBatchRequest = new ObjectMapper().readValue(request.getInputStream(), LfsBatchRequest.class);

                LfsData lfsData = new LfsData();
                //上传的时候 判断lfs剩余的内存是否足够
                if ("upload".equalsIgnoreCase(lfsBatchRequest.getOperation())){
                    boolean lfsStorage = memoryManService.isLfsStorage(request, lfsBatchRequest);
                    if (!lfsStorage){
                        ReturnResponse.lfsNotMemory(response);
                        return;
                    }
                }else {
                    //拉取
                    String requestURI = request.getRequestURI();
                    String address = StringUtils.substringBetween(requestURI, "lfs/", "/objects");
                    Repository repository = repositoryService.findOnlyRpyByAddress(address);
                    String rpyLfsPath = RepositoryUtil.getRpyLfsPath(yamlDataMaService.repositoryAddress(), repository.getRpyId());
                    lfsData.setLfsPath(rpyLfsPath);
                }

                lfsData.setRequest(request);
                lfsData.setResponse(response);
                lfsData.setLfsBatchRequest(lfsBatchRequest);
                lfsData.setType("ssh");

                GitLfsAuth.HandleLfsBatch(lfsData);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    @RequestMapping(path="/{oid}",method = RequestMethod.PUT)
    @ApiMethod(name = "upload",desc = "写入fls文件")
    public Result<Void> upload(@PathVariable String oid,HttpServletRequest request){
        //获取lfs文件存储位置
        String requestURI = request.getRequestURI();
        String address = requestURI.substring(requestURI.indexOf("lfs/")+4, requestURI.indexOf("/"+oid));
        Repository repository = repositoryService.findOnlyRpyByAddress(address);

        String rpyLfsPath = RepositoryUtil.getRpyLfsPath(yamlDataMaService.repositoryAddress(), repository.getRpyId());
        File file = new File(rpyLfsPath, oid);

        try {
            // 确保目录存在
            file.getParentFile().mkdirs();
            FileOutputStream fos = new FileOutputStream(file);
            ServletInputStream inputStream = request.getInputStream();
            IOUtils.copy(inputStream, fos);
            inputStream.close();


            File lfsFile = new File(file.getPath());
            //获取文件类型
            Tika tika = new Tika();
            String detect = tika.detect(lfsFile);
            String type = RepositoryUtil.getFileTypeByExtension(detect);
            String size = RepositoryUtil.countStorageSize(lfsFile.length());

            //编辑lfs文件信息
            RepositoryLfs repositoryLfs = new RepositoryLfs();
            repositoryLfs.setRepositoryId(repository.getRpyId());
            repositoryLfs.setOid(oid);
            repositoryLfs.setFileType(type);
            repositoryLfs.setFileSize(size);
            lfsService.editRepositoryLfs(repositoryLfs);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return Result.ok();
    }

    @RequestMapping(path="/{oid}",method = RequestMethod.GET)
    @ApiMethod(name = "upload",desc = "下载fls文件")
    public void download(@PathVariable String oid,HttpServletRequest request , HttpServletResponse response) throws IOException {
        //获取lfs文件存储位置
        String requestURI = request.getRequestURI();
        String address = requestURI.substring(requestURI.indexOf("lfs/")+4, requestURI.indexOf("/"+oid));
        Repository repository = repositoryService.findOnlyRpyByAddress(address);

        String rpyLfsPath = RepositoryUtil.getRpyLfsPath(yamlDataMaService.repositoryAddress(), repository.getRpyId());
        File file = new File(rpyLfsPath, oid);
        if (file.exists()) {
            response.setContentType("application/octet-stream");
            Files.copy(file.toPath(), response.getOutputStream());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("external filter 'git-lfs filter-process' failed");
        }
    }

}
