package io.thoughtware.gittork.repository.dao;

import io.thoughtware.gittork.repository.model.RemoteInfoQuery;
import io.thoughtware.dal.jpa.JpaTemplate;
import io.thoughtware.dal.jpa.criterial.condition.QueryCondition;
import io.thoughtware.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.thoughtware.gittork.repository.entity.RemoteInfoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RemoteInfoDao {

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建
     * @param remoteInfoEntity
     * @return 
     */
    public String createRemoteInfo(RemoteInfoEntity remoteInfoEntity){
        return jpaTemplate.save(remoteInfoEntity, String.class);
    }

    /**
     * 删除
     * @param rpyId id
     */
    public void deleteRemoteInfo(String rpyId){
        jpaTemplate.delete(RemoteInfoEntity.class,rpyId);
    }


    /**
     * 更新
     * @param remoteInfoEntity 信息
     */
    public void updateRemoteInfo(RemoteInfoEntity remoteInfoEntity){
        jpaTemplate.update(remoteInfoEntity);
    }

    /**
     * 查询单个
     * @param id id
     * @return 信息
     */
    public RemoteInfoEntity findOneRemoteInfo(String id){
        return jpaTemplate.findOne(RemoteInfoEntity.class,id);
    }

    /**
     * 查询所有
     * @return 列表
     */
    public List<RemoteInfoEntity> findAllRemoteInfo(){
        return jpaTemplate.findAll(RemoteInfoEntity.class);
    }


    public List<RemoteInfoEntity> findAllRemoteInfoList(List<String> idList){
        return jpaTemplate.findList(RemoteInfoEntity.class,idList);
    }

    /**
     * 通过ids查询
     * @param idList
     * @return
     */
    public List<RemoteInfoEntity> findRepositoryListByIds(List<String> idList) {
        return jpaTemplate.findList(RemoteInfoEntity.class,idList);
    }

    /**
     * 条件查询库
     * @param remoteInfoQuery
     * @return List <RemoteInfoEntity>
     */
    public List<RemoteInfoEntity> findRepositoryList(RemoteInfoQuery remoteInfoQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(RemoteInfoEntity.class)
                .eq("authWay", remoteInfoQuery.getAuthWay())
                .eq("rpyId", remoteInfoQuery.getRpyId())
                .orders(remoteInfoQuery.getOrderParams())
                .get();
        return jpaTemplate.findList(queryCondition, RemoteInfoEntity.class);
    }




}




























































