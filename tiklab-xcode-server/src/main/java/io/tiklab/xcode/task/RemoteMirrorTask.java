package io.tiklab.xcode.task;

import io.tiklab.xcode.repository.model.RemoteInfo;
import io.tiklab.xcode.repository.service.RemoteInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/*
 *  远程镜像定时任务
 * */
@Component
public class RemoteMirrorTask {
    private static Logger logger = LoggerFactory.getLogger(RemoteMirrorTask.class);

    @Autowired
    RemoteInfoService remoteInfoService;

    //凌晨2点 执行
    @Scheduled(cron = "0 0 2 * * ?")
    public void exeMirror(){
        List<RemoteInfo> allRemoteInfo = remoteInfoService.findAllRemoteInfo();
        for (RemoteInfo remoteInfo:allRemoteInfo){
            Integer timedState = remoteInfo.getTimedState();
            //开启定时任务
            if (timedState==1){
                logger.info(remoteInfo.getRpyId()+"执行定时镜像");
                remoteInfoService.sendOneRepository(remoteInfo);
            }
        }
    }
}
