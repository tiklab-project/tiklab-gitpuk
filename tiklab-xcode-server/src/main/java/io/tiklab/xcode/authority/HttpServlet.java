package io.tiklab.xcode.authority;

import io.tiklab.xcode.util.RepositoryUtil;
import io.tiklab.core.exception.ApplicationException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.errors.RepositoryNotFoundException;
import org.eclipse.jgit.http.server.GitServlet;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.ReceivePack;
import org.eclipse.jgit.transport.ServiceMayNotContinueException;
import org.eclipse.jgit.transport.UploadPack;
import org.eclipse.jgit.transport.resolver.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Enumeration;

/**
 * 拦截git http请求
 */

@WebServlet(name = "xcodeHttpServlet", urlPatterns = {"/xcode/*"},
        loadOnStartup = 1,
        initParams = {
                @WebInitParam(name = "base-path", value = "//"),
                @WebInitParam(name = "export-all", value = "true")
        })
public class HttpServlet extends GitServlet {


        private static final Logger logger = LoggerFactory.getLogger(HttpServlet.class);

        //拦截请求效验数据
        @Override
        public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
                HttpServletResponse res1 = (HttpServletResponse) res;
                boolean authorized = isAuthorized((HttpServletRequest) req);

                if (!authorized){
                        res1.setHeader("WWW-Authenticate", "Basic realm=\"HttpServlet\"");
                        res1.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
                super.service(req, res);
        }

        @Override
        public void init(ServletConfig config) throws ServletException {
                setRepositoryResolver(new HttpRepositoryResolver());
                setUploadPackFactory(new HttpUploadPackFactory());
                setReceivePackFactory(new HttpReceivePackFactory());
                super.init(config);
        }

        @Autowired
        private ValidUsrPwdServer validUsrPwdServer;

        private boolean isAuthorized(HttpServletRequest req) {
                String authHeader = req.getHeader("Authorization");
                if (authHeader != null && authHeader.startsWith("Basic ")) {
                        byte[] decode = Base64.getDecoder().decode(authHeader.substring(6));
                        String[] authTokens = new String(decode).split(":");
                        if (authTokens.length == 2) {
                                String username = authTokens[0];
                                String password = authTokens[1];
                                return validUsrPwdServer.validUserNamePassword(username,password,"1");
                        }
                }
                return false;
        }

        /**
         * 处理仓库地址
         */
        private static class HttpRepositoryResolver implements RepositoryResolver<HttpServletRequest> {
                @Override
                public Repository open(HttpServletRequest req, String name)
                        throws RepositoryNotFoundException, ServiceNotAuthorizedException,
                        ServiceNotEnabledException, ServiceMayNotContinueException {

                        String s = RepositoryUtil.defaultPath();
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

        /**
         * 实现git-upload-pack钩子
         */
        private static class HttpUploadPackFactory implements UploadPackFactory<HttpServletRequest> {
                @Override
                public UploadPack create(HttpServletRequest req, Repository db)
                        throws ServiceNotEnabledException, ServiceNotAuthorizedException {
                        return new UploadPack(db);
                }
        }

        /**
         * 实现git-receive-pack钩子
         */
        private static class HttpReceivePackFactory implements ReceivePackFactory<HttpServletRequest> {
                @Override
                public ReceivePack create(HttpServletRequest req, Repository db)
                        throws ServiceNotEnabledException, ServiceNotAuthorizedException {
                        return new ReceivePack(db);
                }
        }

}






































