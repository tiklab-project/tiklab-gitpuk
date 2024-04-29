package io.thoughtware.gittok.authority.http;

import io.thoughtware.gittok.authority.ValidUsrPwdServer;
import io.thoughtware.gittok.branch.model.Branch;
import io.thoughtware.gittok.branch.model.BranchQuery;
import io.thoughtware.gittok.common.GitTokYamlDataMaService;
import io.thoughtware.gittok.common.git.GitBranchUntil;
import io.thoughtware.gittok.repository.model.Repository;
import io.thoughtware.gittok.repository.service.MemoryManService;
import io.thoughtware.gittok.repository.service.RecordCommitService;
import io.thoughtware.gittok.repository.service.RepositoryService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.http.server.GitServlet;

import org.eclipse.jgit.transport.ReceivePack;
import org.eclipse.jgit.transport.resolver.RepositoryResolver;
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;


import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
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
        private RecordCommitService commitService;

        @Autowired
        private RepositoryService repositoryServer;

        @Resource
        MemoryManService memoryManService;

        @Autowired
        GitTokYamlDataMaService yamlDataMaService;

        //拦截请求效验数据
        @Override
        public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
            // 将ServletRequest对象转换为HttpServletRequest对象
            HttpServletRequest request = (HttpServletRequest) req;
            // 获取上传的分支名
            String branch = request.getParameter("branch");

            HttpServletResponse res1 = (HttpServletResponse) res;
            boolean authorized = isAuthorized((HttpServletRequest) req);
            String requestURI = ((HttpServletRequest) req).getRequestURI();
            logger.info("推送代码请求" + requestURI);

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

            // 获取请求的方法
            String method = ((HttpServletRequest) req).getMethod();

            //git push提交 （客户端第三次请求发送数据 以git-receive-pack结尾）
            if (("POST").equals(method)&&requestURI.endsWith("git-receive-pack")){
                String beforeLast = StringUtils.substringBeforeLast(requestURI, "/");
                String repositoryPath = StringUtils.substringAfterLast(beforeLast, "xcode/").replace(".git", "");
                repositoryServer.compileRepository(repositoryPath);
            }
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
                    //校验用户信息
                     boolean result = validUsrPwdServer.validUserNamePassword(username, password, "1");
                     if (result){
                          //请求路径以git-receive-pack 结尾为提交
                          if (req.getRequestURI().endsWith("git-receive-pack")){
                           //修改提交时间和创建提交记录
                          commitService.updateCommitRecord(req.getRequestURI(),username);

                          }
                     }
                     return result;
                }
        }
        return false;
   }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("执行了dopost");
        super.doPost(req, resp);
    }
}






































