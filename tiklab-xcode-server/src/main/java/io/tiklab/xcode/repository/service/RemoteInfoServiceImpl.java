package io.tiklab.xcode.repository.service;

import io.tiklab.beans.BeanMapper;
import io.tiklab.core.exception.SystemException;
import io.tiklab.join.JoinTemplate;
import io.tiklab.rpc.annotation.Exporter;
import io.tiklab.xcode.git.GitUntil;
import io.tiklab.xcode.repository.dao.RemoteInfoDao;
import io.tiklab.xcode.repository.entity.RemoteInfoEntity;
import io.tiklab.xcode.repository.model.RemoteInfo;
import io.tiklab.xcode.repository.model.RemoteInfoQuery;
import io.tiklab.xcode.util.RepositoryUtil;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URISyntaxException;
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

    @Value("${repository.address}")
    private String memoryAddress;

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
                    GitUntil.remoteRepository(memoryAddress+"/"+remoteInfo.getRpyId()+".git",remoteInfo);
                } catch (Exception  e) {
                    if (e.getMessage().contains("Nothing to push.")){
                        remoteResultMap.put(key,"已经是最新的代码，无需推送");
                    }
                    if (e.getMessage().contains("not authorized")){
                        remoteResultMap.put(key,"认证未通过，请检查认证信息和权限");
                    }
                    if (e.getMessage().contains("501 Not Implemented")){
                        remoteResultMap.put(key,"连接失败，请检查镜像地址是否正确");
                    }
                    if (e.getMessage().contains("Not Found")){
                        remoteResultMap.put(key,"仓库或者地址不正确");
                    }
                    if (e.getMessage().contains("repository not found")){
                        remoteResultMap.put(key,"本地仓库不存在");
                    }
                    if (e.getMessage().contains("Read timed out after")){
                        remoteResultMap.put(key,"连接超时，请检查镜像地址或者镜像仓库");
                    }
                    remoteResultMap.put(key,e.getMessage());
                    throw new RuntimeException(e.getMessage());
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