package io.tiklab.xcode.detection.service;

import io.tiklab.core.exception.ApplicationException;
import io.tiklab.xcode.detection.model.CodeScan;

public interface CodeScanService {

    /**
     * 代码扫描
     * @param repositoryId
     * @return 执行状态 true:成功 false:失败
     * @throws ApplicationException 运行失败
     */
    boolean codeScanExec(String  repositoryId);


    /**
     * 查询代码扫描结果
     * @param repositoryId 代码仓库id
     * @param deployServerId 第三方认证id
     * @return 执行状态 true:成功 false:失败
     * @throws ApplicationException 运行失败
     */

    CodeScan findScanResult(String repositoryId, String deployServerId);

    /**
     * 查询代码扫描状态
     * @param repositoryId 代码仓库id
     * @return 执行状态 true:成功 false:失败
     * @throws ApplicationException 运行失败
     */
    String findScanState(String repositoryId);

    /**
     * 创建扫描方案
     * @param codeScan
     * @throws ApplicationException 运行失败
     */
    String createCodeScan(CodeScan codeScan);
}
