package io.tiklab.gitpuk.repository.dao;

import io.tiklab.gitpuk.repository.entity.RecordCommitEntity;
import io.tiklab.gitpuk.repository.model.RecordCommitQuery;
import io.tiklab.core.page.Pagination;
import io.tiklab.dal.jpa.JpaTemplate;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.condition.QueryCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.QueryBuilders;
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
 * RecordCommitDao-插件数据访问
 */
@Repository
public class RecordCommitDao {

    private static Logger logger = LoggerFactory.getLogger(RecordCommitDao.class);

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建
     * @param recordCommitEntity
     * @return
     */
    public String createRecordCommit(RecordCommitEntity recordCommitEntity) {
        return jpaTemplate.save(recordCommitEntity,String.class);
    }

    /**
     * 更新
     * @param recordCommitEntity
     */
    public void updateRecordCommit(RecordCommitEntity recordCommitEntity){
        jpaTemplate.update(recordCommitEntity);
    }

    /**
     * 删除
     * @param id
     */
    public void deleteRecordCommit(String id){
        jpaTemplate.delete(RecordCommitEntity.class,id);
    }

    public void deleteRecordCommit(DeleteCondition deleteCondition){
        jpaTemplate.delete(deleteCondition);
    }

    /**
     * 查找
     * @param id
     * @return
     */
    public RecordCommitEntity findRecordCommit(String id){
        return jpaTemplate.findOne(RecordCommitEntity.class,id);
    }

    /**
    * findAllRecordCommit
    * @return
    */
    public List<RecordCommitEntity> findAllRecordCommit() {
        return jpaTemplate.findAll(RecordCommitEntity.class);
    }

    /**
     * 通过ids查询
     * @param idList
     * @return
     */
    public List<RecordCommitEntity> findRecordCommitList(List<String> idList) {
        return jpaTemplate.findList(RecordCommitEntity.class,idList);
    }

    /**
     * 条件查询插件
     * @param recordCommitQuery
     * @return
     */
    public List<RecordCommitEntity> findRecordCommitList(RecordCommitQuery recordCommitQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(RecordCommitEntity.class)
                .eq("userId", recordCommitQuery.getUserId())
                .eq("repositoryId", recordCommitQuery.getRepositoryId())
                .get();
        return jpaTemplate.findList(queryCondition, RecordCommitEntity.class);
    }

    /**
     * 条件分页查询插件
     * @param recordCommitQuery
     * @return
     */
    public Pagination<RecordCommitEntity> findRecordCommitPage(RecordCommitQuery recordCommitQuery) {
        QueryCondition queryCondition = QueryBuilders.createQuery(RecordCommitEntity.class)
                .eq("userId", recordCommitQuery.getUserId())
                .eq("repositoryId", recordCommitQuery.getRepositoryId())
                .get();
        return jpaTemplate.findPage(queryCondition, RecordCommitEntity.class);
    }

    /**
     * 查询时间段内的提交
     * @param startTime 开始时间
     * @param endTime 结束时间
     */
    public List<RecordCommitEntity> findTimeRecordCommitList(String startTime, String endTime,String rpyId) {
        String sql = "SELECT * FROM  rpy_record_commit WHERE  commit_time BETWEEN '"+startTime+"' AND '"+endTime+"'" ;

        if (!StringUtils.isEmpty(rpyId)){
            sql=sql+" and repository_id= '"+rpyId+"'";
        }

        NamedParameterJdbcTemplate jdbc = new NamedParameterJdbcTemplate(jpaTemplate.getJdbcTemplate());
        Map<String, Object> paramMap = new HashMap<String, Object>();
        List<RecordCommitEntity> list = jdbc.query(sql, paramMap, new BeanPropertyRowMapper(RecordCommitEntity.class));
        return list;
    }
}