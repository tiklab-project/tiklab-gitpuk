package io.tiklab.gitpuk.repository.service;

import io.tiklab.gitpuk.repository.model.RecordCommit;
import io.tiklab.gitpuk.repository.model.RecordCommitQuery;
import io.tiklab.core.page.Pagination;
import io.tiklab.toolkit.join.annotation.FindAll;
import io.tiklab.toolkit.join.annotation.FindList;
import io.tiklab.toolkit.join.annotation.FindOne;
import io.tiklab.toolkit.join.annotation.JoinProvider;

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
     * @param repositoryId
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
     * 通过仓库id 查询
     * @param recordCommitQuery
     * @return
     */
    List<RecordCommit> findRecordCommits(RecordCommitQuery recordCommitQuery);

    /**
     * 查询列表
     * @param userId
     * @return
     */
    List<RecordCommit> findRecordCommitList(String userId);

    /**
    * 按分页查询
    * @param recordCommitQuery
    * @return
    */
    Pagination<RecordCommit> findRecordCommitPage(RecordCommitQuery recordCommitQuery);


    /**
     * 修改提交记录
     * @param requestURI 提交地址
     * @param userId 提交用户
     * @param commitType 推送类型
     */
    void updateCommitRecord(String requestURI,String userId,String commitType);

    /**
     * 查询时间段内的提交
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    List<RecordCommit> findTimeRecordCommitList(String startTime, String endTime,String rpyId);
}