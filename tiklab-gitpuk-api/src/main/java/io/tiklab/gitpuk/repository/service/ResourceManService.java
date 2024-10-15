package io.tiklab.gitpuk.repository.service;


import io.tiklab.gitpuk.repository.model.ResourceMan;

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
