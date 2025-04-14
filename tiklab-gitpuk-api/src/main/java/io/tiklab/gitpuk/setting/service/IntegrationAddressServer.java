package io.tiklab.gitpuk.setting.service;

import io.tiklab.gitpuk.setting.model.IntegrationAddress;
import io.tiklab.gitpuk.setting.model.IntegrationAddressQuery;
import io.tiklab.toolkit.join.annotation.FindAll;
import io.tiklab.toolkit.join.annotation.FindList;
import io.tiklab.toolkit.join.annotation.FindOne;
import io.tiklab.toolkit.join.annotation.JoinProvider;

import java.util.List;

@JoinProvider(model = IntegrationAddress.class)
public interface IntegrationAddressServer {


    /**
     * 创建集成系统地址
     * @param integrationAddress 信息
     * @return 集成系统地址id
     */
    String createIntegrationAddress(IntegrationAddress integrationAddress);

    /**
     * 删除集成系统地址
     * @param IntegrationAddressId 集成系统地址id
     */
    void deleteIntegrationAddress(String IntegrationAddressId);

    /**
     * 更新集成系统地址
     * @param integrationAddress 集成系统地址信息
     */
    void updateIntegrationAddress(IntegrationAddress integrationAddress);

    /**
     * 查询单个集成系统地址
     * @param IntegrationAddressId 集成系统地址id
     * @return 集成系统地址信息
     */
    @FindOne
    IntegrationAddress findOneIntegrationAddress(String IntegrationAddressId);

    /**
     * 查询所有集成系统地址
     * @return 集成系统地址信息列表
     */
    @FindAll
    List<IntegrationAddress> findAllIntegrationAddress();


    @FindList
    List<IntegrationAddress> findAllIntegrationAddressList(List<String> idList);


    /**
     * t条件查询集成系统地址信息
     * @return 集成系统地址集合
     */
    List<IntegrationAddress> findIntegrationAddressList(IntegrationAddressQuery integrationAddressQuery);

    /**
     * 通过code查询集成系统地址信息
     * @return 集成系统地址集合
     */
    IntegrationAddress findIntegrationAddress(String code);
}









































