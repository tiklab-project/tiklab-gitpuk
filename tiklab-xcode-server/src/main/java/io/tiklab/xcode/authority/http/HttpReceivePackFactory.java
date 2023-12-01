package io.tiklab.xcode.authority.http;

import io.tiklab.xcode.repository.service.RepositoryServer;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.ReceivePack;
import org.eclipse.jgit.transport.resolver.ReceivePackFactory;
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StreamUtils;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.InputStream;

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