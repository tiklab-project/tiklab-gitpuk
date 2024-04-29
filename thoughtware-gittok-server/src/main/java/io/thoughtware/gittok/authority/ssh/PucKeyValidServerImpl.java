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

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Configuration
public class PucKeyValidServerImpl implements PublickeyAuthenticator {
    private static Logger logger = LoggerFactory.getLogger(PucKeyValidServerImpl.class);

    private final AuthSshServer authSshServer;

    public PucKeyValidServerImpl(AuthSshServer authSshServer) {
        this.authSshServer = authSshServer;
    }

    @Override
    public boolean authenticate(String username, PublicKey key, ServerSession session) throws AsyncAuthException {
        String algorithm = key.getAlgorithm();
        if (algorithm.equals(RepositoryFinal.SSH_ENCODER_RSA)){
            List<AuthSsh> userAuthSsh = authSshServer.findAuthSshList(new AuthSshQuery().setUserId(LoginContext.getLoginId()));
            if (userAuthSsh.isEmpty()){
                return false;
            }

            for (AuthSsh auth : userAuthSsh) {
                if (!("0").equals(auth.getExpireTime())&& StringUtils.isNotEmpty(auth.getExpireTime())){
                    try {
                        //过期了
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Date  parse = simpleDateFormat.parse(auth.getExpireTime());
                        if (parse.getTime()<System.currentTimeMillis()){
                            continue;
                        }
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                String value = auth.getValue();
                //获取公钥的Base64编码数据
                String keyBase64 = findKeyBase64(value);
                if (keyBase64 == null){
                    continue;
                }
                //对公钥的Base64编码转换为pem
                PublicKey publicKey = ValidRsaKey(keyBase64);
                if (publicKey == null){
                    continue;
                }
                //认证成功
                if (publicKey.equals(key)){
                    //更新使用时间
                    auth.setUserTime(new Timestamp(System.currentTimeMillis()));
                    authSshServer.updateAuthSsh(auth);
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 获取公钥的Base64编码数据
     * @param key 公钥
     * @return 公钥
     */
    private static String findKeyBase64(String key){
        try {
            if (key.startsWith(RepositoryFinal.Key_TYPE_OPENSSH_RSA)){
                //截取ssh-rsa中的Base64编码数据
                int i = key.indexOf(" ");
                int i1 = key.lastIndexOf(" ");
                return key.substring(i+1,i1);
            }else if (key.startsWith(RepositoryFinal.Key_TYPE_SSH_RSA)){
                return null;
            }
        }catch (Exception e){
            return null;
        }
        return null;
    }

    /**
     * 保存的key
     * @param key 服务器存储的key
     * @return PublicKey对象
     */
    private static PublicKey ValidRsaKey(String key){
        try {
            byte[] decoded = Base64.getDecoder().decode(key);

            // 将字节数组包装到缓冲区中
            ByteBuffer byteBuffer = ByteBuffer.wrap(decoded);
            //自动更新的SIZEOF_INT的值
            AtomicInteger position = new AtomicInteger();

            String algorithm = readString(byteBuffer, position);
            //判断是否为 Key_TYPE_OPENSSH_RSA格式的公钥
            if (!RepositoryFinal.Key_TYPE_OPENSSH_RSA.equals(algorithm)){
                return null;
            }

            // 字符串转换成字节
            BigInteger publicExponent = readMpint(byteBuffer, position);
            BigInteger modulus = readMpint(byteBuffer, position);

            //字节转换成PublicKey公钥
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, publicExponent);
            KeyFactory kf = KeyFactory.getInstance(RepositoryFinal.SSH_ENCODER_RSA);
            return kf.generatePublic(keySpec);

        } catch (Exception e) {
            return null;
        }
    }

    //字节转换成大数
    private static BigInteger readMpint(ByteBuffer buffer, AtomicInteger pos){
        byte[] bytes = readBytes(buffer, pos);
        if(bytes.length == 0){
            return BigInteger.ZERO;
        }
        return new BigInteger(bytes);
    }

    //字节转换成字符
    private static String readString(ByteBuffer buffer, AtomicInteger pos){
        byte[] bytes = readBytes(buffer, pos);
        if(bytes.length == 0){
            return "";
        }
        return new String(bytes, StandardCharsets.US_ASCII);
    }

    //转换成字节
    private static byte[] readBytes(ByteBuffer buffer, AtomicInteger pos){
        int len = buffer.getInt(pos.get());
        byte[] buff = new byte[len];
        int SIZEOF_INT = 4;
        for(int i = 0; i < len; i++) {
            buff[i] = buffer.get(i + pos.get() + SIZEOF_INT);
        }
        pos.set(pos.get() + SIZEOF_INT + len);
        return buff;
    }


}
