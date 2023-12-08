package io.thoughtware.gittork.starter.annotation;

import io.thoughtware.gittork.starter.config.GitTorkConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({GitTorkConfiguration.class })
public @interface EnableGitTork {
}
