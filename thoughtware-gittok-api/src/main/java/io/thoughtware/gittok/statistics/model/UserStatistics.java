package io.thoughtware.gittok.statistics.model;

import java.util.List;

public class UserStatistics {

    //用户名字
    private String name;

    private String type="line";

    //数量
    private List<Integer> data;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }
}
