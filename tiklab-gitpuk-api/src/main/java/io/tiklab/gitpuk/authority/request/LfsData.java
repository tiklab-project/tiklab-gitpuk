package io.tiklab.gitpuk.authority.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LfsData {

    //请求request
    private HttpServletRequest request;

    //返回response
    private HttpServletResponse response;

    //lfs 信息
    private LfsBatchRequest lfsBatchRequest;

    //类型 http、ssh
    private String type;

    //lfs地址
    private String lfsPath;

    //域名地址
    private String domainNamePath;



    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public LfsBatchRequest getLfsBatchRequest() {
        return lfsBatchRequest;
    }

    public void setLfsBatchRequest(LfsBatchRequest lfsBatchRequest) {
        this.lfsBatchRequest = lfsBatchRequest;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLfsPath() {
        return lfsPath;
    }

    public void setLfsPath(String lfsPath) {
        this.lfsPath = lfsPath;
    }

    public String getDomainNamePath() {
        return domainNamePath;
    }

    public void setDomainNamePath(String domainNamePath) {
        this.domainNamePath = domainNamePath;
    }
}
