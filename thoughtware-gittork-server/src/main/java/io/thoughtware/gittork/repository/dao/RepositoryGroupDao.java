package io.thoughtware.gittork.repository.dao;

import io.thoughtware.gittork.repository.model.RepositoryGroupQuery;
import io.thoughtware.core.page.Page;
import io.thoughtware.core.page.Pagination;
import io.thoughtware.dal.jpa.JpaTemplate;
import io.thoughtware.dal.jpa.criterial.condition.QueryCondition;
import io.thoughtware.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.thoughtware.gittork.repository.entity.RepositoryGroupEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RepositoryGroupDao {

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建仓库组
     * @param repositoryGroupEntity 仓库组信息
     * @return 仓库组id
     */
    public String createCodeGroup(RepositoryGroupEntity repositoryGroupEntity){
        return jpaTemplate.save(repositoryGroupEntity, String.class);
    }

    /**
     * 查询单个仓库信息
     * @param groupId 仓库组id
     * @return 仓库信息
     */
    public RepositoryGroupEntity findRepositoryGroup(String groupId){
        return jpaTemplate.findOne(RepositoryGroupEntity.class,groupId);
    }


    /**
     * 删除仓库组
     * @param codeGroupId 仓库组id
     */
    public void deleteCodeGroup(String codeGroupId){
        jpaTemplate.delete(RepositoryGroupEntity.class,codeGroupId);
    }


    /**
     * 更新仓库组
     * @param repositoryGroupEntity 仓库组信息
     */
    public void updateCodeGroup(RepositoryGroupEntity repositoryGroupEntity){
        jpaTemplate.update(repositoryGroupEntity);
    }

    /**
     * 查询单个仓库组信息
     * @param codeGroupId 仓库组id
     * @return 仓库组信息
     */
    public RepositoryGroupEntity findOneCodeGroup(String codeGroupId){
        return jpaTemplate.findOne(RepositoryGroupEntity.class,codeGroupId);
    }

    /**
     * 查询所有仓库组
     * @return 仓库组列表
     */
    public List<RepositoryGroupEntity> findAllCodeGroup(){
        return jpaTemplate.findAll(RepositoryGroupEntity.class);
    }


    public List<RepositoryGroupEntity> findAllCodeGroupList(List<String> idList){
        return jpaTemplate.findList(RepositoryGroupEntity.class,idList);
    }

    /**
     * 通过ids查询
     * @param repositoryGroupQuery
     * @return
     */
    public Pagination<RepositoryGroupEntity> findRepositoryGroupPage(RepositoryGroupQuery repositoryGroupQuery, List ids){
        Pagination pagination = new Pagination();
        Page pageParam = repositoryGroupQuery.getPageParam();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("groupIds", ids);
        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(jpaTemplate.getJdbcTemplate());
        int integer = ids.size();


        pagination.setTotalRecord(integer);
        double result = Math.ceil(integer/pageParam.getPageSize());
        pagination.setTotalPage((int) result);
        int offset = (pageParam.getCurrentPage() - 1) * pageParam.getPageSize();

        //查询数据
        String sql="SELECT gr.group_id ,gr.name,gr.rules,gr.color,count(re.rpy_id) AS repositoryNum,gr.user_id  FROM rpy_group gr LEFT  JOIN rpy_repository re ON gr.group_id=re.group_id " +
                " where gr.group_id in (:groupIds) GROUP BY gr.group_id,gr.name,gr.rules,gr.user_id,gr.color LIMIT " +pageParam.getPageSize()+" offset "+offset;

        List query = jdbc.query(sql, paramMap, new BeanPropertyRowMapper(RepositoryGroupEntity.class));
        pagination.setDataList(query);
        return  pagination;
    }

    /**
     * 条件查询仓库库
     * @param name
     */
    public List<RepositoryGroupEntity> findRepositoryByName(String name) {
        QueryCondition queryCondition = QueryBuilders.createQuery(RepositoryGroupEntity.class)
                .eq("name", name)
                .get();
        return jpaTemplate.findList(queryCondition, RepositoryGroupEntity.class);
    }

     /* *
     * 通过仓库名字模糊查询仓库列表
     * @param repositoryGroupQuery
     * @return*/

    public List<RepositoryGroupEntity> findRepositoryListLike(RepositoryGroupQuery repositoryGroupQuery) {
        QueryBuilders queryBuilders = QueryBuilders.createQuery(RepositoryGroupEntity.class)
                .like("name", repositoryGroupQuery.getName());
        if (("oneself").equals(repositoryGroupQuery.getFindType())){
            queryBuilders.eq("userId", repositoryGroupQuery.getUserId());
        }
        QueryCondition queryCondition = queryBuilders.get();
        return jpaTemplate.findList(queryCondition, RepositoryGroupEntity.class);
    }



    /**
     * 查询所有
     */
    public List<RepositoryGroupEntity> findAllGroup() {
        return jpaTemplate.findAll(RepositoryGroupEntity.class);
    }

    /**
     * 查询自己创建的仓库组
     */
    public List<RepositoryGroupEntity> findCanCreateRpyGroup(String userId) {
        QueryCondition queryCondition = QueryBuilders.createQuery(RepositoryGroupEntity.class)
                .eq("userId",userId)
                .get();
        return jpaTemplate.findList(queryCondition, RepositoryGroupEntity.class);
    }
}
