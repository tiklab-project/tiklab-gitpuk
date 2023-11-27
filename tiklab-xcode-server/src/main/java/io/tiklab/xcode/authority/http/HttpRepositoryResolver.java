package io.tiklab.xcode.authority.http;

import io.tiklab.core.exception.ApplicationException;
import io.tiklab.xcode.repository.service.RepositoryServer;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.ServiceMayNotContinueException;
import org.eclipse.jgit.transport.resolver.RepositoryResolver;
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;


import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

/**
 * 处理仓库地址
 */
public  class HttpRepositoryResolver implements RepositoryResolver<HttpServletRequest> {

   private RepositoryServer repositoryServer;

    public HttpRepositoryResolver(RepositoryServer repositoryServer){
        this.repositoryServer= repositoryServer;
    }

    @Override
    public Repository open(HttpServletRequest req, String name)
            throws RepositoryNotFoundException, ServiceNotAuthorizedException,
            ServiceNotEnabledException, ServiceMayNotContinueException {

        String address = name.substring(0, name.indexOf(".git"));

        String absolutePath = repositoryServer.findRepositoryAp(address);

        File file = new File(absolutePath);
        if (!file.exists()){
            throw new ApplicationException("仓库不存在！");
        }

        Repository repository;
        try {
            repository = Git.open(file).getRepository();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return repository;
    }
}
