package io.tiklab.gitpuk.starter.config;


import io.tiklab.dsm.model.DsmConfig;
import io.tiklab.dsm.model.DsmVersion;
import io.tiklab.dsm.support.DsmConfigBuilder;
import io.tiklab.dsm.support.DsmVersionBuilder;
import io.tiklab.gitpuk.repository.service.InitAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GitPukDsmAutoConfiguration {

    @Autowired
    InitAuthority initAuthority;

    @Bean
    DsmConfig dsmConfig(){
        DsmConfig dsmConfig = new DsmConfig();

        dsmConfig.setVersionList(versionList());
        return dsmConfig;
    }

    /**
     * 初始化Dsm版本列表
     * @return
     */
    List<DsmVersion> versionList(){
        List<DsmVersion> versionList = new ArrayList<>();
        DsmVersion dsmVersion = DsmVersionBuilder.instance()
                        .version("1.0.0")
                        .db(new String[]{
                                "user_1.0.0",
                                //PrivilegeDsm
                                "privilege_1.0.0",
                                //LicenceDsm
                                "app-authorization_1.0.0",
                                //MessageDsm
                                "message_1.0.0",
                                //SecurityDsm
                                "oplog_1.0.0",
                                //TodoTaskDsm
                                "todotask_1.0.0",
                                "openapi_1.0.0",


                                //xcode
                                "xcode_1.0.0",
                                "xdprivilege_1.0.0",
                                "privilege-gittok_1.0.0",
                                "gitpuk_1.0.0",
                         }).get();
        versionList.add(dsmVersion);

        //1.0.1
        dsmVersion = DsmVersionBuilder.instance()
                .version("1.0.1")
                .db(new String[]{
                        "xdprivilege_1.0.1",
                        "privilege-gittok_1.0.1",
                }).get();
        versionList.add(dsmVersion);

        dsmVersion = DsmVersionBuilder.instance()
                .version("1.0.2")
                .db(new String[]{
                        "privilege-gittok_1.0.2",
                }).get();
        versionList.add(dsmVersion);

        dsmVersion = DsmVersionBuilder.instance()
                .version("user_2.0.0")
                .db(new String[]{
                        "user_2.0.0",
                        "user_2.0.1",
                }).get();
        versionList.add(dsmVersion);

        dsmVersion = DsmVersionBuilder.instance()
                .version("1.0.3")
                .db(new String[]{
                        "privilege-gittok_1.0.3",

                }).get();
        versionList.add(dsmVersion);

        dsmVersion = DsmVersionBuilder.instance()
                .version("1.0.4")
                .db(new String[]{
                        "xcode_1.0.4",
                }).get();
        versionList.add(dsmVersion);

        DsmVersion message_109 = DsmVersionBuilder.instance()
                .version("message_1.0.9")
                .db(new String[]{
                        "message_1.0.9",
                }).get();
        versionList.add(message_109);

        dsmVersion = DsmVersionBuilder.instance()
                .version("prvilege_gorup")
                .db(new String[]{
                        "privilege-gorup_1.0.0"
                })
                .get();
        versionList.add(dsmVersion);
        dsmVersion = DsmVersionBuilder.instance()
                .version("prvilege_plat")
                .db(new String[]{
                        "prvilege-system_1.0.0",
                        "prvilege-project_1.0.0",
                })
                .task(initAuthority)
                .get();
        versionList.add(dsmVersion);

        dsmVersion = DsmVersionBuilder.instance()
                .version("message_2.0.0")
                .db(new String[]{
                        "message_2.0.0",
                }).get();
        versionList.add(dsmVersion);

        dsmVersion = DsmVersionBuilder.instance()
                .version("prvilege_gitpuk")
                .db(new String[]{
                        "gitpuk-priviege-system_1.0.0",
                        "gitpuk-priviege-project_1.0.0",
                })
                .task(initAuthority)
                .get();
        versionList.add(dsmVersion);

        dsmVersion = DsmVersionBuilder.instance()
                .version("licence_2.0.0")
                .db(new String[]{
                        "licence_2.0.0",
                })
                .get();
        versionList.add(dsmVersion);

        return versionList;
    }
}
