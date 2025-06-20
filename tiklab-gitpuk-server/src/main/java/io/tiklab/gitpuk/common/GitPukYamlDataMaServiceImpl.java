package io.tiklab.gitpuk.common;

import io.tiklab.toolkit.context.AppContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class GitPukYamlDataMaServiceImpl implements GitPukYamlDataMaService {

    @Value("${DATA_HOME}")
    private String dataHome;


    @Value("${external.url:null}")
    private String visitAddress;

    @Value("${server.port}")
    private String port;

    @Value("${gitPuk.ssh.port:null}")
    private String sshPort;

    @Value("${jdbc.url}")
    String jdbcUrl;

    @Value("${spotbugs.address:null}")
    private String spotbugsAddress;


    @Value("${DATA_HOME}")
    String DATA_HOME;

    @Override
    public String repositoryAddress() {
        return dataHome+"/repository";
    }


    @Override
    public String visitAddress() {
        if (("null".equals(visitAddress))){
            return null;
        }
        return visitAddress;
    }

    @Override
    public String serverPort() {
        return port;
    }

    @Override
    public String sshPort() {
        return sshPort;
    }


    @Override
    public String scanFileAddress() {
        return DATA_HOME+"/scan";
    }

    @Override
    public String lfsCallBackPath(String visitAddress,String rpyPath) {
        String returnPath= visitAddress+"/lfs/"+rpyPath;
        return returnPath;
    }

    @Override
    public String spotbugsAddress() {
        String appHome = AppContext.getAppHome();
        String path;
        if ("null".equals(spotbugsAddress)){
            path = new File(appHome).getParentFile().getParent()+"/embbed/spotbugs-4.8.1/bin";
        }else {
            path = appHome + spotbugsAddress;
        }
        return  path;
    }

}
