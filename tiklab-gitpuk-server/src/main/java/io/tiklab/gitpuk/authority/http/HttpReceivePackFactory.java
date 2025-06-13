package io.tiklab.gitpuk.authority.http;

import io.tiklab.gitpuk.common.GitPukFinal;
import io.tiklab.gitpuk.repository.service.RepositoryPushRule;
import io.tiklab.gitpuk.repository.service.RepositoryService;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.ReceiveCommand;
import org.eclipse.jgit.transport.ReceivePack;
import org.eclipse.jgit.transport.ServiceMayNotContinueException;
import org.eclipse.jgit.transport.resolver.ReceivePackFactory;
import org.eclipse.jgit.transport.resolver.ServiceNotAuthorizedException;
import org.eclipse.jgit.transport.resolver.ServiceNotEnabledException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

/**
 * 实现git-receive-pack钩子
 */
@Component
public   class HttpReceivePackFactory implements ReceivePackFactory<HttpServletRequest> {

    private final RepositoryPushRule repositoryPushRule;
    public HttpReceivePackFactory(RepositoryPushRule repositoryPushRule) {
        this.repositoryPushRule = repositoryPushRule;
    }

    @Override
    public ReceivePack create(HttpServletRequest httpRequest, Repository repository)
            throws ServiceNotEnabledException, ServiceNotAuthorizedException {
     /*   String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring(6);
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            String[] values = credentials.split(":", 2);
            String username = values[0]; // 如: user@example.com
            // String password = values[1]; // 通常不需要密码
        }*/
        String requestURI = httpRequest.getRequestURI();
        ReceivePack receivePack = new ReceivePack(repository);
        if (requestURI.endsWith("git-receive-pack")){

            String substringAfter = StringUtils.substringAfter( requestURI, GitPukFinal.REP_PATH_PREFIX);
            //  仓库组/仓库名。获取仓库
            String repAddress = StringUtils.substringBefore(substringAfter,".git");

            receivePack.setPreReceiveHook((receivePack1, commands) -> {

                for (ReceiveCommand cmd : commands) {
                    //校验提交信息
                    repositoryPushRule.pushRuleVerify(receivePack1,cmd, repAddress);
                }
            });
        }

        return  receivePack ;
    }

}