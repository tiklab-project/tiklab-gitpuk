package io.tiklab.gitpuk.common;

import java.util.HashMap;
import java.util.Map;

public interface GitPukMessageService {

    /**
     * 配置全局消息
     * @param templateId 方案id
     * @param map 信息
     */
    void deployMessage(Map<String, Object> map,String templateId);

    /**
     * 配置日志
     * @param logType 方案类型
     * @param map 信息
     * @param  model 模型
     */
    void deployLog( Map<String, Object> map,String logType,String model);

    /**
     * 初始化公共消息，日志信息
     */
    HashMap<String,Object> initMessageAndLogMap();

}
