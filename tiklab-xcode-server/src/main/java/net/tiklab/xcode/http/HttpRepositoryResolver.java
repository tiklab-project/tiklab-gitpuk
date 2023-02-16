package net.tiklab.xcode.http;

import net.tiklab.core.exception.ApplicationException;
import net.tiklab.xcode.until.CodeFinal;
import net.tiklab.xcode.until.CodeUntil;
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
import java.util.Base64;

/**
 * 处理仓库地址
 */

public class HttpRepositoryResolver implements RepositoryResolver<HttpServletRequest> {


    @Override
    public Repository open(HttpServletRequest req, String name) throws RepositoryNotFoundException, ServiceNotAuthorizedException, ServiceNotEnabledException, ServiceMayNotContinueException {

        String s = CodeUntil.defaultPath();
        File file = new File(s + "/" + name);

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
































