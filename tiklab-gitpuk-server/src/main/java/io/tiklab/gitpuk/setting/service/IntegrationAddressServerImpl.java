package io.tiklab.gitpuk.setting.service;

import io.tiklab.gitpuk.common.RepositoryFinal;
import io.tiklab.gitpuk.common.RepositoryUtil;
import io.tiklab.gitpuk.setting.dao.IntegrationAddressDao;
import io.tiklab.gitpuk.setting.entity.IntegrationAddressEntity;
import io.tiklab.gitpuk.setting.model.IntegrationAddress;
import io.tiklab.gitpuk.setting.model.IntegrationAddressQuery;
import io.tiklab.rpc.annotation.Exporter;
import io.tiklab.toolkit.beans.BeanMapper;
import io.tiklab.toolkit.join.JoinTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigInteger;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@Service
@Exporter
public class IntegrationAddressServerImpl implements IntegrationAddressServer {


    @Autowired
    private IntegrationAddressDao integrationAddressDao;

    /**
     * 创建系统集成地址
     * @param integrationAddress 信息
     * @return 系统集成地址id
     */
    @Override
    public String createIntegrationAddress(IntegrationAddress integrationAddress) {
        integrationAddress.setCreateTime(new Timestamp(System.currentTimeMillis()));
        IntegrationAddressEntity groupEntity = BeanMapper.map(integrationAddress, IntegrationAddressEntity.class);
        return integrationAddressDao.createIntegrationAddress(groupEntity);
    }

    /**
     * 删除系统集成地址
     * @param IntegrationAddressId 系统集成地址id
     */
    @Override
    public void deleteIntegrationAddress(String IntegrationAddressId) {
        integrationAddressDao.deleteIntegrationAddress(IntegrationAddressId);
    }

    /**
     * 更新系统集成地址
     * @param integrationAddress 系统集成地址信息
     */
    @Override
    public void updateIntegrationAddress(IntegrationAddress integrationAddress) {
        IntegrationAddressEntity groupEntity = BeanMapper.map(integrationAddress, IntegrationAddressEntity.class);
        integrationAddressDao.updateIntegrationAddress(groupEntity);
    }

    /**
     * 查询单个系统集成地址
     * @param IntegrationAddressId 系统集成地址id
     * @return 系统集成地址信息
     */
    @Override
    public IntegrationAddress findOneIntegrationAddress(String IntegrationAddressId) {
        IntegrationAddressEntity groupEntity = integrationAddressDao.findOneIntegrationAddress(IntegrationAddressId);
        IntegrationAddress integrationAddress = BeanMapper.map(groupEntity, IntegrationAddress.class);
        return integrationAddress;
    }

    /**
     * 查询所有系统集成地址
     * @return 系统集成地址信息列表
     */
    @Override
    public List<IntegrationAddress> findAllIntegrationAddress() {
        List<IntegrationAddressEntity> groupEntityList = integrationAddressDao.findAllIntegrationAddress();
        List<IntegrationAddress> list = BeanMapper.mapList(groupEntityList, IntegrationAddress.class);
        if (list == null || list.isEmpty()){
            return Collections.emptyList();
        }
        return list;
    }


    @Override
    public List<IntegrationAddress> findAllIntegrationAddressList(List<String> idList) {
        List<IntegrationAddressEntity> groupEntities = integrationAddressDao.findAllIntegrationAddressList(idList);
        List<IntegrationAddress> list = BeanMapper.mapList(groupEntities, IntegrationAddress.class);
        return list;
    }

    @Override
    public List<IntegrationAddress> findIntegrationAddressList(IntegrationAddressQuery integrationAddressQuery) {
        List<IntegrationAddressEntity> integrationAddressEntity =  integrationAddressDao.findIntegrationAddressList(integrationAddressQuery);
        List<IntegrationAddress> integrationAddressList = BeanMapper.mapList(integrationAddressEntity, IntegrationAddress.class);

        return integrationAddressList;
    }

    @Override
    public IntegrationAddress findIntegrationAddress(String code) {
        IntegrationAddressQuery addressQuery = new IntegrationAddressQuery();
        addressQuery.setCode(code);
        List<IntegrationAddress> integrationAddressList = findIntegrationAddressList(addressQuery);
        if (CollectionUtils.isEmpty(integrationAddressList)){
            return null;
        }
        return integrationAddressList.get(0);
    }

}




















