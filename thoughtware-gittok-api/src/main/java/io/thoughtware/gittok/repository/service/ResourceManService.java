package io.thoughtware.gittok.repository.service;


import io.thoughtware.gittok.repository.model.ResourceMan;

/*
* 查询资源占用情况
* */
public interface ResourceManService {

    /**
     * 查询资源
     * @return
     */
    ResourceMan findResource();
}
