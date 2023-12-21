package io.thoughtware.gittork.repository.service;

import io.thoughtware.gittork.common.GitTorkYamlDataMaService;
import io.thoughtware.gittork.common.RepositoryUtil;
import io.thoughtware.gittork.repository.model.ResourceMan;
import io.thoughtware.licence.licence.model.Version;
import io.thoughtware.licence.licence.service.VersionService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ResourceManServiceImpl implements ResourceManService{

    @Autowired
    GitTorkYamlDataMaService yamlDataMaService;

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
        resourceMan.setAllSpace("un");

        Version version = versionService.getVersion();
        resourceMan.setVersion(version.getRelease());


        return resourceMan;
    }
}
