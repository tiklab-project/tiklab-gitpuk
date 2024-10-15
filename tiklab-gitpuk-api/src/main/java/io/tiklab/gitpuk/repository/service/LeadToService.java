package io.tiklab.gitpuk.repository.service;

import io.tiklab.core.page.Pagination;
import io.tiklab.gitpuk.repository.model.LeadTo;
import io.tiklab.gitpuk.repository.model.LeadToQuery;
import io.tiklab.gitpuk.repository.model.LeadToResult;

public interface LeadToService {

    /**
     * 查询第三方 的仓库列表
     * @param leadToQuery leadToQuery
     */
    Pagination<LeadTo> findThirdRepositoryList(LeadToQuery leadToQuery);

    /**
     * 导入仓库
     * @param leadToQuery leadTo
     */
    String toLeadRepository( LeadToQuery leadToQuery);

    /**
     * 查询导入仓库结果
     * @param key key
     */
    LeadToResult findToLeadResult(String key);
}
