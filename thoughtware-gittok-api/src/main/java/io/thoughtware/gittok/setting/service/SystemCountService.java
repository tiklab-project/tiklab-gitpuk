package io.thoughtware.gittok.setting.service;

import io.thoughtware.gittok.setting.model.SystemCount;

public interface SystemCountService {

    /**
     * 系统设置查询汇总数据
     */
    SystemCount collectCount();
}
