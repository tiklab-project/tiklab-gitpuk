package io.thoughtware.gittork.tag.service;

import io.thoughtware.gittork.common.RepositoryUtil;
import io.thoughtware.gittork.common.GitTorkYamlDataMaService;
import io.thoughtware.gittork.common.git.GitTagUntil;
import io.thoughtware.gittork.tag.model.Tag;
import io.thoughtware.core.exception.SystemException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    GitTorkYamlDataMaService yamlDataMaService;

    @Override
    public void createTag(Tag tag) {
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),tag.getRpyId());

        try {
            GitTagUntil.createTag(repositoryAddress,tag);
        } catch (Exception e) {
            throw new SystemException("创建标签失败"+e);
        }
    }

    @Override
    public List<Tag> findTag(String rpyId) {

        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),rpyId);
        try {
            List<Tag> tag = GitTagUntil.findTag(repositoryAddress);
            return tag;
        } catch (Exception e) {
            throw new SystemException("查询失败"+e);
        }
    }

    @Override
    public void deleteTag(Tag tag) {
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),tag.getRpyId());
        try {
            GitTagUntil.deleteTag(repositoryAddress,tag.getTagName());
        } catch (Exception e) {
            throw new SystemException("删除失败"+e);
        }
    }

    @Override
    public Tag findTagByName(String rpyId, String tagName) {
        List<Tag> tagList = findTag(rpyId);
        if (CollectionUtils.isNotEmpty(tagList)){
            List<Tag> tags = tagList.stream().filter(a -> tagName.equals(a.getTagName())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(tags)){
                return tags.get(0);
            }
        }
        return null;
    }

}
