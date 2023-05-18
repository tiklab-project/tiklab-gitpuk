package io.tiklab.xcode.repository.service;

import io.tiklab.core.page.Pagination;
import io.tiklab.join.annotation.FindAll;
import io.tiklab.join.annotation.FindList;
import io.tiklab.join.annotation.FindOne;
import io.tiklab.join.annotation.JoinProvider;
import io.tiklab.xcode.repository.model.OpenRecord;
import io.tiklab.xcode.repository.model.OpenRecordQuery;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* OpenRecordService-插件接口
*/
@JoinProvider(model = OpenRecord.class)
public interface OpenRecordService {

    /**
    * 创建
    * @param openRecord
    * @return
    */
    String createOpenRecord(@NotNull @Valid OpenRecord openRecord);

    /**
    * 更新
    * @param openRecord
    */
    void updateOpenRecord(@NotNull @Valid OpenRecord openRecord);

    /**
    * 删除
    * @param id
    */
    void deleteOpenRecord(@NotNull String id);

    /**
     * 条件删除
     * @param
     */
    void deleteOpenRecordByRecord(@NotNull String repositoryId);

    @FindOne
    OpenRecord findOne(@NotNull String id);
    @FindList
    List<OpenRecord> findList(List<String> idList);

    /**
    * 查找
    * @param id
    * @return
    */
    OpenRecord findOpenRecord(@NotNull String id);

    /**
    * 查找所有
    * @return
    */
    @FindAll
    List<OpenRecord> findAllOpenRecord();

    /**
    * 查询列表
    * @param openRecordQuery
    * @return
    */
    List<OpenRecord> findOpenRecordList(OpenRecordQuery openRecordQuery);

    /**
    * 按分页查询
    * @param openRecordQuery
    * @return
    */
    Pagination<OpenRecord> findOpenRecordPage(OpenRecordQuery openRecordQuery);


}