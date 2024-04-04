package io.thoughtware.gittok.repository.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.thoughtware.gittok.commit.model.Commit;
import io.thoughtware.gittok.commit.model.CommitFileDiff;
import io.thoughtware.gittok.common.RepositoryUtil;
import io.thoughtware.gittok.common.git.GitBranchUntil;
import io.thoughtware.gittok.common.git.GitCommitUntil;
import io.thoughtware.gittok.common.git.GitUntil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.internal.storage.file.PackFile;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.revwalk.*;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class test {

    public static void main(String[] args) throws Exception {
        String bareRepositoryPath = "/Users/limingliang/tiklab/thoughtware-gittok/repository/e35b85357fcb.git"; // 裸仓库路径

        Git git = Git.open(new File(bareRepositoryPath));
        String a="44fb1a681a3129b3736f29a0d4bfa8c324b42fe7";
        String b="9c74d3932994e9b18848b7e3b49c596b9e63f474";
        String path="thoughtware-hadess-server/src/main/java/io/thoughtware/hadess/repository/controller/RepositoryController.java";

        Commit commit = new Commit();
        commit.setBranch("a");
        commit.setTargetBranch("source");
        commit.setFilePath(path);
        List<CommitFileDiff> diffBranchFileDetails = GitCommitUntil.findDiffBranchFileDetails(git, commit);
        System.out.println("");


        //merge();
        //push();
    }

    public static void push() throws Exception {
        String normalRepoDir = "/Users/limingliang/tiklab/thoughtware-gittok/repository/clone/e35b85357fcb"; // 裸仓库路径
        String bareRepoDir = "/Users/limingliang/tiklab/thoughtware-gittok/repository/a15874a0a5c4.git"; // 裸仓库路径
        GitUntil. pushAllBranchRepository(normalRepoDir,bareRepoDir);
    }

    public static void merge() throws IOException, GitAPIException {
        String bareRepositoryPath = "/Users/limingliang/tiklab/thoughtware-gittok/repository/e35b85357fcb.git"; // 裸仓库路径

        GitBranchUntil.mergeBranch1(null,bareRepositoryPath);

        String beforeLast = StringUtils.substringBeforeLast(bareRepositoryPath, "/");
        String afterLast = StringUtils.substringAfterLast(bareRepositoryPath, "/");
        String rpyId = afterLast.substring(0, afterLast.indexOf(".git"));

        String clonePath = beforeLast + "/clone/"+rpyId;

        Git git = Git.open(new File(clonePath));

        //切换到目标分支
        git.checkout().setName("source").call();

        // 检查差异
        List<DiffEntry> diffEntries = diffEntry(git, "source", "a");


        //获取目标对象的id
        ObjectId mergeBase = git.getRepository().resolve("a");

        String mergeMessage = "Merge b into source";
        for (DiffEntry entry : diffEntries) {
            git.add().addFilepattern(entry.getNewPath()).call();
        }
 /*       git.merge()
                .include(mergeBase)
                .setCommit(false)
                .setSquash(true)
                //.setFastForward(MergeCommand.FastForwardMode.NO_FF)
                .setMessage(mergeMessage)
                .call();*/
        //git.add().addFilepattern("*").call();

        git.merge().
                include(mergeBase).
                setCommit(true)
                .setFastForward(MergeCommand.FastForwardMode.NO_FF)
                .setMessage(mergeMessage).
                call();


        //git.add().addFilepattern("*").call();

 /*       //创建提交信息
        git.commit()
                .setMessage(mergeMessage)
                //.setCommitter()
                .call();
*/
    }

    private static List<DiffEntry> diffEntry(Git git, String targetBranch,String branch) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DiffFormatter diffFormatter = new DiffFormatter(out);
        diffFormatter.setRepository(git.getRepository());
        diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
        diffFormatter.setDetectRenames(true);


        Repository repository = git.getRepository();
        RevWalk revWalk = new RevWalk(git.getRepository());

        //目标分支
        ObjectId targetObjectId= git.getRepository().resolve(targetBranch);
        RevCommit targetCommit = revWalk.parseCommit(targetObjectId);

        //源分支
        ObjectId originObjectId = git.getRepository().resolve(branch);
        RevCommit originCommit = revWalk.parseCommit(originObjectId);

        List<DiffEntry> scan = diffFormatter.scan(targetCommit.getTree(), originCommit.getTree());
        return scan;
    }

}


