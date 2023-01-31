package net.tiklab.xcode.branch.dao;

import net.tiklab.dal.jpa.JpaTemplate;
import net.tiklab.xcode.branch.entity.CodeBranchEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CodeBranchDao {

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建分支
     * @param codeBranchEntity 分支信息
     * @return 分支id
     */
    public String createCodeBranch(CodeBranchEntity codeBranchEntity){
        return jpaTemplate.save(codeBranchEntity, String.class);
    }

    /**
     * 删除分支
     * @param codeBranchId 分支id
     */
    public void deleteCodeBranch(String codeBranchId){
        jpaTemplate.delete(CodeBranchEntity.class,codeBranchId);
    }


    /**
     * 更新分支
     * @param codeBranchEntity 分支信息
     */
    public void updateCodeBranch(CodeBranchEntity codeBranchEntity){
        jpaTemplate.update(codeBranchEntity);
    }

    /**
     * 查询单个分支信息
     * @param codeBranchId 分支id
     * @return 分支信息
     */
    public CodeBranchEntity findOneCodeBranch(String codeBranchId){
        return jpaTemplate.findOne(CodeBranchEntity.class,codeBranchId);
    }

    /**
     * 查询所有分支
     * @return 分支列表
     */
    public List<CodeBranchEntity> findAllCodeBranch(){
        return jpaTemplate.findAll(CodeBranchEntity.class);
    }


    public List<CodeBranchEntity> findAllCodeBranchList(List<String> idList){
        return jpaTemplate.findList(CodeBranchEntity.class,idList);
    }
    
    
}
