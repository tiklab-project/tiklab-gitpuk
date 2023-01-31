package net.tiklab.xcode.code.service;

import jakarta.ws.rs.HttpMethod;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@HttpMethod(value = "PROPFIND")
@Documented
public @interface Propfind {
}
