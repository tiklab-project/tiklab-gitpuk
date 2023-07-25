package io.tiklab.xcode.starter.annotation;

import io.tiklab.xcode.starter.config.XcodeAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({XcodeAutoConfiguration.class })
public @interface EnableXcode {
}
