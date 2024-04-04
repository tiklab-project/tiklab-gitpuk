package io.thoughtware.gittok.repository.controller;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class tetGit {

    public static void main(String[] args) throws Exception {
        String bareRepositoryPath = "/Users/limingliang/tiklab/thoughtware-gittok/repository/482d226d0e9c.git"; // 裸仓库路径

        //获取仓库所有分支信息
        Git git = Git.open(new File(bareRepositoryPath));
        Repository repository = git.getRepository();
        List<Ref> branche = git.branchList().call();
        System.out.println("");
    }
}
