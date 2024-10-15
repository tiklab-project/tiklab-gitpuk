package io.tiklab.gitpuk.repository.service;

import io.tiklab.gitpuk.repository.model.LeadAuth;
import io.tiklab.gitpuk.repository.model.LeadAuthQuery;
import io.tiklab.toolkit.join.annotation.FindAll;
import io.tiklab.toolkit.join.annotation.FindList;
import io.tiklab.toolkit.join.annotation.FindOne;
import io.tiklab.toolkit.join.annotation.JoinProvider;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* LeadAuthService-导入第三方应用的仓库
*/
@JoinProvider(model = LeadAuth.class)
public interface LeadAuthService {

    /**
    * 创建
    * @param leadAuth
    * @return
    */
    String createLeadAuth(@NotNull @Valid LeadAuth leadAuth);

    /**
    * 更新
    * @param leadAuth
    */
    void updateLeadAuth(@NotNull @Valid LeadAuth leadAuth);

    /**
    * 删除
    * @param id
    */
    void deleteLeadAuth(@NotNull String id);

    @FindOne
    LeadAuth findOne(@NotNull String id);
    @FindList
    List<LeadAuth> findList(List<String> idList);

    /**
    * 查找
    * @param id
    * @return
    */
    LeadAuth findLeadAuth(@NotNull String id);

    /**
    * 查找所有
    * @return
    */
    @FindAll
    List<LeadAuth> findAllLeadAuth();

    /**
    * 查询列表
    * @param leadAuthQuery
    * @return
    */
    List<LeadAuth> findLeadAuthList(LeadAuthQuery leadAuthQuery);


}