package io.thoughtware.gittok.authority.ssh;

import io.thoughtware.gittok.setting.service.AuthSshServer;
import org.springframework.stereotype.Service;

@Service
public class AuthInstanceImpl implements  AuthInstance {


    public PucKeyValidServerImpl getPublicKeyValid(AuthSshServer authSShServer) {
        return  new PucKeyValidServerImpl(authSShServer);
    }
}
