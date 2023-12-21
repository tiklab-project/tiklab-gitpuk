package io.thoughtware.gittork.repository.controller;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class test {

    public static void main(String[] args) throws IOException {
        Git git = Git.open(new java.io.File("/Users/limingliang/tiklab/thoughtware-gittork/repository/397a18472f7a.git"));
        Repository repository = git.getRepository();

        // 获取所有对象
        ObjectDatabase objectDatabase = repository.getObjectDatabase();
        ObjectLoader loader;
        int threshold = 1 * 1024 * 1024; // 2MB
        ObjectReader reader = repository.newObjectReader();


        ObjectId head = repository.resolve("HEAD");
        RevCommit commit = repository.parseCommit(head);
        RevTree tree = commit.getTree();

        try (TreeWalk treeWalk = new TreeWalk(repository)) {
            treeWalk.addTree(tree);
            treeWalk.setRecursive(true);

            while (treeWalk.next()) {
                System.out.println("File: " + treeWalk.getPathString());
            }
        }


    }


}
