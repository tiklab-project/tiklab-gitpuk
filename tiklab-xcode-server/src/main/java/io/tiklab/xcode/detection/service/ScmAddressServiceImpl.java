package io.tiklab.xcode.detection.service;

import io.tiklab.beans.BeanMapper;
import io.tiklab.core.page.Pagination;
import io.tiklab.core.page.PaginationBuilder;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.DeleteBuilders;
import io.tiklab.join.JoinTemplate;
import io.tiklab.rpc.annotation.Exporter;
import io.tiklab.xcode.detection.service.ScmAddressService;
import io.tiklab.xcode.detection.dao.ScmAddressDao;
import io.tiklab.xcode.detection.entity.ScmAddressEntity;
import io.tiklab.xcode.detection.model.ScmAddress;
import io.tiklab.xcode.detection.model.ScmAddressQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

/**
* ScmAddressServiceImpl-打开仓库的记录接口实现
*/
@Service
@Exporter
public class ScmAddressServiceImpl implements ScmAddressService {

    @Autowired
    ScmAddressDao scmAddressDao;


    @Autowired
    JoinTemplate joinTemplate;


    @Override
    public String createScmAddress(@NotNull @Valid ScmAddress scmAddress) {

        ScmAddressEntity scmAddressEntity = BeanMapper.map(scmAddress, ScmAddressEntity.class);
        scmAddressEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        String scmAddressId= scmAddressDao.createScmAddress(scmAddressEntity);

        return scmAddressId;
    }

    @Override
    public void updateScmAddress(@NotNull @Valid ScmAddress scmAddress) {
        ScmAddressEntity scmAddressEntity = BeanMapper.map(scmAddress, ScmAddressEntity.class);

        scmAddressDao.updateScmAddress(scmAddressEntity);
    }

    @Override
    public void deleteScmAddress(@NotNull String id) {
        scmAddressDao.deleteScmAddress(id);
    }

    @Override
    public void deleteScmAddressByRecord(String repositoryId) {
        DeleteCondition deleteCondition = DeleteBuilders.createDelete(ScmAddressEntity.class)
                .eq("repositoryId", repositoryId)
                .get();
        scmAddressDao.deleteScmAddress(deleteCondition);
    }

    @Override
    public ScmAddress findOne(String id) {
        ScmAddressEntity scmAddressEntity = scmAddressDao.findScmAddress(id);

        ScmAddress scmAddress = BeanMapper.map(scmAddressEntity, ScmAddress.class);
        return scmAddress;
    }

    @Override
    public List<ScmAddress> findList(List<String> idList) {
        List<ScmAddressEntity> scmAddressEntityList =  scmAddressDao.findScmAddressList(idList);

        List<ScmAddress> scmAddressList =  BeanMapper.mapList(scmAddressEntityList, ScmAddress.class);
        return scmAddressList;
    }

    @Override
    public ScmAddress findScmAddress(@NotNull String id) {
        ScmAddress scmAddress = findOne(id);

        joinTemplate.joinQuery(scmAddress);

        return scmAddress;
    }

    @Override
    public List<ScmAddress> findAllScmAddress() {
        List<ScmAddressEntity> scmAddressEntityList =  scmAddressDao.findAllScmAddress();

        List<ScmAddress> scmAddressList =  BeanMapper.mapList(scmAddressEntityList, ScmAddress.class);

        joinTemplate.joinQuery(scmAddressList);

        return scmAddressList;
    }

    @Override
    public List<ScmAddress> findScmAddressList(ScmAddressQuery ScmAddressQuery) {
        List<ScmAddressEntity> scmAddressEntityList = scmAddressDao.findScmAddressList(ScmAddressQuery);

        List<ScmAddress> scmAddressList = BeanMapper.mapList(scmAddressEntityList, ScmAddress.class);



        joinTemplate.joinQuery(scmAddressList);

        return scmAddressList;
    }

    @Override
    public Pagination<ScmAddress> findScmAddressPage(ScmAddressQuery ScmAddressQuery) {
        Pagination<ScmAddressEntity>  pagination = scmAddressDao.findScmAddressPage(ScmAddressQuery);

        List<ScmAddress> scmAddressList = BeanMapper.mapList(pagination.getDataList(), ScmAddress.class);
        joinTemplate.joinQuery(pagination.getDataList());

        return PaginationBuilder.build(pagination,scmAddressList);
    }





}