package net.tiklab.xcode.authority;

import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.eclipse.jgit.transport.UploadPack;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 该类实现类git-upload-pack钩子
 */


public class GitUploadPackCommand implements Command, Runnable {
    private final UploadPack uploadPack;
    private InputStream in;
    private OutputStream out;
    private OutputStream err;

    private ExitCallback exit;

    public GitUploadPackCommand(UploadPack uploadPack) {
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

    /**
     * @param exit
     */
    @Override
    public void setExitCallback(ExitCallback exit) {
        this.exit = exit;
    }


    /**
     * @param channelSession
     * @param environment
     * @throws IOException
     */
    @Override
    public void start(ChannelSession channelSession, Environment environment) throws IOException {
        new Thread(this).start();
    }

    /**
     * @param channelSession
     * @throws Exception
     */
    @Override
    public void destroy(ChannelSession channelSession) throws Exception {

    }

}
