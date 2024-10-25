package io.tiklab.gitpuk.authority.ssh;

import io.tiklab.core.exception.SystemException;
import io.tiklab.gitpuk.common.GitPukYamlDataMaService;
import org.apache.commons.lang.StringUtils;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;

import java.io.*;
import java.nio.charset.StandardCharsets;


/**
 * 实现git-lfs钩子
 */
public class GitLfsCommand  implements Command,Runnable {

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
        new Thread(this).start();

    }

    @Override
    public void destroy(ChannelSession channel) throws Exception {
        // 可在此处清理资源
    }

    @Override
    public void run() {
        String visitAddress = yamlDataMaService.visitAddress();
        if (StringUtils.isEmpty(visitAddress)){
            String path = ip + ":" + yamlDataMaService.serverPort();
            visitAddress=  "http://"+path;
        }
        String returnPath = yamlDataMaService.lfsCallBackPath(visitAddress, rpyAddress);
        try {
            String jsonResponse = "{\n" +
                    "  \"href\": \""+returnPath+"\",\n" +
                    "  \"header\": {\n" +
                    "    \"Authorization\": \"Bearer YOUR_AUTH_TOKEN\"\n" +
                    "  }\n" +
                    "}";
            out.write(jsonResponse.getBytes(StandardCharsets.UTF_8));
            out.flush();
            this.exit.onExit(0);
        }catch (Exception e){
            throw  new SystemException(e);
        }

    }
}
