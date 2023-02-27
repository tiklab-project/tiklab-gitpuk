package net.tiklab.xcode.git;

import net.tiklab.utils.context.LoginContext;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class GitTest {


    public static void main(String[] args) throws IOException, GitAPIException {

        // File file = new File("C:\\Users\\admin\\xcode\\repository\\abcde.git");
        // Git git = Git.open(file);
        // Repository repository = git.getRepository();
        // removeBranchProtect(repository);
        // git.close();

        aa();

    }

    public static void aa() throws IOException, GitAPIException {
        File file = new File("C:\\Users\\admin\\xcode\\repository\\abcde.git");

        Git call = Git.init()
                .setDirectory(file)
                .setBare(true) //裸仓库
                .setInitialBranch(Constants.MASTER)
                .call();
        call.close();

        Git git = Git.open(file);
        Repository repository = git.getRepository();
        createGit(repository);

        git.branchCreate()
                .setStartPoint(Constants.MASTER)
                .setName("aaa")
                .setForce(true)
                .call();
        // addBranchProtect(repository);


        RefUpdate updateRef = repository.updateRef("HEAD");
        updateRef.link(Constants.R_HEADS+"aaa");
        updateRef.update();

        String fullBranch = repository.getFullBranch();
        System.out.println(fullBranch);

        git.close();
    }

    public static  void addBranchProtect(Repository repository) throws IOException {
        Ref branch = repository.exactRef("refs/heads/aaa");
        RefUpdate updateRef = repository.updateRef(branch.getName());
        updateRef.setForceUpdate(true);
        updateRef.setNewObjectId(branch.getObjectId());
        updateRef.setExpectedOldObjectId(branch.getObjectId());
        updateRef.setForceRefLog(true);
        updateRef.setRefLogMessage("Setting branch as protected", false);
        RefUpdate.Result result = updateRef.update();
        if (result == RefUpdate.Result.NEW || result == RefUpdate.Result.NO_CHANGE) {
            System.out.println("设置受保护成功。");
        } else {
            System.out.println("Unable to set branch as protected");
        }
    }
    public static  void removeBranchProtect(Repository repository) throws IOException {
        Ref branch = repository.exactRef("refs/heads/aaa");

        // StoredConfig config = repository.getConfig();
        // String branchConfigName = "branch." + branch.getName().substring(11) + ".protection";
        // String branchProtection = config.getString("gitolite", null, branchConfigName);
        // if (branchProtection != null) {
        //     config.unset("gitolite", null, branchConfigName);
        //     config.save();
        //     System.out.println("Branch protection removed");
        // } else {
        //     System.out.println("Branch is not protected");
        // }

        StoredConfig config = repository.getConfig();
        config.setBoolean("branch", branch.getName(), "isReadOnly", false);
        config.save();

        // RefUpdate updateRef = repository.updateRef(branch.getName());
        // updateRef.setForceUpdate(true);
        // updateRef.setForceRefLog(true);
        // updateRef.setNewObjectId(branch.getObjectId());
        //
        // updateRef.setRefLogMessage("取消分支保护...", false);
        // RefUpdate.Result result = updateRef.update();
        // if (result == RefUpdate.Result.FORCED || result == RefUpdate.Result.NEW) {
        //     System.out.println("移除受保护。");
        // } else {
        //     String refLogMessage = updateRef.getRefLogMessage();
        //     String name = result.name();
        //     System.out.println("Message:" + refLogMessage);
        //     System.out.println("name:" + name);
        // }
    }

    public static void setRepositoryOnlyRead(Repository repository) throws IOException{

        // File workTreeDir = repository.getWorkTree();
        // boolean b1 = workTreeDir.setReadOnly();
        // System.out.println(b1);

        File gitDir = repository.getDirectory();
        boolean b = gitDir.setReadOnly();
        System.out.println(b);


        // StoredConfig config = repository.getConfig();
        // config.setInt("core", null,
        //         "repositoryformatversion", 0);
        // // 允许所有用户读取存储库
        // // config.setBoolean("http", null, "receivepack", true);
        //
        // // 仅允许特定用户写入存储库
        // // config.setString("http", null, "receivepack", "git-receive-pack");
        // // config.setString("http", null, "receivepack", "git-receive-pack --access-filter=repo/group=devteam");
        // config.save();
    }

    public static void createGit(Repository repository) throws GitAPIException, IOException {

        ObjectDatabase objectDatabase = repository.getObjectDatabase();
        ObjectInserter inserter = objectDatabase.newInserter();

        // 写入.gitignore文件到对象数据库
        File ignoreFile = new File("D:\\桌面\\新建文件夹\\.gitignore");
        FileInputStream inputStream = new FileInputStream(ignoreFile);
        ObjectId objectId = inserter.insert(Constants.OBJ_BLOB, ignoreFile.length(), inputStream);
        inserter.flush();
        inserter.close();

        // 写入README.md文件到对象数据库
        File mdFile = new File("D:\\桌面\\新建文件夹\\README.md");
        FileInputStream mdFileInputStream = new FileInputStream(mdFile);
        ObjectId mdObjectId = inserter.insert(Constants.OBJ_BLOB, mdFile.length(), mdFileInputStream);
        inserter.flush();
        inserter.close();

        //写入对象数据库到树
        TreeFormatter treeFormatter = new TreeFormatter();
        treeFormatter.append(".gitignore", FileMode.REGULAR_FILE, objectId);
        treeFormatter.append("README.md", FileMode.REGULAR_FILE, mdObjectId);
        ObjectId treeId = inserter.insert(treeFormatter);

        //设置提交信息
        CommitBuilder commitBuilder = new CommitBuilder();
        commitBuilder.setTreeId(treeId);
        String loginId = LoginContext.getLoginId();
        PersonIdent user = new PersonIdent("zhangcheng", "zhang@qq.com");
        commitBuilder.setCommitter(user);
        commitBuilder.setAuthor(user);

        // 提交更改
        ObjectInserter objectInserter = repository.newObjectInserter();
        ObjectId objectId1 = objectInserter.insert(commitBuilder);
        objectInserter.flush();
        objectInserter.close();

        //提交写入到master分支
        RefUpdate refUpdate = repository.updateRef(Constants.R_HEADS + Constants.MASTER);
        refUpdate.setNewObjectId(objectId1);
        refUpdate.setExpectedOldObjectId(ObjectId.zeroId());
        refUpdate.setForceUpdate(true);
        refUpdate.update();

    }





















}








































