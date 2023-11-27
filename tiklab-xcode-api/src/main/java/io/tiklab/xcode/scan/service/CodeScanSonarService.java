package io.tiklab.xcode.scan.service;

import io.tiklab.core.exception.ApplicationException;
import io.tiklab.core.page.Pagination;
import io.tiklab.xcode.scan.model.*;

import java.util.List;
import java.util.Map;

public interface CodeScanSonarService {


    /**
     * 通过sonar 扫描
     * @param scanPlayId 扫描计划id
     * @return 执行状态 true:成功 false:失败
     * @throws ApplicationException 运行失败
     */
    void codeScanBySonar(String scanPlayId);

    /**
     * 查询通过sonar 扫描的结果
     * @param scanPlayId 扫描计划id
     * @return 执行状态 true:成功 false:失败
     * @throws ApplicationException 运行失败
     */
    String findScanBySonar(String scanPlayId);

    /**
     * 查询通过sonar 问题列表
     * @param scanIssuesQuery 扫描计划id
     */
    Pagination<ScanIssues> findScanIssuesBySonar(ScanIssuesQuery scanIssuesQuery);

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
