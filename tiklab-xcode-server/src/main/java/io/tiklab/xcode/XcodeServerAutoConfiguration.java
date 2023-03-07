package io.tiklab.xcode;

import io.tiklab.dsm.annotation.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@SQL(modules = {
        "xcode",
        "xcode-setting",
},order = 101)
@ComponentScan({"io.tiklab.xcode"})
public class XcodeServerAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(XcodeServerAutoConfiguration.class);
}
