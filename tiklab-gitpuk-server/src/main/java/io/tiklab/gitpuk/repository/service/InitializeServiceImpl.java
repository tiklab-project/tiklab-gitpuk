package io.tiklab.gitpuk.repository.service;

import io.tiklab.core.context.AppHomeContext;
import io.tiklab.core.exception.SystemException;
import io.tiklab.gitpuk.repository.model.Repository;
import io.tiklab.gitpuk.repository.model.RepositoryQuery;
import io.tiklab.gitpuk.common.GitPukYamlDataMaServiceImpl;
import io.tiklab.gitpuk.common.RepositoryFileUtil;
import io.tiklab.user.user.model.User;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class InitializeServiceImpl implements InitializeService {

    @Autowired
    RepositoryService  repositoryServer;

    @Autowired
    GitPukYamlDataMaServiceImpl yamlDataMaService;


    @Override
    public void createSampleData() {

        //已经存在演示库就不需要在初始化
        List<Repository> repositoryList = repositoryServer.findRepositoryList(new RepositoryQuery().setCategory(1));
        if (CollectionUtils.isNotEmpty(repositoryList)){
            return;
        }
        File zipFile = new File(AppHomeContext.getAppHome()+"/file/sample.zip");
        //示例文件不存在 不创建示例数据
        if (!zipFile.exists()){
          return;
        }

        //创建示例仓库
        Repository repository = new Repository();
        repository.setName("示例仓库");
        repository.setAddress("admin/sample");
        repository.setRules("public");
        repository.setClassifyState("false");
        User user = new User();
        user.setId("111111");
        repository.setUser(user);
        repository.setCategory(1);

        long length = zipFile.length();
        repository.setSize(length);

        String rpyId = repositoryServer.createRpy(repository);
        //仓库数据放入开通租户内存下面
        try {
            String rpyAddress = yamlDataMaService.repositoryAddress();

            File memoryAddressFile = new File(yamlDataMaService.repositoryAddress());
            if (!memoryAddressFile.exists()){
                memoryAddressFile.mkdirs();
            }



            //将zip 拷贝到租户存储仓库文件下面
            FileUtils.copyFileToDirectory(zipFile,memoryAddressFile);
            //解压
            RepositoryFileUtil.decompressionZip(rpyAddress + "/sample.zip",rpyAddress);

            //修改压缩后仓库名字
            File sourceFile = new File(rpyAddress + "/sample.git");
            // 创建新的文件对象，包含新的文件名和原始文件所在目录路径
            File newFile = new File(rpyAddress, rpyId+".git");
            // 使用renameTo()方法重命名.git 仓库
            sourceFile.renameTo(newFile);

            File RpyZipFile = new File(rpyAddress + "/sample.zip");
            FileUtils.delete(RpyZipFile);
        } catch (IOException e) {
            throw new SystemException(e.getMessage());
        }
    }
}
