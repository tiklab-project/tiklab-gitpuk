package io.tiklab.gitpuk.authority.lfs;

import io.tiklab.gitpuk.common.GitPukYamlDataMaService;
import org.apache.sshd.server.command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface LfsAuthService {

    /*
     *  解析客户端http方式上传的数据
     *  @param lfsData
     * */
    boolean HttpLfsAnalysis(HttpServletRequest request, HttpServletResponse response) throws IOException;

    /*
     *  解析客户端ssh方式上传的数据
     *  @param lfsData
     * */
    Command SshLfsCommand(GitPukYamlDataMaService yamlDataMaService, String ip, String rpyAddress);
}
