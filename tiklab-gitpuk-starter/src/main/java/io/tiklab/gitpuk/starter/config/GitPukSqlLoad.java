package io.tiklab.gitpuk.starter.config;


import io.tiklab.dsm.config.model.DsmConfig;
import io.tiklab.dsm.support.DsmConfigBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class GitPukSqlLoad {

    @Bean
    DsmConfig initDsmConfig() {
        DsmConfig dsmConfig = DsmConfigBuilder.instance();
        dsmConfig.newVersion("1.0.0", new String[]{
                //PrivilegeDsm
                "privilege_1.0.0",
                //UserDsm
                "user_1.0.0",
                "userCe_1.0.0",
                //IntegrationDsm
                "tool_1.0.0",
                //LicenceDsm
                "app-authorization_1.0.0",
                //MessageDsm
                "message_1.0.0",
                //SecurityDsm
                "oplog_1.0.0",
                //TodoTaskDsm
                "todotask_1.0.0",
                //xcode
                "xcode_1.0.0",
                "scan_1.0.0",
                "xdprivilege_1.0.0",
                "backups_1.0.0",
                "privilege-gittok_1.0.0",
                "gitpuk_1.0.0",
        });

        dsmConfig.newVersion("1.0.1", new String[]{
                "xcode_1.0.1",
                "oplog_1.0.1",
                "message_1.0.1",
                "todotask_1.0.1",
                "xdprivilege_1.0.1",
                "apply-auth_1.0.1",
                "privilege_1.0.1",
                "privilege-gittok_1.0.1",
        });
        dsmConfig.newVersion("1.0.2", new String[]{
                "message_1.0.2",
                "oplog_1.0.2",
                "todotask_1.0.2",
                "apply-auth_1.0.2",
                "xcode_1.0.2",
                "privilege_1.0.2",
                "privilege-gittok_1.0.2",
        });
        dsmConfig.newVersion("1.0.3", new String[]{
                "message_1.0.3",
                "oplog_1.0.3",
                "apply-auth_1.0.3",
                "xcode_1.0.3",
                "privilege_1.0.3",
                "privilege-gittok_1.0.3",
        });
        dsmConfig.newVersion("1.0.4", new String[]{
                "message_1.0.4",
                "oplog_1.0.4",
                "apply-auth_1.0.4",
                "xcode_1.0.4",
                "privilege_1.0.4",
        });
        dsmConfig.newVersion("1.0.5", new String[]{
                "message_1.0.5",
        });
        dsmConfig.newVersion("1.0.6", new String[]{
                "message_1.0.6"
        });
        dsmConfig.newVersion("1.0.7", new String[]{
                "message_1.0.7"
        });
        dsmConfig.newVersion("1.0.8", new String[]{
                "message_1.0.8"
        });
        dsmConfig.newVersion("1.1.1", new String[]{
                "user_1.1.1"
        });
        return dsmConfig;
    }
}