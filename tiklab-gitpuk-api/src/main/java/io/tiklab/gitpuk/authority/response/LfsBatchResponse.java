package io.tiklab.gitpuk.authority.response;

import java.util.List;

public class LfsBatchResponse {

    private String transfer="basic";
    private List<LfsAction> objects;

    public String getTransfer() {
        return transfer;
    }

    public void setTransfer(String transfer) {
        this.transfer = transfer;
    }

    public List<LfsAction> getObjects() {
        return objects;
    }

    public void setObjects(List<LfsAction> objects) {
        this.objects = objects;
    }
}
