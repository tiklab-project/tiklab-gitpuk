package io.tiklab.xcode.task;

import io.tiklab.xcode.setting.model.Backups;
import io.tiklab.xcode.setting.service.BackupsServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
/*
*  备份定时任务
* */
@Component
public class BackupsTask {
    private static Logger logger = LoggerFactory.getLogger(BackupsTask.class);
    @Autowired
    BackupsServer backupsServer;

    //@Scheduled(cron = "0 */2 * * * ?")

    //凌晨2点 执行
    @Scheduled(cron = "0 0 2 * * ?")
    public void exeBackups(){
        Backups backups = backupsServer.findBackups();
        //设置了定时备份数据
        if (("true").equals(backups.getBackupsAddress())){
            logger.info("执行定时备份");
            backupsServer.backupsExec();
        }
    }
}
