package io.thoughtware.gittok.starter.config;


import io.thoughtware.dsm.config.model.DsmConfig;
import io.thoughtware.dsm.support.DsmConfigBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class GitTokSqlLoad {

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
                //gittok
                "xcode_1.0.0",
                "xdprivilege_1.0.0",
                "backups_1.0.0",
        });

        dsmConfig.newVersion("1.0.1", new String[]{
                "xcode_1.0.1",
                "oplog_1.0.1",
                "message_1.0.1",
                "todotask_1.0.1",
                "xdprivilege_1.0.1",

        });
        dsmConfig.newVersion("1.0.2", new String[]{
                "xcode_1.0.2",
                "message_1.0.2",
                "oplog_1.0.2",
                "todotask_1.0.2",
                "xdprivilege_1.0.2",
        });
        dsmConfig.newVersion("1.0.3", new String[]{
                "xcode_1.0.3",
                "message_1.0.3",
                "oplog_1.0.3",
        });
        dsmConfig.newVersion("1.0.4", new String[]{
                "message_1.0.4",
                "oplog_1.0.4"
        });
        dsmConfig.newVersion("1.0.5", new String[]{
                "message_1.0.5",
        });
        dsmConfig.newVersion("1.0.6", new String[]{
               "message_1.0.6"
        });
        return dsmConfig;
    }
}
