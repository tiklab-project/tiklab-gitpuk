package io.tiklab.gitpuk.authority;

public interface ValidUsrPwdServer {

    /**
     * 效验账户 密码
     * @param username 用户名
     * @param password 密码
     * @param id 效验类型
     * @return true 效验成功 false 失败
     */
    boolean validUserNamePassword(String username ,String password,String id);

    /**
     * 效验账户 密码
     * @param username 用户名
     * @param password 密码
     * @param id 效验类型
     * @return EamTicket
     */
    void validUser(String username ,String password,String id);

    /**
     * 通过用户查询是否有推送或者拉取的权限
     * @param username 用户名
     * @param repositoryId 仓库id
     * @return true 效验成功 false 失败
     */
    boolean validUserPrivilege(String username,String repositoryId);
}
