package io.thoughtware.gittok.statistics.common;


import io.thoughtware.gittok.common.RepositoryUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class GitCommit {


    /**
     * 获取仓库数量
     * @param repositoryAddress repositoryAddress
     */
    public static List<String> getRpyCommitCount(String repositoryAddress,List<String> dayList) throws IOException, GitAPIException {
        List<String> commitIds = new ArrayList<>();

        Git git = Git.open(new File(repositoryAddress));
        Repository repository = git.getRepository();

        String startTime = dayList.get(0);
        String endTime = dayList.get(dayList.size()-1);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(startTime, formatter);
        LocalDate endDate = LocalDate.parse(endTime, formatter);

        RevWalk revWalk = new RevWalk(repository);

        //所有分支
        List<Ref> refs = git.branchList().call();
        for (Ref ref : refs) {
            revWalk.markStart(revWalk.parseCommit(ref.getObjectId()));
        }

        //所有分支的提交  重复的提交只会算一个
        for (RevCommit commit : revWalk) {
            LocalDate commitDate = Instant.ofEpochSecond(commit.getCommitTime())
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            if (commitDate.isEqual(startDate) ||(commitDate.isAfter(startDate) && commitDate.isBefore(endDate))){
                String name = commit.getName();
                commitIds.add(name);
            }
        }

        return commitIds;
    }
}
