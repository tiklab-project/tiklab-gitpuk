package io.tiklab.xcode.repository.service;

public interface XcodeYamlDataMaService {


    /**
     * 仓库地址
     */
    String repositoryAddress();

    /**
     * 仓库备份地址
     */
    String backupAddress();

    /**
     *仓库上传的路径
     */
    String visitAddress();

    /**
     *项目port
     */
    String serverPort();


    /**
     *ssh port
     */
    String sshPort();

    /**
     *jdbc host
     */
     String host();

    /**
     *jdbc dbName
     */
    String dbName();

    /**
     *jdbc schemaName
     */
    String schemaName();


}
