package net.tiklab.xcode.authority;

import org.eclipse.jgit.http.server.ReceivePackErrorHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpReceivePackErrorHandler implements ReceivePackErrorHandler {

    /**
     * @param req The HTTP request
     * @param rsp The HTTP response
     * @param r   A continuation that handles a git-receive-pack request.
     * @throws IOException
     */
    @Override
    public void receive(HttpServletRequest req, HttpServletResponse rsp, ReceivePackRunnable r) throws IOException {
        r.receive();
    }
}
