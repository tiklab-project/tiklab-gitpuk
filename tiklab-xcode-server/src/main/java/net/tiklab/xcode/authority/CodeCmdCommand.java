package net.tiklab.xcode.authority;

import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;
import org.eclipse.jgit.lib.Ref;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CodeCmdCommand implements Command, Runnable  {

    private final String command;

    private final String message;

    @SuppressWarnings("unused")
    private InputStream in;

    @SuppressWarnings("unused")
    private OutputStream out;

    private OutputStream err;

    private ExitCallback callback;

    public CodeCmdCommand(String command) {

        this.command = command;

        this.message = "exec command: " + command;
    }


    public String getMessage() {
        return message;
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
    public void setExitCallback(ExitCallback callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        System.out.println("命令：" + command);
        // Read the input from the Git client
        // BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        // String line = null;
        try {
            // line = reader.readLine();

            // Parse the command
            String[] commandParts = command.split(" ");
            String gitCommand = commandParts[0];

            // Execute the command
            if (gitCommand.equals("git-receive-pack")) {
                // Implement the git-upload-pack command here
                out.write("Successfully executed git-upload-pack command\n".getBytes());
            } else if (gitCommand.equals("git-upload-pack")) {
                // Implement the git-receive-pack command here
                String cmd = "010f00320dca01d6f4a3ce5aadcf0c3ca84076569da2 HEADmulti_ack thin-pack side-band side-band-64k ofs-delta shallow deepen-since deepen-not deepen-relative no-progress include-tag multi_ack_detailed symref=HEAD:refs/heads/master object-format=sha1 agent=git/2.39.1.windows.1\n" +
                        "003c0e98bebead816f6a3dd3f9a48211ab32a79bb633 refs/heads/aaa\n" +
                        "003f00320dca01d6f4a3ce5aadcf0c3ca84076569da2 refs/heads/master\n" +
                        "0000\n";

                out.write(cmd.getBytes());
            }

            // Call the exit callback to indicate the command has completed
            callback.onExit(0);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void start(ChannelSession channel, Environment env) throws IOException {
        err.write(("执行命令："+command+"\n").getBytes());
        err.flush();

        String s = "010f00320dca01d6f4a3ce5aadcf0c3ca84076569da2 HEADmulti_ack thin-pack " +
                "side-band side-band-64k ofs-delta shallow deepen-since deepen-not deepen-relative no-progress include-tag " +
                "multi_ack_detailed symref=HEAD:refs/heads/master " +
                "object-format=sha1 agent=git/2.39.1.windows.1\n" +
                "003c0e98bebead816f6a3dd3f9a48211ab32a79bb633 refs/heads/aaa\n" +
                "003f00320dca01d6f4a3ce5aadcf0c3ca84076569da2 refs/heads/master\n" +
                "0000";




        out.write(s.getBytes());
        out.flush();





        callback.onExit(0);



        // runGitUploadPackCommand(out);




    }

    private void runGitUploadPackCommand(OutputStream outputStream) {


        List<String> command = new ArrayList<>();
        command.add("git");
        command.add("upload-pack");
        command.add("--stateless-rpc");
        command.add("C:\\Users\\admin\\xcode\\repository\\aa.git");

        // Create a ProcessBuilder to run the Git command
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);

        try {
            // Start the Git process
            Process process = pb.start();

            // Connect the output stream of the client to the input stream of the Git process
            OutputStream gitInputStream = process.getOutputStream();
            // Thread copyThread = new Thread(new StreamCopier(gitInputStream, gitInputStream));
            // copyThread.start();

            // Connect the input stream of the client to the output stream of the Git process
            InputStream gitOutputStream = process.getInputStream();
            Thread copyThread = new Thread(new StreamCopier(gitOutputStream, outputStream));
            copyThread.start();

            // Wait for the copy threads to complete
            copyThread.join();

            // Wait for the Git process to complete
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            // Handle the exceptions thrown by the ProcessBuilder and the waitFor method
            e.printStackTrace();
        }
    }




    @Override
    public void destroy(ChannelSession channel) {
        // ignored
    }



}
