package io.tiklab.xcode;

import io.tiklab.dsm.model.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan({"io.tiklab.xcode"})
public class XcodeServerAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(XcodeServerAutoConfiguration.class);

    @Bean
    SQL xcodeInitSql(){
        logger.info("init xcode project SQL");
        return new SQL(new String[]{
                "xcode",
                "xcode-setting",
        } ,101);
    }

}























