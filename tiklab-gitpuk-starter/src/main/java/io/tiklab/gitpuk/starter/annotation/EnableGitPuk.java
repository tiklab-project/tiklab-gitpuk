package io.tiklab.gitpuk.starter.annotation;

import io.tiklab.gitpuk.starter.config.GitPukConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({GitPukConfiguration.class })
public @interface EnableGitPuk {
}
