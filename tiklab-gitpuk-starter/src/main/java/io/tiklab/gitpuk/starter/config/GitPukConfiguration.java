package io.tiklab.gitpuk.starter.config;

import io.tiklab.core.exception.ApplicationException;
import io.tiklab.dsm.boot.starter.annotation.EnableDsm;
import io.tiklab.gitpuk.EnableGitPukServer;
import io.tiklab.dal.boot.starter.annotation.EnableDal;
import io.tiklab.dcs.boot.starter.annotation.EnableDcsClient;
import io.tiklab.dcs.boot.starter.annotation.EnableDcsServer;
import io.tiklab.eam.boot.starter.annotation.EnableEamClient;
import io.tiklab.eam.boot.starter.annotation.EnableEamServer;
import io.tiklab.gateway.boot.starter.annotation.EnableGateway;
import io.tiklab.gitpuk.common.GitPukFinal;
import io.tiklab.gitpuk.repository.service.InitializeService;
import io.tiklab.gitpuk.timedtask.model.TimeTaskInstance;
import io.tiklab.gitpuk.timedtask.service.TimeTaskInstanceService;
import io.tiklab.gitpuk.timedtask.service.TimeTaskService;
import io.tiklab.gitpuk.timedtask.util.JobManager;
import io.tiklab.gitpuk.timedtask.util.RunJob;
import io.tiklab.install.runner.TiklabApplicationRunner;
import io.tiklab.install.spring.boot.starter.EnableInstallServer;
import io.tiklab.licence.boot.starter.annotation.EnableLicenceServer;
import io.tiklab.messsage.boot.starter.annotation.EnableMessageServer;
import io.tiklab.openapi.boot.starter.annotation.EnableOpenApi;
import io.tiklab.postgresql.spring.boot.starter.EnablePostgresql;
import io.tiklab.postin.client.EnablePostInClient;
import io.tiklab.postin.client.openapi.ParamConfig;
import io.tiklab.postin.client.openapi.ParamConfigBuilder;
import io.tiklab.postin.client.openapi.PostInClientConfig;
import io.tiklab.privilege.boot.starter.annotation.EnablePrivilegeServer;
import io.tiklab.rpc.boot.starter.annotation.EnableRpc;
import io.tiklab.security.backups.config.BackupsConfig;
import io.tiklab.security.boot.stater.annotation.EnableSecurityServer;
import io.tiklab.toolkit.boot.starter.annotation.EnableToolkit;
import io.tiklab.user.boot.starter.annotation.EnableUserClient;
import io.tiklab.user.boot.starter.annotation.EnableUserServer;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static io.tiklab.security.backups.util.BackupsFinal.GITPUK;


@Configuration
//common
@EnableToolkit
@EnableInstallServer
@EnablePostgresql
@EnableDal
@EnableDsm
@EnableDcsServer
@EnableDcsClient
@EnableOpenApi
@EnableRpc



//eam
@EnableEamServer
@EnableEamClient
@EnableLicenceServer

@EnableSecurityServer
@EnableUserServer
@EnableUserClient
@EnableMessageServer
@EnablePostInClient

@EnableGateway
@EnablePrivilegeServer
@EnableGitPukServer
@ComponentScan(value = "io.tiklab.gitpuk")
public class GitPukConfiguration {


    //设置文件上传大小
    @Configuration
    public class FileUploadConfiuration {
        @Bean
        public MultipartConfigElement multipartConfig() {
            MultipartConfigFactory factory = new MultipartConfigFactory();
            //单个文件大小5GB
            factory.setMaxFileSize(DataSize.ofGigabytes(10L));
            //设置总上传数据大小5GB
            factory.setMaxRequestSize(DataSize.ofGigabytes(10L));

            return factory.createMultipartConfig();
        }
    }

   //启动项目初始化示例仓库数据
    @Component()
    public class InitializeSample implements TiklabApplicationRunner {

        @Autowired
        InitializeService sampleService;


        @Override
        public void run() {
            // 在这里执行需要最后加载的操作，例如创建和初始化特定Bean
            sampleService.createSampleData();

            //修改仓库的角色
            sampleService.updateRepRole();
        }
    }

    //备份文件路径
    @Configuration
    public class SwardBackupsConfiguration {


        @Value("${DATA_HOME}")
        private String dataHome;

        @Bean
        BackupsConfig backupsConfig() {
            return BackupsConfig.instance()
                    .addPath(GITPUK,dataHome+"/repository")
                    .get();
        }

    }


    @Configuration
    public class PostInClientAutoConfiguration {

        @Bean
        PostInClientConfig postInClientConfig(ParamConfig paramConfig){
            PostInClientConfig config = new PostInClientConfig();
            config.setParamConfig(paramConfig);

            return config;
        }

        @Bean
        ParamConfig paramConfig(){
            //设置请求头，属性名称：属性描述
            HashMap<String,String> headers = new HashMap<>();
            headers.put("accessToken","设置的apiKey");

            return ParamConfigBuilder.instance()
                    .setScanPackage("io.tiklab.gitpuk") //设置扫描的包路径
                    .prePath("/api")             //设置额外的前缀
                    .setHeaders(headers)               //设置请求头
                    .get();
        }

    }

    //启动定时任务
    @Component
    public class QuartzListener implements TiklabApplicationRunner {

        @Autowired
        private JobManager jobManager;

        @Autowired
        TimeTaskService timeTaskService;

        @Autowired
        TimeTaskInstanceService taskInstanceService;

        @Override
        public void run() {
            exec();
        }


        //定时任务执行
        public void exec(){
            List<TimeTaskInstance> timeTaskInstance = taskInstanceService.findAllTimeTaskInstance();
            if (CollectionUtils.isNotEmpty(timeTaskInstance)){
                //过滤出触发过后的
                List<TimeTaskInstance> instances = timeTaskInstance.stream().filter(a -> a.getExecState() != 2).collect(Collectors.toList());

                if (CollectionUtils.isNotEmpty(instances)){
                    for (TimeTaskInstance taskInstance:instances){
                        try {
                            jobManager.addJob(taskInstance, RunJob.class, GitPukFinal.DEFAULT);
                        } catch (SchedulerException e) {
                            throw new ApplicationException(e);
                        }
                    }
                }
            }
        }
    }
}
