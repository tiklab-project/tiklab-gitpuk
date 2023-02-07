package net.tiklab.xcode.authority;

import org.apache.sshd.common.Factory;
import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.shell.ShellFactory;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.util.SystemReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

public class CodeShellCommand implements ShellFactory {


    /**
     * @param channelSession
     * @return
     * @throws IOException
     */
    @Override
    public Command createShell(ChannelSession channelSession) throws IOException {
        return new SendMessage();
    }


    private static class SendMessage implements Command {

        private InputStream in;
        private OutputStream out;
        private OutputStream err;
        private ExitCallback exit;


        @Override
        public void setInputStream(final InputStream in) {
            this.in = in;
        }

        @Override
        public void setOutputStream(final OutputStream out) {
            this.out = out;
        }

        @Override
        public void setErrorStream(final OutputStream err) {
            this.err = err;
        }

        @Override
        public void setExitCallback(final ExitCallback callback) {
            this.exit = callback;
        }



        /**
         * Starts the command execution. All streams must have been set <U>before</U> calling this method. The command
         * should implement {@link Runnable}, and this method should spawn a new thread like:
         *
         * <pre>
         * {@code Thread(this).start(); }
         * </pre>
         *
         * @param channel The {@link ChannelSession} through which the command has been received
         * @param env     The {@link Environment}
         * @throws IOException If failed to start
         */
        @Override
        public void start(ChannelSession channel, Environment env) throws IOException {
            err.write("执行信息".getBytes());
            err.flush();

            in.close();
            out.close();
            err.close();
            exit.onExit(0);
        }

        @Override
        public void destroy(ChannelSession channel) {

        }




    }


}
