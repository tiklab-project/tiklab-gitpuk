package io.tiklab.gitpuk.repository.controller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
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
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

public class test {

    public static void main(String[] args) throws Exception {

        // 源文件夹路径
        String sourceDirPath = "/Users/limingliang/tiklab/tiklab-gitpuk/repository/aee3bef1c5e7.git";
        // 目标文件夹路径
        String targetDirPath = "/Users/limingliang/tiklab/tiklab-gitpuk/repository/aee3bef1c5e7123.git";

        File sourceDir = new File(sourceDirPath);
        File targetDir = new File(targetDirPath);

        FileUtils.copyDirectory(sourceDir, targetDir);



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
     //   createFolderTree();


/*
        List<String> items = Arrays.asList("item1", "item2", "item3", "item4");

        for (String item : items) {
            try {
                // 模拟可能抛出异常的处理
                if (item.equals("item2")) {
                    throw new Exception("处理失败");
                }
                System.out.println("成功处理: " + item);
            } catch (Exception e) {
                // 捕获异常并打印错误信息

                System.out.println("处理 " + item + " 时发生异常: " + e.getMessage());
                continue;
            }
            System.out.println("循环结束，继续执行后续操作"+item);
        }*/

        System.out.println("循环结束，继续执行后续操作");
    }

    private static void processItem(String item) throws Exception {
        // 模拟可能抛出异常的处理
        if (item.equals("item2")) {
            throw new Exception("处理失败");
        }
        System.out.println("成功处理: " + item);
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
