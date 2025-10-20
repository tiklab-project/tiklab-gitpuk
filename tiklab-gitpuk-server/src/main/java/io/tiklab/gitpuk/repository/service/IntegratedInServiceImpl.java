package io.tiklab.gitpuk.repository.service;

import io.tiklab.arbess.pipeline.definition.model.Pipeline;
import io.tiklab.arbess.pipeline.definition.model.PipelineQuery;
import io.tiklab.arbess.pipeline.definition.service.PipelineService;
import io.tiklab.core.exception.ApplicationException;
import io.tiklab.core.exception.SystemException;
import io.tiklab.core.page.Pagination;
import io.tiklab.gitpuk.common.RepositoryFinal;
import io.tiklab.gitpuk.repository.model.IntRelevancy;
import io.tiklab.gitpuk.repository.model.IntRelevancyQuery;
import io.tiklab.gitpuk.repository.model.IntegratedInQuery;
import io.tiklab.gitpuk.setting.service.IntegrationAddressServer;
import io.tiklab.rpc.client.RpcClient;
import io.tiklab.rpc.client.config.RpcClientConfig;
import io.tiklab.rpc.client.router.lookup.FixedLookup;
import io.tiklab.sourcefare.project.model.Project;
import io.tiklab.sourcefare.project.model.ProjectQuery;
import io.tiklab.sourcefare.project.service.ProjectService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IntegratedInServiceImpl implements IntegratedInService{

    @Autowired
    IntegrationAddressServer integrationAddressServer;

    @Autowired
    IntRelevancyService intRelevancyService;


    @Override
    public Object findPipelinePage(IntegratedInQuery integratedInQuery) {
        String address = getAddress(integratedInQuery.getAddress(), "arbess");

        PipelineService pipelineService = rpcClient().getBean(PipelineService.class, new FixedLookup(address));
        PipelineQuery pipelineQuery = new PipelineQuery();
        pipelineQuery.setPipelineName(integratedInQuery.getPipelineName());

        pipelineQuery.setPageParam(integratedInQuery.getPageParam());
        pipelineQuery.setUsername(integratedInQuery.getUserName());
        pipelineQuery.setPassword(integratedInQuery.getPassword());
        Pagination<Pipeline> pipelinePage = pipelineService.findUserPipelinePageByUser(pipelineQuery);

        return pipelinePage;
    }

    @Override
    public Object findRelevancePipelinePage(IntegratedInQuery integratedInQuery) {
        //查询关联的流水线id
        List<IntRelevancy> relevancyList = intRelevancyService.findIntRelevancyList(new IntRelevancyQuery().setRepositoryId(integratedInQuery.getRepositoryId())
                .setType(RepositoryFinal.PIPELINE));
        if (CollectionUtils.isEmpty(relevancyList)){
            return null;
        }

        String address = getAddress(integratedInQuery.getAddress(), "arbess");

        PipelineQuery pipelineQuery = new PipelineQuery();
        pipelineQuery.setPageParam(integratedInQuery.getPageParam());
        pipelineQuery.setUsername(integratedInQuery.getUserName());
        pipelineQuery.setPassword(integratedInQuery.getPassword());

        String[] strings = new String[relevancyList.size()];
        String[] pipelineIds = relevancyList.stream().map(IntRelevancy::getRelevancyId).collect(Collectors.toList()).toArray(strings);
        pipelineQuery.setIdString(pipelineIds);

        PipelineService pipelineService = rpcClient().getBean(PipelineService.class, new FixedLookup(address));

        Pagination<Pipeline> pipelinePage = pipelineService.findUserPipelinePageByUser(pipelineQuery);

        return pipelinePage;
    }

    @Override
    public Object findScanPlayPage(IntegratedInQuery integratedInQuery) {
        String address = integratedInQuery.getAddress();
        if (StringUtils.isBlank(address)){
            throw new SystemException(409,"未添加sourcewair服务地址");
        }
        if (address.endsWith("/")){
            address = StringUtils.substringBeforeLast(address, "/");
        }

        //查询代码关联的扫描计划
        List<IntRelevancy> relevancyList = intRelevancyService.findIntRelevancyList(new IntRelevancyQuery()
                .setRepositoryId(integratedInQuery.getRepositoryId())
                .setType("scan"));


        ProjectService projectService = rpcClient().getBean(ProjectService.class, new FixedLookup(address));
        ProjectQuery projectQuery = new ProjectQuery();
        projectQuery.setPageParam(integratedInQuery.getPageParam());
        projectQuery.setScanWay("server");
        if (!CollectionUtils.isEmpty(relevancyList)){
            List<String> stringList = relevancyList.stream().map(IntRelevancy::getRelevancyId).collect(Collectors.toList());
          //  projectQuery.setIds(stringList);
        }
        try {
            Pagination<Project> projectPage = projectService.findProjectPage(projectQuery);
            return projectPage;
        }catch (Exception e){
            throw new ApplicationException(5000,"连接"+address+"失败");
        }
    }

    @Override
    public Object findRelevanceScanPlay(IntegratedInQuery integratedInQuery) {
        String address = integratedInQuery.getAddress();
        if (StringUtils.isBlank(address)){
            throw new SystemException(409,"未添加sourcewair服务地址");
        }
        if (address.endsWith("/")){
            address = StringUtils.substringBeforeLast(address, "/");
        }
        ProjectService projectService = rpcClient().getBean(ProjectService.class, new FixedLookup(address));
        try {
            List<Project> projectServiceList = projectService.findList(integratedInQuery.getRelevancyIdS());
            return projectServiceList;
        }catch (Exception e){
            throw new SystemException(5000,"连接"+address+"失败");
        }
    }

    /**
     * 解析地址
     * @param address 地址
     * @param type 类型 sourcefare、arbess
     */

    public String getAddress(String address,String type ){
        if (StringUtils.isBlank(address)){
            throw new SystemException(409,"未添加"+type+"服务地址");
        }
        if (address.endsWith("/")){
            address = StringUtils.substringBeforeLast(address, "/");
        }
        return address;
    }

    //rpc配置
    RpcClient rpcClient(){
        RpcClientConfig rpcClientConfig = RpcClientConfig.instance();
        RpcClient rpcClient = new RpcClient(rpcClientConfig);
        return rpcClient;
    }
}
