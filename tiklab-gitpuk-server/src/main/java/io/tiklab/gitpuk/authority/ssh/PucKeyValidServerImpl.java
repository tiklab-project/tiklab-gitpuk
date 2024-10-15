package io.tiklab.gitpuk.authority.ssh;

import io.tiklab.gitpuk.setting.model.AuthSsh;
import io.tiklab.gitpuk.setting.service.AuthSshServer;
import io.tiklab.gitpuk.common.RepositoryFinal;
import org.apache.commons.lang.StringUtils;
import org.apache.sshd.server.auth.AsyncAuthException;
import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;
import org.apache.sshd.server.session.ServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;

import java.security.PublicKey;

import java.security.interfaces.RSAPublicKey;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Configuration
public class PucKeyValidServerImpl implements PublickeyAuthenticator {
    private static Logger logger = LoggerFactory.getLogger(PucKeyValidServerImpl.class);

    public final AuthSshServer authSshServer;

    public PucKeyValidServerImpl(AuthSshServer authSshServer) {
        this.authSshServer = authSshServer;
    }

    @Override
    public boolean authenticate(String username, PublicKey key, ServerSession session) throws AsyncAuthException {

        String algorithm = key.getAlgorithm();

        if (algorithm.equals(RepositoryFinal.SSH_ENCODER_RSA)){
            List<AuthSsh> userAuthSsh = authSshServer.findAllAuthSsh();
            if (userAuthSsh.isEmpty()){
                return false;
            }
            RSAPublicKey rsaPublicKey = (RSAPublicKey) key;
            BigInteger modulus = rsaPublicKey.getModulus();
            List<AuthSsh> authSshes = userAuthSsh.stream().filter(a -> modulus.toString().equals(a.getModulus())).collect(Collectors.toList());
           if (CollectionUtils.isEmpty(authSshes)){
               return false;
           }
            AuthSsh auth = authSshes.get(0);
            if (!("0").equals(auth.getExpireTime())&& StringUtils.isNotEmpty(auth.getExpireTime())){
                try {
                    //过期了
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date  parse = simpleDateFormat.parse(auth.getExpireTime());
                    //存的日期为 当天00:00  因此加一天
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(parse);
                    calendar.add(Calendar.DATE, 1);
                    Date time = calendar.getTime();

                    if (time.getTime()<System.currentTimeMillis()){
                        logger.info("私钥过期了");
                        return false;
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            String authMode = auth.getModulus();
            //认证成功
            if (modulus.toString().equals(authMode)){
                //更新使用时间
                auth.setUserTime(new Timestamp(System.currentTimeMillis()));
                authSshServer.updateAuthSsh(auth);

                //向session会话中添加 提交用户信息
                session.setAttribute(AttributeKeys.USERNAME_KEY, auth.getUser().getId());

               /* final AttributeKey<String> ACTUAL_USERNAME_KEY = AttributeKey.valueOf("actualUsername");
                session.setAttribute(ACTUAL_USERNAME_KEY, "123");*/
                return true;
            }
        }
        logger.info("私钥不正确");
        return false;
    }
}
