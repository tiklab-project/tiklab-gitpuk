package net.tiklab.xcode.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GitDiff {

    public static void main(String[] args) throws Exception {
        Repository repository = Git.open(new File("C:\\Users\\admin\\xcode\\repository\\aaa.git")).getRepository(); // 用于获取存储库
        RevWalk walk = new RevWalk(repository);
        ObjectId objectId = repository.resolve("576b4d1c596f1e369ffc80e898508afb4cb66d66");
        ObjectId oldObjectId = repository.resolve("c0b56951259b0890bb0325bc59f62bda1964d462");

        RevCommit newCommit = walk.parseCommit(objectId);
        RevCommit oldCommit = walk.parseCommit(oldObjectId);
        String filePath = "tiklab-xcode-api/src/main/java/net/tiklab/xcode/code/model/Code.java"; // 文件路径

        // 获取文件的详细差异
        ObjectReader reader = repository.newObjectReader();

        CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
        oldTreeIter.reset(reader, oldCommit.getTree());

        CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
        newTreeIter.reset(reader, newCommit.getTree());

        // TreeWalk treeWalk = new TreeWalk(repository, reader);
        // treeWalk.addTree(oldTreeIter);
        // treeWalk.addTree(newTreeIter);
        // treeWalk.setFilter(PathFilter.create(filePath));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // DiffFormatter diffFormatter = new DiffFormatter(out);
        // diffFormatter.setRepository(repository);
        // diffFormatter.setDetectRenames(true);
        // diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
        // PathFilter pathFilter = PathFilter.create(filePath);
        // diffFormatter.setPathFilter(pathFilter);
        // List<DiffEntry> diffs = diffFormatter.scan(oldTreeIter, newTreeIter);

        DiffFormatter diffFormatter = new DiffFormatter(out);
        diffFormatter.setRepository(repository);
        diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
        diffFormatter.setDetectRenames(true);
        PathFilter pathFilter = PathFilter.create(filePath);
        diffFormatter.setPathFilter(pathFilter);
        List<DiffEntry> diffs =  diffFormatter.scan(oldTreeIter, newTreeIter);

        // 获取差异内容并解析为行列表

        for (DiffEntry diff : diffs) {
            if (diff.getNewPath().equals(filePath)) {
                diffFormatter.format(diff);
                String diffText = out.toString();
                String[] diffLines = diffText.split("\n");
                for (String line : diffLines) {
                    // if (line.startsWith("@@") && line.endsWith("@@")){
                    //     System.out.println("更新数据："+line);
                    // }
                    System.out.println(line);
                }
            }
        }

        // // 输出行列表
        // for (String line : lines) {
        //     System.out.println(line);
        // }
    }



    // public static void main(String[] args) throws Exception {
    //     Repository repository = Git.open(new File("C:\\Users\\admin\\xcode\\repository\\aaa.git")).getRepository(); // 用于获取存储库
    //
    //     RevWalk walk = new RevWalk(repository);
    //     ObjectId objectId = repository.resolve("2801f1d270c085cfc2131e11647eb8d8bea3f7dd");
    //     ObjectId oldObjectId = repository.resolve("99053b6de525b7322b4b9c0d4136a45819bf0fcc");
    //
    //     RevCommit commit2 = walk.parseCommit(objectId);
    //     RevCommit commit1 = walk.parseCommit(oldObjectId);
    //
    //     // 获取两个提交之间的文件差异
    //     ObjectReader reader = repository.newObjectReader();
    //     CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
    //     oldTreeIter.reset(reader, commit1.getTree());
    //     CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
    //     newTreeIter.reset(reader, commit2.getTree());
    //     TreeWalk treeWalk = new TreeWalk(repository, reader);
    //     treeWalk.addTree(oldTreeIter);
    //     treeWalk.addTree(newTreeIter);
    //     ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    //     DiffFormatter diffFormatter = new DiffFormatter(outputStream);
    //     diffFormatter.setRepository(repository);
    //     diffFormatter.setDetectRenames(true);
    //     List<DiffEntry> diffs = diffFormatter.scan(oldTreeIter, newTreeIter);
    //
    //     // 在控制台上显示差异
    //     for (DiffEntry diff : diffs) {
    //         if (!diff.getNewPath().equals("tiklab-xcode-server/pom.xml")){
    //             continue;
    //         }
    //         diffFormatter.format(diff);
    //         String diffText = outputStream.toString();
    //         System.out.println(diffText);
    //     }
    // }
}




