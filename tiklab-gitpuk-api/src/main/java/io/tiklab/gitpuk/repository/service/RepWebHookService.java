package io.tiklab.gitpuk.repository.service;

import io.tiklab.core.page.Pagination;
import io.tiklab.gitpuk.repository.model.RepWebHook;
import io.tiklab.gitpuk.repository.model.RepWebHookQuery;
import io.tiklab.toolkit.join.annotation.FindAll;
import io.tiklab.toolkit.join.annotation.FindList;
import io.tiklab.toolkit.join.annotation.FindOne;
import io.tiklab.toolkit.join.annotation.JoinProvider;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* RepWebHookService-RepWebHook
*/
@JoinProvider(model = RepWebHook.class)
public interface RepWebHookService {

    /**
    * 创建
    * @param repRepWebHook
    * @return
    */
    String createRepWebHook(@NotNull @Valid RepWebHook repRepWebHook);

    /**
    * 更新
    * @param repRepWebHook
    */
    void updateRepWebHook(@NotNull @Valid RepWebHook repRepWebHook);

    /**
    * 删除
    * @param id
    */
    void deleteRepWebHook(@NotNull String id);

    @FindOne
    RepWebHook findOne(@NotNull String id);
    @FindList
    List<RepWebHook> findList(List<String> idList);

    /**
    * 查找
    * @param id
    * @return
    */
    RepWebHook findRepWebHook(@NotNull String id);

    /**
    * 查找所有
    * @return
    */
    @FindAll
    List<RepWebHook> findAllRepWebHook();

    /**
    * 查询列表
    * @param repRepWebHookQuery
    * @return
    */
    List<RepWebHook> findRepWebHookList(RepWebHookQuery repRepWebHookQuery);


    /**
     * 分页查询列表
     * @param repRepWebHookQuery
     */
    Pagination<RepWebHook> findRepWebHookPag(RepWebHookQuery repRepWebHookQuery);

    /**
     * 执行webHook
     * @param repositoryId 仓库id
     * @param event ernHook事件 push、tag、merge
     */
    void execWebHook(String repositoryId,String event,String name);
}