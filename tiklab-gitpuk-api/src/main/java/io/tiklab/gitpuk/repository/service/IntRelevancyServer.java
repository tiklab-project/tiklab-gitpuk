package io.tiklab.gitpuk.repository.service;

import io.tiklab.gitpuk.repository.model.IntRelevancy;
import io.tiklab.gitpuk.repository.model.IntRelevancyQuery;
import io.tiklab.toolkit.join.annotation.FindAll;
import io.tiklab.toolkit.join.annotation.FindList;
import io.tiklab.toolkit.join.annotation.FindOne;
import io.tiklab.toolkit.join.annotation.JoinProvider;

import java.util.List;

@JoinProvider(model = IntRelevancy.class)
public interface IntRelevancyServer {


    /**
     * 创建集成关联
     * @param intRelevancy 信息
     * @return 集成关联id
     */
    String createIntRelevancy(IntRelevancy intRelevancy);

    /**
     * 删除集成关联
     * @param IntRelevancyId 集成关联id
     */
    void deleteIntRelevancy(String IntRelevancyId);

    /**
     * 删除集成关联
     * @param intRelevancyQuery intRelevancyQuery
     */
    void deleteIntRelevancy(IntRelevancyQuery intRelevancyQuery);

    /**
     * 更新集成关联
     * @param intRelevancy 集成关联信息
     */
    void updateIntRelevancy(IntRelevancy intRelevancy);

    /**
     * 查询单个集成关联
     * @param IntRelevancyId 集成关联id
     * @return 集成关联信息
     */
    @FindOne
    IntRelevancy findOneIntRelevancy(String IntRelevancyId);

    /**
     * 查询所有集成关联
     * @return 集成关联信息列表
     */
    @FindAll
    List<IntRelevancy> findAllIntRelevancy();


    @FindList
    List<IntRelevancy> findAllIntRelevancyList(List<String> idList);


    /**
     * t条件查询集成关联信息
     * @return 集成关联集合
     */
    List<IntRelevancy> findIntRelevancyList(IntRelevancyQuery intRelevancyQuery);
}









































