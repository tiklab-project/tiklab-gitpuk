package io.thoughtware.gittork.scan.service;

import io.thoughtware.gittork.scan.model.*;
import io.thoughtware.core.exception.ApplicationException;
import io.thoughtware.core.page.Pagination;

import java.util.List;

public interface CodeScanSonarService {


    /**
     * 通过sonar 扫描
     * @param scanPlay 扫描计划
     * @return 执行状态 true:成功 false:失败
     * @throws ApplicationException 运行失败
     */
    void codeScanBySonar(ScanPlay scanPlay);

    /**
     * 查询通过sonar 扫描的结果
     * @param scanPlayId 扫描计划id
     * @return 执行状态 true:成功 false:失败
     * @throws ApplicationException 运行失败
     */
    ScanRecord findScanBySonar(String scanPlayId);



    /**
     * 查询通过sonar 问题列表
     * @param scanRecordInstanceQuery
     */
    Pagination<ScanRecordInstance> findScanIssuesBySonar(ScanRecordInstanceQuery scanRecordInstanceQuery);

    /**
     * 查询通过sonar 问题详情
     * @param issueKey 扫描的key
     */
    List<ScanIssuesDetails> findScanIssuesDeBySonar(String issueKey, String component);
}
