package io.thoughtware.gittok.authority.http;

import io.thoughtware.core.exception.ApplicationException;
import io.thoughtware.gittok.repository.service.RepositoryService;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.ReceivePack;
import org.eclipse.jgit.transport.resolver.ReceivePackFactory;
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Map;

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