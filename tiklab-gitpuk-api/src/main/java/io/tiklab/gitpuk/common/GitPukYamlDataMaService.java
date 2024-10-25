package io.tiklab.gitpuk.common;

public interface GitPukYamlDataMaService {


    /**
     * 仓库地址
     */
    String repositoryAddress();




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
     * spotbugsAddress的地址
     */
    String spotbugsAddress();


    /**
     * 扫描文件存放地址
     */
    String scanFileAddress();

    /**
     * lfs 回调用地址
     * @param visitAddress 地址前缀
     * @param rpyPath 仓库路径  仓库组/仓库名字
     */
    String lfsCallBackPath(String visitAddress,String rpyPath);
}
