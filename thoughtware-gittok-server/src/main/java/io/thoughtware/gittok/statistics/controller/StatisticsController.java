package io.thoughtware.gittok.statistics.controller;

import io.thoughtware.core.Result;
import io.thoughtware.gittok.statistics.model.Statistics;
import io.thoughtware.gittok.statistics.model.StatisticsQuery;
import io.thoughtware.gittok.statistics.service.StatisticsService;
import io.thoughtware.postin.annotation.Api;
import io.thoughtware.postin.annotation.ApiMethod;
import io.thoughtware.postin.annotation.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * CommitStatisticsController 提交统计
 */
@RestController
@RequestMapping("/statistics")
@Api(name = "CommitStatisticsController",desc = "统计")
public class StatisticsController {

    @Autowired
    StatisticsService statisticsService;

    @RequestMapping(path="/commitRpyStatistics",method = RequestMethod.POST)
    @ApiMethod(name = "commitRpyStatistics",desc = "全部仓库提交数量统计")
    @ApiParam(name = "statisticsQuery",desc = "statisticsQuery",required = true)
    public Result<Statistics> commitRpyStatistics(@RequestBody @NotNull @Valid StatisticsQuery statisticsQuery){
        Statistics statistics = statisticsService.commitRpyStatistics(statisticsQuery);

        return Result.ok(statistics);
    }

    @RequestMapping(path="/commitStatistics",method = RequestMethod.POST)
    @ApiMethod(name = "commitCount",desc = "单个仓库提交数量统计")
    @ApiParam(name = "statisticsQuery",desc = "statisticsQuery",required = true)
    public Result<Statistics> commitStatistics(@RequestBody @NotNull @Valid StatisticsQuery statisticsQuery){
        Statistics statistics = statisticsService.commitStatics(statisticsQuery);

        return Result.ok(statistics);
    }



    @RequestMapping(path="/commitRpyUserStatistics",method = RequestMethod.POST)
    @ApiMethod(name = "commitRpyUserStatistics",desc = "全部仓库用户提交数量统计")
    @ApiParam(name = "statisticsQuery",desc = "statisticsQuery",required = true)
    public Result<Statistics> commitRpyUserStatistics(@RequestBody @NotNull @Valid StatisticsQuery statisticsQuery){
        Statistics statistics = statisticsService.commitRpyUserStatistics(statisticsQuery);

        return Result.ok(statistics);
    }

    @RequestMapping(path="/commitUserStatistics",method = RequestMethod.POST)
    @ApiMethod(name = "commitUserStatistics",desc = "单个仓库用户提交数量统计")
    @ApiParam(name = "statisticsQuery",desc = "statisticsQuery",required = true)
    public Result<Statistics> commitUserStatistics(@RequestBody @NotNull @Valid StatisticsQuery statisticsQuery){
        Statistics statistics = statisticsService.commitUserStatistics(statisticsQuery);

        return Result.ok(statistics);
    }


    @RequestMapping(path="/codesStatistics",method = RequestMethod.POST)
    @ApiMethod(name = "commitCount",desc = "单个仓库提交代码统计")
    @ApiParam(name = "commitCount",desc = "LeadRecord",required = true)
    public Result<Statistics> codesStatistics(@RequestBody @NotNull @Valid StatisticsQuery statisticsQuery){
        Statistics statistics = statisticsService.codesStatistics(statisticsQuery);

        return Result.ok(statistics);
    }







    @RequestMapping(path="/mergeReqRpyStatistics",method = RequestMethod.POST)
    @ApiMethod(name = "mergeReqRpyStatistics",desc = "时间段内所有仓库合并请求数")
    @ApiParam(name = "statisticsQuery",desc = "statisticsQuery",required = true)
    public Result<Statistics> mergeReqRpyStatistics(@RequestBody @NotNull @Valid StatisticsQuery statisticsQuery){
        Statistics statistics = statisticsService.mergeReqRpyStatistics(statisticsQuery);

        return Result.ok(statistics);
    }

    @RequestMapping(path="/mergeReqRpyUserStatistics",method = RequestMethod.POST)
    @ApiMethod(name = "mergeReqRpyUserStatistics",desc = "时间段内所有仓库用户的合并请求数")
    @ApiParam(name = "statisticsQuery",desc = "statisticsQuery",required = true)
    public Result<Statistics> mergeReqRpyUserStatistics(@RequestBody @NotNull @Valid StatisticsQuery statisticsQuery){
        Statistics statistics = statisticsService.mergeReqRpyUserStatistics(statisticsQuery);

        return Result.ok(statistics);
    }


    @RequestMapping(path="/mergeReqStatistics",method = RequestMethod.POST)
    @ApiMethod(name = "mergeReqStatistics",desc = "单个仓库的合并请求数")
    @ApiParam(name = "statisticsQuery",desc = "statisticsQuery",required = true)
    public Result<Statistics> mergeReqStatistics(@RequestBody @NotNull @Valid StatisticsQuery statisticsQuery){
        Statistics statistics = statisticsService.mergeReqStatistics(statisticsQuery);

        return Result.ok(statistics);
    }

    @RequestMapping(path="/mergeReviewStatistics",method = RequestMethod.POST)
    @ApiMethod(name = "mergeReviewStatistics",desc = "单个仓库的合并审核")
    @ApiParam(name = "statisticsQuery",desc = "statisticsQuery",required = true)
    public Result<Statistics> mergeReviewStatistics(@RequestBody @NotNull @Valid StatisticsQuery statisticsQuery){
        Statistics statistics = statisticsService.mergeReviewStatistics(statisticsQuery);

        return Result.ok(statistics);
    }


    @RequestMapping(path="/statisticsTodoWorkByStatus",method = RequestMethod.POST)
    @ApiMethod(name = "statisticsTodoWorkByStatus",desc = "统计某个项目下，某个成员负责的事项对比")
    public Result<Map<String,Object>> statisticsTodoWorkByStatus(@RequestBody @NotNull @Valid HashMap<String, String> params){
        Map<String, Integer> todoCount = statisticsService.statisticsTodoWorkByStatus(params);

        return Result.ok(todoCount);
    }
}
