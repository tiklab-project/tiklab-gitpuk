package io.tiklab.gitpuk.file.model;

import java.util.List;

public class FileTree {



    private String path;

    private String name;

    private List<FileTreeItem> items;


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FileTreeItem> getItems() {
        return items;
    }

    public void setItems(List<FileTreeItem> items) {
        this.items = items;
    }
}

