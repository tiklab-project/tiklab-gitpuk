package io.thoughtware.gittok.repository.service;

import io.thoughtware.gittok.common.GitTokYamlDataMaService;
import io.thoughtware.gittok.common.git.GitUntil;
import io.thoughtware.gittok.repository.dao.RemoteInfoDao;
import io.thoughtware.gittok.repository.entity.RemoteInfoEntity;
import io.thoughtware.gittok.repository.model.RemoteInfo;
import io.thoughtware.gittok.repository.model.RemoteInfoQuery;
import io.thoughtware.toolkit.beans.BeanMapper;
import io.thoughtware.core.exception.SystemException;
import io.thoughtware.toolkit.join.JoinTemplate;
import io.thoughtware.rpc.annotation.Exporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
* RemoteInfoServiceImpl-制品库
*/
@Service
@Exporter
public class RemoteInfoServiceImpl implements RemoteInfoService {

    @Autowired
    RemoteInfoDao remoteInfoDao;

    @Autowired
    JoinTemplate joinTemplate;

    @Autowired
    GitTokYamlDataMaService yamlDataMaService;

    //推送镜像结果
    public static Map<String , String> remoteResultMap = new HashMap<>();
    

    @Override
    public String createRemoteInfo(@NotNull @Valid RemoteInfo remoteInfo) {
        RemoteInfoEntity repositoryEntity = BeanMapper.map(remoteInfo, RemoteInfoEntity.class);

        repositoryEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));


        String repositoryId = remoteInfoDao.createRemoteInfo(repositoryEntity);

        return repositoryId;
    }

    @Override
    public void updateRemoteInfo(@NotNull @Valid RemoteInfo remoteInfo) {
        RemoteInfoEntity repositoryEntity = BeanMapper.map(remoteInfo, RemoteInfoEntity.class);

        remoteInfoDao.updateRemoteInfo(repositoryEntity);
    }

    @Override
    public void deleteRemoteInfo(@NotNull String id) {

        remoteInfoDao.deleteRemoteInfo(id);

    }

    @Override
    public RemoteInfo findOne(String id) {
        RemoteInfoEntity repositoryEntity = remoteInfoDao.findOneRemoteInfo(id);

        RemoteInfo remoteInfo = BeanMapper.map(repositoryEntity, RemoteInfo.class);

        return remoteInfo;
    }

    @Override
    public List<RemoteInfo> findList(List<String> idList) {
        List<RemoteInfoEntity> repositoryEntityList =  remoteInfoDao.findRepositoryListByIds(idList);

        List<RemoteInfo> repositoryList =  BeanMapper.mapList(repositoryEntityList,RemoteInfo.class);
        return repositoryList;
    }

    @Override
    public RemoteInfo findRemoteInfo(@NotNull String id) {
        RemoteInfo remoteInfo = findOne(id);

        joinTemplate.joinQuery(remoteInfo);

        return remoteInfo;
    }

    @Override
    public List<RemoteInfo> findAllRemoteInfo() {
        List<RemoteInfoEntity> repositoryEntityList =  remoteInfoDao.findAllRemoteInfo();

        List<RemoteInfo> repositoryList =  BeanMapper.mapList(repositoryEntityList,RemoteInfo.class);

        joinTemplate.joinQuery(repositoryList);

        return repositoryList;
    }

    @Override
    public List<RemoteInfo> findRemoteInfoList(RemoteInfoQuery repositoryQuery) {
        List<RemoteInfoEntity> repositoryEntityList = remoteInfoDao.findRepositoryList(repositoryQuery);

        List<RemoteInfo> repositoryList = BeanMapper.mapList(repositoryEntityList,RemoteInfo.class);

        joinTemplate.joinQuery(repositoryList);

        return repositoryList;
    }

    @Override
    public RemoteInfo sendRepository(RemoteInfoQuery remoteInfoQuery) {
        List<RemoteInfo> remoteInfoList = findRemoteInfoList(remoteInfoQuery);
        for (RemoteInfo remoteInfo:remoteInfoList){
            sendOneRepository(remoteInfo);
        }
        return null;
    }

    @Override
    public String sendOneRepository(RemoteInfo remoteInfo) {
        String key = remoteInfo.getId() + remoteInfo.getRpyId();
        remoteResultMap.remove(key);

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(new Runnable(){
            @Override
            public void run() {
                try {
                    GitUntil.remoteRepository(yamlDataMaService.repositoryAddress()+"/"+remoteInfo.getRpyId()+".git",remoteInfo);
                } catch (Exception  e) {
                    if (e.getMessage().contains("Nothing to push.")){
                        remoteResultMap.put(key,"已经是最新的代码，无需推送");
                        return;
                    }
                    if (e.getMessage().contains("not authorized")){
                        remoteResultMap.put(key,"认证未通过，请检查认证信息和权限");
                        return;
                    }
                    if (e.getMessage().contains("501 Not Implemented")){
                        remoteResultMap.put(key,"连接失败，请检查镜像地址是否正确");
                        return;
                    }
                    if (e.getMessage().contains("Not Found")){
                        remoteResultMap.put(key,"仓库或者地址不正确");
                        return;
                    }
                    if (e.getMessage().contains("repository not found")){
                        remoteResultMap.put(key,"本地仓库不存在");
                        return;
                    }
                    if (e.getMessage().contains("Read timed out after")){
                        remoteResultMap.put(key,"连接超时，请检查镜像地址或者镜像仓库");
                        return;
                    }
                    remoteResultMap.put(key,e.getMessage());
                    throw new SystemException(e.getMessage());
                }
                remoteResultMap.put(key,"succeed");
            }
        });

        return "OK";
    }

    @Override
    public String findMirrorResult(String remoteInfoId,String rpyId) {
        return remoteResultMap.get(remoteInfoId+rpyId);
    }
}