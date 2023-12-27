package io.thoughtware.gittok.starter.annotation;

import io.thoughtware.gittok.starter.config.GitTokConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({GitTokConfiguration.class })
public @interface EnableGitTok {
}
