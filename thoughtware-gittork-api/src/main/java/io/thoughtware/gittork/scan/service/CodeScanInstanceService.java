package io.thoughtware.gittork.scan.service;

import io.thoughtware.gittork.scan.model.CodeScanInstance;
import io.thoughtware.gittork.scan.model.CodeScanInstanceQuery;
import io.thoughtware.core.page.Pagination;
import io.thoughtware.join.annotation.FindAll;
import io.thoughtware.join.annotation.FindList;
import io.thoughtware.join.annotation.FindOne;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface CodeScanInstanceService {

    /**
     * 创建
     * @param codeScanInstance
     * @return
     */
    String createCodeScanInstance(@NotNull @Valid CodeScanInstance codeScanInstance);

    /**
     * 更新
     * @param codeScanInstance
     */
    void updateCodeScanInstance(@NotNull @Valid CodeScanInstance codeScanInstance);

    /**
     * 删除
     * @param id
     */
    void deleteCodeScanInstance(@NotNull String id);

    /**
     * 条件删除
     * @param repositoryId
     */
    void deleteCodeScanInstanceByRecord(@NotNull String repositoryId);

    @FindOne
    CodeScanInstance findOne(@NotNull String id);
    @FindList
    List<CodeScanInstance> findList(List<String> idList);

    /**
     * 查找
     * @param id
     * @return
     */
    CodeScanInstance findCodeScanInstance(@NotNull String id);

    /**
     * 查找所有
     * @return
     */
    @FindAll
    List<CodeScanInstance> findAllCodeScanInstance();

    /**
     * 查询列表
     * @param codeScanInstanceQuery
     * @return
     */
    List<CodeScanInstance> findCodeScanInstanceList(CodeScanInstanceQuery codeScanInstanceQuery);

    /**
     * 按分页查询
     * @param codeScanInstanceQuery
     * @return
     */
    Pagination<CodeScanInstance> findCodeScanInstancePage(CodeScanInstanceQuery codeScanInstanceQuery);
}
