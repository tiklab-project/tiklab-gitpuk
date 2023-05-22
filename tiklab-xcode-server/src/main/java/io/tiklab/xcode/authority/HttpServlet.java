package io.tiklab.xcode.authority;

import io.tiklab.user.user.model.User;
import io.tiklab.user.user.service.UserService;
import io.tiklab.xcode.repository.model.RecordCommit;
import io.tiklab.xcode.repository.model.RepositoryQuery;
import io.tiklab.xcode.repository.service.RecordCommitService;
import io.tiklab.xcode.repository.service.RepositoryServer;
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
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

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


        @Autowired
        private ValidUsrPwdServer validUsrPwdServer;

        @Autowired
        private RecordCommitService  commitService;

        @Autowired
        private RepositoryServer repositoryServer;

        @Autowired
        private UserService userService;


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



        private boolean isAuthorized(HttpServletRequest req) {
                String authHeader = req.getHeader("Authorization");
                if (authHeader != null && authHeader.startsWith("Basic ")) {
                        byte[] decode = Base64.getDecoder().decode(authHeader.substring(6));
                        String[] authTokens = new String(decode).split(":");
                        if (authTokens.length == 2) {
                                String username = authTokens[0];
                                String password = authTokens[1];
                                boolean result = validUsrPwdServer.validUserNamePassword(username, password, "1");
                                if (result){
                                        updateRepository(req.getRequestURI(),username);
                                }
                                return result;

                        }
                }
                return false;
        }

        /**
         * 修改提交记录
         */
        private void updateRepository(String requestURI,String userName){
                if (requestURI.endsWith("git-receive-pack")){
                        User user = userService.findUserByUsername(userName);

                        String[] split = requestURI.split("/");
                        String repositoryName=split[2];
                        String name = repositoryName.substring(0, repositoryName.lastIndexOf(".git"));
                        List<io.tiklab.xcode.repository.model.Repository> repositoryList = repositoryServer.findRepositoryList(new RepositoryQuery().setName(name).setUserId(user.getId()));

                        RecordCommit recordCommit = new RecordCommit();
                        recordCommit.setRepository(repositoryList.get(0));
                        recordCommit.setCommitTime(new Timestamp(System.currentTimeMillis()));
                        recordCommit.setUserId(user.getId());
                        commitService.createRecordCommit(recordCommit);
                }
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






































