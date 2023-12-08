package io.thoughtware.gittork.repository.dao;

import io.thoughtware.gittork.repository.model.RepositoryQuery;
import io.thoughtware.core.order.OrderBuilders;
import io.thoughtware.core.page.Pagination;
import io.thoughtware.dal.jdbc.JdbcTemplate;
import io.thoughtware.dal.jpa.JpaTemplate;
import io.thoughtware.dal.jpa.criterial.condition.QueryCondition;
import io.thoughtware.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.thoughtware.gittork.repository.entity.RepositoryEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RepositoryDao {

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建仓库
     * @param repositoryEntity 仓库信息
     * @return 仓库id
     */
    public String createRpy(RepositoryEntity repositoryEntity){
        return jpaTemplate.save(repositoryEntity, String.class);
    }

    /**
     * 删除仓库
     * @param rpyId 仓库id
     */
    public void deleteRpy(String rpyId){
        jpaTemplate.delete(RepositoryEntity.class,rpyId);
    }


    /**
     * 更新仓库
     * @param repositoryEntity 仓库信息
     */
    public void updateRpy(RepositoryEntity repositoryEntity){
        jpaTemplate.update(repositoryEntity);
    }

    /**
     * 查询单个仓库信息
     * @param rpyId 仓库id
     * @return 仓库信息
     */
    public RepositoryEntity findOneRpy(String rpyId){
        return jpaTemplate.findOne(RepositoryEntity.class,rpyId);
    }

    /**
     * 查询所有仓库
     * @return 仓库列表
     */
    public List<RepositoryEntity> findAllRpy(){
        return jpaTemplate.findAll(RepositoryEntity.class);
    }


    public List<RepositoryEntity> findAllRpyList(List<String> idList){
        return jpaTemplate.findList(RepositoryEntity.class,idList);
    }

    /**
     * 通过ids查询
     * @param idList
     * @return
     */
    public List<RepositoryEntity> findRepositoryListByIds(List<String> idList) {
        return jpaTemplate.findList(RepositoryEntity.class,idList);
    }

    /**
     * 条件查询仓库库
     * @param repositoryQuery
     */
    public List<RepositoryEntity> findRepositoryList(RepositoryQuery repositoryQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(RepositoryEntity.class)
                .eq("name", repositoryQuery.getName())
                .eq("userId", repositoryQuery.getUserId())
                .eq("groupId",repositoryQuery.getGroupId())
                .eq("address",repositoryQuery.getAddress())
                .get();
        return jpaTemplate.findList(queryCondition, RepositoryEntity.class);
    }

    /**
     * 通过ids查询
     * @param repositoryQuery
     * @return
     */
    public Pagination<RepositoryEntity> findRepositoryPage(RepositoryQuery repositoryQuery, String[] ids) {

        QueryBuilders pagination = QueryBuilders.createQuery(RepositoryEntity.class)
                .in("rpyId", ids);

        if (StringUtils.isNotEmpty(repositoryQuery.getSort())){
            if (repositoryQuery.getSort().equals("asc")){
                pagination.orders(OrderBuilders.instance().asc("size").get());
            }else {
                pagination.orders(OrderBuilders.instance().desc("size").get());
            }
        }
        QueryCondition queryCondition = pagination.pagination(repositoryQuery.getPageParam())
                .get();
        return jpaTemplate.findPage(queryCondition,RepositoryEntity.class);
    }

    /**
     * 通过地址模糊查询
     * @param address
     * @return
     */
    public List<RepositoryEntity> findRepositoryByAddress(String  address) {
        QueryCondition queryCondition = QueryBuilders.createQuery(RepositoryEntity.class)
                .eq("address", address)
                .get();
        return jpaTemplate.findList(queryCondition, RepositoryEntity.class);
    }

    public List<RepositoryEntity> findRepositoryByNamespace(String  namespace) {
        QueryCondition queryCondition = QueryBuilders.createQuery(RepositoryEntity.class)
                .like("address", namespace)
                .get();
        return jpaTemplate.findList(queryCondition, RepositoryEntity.class);
    }

    /**
     * 通过仓库组名字查询仓库列表
     * @param repositoryQuery
     * @return
     */
    public  List<RepositoryEntity> findGroupRepository(RepositoryQuery repositoryQuery) {
        String sql="SELECT re.* FROM rpy_repository re LEFT JOIN rpy_group gr ON re.group_id=gr.group_id WHERE gr.name ='"+repositoryQuery.getGroupName()+"'";
        if (StringUtils.isNotEmpty(repositoryQuery.getName())){
             sql = sql + " and re.name like '%" + repositoryQuery.getName() + "%'";
        }

        JdbcTemplate jdbcTemplate = jpaTemplate.getJdbcTemplate();
        List<RepositoryEntity> repositoryEntities = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(RepositoryEntity.class));
        return repositoryEntities;
    }

    /**
     * 通过仓库名字模糊查询仓库列表
     * @param repositoryQuery
     * @return
     */
    public List<RepositoryEntity> findRepositoryListLike(RepositoryQuery repositoryQuery) {
        QueryBuilders queryBuilders = QueryBuilders.createQuery(RepositoryEntity.class)
                .like("name", repositoryQuery.getName());
        if (("oneself").equals(repositoryQuery.getFindType())){
            queryBuilders.eq("userId", repositoryQuery.getUserId());
        }
        QueryCondition queryCondition = queryBuilders.get();
        return jpaTemplate.findList(queryCondition, RepositoryEntity.class);
    }
}




























































