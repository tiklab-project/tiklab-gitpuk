package net.tiklab.xcode.git;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.transport.RefSpec;
import java.io.*;

public class GitTest {


    public static void main(String[] args) throws Exception {

        findAllBranch("C:\\Users\\admin\\xcode\\repository\\aa.git");

    }

    public static void findAllBranch(String repositoryAddress) throws IOException {

        // 2. 获取Git对象
        Git git = Git.open(new File(repositoryAddress));
        Repository repository = git.getRepository();
        // 3. 设置分支保护
        RefUpdate branch = repository.updateRef("refs/heads/master");
        branch.setForceUpdate(false);
        branch.setRefLogMessage("Protected branch", false);
        RefSpec spec = new RefSpec("+refs/heads/master");
        spec.setForceUpdate(false);
        ObjectId objectId = repository.resolve("master");
        branch.setExpectedOldObjectId(objectId);
        branch.setNewObjectId(objectId);
        PersonIdent ident = new PersonIdent("John Doe", "john.doe@example.com");
        branch.setRefLogIdent(ident);
        branch.update();

        // 4. 关闭Git对象和仓库
        git.close();

    }


    // public static void createGit() throws IOException {
    //     // 1. 初始化Git仓库
    //     Repository repository = Git.init()
    //             .setDirectory(new FileQuery("/path/to/repository"))
    //             .call()
    //             .getRepository();
    //
    //     // 2. 创建README.md文件
    //     String readmeContent = "This is a README file.";
    //     FileQuery readmeFile = new FileQuery(repository.getDirectory().getParent(), "README.md");
    //     FileUtils.write(readmeFile, readmeContent);
    //
    //     // 3. 创建.gitignore文件
    //     String gitignoreContent = "*.log\n/target/\n";
    //     FileQuery gitignoreFile = new FileQuery(repository.getDirectory().getParent(), ".gitignore");
    //     FileUtils.write(gitignoreFile, gitignoreContent);
    //
    //     // 4. 创建默认分支
    //     RefUpdate updateRef = repository.updateRef(Constants.HEAD);
    //     ObjectId emptyTreeId = repository.writeTree(repository.newObjectReader());
    //     DirCache dirCache = DirCache.newInCore();
    //     DirCacheEntry readmeEntry = new DirCacheEntry("README.md");
    //     readmeEntry.setFileMode(FileMode.REGULAR_FILE);
    //
    //     readmeEntry.setObjectId(repository.getObjectDatabase().insert(Constants.OBJ_BLOB, readmeContent.getBytes()));
    //     DirCacheEntry gitignoreEntry = new DirCacheEntry(".gitignore");
    //     gitignoreEntry.setFileMode(FileMode.REGULAR_FILE);
    //     gitignoreEntry.setObjectId(repository.getObjectDatabase().insert(Constants.OBJ_BLOB, gitignoreContent.getBytes()));
    //     dirCache.add(readmeEntry);
    //     dirCache.add(gitignoreEntry);
    //     dirCache.write();
    //     ObjectId treeId = dirCache.writeTree(repository.getObjectDatabase());
    //     RevCommit initialCommit = new RevCommit(ObjectId.zeroId());
    //     initialCommit.setTreeId(treeId);
    //     initialCommit.setAuthor(new PersonIdent("John Doe", "john.doe@example.com"));
    //     initialCommit.setCommitter(new PersonIdent("John Doe", "john.doe@example.com"));
    //     initialCommit.setMessage("Initial commit");
    //     Ref branch = updateRef.update(initialCommit);
    //
    //     // 5. 关闭Git仓库
    //     repository.close();
    // }























}








































