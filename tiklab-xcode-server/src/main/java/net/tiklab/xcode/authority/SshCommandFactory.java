package net.tiklab.xcode.authority;


import net.tiklab.xcode.until.CodeUntil;
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

/**
 * 处理不同的ssh请求
 */

public class SshCommandFactory implements CommandFactory {

    private static final Logger logger = LoggerFactory.getLogger(SshCommandFactory.class);

    public SshCommandFactory() {
    }

    @Override
    public Command createCommand(ChannelSession channelSession, String command) throws IOException {
        String cmd  = command.replace("'","");
        File file = new File(CodeUntil.defaultPath() + cmd.split(" ")[1]);
        String repositoryPath = file.getAbsolutePath();

        logger.info("ssh clone repository address " + " "+ repositoryPath);

        try {
            Repository repo = Git.open(new File(repositoryPath)).getRepository();
            if (command.startsWith("git-upload-pack")) {
                UploadPack uploadPack = new UploadPack(repo);
                repo.close();
                return new GitUploadPackCommand(uploadPack);
            }else if (command.startsWith("git-receive-pack")){
                ReceivePack receivePack = new ReceivePack(repo);
                repo.close();
                return new GitReceivePackCommand(receivePack);
            }
        } catch (IOException e) {
           return null;
        }
        return null;
    }












}
