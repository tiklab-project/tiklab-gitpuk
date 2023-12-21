package io.thoughtware.gittork.repository.service;


import io.thoughtware.gittork.repository.model.ResourceMan;

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
