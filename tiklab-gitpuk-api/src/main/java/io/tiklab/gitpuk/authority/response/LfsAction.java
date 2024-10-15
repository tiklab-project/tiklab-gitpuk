package io.tiklab.gitpuk.authority.response;

import java.util.Map;

public class LfsAction {

    //LFS 对象的字符串 OID
    private String oid;

    //LFS 对象的整数字节大小。必须至少为零。
    private  long size=0L;

    //指定是否请求此 特定对象已被验证。如果省略或为 false，Git LFS 将尝试 查找 此 URL 的凭据 。
    private boolean authenticated=true;

    // 该对象的下一个操作的对象,取决于客户端将使用哪个传输适配器 正在使用。
    //private LfsObjectActions actions;

    private Map<String,LfsActionDetails> actions;


    //用于为此命名 Git LFS 对象的哈希算法 存储库。默认为 sha256 如果没有指定
    private String hash_algo="sha256";

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Map<String, LfsActionDetails> getActions() {
        return actions;
    }

    public void setActions(Map<String, LfsActionDetails> actions) {
        this.actions = actions;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getHash_algo() {
        return hash_algo;
    }

    public void setHash_algo(String hash_algo) {
        this.hash_algo = hash_algo;
    }
}
