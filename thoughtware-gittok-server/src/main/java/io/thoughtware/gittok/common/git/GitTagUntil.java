package io.thoughtware.gittok.common.git;


import io.thoughtware.gittok.common.RepositoryUtil;
import io.thoughtware.gittok.tag.model.Tag;
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


public class GitTagUntil {


    /**
     * 创建分支
     * @param repositoryAddress 仓库地址
     * @param tag 标签
     */
    public static void createTag(String repositoryAddress , Tag tag) throws IOException, GitAPIException {
        Repository repository = Git.open(new File(repositoryAddress)).getRepository();
        Git git = new Git(repository);

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
            tag.setCommitDesc(revCommit.getFullMessage());
            arrayList.add(tag);

        }
        git.close();
        return arrayList;
    }
}
