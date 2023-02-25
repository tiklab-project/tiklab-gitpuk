package net.tiklab.xcode.branch.dao;

import net.tiklab.dal.jpa.JpaTemplate;
import net.tiklab.xcode.branch.entity.BranchEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BranchDao {

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建分支
     * @param branchEntity 分支信息
     * @return 分支id
     */
    public String createCodeBranch(BranchEntity branchEntity){
        return jpaTemplate.save(branchEntity, String.class);
    }

    /**
     * 删除分支
     * @param codeBranchId 分支id
     */
    public void deleteCodeBranch(String codeBranchId){
        jpaTemplate.delete(BranchEntity.class,codeBranchId);
    }


    /**
     * 更新分支
     * @param branchEntity 分支信息
     */
    public void updateCodeBranch(BranchEntity branchEntity){
        jpaTemplate.update(branchEntity);
    }

    /**
     * 查询单个分支信息
     * @param codeBranchId 分支id
     * @return 分支信息
     */
    public BranchEntity findOneCodeBranch(String codeBranchId){
        return jpaTemplate.findOne(BranchEntity.class,codeBranchId);
    }

    /**
     * 查询所有分支
     * @return 分支列表
     */
    public List<BranchEntity> findAllCodeBranch(){
        return jpaTemplate.findAll(BranchEntity.class);
    }


    public List<BranchEntity> findAllCodeBranchList(List<String> idList){
        return jpaTemplate.findList(BranchEntity.class,idList);
    }
    
    
}
