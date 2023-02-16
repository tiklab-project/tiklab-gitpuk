package net.tiklab.xcode.http;

import net.tiklab.xcode.authority.XcodeValidServer;
import org.eclipse.jgit.http.server.GitServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.*;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;

/**
 * http请求
 */

@WebServlet(name = "xcodeHttpServlet", urlPatterns = {"/xcode/*"},
        loadOnStartup = 1,
        initParams = {
                @WebInitParam(name = "base-path", value = "//"),
                @WebInitParam(name = "export-all", value = "true")
        })
public class XcodeHttpServlet extends GitServlet {


        private static final Logger logger = LoggerFactory.getLogger(XcodeHttpServlet.class);

        //拦截请求效验数据
        @Override
        public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
                boolean authorized = isAuthorized((HttpServletRequest) req);
                HttpServletResponse res1 = (HttpServletResponse) res;
                if (!authorized){
                        res1.setHeader("WWW-Authenticate", "Basic realm=\"XcodeHttpServlet\"");
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
        private XcodeValidServer validServer;

        private boolean isAuthorized(HttpServletRequest req) {
                String authHeader = req.getHeader("Authorization");
                if (authHeader != null && authHeader.startsWith("Basic ")) {
                        byte[] decode = Base64.getDecoder().decode(authHeader.substring(6));
                        String[] authTokens = new String(decode).split(":");
                        if (authTokens.length == 2) {
                                String username = authTokens[0];
                                String password = authTokens[1];
                                return validServer.validUserNamePassword(username,password,"1");
                        }
                }
                return false;
        }


}






































