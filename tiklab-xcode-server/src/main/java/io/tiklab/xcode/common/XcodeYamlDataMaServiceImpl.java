package io.tiklab.xcode.common;

import io.tiklab.core.context.AppHomeContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class XcodeYamlDataMaServiceImpl implements XcodeYamlDataMaService {

    @Value("${DATA_HOME}")
    private String dataHome;

    @Value("${repository.address}")
    private String repositoryAddress;

    @Value("${backup.address}")
    private String backupAddress;

    @Value("${visit.address:null}")
    private String visitAddress;

    @Value("${server.port}")
    private String port;

    @Value("${xcode.ssh.port:null}")
    private String sshPort;

    @Value("${jdbc.url}")
    String jdbcUrl;

    @Override
    public String repositoryAddress() {
        return repositoryAddress;
    }

    @Override
    public String backupAddress() {
        return backupAddress;
    }
    @Override
    public String uploadAddress() {
        return dataHome+"/upload";
    }

    @Override
    public String visitAddress() {
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
    public String host() {
        String substring = jdbcUrl.substring(jdbcUrl.indexOf("//", 1)+2, jdbcUrl.indexOf("/", jdbcUrl.indexOf("/") + 2));
        String[] split = substring.split(":");
        return split[0];
    }

    @Override
    public String dbName() {
        String dbName = jdbcUrl.substring(jdbcUrl.indexOf("/", jdbcUrl.indexOf("/") + 2)+1, jdbcUrl.indexOf("?", 1));
        return dbName;
    }

    @Override
    public String schemaName() {
        return "public";
    }

    @Override
    public String pgSqlAddress() {
        String appHome = AppHomeContext.getAppHome();
        String path = new File(appHome).getParentFile().getParentFile().getAbsolutePath();
        return path+"/embbed/pgsql-10.23/bin";
        //return "/Users/limingliang/postgreSQL/bin";
    }

}
