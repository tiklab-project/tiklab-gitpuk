package io.tiklab.gitpuk.authority.ssh;

import io.tiklab.gitpuk.setting.service.AuthSshServer;
import org.springframework.stereotype.Service;

@Service
public class AuthInstanceImpl implements  AuthInstance {


    public PucKeyValidServerImpl getPublicKeyValid(AuthSshServer authSShServer) {
        return  new PucKeyValidServerImpl(authSShServer);
    }
}
