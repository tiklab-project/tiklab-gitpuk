package io.tiklab.gitpuk.authority.ssh;

import io.tiklab.gitpuk.setting.service.AuthSshServer;
import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;

public  interface AuthInstance<t>  {

    PublickeyAuthenticator getPublicKeyValid(AuthSshServer authSShServer);
}
