package io.tiklab.gitpuk.authority.request;

import java.util.List;

public class LfsBatchRequest {
    private String operation;
    private List<LfsObject> objects;

    private List<String> transfers;


    private LfsRef ref;


    private String hash_algo;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public List<LfsObject> getObjects() {
        return objects;
    }

    public void setObjects(List<LfsObject> objects) {
        this.objects = objects;
    }

    public List<String> getTransfers() {
        return transfers;
    }

    public void setTransfers(List<String> transfers) {
        this.transfers = transfers;
    }

    public LfsRef getRef() {
        return ref;
    }

    public void setRef(LfsRef ref) {
        this.ref = ref;
    }

    public String getHash_algo() {
        return hash_algo;
    }

    public void setHash_algo(String hash_algo) {
        this.hash_algo = hash_algo;
    }
}


