package io.thoughtware.gittok.repository.service;

import io.thoughtware.core.page.Pagination;
import io.thoughtware.gittok.repository.model.LeadTo;
import io.thoughtware.gittok.repository.model.LeadToQuery;
import io.thoughtware.gittok.repository.model.LeadToResult;

import java.util.List;

public interface LeadToService {

    /**
     * 查询第三方 的仓库列表
     * @param importAuthId 第三方仓库认证信息
     * @param page 分页参数
     */
    Pagination<LeadTo> findThirdRepositoryList(String importAuthId, String page);

    /**
     * 导入仓库
     * @param leadTo leadTo
     */
    String toLeadRepository( LeadToQuery leadToQuery);

    /**
     * 查询导入仓库结果
     * @param key key
     */
    LeadToResult findToLeadResult(String key);
}
