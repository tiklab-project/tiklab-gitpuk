package net.tiklab.xcode.ssh;


import net.tiklab.core.exception.ApplicationException;
import net.tiklab.xcode.authority.XcodeValidServer;
import net.tiklab.xcode.until.CodeFinal;
import org.apache.sshd.common.keyprovider.FileKeyPairProvider;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;


/**
 * 使用sshd效验ssh连接
 */

@Configuration
public class XcodeSshServer {

    @Autowired
    XcodeValidServer validServer;

    @Value("${xcode.ssh.port:8082}")
    private int sshPort;

    private static final Logger logger = LoggerFactory.getLogger(XcodeSshServer.class);

    @Bean
    public  void sshAuthority()  {
        SshServer sshServer = SshServer.setUpDefaultServer();
        sshServer.setPort(sshPort);
        sshServer.setHost (CodeFinal.SSH_HOST);
        Path my_ssh_key = Paths.get("D:\\Idea-22-3\\tiklab\\tiklab-xcode\\tiklab-xcode-starter\\src\\main\\resources\\key\\id_rsa");
        FileKeyPairProvider keyProvider = new FileKeyPairProvider(my_ssh_key);
        sshServer.setKeyPairProvider(keyProvider);

        // XcodePublicKeyAuthenticator publicKeyAuthenticator = new XcodePublicKeyAuthenticator();

        // boolean result = publicKeyAuthenticator.isResult();

        //效验账户名密码
        PasswordAuthenticator passwordAuthenticator =
                (username, password, session) -> validServer.validUserNamePassword(username,password,"1");

        sshServer.setPasswordAuthenticator(passwordAuthenticator);

        // sshServer.setPublickeyAuthenticator(publicKeyAuthenticator);


        try {
            sshServer.start();
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
        sshServer.setCommandFactory(new XcodeSshCommandFactory());
    }




}






























