package io.thoughtware.gittok.repository.service;

import io.thoughtware.gittok.common.GitTokYamlDataMaService;
import io.thoughtware.gittok.common.RepositoryUtil;
import io.thoughtware.gittok.repository.model.ResourceMan;
import io.thoughtware.licence.licence.model.Version;
import io.thoughtware.licence.licence.service.VersionService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ResourceManServiceImpl implements ResourceManService{

    @Autowired
    GitTokYamlDataMaService yamlDataMaService;

    @Autowired
    VersionService versionService;

    @Override
    public ResourceMan findResource() {
        ResourceMan resourceMan = new ResourceMan();

        String repositoryAddress = yamlDataMaService.repositoryAddress();
        File file = new File(repositoryAddress);
        //第一次还未创建文件夹
        if (!file.exists()){
            return null;
        }

        long logBytes = FileUtils.sizeOfDirectory(file);
        String size = RepositoryUtil.countStorageSize(logBytes);
        resourceMan.setUsedSpace(size);

        float diskSize = RepositoryUtil.findDiskSize(repositoryAddress);
        resourceMan.setAllSpace(diskSize+"GB");

        Version version = versionService.getVersion();
        resourceMan.setVersion(version.getRelease());


        return resourceMan;
    }
}
