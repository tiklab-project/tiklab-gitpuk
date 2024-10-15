package io.tiklab.gitpuk.tag.service;

import io.tiklab.gitpuk.common.GitPukYamlDataMaService;
import io.tiklab.gitpuk.tag.model.Tag;
import io.tiklab.gitpuk.common.RepositoryUtil;
import io.tiklab.gitpuk.common.git.GitTagUntil;
import io.tiklab.core.exception.SystemException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    GitPukYamlDataMaService yamlDataMaService;

    @Override
    public void createTag(Tag tag) {
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),tag.getRpyId());

        try {

            GitTagUntil.createTag(repositoryAddress,tag);
        } catch (Exception e) {
            if (e.getMessage().endsWith("标签名与分支名不可以重复")){
                throw new SystemException("标签名与分支名不可以重复");
            }
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
