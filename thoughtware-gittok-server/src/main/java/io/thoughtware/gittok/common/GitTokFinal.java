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
}
