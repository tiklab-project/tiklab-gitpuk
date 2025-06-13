package io.tiklab.gitpuk.repository.service;

import io.tiklab.gitpuk.repository.model.IntegratedInQuery;

public interface IntegratedInService {

    /**
     * 分页查询流水线
     * @param integratedInQuery integratedInQuery
     */
    Object findPipelinePage(IntegratedInQuery integratedInQuery);

    //查询关联的流水线
    Object findRelevancePipelinePage(IntegratedInQuery integratedInQuery);

    /**
     * 分页查询未关联扫描计划
     * @param integratedInQuery integratedInQuery
     */
    Object findScanPlayPage(IntegratedInQuery integratedInQuery);

    /**
     * 查询关联的扫描计划
     * @param integratedInQuery integratedInQuery
     */
    Object findRelevanceScanPlay(IntegratedInQuery integratedInQuery);


}
