package net.tiklab.xcode.authority;

import org.eclipse.jgit.http.server.GitServlet;
import org.eclipse.jgit.http.server.glue.ErrorServlet;
import org.eclipse.jgit.transport.http.HttpConnection;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Enumeration;

/**
 * http请求
 */

@WebServlet(name = "xcode httpServlet", urlPatterns = {"/xcode/*"},
        loadOnStartup = 1,
        initParams = {
                @WebInitParam(name = "base-path", value = "//"),
                @WebInitParam(name = "export-all", value = "true")
        })
public class GitHttpServlet extends GitServlet {

        @Override
        public void init(ServletConfig config) throws ServletException {
                setRepositoryResolver(new HttpRepositoryResolver());
                setUploadPackFactory(new HttpUploadPackFactory());
                setReceivePackFactory(new HttpReceivePackFactory());
                setUploadPackErrorHandler(new HttpUploadPackErrorHandler());
                setReceivePackErrorHandler(new HttpReceivePackErrorHandler());
                super.init(config);
        }

}






































