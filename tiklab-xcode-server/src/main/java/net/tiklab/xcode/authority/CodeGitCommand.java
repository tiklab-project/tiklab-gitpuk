package net.tiklab.xcode.authority;

import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;

import java.io.*;

public class CodeGitCommand implements Command {
    private InputStream in;
    private OutputStream out;
    private OutputStream err;
    private ExitCallback callback;

    public CodeGitCommand() {
    }

    @Override
    public void setInputStream(InputStream in) {
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
    public void setExitCallback(ExitCallback callback) {
        this.callback = callback;
    }

    @Override
    public void start(ChannelSession channel, Environment env) throws IOException {
        // Read the command from the input stream
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String command = reader.readLine();

        // Check if the command is "git-upload-pack"
        if (command.startsWith("git-upload-pack")) {
            // Handle the Git upload-pack command

            // Write the response to the output stream
            out.write("001e# service=git-upload-pack\n".getBytes());
            out.write("0000".getBytes());
            out.flush();

            // Read the client's request
            String clientRequest = reader.readLine();
            while (!clientRequest.startsWith("done")) {
                String[] requestParts = clientRequest.split(" ");
                String commandType = requestParts[0];
                String objectHash = requestParts[1];

                switch (commandType) {
                    case "want":
                        // Handle the want command by sending a have command
                        out.write(("have " + objectHash + "\n").getBytes());
                        out.flush();
                        break;
                    case "have":
                        // Handle the have command by storing the object hash in a list
                        break;
                    default:
                        // Handle any other commands
                        break;
                }
                // Parse the client's request and perform the appropriate action

                // Write the response to the output stream
                out.write("0000".getBytes());
                out.flush();
            }

            // Call the exit callback to indicate the command has finished executing
            callback.onExit(0);
        }
    }

    /**
     * This method is called by the SSH server to destroy the command because the client has disconnected somehow.
     *
     * @param channel The {@link ChannelSession} through which the command has been received
     * @throws Exception if failed to destroy
     */
    @Override
    public void destroy(ChannelSession channel) throws Exception {

    }

}
