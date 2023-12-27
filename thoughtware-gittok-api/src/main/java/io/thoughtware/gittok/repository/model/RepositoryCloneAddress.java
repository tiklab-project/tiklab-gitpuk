package io.thoughtware.gittok.repository.model;

/**
 * 仓库克隆地址
 */
public class RepositoryCloneAddress {

    /**
     * http 克隆地址
     */
    private String httpAddress;

    /**
     * ssh 克隆地址
     */
    private String SSHAddress;

    /**
     * 文件克隆地址
     */
    private String fileAddress;

    public String getHttpAddress() {
        return httpAddress;
    }

    public void setHttpAddress(String httpAddress) {
        this.httpAddress = httpAddress;
    }

    public String getSSHAddress() {
        return SSHAddress;
    }

    public void setSSHAddress(String SSHAddress) {
        this.SSHAddress = SSHAddress;
    }

    public String getFileAddress() {
        return fileAddress;
    }

    public void setFileAddress(String fileAddress) {
        this.fileAddress = fileAddress;
    }
}














































