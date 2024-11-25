package io.tiklab.gitpuk.authority.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/*
* response 返回的数据
* */
public class ReturnResponse {


    /**
     * lfs内存不足
     * @param response response
     */
    public static void lfsNotMemory (HttpServletResponse response) throws IOException {
        String jsonResponse ="{\"message\": \"Server memory insufficient to process the LFS object.\"," +
                " \"documentation_url\": \"https://your-server.com/docs\"}";
        // 设置状态码 507
        response.setStatus(507); // 507
        response.setContentType("application/json; charset=UTF-8");
        response.getWriter().write(jsonResponse);
    }

    /**
     * 服务内存不足
     * @param response response
     */
    public static void serverNotMemory (HttpServletResponse response) throws IOException {
        // 设置 HTTP 状态码 503 服务不可用
        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE); // 503
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setContentType("text/plain; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.print("Server memory is insufficient to process the Git push request");
    }

    /**
     * 401 账号认证失败
     * @param response response
     */
    public static void authFailure (HttpServletResponse response) throws IOException {
        response.setHeader("WWW-Authenticate", "Basic realm=\"HttpServlet\"");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); //401
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setContentType("text/plain; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.print("authentication failure");
    }

    /**
     * 认证用户权限
     * @param response response
     */
    public static void authPrivilege (HttpServletResponse response) throws IOException {
        response.setHeader("WWW-Authenticate", "Basic realm=\"HttpServlet\"");
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); //500
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setContentType("text/plain; charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.print("User has no permission");
    }

}
