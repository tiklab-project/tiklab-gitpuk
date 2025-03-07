package io.tiklab.gitpuk.merge.dao;

import io.tiklab.core.page.Pagination;
import io.tiklab.dal.jpa.JpaTemplate;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.condition.QueryCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.QueryBuilders;
import io.tiklab.gitpuk.merge.entity.MergeRequestEntity;
import io.tiklab.gitpuk.merge.model.MergeRequestQuery;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MergeRequestDao-合并请求数据库访问
 */
@Repository
public class MergeRequestDao {

    private static Logger logger = LoggerFactory.getLogger(MergeRequestDao.class);

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建
     * @param mergeRequestEntity
     * @return
     */
    public String createMergeRequest(MergeRequestEntity mergeRequestEntity) {
        return jpaTemplate.save(mergeRequestEntity,String.class);
    }

    /**
     * 更新
     * @param mergeRequestEntity
     */
    public void updateMergeRequest(MergeRequestEntity mergeRequestEntity){
        jpaTemplate.update(mergeRequestEntity);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteMergeRequest(String id){
        jpaTemplate.delete(MergeRequestEntity.class,id);
    }

    /**
     * 条件删除合并请求
     * @param deleteCondition
     */
    public void deleteMergeRequest(DeleteCondition deleteCondition){
        jpaTemplate.delete(deleteCondition);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public MergeRequestEntity findMergeRequest(String id){
        return jpaTemplate.findOne(MergeRequestEntity.class,id);
    }

    /**
    * 查询所有漏洞
    * @return
    */
    public List<MergeRequestEntity> findAllMergeRequest() {
        return jpaTemplate.findAll(MergeRequestEntity.class);
    }

    /**
     * 通过ids查询漏洞
     * @param idList
     * @return List <MergeRequestEntity>
     */
    public List<MergeRequestEntity> findMergeRequestList(List<String> idList) {
        return jpaTemplate.findList(MergeRequestEntity.class,idList);
    }

    /**
     * 条件查询合并请求
     * @param mergeRequestQuery
     * @return List <MergeRequestEntity>
     */
    public List<MergeRequestEntity> findMergeRequestList(MergeRequestQuery mergeRequestQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(MergeRequestEntity.class)
                .eq("rpyId", mergeRequestQuery.getRpyId())
                .eq("mergeState",mergeRequestQuery.getMergeState())
                .eq("mergeOrigin", mergeRequestQuery.getMergeOrigin())
                .eq("mergeTarget",mergeRequestQuery.getMergeTarget())
                .like("title",mergeRequestQuery.getTitle())
                .orders(mergeRequestQuery.getOrderParams())
                .get();
        return jpaTemplate.findList(queryCondition,MergeRequestEntity.class);
    }

    /**
     * 条件分页查询合并请求
     * @param mergeRequestQuery
     * @return Pagination <MergeRequestEntity>
     */
    public Pagination<MergeRequestEntity> findMergeRequestPage(MergeRequestQuery mergeRequestQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(MergeRequestEntity.class)
                .eq("rpyId", mergeRequestQuery.getRpyId())
                .eq("mergeState",mergeRequestQuery.getMergeState())
                .eq("mergeOrigin", mergeRequestQuery.getMergeOrigin())
                .eq("mergeTarget",mergeRequestQuery.getMergeTarget())
                .like("title",mergeRequestQuery.getTitle())
                .orders(mergeRequestQuery.getOrderParams())
                .pagination(mergeRequestQuery.getPageParam())
                .get();


        return jpaTemplate.findPage(queryCondition,MergeRequestEntity.class);
    }
    public List<MergeRequestEntity> findTimeMergeRequestList(MergeRequestQuery mergeRequestQuery) {
        String sql =  " SELECT * FROM rpy_merge_request WHERE create_time BETWEEN '"+mergeRequestQuery.getStartTime()+"' AND '"+mergeRequestQuery.getEndTime()+"' ";

        if (!StringUtils.isEmpty(mergeRequestQuery.getRpyId())){
            sql=sql+" and rpy_id= '"+mergeRequestQuery.getRpyId()+"'";
        }

        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(jpaTemplate.getJdbcTemplate());

        Map<String, Object> paramMap = new HashMap<String, Object>();
        List<MergeRequestEntity> list = jdbc.query(sql, paramMap, new BeanPropertyRowMapper(MergeRequestEntity.class));

        return list;

    }


}