package net.tiklab.xcode.code.service;

import jakarta.ws.rs.Path;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface CloneCodeServer {

    /**
     * @param request 请求信息
     * @param response 请求体
     */

    void clone(ServletRequest request, ServletResponse response, String service);

}
