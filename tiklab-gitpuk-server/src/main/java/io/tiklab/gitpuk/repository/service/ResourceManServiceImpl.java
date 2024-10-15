package io.tiklab.gitpuk.repository.service;

import io.tiklab.gitpuk.repository.model.ResourceMan;
import io.tiklab.gitpuk.common.GitPukYamlDataMaService;
import io.tiklab.gitpuk.common.RepositoryUtil;
import io.tiklab.licence.licence.model.Version;
import io.tiklab.licence.licence.service.VersionService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class ResourceManServiceImpl implements ResourceManService{

    @Autowired
    GitPukYamlDataMaService yamlDataMaService;

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

        //使用大小
        long logBytes = FileUtils.sizeOfDirectory(file);
        String size = RepositoryUtil.countStorageSize(logBytes);
        resourceMan.setUsedSpace(size);

        //总大小
        resourceMan.setAllSpace(getDiskSize(repositoryAddress));

        Version version = versionService.getVersion();
        resourceMan.setVersion(version.getRelease());


        return resourceMan;
    }

    /**
     * 磁盘大小
     * @return
     */
    public String getDiskSize(String repositoryAddress){
        float diskSize = RepositoryUtil.findDiskSize(repositoryAddress);
        return diskSize+"GB";
    }

}
