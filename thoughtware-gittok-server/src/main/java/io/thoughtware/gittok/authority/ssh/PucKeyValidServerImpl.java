package io.thoughtware.gittok.authority.ssh;

import io.thoughtware.eam.common.context.LoginContext;
import io.thoughtware.gittok.common.RepositoryFinal;
import io.thoughtware.gittok.common.RepositoryUtil;
import io.thoughtware.gittok.setting.model.AuthSsh;
import io.thoughtware.gittok.setting.model.AuthSshQuery;
import io.thoughtware.gittok.setting.service.AuthSshServer;
import org.apache.commons.lang.StringUtils;
import org.apache.sshd.server.auth.AsyncAuthException;
import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;
import org.apache.sshd.server.session.ServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.math.BigInteger;

import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.EdECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
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
                return true;
            }
        }
        return false;
    }
}
