package io.tiklab.gitpuk.repository.controller;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.dircache.DirCacheBuilder;
import org.eclipse.jgit.dircache.DirCacheEntry;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.*;
import java.nio.charset.StandardCharsets;

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
    private static void createFolderTree() throws IOException {
        String s = StringUtils.substringAfter("http://e.tiklab.net", "http://");
        System.out.println("s:"+s);
    }
}
