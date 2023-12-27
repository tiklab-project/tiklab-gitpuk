package io.thoughtware.gittok;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({GittokServerAutoConfiguration.class})
public @interface EnableGitTokServer {
}
