package io.thoughtware.gittork.authority.http;

import io.thoughtware.gittork.repository.service.RepositoryServer;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.ReceivePack;
import org.eclipse.jgit.transport.resolver.ReceivePackFactory;
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 实现git-receive-pack钩子
 */
public   class HttpReceivePackFactory implements ReceivePackFactory<HttpServletRequest> {

    @Resource
    private RepositoryServer repositoryServer;

    @Override
    public ReceivePack create(HttpServletRequest req, Repository db)
            throws ServiceNotEnabledException, ServiceNotAuthorizedException {

        ReceivePack receivePack = new ReceivePack(db);
        return  receivePack ;
    }
}