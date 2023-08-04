package io.tiklab.xcode.repository.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class XcodeYamlDataMaServiceImpl implements XcodeYamlDataMaService {

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

    @Override
    public String repositoryAddress() {
        return repositoryAddress;
    }

    @Override
    public String backupAddress() {
        return backupAddress;
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


}
