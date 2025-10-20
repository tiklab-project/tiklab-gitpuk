package io.tiklab.gitpuk.starter.config;

import io.tiklab.eam.author.Authenticator;

import io.tiklab.eam.client.author.handler.DefaultAuthorHandler;
import io.tiklab.gateway.config.GatewayConfig;
import io.tiklab.gateway.config.IgnoreConfig;
import io.tiklab.gateway.config.IgnoreConfigBuilder;
import io.tiklab.gateway.handler.author.AuthorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GitPukGatewayAutoConfiguration {


    @Bean
    GatewayConfig gatewayConfig(IgnoreConfig ignoreConfig){
        GatewayConfig gatewayConfig = new GatewayConfig();
        gatewayConfig.setIgnoreConfig(ignoreConfig);

        return gatewayConfig;
    }


    @Bean
    public IgnoreConfig ignoreConfig(){
        return IgnoreConfigBuilder.instance()
                .ignoreTypes(new String[]{
                        ".ico",
                        ".jpg",
                        ".jpeg",
                        ".png",
                        ".gif",
                        ".html",
                        ".js",
                        ".css",
                        ".json",
                        ".xml",
                        ".ftl",
                        ".map",
                        ".gz",
                        "svg",
                        ".txt"
                })
                .ignoreUrls(new String[]{
                        "/",
                        "/eam/auth/login",
                        "/eam/auth/logout",
                        "/eam/auth/valid",
                        "/auth/valid",
                        "/document/view",
                        "/comment/view",
                        "/share/verifyAuthCode",
                        "/share/judgeAuthCode",
                        "/user/user/findAllUser",
                        "/user/orga/findAllOrga",
                        "/userOrga/findAllUserOrga",
                        "/dingding/passport/login",
                        "/user/dingdingcfg/findId",
                        "/dingding/passport/logout",
                        "/dingding/passport/valid",
                        "/user/wechatcfg/findWechatById",
                        "/wechat/passport/login",
                        "/wechat/passport/logout",
                        "/wechat/passport/internallogin",
                        "/wechat/passport/internalacclogin",
                        "/ldap/passport/login",
                        "/ldap/passport/logout",
                        "/version/getVersion",
                        "/licence/import",
                        "/wechatCallback/instruct",
                        "/alterSql/updateId",
                        "/gui",
                        "/gitignoreFile/createGitignoreFile",

                        "/message/messageItem/syncUpdateMessage",
                        "/message/messageItem/syncDeleteMessage",
                        "/permission/findPermissions",
                        "/RepWeb/createRepWebHook",
                        "/init/install/findStatus",
                        "/state/apply/findApply",
                        "/openapi/doc",
                        "/user/ldap/common/cfg/findLdapCfg",
                        "/eam/ldap/passport/login"

                })
                .ignorePreUrls(new String[]{
                        "/service",
                        "/apis/list",
                        "/apis/detail",
                        "/file",
                        "/plugin",
                        "/authConfig",
                        "/ws",
                        "/socket",
                        "/start",
                        "/eas",
                        "/gittok",
                        "/xcode",
                        "/backups",
                        "/remoteInfo",
                        "/toLead",
                        "/tag",
                        "/codeScan",
                        "/branch",
                        "/message",
                        "/repositoryClean",
                        "/lfs",
                        "/repositoryFile"
                })
                .get();
    }
}
