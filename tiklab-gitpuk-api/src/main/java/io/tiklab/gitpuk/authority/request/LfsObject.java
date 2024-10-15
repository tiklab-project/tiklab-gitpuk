package io.tiklab.gitpuk.authority.request;

public class LfsObject {

    private String oid;
    private long size;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
