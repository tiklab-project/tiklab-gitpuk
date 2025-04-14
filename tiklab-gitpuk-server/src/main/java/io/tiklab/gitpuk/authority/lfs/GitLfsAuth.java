package io.tiklab.gitpuk.authority.lfs;

import io.tiklab.gitpuk.authority.ssh.GitLfsCommand;
import io.tiklab.gitpuk.authority.utils.ReturnResponse;
import io.tiklab.gitpuk.common.GitPukYamlDataMaService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class GitLfsAuth implements LfsAuthService {

    @Override
    public boolean HttpLfsAnalysis(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //社区版本不支持 lfs文件存储
        ReturnResponse.lfsNotSupport(response);
        return false;
    }

    @Override
    public GitLfsCommand SshLfsCommand(GitPukYamlDataMaService yamlDataMaService,String ip,String rpyAddress) {
        GitLfsCommand gitLfsCommand = new GitLfsCommand(yamlDataMaService, ip, rpyAddress);
        return gitLfsCommand;
    }

}
