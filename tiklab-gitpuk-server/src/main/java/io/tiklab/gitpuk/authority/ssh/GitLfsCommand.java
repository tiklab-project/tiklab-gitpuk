package io.tiklab.gitpuk.authority.ssh;

import io.tiklab.core.exception.SystemException;
import io.tiklab.gitpuk.common.GitPukYamlDataMaService;
import org.apache.commons.lang.StringUtils;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;


/**
 * 实现git-lfs钩子
 */
public class GitLfsCommand  implements Command {

    private final GitPukYamlDataMaService yamlDataMaService;

    private OutputStream out;
    private OutputStream err;

    private InputStream in;
    private ExitCallback exit;

    private String ip;

    private String rpyAddress;

    public GitLfsCommand(GitPukYamlDataMaService yamlDataMaService, String ip,String rpyAddress) {
        this.yamlDataMaService = yamlDataMaService;
        this.ip=ip;
        this.rpyAddress=rpyAddress;
    }

    @Override
    public void setExitCallback(ExitCallback callback) {
        this.exit = callback;
    }

    @Override
    public void setErrorStream(OutputStream err) {
        this.err = err;
    }

    @Override
    public void setInputStream(InputStream in) {
        this.in = in;
    }

    @Override
    public void setOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void start(ChannelSession channel, Environment env) throws IOException {
        PrintStream errPrintStream = new PrintStream(err);

        errPrintStream.println("Error:This server does not support Git LFS. Please remove LFS files Or Upgrade community Edition" );
        errPrintStream.flush();
        exit.onExit(1, "This server does not support Git LFS. Please remove LFS files Or Upgrade community Edition"); // 失败退出

    }

    @Override
    public void destroy(ChannelSession channel) throws Exception {
        // 可在此处清理资源
    }
}
