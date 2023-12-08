package io.thoughtware.gittork.repository.service;

import io.thoughtware.gittork.repository.model.RecordOpen;
import io.thoughtware.gittork.repository.model.RecordOpenQuery;
import io.thoughtware.core.page.Pagination;
import io.thoughtware.join.annotation.FindAll;
import io.thoughtware.join.annotation.FindList;
import io.thoughtware.join.annotation.FindOne;
import io.thoughtware.join.annotation.JoinProvider;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* RecordOpenService-插件接口
*/
@JoinProvider(model = RecordOpen.class)
public interface RecordOpenService {

    /**
    * 创建
    * @param recordCommit
    * @return
    */
    String createRecordOpen(@NotNull @Valid RecordOpen recordCommit);

    /**
    * 更新
    * @param recordCommit
    */
    void updateRecordOpen(@NotNull @Valid RecordOpen recordCommit);

    /**
    * 删除
    * @param id
    */
    void deleteRecordOpen(@NotNull String id);

    /**
     * 条件删除
     * @param repositoryId
     */
    void deleteRecordOpenByRecord(@NotNull String repositoryId);

    @FindOne
    RecordOpen findOne(@NotNull String id);
    @FindList
    List<RecordOpen> findList(List<String> idList);

    /**
    * 查找
    * @param id
    * @return
    */
    RecordOpen findRecordOpen(@NotNull String id);

    /**
    * 查找所有
    * @return
    */
    @FindAll
    List<RecordOpen> findAllRecordOpen();

    /**
    * 查询列表
    * @param recordOpenQuery
    * @return
    */
    List<RecordOpen> findRecordOpenList(RecordOpenQuery recordOpenQuery);

    /**
    * 按分页查询
    * @param recordOpenQuery
    * @return
    */
    Pagination<RecordOpen> findRecordOpenPage(RecordOpenQuery recordOpenQuery);


}