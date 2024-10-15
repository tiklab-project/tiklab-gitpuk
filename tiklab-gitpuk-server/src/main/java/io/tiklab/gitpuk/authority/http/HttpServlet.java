package io.tiklab.gitpuk.authority.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.tiklab.gitpuk.authority.lfs.GitLfsAuth;
import io.tiklab.gitpuk.authority.ValidUsrPwdServer;
import io.tiklab.gitpuk.authority.request.LfsBatchRequest;
import io.tiklab.gitpuk.authority.request.LfsData;
import io.tiklab.gitpuk.authority.utils.ReturnResponse;
import io.tiklab.gitpuk.common.GitPukYamlDataMaService;
import io.tiklab.gitpuk.common.RepositoryUtil;
import io.tiklab.gitpuk.repository.model.Repository;
import io.tiklab.gitpuk.repository.service.MemoryManService;
import io.tiklab.gitpuk.repository.service.RecordCommitService;
import io.tiklab.gitpuk.repository.service.RepositoryService;
import io.tiklab.user.user.model.User;
import io.tiklab.user.user.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.http.server.GitServlet;

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
        private MemoryManService memoryManService;

        @Autowired
        private GitPukYamlDataMaService yamlDataMaService;

        @Autowired
        private UserService userService;

        //拦截请求效验数据

        public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
            // 将ServletRequest对象转换为HttpServletRequest对象
            HttpServletRequest request = (HttpServletRequest) req;
            String requestURI = request.getRequestURI();
            logger.info("代码请求" + requestURI);
            HttpServletResponse response = (HttpServletResponse) res;

            //认证用户信息
            boolean authorized=false;
            String username=null;
            String authHeader = request.getHeader("Authorization");
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

            /*//仓库上传权限
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
            }*/

            //处理lfs
            if (requestURI.endsWith("/info/lfs/objects/batch")) {
                lfsAnalysis(request,response);
            }else {
                super.service(req, res);
            }

            //推送成功后 编辑提交记录
            compileCommit(request,username);
        }

        @Override
        public void init(ServletConfig config) throws ServletException {
                setRepositoryResolver(new HttpRepositoryResolver(repositoryServer));
                setUploadPackFactory(new HttpUploadPackFactory());
                setReceivePackFactory(new HttpReceivePackFactory());
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
     * 解析lfs 数据
     * @param request request
     * @param response response
     */
    public void lfsAnalysis(HttpServletRequest request,HttpServletResponse response) throws IOException {
        String requestURI = request.getRequestURI();
        LfsData lfsData = new LfsData();

        //解析 客户端上传的数据
        LfsBatchRequest lfsBatchRequest = new ObjectMapper().readValue(request.getInputStream(), LfsBatchRequest.class);

        //上传的时候 判断lfs剩余的内存是否足够
        if ("upload".equalsIgnoreCase(lfsBatchRequest.getOperation())){
            boolean lfsStorage = memoryManService.isLfsStorage(request,lfsBatchRequest);
            if (!lfsStorage){
                ReturnResponse.lfsNotMemory(response);
                return;
            }
        }else {
            //拉取
            String address = StringUtils.substringBetween(requestURI, "xcode/", ".git");
            Repository repository = repositoryServer.findOnlyRpyByAddress(address);
            String rpyLfsPath = RepositoryUtil.getRpyLfsPath(yamlDataMaService.repositoryAddress(), repository.getRpyId());
            lfsData.setLfsPath(rpyLfsPath);
        }
        lfsData.setRequest(request);
        lfsData.setResponse(response);
        lfsData.setLfsBatchRequest(lfsBatchRequest);
        lfsData.setType("http");

        //解析lfs数据
        GitLfsAuth.HandleLfsBatch(lfsData);
    }


    /**
     * 推送后 编辑提交
     * @param request request
     * @param username 用户名称
     */
    public void compileCommit(HttpServletRequest request ,String username) throws IOException {
        String method = request.getMethod();
        String requestURI = request.getRequestURI();
        if (("POST").equals(method)&&requestURI.endsWith("git-receive-pack")){
          if (!ObjectUtils.isEmpty(request.getHeader("accept-encoding"))){
              String beforeLast = StringUtils.substringBeforeLast(requestURI, "/");
              String repositoryPath = StringUtils.substringAfterLast(beforeLast, "xcode/").replace(".git", "");
              repositoryServer.compileRepository(repositoryPath);

              //添加推送记录
              User user = userService.findUserByUsername(username,null);
              commitService.updateCommitRecord(requestURI,user.getId(),"http");
          }
        }
    }
}
