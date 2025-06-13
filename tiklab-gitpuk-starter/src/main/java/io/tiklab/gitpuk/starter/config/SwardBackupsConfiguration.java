package io.tiklab.gitpuk.starter.config;


import io.tiklab.security.backups.config.BackupsConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.tiklab.security.backups.util.BackupsFinal.GITPUK;

@Configuration
public class SwardBackupsConfiguration {


    @Value("${DATA_HOME}")
    private String dataHome;

    @Bean
    BackupsConfig backupsConfig() {
        return BackupsConfig.instance()
                .addPath(GITPUK,dataHome+"/repository")
                .get();
    }

}