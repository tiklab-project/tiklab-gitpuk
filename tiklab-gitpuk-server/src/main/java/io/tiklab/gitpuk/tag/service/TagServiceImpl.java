package io.tiklab.gitpuk.tag.service;

import io.tiklab.gitpuk.common.GitPukYamlDataMaService;
import io.tiklab.gitpuk.tag.model.Tag;
import io.tiklab.gitpuk.common.RepositoryUtil;
import io.tiklab.gitpuk.common.git.GitTagUntil;
import io.tiklab.core.exception.SystemException;
import io.tiklab.gitpuk.tag.model.TagQuery;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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
    public List<Tag> findTagList(TagQuery tagQuery) {

        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),tagQuery.getRpyId());
        try {
            List<Tag> tag = GitTagUntil.findTag(repositoryAddress);
            if (StringUtils.isNotEmpty(tagQuery.getTagName())){
                tag=tag.stream().filter(a->a.getTagName().contains(tagQuery.getTagName())).collect(Collectors.toList());
            }
            return tag;
        } catch (Exception e) {
            throw new SystemException("查询tag失败"+e);
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
}
