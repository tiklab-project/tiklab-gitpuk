package net.tiklab.xcode.until;

public class RepositoryFinal {

    /**
     * 默认分支
     */
    public final static String DEFAULT_MASTER = "master";

    /**
     * 项目名称
     */
    public final static String APP_NAME = "xcode";

    /**
     * 树
     */
    public final static String FILE_TYPE_TREE = "tree";

    /**
     * 文件
     */
    public final static String FILE_TYPE_BLOB = "blob";

    /**
     * commitId唯一标识
     */
    public final static String COMMIT_ONLY_ID = "commit_id";


    /**
     * ssh监听地址
     */
    public final static String SSH_HOST = "0.0.0.0";

    /**
     * 返回true
     */
    public final static boolean TRUE = true;

    /**
     * 返回false
     */
    public final static boolean FALSE = false;

    /**
     * 文件内容添加
     */
    public final static String DIFF_TYPE_ADD = "+";

    /**
     * 文件内容删除
     */
    public final static String DIFF_TYPE_DELETE = "-";

    /**
     * 大文件
     */
    public final static String DIFF_TYPE_BIG = "big";

    /**
     * 向上
     */
    public final static String FILE_DOWN = "down";

    /**
     * 地址为空
     */
    public final static String FILE_PATH_NULL = "/dev/null";

    /**
     * 向下
     */
    public final static String FILE_UP = "up";

    /**
     * 文件内容未改变
     */
    public final static String DIFF_TYPE_TEXT = " ";


    /**
     * 解析DIFF文件发生变化的信息
     */
    public final static String DIFF_REGEX = "@@ -(\\d+)(?:,(\\d+))? \\+(\\d+)(?:,(\\d+))? @@.*";

    /**
     * DIFF变化
     */
    public final static String DIFF_TYPE = "\u0000";

    /**
     * 秘钥编码类型RSA
     */
    public final static String SSH_ENCODER_RSA = "RSA";

    /**
     * 以ssh-rsa开头的私钥
     */
    public final static String Key_TYPE_OPENSSH_RSA = "ssh-rsa";

    /**
     * 以-----BEGIN开头的私钥
     */
    public final static String Key_TYPE_SSH_RSA = "-----BEGIN ";



}












































