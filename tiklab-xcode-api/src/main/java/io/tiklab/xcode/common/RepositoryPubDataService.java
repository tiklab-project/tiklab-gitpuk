package io.tiklab.xcode.common;

public interface RepositoryPubDataService {


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

}
