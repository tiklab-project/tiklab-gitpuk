package net.tiklab.xcode.authority;

import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.eclipse.jgit.transport.ReceivePack;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 该类实现类git-receive-pack钩子
 */

public class GitReceivePackCommand  implements Command, Runnable {
    private final ReceivePack receivePack;
    private InputStream in;
    private OutputStream out;
    private OutputStream err;

    private ExitCallback exit;

    public GitReceivePackCommand(ReceivePack receivePack) {
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
     * @param environment 变量
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
