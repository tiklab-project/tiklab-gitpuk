package io.tiklab.xcode.repository.service;

import io.tiklab.xcode.repository.model.ImportAuth;
import io.tiklab.xcode.repository.model.RepositoryToLead;

import java.util.List;

public interface RepositoryToLeadService {

    /**
     * 查询第三方 的仓库列表
     * @param importAuthId 第三方仓库认证信息
     * @param page 分页参数
     */
    List findThirdRepositoryList(String importAuthId,String page);

    /**
     * 导入仓库
     * @param repositoryToLead repositoryToLead
     */
    String toLeadRepository(RepositoryToLead repositoryToLead);

    /**
     * 查询导入仓库结果
     * @param thirdRepositoryId thirdRepositoryId
     */
    String findToLeadResult(String thirdRepositoryId);
}
