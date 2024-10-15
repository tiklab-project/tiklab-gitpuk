package io.tiklab.gitpuk.setting.service;

import io.tiklab.gitpuk.setting.model.SystemCount;

public interface SystemCountService {

    /**
     * 系统设置查询汇总数据
     */
    SystemCount collectCount();

}
