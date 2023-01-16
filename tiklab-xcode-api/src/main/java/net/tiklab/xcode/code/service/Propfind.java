package net.tiklab.xcode.code.service;

import jakarta.ws.rs.HttpMethod;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
// @HttpMethod(value = "PROPFIND")
@HttpMethod(value = "Propfind")
@Documented
public @interface Propfind {
}
