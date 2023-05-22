package io.tiklab.xcode.detection.service;

import io.tiklab.core.exception.ApplicationException;

public interface CodeScanManageService {

    /**
     * 代码扫描
     * @param repositoryId 代码仓库id
     * @return 执行状态 true:成功 false:失败
     * @throws ApplicationException 运行失败
     */
    boolean codeScanExec(String repositoryId);
}
