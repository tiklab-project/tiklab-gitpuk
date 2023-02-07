package net.tiklab.xcode.authority;

import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.apache.sshd.server.command.CommandFactory;
import org.apache.sshd.server.session.ServerSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SshCommandFactory implements CommandFactory {


    /**
     * Create a command with the given name. If the command is not known, a dummy command should be returned to allow
     * the display output to be sent back to the client.
     *
     * @param channel The {@link ChannelSession} through which the command has been received
     * @param command The command that will be run
     * @return a non {@code null} {@link Command} instance
     * @throws IOException if failed to create the instance
     */
    @Override
    public Command createCommand(ChannelSession channel, String command) throws IOException {
        return new CodeCmdCommand(command);
    }












}
