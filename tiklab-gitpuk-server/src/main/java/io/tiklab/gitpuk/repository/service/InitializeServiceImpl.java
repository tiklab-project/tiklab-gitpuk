package io.tiklab.gitpuk.repository.service;

import io.tiklab.core.exception.SystemException;
import io.tiklab.gitpuk.repository.model.Repository;
import io.tiklab.gitpuk.repository.model.RepositoryQuery;
import io.tiklab.gitpuk.common.GitPukYamlDataMaServiceImpl;
import io.tiklab.gitpuk.common.RepositoryFileUtil;
import io.tiklab.privilege.dmRole.model.DmRole;
import io.tiklab.privilege.dmRole.model.DmRoleQuery;
import io.tiklab.privilege.dmRole.service.DmRoleService;
import io.tiklab.privilege.role.model.Role;
import io.tiklab.privilege.role.model.RoleQuery;
import io.tiklab.privilege.role.service.RoleService;
import io.tiklab.toolkit.context.AppContext;
import io.tiklab.user.user.model.User;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ObjectUtils;
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

    @Autowired
    RoleService roleService;

    @Autowired
    DmRoleService dmRoleService;


    @Override
    public void createSampleData() {
        updateRepRole();
        //已经存在演示库就不需要在初始化
        Repository demoRepository = repositoryServer.findDemoRepository();

        if (ObjectUtils.isNotEmpty(demoRepository)){
            return;
        }
        File zipFile = new File(AppContext.getAppHome()+"/file/sample.zip");
        //示例文件不存在 不创建示例数据
        if (!zipFile.exists()){
          return;
        }

        //创建示例仓库
        Repository repository = new Repository();
        repository.setName("sample");
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

    @Override
    public void updateRepRole() {
        List<Role> allRole = roleService.findAllRole();


        if (CollectionUtils.isNotEmpty(allRole)){
            for (Role role:allRole){
                //移除项目管理员
                if ((("3").equals(role.getParentId()))||("3").equals(role.getId())){
                    roleService.deleteRole(role.getId());

                    DmRoleQuery dmRoleQuery = new DmRoleQuery();
                    dmRoleQuery.setRoleId(role.getId());
                    List<DmRole> dmRoleList = dmRoleService.findDmRoleListByQuery(dmRoleQuery);
                  if (CollectionUtils.isNotEmpty(dmRoleList)){
                      for (DmRole dmRole:dmRoleList){
                          dmRoleService.deleteDmRole(dmRole.getId());
                      }
                  }

                }

                //修改项目超级管理员
                if (("pro_111111").equals(role.getParentId()) ||("pro_111111").equals(role.getId())){
                    role.setName("项目管理员");
                    roleService.updateRole(role);
                }

                if (("管理员角色").equals(role.getName())){
                    role.setName("管理员");
                    roleService.updateRole(role);
                }
                if (("普通角色").equals(role.getName())){
                    role.setName("普通用户");
                    roleService.updateRole(role);
                }
            }
        }
    }
}
