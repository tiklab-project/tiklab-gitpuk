package io.tiklab.gitpuk.tag.controller;

import io.tiklab.gitpuk.tag.model.Tag;
import io.tiklab.gitpuk.tag.model.TagQuery;
import io.tiklab.gitpuk.tag.service.TagService;
import io.tiklab.core.Result;
import io.tiklab.postin.annotation.Api;
import io.tiklab.postin.annotation.ApiMethod;
import io.tiklab.postin.annotation.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/tag")
@Api(name = "LeadAuthController",desc = "标签")
public class TagController {

    @Autowired
    TagService tagService;
    @RequestMapping(path="/createTag",method = RequestMethod.POST)
    @ApiMethod(name = "create",desc = "创建标签")
    @ApiParam(name = "repository",desc = "repository",required = true)
    public Result<String> createTag(@RequestBody @NotNull @Valid Tag tag){

      tagService.createTag(tag);

        return Result.ok();
    }


    @RequestMapping(path="/deleteTag",method = RequestMethod.POST)
    @ApiMethod(name = "deleteTag",desc = "删除标签")
    @ApiParam(name = "tagName",desc = "tagName",required = true)
    public Result<String> deleteTag(@RequestBody @NotNull @Valid Tag tag){

        tagService.deleteTag(tag);

        return Result.ok();
    }

    @RequestMapping(path="/findTagList",method = RequestMethod.POST)
    @ApiMethod(name = "findTagList",desc = "查询标签")
    @ApiParam(name = "rpyId",desc = "rpyId",required = true)
    public Result<List<Tag>> findTagList(@RequestBody @NotNull @Valid TagQuery tagQuery){

       List<Tag> tagList= tagService.findTagList(tagQuery);

        return Result.ok(tagList);
    }
}
