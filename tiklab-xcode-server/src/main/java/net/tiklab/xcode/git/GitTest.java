package net.tiklab.xcode.git;

import net.tiklab.xcode.until.RepositoryFinal;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.transport.RefSpec;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class GitTest {


    public static void main(String[] args) throws Exception {

        File file = new File("C:\\Users\\admin\\xcode\\repository\\abcde.git");
        Git git = Git.init()
                .setDirectory(file)
                .setBare(true) //裸仓库
                .setInitialBranch(RepositoryFinal.DEFAULT_MASTER)
                .call();
        git.close();
        createGit("C:\\Users\\admin\\xcode\\repository\\abcde.git");

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


    public static void createGit(String repositoryAddress) throws GitAPIException, IOException {

        File file = new File(repositoryAddress);


        Git git = Git.open(file);
        Repository repository = git.getRepository();
        String branch = repository.getBranch();
        ObjectId id = repository.resolve(branch);
        ObjectId zeroId = ObjectId.zeroId();
        ObjectDatabase objectDatabase = repository.getObjectDatabase();
        ObjectInserter inserter = objectDatabase.newInserter();
        // 写入文件到对象数据库
        File files = new File("D:\\桌面\\新建文件夹\\.gitignore");
        FileInputStream fileInputStream = new FileInputStream(files);
        long length = files.length();
        ObjectId objectId = inserter.insert(Constants.OBJ_BLOB, length, fileInputStream);
        inserter.flush();
        inserter.close();

        CommitBuilder commitBuilder = new CommitBuilder();
        commitBuilder.setTreeId(objectId);
        commitBuilder.setParentIds(zeroId);
        commitBuilder.setCommitter(new PersonIdent("zhangcheng","zhang@qq.com"));
        commitBuilder.setAuthor(new PersonIdent("zhangcheng","zhang@qq.com"));
        // 提交更改
        ObjectInserter objectInserter = repository.newObjectInserter();
        objectInserter.insert(commitBuilder);
        objectInserter.flush();
        objectInserter.close();


    }






















}








































