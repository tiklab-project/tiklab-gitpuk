package io.tiklab.gitpuk.tag.service;

import io.tiklab.gitpuk.tag.model.Tag;
import io.tiklab.gitpuk.tag.model.TagQuery;

import java.util.List;

public interface TagService {

    /**
     * 创建标签
     * @param tag
     */
    void createTag(Tag tag);

    /**
     * 查询标签
     * @param tagQuery tagQuery
     */
    List<Tag> findTagList(TagQuery tagQuery);

    /**
     * 删除标签
     * @param tag
     */
    void deleteTag(Tag tag);

}
