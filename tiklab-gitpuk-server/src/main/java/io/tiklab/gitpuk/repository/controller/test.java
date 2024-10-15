package io.tiklab.gitpuk.repository.controller;

import org.eclipse.jgit.lib.*;

import java.io.IOException;

public class test {

    public static void main(String[] args) throws Exception {

        double numerator = 7; // 被除数
        double denominator = 2; // 除数

        // 计算相除并向上取整
        double result = Math.ceil(numerator / denominator);

        // 输出结果
        System.out.println("结果: " + result);
    }

    // 创建文件夹的 Tree
    private static ObjectId createFolderTree(ObjectInserter inserter) throws IOException {
        TreeFormatter treeFormatter = new TreeFormatter();
        return inserter.insert(treeFormatter);
    }
}
