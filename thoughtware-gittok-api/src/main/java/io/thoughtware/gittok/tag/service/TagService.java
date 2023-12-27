package io.thoughtware.gittok.tag.service;

import io.thoughtware.gittok.tag.model.Tag;

import java.util.List;

public interface TagService {

    /**
     * 创建标签
     * @param tag
     */
    void createTag(Tag tag);

    /**
     * 查询标签
     * @param rpyId
     */
    List<Tag> findTag(String rpyId);

    /**
     * 删除标签
     * @param tag
     */
    void deleteTag(Tag tag);

    /**
     * 查询标签
     * @param rpyId
     */
    Tag findTagByName(String rpyId, String tagName);
}
