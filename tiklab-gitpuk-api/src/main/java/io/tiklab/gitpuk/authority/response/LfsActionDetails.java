package io.tiklab.gitpuk.authority.response;

import java.util.Map;

public class LfsActionDetails {

    //用于下载(上传)对象的字符串 URL。
    private String href;

    //要应用的字符串 HTTP 标头键/值对的可选哈希 到请求
    private Map<String, String> header;

    //带秒的字符串大写 RFC 3339 格式时间戳 给定操作到期时的精度（通常是由于临时的 令牌）
    private String expires_at;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }


}
