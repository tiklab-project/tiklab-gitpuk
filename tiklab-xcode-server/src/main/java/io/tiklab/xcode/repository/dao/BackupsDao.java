package io.tiklab.xcode.repository.dao;

import io.tiklab.dal.jpa.JpaTemplate;
import io.tiklab.xcode.repository.entity.BackupsEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BackupsDao {

    private static Logger logger = LoggerFactory.getLogger(BackupsDao.class);

    @Autowired
    JpaTemplate jpaTemplate;


    /**
     * 更新
     * @param backupsEntity
     */
    public void updateBackups(BackupsEntity backupsEntity){
        jpaTemplate.update(backupsEntity);
    }


    /**
     * findAllBackups
     * @return
     */
    public List<BackupsEntity> findAllBackups() {
        return jpaTemplate.findAll(BackupsEntity.class);
    }


}
