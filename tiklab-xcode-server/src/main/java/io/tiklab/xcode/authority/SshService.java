package io.tiklab.xcode.authority;


import io.tiklab.core.exception.ApplicationException;
import io.tiklab.xcode.setting.service.AuthServer;
import io.tiklab.xcode.util.RepositoryFinal;
import io.tiklab.xcode.util.RepositoryUtil;
import org.apache.sshd.common.keyprovider.FileKeyPairProvider;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.command.CommandFactory;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.ReceivePack;
import org.eclipse.jgit.transport.UploadPack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * git ssh连接
 */

@Configuration
public class SshService {

    @Autowired
    ValidUsrPwdServer validUsrPwdServer;

    @Autowired
    private AuthServer authServer;


    @Value("${xcode.ssh.port:10000}")
    private int sshPort;

    @Value("${xcode.ssh.key:/conf/id_rsa}")
    private String sshKey;

    private static final Logger logger = LoggerFactory.getLogger(SshService.class);

    @Bean
    public  void sshAuthority()  {
        SshServer sshServer = SshServer.setUpDefaultServer();
        sshServer.setPort(sshPort);
        sshServer.setHost (RepositoryFinal.SSH_HOST);
        String property = System.getProperty("user.dir");
        if (!property.endsWith(RepositoryFinal.APP_NAME)){
            File file = new File(property);
            property= file.getParent();
        }
        Path ssh_key = Paths.get(property+"/"+sshKey);
        logger.info("ssh listening address："+ sshPort);
        logger.info("pryKey address："+ ssh_key);
        FileKeyPairProvider keyProvider = new FileKeyPairProvider(ssh_key);
        sshServer.setKeyPairProvider(keyProvider);

        try {
            sshServer.start();
            sshServer.setCommandFactory(new XcodeSshCommandFactory());
        } catch (Exception e) {
            throw new ApplicationException(e);
        }

        //秘钥认证失败
        PucKeyValidServerImpl publicKeyAuthenticator = new PucKeyValidServerImpl(authServer);
        sshServer.setPublickeyAuthenticator(publicKeyAuthenticator);

        //效验账户名密码
        PasswordAuthenticator passwordAuthenticator =
                (username, password, session) -> validUsrPwdServer.validUserNamePassword(username,password,"1");
        sshServer.setPasswordAuthenticator(passwordAuthenticator);

    }


    /**
     * 通过不同请求触发不同钩子
     */
    private static class XcodeSshCommandFactory implements CommandFactory {

        public XcodeSshCommandFactory() {
        }

        @Override
        public Command createCommand(ChannelSession channelSession, String command) {
            String cmd = command.replace("'", "");
            String s = cmd.split(" ")[1];
            File file = new File(RepositoryUtil.defaultPath() + s);
            String repositoryPath = file.getAbsolutePath();

            logger.info("ssh repository address " + " " + repositoryPath);

            try {
                File file1 = new File(repositoryPath);

                if (!file1.exists()) {
                    throw new ApplicationException("仓库不存在");
                }
                Repository repo = Git.open(file1).getRepository();

                if (command.startsWith("git-upload-pack")) {
                    UploadPack uploadPack = new UploadPack(repo);
                    repo.close();
                    return new UploadPackCommand(uploadPack);
                } else if (command.startsWith("git-receive-pack")) {
                    ReceivePack receivePack = new ReceivePack(repo);
                    repo.close();
                    return new ReceivePackCommand(receivePack);
                }
            } catch (Exception | Error e) {
                throw new ApplicationException("仓库不存在");
            }
            return null;
        }

    }

    /**
     * 实现git-upload-pack钩子
     */
    private static class UploadPackCommand implements Command, Runnable {
        private final UploadPack uploadPack;
        private InputStream in;
        private OutputStream out;
        private OutputStream err;

        private ExitCallback exit;

        public UploadPackCommand(UploadPack uploadPack) {
            this.uploadPack = uploadPack;
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
        public void setErrorStream(OutputStream err) {
            this.err = err;
        }

        @Override
        public void run() {
            try {
                uploadPack.upload(in, out, err);
                this.exit.onExit(0);
            } catch (Exception e) {
                try {
                    throw new IOException(e);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        @Override
        public void setExitCallback(ExitCallback exit) {
            this.exit = exit;
        }

        @Override
        public void start(ChannelSession channelSession, Environment environment) throws IOException {
            new Thread(this).start();
        }

        @Override
        public void destroy(ChannelSession channelSession) throws Exception {
        }

    }

    /**
     * 实现git-receive-pack钩子
     */
    private static class ReceivePackCommand implements Command, Runnable {
        private final ReceivePack receivePack;
        private InputStream in;
        private OutputStream out;
        private OutputStream err;

        private ExitCallback exit;

        public ReceivePackCommand(ReceivePack receivePack) {
            this.receivePack = receivePack;
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
        public void setErrorStream(OutputStream err) {
            this.err = err;
        }

        @Override
        public void run() {
            try {
                receivePack.receive(in, out, err);
                this.exit.onExit(0);
            } catch (Exception e) {
                try {
                    throw new IOException(e);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        /**
         * @param exit 退出
         */
        @Override
        public void setExitCallback(ExitCallback exit) {
            this.exit = exit;
        }

        /**
         * @param channelSession 实例
         * @param environment    变量
         * @throws IOException 异常
         */
        @Override
        public void start(ChannelSession channelSession, Environment environment) throws IOException {
            // String clientAddress = channelSession.getSession().getClientAddress().toString();
            //
            // if (!ValidKey.isType()){
            //     this.exit.onExit(3,"Permission Denied");
            // }
            new Thread(this).start();
        }

        /**
         * @param channelSession 实例
         * @throws Exception 异常
         */
        @Override
        public void destroy(ChannelSession channelSession) throws Exception {

        }
    }






















}






























