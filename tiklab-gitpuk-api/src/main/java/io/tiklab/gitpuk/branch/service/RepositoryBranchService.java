package io.tiklab.gitpuk.branch.service;



import io.tiklab.core.page.Pagination;
import io.tiklab.gitpuk.branch.model.RepositoryBranch;
import io.tiklab.gitpuk.branch.model.RepositoryBranchQuery;
import io.tiklab.toolkit.join.annotation.FindAll;
import io.tiklab.toolkit.join.annotation.FindList;
import io.tiklab.toolkit.join.annotation.FindOne;
import io.tiklab.toolkit.join.annotation.JoinProvider;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
* RepositoryBranchService-合并请求评论
*/
@JoinProvider(model = RepositoryBranch.class)
public interface RepositoryBranchService {

    /**
    * 创建
    * @param repositoryBranch
    * @return
    */
    String createRepositoryBranch(@NotNull @Valid RepositoryBranch repositoryBranch);

    /**
    * 更新
    * @param repositoryBranch
    */
    void updateRepositoryBranch(@NotNull @Valid RepositoryBranch repositoryBranch);

    /**
    * 删除
    * @param id
    */
    void deleteRepositoryBranch(@NotNull String id);

    /**
     * 通过仓库id、分支名字删除
     * @param repositoryId 仓库id
     * @param  branchName 分支名字
     */
    void deleteRepositoryBranch(String repositoryId,String branchName);

    @FindOne
    RepositoryBranch findOne(@NotNull String id);

    @FindList
    List<RepositoryBranch> findList(List<String> idList);

    /**
    * 查找
    * @param id
    * @return
    */

    RepositoryBranch findRepositoryBranch(@NotNull String id);

    /**
    * 查找所有
    * @return
    */
    @FindAll
    List<RepositoryBranch> findAllRepositoryBranch();

    /**
    * 查询列表
    * @param repositoryBranchQuery
    * @return
    */
    List<RepositoryBranch> findRepositoryBranchList(RepositoryBranchQuery repositoryBranchQuery);

    /**
    * 按分页查询
    * @param repositoryBranchQuery
    * @return
    */
    Pagination<RepositoryBranch> findRepositoryBranchPage(RepositoryBranchQuery repositoryBranchQuery);


}