package io.thoughtware.gittok.repository.service;

import io.thoughtware.core.exception.ApplicationException;
import io.thoughtware.core.exception.SystemException;
import io.thoughtware.gittok.common.GitTokYamlDataMaService;
import io.thoughtware.gittok.common.RepositoryUtil;
import io.thoughtware.gittok.common.git.GitUntil;
import io.thoughtware.gittok.repository.model.RepositoryClean;
import io.thoughtware.gittok.repository.model.RepositoryCleanQuery;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.errors.NotSupportedException;
import org.eclipse.jgit.errors.TransportException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.transport.Transport;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class RepositoryCleanServiceImpl implements RepositoryCleanService{

    private static Logger logger = LoggerFactory.getLogger(RepositoryCleanServiceImpl.class);

    @Autowired
    GitTokYamlDataMaService gitTorkYamlDataMaService;

    @Autowired
    RepositoryServer repositoryServer;


    //文件
    public static Map<String , List<RepositoryClean>> fileMap = new HashMap<>();


    //删除结果
    public static Map<String ,Map<String ,String>> clearLogResult = new HashMap<>();

    //执行开始时间
    public static Map<String , Date> scanExecStarTime = new HashMap<>();


    @Override
    public  String findLargeFile(RepositoryCleanQuery repositoryCleanQuery){
        long startTime = System.currentTimeMillis();
        fileMap.remove(repositoryCleanQuery.getRepositoryId());
        String rpyPath = gitTorkYamlDataMaService.repositoryAddress() + "/" + repositoryCleanQuery.getRepositoryId() + ".git";

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(new Runnable(){
            @Override
            public void run() {
                //最后结果的list
                List<RepositoryClean> arrayList = new ArrayList<>();
                try {
                    //文件的list
                    List<RepositoryClean> fileList = new ArrayList<>();
                    Git git = Git.open(new File(rpyPath));
                    Repository repository = git.getRepository();
                  /*  RevWalk revWalk = new RevWalk(repository);

                    Ref head = repository.getRefDatabase().exactRef(Constants.HEAD);
                    RevCommit commit = revWalk.parseCommit(head.getObjectId());
                    revWalk.markStart(commit);*/

                    //long sizeMb = repositoryCleanQuery.getFileSize() * 1048576;

                    long sizeMb =138860;
                    //获取项目所有提交
                    Iterable<RevCommit> commits = git.log().all().call();

                    for (RevCommit revCommit : commits) {
                        RevTree tree = revCommit.getTree();
                        TreeWalk treeWalk = new TreeWalk(repository);
                        treeWalk.addTree(tree);
                        treeWalk.setRecursive(true);
                        while (treeWalk.next()) {
                            ObjectId objectId = treeWalk.getObjectId(0);
                            try {
                                ObjectReader objectReader = repository.newObjectReader();

                                /**
                                 * 查询文件大小
                                 * @param Constants.OBJ_BLOB：表示对象是一个blob（文件）。
                                 * @param Constants.OBJ_TREE：表示对象是一个tree（文件树）。
                                 * @param Constants.OBJ_COMMIT：表示对象是一个commit（提交）。
                                 * @param Constants.OBJ_TAG：表示对象是一个tag（标签）。
                                 */
                                long objectSize = objectReader.getObjectSize(objectId, Constants.OBJ_BLOB);
                                if (objectSize>=sizeMb){
                                    RepositoryClean repositoryClean = new RepositoryClean();
                                    repositoryClean.setFileName(treeWalk.getPathString());
                                    repositoryClean.setFileSize(objectSize);
                                    String size = RepositoryUtil.countStorageSize(objectSize);
                                    repositoryClean.setSize(size);
                                    fileList.add(repositoryClean);
                                    logger.info("文件："+treeWalk.getPathString()+" " +objectSize);
                                }
                            }catch (Exception e){
                                //没有查询到当前文件直接跳过
                                if (e.getMessage().contains("Missing unknown")){
                                    continue;
                                }
                            }
                        }
                        treeWalk.close();
                   }

                    //如果查询为空
                    if (CollectionUtils.isEmpty(fileList)){
                        RepositoryClean repositoryClean = new RepositoryClean();
                        repositoryClean.setMsg("none");
                        arrayList.add(repositoryClean);
                    }else {
                        //不为空去重留最大的file
                        Map<String, List<RepositoryClean>> collect = fileList.stream().collect(Collectors.groupingBy(RepositoryClean::getFileName));
                        Set<String> strings = collect.keySet();
                        for (String key:strings){
                            List<RepositoryClean> repositoryCleans = collect.get(key).stream().sorted(Comparator.comparing(RepositoryClean::getFileSize).reversed()).collect(Collectors.toList());
                            arrayList.add(repositoryCleans.get(0));
                        }
                    }

                    fileMap.put(repositoryCleanQuery.getRepositoryId(),arrayList);
                }catch (Exception e){
                    RepositoryClean repositoryClean = new RepositoryClean();
                    repositoryClean.setMsg("fail");
                    arrayList.add(repositoryClean);
                    fileMap.put(repositoryCleanQuery.getRepositoryId(),arrayList);
                    logger.info("执行错误："+e.getMessage());
                    throw new SystemException(900,e.getMessage());
                }
            }});
        return "OK" ;
    }


    @Override
    public List<RepositoryClean> findLargeFileResult(RepositoryCleanQuery repositoryCleanQuery) {
        List<RepositoryClean> repositoryCleans = fileMap.get(repositoryCleanQuery.getRepositoryId());
        if (CollectionUtils.isNotEmpty(repositoryCleans)){
            if (StringUtils.isNotEmpty(repositoryCleanQuery.getSort())&&("asc").equals(repositoryCleanQuery.getSort())){
                 repositoryCleans = repositoryCleans.stream().sorted(Comparator.comparing(RepositoryClean::getFileSize)).collect(Collectors.toList());
            }else {
                repositoryCleans = repositoryCleans.stream().sorted(Comparator.comparing(RepositoryClean::getFileSize).reversed()).collect(Collectors.toList());
            }
        }
        return repositoryCleans;
    }

    @Override
    public String execCleanFile(String rpyId) {


        String rpyPath = gitTorkYamlDataMaService.repositoryAddress() + "/" + rpyId + ".git";

        try {
            Git git = Git.open(new File(rpyPath));
            Repository repository = git.getRepository();


            git.gc().setExpire(new java.util.Date()).call();

            // 关闭Git仓库
            repository.close();


            File folder = new File(rpyPath+"/objects/pack");
            if (folder.isDirectory()) {
                File[] files = folder.listFiles();
                if (files != null) {
                    List<File> idxFileList = new ArrayList<>();
                    List<File> packFileList = new ArrayList<>();
                    for (File file : files) {
                        if (file.getName().endsWith(".idx")){
                            idxFileList.add(file);
                        }
                        if (file.getName().endsWith(".pack")){
                            packFileList.add(file);
                        }
                    }


                    // 按照创建时间进行排序
                    idxFileList.sort(Comparator.comparingLong(File::lastModified).reversed());
                    for (int i=0;i<idxFileList.size();i++){
                        String name = idxFileList.get(i).getName();
                        if (i>0){
                            idxFileList.get(i).delete();
                        }
                    }
                    packFileList.sort(Comparator.comparingLong(File::lastModified).reversed());
                    for (int i=0;i<packFileList.size();i++){
                        if (i>0){
                            packFileList.get(i).delete();
                        }
                    }
                }
            }

            io.thoughtware.gittok.repository.model.Repository rpy = repositoryServer.findOneRpy(rpyId);
            //修改数据库中仓库大小
            long logBytes = FileUtils.sizeOfDirectory(new File(rpyPath));
            rpy.setSize(logBytes);
            repositoryServer.updateRepository(rpy);
            // 关闭对象
            return "ok";
        }catch (Exception e){
            throw new SystemException(e);
        }
    }

/*    @Override
    public String clearLargeFile(RepositoryCleanQuery repositoryCleanQuery) {

        String repositoryId = repositoryCleanQuery.getRepositoryId();
        scanExecStarTime.put(repositoryId,new Date(System.currentTimeMillis()));
        //清除删除日志
       if (StringUtils.isNotEmpty(repositoryId)){
           clearLogResult.remove(repositoryCleanQuery.getRepositoryId());

           joinClearLargeFileLog(repositoryId,"开始清除大文件","run");


           ExecutorService executorService = Executors.newCachedThreadPool();
           executorService.submit(new Runnable(){
               @Override
               public void run() {
                   String rpyPath = gitTorkYamlDataMaService.repositoryAddress() + "/" + repositoryCleanQuery.getRepositoryId() + ".git";
                   //清理仓库的地址
                   String cleanRpyPath=gitTorkYamlDataMaService.repositoryAddress()+"/clean/"+repositoryCleanQuery.getRepositoryId();
                   try{
                       File file = new File(cleanRpyPath);
                       if (file.exists()) {
                           FileUtils.deleteDirectory(new File(cleanRpyPath));
                       }
                       //克隆所有分支的仓库
                       logger.info("开始准备清理仓库的仓库" );
                       joinClearLargeFileLog(repositoryId,"准备清理仓库","run");
                       GitUntil.cloneAllBranchRepository(rpyPath,cleanRpyPath);
                       joinClearLargeFileLog(repositoryId,"成功准备清理仓库","run");

                       //进入执行环境
                       logger.info("进入清理仓库的仓库" );
                       joinClearLargeFileLog(repositoryId,"进入清理仓库的仓库","run");
                       String cd="cd "+cleanRpyPath;
                       execOrder(cd,cleanRpyPath,repositoryId);
                       joinClearLargeFileLog(repositoryId,"成功进入清理仓库的仓库","run");

                       //执行stash命令
                       logger.info("保存临时修改文件");
                       joinClearLargeFileLog(repositoryId,"执行保存临时修改文件","run");
                       String stash="git stash";
                       execOrder(stash,cleanRpyPath,repositoryId);
                       joinClearLargeFileLog(repositoryId,"成功执行保存临时修改文件","run");

                       //删除文件
                       removeFile(repositoryCleanQuery, cleanRpyPath);

                       //回收空间
                       logger.info("删除原始引用");
                       joinClearLargeFileLog(repositoryId,"执行删除原始引用","run");
                       String eachRef= "git for-each-ref --format='delete %(refname)' refs/original | git update-ref --stdin";
                       execOrder(eachRef,cleanRpyPath,repositoryId);
                       joinClearLargeFileLog(repositoryId,"成功执行删除原始引用","run");

                       logger.info("删除.git/refs/original下面引用");
                       joinClearLargeFileLog(repositoryId,"删除.git/refs/original下面引用","run");
                       String rmRf= "rm -rf .git/refs/original/";
                       execOrder(rmRf,cleanRpyPath,repositoryId);
                       joinClearLargeFileLog(repositoryId,"成功删除.git/refs/original下面引用","run");

                       logger.info("删除日志引用");
                       joinClearLargeFileLog(repositoryId,"删除日志引用","run");
                       String reflog= "git reflog expire --expire=now --all";
                       execOrder(reflog,cleanRpyPath,repositoryId);
                       joinClearLargeFileLog(repositoryId,"成功删除日志引用","run");

                       logger.info("执行垃圾回收清理仓库中删除不可达的对象，优化Git仓库的大小和性能");
                       joinClearLargeFileLog(repositoryId,"执行清理仓库垃圾回收删除不可达的对象，优化Git仓库的大小和性能","run");
                       String prune= "git gc --prune=now";
                       execOrder(prune,cleanRpyPath,repositoryId);
                       joinClearLargeFileLog(repositoryId,"成功执行清理仓库垃圾回收删除不可达的对象，优化Git仓库的大小和性能","run");

                       //推送清理后的仓库
                       logger.info("推送最新的仓库到裸仓库中");
                       joinClearLargeFileLog(repositoryId,"替换清理后的仓库","run");
                       GitUntil.pushAllBranchRepository(cleanRpyPath,rpyPath);
                       joinClearLargeFileLog(repositoryId,"成功替换清理后的仓库","run");

                       //进入裸仓库
                       logger.info("进入裸仓库中");
                       joinClearLargeFileLog(repositoryId,"进入裸仓库中","run");
                       String cdRpy= "cd "+rpyPath;
                       execOrder(cdRpy,rpyPath,repositoryId);
                       joinClearLargeFileLog(repositoryId,"成功进入裸仓库中","run");

                       //清理裸仓库中的无效文件
                       logger.info("执行裸仓库中垃圾回收删除不可达的对象，优化Git仓库的大小和性能");
                       joinClearLargeFileLog(repositoryId,"执行裸仓库中垃圾回收删除不可达的对象，优化Git仓库的大小和性能","run");
                       String gcPrune= "git gc --prune=now ";
                       execOrder(gcPrune,rpyPath,repositoryId);
                       joinClearLargeFileLog(repositoryId,"成功执行裸仓库中垃圾回收删除不可达的对象，优化Git仓库的大小和性能","run");

                       //删除清理仓库
                       logger.info("删除清理仓库");
                       FileUtils.deleteDirectory(new File(cleanRpyPath));

                       //修改仓库大小
                       logger.info("修改仓库");
            io.thoughtware.gittok.repository.model.Repository repository = repositoryServer.findOneOnlyRpy(repositoryCleanQuery.getRepositoryId());
            if (ObjectUtils.isNotEmpty(repository)){
                long length = new File(rpyPath).length();
                repository.setSize(length);
            }
            repositoryServer.updateRepository(repository);
                       logger.info("执行成功");
                       joinClearLargeFileLog(repositoryId,"清除成功","success");
                   }catch (Exception e){
                       logger.info("操作失败"+e.getMessage() );
                       joinClearLargeFileLog(repositoryId,"清除失败："+e.getMessage(),"fail");
                   }
               }});
           return "OK";
       }


        return "fail";

    }*/

    @Override
    public String clearLargeFile(RepositoryCleanQuery repositoryCleanQuery) {

        String repositoryId = repositoryCleanQuery.getRepositoryId();
        scanExecStarTime.put(repositoryId,new Date(System.currentTimeMillis()));
        //清除删除日志
        if (StringUtils.isNotEmpty(repositoryId)){
            clearLogResult.remove(repositoryCleanQuery.getRepositoryId());

            joinClearLargeFileLog(repositoryId,"开始清除大文件","run");

            String rpyPath = gitTorkYamlDataMaService.repositoryAddress() + "/" + repositoryCleanQuery.getRepositoryId() + ".git";
            //清理仓库的地址
            String cleanRpyPath=gitTorkYamlDataMaService.repositoryAddress()+"/clean/"+repositoryCleanQuery.getRepositoryId();

            testDelete(cleanRpyPath,repositoryCleanQuery);
       /*     ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.submit(new Runnable(){
                @Override
                public void run() {
                    String rpyPath = gitTorkYamlDataMaService.repositoryAddress() + "/" + repositoryCleanQuery.getRepositoryId() + ".git";
                    //清理仓库的地址
                    String cleanRpyPath=gitTorkYamlDataMaService.repositoryAddress()+"/clean/"+repositoryCleanQuery.getRepositoryId();


                  *//*  try {
                        File file = new File(cleanRpyPath);
                        if (file.exists()) {
                            FileUtils.deleteDirectory(new File(cleanRpyPath));
                        }
                        GitUntil.cloneAllBranchRepository(rpyPath,cleanRpyPath);

                        Git git = Git.open(new File(cleanRpyPath));
                        RmCommand rmCommand = git.rm();

                        for (String fileName:repositoryCleanQuery.getFileNameList()){
                            rmCommand.addFilepattern(fileName);
                        }
                        rmCommand.call();
                    } catch (GitAPIException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }*//*

                   testDelete(rpyPath,cleanRpyPath,repositoryCleanQuery);



                   *//* try{
                        File file = new File(cleanRpyPath);
                        if (file.exists()) {
                            FileUtils.deleteDirectory(new File(cleanRpyPath));
                        }
                        //克隆所有分支的仓库
                        logger.info("开始准备清理仓库的仓库" );
                        joinClearLargeFileLog(repositoryId,"准备清理仓库","run");
                        GitUntil.cloneAllBranchRepository(rpyPath,cleanRpyPath);
                        joinClearLargeFileLog(repositoryId,"成功准备清理仓库","run");

                        //进入执行环境
                        logger.info("进入清理仓库的仓库" );
                        joinClearLargeFileLog(repositoryId,"进入清理仓库的仓库","run");
                        String cd="cd "+cleanRpyPath;
                        execOrder(cd,cleanRpyPath,repositoryId);
                        joinClearLargeFileLog(repositoryId,"成功进入清理仓库的仓库","run");


                        Git git = Git.open(new File(cleanRpyPath));
                        // 暂存当前工作目录的修改
                        logger.info("保存临时修改文件");
                        joinClearLargeFileLog(repositoryId,"执行保存临时修改文件","run");
                        StashCreateCommand stashCreateCommand = git.stashCreate();
                        stashCreateCommand.call();
                        joinClearLargeFileLog(repositoryId,"成功执行保存临时修改文件","run");


                        RmCommand rm = git.rm();
                        List<String> fileNameList = repositoryCleanQuery.getFileNameList();
                        for (String fileName:fileNameList){
                            rm.addFilepattern(fileName);

                        }
                        rm.call();

                        // 提交删除操作
                        git.commit()
                                .setMessage("Remove large files")
                                .call();

                        // 推送到远程仓库
                        git.push()
                                .setPushAll()
                                .call();

                     *//**//*   //执行stash命令
                        logger.info("保存临时修改文件");
                        joinClearLargeFileLog(repositoryId,"执行保存临时修改文件","run");
                        String stash="git stash";
                        execOrder(stash,cleanRpyPath,repositoryId);
                        joinClearLargeFileLog(repositoryId,"成功执行保存临时修改文件","run");

                        //删除文件
                       // removeFile(repositoryCleanQuery, cleanRpyPath);
                        removeFile(repositoryCleanQuery,git);

                        //回收空间
                        logger.info("删除原始引用");
                        joinClearLargeFileLog(repositoryId,"执行删除原始引用","run");
                        String eachRef= "git for-each-ref --format='delete %(refname)' refs/original | git update-ref --stdin";
                        execOrder(eachRef,cleanRpyPath,repositoryId);
                        joinClearLargeFileLog(repositoryId,"成功执行删除原始引用","run");

                      logger.info("删除.git/refs/original下面引用");
                        joinClearLargeFileLog(repositoryId,"删除.git/refs/original下面引用","run");
                        String rmRf= "rm -rf .git/refs/original/";
                        execOrder(rmRf,cleanRpyPath,repositoryId);
                        joinClearLargeFileLog(repositoryId,"成功删除.git/refs/original下面引用","run");

                          logger.info("删除日志引用");
                        joinClearLargeFileLog(repositoryId,"删除日志引用","run");
                        String reflog= "git reflog expire --expire=now --all";
                        execOrder(reflog,cleanRpyPath,repositoryId);
                        joinClearLargeFileLog(repositoryId,"成功删除日志引用","run");

                        logger.info("执行垃圾回收清理仓库中删除不可达的对象，优化Git仓库的大小和性能");
                        joinClearLargeFileLog(repositoryId,"执行清理仓库垃圾回收删除不可达的对象，优化Git仓库的大小和性能","run");
                        String prune= "git gc --prune=now";
                        execOrder(prune,cleanRpyPath,repositoryId);
                        joinClearLargeFileLog(repositoryId,"成功执行清理仓库垃圾回收删除不可达的对象，优化Git仓库的大小和性能","run");

                        //推送清理后的仓库
                        logger.info("推送最新的仓库到裸仓库中");
                        joinClearLargeFileLog(repositoryId,"替换清理后的仓库","run");
                        GitUntil.pushAllBranchRepository(cleanRpyPath,rpyPath);
                        joinClearLargeFileLog(repositoryId,"成功替换清理后的仓库","run");

                        //进入裸仓库
                        logger.info("进入裸仓库中");
                        joinClearLargeFileLog(repositoryId,"进入裸仓库中","run");
                        String cdRpy= "cd "+rpyPath;
                        execOrder(cdRpy,rpyPath,repositoryId);
                        joinClearLargeFileLog(repositoryId,"成功进入裸仓库中","run");

                        //清理裸仓库中的无效文件
                        logger.info("执行裸仓库中垃圾回收删除不可达的对象，优化Git仓库的大小和性能");
                        joinClearLargeFileLog(repositoryId,"执行裸仓库中垃圾回收删除不可达的对象，优化Git仓库的大小和性能","run");
                        String gcPrune= "git gc --prune=now ";
                        execOrder(gcPrune,rpyPath,repositoryId);
                        joinClearLargeFileLog(repositoryId,"成功执行裸仓库中垃圾回收删除不可达的对象，优化Git仓库的大小和性能","run");
*//**//*
                        //删除清理仓库
                        logger.info("删除清理仓库");
                        FileUtils.deleteDirectory(new File(cleanRpyPath));

                        //修改仓库大小
                        logger.info("修改仓库");
                        io.thoughtware.gittok.repository.model.Repository repository = repositoryServer.findOneOnlyRpy(repositoryCleanQuery.getRepositoryId());
                        if (ObjectUtils.isNotEmpty(repository)){
                            long length = new File(rpyPath).length();
                            repository.setSize(length);
                        }
                        repositoryServer.updateRepository(repository);
                        logger.info("执行成功");
                        joinClearLargeFileLog(repositoryId,"清除成功","success");
                    }catch (Exception e){
                        logger.info("操作失败"+e );
                        joinClearLargeFileLog(repositoryId,"清除失败："+e.getMessage(),"fail");
                    }*//*
                }});*/
            return "OK";
        }


        return "fail";

    }
    @Override
    public Map<String, String> findClearResult(String repositoryId)   {

        Map<String, String> stringMap = clearLogResult.get(repositoryId);
        if (ObjectUtils.isNotEmpty(stringMap)){
            if (("success").equals(stringMap.get("state"))){
                String rpyPath = gitTorkYamlDataMaService.repositoryAddress() + "/" + repositoryId + ".git";
                io.thoughtware.gittok.repository.model.Repository repository = repositoryServer.findOneOnlyRpy(repositoryId);
                if (ObjectUtils.isNotEmpty(repository)) {
                    long sizeOf = FileUtils.sizeOf(new File(rpyPath));
                    repository.setSize(sizeOf);
                    repositoryServer.updateRepository(repository);
                }
            }
            //开始时间
            Date date = scanExecStarTime.get(repositoryId);

            String time = RepositoryUtil.time(date,"clean");
            stringMap.put("timeLong",time);
        }

        return stringMap;
    }


    /**
     *  通过文件执行删除
     *  @param repositoryCleanQuery:清除信息
     * @param git git
     */
    public void removeFile(RepositoryCleanQuery repositoryCleanQuery,Git git) throws Exception {

        String fileName = repositoryCleanQuery.getFileName();
        // 获取所有提交对象
        Iterable<RevCommit> commits = git.log().all().call();
        for (RevCommit commit : commits) {
            RevTree tree = commit.getTree();
            try (TreeWalk treeWalk = new TreeWalk(git.getRepository())) {
                treeWalk.addTree(tree);
                treeWalk.setRecursive(true);
                while (treeWalk.next()) {
                    String pathString = treeWalk.getPathString();
                    if (pathString.equals(fileName)) {
                        logger.info("文件："+pathString);
                        RmCommand rmCommand = git.rm();
                        rmCommand.setCached(true);   // 将文件从暂存区中删除
                        rmCommand.addFilepattern(fileName);
                        rmCommand.call();


                        // Keep the changes in the working directory
                        git.checkout().setAllPaths(true).call();
                    }
                }
            }
        }
    }


    /**
     *  通过文件执行删除
     *  @param repositoryCleanQuery:清除信息
     * @param cleanRpyPath 清理仓库地址
     */
    public void removeFile(RepositoryCleanQuery repositoryCleanQuery,String cleanRpyPath) throws Exception {
        List<String> fileNameList = repositoryCleanQuery.getFileNameList();
        String repositoryId = repositoryCleanQuery.getRepositoryId();
        for (String fileName:fileNameList){
            //执行清除命令
            logger.info("执行清除："+fileName);
            joinClearLargeFileLog(repositoryId,"执行清除文件："+fileName,"run");
            String clean="git filter-branch --force --index-filter 'git rm -rf --cached --ignore-unmatch "+fileName+"' --prune-empty --tag-name-filter cat -- --all";
            execOrder(clean,cleanRpyPath,repositoryId);
            joinClearLargeFileLog(repositoryId,"成功清除文件："+fileName,"run");
        }
    }


    /**
     *  执行git命令
     *  @param order:命令
     * @param path 执行环境地址
     * @param repositoryId 仓库id
     */
    public boolean execOrder(String order,String path,String repositoryId) throws Exception {
        Process process = GitUntil.execGitOrder(order,path);

        //读取执行的日志
        readFile(process,repositoryId);
        int waitFor = process.waitFor();
        if (waitFor==0){
            return true;
        }
        joinClearLargeFileLog(repositoryId,"执行失败","fail");
        logger.info("执行失败" );
        throw  new SystemException("执行失败");
    }



    /**
     *  执行日志
     *  @param process:执行结果
     * @param  repositoryId 仓库id
     */
    public void  readFile(Process process,String repositoryId) throws IOException {

        // 获取命令行输出
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        // mvn 编译日志
        while ((line = reader.readLine()) != null) {
            logger.info("执行命令日志:"+line);
            joinClearLargeFileLog(repositoryId,line,"run");
        }

        //spotBugs日志
        InputStream errorStream = process.getErrorStream();
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
        String errorLine;
        while ((errorLine = errorReader.readLine()) != null) {
            logger.info("执行命令日志02:"+errorLine);
            joinClearLargeFileLog(repositoryId,errorLine,"run");
        }
    }

    /**
     *  拼接清理大文件日志
     * @param  repositoryId 仓库id
     *  @param log 日志
     * @param  execState 执行状态
     */
    public void joinClearLargeFileLog(String repositoryId,String log,String execState){

        LocalDateTime now = LocalDateTime.now();
        // 自定义时间格式
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String customDateTime = now.format(customFormatter);
        //拼接的日志
        String resultLog = "["+customDateTime + "] " + log;

        Map<String, String> stringMap = clearLogResult.get(repositoryId);
        if (ObjectUtils.isEmpty(stringMap)){
            Map<String, String> resultMap = new HashMap<>();

            resultMap.put("startTime",customDateTime);
            resultMap.put("state",execState);
            resultMap.put("log",resultLog);
            clearLogResult.put(repositoryId,resultMap);
        }else {
            stringMap.put("state",execState);
            stringMap.put("log",stringMap.get("log")+"\n"+resultLog);
            clearLogResult.put(repositoryId,stringMap);
        }
    }



    public void deleteFile(String fileName){
        String repositoryPath="/Users/limingliang/test/thoughtware-hadess-cloud";

    }
/*
    public  String testDelete(String cleanRpyPath,RepositoryCleanQuery repositoryCleanQuery){
        try {
            String filePath = "tiklab-xpack-starter/embbed/jdk-16.0.2/lib/modules";
          *//*  File file = new File(cleanRpyPath);
            if (file.exists()) {
                FileUtils.deleteDirectory(new File(cleanRpyPath));
            }
            GitUntil.cloneAllBranchRepository(rpyPath,cleanRpyPath);*//*

            // 打开仓库
       *//*     Repository repository = new RepositoryBuilder()
                    .setGitDir(new File(cleanRpyPath))
                    .build();
            Git git = Git.wrap(repository);*//*
            Git git = Git.open(new File(cleanRpyPath));
            Repository repository = git.getRepository();


            RevWalk walk = new RevWalk(repository);
            // 获取所有提交
            Iterable<RevCommit> commits = git.log().all().call();
            for (RevCommit revCommit : commits) {
                // 重写提交的树
                git.checkout().setName(revCommit.getName()).call();
                TreeWalk treeWalk = new TreeWalk(repository);
                treeWalk.addTree(revCommit.getTree());
                treeWalk.setRecursive(true);
                treeWalk.setFilter(PathFilter.create(filePath));
                if (!treeWalk.next()) {
                    treeWalk.close();
                    continue;
                }

                git.rm().addFilepattern(filePath).call();
            }


            for (RevCommit revCommit : commits) {
                RevTree tree = revCommit.getTree();
                TreeWalk treeWalk = new TreeWalk(git.getRepository());
                treeWalk.addTree(tree);
                treeWalk.setRecursive(true);
                treeWalk.setFilter(PathFilter.create("tiklab-xpack-starter/embbed/jdk-16.0.2/lib/modules"));

                while (treeWalk.next()) {

                    String pathString = treeWalk.getPathString();
                    // 创建 RmCommand 对象
                    RmCommand rmCommand = git.rm();

                    // 添加要删除的目录到 RmCommand
                    rmCommand.addFilepattern("tiklab-xpack-starter/embbed/jdk-16.0.2/lib/modules");
                    rmCommand.call();
                }

                // 提交更改
                git.commit()
                        .setAuthor(revCommit.getAuthorIdent())
                        .setCommitter(revCommit.getCommitterIdent())
                        .setMessage(revCommit.getFullMessage())
                        .call();

            *//*    // 从工作目录和暂存区中删除文件
                git.rm().setCached(false).addFilepattern("tiklab-xpack-starter/embbed/jdk-16.0.2/lib/modules").call();




                // 提交更改
                git.commit()
                        .setAmend(true)
                        .setAuthor(revCommit.getAuthorIdent())
                        .setCommitter(revCommit.getCommitterIdent())
                        .setMessage(revCommit.getFullMessage())
                        .call();*//*

               *//* // 添加要删除的目录到 RmCommand
                rmCommand.addFilepattern("tiklab-xpack-starter/embbed/jdk-16.0.2/lib/modules");
                rmCommand.call();

                // 提交更改
                git.commit()
                        .setAuthor(revCommit.getAuthorIdent())
                        .setCommitter(revCommit.getCommitterIdent())
                        .setMessage(revCommit.getFullMessage())
                        .call();*//*

                *//*RevTree tree = revCommit.getTree();
                TreeWalk treeWalk = new TreeWalk(git.getRepository());
                treeWalk.addTree(tree);
                treeWalk.setRecursive(true);
                treeWalk.setFilter(PathFilter.create("tiklab-xpack-starter/embbed/jdk-16.0.2/lib/modules"));
                while (treeWalk.next()) {
                    String pathString = treeWalk.getPathString();
                    // 创建 RmCommand 对象
                    RmCommand rmCommand = git.rm();

                    // 添加要删除的目录到 RmCommand
                    rmCommand.addFilepattern("tiklab-xpack-starter/embbed/jdk-16.0.2/lib/modules");
                    rmCommand.call();

                    // 提交更改
                    git.commit()
                            .setAuthor(revCommit.getAuthorIdent())
                            .setCommitter(revCommit.getCommitterIdent())
                            .setMessage(revCommit.getFullMessage())
                            .call();
                }
                treeWalk.close();*//*
            }
            System.out.println("清理完成");
         *//*   // 清理无效引用
            git.gc().call();*//*


         *//*   Status status=git.status().call();
            Set<String> removed = status.getRemoved();
            for (String removedFile : removed) {
                System.out.println("已经删除的文件："+removedFile);
            }*//*


        } catch (IOException e) {
            logger.info("报错1:"+e.getMessage());
            throw new RuntimeException(e);
        } catch (GitAPIException e) {
            logger.info("报错2:"+e.getMessage());
            throw new RuntimeException(e);
        }

        return "OK" ;
    }*/

    public  String testDelete(String cleanRpyPath,RepositoryCleanQuery repositoryCleanQuery){
        try {
            String filePath = "tiklab-xpack-starter/embbed/jdk-16.0.2/lib/modules";
            Git git = Git.open(new File(cleanRpyPath));
            Repository repository = git.getRepository();


            RevWalk walk = new RevWalk(repository);
            // 获取所有提交
            Iterable<RevCommit> commits = git.log().all().call();

            for (RevCommit commit : commits) {
                RevTree tree = commit.getTree();
                TreeWalk treeWalk = new TreeWalk(git.getRepository());
                treeWalk.addTree(tree);
                treeWalk.setRecursive(true);
                treeWalk.setFilter(PathFilter.create("tiklab-xpack-starter/embbed/jdk-16.0.2/lib/modules"));

                //不存在就跳过
                if (!treeWalk.next()) {
                    treeWalk.close();
                    continue;
                }

                git.rm().setCached(false).addFilepattern("tiklab-xpack-starter/embbed/jdk-16.0.2/lib/modules");
                PersonIdent authorIdent = commit.getAuthorIdent();
                PersonIdent committerIdent = commit.getCommitterIdent();
                RevCommit newCommit = git.commit()
                        .setAuthor(authorIdent)
                        .setCommitter(committerIdent)
                        .setMessage(commit.getFullMessage())
                        .setAmend(true)
                        .call();


                // Update the parent reference
                //git.rebase().setUpstream(newCommit).call();

                // Replace the old commit with the new commit
                git.rebase().setUpstream(commit.getParents()[0]).call();

            }
            System.out.println("清理完成");


        } catch (IOException e) {
            logger.info("报错1:"+e.getMessage());
            throw new RuntimeException(e);
        } catch (GitAPIException e) {
            logger.info("报错2:"+e.getMessage());
            throw new RuntimeException(e);
        }

        return "OK" ;
    }


    /**
     *  历史重写
     * @param  git git

     */
    public  void overwrite(Git git) throws IOException, GitAPIException {
        Repository repository = git.getRepository();
        Iterable<RevCommit> commits = git.log().all().call();

        // 遍历每个提交
        for (RevCommit commit : commits) {
            // 如果提交是空的，移除它
            if (commit.getParentCount() == 0) {
                git.tagDelete().setTags(commit.getName()).call();
                git.branchDelete().setBranchNames(commit.getName()).setForce(true).call();
            }
        }

        List<Ref> refList = git.tagList().call();
        // 获取所有的标签
        for (Ref tagRef :refList ) {
            // 获取标签的名字
            String tagName = tagRef.getName();

            // 删除标签
            git.tagDelete().setTags(tagName).call();

            RevWalk walk = new RevWalk(repository);
            RevCommit commit = walk.parseCommit(tagRef.getObjectId());
            // 重新创建标签
            git.tag().setName(tagName).setObjectId(commit).setForceUpdate(true).call();
        }
    }


    public void deleteCite(Git git) throws GitAPIException, URISyntaxException, IOException {

        // 获取所有的引用
        Collection<Ref> refs = git.getRepository().getAllRefs().values();


        // 遍历引用并删除符合条件的引用
        for (Ref ref : refs) {
            if (ref.getName().startsWith("refs/original")) {
                // 创建 RefUpdate 对象
                RefUpdate refUpdate = git.getRepository().updateRef(ref.getName());

                // 设置要删除引用的强制更新
                refUpdate.setForceUpdate(true);

                // 删除引用
                RefUpdate.Result result = refUpdate.delete();
            }


     /*       // 删除original原始引用
            RefSpec refSpec = new RefSpec()
                    .setSourceDestination(Constants.R_REFS + "original/*", "")
                    .setForceUpdate(true);
            git.push().setRemote("origin").setRefSpecs(refSpec).call();*/


            //立即删除无效引用
            git.gc().call();

            git.getRepository().close();
        }
    }







}
