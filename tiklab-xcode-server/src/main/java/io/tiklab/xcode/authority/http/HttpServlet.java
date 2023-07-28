package io.tiklab.xcode.authority.http;

import io.tiklab.user.user.model.User;
import io.tiklab.user.user.service.UserService;
import io.tiklab.xcode.authority.ValidUsrPwdServer;
import io.tiklab.xcode.repository.model.RecordCommit;
import io.tiklab.xcode.repository.model.Repository;
import io.tiklab.xcode.repository.service.MemoryManService;
import io.tiklab.xcode.repository.service.RecordCommitService;
import io.tiklab.xcode.repository.service.RepositoryServer;
import org.eclipse.jgit.http.server.GitServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;

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

        @Resource
        MemoryManService memoryManService;

        //拦截请求效验数据
        @Override
        public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
                HttpServletResponse res1 = (HttpServletResponse) res;
                boolean authorized = isAuthorized((HttpServletRequest) req);
                String requestURI = ((HttpServletRequest) req).getRequestURI();

                if (!authorized){
                        res1.setHeader("WWW-Authenticate", "Basic realm=\"HttpServlet\"");
                        res1.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }

                //查询是否还有剩余内存
                boolean resMemory = memoryManService.findResMemory();
                if (!resMemory){
                        logger.warn("存储空间不足");
                        res1.setHeader("Content-Type", "text/plain");
                        res1.setStatus(201);
                        res1.getWriter().write("pack exceeds maximum allowed size");
                        return;
                }

                /*//仓库上传权限
                if (requestURI.endsWith("git-receive-pack")){
                        String[] split = requestURI.split("/");
                        String groupName = split[2];
                        String name = split[3].substring(0,split[3].indexOf(".git"));

                        Repository repository = repositoryServer.findRepositoryByAddress(groupName + "/" + name);

                        if(repository.getState()==2){
                                res1.setHeader("Content-Type", "text/plain");
                                res1.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                res1.getWriter().write("You are not allowed to push code to this project");
                                return;
                        }
                }*/
                super.service(req, res);

        }

        @Override
        public void init(ServletConfig config) throws ServletException {
                setRepositoryResolver(new HttpRepositoryResolver(repositoryServer));
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
                                        commitService.updateCommitRecord(req.getRequestURI(),username);
                                }
                                return result;

                        }
                }
                return false;
        }

}






































