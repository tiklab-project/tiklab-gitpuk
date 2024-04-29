package io.thoughtware.gittok.branch.service;

import io.thoughtware.core.page.Pagination;
import io.thoughtware.core.page.PaginationBuilder;
import io.thoughtware.dal.jpa.criterial.condition.DeleteCondition;
import io.thoughtware.dal.jpa.criterial.conditionbuilder.DeleteBuilders;
import io.thoughtware.gittok.branch.dao.RepositoryBranchDao;
import io.thoughtware.gittok.branch.entity.RepositoryBranchEntity;
import io.thoughtware.gittok.branch.model.Branch;
import io.thoughtware.gittok.branch.model.RepositoryBranch;
import io.thoughtware.gittok.branch.model.RepositoryBranchQuery;
import io.thoughtware.gittok.commit.service.MergeConditionService;
import io.thoughtware.gittok.common.git.GitBranchUntil;
import io.thoughtware.gittok.repository.entity.RecordCommitEntity;
import io.thoughtware.toolkit.beans.BeanMapper;
import io.thoughtware.toolkit.join.JoinTemplate;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

/**
* RepositoryBranchServiceImpl-仓库分支
*/
@Service
public class RepositoryBranchServiceImpl implements RepositoryBranchService {

    @Autowired
    RepositoryBranchDao repositoryBranchDao;

    @Autowired
    JoinTemplate joinTemplate;

    @Autowired
    MergeConditionService mergeConditionService;



    public void initRepositoryBranch(String repositoryId,String repositoryUrl,String userId) {

        try {
            //获取所有分支
            List<Branch> allBranch = GitBranchUntil.findAllBranch(repositoryUrl);
            List<RepositoryBranch> repositoryBranchList = this.findRepositoryBranchList(new RepositoryBranchQuery().setRepositoryId(repositoryId));
            for (Branch branch :allBranch){
                List<RepositoryBranch> branches = repositoryBranchList.stream().filter(a -> branch.getBranchId().equals(a.getBranchId())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(branches)){
                    RepositoryBranchEntity repositoryBranchEntity = new RepositoryBranchEntity();
                    repositoryBranchEntity.setBranchId(branch.getBranchId());
                    repositoryBranchEntity.setRepositoryId(repositoryId);
                    repositoryBranchEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
                    repositoryBranchEntity.setCreateUser(userId);
                    repositoryBranchDao.createRepositoryBranch(repositoryBranchEntity);
                }
            }
        }catch (Exception e){

        }

    }

    @Override
    @Transactional
    public String createRepositoryBranch(@NotNull @Valid RepositoryBranch repositoryBranch) {
        RepositoryBranchEntity repositoryBranchEntity = BeanMapper.map(repositoryBranch, RepositoryBranchEntity.class);
        repositoryBranchEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return repositoryBranchDao.createRepositoryBranch(repositoryBranchEntity);
    }

    @Override
    public void updateRepositoryBranch(@NotNull @Valid RepositoryBranch repositoryBranch) {
        RepositoryBranchEntity repositoryBranchEntity = BeanMapper.map(repositoryBranch, RepositoryBranchEntity.class);
        repositoryBranchDao.updateRepositoryBranch(repositoryBranchEntity);
    }

    @Override
    public void deleteRepositoryBranch(@NotNull String id) {

        repositoryBranchDao.deleteRepositoryBranch(id);
    }

    @Override
    public void deleteRepositoryBranch(String repositoryId,String branchName) {
        DeleteCondition deleteCondition = DeleteBuilders.createDelete(RepositoryBranchEntity.class)
                .eq("repositoryId", repositoryId)
                .eq("branchName",branchName)
                .get();
        repositoryBranchDao.deleteRepositoryBranch(deleteCondition);
    }


    @Override
    public RepositoryBranch findOne(String id) {
        RepositoryBranchEntity repositoryBranchEntity = repositoryBranchDao.findRepositoryBranch(id);

        RepositoryBranch repositoryBranch = BeanMapper.map(repositoryBranchEntity, RepositoryBranch.class);
        return repositoryBranch;
    }

    @Override
    public List<RepositoryBranch> findList(List<String> idList) {
        List<RepositoryBranchEntity> repositoryBranchEntityList =  repositoryBranchDao.findRepositoryBranchList(idList);

        List<RepositoryBranch> repositoryBranchList =  BeanMapper.mapList(repositoryBranchEntityList,RepositoryBranch.class);
        return repositoryBranchList;
    }

    @Override
    public RepositoryBranch findRepositoryBranch(@NotNull String id) {
        RepositoryBranch repositoryBranch = findOne(id);

        joinTemplate.joinQuery(repositoryBranch);

        return repositoryBranch;
    }

    @Override
    public List<RepositoryBranch> findAllRepositoryBranch() {
        List<RepositoryBranchEntity> repositoryBranchEntityList =  repositoryBranchDao.findAllRepositoryBranch();

        List<RepositoryBranch> repositoryBranchList =  BeanMapper.mapList(repositoryBranchEntityList,RepositoryBranch.class);

        joinTemplate.joinQuery(repositoryBranchList);

        return repositoryBranchList;
    }

    @Override
    public List<RepositoryBranch> findRepositoryBranchList(RepositoryBranchQuery repositoryBranchQuery) {
        List<RepositoryBranchEntity> repositoryBranchEntityList = repositoryBranchDao.findRepositoryBranchList(repositoryBranchQuery);

        List<RepositoryBranch> repositoryBranchList = BeanMapper.mapList(repositoryBranchEntityList,RepositoryBranch.class);

        joinTemplate.joinQuery(repositoryBranchList);

        return repositoryBranchList;
    }

    @Override
    public Pagination<RepositoryBranch> findRepositoryBranchPage(RepositoryBranchQuery repositoryBranchQuery) {

        Pagination<RepositoryBranchEntity>  pagination = repositoryBranchDao.findRepositoryBranchPage(repositoryBranchQuery);

        List<RepositoryBranch> repositoryBranchList = BeanMapper.mapList(pagination.getDataList(),RepositoryBranch.class);

        joinTemplate.joinQuery(repositoryBranchList);

        return PaginationBuilder.build(pagination,repositoryBranchList);
    }
}