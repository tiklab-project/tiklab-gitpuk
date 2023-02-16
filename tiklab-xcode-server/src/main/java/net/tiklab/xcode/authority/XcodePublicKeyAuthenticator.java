package net.tiklab.xcode.authority;

import org.apache.sshd.server.auth.AsyncAuthException;
import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;
import org.apache.sshd.server.session.ServerSession;
import java.security.PublicKey;
import java.util.Base64;

public class XcodePublicKeyAuthenticator implements PublickeyAuthenticator {

    private boolean result = false;

    @Override
    public boolean authenticate(String username, PublicKey key, ServerSession session) throws AsyncAuthException {

        String algorithm = key.getAlgorithm();
        String format = key.getFormat();
        byte[] publicKeyBytes = key.getEncoded();
        String publicKeyString = Base64.getEncoder().encodeToString(publicKeyBytes);
        System.out.println("解密后数据：\n" + publicKeyString);
        if (publicKeyString == null){
            result = false;
        }
        return result;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
