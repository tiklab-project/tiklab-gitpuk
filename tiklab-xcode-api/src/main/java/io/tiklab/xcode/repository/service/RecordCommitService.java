package io.tiklab.xcode.repository.service;

import io.tiklab.core.page.Pagination;
import io.tiklab.join.annotation.FindAll;
import io.tiklab.join.annotation.FindList;
import io.tiklab.join.annotation.FindOne;
import io.tiklab.join.annotation.JoinProvider;
import io.tiklab.xcode.repository.model.RecordCommit;
import io.tiklab.xcode.repository.model.RecordCommitQuery;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* RecordCommitService-插件接口
*/
@JoinProvider(model = RecordCommit.class)
public interface RecordCommitService {

    /**
    * 创建
    * @param recordCommit
    * @return
    */
    String createRecordCommit(@NotNull @Valid RecordCommit recordCommit);

    /**
    * 更新
    * @param recordCommit
    */
    void updateRecordCommit(@NotNull @Valid RecordCommit recordCommit);

    /**
    * 删除
    * @param id
    */
    void deleteRecordCommit(@NotNull String id);

    /**
     * 通过仓库删除
     * @param repositoryId 仓库id
     */
    void deleteRecordCommitByRepository(@NotNull String repositoryId);

    /**
     * 条件删除
     * @param
     */
    void deleteRecordCommitByRecord(@NotNull String repositoryId);

    @FindOne
    RecordCommit findOne(@NotNull String id);
    @FindList
    List<RecordCommit> findList(List<String> idList);

    /**
    * 查找
    * @param id
    * @return
    */
    RecordCommit findRecordCommit(@NotNull String id);

    /**
    * 查找所有
    * @return
    */
    @FindAll
    List<RecordCommit> findAllRecordCommit();

    /**
    * 查询列表
    * @param recordCommitQuery
    * @return
    */
    List<RecordCommit> findRecordCommitList(RecordCommitQuery recordCommitQuery);

    /**
    * 按分页查询
    * @param recordCommitQuery
    * @return
    */
    Pagination<RecordCommit> findRecordCommitPage(RecordCommitQuery recordCommitQuery);


}