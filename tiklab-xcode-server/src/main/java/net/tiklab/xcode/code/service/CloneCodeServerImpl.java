package net.tiklab.xcode.code.service;

import com.alibaba.fastjson.JSONObject;
import net.tiklab.core.exception.ApplicationException;
import net.tiklab.xcode.until.CodeUntil;
import org.apache.commons.lang3.time.CalendarUtils;
import org.apache.http.protocol.HTTP;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.ReceiveCommand;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import org.jboss.netty.handler.codec.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import static net.tiklab.xcode.until.CodeUntil.findSystemType;

@Service
public class CloneCodeServerImpl implements CloneCodeServer {

    @Autowired
    RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(CloneCodeServerImpl.class);


    /**
     * @param request 请求信息
     * @param response 请求体
     */
    @Override
    public void clone(ServletRequest request, ServletResponse response, String service) {


        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String rep = httpRequest.getParameter("rep");

        // httpResponse.sendError();
        try {

            String requestURI = httpRequest.getRequestURI();
            String method = httpRequest.getMethod();
            String contentType = httpRequest.getContentType();
            logger.info("==========================================================");
            logger.info("解析信息：method: "+method +"    type: "+contentType+"   service: "+service);

            // Iterator<String> headerItr = Collections.list(httpRequest.getHeaders("Accept")).iterator();
            // while (headerItr.hasNext()) {
            //     logger.info(headerItr.next());
            //     httpResponse.setHeader("WWW-Authenticate","Basic realm=\"Gitblit\"");
            // }


            HttpSession session = httpRequest.getSession(false);


            if (CodeUntil.isNoNull(service)){
                httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                // httpResponse.sendError(HttpServletResponse.SC_CONTINUE);
                return;
            }

            httpResponse.setStatus(HttpServletResponse.SC_OK);

            // httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED,"请求错误");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        // return "ref: refs/heads/master";
    }


    private void newSession(HttpServletRequest request, HttpServletResponse response) {
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null && oldSession.getAttribute("com.gitblit.secured") == null) {
            synchronized (this) {
                Map<String, Object> attributes = new HashMap<String, Object>();
                Enumeration<String> e = oldSession.getAttributeNames();
                while (e.hasMoreElements()) {
                    String name = e.nextElement();
                    attributes.put(name, oldSession.getAttribute(name));
                    oldSession.removeAttribute(name);
                }
                oldSession.invalidate();

                HttpSession newSession = request.getSession(true);
                newSession.setAttribute( "com.gitblit.secured", Boolean.TRUE);
                for (Map.Entry<String, Object> entry : attributes.entrySet()) {
                    newSession.setAttribute(entry.getKey(), entry.getValue());
                }
            }
        }
    }





}
