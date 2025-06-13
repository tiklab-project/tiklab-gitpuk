package io.tiklab.gitpuk.branch.controller;

import io.tiklab.core.Result;
import io.tiklab.core.page.Pagination;
import io.tiklab.gitpuk.branch.model.RepositoryBranch;
import io.tiklab.gitpuk.branch.model.RepositoryBranchQuery;
import io.tiklab.gitpuk.branch.service.RepositoryBranchService;
import io.tiklab.postin.annotation.Api;
import io.tiklab.postin.annotation.ApiMethod;
import io.tiklab.postin.annotation.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * RepositoryBranchController
 */
@RestController
@RequestMapping("/repositoryBranch")
//@Api(name = "RepositoryBranchController",desc = "数据库存储分支记录管理")
public class RepositoryBranchController {

    private static Logger logger = LoggerFactory.getLogger(RepositoryBranchController.class);

    @Autowired
    private RepositoryBranchService repositoryBranchService;

    @RequestMapping(path="/createRepositoryBranch",method = RequestMethod.POST)
    @ApiMethod(name = "createRepositoryBranch",desc = "添加仓库分支")
    @ApiParam(name = "repositoryBranch",desc = "repositoryBranch",required = true)
    public Result<String> createRepositoryBranch(@RequestBody @Valid @NotNull RepositoryBranch repositoryBranch){
        String repositoryBranchId = repositoryBranchService.createRepositoryBranch(repositoryBranch);

        return Result.ok(repositoryBranchId);
    }

    @RequestMapping(path="/updateRepositoryBranch",method = RequestMethod.POST)
    @ApiMethod(name = "updateRepositoryBranch",desc = "更新仓库分支")
    @ApiParam(name = "repositoryBranch",desc = "repositoryBranch",required = true)
    public Result<String> updateRepositoryBranch(@RequestBody @Valid @NotNull RepositoryBranch repositoryBranch){
         repositoryBranchService.updateRepositoryBranch(repositoryBranch);

        return Result.ok();
    }


    @RequestMapping(path="/deleteRepositoryBranch",method = RequestMethod.POST)
    @ApiMethod(name = "deleteRepositoryBranch",desc = "删除仓库分支")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<Void> deleteRepositoryBranch(@NotNull String id){
        repositoryBranchService.deleteRepositoryBranch(id);

        return Result.ok();
    }

    @RequestMapping(path="/findRepositoryBranch",method = RequestMethod.POST)
    @ApiMethod(name = "findRepositoryBranch",desc = "通过id查询仓库分支")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<RepositoryBranch> findRepositoryBranch(@NotNull String id){
        RepositoryBranch repositoryBranch = repositoryBranchService.findRepositoryBranch(id);

        return Result.ok(repositoryBranch);
    }

    @RequestMapping(path="/findAllRepositoryBranch",method = RequestMethod.POST)
    @ApiMethod(name = "findAllRepositoryBranch",desc = "查询所有仓库分支")
    public Result<List<RepositoryBranch>> findAllRepositoryBranch(){
        List<RepositoryBranch> repositoryBranchList = repositoryBranchService.findAllRepositoryBranch();

        return Result.ok(repositoryBranchList);
    }

    @RequestMapping(path = "/findRepositoryBranchList",method = RequestMethod.POST)
    @ApiMethod(name = "findRepositoryBranchList",desc = "条件查询仓库分支")
    @ApiParam(name = "repositoryBranchQuery",desc = "repositoryBranchQuery",required = true)
    public Result<List<RepositoryBranch>> findRepositoryBranchList(@RequestBody @Valid @NotNull RepositoryBranchQuery repositoryBranchQuery){
        List<RepositoryBranch> repositoryBranchList = repositoryBranchService.findRepositoryBranchList(repositoryBranchQuery);

        return Result.ok(repositoryBranchList);
    }

    @RequestMapping(path = "/findRepositoryBranchPage",method = RequestMethod.POST)
    @ApiMethod(name = "findRepositoryBranchPage",desc = "条件分页查询仓库分支")
    @ApiParam(name = "repositoryBranchQuery",desc = "repositoryBranchQuery",required = true)
    public Result<Pagination<RepositoryBranch>> findRepositoryBranchPage(@RequestBody @Valid @NotNull RepositoryBranchQuery repositoryBranchQuery){
        Pagination<RepositoryBranch> pagination = repositoryBranchService.findRepositoryBranchPage(repositoryBranchQuery);

        return Result.ok(pagination);
    }

}
