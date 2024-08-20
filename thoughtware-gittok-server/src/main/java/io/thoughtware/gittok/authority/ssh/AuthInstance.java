package io.thoughtware.gittok.authority.ssh;

import io.thoughtware.gittok.setting.service.AuthSshServer;
import org.apache.sshd.server.auth.pubkey.PublickeyAuthenticator;

public  interface AuthInstance<t>  {

    PublickeyAuthenticator getPublicKeyValid(AuthSshServer authSShServer);
}
