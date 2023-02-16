package net.tiklab.xcode.ssh;


import net.tiklab.core.exception.ApplicationException;
import net.tiklab.xcode.until.CodeUntil;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.command.CommandFactory;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.ReceivePack;
import org.eclipse.jgit.transport.UploadPack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 处理不同的ssh请求
 */

public class XcodeSshCommandFactory implements CommandFactory {

    private static final Logger logger = LoggerFactory.getLogger(XcodeSshCommandFactory.class);

    public XcodeSshCommandFactory() {
    }

    @Override
    public Command createCommand(ChannelSession channelSession, String command) {
        String cmd  = command.replace("'","");
        File file = new File(CodeUntil.defaultPath() + cmd.split(" ")[1]);
        String repositoryPath = file.getAbsolutePath();

        logger.info("ssh repository address " + " "+ repositoryPath);

        try {
            File file1 = new File(repositoryPath);

            if (!file1.exists()){
                throw  new ApplicationException("仓库不存在");
            }
            Repository repo = Git.open(file1).getRepository();

            if (command.startsWith("git-upload-pack")) {
                UploadPack uploadPack = new UploadPack(repo);
                repo.close();
                return new UploadPackCommand(uploadPack);
            }else if (command.startsWith("git-receive-pack")){
                ReceivePack receivePack = new ReceivePack(repo);
                repo.close();
                return new ReceivePackCommand(receivePack);
            }
        } catch (IOException e) {
           throw  new ApplicationException("仓库不存在");
        }
        return null;
    }

    /**
     * 该类实现类git-upload-pack钩子
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
     * 该类实现类git-receive-pack钩子
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
