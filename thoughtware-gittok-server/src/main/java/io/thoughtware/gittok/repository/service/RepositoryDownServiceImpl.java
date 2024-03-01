package io.thoughtware.gittok.repository.service;

import com.alibaba.fastjson.JSONObject;
import io.thoughtware.gittok.common.GitTokYamlDataMaService;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.api.ArchiveCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class RepositoryDownServiceImpl implements RepositoryDownService{

    @Autowired
    RepositoryServer repositoryServer;

    @Autowired
    GitTokYamlDataMaService dataMaService;

    @Override
    public String downloadRpy(String reqData, int name) {
        String[] split = reqData.split("&");
        String type = StringUtils.substringAfterLast(split[0], "=");
        String rpyId = StringUtils.substringAfterLast(split[1], "=");


        String rpyPath = dataMaService.repositoryAddress() + "/" + rpyId + ".git";

        return rpyPath;

    }


    private void addFilesToZip(ZipOutputStream zipOut, String folderPath, String parentFolderPath) throws IOException {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        byte[] buffer = new byte[1024];
        int bytesRead;

        for (File file : files) {
            if (file.isDirectory()) {
                String subFolderPath = parentFolderPath + file.getName() + "/";
                addFilesToZip(zipOut, file.getPath(), subFolderPath);
            } else {
                try (FileInputStream fis = new FileInputStream(file)) {
                    String entryPath = parentFolderPath + file.getName();
                    ZipEntry zipEntry = new ZipEntry(entryPath);
                    zipOut.putNextEntry(zipEntry);

                    while ((bytesRead = fis.read(buffer)) != -1) {
                        zipOut.write(buffer, 0, bytesRead);
                    }
                }
            }
        }
    }

}
