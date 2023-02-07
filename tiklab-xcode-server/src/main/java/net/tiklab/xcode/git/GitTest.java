package net.tiklab.xcode.git;

import net.tiklab.xcode.authority.CodeShellCommand;
import net.tiklab.xcode.authority.PublicKeyAuth;
import net.tiklab.xcode.authority.SshCommandFactory;
import org.apache.sshd.common.session.helpers.AbstractSession;
import org.apache.sshd.server.ServerFactoryManager;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.command.CommandFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerProxyAcceptor;
import org.apache.sshd.server.session.SessionFactory;
import org.apache.sshd.server.shell.ShellFactory;


import java.io.*;
import java.net.*;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.util.List;
import java.util.Map;

public class GitTest {




    public static void main(String[] args) throws IOException, InterruptedException {

        SshServer sshServer = SshServer.setUpDefaultServer();
        sshServer.setPort(8083);
        sshServer.setHost ("0.0.0.0");
        sshServer.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(Paths.get("C:\\Users\\admin\\.ssh\\id_rsa")));
        sshServer.setPasswordAuthenticator((username, password, session) -> true);

        sshServer.start();

        sshServer.setCommandFactory(new SshCommandFactory());
        sshServer.setShellFactory(new CodeShellCommand());


        Thread.sleep(2000000);

    }


























}








































