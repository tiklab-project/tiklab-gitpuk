package io.tiklab.gitpuk.statistics.service;


import io.tiklab.gitpuk.statistics.model.StatisticsQuery;
import io.tiklab.gitpuk.statistics.model.Statistics;

import java.util.HashMap;
import java.util.Map;

public interface StatisticsService {

    /**
     * 单个仓库提交数量统计
     * @param statisticsQuery statisticsQuery
     */

    Statistics commitStatics(StatisticsQuery statisticsQuery);

    /**
     * 时间段内所有用户总提交数量统计
     * @param statisticsQuery statisticsQuery
     */
    Statistics commitRpyUserStatistics(StatisticsQuery statisticsQuery);

    /**
     * 单个仓库提交代码统计
     * @param statisticsQuery statisticsQuery
     */
    Statistics codesStatistics(StatisticsQuery statisticsQuery);

    /**
     * 时间段内所有仓库提交数量统计
     * @param statisticsQuery statisticsQuery
     */
    Statistics commitRpyStatistics(StatisticsQuery statisticsQuery);

    /**
     * 时间段内所有仓库合并请求数
     * @param statisticsQuery statisticsQuery
     */
    Statistics mergeReqRpyStatistics(StatisticsQuery statisticsQuery);

    /**
     * 单个仓库合并请求数统计
     * @param statisticsQuery statisticsQuery
     */
    Statistics mergeReqStatistics(StatisticsQuery statisticsQuery);

    /**
     * 时间段内所有仓库用户的合并请求数
     * @param statisticsQuery statisticsQuery
     */
    Statistics mergeReqRpyUserStatistics(StatisticsQuery statisticsQuery);

    /**
     * 单个仓库合并请求审核数统计
     * @param statisticsQuery statisticsQuery
     */
    Statistics mergeReviewStatistics(StatisticsQuery statisticsQuery);

    /**
     * 统计某个项目下，某个成员负责的事项对比
     * @param params params
     */
    Map<String, Integer> statisticsTodoWorkByStatus(HashMap<String, String> params);
    /**
     * 单个仓库用户提交数量统计
     * @param statisticsQuery statisticsQuery
     */
    Statistics commitUserStatistics(StatisticsQuery statisticsQuery);
}
