package io.thoughtware.gittork.scan.service;

import io.thoughtware.gittork.scan.model.ScanPlay;
import io.thoughtware.core.exception.ApplicationException;

/*
* spotBugs扫描 service
* */
public interface CodeScanSpotBugsService {

    /**
     * 通过sonar 扫描
     * @param scanPlay 扫描计划
     * @return 执行状态 true:成功 false:失败
     * @throws ApplicationException 运行失败
     */
    void codeScanBySpotBugs( ScanPlay scanPlay);

    /**
     * 查询扫描的问题
     * @param scanPlayId 扫描计划Id
     */
    String findScanBySpotBugs( String scanPlayId) ;
}
