package io.tiklab.gitpuk.authority.http;

import io.tiklab.gitpuk.repository.service.RepositoryService;
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
    private RepositoryService repositoryServer;

    @Override
    public ReceivePack create(HttpServletRequest httpRequest, Repository db)
            throws ServiceNotEnabledException, ServiceNotAuthorizedException {

        ReceivePack receivePack = new ReceivePack(db);

        return  receivePack ;
    }
}