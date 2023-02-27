```java
    // 创建裸仓库
    Git git = Git.init()
            .setDirectory(new File(repositoryAddress))
            .setBare(true) //裸仓库
            .call();
    // 创建 README.md 文件并提交
    git.add()
            .addFilepattern("README.md")
            .addFilepattern("D:\\桌面\\新建文件夹\\.gitignore")
            .call();
    git.commit()
            .setMessage("Initial commit")
            .call();
    // 关闭 Git 对象
    git.close();
}
```