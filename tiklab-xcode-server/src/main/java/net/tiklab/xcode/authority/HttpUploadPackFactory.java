package net.tiklab.xcode.authority;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.UploadPack;
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;
import org.eclipse.jgit.transport.resolver.UploadPackFactory;
import javax.servlet.http.HttpServletRequest;

/**
 * http 拉取实现
 */

public class HttpUploadPackFactory implements UploadPackFactory<HttpServletRequest> {

    @Override
    public UploadPack create(HttpServletRequest req, Repository db) throws ServiceNotEnabledException, ServiceNotAuthorizedException {
        return new UploadPack(db);
    }
}
