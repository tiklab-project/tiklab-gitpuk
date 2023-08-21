package io.tiklab.xcode.repository.service;

import io.tiklab.core.page.Pagination;
import io.tiklab.join.annotation.FindAll;
import io.tiklab.join.annotation.FindList;
import io.tiklab.join.annotation.FindOne;
import io.tiklab.join.annotation.JoinProvider;
import io.tiklab.xcode.repository.model.ImportAuth;
import io.tiklab.xcode.repository.model.ImportAuthQuery;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* ImportAuthService-导入第三方应用的仓库
*/
@JoinProvider(model = ImportAuth.class)
public interface ImportAuthService {

    /**
    * 创建
    * @param importAuth
    * @return
    */
    String createImportAuth(@NotNull @Valid ImportAuth importAuth);

    /**
    * 更新
    * @param importAuth
    */
    void updateImportAuth(@NotNull @Valid ImportAuth importAuth);

    /**
    * 删除
    * @param id
    */
    void deleteImportAuth(@NotNull String id);

    @FindOne
    ImportAuth findOne(@NotNull String id);
    @FindList
    List<ImportAuth> findList(List<String> idList);

    /**
    * 查找
    * @param id
    * @return
    */
    ImportAuth findImportAuth(@NotNull String id);

    /**
    * 查找所有
    * @return
    */
    @FindAll
    List<ImportAuth> findAllImportAuth();

    /**
    * 查询列表
    * @param importAuthQuery
    * @return
    */
    List<ImportAuth> findImportAuthList(ImportAuthQuery importAuthQuery);


}