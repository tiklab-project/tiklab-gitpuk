package io.tiklab.xcode.detection.service;

import io.tiklab.core.exception.ApplicationException;
import io.tiklab.xcode.detection.model.AuthThird;
import io.tiklab.xcode.detection.model.AuthThirdQuery;
import io.tiklab.xcode.repository.model.Repository;
import io.tiklab.xcode.repository.service.RepositoryServer;
import io.tiklab.xcode.util.RepositoryUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class CodeScanManageServiceImpl implements CodeScanManageService{

    @Autowired
    AuthThirdService authThirdService;

    @Autowired
    RepositoryServer repositoryServer;

    @Override
    public boolean codeScanExec(String repositoryId) {
        Repository repository = repositoryServer.findOneRpy(repositoryId);

        String execOrder =  "mvn clean verify sonar:sonar ";
        List<AuthThird> authThirdList = authThirdService.findAuthThirdList(new AuthThirdQuery().setAuthName("sonar"));
        if (CollectionUtils.isEmpty(authThirdList)){
            throw new ApplicationException("不存在sonar配置");
        }
        AuthThird authThird = authThirdList.get(0);
        String mavenAddress = authThird.getMavenAddress();
        RepositoryUtil.validFile(mavenAddress,21);

        execOrder = execOrder +
                " -Dsonar.projectKey="+ repository.getName()+
                " -Dsonar.host.url="+ authThird.getServerAddress();
        if (authThird.getAuthType() == "account"){
            execOrder = execOrder +
                    " -Dsonar.login="+authThird.getUserName()+
                    " -Dsonar.password="+authThird.getPassWord();
        }else {
            execOrder = execOrder +
                    " -Dsonar.login="+authThird.getPrivateKey();
        }

        String path = RepositoryUtil.findFileAddress(null);
        String order = " ./" + execOrder + " " + "-f" +" " +path ;
        if (RepositoryUtil.findSystemType() == 1){
            order = " .\\" + execOrder + " " + "-f"+" "  +path;
        }
        try {
            RepositoryUtil.process(mavenAddress, order);
        }catch (Exception e){
            return  false;
        }
        return true;
    }


}
