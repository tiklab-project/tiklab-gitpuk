package io.tiklab.gitpuk.file.model;


/**
 * 文件树信息
 */

public class FileFindQuery{

    //仓库id
    private String rpyId;


    //查询对象  分支、标签、commitId的code
    private String refCode;


    //查询类型 tag、branch、commit
    private String refCodeType;


    //路径
    private String path;


    //查询类型 刷新查询带子集的：find、点击查询单层文件的：onclickFind
    private String findType;


    public String getRpyId() {
        return rpyId;
    }

    public void setRpyId(String rpyId) {
        this.rpyId = rpyId;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    public String getRefCodeType() {
        return refCodeType;
    }

    public void setRefCodeType(String refCodeType) {
        this.refCodeType = refCodeType;
    }

    public String getFindType() {
        return findType;
    }

    public void setFindType(String findType) {
        this.findType = findType;
    }
}
