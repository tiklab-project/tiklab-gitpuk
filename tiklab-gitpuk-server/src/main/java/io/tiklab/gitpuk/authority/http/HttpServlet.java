package io.tiklab.gitpuk.authority.http;

import io.tiklab.gitpuk.authority.lfs.LfsAuthService;
import io.tiklab.gitpuk.authority.ValidUsrPwdServer;
import io.tiklab.gitpuk.authority.utils.ReturnResponse;
import io.tiklab.gitpuk.repository.model.Repository;
import io.tiklab.gitpuk.repository.model.RepositoryQuery;
import io.tiklab.gitpuk.repository.service.MemoryManService;
import io.tiklab.gitpuk.repository.service.RecordCommitService;
import io.tiklab.gitpuk.repository.service.RepositoryPushRule;
import io.tiklab.gitpuk.repository.service.RepositoryService;
import io.tiklab.user.user.model.User;
import io.tiklab.user.user.service.UserProcessor;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.http.server.GitServlet;

import org.eclipse.jgit.transport.ReceivePack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;


import javax.servlet.*;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

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

        @Autowired
        private RepositoryPushRule repositoryPushRule;

        @Autowired
        private MemoryManService memoryManService;


        @Autowired
        private UserProcessor userProcessor;

        @Autowired
        private LfsAuthService httpLfsAuthService;



        //拦截请求效验数据

        public void service(ServletRequest req, ServletResponse res) {

            try {
                String commitMessage = req.getParameter("message");
                // 将ServletRequest对象转换为HttpServletRequest对象
                HttpServletRequest request = (HttpServletRequest) req;
                String requestURI = request.getRequestURI();
                logger.info("代码请求" + requestURI);
                HttpServletResponse response = (HttpServletResponse) res;

                //认证用户信息
                boolean authorized=false;
                String username=null;
                String authHeader = request.getHeader("Authorization");
                Enumeration<String> headerNames = request.getHeaderNames();
                if (authHeader != null && authHeader.startsWith("Basic ")) {
                    byte[] decode = Base64.getDecoder().decode(authHeader.substring(6));
                    String[] authTokens = new String(decode).split(":");
                    if (authTokens.length == 2) {
                        username = authTokens[0];
                        String password = authTokens[1];
                        //校验用户信息
                        authorized = validUsrPwdServer.validUserNamePassword(username, password, "1");
                    }
                }
                //认证失败
                if (!authorized){
                    logger.info("认证失败");
                    ReturnResponse.authFailure(response);
                    return;
                }


                //查询是否还有剩余内存
                boolean resMemory = memoryManService.findResMemory();
                if (!resMemory){
                    logger.info("存储空间不足");
                    ReturnResponse.serverNotMemory(response);
                    return;
                }


                if (requestURI.endsWith("info/refs")){
                    //查询仓库
                    Repository  repository = findRepository(request.getRequestURI());

                    boolean privilege = validUsrPwdServer.validUserPrivilege(username, repository.getRpyId());
                    if (!privilege){
                        ReturnResponse.authPrivilege(response);
                        return;
                    }
                }


                //仓库上传权限
                if (requestURI.endsWith("git-receive-pack")){
                    String[] split = requestURI.split("/");
                    String groupName = split[2];
                    String name = split[3].substring(0,split[3].indexOf(".git"));

                    Repository repository = repositoryServer.findRepositoryByAddress(groupName + "/" + name);

                    if(repository.getState()==2){
                        response.setHeader("Content-Type", "text/plain");
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.getWriter().write("You are not allowed to push code to this project");
                        return;
                    }


                }

                //处理lfs
                if (requestURI.endsWith("/info/lfs/objects/batch")) {
                    boolean result = httpLfsAuthService.HttpLfsAnalysis(request, response);
                    if (!result){
                        return;
                    }
                }else {
                    super.service(req, res);
                }

                //推送成功后 编辑提交记录
                compileCommit(request,username);
            } catch (Exception e) {
                e.printStackTrace();
                HttpServletResponse response = (HttpServletResponse) res;
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }

        }



        @Override
        public void init(ServletConfig config) throws ServletException {
                setRepositoryResolver(new HttpRepositoryResolver(repositoryServer));
                setUploadPackFactory(new HttpUploadPackFactory());
                setReceivePackFactory(new HttpReceivePackFactory(repositoryPushRule));
                super.init(config);
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            super.doPost(req, resp);
        }

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse rsp) throws ServletException, IOException {
            super.doGet(req, rsp);
        }



    /**
     * 推送后 编辑提交
     * @param request request
     * @param username 用户名称
     */
    public void compileCommit(HttpServletRequest request,String username) throws IOException {
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        if (("POST").equals(method)&&requestURI.endsWith("git-receive-pack")){
          if (!ObjectUtils.isEmpty(request.getHeader("accept-encoding"))){
              String beforeLast = StringUtils.substringBeforeLast(requestURI, "/");
              String repositoryPath = StringUtils.substringAfterLast(beforeLast, "xcode/").replace(".git", "");

              repositoryServer.compileRepository(repositoryPath);

              //添加推送记录
              User user = userProcessor.findUserByUsername(username,null);
              commitService.updateCommitRecord(requestURI,user.getId(),"http");
          }
        }
    }

    /**
     * 截取仓库地址查询仓库
     * @param requestURI 客户端地址
     */
    public Repository findRepository(String requestURI){
        String beforeLast = StringUtils.substringBeforeLast(requestURI, "/");
        String afterLast = StringUtils.substringAfterLast(beforeLast, "xcode/");
        String repositoryPath=StringUtils.substringBeforeLast(afterLast,".git");
        //通过仓库地址查询仓库是否存在
        List<Repository> repositoryList = repositoryServer.findRepositoryList(new RepositoryQuery().setAddress(repositoryPath));
        return repositoryList.get(0);
    }

}
