package io.thoughtware.gittok.common;

public class GitTokFinal {


    // 日志类型
    public static final String LOG_TYPE_CREATE = "GTK_CREATE";

    public static final String LOG_TYPE_UPDATE = "GTK_UPDATE";

    public static final String LOG_TYPE_DELETE = "GTK_DELETE";

    public static final String LOG_TYPE_GROUP_CREATE = "GTKGP_CREATE";

    public static final String LOG_TYPE_GROUP_UPDATE = "GTKGP_UPDATE";

    public static final String LOG_TYPE_GROUP_DELETE = "GTKGP_DELETE";



    //仓库创建
    public static final String LOG_RPY_CREATE = "/repository/${repositoryPath}/setting/info";
    //仓库删除
    public static final String LOG_RPY_DELETE = "/repository";
    //仓库修改
    public static final String LOG_RPY_UPDATE = "/repository/${repositoryPath}/setting/info";

    //仓库组创建
    public static final String GROUP_RPY_CREATE = "/group/${groupName}/setting/info";
    //仓库组删除
    public static final String GROUP_RPY_DELETE = "/group";
    //仓库组更新
    public static final String GROUP_RPY_UPDATE = "/group/${groupName}/setting/info";


    /**
     * DEFAULT
     */
    public static final String DEFAULT = "default";

    //gitlab 查询仓库API地址
    public static final String GITLAB_API_URL = "https://gitlab.com/api/v4/projects";

    //github 查询仓库API地址
    public static final String GITHUB_API_URL = "https://api.github.com/user/repos";

    //gitee 查询仓库API地址
    public static final String GITEE_API_URL = "https://gitee.com/api/v5/user/repos";



    /*
    * 合并请求的动态
    * */

    // 创建和并请求
    public static final String MERGE_CREATE = "create";
    public static final String MERGE_CREATE_DESC = "创建了合并请求";


    // 打开和并请求
    public static final String MERGE_OPEN = "open";
    public static final String MERGE_OPEN_DESC = "打开了合并请求";

    // 关闭和并请求
    public static final String MERGE_CLOSE = "close";
    public static final String MERGE_CLOSE_DESC = "关闭了合并请求";

    //创建评论
    public static final String MERGE_COMMENT = "comment";
    public static final String MERGE_COMMENT_DESC = "发布了评论";

    //完成合并
    public static final String MERGE_COMPLETE = "complete";
    public static final String MERGE_COMPLETE_DESC = "合并分支";


}
