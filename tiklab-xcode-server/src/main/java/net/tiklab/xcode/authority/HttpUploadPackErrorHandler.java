package net.tiklab.xcode.authority;

import org.eclipse.jgit.http.server.UploadPackErrorHandler;
import org.eclipse.jgit.transport.http.HttpConnection;


import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Collection;
import java.util.Enumeration;

public class HttpUploadPackErrorHandler implements UploadPackErrorHandler {

    @Override
    public void upload(HttpServletRequest req, HttpServletResponse rsp, UploadPackRunnable r) throws IOException {

        System.out.println("=================================================");
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        String requestURI = req.getRequestURI();
        System.out.println("requestURI："+requestURI);

        BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println("内容为："+ line);
        }
        br.close();

        Collection<String> headerNames = rsp.getHeaderNames();

        ServletOutputStream outputStream = rsp.getOutputStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.writeTo(outputStream);

        r.upload();

        // rsp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

}



























