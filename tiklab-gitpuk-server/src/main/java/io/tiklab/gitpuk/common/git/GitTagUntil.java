package io.tiklab.gitpuk.common.git;


import io.tiklab.core.exception.SystemException;
import io.tiklab.gitpuk.common.GitPukFinal;
import io.tiklab.gitpuk.tag.model.Tag;
import io.tiklab.gitpuk.common.RepositoryUtil;
import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class GitTagUntil {


    /**
     * 创建分支
     * @param repositoryAddress 仓库地址
     * @param tag 标签
     */
    public static void createTag(String repositoryAddress , Tag tag) throws IOException, GitAPIException {
        Repository repository = Git.open(new File(repositoryAddress)).getRepository();
        Git git = new Git(repository);

        // 获取本地分支
        List<Ref> localBranches = repository.getRefDatabase().getRefsByPrefix("refs/heads/");
        List<Ref> collect = localBranches.stream().filter(ref -> ref.getName().endsWith(tag.getTagName())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)){
            throw new SystemException(GitPukFinal.REPEAT01_EXCEPTION,"标签名与分支名不可以重复");
        }

        // 获取远程分支
        //List<Ref> remoteBranches = repository.getRefDatabase().getRefsByPrefix("refs/remotes/");

        ObjectId resolved = repository.resolve(tag.getCommitId());
        RevWalk walk = new RevWalk(repository);
        RevCommit commit = walk.parseCommit(resolved);

         git.tag().setName(tag.getTagName())
                .setObjectId(commit)
                .setMessage(tag.getDesc())
                .call();

        git.close();
    }

    /**
     * 修改分支
     * @param repositoryAddress 仓库地址
     */
    public static void updateTag(String repositoryAddress){

    }

    /**
     * 删除分支
     * @param repositoryAddress 仓库地址
     */
    public static void deleteTag(String repositoryAddress,String TagName) throws IOException, GitAPIException {
        Repository repository = Git.open(new File(repositoryAddress)).getRepository();
        Git git = new Git(repository);

        git.tagDelete().setTags(repositoryAddress,TagName).call();
        git.close();
    }

    /**
     * 查询分支
     * @param repositoryAddress 仓库地址
     */
    public static List findTag(String repositoryAddress) throws IOException, GitAPIException {
        List<Tag> arrayList = new ArrayList<>();

        Repository repository = Git.open(new File(repositoryAddress)).getRepository();
        Git git = new Git(repository);

        List<Ref> refList = git.tagList().call();
        for (Ref ref : refList){
            Tag tag = new Tag();
            String tagName = ref.getName().replace(Constants.R_TAGS, "");
            tag.setTagName(tagName);


            //描述
            RevWalk revWalk = new RevWalk(git.getRepository());
            RevTag revTag = revWalk.parseTag(ref.getObjectId());
            tag.setDesc(revTag.getFullMessage());

            //提交时间
            RevCommit revCommit = revWalk.parseCommit(ref.getObjectId());
            Integer commitTime = revCommit.getCommitTime();
            long longValue = commitTime.longValue()*1000;
            String timedBad = RepositoryUtil.timeBad(longValue);
            tag.setTimeDiffer(timedBad);

            //提交id
            String commitID = revCommit.getName();
            tag.setCommitId(commitID);

            //提交描述
            tag.setCommitDesc(revCommit.getShortMessage());
            arrayList.add(tag);

        }
        git.close();
        return arrayList;
    }
}
