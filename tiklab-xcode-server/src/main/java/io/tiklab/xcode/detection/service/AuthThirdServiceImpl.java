package io.tiklab.xcode.detection.service;

import io.tiklab.beans.BeanMapper;
import io.tiklab.core.page.Pagination;
import io.tiklab.core.page.PaginationBuilder;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.DeleteBuilders;
import io.tiklab.join.JoinTemplate;
import io.tiklab.rpc.annotation.Exporter;
import io.tiklab.xcode.detection.dao.AuthThirdDao;
import io.tiklab.xcode.detection.entity.AuthThirdEntity;
import io.tiklab.xcode.detection.model.AuthThird;
import io.tiklab.xcode.detection.model.AuthThirdQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

/**
* AuthThirdServiceImpl-打开仓库的记录接口实现
*/
@Service
@Exporter
public class AuthThirdServiceImpl implements AuthThirdService {

    @Autowired
    AuthThirdDao authThirdDao;


    @Autowired
    JoinTemplate joinTemplate;


    @Override
    public String createAuthThird(@NotNull @Valid AuthThird authThird) {

        AuthThirdEntity authThirdEntity = BeanMapper.map(authThird, AuthThirdEntity.class);
        authThirdEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        String authThirdId= authThirdDao.createAuthThird(authThirdEntity);

        return authThirdId;
    }

    @Override
    public void updateAuthThird(@NotNull @Valid AuthThird authThird) {
        AuthThirdEntity authThirdEntity = BeanMapper.map(authThird, AuthThirdEntity.class);

        authThirdDao.updateAuthThird(authThirdEntity);
    }

    @Override
    public void deleteAuthThird(@NotNull String id) {
        authThirdDao.deleteAuthThird(id);
    }

    @Override
    public void deleteAuthThirdByRecord(String repositoryId) {
        DeleteCondition deleteCondition = DeleteBuilders.createDelete(AuthThirdEntity.class)
                .eq("repositoryId", repositoryId)
                .get();
        authThirdDao.deleteAuthThird(deleteCondition);
    }

    @Override
    public AuthThird findOne(String id) {
        AuthThirdEntity authThirdEntity = authThirdDao.findAuthThird(id);

        AuthThird authThird = BeanMapper.map(authThirdEntity, AuthThird.class);
        return authThird;
    }

    @Override
    public List<AuthThird> findList(List<String> idList) {
        List<AuthThirdEntity> authThirdEntityList =  authThirdDao.findAuthThirdList(idList);

        List<AuthThird> authThirdList =  BeanMapper.mapList(authThirdEntityList, AuthThird.class);
        return authThirdList;
    }

    @Override
    public AuthThird findAuthThird(@NotNull String id) {
        AuthThird authThird = findOne(id);

        joinTemplate.joinQuery(authThird);

        return authThird;
    }

    @Override
    public List<AuthThird> findAllAuthThird() {
        List<AuthThirdEntity> authThirdEntityList =  authThirdDao.findAllAuthThird();

        List<AuthThird> authThirdList =  BeanMapper.mapList(authThirdEntityList, AuthThird.class);

        joinTemplate.joinQuery(authThirdList);

        return authThirdList;
    }

    @Override
    public List<AuthThird> findAuthThirdList(AuthThirdQuery AuthThirdQuery) {
        List<AuthThirdEntity> authThirdEntityList = authThirdDao.findAuthThirdList(AuthThirdQuery);

        List<AuthThird> authThirdList = BeanMapper.mapList(authThirdEntityList, AuthThird.class);



        joinTemplate.joinQuery(authThirdList);

        return authThirdList;
    }

    @Override
    public Pagination<AuthThird> findAuthThirdPage(AuthThirdQuery AuthThirdQuery) {
        Pagination<AuthThirdEntity>  pagination = authThirdDao.findAuthThirdPage(AuthThirdQuery);

        List<AuthThird> authThirdList = BeanMapper.mapList(pagination.getDataList(), AuthThird.class);
        joinTemplate.joinQuery(pagination.getDataList());

        return PaginationBuilder.build(pagination,authThirdList);
    }





}