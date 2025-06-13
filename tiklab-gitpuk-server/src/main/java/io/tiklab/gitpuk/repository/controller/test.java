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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

    public static void main(String[] args) throws Exception {

        String file="12";


        boolean matches = file.matches("(?s)(?=.*1)(?=.*2).*");


        System.out.println("结果："+matches);

       /* // 源文件夹路径
        String sourceDirPath = "/Users/limingliang/tiklab/tiklab-gitpuk/repository/aee3bef1c5e7.git";
        // 目标文件夹路径
        String targetDirPath = "/Users/limingliang/tiklab/tiklab-gitpuk/repository/aee3bef1c5e7123.git";

        File sourceDir = new File(sourceDirPath);
        File targetDir = new File(targetDirPath);

        FileUtils.copyDirectory(sourceDir, targetDir);*/


        System.out.println("循环结束，继续执行后续操作");
    }

}
