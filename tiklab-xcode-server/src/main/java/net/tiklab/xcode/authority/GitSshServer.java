package net.tiklab.xcode.authority;

import net.tiklab.xcode.code.model.Code;
import net.tiklab.xcode.code.service.CodeServer;
import net.tiklab.xcode.until.CodeFinal;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * 使用sshd效验ssh连接
 */

@Configuration
public class GitSshServer {

    @Autowired
    private  CodeServer codeServer;

    private static final Logger logger = LoggerFactory.getLogger(GitSshServer.class);

    @Bean
    public  void sshAuthority()  {
        SshServer sshServer = SshServer.setUpDefaultServer();
        sshServer.setPort(CodeFinal.SSH_PORT);
        sshServer.setHost (CodeFinal.SSH_HOST);
        SimpleGeneratorHostKeyProvider keyProvider = new SimpleGeneratorHostKeyProvider(Paths.get("C:\\Users\\admin\\.ssh\\id_rsa"));
        sshServer.setKeyPairProvider(keyProvider);
        sshServer.setPasswordAuthenticator((username, password, session) -> validUserNamePassword(username,password));
        try {
            sshServer.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sshServer.setCommandFactory(new SshCommandFactory());

    }

    //效验账户名密码
    private boolean validUserNamePassword(String username,String password){
        List<Code> allCode = codeServer.findAllCode();
        int size = allCode.size();
        logger.info("认证用户名为："+ username);
        logger.info("认证密码为："+ password);
        if (size > 0){
            logger.info("认证成功。。。");
        }
        return size > 0;
    }



}






























