package io.tiklab.gitpuk;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({GitPukServerAutoConfiguration.class})
public @interface EnableGitPukServer {
}
