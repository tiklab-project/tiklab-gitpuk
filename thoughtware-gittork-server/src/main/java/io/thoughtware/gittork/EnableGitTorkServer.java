package io.thoughtware.gittork;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({GittorkServerAutoConfiguration.class})
public @interface EnableGitTorkServer {
}
