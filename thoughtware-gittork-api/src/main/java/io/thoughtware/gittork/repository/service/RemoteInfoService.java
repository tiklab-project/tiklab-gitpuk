package io.thoughtware.gittork.repository.service;


import io.thoughtware.gittork.repository.model.RemoteInfo;
import io.thoughtware.gittork.repository.model.RemoteInfoQuery;
import io.thoughtware.join.annotation.FindAll;
import io.thoughtware.join.annotation.FindList;
import io.thoughtware.join.annotation.FindOne;
import io.thoughtware.join.annotation.JoinProvider;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* RemoteInfoService-存储库接口
*/
@JoinProvider(model = RemoteInfo.class)
public interface RemoteInfoService {

    /**
    * 创建
    * @param remoteInfo
    * @return
    */
    String createRemoteInfo(@NotNull @Valid RemoteInfo remoteInfo);

    /**
    * 更新
    * @param remoteInfo
    */
    void updateRemoteInfo(@NotNull @Valid RemoteInfo remoteInfo);

    /**
    * 删除
    * @param id
    */
    void deleteRemoteInfo(@NotNull String id);

    @FindOne
    RemoteInfo findOne(@NotNull String id);

    List<RemoteInfo> findList(List<String> idList);

    /**
    * 查找
    * @param id
    * @return
    */
    @FindList
    RemoteInfo findRemoteInfo(@NotNull String id);

    /**
    * 查找所有
    * @return
    */
    @FindAll
    List<RemoteInfo> findAllRemoteInfo();

    /**
    * 查询列表
    * @param remoteInfoQuery
    * @return
    */
    List<RemoteInfo> findRemoteInfoList(RemoteInfoQuery remoteInfoQuery);

    /**
     * 向第三方推送仓库数据
     * @param remoteInfoQuery
     * @return
     */
    RemoteInfo sendRepository(RemoteInfoQuery remoteInfoQuery);

    /**
     * 单个向第三方推送仓库数据
     * @param remoteInfo
     * @return
     */
    String sendOneRepository(RemoteInfo remoteInfo);

    /**
     * 获取推送结果
     * @param remoteInfoId
     * @param  rpyId
     * @return
     */
    String findMirrorResult(String remoteInfoId,String rpyId);
}