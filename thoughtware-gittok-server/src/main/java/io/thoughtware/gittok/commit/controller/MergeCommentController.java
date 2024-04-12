package io.thoughtware.gittok.commit.controller;

import io.thoughtware.core.Result;
import io.thoughtware.core.page.Pagination;
import io.thoughtware.gittok.commit.model.MergeComment;
import io.thoughtware.gittok.commit.model.MergeCommentQuery;
import io.thoughtware.gittok.commit.service.MergeCommentService;
import io.thoughtware.postin.annotation.Api;
import io.thoughtware.postin.annotation.ApiMethod;
import io.thoughtware.postin.annotation.ApiParam;
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
 * MergeCommentController
 */
@RestController
@RequestMapping("/mergeComment")
@Api(name = "MergeCommentController",desc = "合并请求动态评论")
public class MergeCommentController {

    private static Logger logger = LoggerFactory.getLogger(MergeCommentController.class);

    @Autowired
    private MergeCommentService mergeCommentService;

    @RequestMapping(path="/createMergeComment",method = RequestMethod.POST)
    @ApiMethod(name = "createMergeComment",desc = "添加合并请求动态评论")
    @ApiParam(name = "mergeComment",desc = "mergeComment",required = true)
    public Result<String> createMergeComment(@RequestBody @Valid @NotNull MergeComment mergeComment){
        String mergeCommentId = mergeCommentService.createMergeComment(mergeComment);

        return Result.ok(mergeCommentId);
    }

    @RequestMapping(path="/updateMergeComment",method = RequestMethod.POST)
    @ApiMethod(name = "updateMergeComment",desc = "更新合并请求动态评论")
    @ApiParam(name = "mergeComment",desc = "mergeComment",required = true)
    public Result<String> updateMergeComment(@RequestBody @Valid @NotNull MergeComment mergeComment){
         mergeCommentService.updateMergeComment(mergeComment);

        return Result.ok();
    }


    @RequestMapping(path="/deleteMergeComment",method = RequestMethod.POST)
    @ApiMethod(name = "deleteMergeComment",desc = "删除合并请求动态评论")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<Void> deleteMergeComment(@NotNull String id){
        mergeCommentService.deleteMergeComment(id);

        return Result.ok();
    }

    @RequestMapping(path="/findMergeComment",method = RequestMethod.POST)
    @ApiMethod(name = "findMergeComment",desc = "通过id查询合并请求动态评论")
    @ApiParam(name = "id",desc = "id",required = true)
    public Result<MergeComment> findMergeComment(@NotNull String id){
        MergeComment mergeComment = mergeCommentService.findMergeComment(id);

        return Result.ok(mergeComment);
    }

    @RequestMapping(path="/findAllMergeComment",method = RequestMethod.POST)
    @ApiMethod(name = "findAllMergeComment",desc = "查询所有合并请求动态评论")
    public Result<List<MergeComment>> findAllMergeComment(){
        List<MergeComment> mergeCommentList = mergeCommentService.findAllMergeComment();

        return Result.ok(mergeCommentList);
    }

    @RequestMapping(path = "/findMergeCommentList",method = RequestMethod.POST)
    @ApiMethod(name = "findMergeCommentList",desc = "条件查询合并请求动态评论")
    @ApiParam(name = "mergeCommentQuery",desc = "mergeCommentQuery",required = true)
    public Result<List<MergeComment>> findMergeCommentList(@RequestBody @Valid @NotNull MergeCommentQuery mergeCommentQuery){
        List<MergeComment> mergeCommentList = mergeCommentService.findMergeCommentList(mergeCommentQuery);

        return Result.ok(mergeCommentList);
    }

    @RequestMapping(path = "/findMergeCommentPage",method = RequestMethod.POST)
    @ApiMethod(name = "findMergeCommentPage",desc = "条件分页查询合并请求动态评论")
    @ApiParam(name = "mergeCommentQuery",desc = "mergeCommentQuery",required = true)
    public Result<Pagination<MergeComment>> findMergeCommentPage(@RequestBody @Valid @NotNull MergeCommentQuery mergeCommentQuery){
        Pagination<MergeComment> pagination = mergeCommentService.findMergeCommentPage(mergeCommentQuery);

        return Result.ok(pagination);
    }

}
