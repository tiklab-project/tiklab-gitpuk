package io.tiklab.xcode.setting.service;

import io.tiklab.xcode.setting.model.Backups;

import java.io.InputStream;

public interface BackupsServer {

    /**
     * 执行备份
     */
    void backupsExec();

    /**
     * 获取备份结果
     * @param type :backups、recovery
     */
    String gainBackupsRes(String type);

    /**
     * 修改备份相关数据
     */
    void updateBackups(Backups backups);

    /**
     * 查询备份相关数据
     */
    Backups findBackups();

    /**
     * 数据恢复
     * @param  userId yonhuid
     * @param fileName 文件名称
     */
    void recoveryData(String userId,String fileName);

    /**
     * 备份数据上传
     * @param  inputStream inputStream
     * @param fileName 文件名称
     * @param userId 用户id
     */
    void uploadBackups(InputStream inputStream, String fileName,String userId);
}