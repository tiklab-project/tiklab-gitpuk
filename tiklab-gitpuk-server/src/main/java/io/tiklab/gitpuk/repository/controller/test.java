package io.tiklab.gitpuk.repository.controller;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.dircache.DirCacheBuilder;
import org.eclipse.jgit.dircache.DirCacheEntry;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.internal.storage.file.GC;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

public class test {

    public static void main(String[] args) throws Exception {

       /* StringBuilder orders = new StringBuilder();
        orders.append("export PGPASSWORD=darth2020");
        orders.append(" && ");
        orders.append(" /Users/limingliang/work/pgsql-10.23/bin ").append("/psql ");
        orders.append(" -U postgres ");
        orders.append(" -d prd_gittok_ee ");
        orders.append(" -n xcode_ee ");
        orders.append(" -h 172.13.1.23 ");
        orders.append(" -p 5432 ");
        orders.append(" -f /Users/limingliang/work/work-source/thoughtware-gittok-ee_db_backups.sql");
*/
        createFolderTree();
        double numerator = 7; // 被除数
        double denominator = 2; // 除数

        // 计算相除并向上取整
        double result = Math.ceil(numerator / denominator);

        // 输出结果
        System.out.println("结果: " + result);
    }

    // 创建文件夹的 Tree
    private static void createFolderTree() throws IOException, ParseException, GitAPIException {
        File bareRepoDir = new File("/Users/limingliang/tiklab/tiklab-gitpuk/repository/f8f3f57db2ae.git");
        // 加载裸仓库
        Repository repository = new FileRepositoryBuilder()
                .setGitDir(bareRepoDir)
                .build();
     /*   ObjectId objectId = ObjectId.fromString("7b3d75af05fdde969c4f53ed56af7a973476990d");
        RefUpdate refUpdate = repository.updateRef("refs/heads/master");
        refUpdate.setNewObjectId(objectId);
        refUpdate.setForceUpdate(true);
        refUpdate.update();*/

        FileRepository fileRepo = new FileRepository(bareRepoDir);
        GC gc = new GC(fileRepo);
        gc.setExpireAgeMillis(0); // Expire all unreachable objects immediately
        gc.prunePacked();
        gc.gc();



    /*    Git git = new Git(repository);
        git.gc().setAggressive(true).call();*/
    }
}
