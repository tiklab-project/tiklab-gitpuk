package io.tiklab.xcode.git;


import io.tiklab.core.exception.ApplicationException;
import io.tiklab.user.user.model.User;
import io.tiklab.xcode.repository.model.RemoteInfo;
import io.tiklab.xcode.common.RepositoryUtil;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.transport.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    public static void createRepository(String repositoryAddress, String ignoreFilePath, String mdFilePath, User user) throws ApplicationException {
        File file = new File(repositoryAddress);
        Git git;
        try {
            git = Git.init()
                    .setDirectory(file)
                    .setBare(true) //裸仓库
                    //.setInitialBranch(Constants.MASTER)
                    .call();

            // Repository repository = git.getRepository();
            //
            // if (ignoreFilePath != null || mdFilePath != null){
            //     addRepositoryInitFile(repository,ignoreFilePath,mdFilePath,user);
            // }
            git.close();
        } catch (GitAPIException e) {
            throw new ApplicationException("仓库创建失败。" + e);
        }
        git.close();
    }

    /**
     * 裸仓库添加文件
     * @param repository 仓库
     * @param ignoreFilePath .gitignore文件
     * @param mdFilePath README.md文件
     * @param user 用户
     */
    private static void addRepositoryInitFile(Repository repository, String ignoreFilePath, String mdFilePath, User user)
            throws ApplicationException {

        try {

            ObjectDatabase objectDatabase = repository.getObjectDatabase();
            ObjectInserter inserter = objectDatabase.newInserter();

            TreeFormatter treeFormatter = new TreeFormatter();
            // 写入.gitignore文件到对象数据库
            if (RepositoryUtil.isNoNull(ignoreFilePath)){

                File ignoreFile = new File(ignoreFilePath);
                FileInputStream inputStream ;
                try {
                    inputStream = new FileInputStream(ignoreFile);
                } catch (FileNotFoundException e) {
                    throw new ApplicationException("文件.gitignore写入失败：" + e);
                }

                ObjectId objectId = inserter.insert(Constants.OBJ_BLOB, ignoreFile.length(), inputStream);
                inserter.flush();
                inserter.close();
                treeFormatter.append(".gitignore", FileMode.REGULAR_FILE, objectId);
            }

            // 写入README.md文件到对象数据库
            if (RepositoryUtil.isNoNull(mdFilePath)){

                File mdFile = new File(mdFilePath);
                FileInputStream mdFileInputStream ;
                try {
                    mdFileInputStream = new FileInputStream(mdFile);
                } catch (FileNotFoundException e) {
                    throw new ApplicationException("文件README.md写入失败：" + e);
                }

                ObjectId mdObjectId = inserter.insert(Constants.OBJ_BLOB, mdFile.length(), mdFileInputStream);
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
        } else {

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
    }


}






























































