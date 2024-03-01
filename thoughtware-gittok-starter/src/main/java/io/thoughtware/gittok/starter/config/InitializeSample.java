package io.thoughtware.gittok.starter.config;

import io.thoughtware.gittok.repository.service.InitializeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/*
 * 启动项目初始化示例仓库数据
 * */
@Component()
public class InitializeSample implements ApplicationRunner {

    @Autowired
    InitializeService sampleService;



    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 在这里执行需要最后加载的操作，例如创建和初始化特定Bean
        sampleService.createSampleData();
    }
}
