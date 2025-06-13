package io.tiklab.gitpuk.file.service;

import com.alibaba.fastjson.JSONObject;
import io.tiklab.core.exception.ApplicationException;
import io.tiklab.core.exception.SystemException;
import io.tiklab.gitpuk.common.GitPukFinal;
import io.tiklab.gitpuk.common.GitPukYamlDataMaService;
import io.tiklab.gitpuk.common.git.GitBranchUntil;
import io.tiklab.gitpuk.common.git.GitFileUtil;
import io.tiklab.gitpuk.file.model.*;
import io.tiklab.gitpuk.common.RepositoryFileUtil;
import io.tiklab.gitpuk.common.RepositoryUtil;
import io.tiklab.gitpuk.repository.service.RepositoryService;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@Service
public class RepositoryFileServerImpl implements RepositoryFileServer {

    @Autowired
    RepositoryService repositoryServer;

    @Autowired
    GitPukYamlDataMaService yamlDataMaService;


    @Override
    public void createBareFolder(RepositoryFile repositoryFile) {
        String filePath = repositoryFile.getFilePath();
        String rpyPath = yamlDataMaService.repositoryAddress();
        //裸仓库地址
        String address = RepositoryUtil.findRepositoryAddress(rpyPath, repositoryFile.getRepositoryId());

        if (filePath.startsWith("/")){
            String s = StringUtils.substringAfter(filePath, "/");
            repositoryFile.setFilePath(s);
        }
        //文件路径后缀的文件名不等于上传的文件名
        if (!filePath.endsWith(repositoryFile.getFileName())){
            if (filePath.contains("/")){
                String beforeLast = StringUtils.substringBeforeLast(filePath, "/");
                String s = beforeLast +"/" +repositoryFile.getFileName();
                repositoryFile.setFilePath(s);
            }else {
                repositoryFile.setFilePath(repositoryFile.getFileName());
            }
        }

        //创建裸仓库的文件
        GitFileUtil.createBareRepoFolder(repositoryFile,address);
    }

    @Override
    public void deleteBareFile(RepositoryFile repositoryFile) {
        String rpyPath = yamlDataMaService.repositoryAddress();
        //裸仓库地址
        String address = RepositoryUtil.findRepositoryAddress(rpyPath, repositoryFile.getRepositoryId());

        if (repositoryFile.getFilePath().startsWith("/")){
            String s = StringUtils.substringAfter(repositoryFile.getFilePath(), "/");
            repositoryFile.setFilePath(s);
        }
        //删除裸仓库的文件
        GitFileUtil.deleteBareRepoFile(repositoryFile,address);

    }

    @Override
    public void updateBareFile(RepositoryFile repositoryFile) {
        String rpyPath = yamlDataMaService.repositoryAddress();
        //裸仓库地址
        String address = RepositoryUtil.findRepositoryAddress(rpyPath, repositoryFile.getRepositoryId());


        if (repositoryFile.getFilePath().startsWith("/")){
            String s = StringUtils.substringAfter(repositoryFile.getFilePath(), "/");
            repositoryFile.setFilePath(s);
        }
        //更新裸仓库的文件
        GitFileUtil.updateBareRepoFile(repositoryFile,address);

    }


    @Override
    public List<FileTree> findFileTree(FileFindQuery message){

        //仓库存储地址
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),message.getRpyId()) ;

        List<FileTree> fileTrees ;
        try {
            //查询仓库分支
            List<Ref> bareRepoBranchList = GitBranchUntil.findBareRepoBranchList(repositoryAddress);
            if (bareRepoBranchList.isEmpty()){
                return null;
            }

            //查询文件
            fileTrees = RepositoryFileUtil.findFileTree(repositoryAddress,message);
        } catch (IOException e) {
            throw new ApplicationException(GitPukFinal.SYSTEM_EXCEPTION,"仓库信息获取失败：" + e);
        } catch (GitAPIException e) {
            throw new ApplicationException(GitPukFinal.SYSTEM_EXCEPTION, "提交信息获取失败：" + e);
        }
        return fileTrees;
    }


    @Override
    public List<String> findBareAllFile(FileFindQuery fileFindQuery) {
        //仓库地址
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),fileFindQuery.getRpyId());
        List<String> bareAllFile = GitFileUtil.findBareAllFile(repositoryAddress, fileFindQuery.getRefCode(), fileFindQuery.getRefCodeType());
        return bareAllFile;
    }



    @Override
    public byte[] downLoadBareFile(JSONObject jsonObject) {
        String branch = jsonObject.get("branch").toString();
        String filePath = jsonObject.get("filePath").toString();
        String type = jsonObject.get("type").toString();
        String rpyPath = yamlDataMaService.repositoryAddress();
        //裸仓库地址
        String bareAddress = RepositoryUtil.findRepositoryAddress(rpyPath, jsonObject.get("rpyId").toString());
        if (filePath.startsWith("/")){
            filePath = StringUtils.substringAfter(filePath, "/");
        }
        //下载裸仓库文件
        byte[] bytes = GitFileUtil.downLoadBareRepoFile(filePath, bareAddress,branch,type);
        return bytes;
    }

    @Override
    public void downLoadBareRepo(HttpServletResponse response,String queryString) {
        JSONObject downLoadQuery = this.getDownLoadQuery(queryString);

        String headName;

        String rpyName = downLoadQuery.get("rpyName").toString();
        String type = downLoadQuery.get("type").toString();
        Object branchObj = downLoadQuery.get("branch");
        String refCodeType = downLoadQuery.get("refCodeType").toString();

        if (ObjectUtils.isNotEmpty(branchObj)){
            headName=branchObj.toString();

        }else {
            headName = downLoadQuery.get("tag").toString();
        }

        String rpyPath = yamlDataMaService.repositoryAddress();

        //裸仓库地址
        String bareAddress = RepositoryUtil.findRepositoryAddress(rpyPath, downLoadQuery.get("rpyId").toString());
       //下载裸仓库为zip
        if (("zip").equals(type)){
            response.setContentType("application/zip");
            response.setHeader("Content-Disposition", "attachment; filename="+rpyName+"."+type);
            //下载裸仓库文件
            GitFileUtil.downLoadBareRepoZip(bareAddress,headName,refCodeType,response);
        }
        //下载裸仓库为tar
        if (("tar").equals(type)){
            response.setContentType("application/x-tar");
            response.setHeader("Content-Disposition", "attachment; filename="+rpyName+"."+type);
            //下载裸仓库文件
            GitFileUtil.downLoadBareRepoTar(bareAddress,headName,refCodeType,response);
        }

    }


    /**
     * 读取文件
     * @param fileQuery 文件信息
     * @return 文件信息
     */
    @Override
    public FileMessage readBareFile(FileQuery fileQuery) {
        String rpyId = fileQuery.getRpyId();
        String fileAddress = fileQuery.getFileAddress();
        String refCode = fileQuery.getRefCode();

        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),rpyId);
        FileMessage fileMessage ;
        try {

            if (fileAddress.startsWith("/")){
                 fileAddress = fileAddress.substring(1);

            }
            //fileAddress= StringUtils.substringAfter(fileAddress,"/");
            //读取裸仓库文件
             fileMessage = GitFileUtil.readBranchFile(repositoryAddress, fileAddress, refCode, fileQuery.getRefCodeType());

           //如果该文件为lfs文件时，判断lfs文件是否存在
            if (StringUtils.isNotEmpty(fileMessage.getOid())){
                String rpyLfsPath = RepositoryUtil.getRpyLfsPath(yamlDataMaService.repositoryAddress(), rpyId);
                File file = new File(rpyLfsPath, fileMessage.getOid());
                if (!file.exists()) {
                    fileMessage.setFileExist(false);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fileMessage;
    }


    @Override
    public byte[] downloadLfsFile(JSONObject jsonObject) {
        Object oid = jsonObject.get("oid");
        try {
            File file=null;
            //下载lfs 文件
            if (ObjectUtils.isNotEmpty(oid)){
                //仓库lfs文件路径
                String rpyLfsPath = RepositoryUtil.getRpyLfsPath(yamlDataMaService.repositoryAddress(), jsonObject.get("rpyId").toString());
                file = new File(rpyLfsPath, oid.toString());
            }else {
                return null;
            }
            //获取lfs 文件名字
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bytes = IOUtils.toByteArray(fileInputStream);

       /*     String s = new String(byteArray, "UTF-8");
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(bytes);
            byte[] digest = md.digest();
            String fileName = new BigInteger(1, digest).toString(16).toUpperCase();*/

            return bytes;
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    @Override
    public void downloadLfsFile(JSONObject downLoadQuery, HttpServletResponse response) {
        Object oid = downLoadQuery.get("oid");

        String filePath = downLoadQuery.get("filePath").toString();
        String fileName =  filePath.substring( filePath.lastIndexOf("/")+1);
        response.setHeader("Content-Disposition", "attachment; filename="+fileName);
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            File file=null;
            //下载lfs 文件
            if (ObjectUtils.isNotEmpty(oid)){
                //仓库lfs文件路径
                String rpyLfsPath = RepositoryUtil.getRpyLfsPath(yamlDataMaService.repositoryAddress(), downLoadQuery.get("rpyId").toString());
                file = new File(rpyLfsPath, oid.toString());
            }else {
                throw new SystemException("没有传入下载的oid");
            }
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                outputStream.write(buffer);
            }
        } catch (Exception e) {
            throw new SystemException(e);
        }
    }

    public JSONObject getDownLoadQuery(String queryString){
        // 创建一个空的 JSONObject
        JSONObject jsonObject = new JSONObject();
        String[] pairs = queryString.split("&");

        // 处理每一个键值对
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            // 将键值对添加到 JSONObject
            if (keyValue.length == 2) {
                jsonObject.put(keyValue[0], keyValue[1]);
            }
        }
        return jsonObject;
    }

}






















































