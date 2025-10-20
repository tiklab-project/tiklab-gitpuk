package io.tiklab.gitpuk.repository.service;

import io.tiklab.core.exception.SystemException;
import io.tiklab.core.page.Pagination;
import io.tiklab.core.page.PaginationBuilder;
import io.tiklab.dal.jpa.criterial.condition.DeleteCondition;
import io.tiklab.dal.jpa.criterial.conditionbuilder.DeleteBuilders;
import io.tiklab.gitpuk.common.GitPukYamlDataMaService;
import io.tiklab.gitpuk.common.RepositoryUtil;
import io.tiklab.gitpuk.repository.dao.RepositoryForkDao;
import io.tiklab.gitpuk.repository.entity.RepositoryForkEntity;
import io.tiklab.gitpuk.repository.model.*;
import io.tiklab.rpc.annotation.Exporter;
import io.tiklab.toolkit.beans.BeanMapper;
import io.tiklab.toolkit.join.JoinTemplate;
import io.tiklab.user.user.model.User;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
* RepositoryForkServiceImpl 仓库fork
*/
@Service
@Exporter
public class RepositoryForkServiceImpl implements RepositoryForkService {
    private static Logger logger = LoggerFactory.getLogger(RepositoryForkServiceImpl.class);

    @Autowired
    RepositoryForkDao repositoryForkDao;

    @Autowired
    RepositoryService repositoryService;


    @Autowired
    JoinTemplate joinTemplate;

    @Autowired
    GitPukYamlDataMaService yamlDataMaService;

    public static final Map<String,Integer> ForkState = new HashMap();


    @Override
    public String execRepositoryFork(RepositoryFork repositoryFork) {
        ForkState.put(repositoryFork.getRepositoryId(),0);
        Repository rpy = repositoryService.findOne(repositoryFork.getRepositoryId());
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(new Runnable(){
            @Override
            public void run() {
                //创建fork仓库
                Repository repository = new Repository();
                repository.setName(rpy.getName());
                repository.setType(rpy.getType());
                repository.setRules(rpy.getRules());
                repository.setSize(rpy.getSize());
                repository.setIsReadme(rpy.getIsReadme());
                repository.setUser(repositoryFork.getUser());
                repository.setAddress(repositoryFork.getRepAddress());
                repository.setGroup(new RepositoryGroup().setGroupId(repositoryFork.getGroupId()));
                String rpyId = repositoryService.createRpy(repository);

                 try {
                     //仓库地址
                     String rpyAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(), repositoryFork.getRepositoryId());

                     //fork仓库的地址
                     String forkAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(), rpyId);

                     File sourceDir = new File(rpyAddress);
                    File targetDir = new File(forkAddress);
                    FileUtils.copyDirectory(sourceDir, targetDir);

                    //创建fork记录
                    repositoryFork.setRepository(new Repository().setRpyId(rpyId));
                    createRepositoryFork(repositoryFork);
                    ForkState.put(repositoryFork.getRepositoryId(),200);
                }catch (Exception e){
                     logger.info("fork仓库失败："+e.getMessage());
                     ForkState.put(repositoryFork.getRepositoryId(),400);
                    throw new SystemException(400,"fork复制仓库失败");
                }
            }});
        return "ok";
    }

    @Override
    public Integer findForkResult(String repositoryId) {
        Integer integer = ForkState.get(repositoryId);
        if (integer==200){
            ForkState.remove(repositoryId);
        }
        return integer;
    }


    @Override
    public String createRepositoryFork(@NotNull @Valid RepositoryFork repositoryFork) {

        RepositoryForkEntity repositoryForkEntity = BeanMapper.map(repositoryFork, RepositoryForkEntity.class);
        repositoryForkEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));

        String repositoryForkId= repositoryForkDao.createRepositoryFork(repositoryForkEntity);
        return repositoryForkId;
    }

    @Override
    public void updateRepositoryFork(@NotNull @Valid RepositoryFork repositoryFork) {
        RepositoryForkEntity repositoryForkEntity = BeanMapper.map(repositoryFork, RepositoryForkEntity.class);

        repositoryForkDao.updateRepositoryFork(repositoryForkEntity);
    }

    @Override
    public void deleteRepositoryFork(@NotNull String id) {
        repositoryForkDao.deleteRepositoryFork(id);
    }

    @Override
    public void deleteRepForkByRpyId(String forkRepId) {
        DeleteCondition deleteCondition = DeleteBuilders.createDelete(RepositoryForkEntity.class)
                .eq("forkRepositoryId", forkRepId)
                .get();
        repositoryForkDao.deleteRepositoryFork(deleteCondition);
    }






    @Override
    public RepositoryFork findOne(String id) {
        RepositoryForkEntity repositoryForkEntity = repositoryForkDao.findRepositoryFork(id);

        RepositoryFork repositoryFork = BeanMapper.map(repositoryForkEntity, RepositoryFork.class);
        return repositoryFork;
    }

    @Override
    public List<RepositoryFork> findList(List<String> idList) {
        List<RepositoryForkEntity> repositoryForkEntityList =  repositoryForkDao.findRepositoryForkList(idList);

        List<RepositoryFork> repositoryForkList =  BeanMapper.mapList(repositoryForkEntityList, RepositoryFork.class);
        return repositoryForkList;
    }

    @Override
    public RepositoryFork findRepositoryFork(@NotNull String id) {
        RepositoryFork repositoryFork = findOne(id);

        joinTemplate.joinQuery(repositoryFork,new String[]{"user"});

        return repositoryFork;
    }

    @Override
    public List<RepositoryFork> findAllRepositoryFork() {
        List<RepositoryForkEntity> repositoryForkEntityList =  repositoryForkDao.findAllRepositoryFork();

        List<RepositoryFork> repositoryForkList =  BeanMapper.mapList(repositoryForkEntityList, RepositoryFork.class);

        joinTemplate.joinQuery(repositoryForkList,new String[]{"user"});


        return repositoryForkList;
    }

    @Override
    public List<RepositoryFork> findRepositoryForkList(RepositoryForkQuery RepositoryForkQuery) {
        List<RepositoryForkEntity> repositoryForkEntityList = repositoryForkDao.findRepositoryForkList(RepositoryForkQuery);

        List<RepositoryFork> repositoryForkList = BeanMapper.mapList(repositoryForkEntityList, RepositoryFork.class);

        return repositoryForkList;
    }

    @Override
    public Pagination<RepositoryFork> findRepositoryForkPage(RepositoryForkQuery repositoryForkQuery) {
        Pagination<RepositoryForkEntity> repositoryForkPage = repositoryForkDao.findRepositoryForkPage(repositoryForkQuery);

        List<RepositoryFork> openRecordList = BeanMapper.mapList(repositoryForkPage.getDataList(), RepositoryFork.class);

        joinTemplate.joinQuery(openRecordList,new String[]{"user"});

        return PaginationBuilder.build(repositoryForkPage,openRecordList);
    }


}