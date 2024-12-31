package io.tiklab.gitpuk.starter.config;

import io.tiklab.eam.client.author.config.TiklabApplicationRunner;
import io.tiklab.gitpuk.repository.service.InitializeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
 * 启动项目初始化示例仓库数据
 * */
@Component()
public class InitializeSample implements TiklabApplicationRunner {

    @Autowired
    InitializeService sampleService;

    @Override
    public void run() {
        // 在这里执行需要最后加载的操作，例如创建和初始化特定Bean
        sampleService.createSampleData();
    }
}
