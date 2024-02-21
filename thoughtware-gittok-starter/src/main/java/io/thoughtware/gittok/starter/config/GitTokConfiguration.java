package io.thoughtware.gittok.starter.config;

import io.thoughtware.dsm.boot.starter.annotation.EnableDsm;
import io.thoughtware.gittok.EnableGitTokServer;
import io.thoughtware.dal.boot.starter.annotation.EnableDal;
import io.thoughtware.dcs.boot.starter.annotation.EnableDcsClient;
import io.thoughtware.dcs.boot.starter.annotation.EnableDcsServer;
import io.thoughtware.eam.boot.starter.annotation.EnableEamClient;
import io.thoughtware.eam.boot.starter.annotation.EnableEamServer;
import io.thoughtware.gateway.boot.starter.annotation.EnableGateway;
import io.thoughtware.licence.boot.starter.annotation.EnableLicenceServer;
import io.thoughtware.messsage.boot.starter.annotation.EnableMessageServer;
import io.thoughtware.openapi.boot.starter.annotation.EnableOpenApi;
import io.thoughtware.plugin.starter.EnablePluginServer;
import io.thoughtware.postgresql.EnablePostgresql;
import io.thoughtware.privilege.boot.starter.annotation.EnablePrivilegeServer;
import io.thoughtware.rpc.boot.starter.annotation.EnableRpc;
import io.thoughtware.security.boot.stater.annotation.EnableSecurityServer;
import io.thoughtware.toolkit.boot.starter.annotation.EnableToolkit;
import io.thoughtware.user.boot.starter.annotation.EnableUserClient;
import io.thoughtware.user.boot.starter.annotation.EnableUserServer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
//common
@EnableToolkit
@EnablePostgresql
@EnableDal
@EnableDsm
@EnableDcsServer
@EnableDcsClient
@EnableOpenApi
@EnableRpc


@EnablePluginServer

//eam
@EnableEamServer
@EnableEamClient
@EnableLicenceServer

@EnableSecurityServer
@EnableUserServer
@EnableUserClient
@EnableMessageServer

@EnableGateway
@EnablePrivilegeServer
@EnableGitTokServer
@ComponentScan(value = "io.thoughtware.gittok")
public class GitTokConfiguration {
}
