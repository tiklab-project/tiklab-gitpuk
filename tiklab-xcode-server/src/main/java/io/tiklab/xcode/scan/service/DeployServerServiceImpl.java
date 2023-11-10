package io.tiklab.xcode.scan.service;

import io.tiklab.beans.BeanMapper;
import io.tiklab.core.page.Pagination;
import io.tiklab.core.page.PaginationBuilder;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.DeleteBuilders;
import io.tiklab.join.JoinTemplate;
import io.tiklab.rpc.annotation.Exporter;
import io.tiklab.xcode.scan.dao.DeployServerDao;
import io.tiklab.xcode.scan.entity.DeployServerEntity;
import io.tiklab.xcode.scan.model.DeployServer;
import io.tiklab.xcode.scan.model.DeployServerQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

/**
* DeployServerServiceImpl-部署服务接口实现
*/
@Service
@Exporter
public class DeployServerServiceImpl implements DeployServerService {

    @Autowired
    DeployServerDao deployServerDao;


    @Autowired
    JoinTemplate joinTemplate;


    @Override
    public String createDeployServer(@NotNull @Valid DeployServer deployServer) {

        DeployServerEntity deployServerEntity = BeanMapper.map(deployServer, DeployServerEntity.class);
        deployServerEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        String deployServerId= deployServerDao.createDeployServer(deployServerEntity);

        return deployServerId;
    }

    @Override
    public void updateDeployServer(@NotNull @Valid DeployServer deployServer) {
        DeployServerEntity deployServerEntity = BeanMapper.map(deployServer, DeployServerEntity.class);

        deployServerDao.updateDeployServer(deployServerEntity);
    }

    @Override
    public void deleteDeployServer(@NotNull String id) {
        deployServerDao.deleteDeployServer(id);
    }

    @Override
    public void deleteDeployServerByRecord(String repositoryId) {
        DeleteCondition deleteCondition = DeleteBuilders.createDelete(DeployServerEntity.class)
                .eq("repositoryId", repositoryId)
                .get();
        deployServerDao.deleteDeployServer(deleteCondition);
    }

    @Override
    public DeployServer findOne(String id) {
        DeployServerEntity deployServerEntity = deployServerDao.findDeployServer(id);

        DeployServer deployServer = BeanMapper.map(deployServerEntity, DeployServer.class);
        return deployServer;
    }

    @Override
    public List<DeployServer> findList(List<String> idList) {
        List<DeployServerEntity> deployServerEntityList =  deployServerDao.findDeployServerList(idList);

        List<DeployServer> deployServerList =  BeanMapper.mapList(deployServerEntityList, DeployServer.class);
        return deployServerList;
    }

    @Override
    public DeployServer findDeployServer(@NotNull String id) {
        DeployServer deployServer = findOne(id);

        joinTemplate.joinQuery(deployServer);

        return deployServer;
    }

    @Override
    public List<DeployServer> findAllDeployServer() {
        List<DeployServerEntity> deployServerEntityList =  deployServerDao.findAllDeployServer();

        List<DeployServer> deployServerList =  BeanMapper.mapList(deployServerEntityList, DeployServer.class);

        joinTemplate.joinQuery(deployServerList);

        return deployServerList;
    }

    @Override
    public List<DeployServer> findDeployServerList(DeployServerQuery DeployServerQuery) {
        List<DeployServerEntity> deployServerEntityList = deployServerDao.findDeployServerList(DeployServerQuery);

        List<DeployServer> deployServerList = BeanMapper.mapList(deployServerEntityList, DeployServer.class);



        joinTemplate.joinQuery(deployServerList);

        return deployServerList;
    }

    @Override
    public Pagination<DeployServer> findDeployServerPage(DeployServerQuery DeployServerQuery) {
        Pagination<DeployServerEntity>  pagination = deployServerDao.findDeployServerPage(DeployServerQuery);

        List<DeployServer> deployServerList = BeanMapper.mapList(pagination.getDataList(), DeployServer.class);
        joinTemplate.joinQuery(pagination.getDataList());

        return PaginationBuilder.build(pagination,deployServerList);
    }





}