package net.tiklab.xcode.code.dao;

import net.tiklab.dal.jpa.JpaTemplate;
import net.tiklab.xcode.code.entity.CodeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CodeDao {

    @Autowired
    JpaTemplate jpaTemplate;

    /**
     * 创建仓库
     * @param codeEntity 仓库信息
     * @return 仓库id
     */
    public String createCode(CodeEntity codeEntity){
        return jpaTemplate.save(codeEntity, String.class);
    }

    /**
     * 删除仓库
     * @param codeId 仓库id
     */
    public void deleteCode(String codeId){
        jpaTemplate.delete(CodeEntity.class,codeId);
    }


    /**
     * 更新仓库
     * @param codeEntity 仓库信息
     */
    public void updateCode(CodeEntity codeEntity){
        jpaTemplate.update(codeEntity);
    }

    /**
     * 查询单个仓库信息
     * @param codeId 仓库id
     * @return 仓库信息
     */
    public CodeEntity findOneCode(String codeId){
        return jpaTemplate.findOne(CodeEntity.class,codeId);
    }

    /**
     * 查询所有仓库
     * @return 仓库列表
     */
    public List<CodeEntity> findAllCode(){
        return jpaTemplate.findAll(CodeEntity.class);
    }


    public List<CodeEntity> findAllCodeList(List<String> idList){
        return jpaTemplate.findList(CodeEntity.class,idList);
    }



}




























































