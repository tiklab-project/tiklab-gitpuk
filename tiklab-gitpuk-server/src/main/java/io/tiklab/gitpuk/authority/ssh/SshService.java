package io.tiklab.gitpuk.authority.ssh;


import io.tiklab.gitpuk.authority.ValidUsrPwdServer;
import io.tiklab.gitpuk.common.GitPukYamlDataMaService;
import io.tiklab.gitpuk.repository.service.RecordCommitService;
import io.tiklab.gitpuk.setting.service.AuthSshServer;
import io.tiklab.gitpuk.common.RepositoryFinal;
import io.tiklab.gitpuk.repository.service.RepositoryService;
import io.tiklab.core.context.AppHomeContext;
import io.tiklab.core.exception.ApplicationException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.sshd.common.keyprovider.FileKeyPairProvider;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.command.CommandFactory;
import org.apache.sshd.server.session.ServerSession;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;


/**
 * git ssh连接
 */

@Configuration
public class SshService {
    private static Logger logger = LoggerFactory.getLogger(Logger.class);
    @Autowired
    ValidUsrPwdServer validUsrPwdServer;

    @Autowired
    AuthSshServer authSShServer;

    @Autowired
    RepositoryService repositoryServer;

    @Autowired
    RecordCommitService recordCommitService;

    @Autowired
    AuthInstance authInstance;


    @Value("${gitPuk.ssh.port:10000}")
    private int sshPort;

    @Autowired
    GitPukYamlDataMaService GitTokYamlDataMaService;



    @Bean
    public  void sshAuthority()  {

        SshServer sshServer = SshServer.setUpDefaultServer();

        sshServer.setPort(sshPort);
        sshServer.setHost (RepositoryFinal.SSH_HOST);
        String property = System.getProperty("user.dir");
      /*  if (!property.endsWith(RepositoryFinal.APP_NAME)){
            File file = new File(property);
            property= file.getParent();
        }*/
        // 设置主机密钥提供者
        String appHome = AppHomeContext.getAppHome();
        Path ssh_key = Paths.get(appHome+"/file/id_rsa");
        FileKeyPairProvider keyProvider = new FileKeyPairProvider(ssh_key);
        sshServer.setKeyPairProvider(keyProvider);

        // 设置公钥认证器
        PublickeyAuthenticator publicKeyValid = authInstance.getPublicKeyValid(authSShServer);
        sshServer.setPublickeyAuthenticator(publicKeyValid);
        try {
            sshServer.setCommandFactory(new GitTokSshCommandFactory(GitTokYamlDataMaService,repositoryServer,recordCommitService));
            sshServer.start();
            int port = sshServer.getPort();
            logger.info("ssh端口："+port);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }

        //提交时 需要输入账号密码进行校验
/*        PasswordAuthenticator passwordAuthenticator =
                (username, password, session) -> validUsrPwdServer.validUserNamePassword(username,password,"1");
        sshServer.setPasswordAuthenticator(passwordAuthenticator);*/
    }


    /**
     * 通过不同请求触发不同钩子
     */
    private static class GitTokSshCommandFactory implements CommandFactory {
        private static Logger logger = LoggerFactory.getLogger(GitTokSshCommandFactory.class);

        private final GitPukYamlDataMaService yamlDataMaService;

        private final RepositoryService repositoryServer;

        private final RecordCommitService recordCommitService;

        public GitTokSshCommandFactory(GitPukYamlDataMaService yamlDataMaService, RepositoryService repositoryServer, RecordCommitService recordCommitService) {
            this.yamlDataMaService = yamlDataMaService;
            this.repositoryServer=repositoryServer;
            this.recordCommitService=recordCommitService;
        }


        @Override
        public Command createCommand(ChannelSession channelSession, String command) {
            ServerSession session = channelSession.getSession();
            String clientAddress = session.getClientAddress().toString();

            //提交用户
            String userId = session.getAttribute(AttributeKeys.USERNAME_KEY);

            String cmd = command.replace("'", "");
            String rpyPath = cmd.split(" ")[1];
           if (rpyPath.startsWith("/")){
               rpyPath = rpyPath.substring(1).replace(".git","");
           }else {
               rpyPath = rpyPath.replace(".git","");
           }
            io.tiklab.gitpuk.repository.model.Repository repository = repositoryServer.findRepositoryByAddress(rpyPath);
            if (ObjectUtils.isNotEmpty(repository)){
                String rpyId = repository.getRpyId();
                String repositoryPath = yamlDataMaService.repositoryAddress() +"/"+ rpyId+".git";
                try {
                    File file1 = new File(repositoryPath);

                    if (!file1.exists()) {
                        throw new ApplicationException("仓库不存在");
                    }
                    Repository repo = Git.open(file1).getRepository();

                    if (command.startsWith("git-upload-pack")) {
                        //ssh 拉取
                        UploadPack uploadPack = new UploadPack(repo);
                        repo.close();
                        return new UploadPackCommand(uploadPack);
                    } else if (command.startsWith("git-receive-pack")) {
                        //ssh 上传
                        ReceivePack receivePack = new ReceivePack(repo);
                        repo.close();
                        ReceivePackCommand receivePackCommand = new ReceivePackCommand(receivePack, repositoryServer, recordCommitService,
                                rpyPath, userId);

                        return receivePackCommand;

                        //git lfs文件
                    }else if (command.startsWith("git-lfs-authenticate")){
                        //截取上传路径或ip
                        String s = StringUtils.substringBefore(clientAddress, ":");
                        if (s.startsWith("/")){
                            s=StringUtils.substringAfter(s, "/");
                        }
                        return new GitLfsCommand(yamlDataMaService,s,rpyPath);
                    }
                } catch (Exception | Error e) {
                    throw new ApplicationException("仓库不存在");
                }
            }
            logger.info("仓库不存在");
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

        private RepositoryService repositoryServer;

        private String rpyPath;

        private RecordCommitService recordCommitService;

        private String userId;

        public ReceivePackCommand(ReceivePack receivePack,RepositoryService repositoryServer,RecordCommitService recordCommitService,String rpyPath,String userId) {
            this.receivePack = receivePack;
            this.repositoryServer=repositoryServer;
            this.rpyPath=rpyPath;
            this.recordCommitService=recordCommitService;
            this.userId=userId;
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

                //PostReceiveHook 来检查引用（refs）是否有更新，从而判断是否有更改被推送。
                receivePack.setPostReceiveHook(new PostReceiveHook() {
                    @Override
                    public void onPostReceive(ReceivePack rp, Collection<ReceiveCommand> commands) {

                        //push 推送后编辑仓库数据
                        repositoryServer.compileRepository(rpyPath);

                        recordCommitService.updateCommitRecord(rpyPath,userId,"ssh");

                      /*  for (ReceiveCommand command : commands) {
                            ReceiveCommand.Type type = command.getType();
                        }*/
                    }
                });

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

