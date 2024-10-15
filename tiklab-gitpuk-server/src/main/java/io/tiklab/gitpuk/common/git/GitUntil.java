package io.tiklab.gitpuk.common.git;


import io.tiklab.gitpuk.repository.model.LeadAuth;
import io.tiklab.gitpuk.repository.model.RemoteInfo;
import io.tiklab.gitpuk.common.RepositoryUtil;
import io.tiklab.core.exception.ApplicationException;
import io.tiklab.user.user.model.User;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.transport.*;

import java.io.*;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;


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
    public static void addRepositoryInitFile(Git git, io.tiklab.gitpuk.repository.model.Repository rpy, String ignoreFilePath) {

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
     * 通过file协议将裸仓库转为普通仓库
     * @param bareRepositoryPath 裸仓库地址
     * @param  freeRepositoryPath 普通仓库地址
     * @param branch 分支
     */
    public static void cloneRepositoryByFile(String bareRepositoryPath,String freeRepositoryPath,String branch) throws GitAPIException {
        Git git = Git.cloneRepository()
                .setURI("file://" + bareRepositoryPath)
                .setDirectory(new File(freeRepositoryPath))
                .setBranch(branch)
               /* .setBare(true)*/
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
            git.close();
    }

    /**
     * 复制luo仓库
     * @param localAddress 复制的地址
     * @param remoteAddress 远程仓库地址
     *
     */
    public static void copyRepository(String localAddress, String remoteAddress, LeadAuth importAuth, String userAccount ) throws GitAPIException {
       File localPath = new File(localAddress);
        UsernamePasswordCredentialsProvider credentialsProvider;
       if(("priGitlab").equals(importAuth.getType())||("gitlab").equals(importAuth.getType())||("github").equals(importAuth.getType())){
           credentialsProvider= new UsernamePasswordCredentialsProvider("access_token", importAuth.getAccessToken());
       }else if(("gitee").equals(importAuth.getType())) {
           credentialsProvider= new UsernamePasswordCredentialsProvider(userAccount,importAuth.getAccessToken());
       }else {
           credentialsProvider= new UsernamePasswordCredentialsProvider("limingliang",importAuth.getAccessToken());
       }

        /*UsernamePasswordCredentialsProvider credentialsProvider =
                new UsernamePasswordCredentialsProvider("admin", importAuth.getAccessToken());*/
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


    /**
     * 克隆仓库所有分支
     * @param repositoryPath 裸仓库地址
     * @param  clonePath 普通仓库地址
     */
    public static void cloneRepositoryAllBranch(String repositoryPath,String clonePath){

        try {
            //只会clone 默认分支
            Git git = Git.cloneRepository()
                    .setURI("file://"+repositoryPath)
                    .setDirectory(new File(clonePath))
                    .call();

            Repository repository = git.getRepository();
            //普通仓库的分支
            List<Ref> branchList = git.branchList().call();

            //获取普通仓库的引用和远程引用
            List<Ref> refs = repository.getRefDatabase().getRefs();
            String afterLast = StringUtils.substringAfterLast(branchList.get(0).getName(), "/");
            //获取普通仓库中的远程分支
            List<Ref> collect = refs.stream().filter(a -> !a.getName().endsWith(afterLast)).collect(Collectors.toList());

            String defultName=null;
            for (Ref branch : collect) {
                String branchName = branch.getName();
                //排除标签
                if (branchName.contains(Constants.R_TAGS)){
                    continue;
                }
                if (branchName.equals("HEAD")){
                    defultName = branch.getTarget().getName();
                }

                if (!branchName.equals("HEAD")&&!branchName.equals(defultName)
                        && branchName.startsWith("refs/remotes/origin/")) {

                    String after = StringUtils.substringAfterLast(branchName, "/");
                    git.checkout()
                            .setName(after)
                            .setCreateBranch(true)
                            .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
                            .setStartPoint("refs/remotes/origin/"+after)
                            .call();
                }
            }
            //切换到最初的默认分支
            git.checkout().setName(afterLast).call();

        }catch (Exception e){
            throw new ApplicationException("clone 仓库失败:"+e.getMessage());
        }
    }




}
