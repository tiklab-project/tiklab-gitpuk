package io.tiklab.gitpuk.repository.service;

import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.DeleteBuilders;
import io.tiklab.gitpuk.repository.dao.IntRelevancyDao;
import io.tiklab.gitpuk.repository.entity.IntRelevancyEntity;
import io.tiklab.gitpuk.repository.model.IntRelevancy;
import io.tiklab.gitpuk.repository.model.IntRelevancyQuery;
import io.tiklab.rpc.annotation.Exporter;
import io.tiklab.toolkit.beans.BeanMapper;
import io.tiklab.toolkit.join.JoinTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@Service
@Exporter
public class IntRelevancyServerImpl implements IntRelevancyService {


    @Autowired
    private IntRelevancyDao intRelevancyDao;

    @Autowired
    private JoinTemplate joinTemplate;
    /**
     * 创建集成关联
     * @param intRelevancy 信息
     * @return 集成关联id
     */
    @Override
    public String createIntRelevancy(IntRelevancy intRelevancy) {
        intRelevancy.setCreateTime(new Timestamp(System.currentTimeMillis()));
        IntRelevancyEntity groupEntity = BeanMapper.map(intRelevancy, IntRelevancyEntity.class);

        //关联多个流水线
        List<String> relevancyIdList = intRelevancy.getRelevancyIdList();
        if (!CollectionUtils.isEmpty(intRelevancy.getRelevancyIdList())){
            for (String relevancyId:relevancyIdList){
                groupEntity.setRelevancyId(relevancyId);
                intRelevancyDao.createIntRelevancy(groupEntity);
            }
            return null;
        }
        return intRelevancyDao.createIntRelevancy(groupEntity);
    }

    /**
     * 删除集成关联
     * @param IntRelevancyId 集成关联id
     */
    @Override
    public void deleteIntRelevancy(String IntRelevancyId) {
        intRelevancyDao.deleteIntRelevancy(IntRelevancyId);
    }

    @Override
    public void deleteIntRelevancy(IntRelevancyQuery intRelevancyQuery) {
        DeleteCondition deleteCondition = DeleteBuilders.createDelete(IntRelevancyEntity.class)
                .eq("repositoryId", intRelevancyQuery.getRepositoryId())
                .eq("relevancyId",intRelevancyQuery.getRelevancyId())
                .get();
        intRelevancyDao.deleteIntRelevancy(deleteCondition);
    }

    /**
     * 更新集成关联
     * @param intRelevancy 集成关联信息
     */
    @Override
    public void updateIntRelevancy(IntRelevancy intRelevancy) {
        IntRelevancyEntity groupEntity = BeanMapper.map(intRelevancy, IntRelevancyEntity.class);
        intRelevancyDao.updateIntRelevancy(groupEntity);
    }

    /**
     * 查询单个集成关联
     * @param IntRelevancyId 集成关联id
     * @return 集成关联信息
     */
    @Override
    public IntRelevancy findOneIntRelevancy(String IntRelevancyId) {
        IntRelevancyEntity groupEntity = intRelevancyDao.findOneIntRelevancy(IntRelevancyId);
        IntRelevancy intRelevancy = BeanMapper.map(groupEntity, IntRelevancy.class);
        // joinTemplate.joinQuery(intRelevancy);
        return intRelevancy;
    }

    /**
     * 查询所有集成关联
     * @return 集成关联信息列表
     */
    @Override
    public List<IntRelevancy> findAllIntRelevancy() {
        List<IntRelevancyEntity> groupEntityList = intRelevancyDao.findAllIntRelevancy();
        List<IntRelevancy> list = BeanMapper.mapList(groupEntityList, IntRelevancy.class);
        // joinTemplate.joinQuery(list);
        if (list == null || list.isEmpty()){
            return Collections.emptyList();
        }
        return list;
    }


    @Override
    public List<IntRelevancy> findAllIntRelevancyList(List<String> idList) {
        List<IntRelevancyEntity> groupEntities = intRelevancyDao.findAllIntRelevancyList(idList);
        List<IntRelevancy> list = BeanMapper.mapList(groupEntities, IntRelevancy.class);
        // joinTemplate.joinQuery(list);
        return list;
    }

    @Override
    public List<IntRelevancy> findIntRelevancyList(IntRelevancyQuery intRelevancyQuery) {
        List<IntRelevancyEntity> intRelevancyEntity =  intRelevancyDao.findIntRelevancyList(intRelevancyQuery);
        List<IntRelevancy> intRelevancyList = BeanMapper.mapList(intRelevancyEntity, IntRelevancy.class);

        joinTemplate.joinQuery(intRelevancyList);


        return intRelevancyList;
    }
}




















