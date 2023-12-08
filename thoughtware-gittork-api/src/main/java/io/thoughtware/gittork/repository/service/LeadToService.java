package io.thoughtware.gittork.repository.service;

import io.thoughtware.gittork.repository.model.LeadTo;

import java.util.List;

public interface LeadToService {

    /**
     * 查询第三方 的仓库列表
     * @param importAuthId 第三方仓库认证信息
     * @param page 分页参数
     */
    List findThirdRepositoryList(String importAuthId,String page);

    /**
     * 导入仓库
     * @param leadTo leadTo
     */
    String toLeadRepository(LeadTo leadTo);

    /**
     * 查询导入仓库结果
     * @param thirdRepositoryId thirdRepositoryId
     */
    String findToLeadResult(String thirdRepositoryId);
}
