package io.thoughtware.gittok.common.git;


import io.thoughtware.gittok.common.RepositoryUtil;
import io.thoughtware.gittok.repository.model.LeadAuth;
import io.thoughtware.gittok.repository.model.RemoteInfo;
import io.thoughtware.core.exception.ApplicationException;
import io.thoughtware.user.user.model.User;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.transport.*;

import java.io.*;
import java.net.URISyntaxException;


/**
 * git仓库操作
 */

public class GitUntil {

    /**
     * 创建仓库
     * @param repositoryAddress 仓库地址
     * @throws ApplicationException 仓库创建失败
     */
    public static Git createRepository(String repositoryAddress) throws ApplicationException {
        File file = new File(repositoryAddress);
        Git git;
        try {
            git = Git.init()
                    .setDirectory(file)
                    .setBare(true) //裸仓库
                    //.setInitialBranch(Constants.MASTER)
                    .call();
            git.close();
        } catch (GitAPIException e) {
            throw new ApplicationException("仓库创建失败。" + e);
        }
        git.close();
        return git;
    }


    /**
     * 裸仓库初始化添加文件
     * @param git 裸仓库
     * @param rpy 创建仓库数据
     * @param ignoreFilePath gitignore
     */
    public static void addRepositoryInitFile(Git git, io.thoughtware.gittok.repository.model.Repository rpy,String ignoreFilePath) {

        User user = rpy.getUser();
        Integer isReadme = rpy.getIsReadme();
        Repository repository = git.getRepository();

        try {

            ObjectDatabase objectDatabase = repository.getObjectDatabase();
            ObjectInserter inserter = objectDatabase.newInserter();

            TreeFormatter treeFormatter = new TreeFormatter();
            // 写入.gitignore文件到对象数据库
            if (RepositoryUtil.isNoNull(ignoreFilePath)){
                File ignoreFile = new File(ignoreFilePath);
                //byte[] bytes = ignoreFileData.getBytes();
                InputStream inputStream;
                try {
                    inputStream = new FileInputStream(ignoreFile);
                    //inputStream = new ByteArrayInputStream(bytes);
                } catch (Exception e) {
                    throw new ApplicationException("文件.gitignore写入失败：" + e);
                }

                ObjectId objectId = inserter.insert(Constants.OBJ_BLOB, ignoreFile.length(), inputStream);
                inserter.flush();
                inserter.close();
                treeFormatter.append(".gitignore", FileMode.REGULAR_FILE, objectId);
            }

            // 写入README.md文件到对象数据库
            if (isReadme==1){
                InputStream inputStream;
                String data= rpy.getName();
                byte[] bytes = data.getBytes();
                try {
                    // 创建 ByteArrayInputStream 对象
                     inputStream = new ByteArrayInputStream(bytes);
                } catch (Exception e) {
                    throw new ApplicationException("文件README.md写入失败：" + e);
                }

                ObjectId mdObjectId = inserter.insert(Constants.OBJ_BLOB, bytes.length, inputStream);
                inserter.flush();
                inserter.close();
                treeFormatter.append("README.md", FileMode.REGULAR_FILE, mdObjectId);
            }

            ObjectId treeId = inserter.insert(treeFormatter);
            //设置提交信息
            CommitBuilder commitBuilder = new CommitBuilder();
            commitBuilder.setTreeId(treeId);
            String email = user.getEmail();
            if (!RepositoryUtil.isNoNull(email)){
                email = user.getName();
            }
            PersonIdent ident = new PersonIdent(user.getName(), email);
            commitBuilder.setCommitter(ident);
            commitBuilder.setAuthor(ident);
            commitBuilder.setMessage("Initial Commit");
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

        }catch (IOException e){
            throw new ApplicationException("初始化文件到仓库失败：" + e);
        }


    }

    /**
     * 克隆仓库
     * @param repositoryAddress 仓库地址
     * @param branch 分支
     * @param cloneAddress  克隆地址
     * @throws GitAPIException 克隆失败
     */
    public static void cloneRepository(String repositoryAddress,String branch,String cloneAddress)   {
        try {
            File folder = new File(cloneAddress);
            if (!folder.exists() && !folder.isDirectory()) {
                folder.mkdirs();
            }
            Git git = Git.cloneRepository()
                    .setURI(repositoryAddress)
                    .setDirectory(folder)
                    .setBranch(branch)
                    .call();
            git.close();
        }catch (Exception e){
            throw  new ApplicationException(e.getMessage());
        }

    }

    /**
     * 克隆所有分支的仓库
     * @param repositoryAddress 仓库地址
     * @param cloneAddress  克隆地址
     * @throws GitAPIException 克隆失败
     */
    public static void cloneAllBranchRepository(String repositoryAddress,String cloneAddress) throws GitAPIException {
        File folder = new File(cloneAddress);
        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
        }
        Git git = Git.cloneRepository()
                .setURI(repositoryAddress)
                .setDirectory(folder)
                .setCloneAllBranches(true)
                .call();
        git.close();
    }

    /**
     * push 所有分支到裸仓库
     * @param normalRepoDir 本地仓库
     * @param bareRepoDir  远程仓库
     * @throws GitAPIException 克隆失败
     */
    public static void pushAllBranchRepository(String normalRepoDir,String bareRepoDir) throws Exception {
        // 打开普通仓库
        Repository normalRepository = Git.open(new File(normalRepoDir)).getRepository();

        // 打开远程仓库
        Repository bareRepository = Git.open(new File(bareRepoDir)).getRepository();


        // 推送普通仓库到裸仓库
        Git git = new Git(normalRepository);
        git.push()
            .setRemote(bareRepository.getDirectory().getAbsolutePath())
                .setRefSpecs(new RefSpec("refs/heads/*:refs/heads/*"))  //推送所有分支
                .setForce(true)  //强制推送
                .call();

        // 关闭仓库
        normalRepository.close();
        bareRepository.close();

    }


    /**
     * 更新仓库
     * @param repositoryAddress 仓库地址
     */
    public static void pullRepository(String repositoryAddress,String branch) throws IOException, GitAPIException {
        Git git = Git.open(new File(repositoryAddress+"_"+branch));
        git.pull()
                .setRebase(false)
                .setRemoteBranchName(branch) //拉取分支
                .call();
        git.close();
    }


    /**
     * 推送到远程
     * @param repositoryAddress 仓库地址
     * @param branch 分支
     * @throws IOException 仓库不存在
     * @throws URISyntaxException 远程地址错误
     * @throws GitAPIException 推送失败
     */
    public static void remoteRepositoryFile(String repositoryAddress,String branch) throws IOException, URISyntaxException, GitAPIException {
        Git git = Git.open(new File(repositoryAddress));

        git.remoteAdd() //远程仓库
                .setName("origin")
                .setUri(new URIish(repositoryAddress+".git"))
                .call();
        git.push()
                .setPushAll()
                .call();
        git.close();
    }

    /**
     * 删除远程文件
     * @param repositoryAddress 仓库地址
     * @param fileAddress 文件地址
     * @throws IOException 仓库不存在
     * @throws URISyntaxException 远程地址错误
     * @throws GitAPIException 推送失败
     */
    public static void deleteRepositoryFile(String repositoryAddress,String fileAddress) throws IOException, GitAPIException, URISyntaxException {
        Git git = Git.open(new File(repositoryAddress));
        git.remoteAdd() //添加远程仓库
                .setName("origin")
                .setUri(new URIish(repositoryAddress+".git"))
                .call();
        git.rm()
                .addFilepattern(fileAddress)
                .setCached(false) //true 仅从索引中删除 false 索引文件都删除
                .call();
        git.close();
    }


    /**
     * 推送到第三方仓库
     * @param repositoryAddress 仓库地址
     * @param remoteInfo 分支
     * @throws IOException 仓库不存在
     * @throws URISyntaxException 远程地址错误
     * @throws GitAPIException 推送失败
     */
    public static void remoteRepository(String repositoryAddress, RemoteInfo remoteInfo) throws IOException, URISyntaxException, GitAPIException {

        Git git = Git.open(new File(repositoryAddress));
        git.remoteSetUrl()
                .setRemoteName("origin")
                .setRemoteUri(new URIish(remoteInfo.getAddress()))
                .call();

        if (("password").equals(remoteInfo.getAuthWay())) {

            //账号密码认证
            UsernamePasswordCredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(remoteInfo.getAccount(), remoteInfo.getPassword());
            git.push().setCredentialsProvider(credentialsProvider)
                    .setPushAll()
                    .call();
        }
        //通过令牌推送
        if (("token").equals(remoteInfo.getAuthWay())){
            PushCommand pushCommand = git.push();
            pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(remoteInfo.getSecretKey(), ""));
            pushCommand.call();
        }


           /* //ssh 认证
            SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
                @Override
                protected void configure(OpenSshConfig.Host host, Session session) {
                    // Set any SSH session configurations if needed
                }

                @Override
                protected JSch createDefaultJSch(FS fs) throws JSchException {
                    JSch jSch = super.createDefaultJSch(fs);
                    jSch.addIdentity(remoteInfo.getSecretKey());
                    return jSch;
                }
            };
            git.push().setTransportConfigCallback(transport -> {
                if (transport instanceof SshTransport) {
                    SshTransport sshTransport = (SshTransport) transport;
                    sshTransport.setSshSessionFactory(sshSessionFactory);
                }
            }).setPushAll().call();
        }*/
            git.close();
    }

    /**
     * 复制luo仓库
     * @param localAddress 复制的地址
     * @param remoteAddress 远程仓库地址
     *
     */
    public static void copyRepository(String localAddress,String remoteAddress, LeadAuth importAuth ) throws GitAPIException {
        File localPath = new File(localAddress);
        UsernamePasswordCredentialsProvider credentialsProvider =
                new UsernamePasswordCredentialsProvider("access_token", importAuth.getAccessToken());
        Git.cloneRepository()
                .setURI(remoteAddress)
                .setDirectory(localPath)
                .setBare(true)
                .setCredentialsProvider(credentialsProvider)
                .call();
    }

    /**
     *执行git 命令
     * @param order 命令
     * @return
     */
    public static Process execGitOrder(String order,String path) throws IOException {

        Runtime runtime=Runtime.getRuntime();
        Process process;
        if (RepositoryUtil.findSystemType()==1){
            String[]  cmd = new String[] { "cmd.exe /c", "-c", "cd " + path + ";" +order };
            process = runtime.exec(cmd,null,new File(path));
        }else {
            String[]  cmd = new String[] { "/bin/sh", "-c", "cd " + path + ";"+ order };
            process = runtime.exec(cmd,null,new File(path));
        }
        return process;
    }
}
