package io.thoughtware.gittok.scan.service;

import io.thoughtware.gittok.scan.model.CodeScanInstance;
import io.thoughtware.gittok.scan.model.CodeScanInstanceQuery;
import io.thoughtware.toolkit.beans.BeanMapper;
import io.thoughtware.core.page.Pagination;
import io.thoughtware.core.page.PaginationBuilder;
import io.thoughtware.dal.jpa.criterial.condition.DeleteCondition;
import io.thoughtware.dal.jpa.criterial.conditionbuilder.DeleteBuilders;
import io.thoughtware.toolkit.join.JoinTemplate;
import io.thoughtware.rpc.annotation.Exporter;
import io.thoughtware.gittok.scan.dao.CodeScanInstanceDao;
import io.thoughtware.gittok.scan.entity.CodeScanInstanceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

/**
* CodeScanInstanceServiceImpl-打开仓库的记录接口实现
*/
@Service
@Exporter
public class CodeScanInstanceImpl implements CodeScanInstanceService {

    @Autowired
    CodeScanInstanceDao codeScanInstanceDao;


    @Autowired
    JoinTemplate joinTemplate;


    @Override
    public String createCodeScanInstance(@NotNull @Valid CodeScanInstance codeScanInstance) {

        CodeScanInstanceEntity codeScanInstanceEntity = BeanMapper.map(codeScanInstance, CodeScanInstanceEntity.class);
        codeScanInstanceEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        String codeScanInstanceId= codeScanInstanceDao.createCodeScanInstance(codeScanInstanceEntity);

        return codeScanInstanceId;
    }

    @Override
    public void updateCodeScanInstance(@NotNull @Valid CodeScanInstance codeScanInstance) {
        CodeScanInstanceEntity codeScanInstanceEntity = BeanMapper.map(codeScanInstance, CodeScanInstanceEntity.class);

        codeScanInstanceDao.updateCodeScanInstance(codeScanInstanceEntity);
    }

    @Override
    public void deleteCodeScanInstance(@NotNull String id) {
        codeScanInstanceDao.deleteCodeScanInstance(id);
    }

    @Override
    public void deleteCodeScanInstanceByRecord(String repositoryId) {
        DeleteCondition deleteCondition = DeleteBuilders.createDelete(CodeScanInstanceEntity.class)
                .eq("repositoryId", repositoryId)
                .get();
        codeScanInstanceDao.deleteCodeScanInstance(deleteCondition);
    }

    @Override
    public CodeScanInstance findOne(String id) {
        CodeScanInstanceEntity codeScanInstanceEntity = codeScanInstanceDao.findCodeScanInstance(id);

        CodeScanInstance codeScanInstance = BeanMapper.map(codeScanInstanceEntity, CodeScanInstance.class);
        return codeScanInstance;
    }

    @Override
    public List<CodeScanInstance> findList(List<String> idList) {
        List<CodeScanInstanceEntity> codeScanInstanceEntityList =  codeScanInstanceDao.findCodeScanInstanceList(idList);

        List<CodeScanInstance> codeScanInstanceList =  BeanMapper.mapList(codeScanInstanceEntityList, CodeScanInstance.class);
        return codeScanInstanceList;
    }

    @Override
    public CodeScanInstance findCodeScanInstance(@NotNull String id) {
        CodeScanInstance codeScanInstance = findOne(id);

        joinTemplate.joinQuery(codeScanInstance);

        return codeScanInstance;
    }

    @Override
    public List<CodeScanInstance> findAllCodeScanInstance() {
        List<CodeScanInstanceEntity> codeScanInstanceEntityList =  codeScanInstanceDao.findAllCodeScanInstance();

        List<CodeScanInstance> codeScanInstanceList =  BeanMapper.mapList(codeScanInstanceEntityList, CodeScanInstance.class);

        joinTemplate.joinQuery(codeScanInstanceList);

        return codeScanInstanceList;
    }

    @Override
    public List<CodeScanInstance> findCodeScanInstanceList(CodeScanInstanceQuery CodeScanInstanceQuery) {
        List<CodeScanInstanceEntity> codeScanInstanceEntityList = codeScanInstanceDao.findCodeScanInstanceList(CodeScanInstanceQuery);

        List<CodeScanInstance> codeScanInstanceList = BeanMapper.mapList(codeScanInstanceEntityList, CodeScanInstance.class);



        joinTemplate.joinQuery(codeScanInstanceList);

        return codeScanInstanceList;
    }

    @Override
    public Pagination<CodeScanInstance> findCodeScanInstancePage(CodeScanInstanceQuery CodeScanInstanceQuery) {
        Pagination<CodeScanInstanceEntity>  pagination = codeScanInstanceDao.findCodeScanInstancePage(CodeScanInstanceQuery);

        List<CodeScanInstance> codeScanInstanceList = BeanMapper.mapList(pagination.getDataList(), CodeScanInstance.class);
        joinTemplate.joinQuery(pagination.getDataList());

        return PaginationBuilder.build(pagination,codeScanInstanceList);
    }





}