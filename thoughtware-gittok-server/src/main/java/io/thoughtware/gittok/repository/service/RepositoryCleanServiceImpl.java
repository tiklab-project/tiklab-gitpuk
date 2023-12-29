package io.thoughtware.gittok.repository.service;

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
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.Date;
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
                    RevWalk revWalk = new RevWalk(repository);

                    Ref head = repository.getRefDatabase().exactRef(Constants.HEAD);
                    RevCommit commit = revWalk.parseCommit(head.getObjectId());
                    revWalk.markStart(commit);

                    long sizeMb = repositoryCleanQuery.getFileSize() * 1048576;
                    for (RevCommit revCommit : revWalk) {

                        RevTree tree = revCommit.getTree();
                        TreeWalk treeWalk = new TreeWalk(repository);
                        treeWalk.addTree(tree);
                        treeWalk.setRecursive(true);

                        while (treeWalk.next()) {

                            ObjectId objectId = treeWalk.getObjectId(0);
                            try {
                                ObjectLoader open = repository.open(objectId);  //文件大小
                                if (open.getSize()>=sizeMb){
                                    RepositoryClean repositoryClean = new RepositoryClean();
                                    repositoryClean.setFileName(treeWalk.getPathString());
                                    repositoryClean.setFileSize(open.getSize());
                                    String size = RepositoryUtil.countStorageSize(open.getSize());
                                    repositoryClean.setSize(size);
                                    fileList.add(repositoryClean);
                                    logger.info("文件："+treeWalk.getPathString()+" " +open.getSize());
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
/*    @Override
    public  List<RepositoryClean> findLargeFile(RepositoryCleanQuery repositoryCleanQuery) {
        List<RepositoryClean> arrayList = new ArrayList<>();

        int i = 1048576 * repositoryCleanQuery.getFileSize();
        String rpyPath = gitTorkYamlDataMaService.repositoryAddress() + "/" + repositoryCleanQuery.getRepositoryId() + ".git";
        String order="git rev-list --objects --all | git cat-file --batch-check='%(objecttype) %(objectname) %(objectsize) %(rest)' | awk '$3 > '"+i+"' {printf \"%.f,%s\\n\",$3,$4}'";
        try {
            Process process = GitUntil.execGitOrder(order,rpyPath);
            // 获取命令行输出
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            StringBuffer stringBuffer = new StringBuffer();
            // mvn 编译日志
            while ((line = reader.readLine()) != null) {
                logger.info("执行命令日志:"+line);
                stringBuffer.append(line).append(";");
            }

            String[] split = stringBuffer.toString().split(";");
           for (String value:split){
               RepositoryClean repositoryClean = new RepositoryClean();
               String[] data = value.split(",");
               repositoryClean.setFileName(data[1]);
               repositoryClean.setFileSize(Long.valueOf(data[0]));
               String size = RepositoryUtil.countStorageSize(Long.valueOf(data[0]));
               repositoryClean.setSize(size);
               arrayList.add(repositoryClean);
           }
           if (StringUtils.isNotEmpty(repositoryCleanQuery.getSort())){
               if (("asc").equals(repositoryCleanQuery.getSort())){
                   arrayList = arrayList.stream().sorted(Comparator.comparing(RepositoryClean::getFileSize)).collect(Collectors.toList());
               }else {
                   arrayList = arrayList.stream().sorted(Comparator.comparing(RepositoryClean::getFileSize).reversed()).collect(Collectors.toList());
               }
           }

        }catch (Exception e){
            logger.info("错误信息："+e.getMessage());
        }

        return arrayList;
    }*/

 /*   @Override
    public String findLargeFile(RepositoryCleanQuery repositoryCleanQuery) {
        String rpyPath = gitTorkYamlDataMaService.repositoryAddress() + "/" + repositoryCleanQuery.getRepositoryId() + ".git";
        fileMap.remove(repositoryCleanQuery.getRepositoryId());

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(new Runnable(){
            @Override
            public void run() {
                List<RepositoryClean> arrayList = new ArrayList<>();
                try {
                    Git git = Git.open(new File(rpyPath));
                    Repository repository = git.getRepository();
                    List<RepositoryClean> fileList = new ArrayList<>();
                    // 获取所有提交对象
                    Iterable<RevCommit> commits = git.log().all().call();
                    logger.info("获取提交");
                    for (RevCommit commit : commits) {
                        logger.info("1");
                        RevTree tree = commit.getTree();
                        try (TreeWalk treeWalk = new TreeWalk(repository)) {
                            treeWalk.addTree(tree);
                            treeWalk.setRecursive(true);

                            logger.info("12");
                            while (treeWalk.next()) {
                                logger.info("123");
                                ObjectId objectId = treeWalk.getObjectId(0);
                                logger.info("objectId:"+objectId);
                                try {
                                    ObjectLoader open = repository.open(objectId);
                                    logger.info("1234");
                                    RepositoryClean repositoryClean = new RepositoryClean();
                                    repositoryClean.setFileName(treeWalk.getPathString());
                                    repositoryClean.setFileSize(open.getSize());

                                    String size = RepositoryUtil.countStorageSize(open.getSize());
                                    repositoryClean.setSize(size);
                                    fileList.add(repositoryClean);
                                    logger.info("文件路径: " + treeWalk.getPathString()+"；大小："+open.getSize());
                                }catch (Exception e){
                                    //没有查询到当前文件直接跳过
                                    if (e.getMessage().contains("Missing unknown")){
                                        continue;
                                    }
                                }
                            }
                        }
                    }

                    logger.info("获取文件成功");

                    Map<String, List<RepositoryClean>> collect = fileList.stream().collect(Collectors.groupingBy(RepositoryClean::getFileName));
                    Set<String> strings = collect.keySet();
                    for (String key:strings){
                        List<RepositoryClean> repositoryCleans = collect.get(key).stream().sorted(Comparator.comparing(RepositoryClean::getFileSize).reversed()).collect(Collectors.toList());
                        arrayList.add(repositoryCleans.get(0));

                    }
                    if (ObjectUtils.isNotEmpty(repositoryCleanQuery.getFileSize())){
                        long sizeMb = repositoryCleanQuery.getFileSize() * 1048576;
                        arrayList = arrayList.stream().filter(a -> a.getFileSize() > sizeMb).collect(Collectors.toList());
                        if (arrayList.size()>1){
                            logger.info("文件添加成功");
                        }else {
                            RepositoryClean repositoryClean = new RepositoryClean();
                            repositoryClean.setMsg("none");
                            arrayList.add(repositoryClean);
                        }
                    }
                    fileMap.put(repositoryCleanQuery.getRepositoryId(),arrayList);
                    logger.info("文件添加成功");
                    //List<RepositoryClean> repositoryCleans = arrayList.stream().sorted(Comparator.comparing(RepositoryClean::getFileSize).reversed()).collect(Collectors.toList());

                }catch (Exception e){

                    RepositoryClean repositoryClean = new RepositoryClean();
                    repositoryClean.setMsg("fail");
                    arrayList.add(repositoryClean);
                    fileMap.put(repositoryCleanQuery.getRepositoryId(),arrayList);
                    logger.info("获取最大文件错误: " + e.getMessage());
                }
            }});
        return "OK";
    }*/

    @Override
    public List<RepositoryClean> findLargeFileResult(RepositoryCleanQuery repositoryCleanQuery) {
        List<RepositoryClean> repositoryCleans = fileMap.get(repositoryCleanQuery.getRepositoryId());
        if (CollectionUtils.isNotEmpty(repositoryCleans)){
            if (StringUtils.isNotEmpty(repositoryCleanQuery.getSort())){
                if (("asc").equals(repositoryCleanQuery.getSort())){
                     repositoryCleans = repositoryCleans.stream().sorted(Comparator.comparing(RepositoryClean::getFileSize)).collect(Collectors.toList());
                }else {
                    repositoryCleans = repositoryCleans.stream().sorted(Comparator.comparing(RepositoryClean::getFileSize).reversed()).collect(Collectors.toList());
                }
            }
        }

        return repositoryCleans;
    }

    @Override
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

    }

 /*   @Override
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
                       *//* if (file.exists()) {
                            FileUtils.deleteDirectory(new File(cleanRpyPath));
                        }
                        //克隆所有分支的仓库
                        logger.info("开始准备清理仓库的仓库" );
                        joinClearLargeFileLog(repositoryId,"准备清理仓库","run");
                        GitUntil.cloneAllBranchRepository(rpyPath,cleanRpyPath);
                        joinClearLargeFileLog(repositoryId,"成功准备清理仓库","run");*//*

                        //进入执行环境
                        logger.info("进入清理仓库的仓库" );
                        joinClearLargeFileLog(repositoryId,"进入清理仓库的仓库","run");
                        String cd="cd "+cleanRpyPath;
                        execOrder(cd,cleanRpyPath,repositoryId);
                        joinClearLargeFileLog(repositoryId,"成功进入清理仓库的仓库","run");


                        Git git = Git.open(new File(cleanRpyPath));
                        // 暂存当前工作目录的修改
                *//*        logger.info("保存临时修改文件");
                        joinClearLargeFileLog(repositoryId,"执行保存临时修改文件","run");
                        StashCreateCommand stashCreateCommand = git.stashCreate();
                        stashCreateCommand.call();
                        joinClearLargeFileLog(repositoryId,"成功执行保存临时修改文件","run");
*//*


                    *//*    //执行stash命令
                        logger.info("保存临时修改文件");
                        joinClearLargeFileLog(repositoryId,"执行保存临时修改文件","run");
                        String stash="git stash";
                        execOrder(stash,cleanRpyPath,repositoryId);
                        joinClearLargeFileLog(repositoryId,"成功执行保存临时修改文件","run");*//*

                        //删除文件
                       // removeFile(repositoryCleanQuery, cleanRpyPath);
                        removeFile(repositoryCleanQuery,git);

                        //回收空间
                       *//* logger.info("删除原始引用");
                        joinClearLargeFileLog(repositoryId,"执行删除原始引用","run");
                        String eachRef= "git for-each-ref --format='delete %(refname)' refs/original | git update-ref --stdin";
                        execOrder(eachRef,cleanRpyPath,repositoryId);
                        joinClearLargeFileLog(repositoryId,"成功执行删除原始引用","run");*//*

                      logger.info("删除.git/refs/original下面引用");
                        joinClearLargeFileLog(repositoryId,"删除.git/refs/original下面引用","run");
                        String rmRf= "rm -rf .git/refs/original/";
                        execOrder(rmRf,cleanRpyPath,repositoryId);
                        joinClearLargeFileLog(repositoryId,"成功删除.git/refs/original下面引用","run");

                       *//*   logger.info("删除日志引用");
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
                        repositoryServer.updateRepository(repository);*//*
                        logger.info("执行成功");
                        joinClearLargeFileLog(repositoryId,"清除成功","success");
                    }catch (Exception e){
                        logger.info("操作失败"+e );
                        joinClearLargeFileLog(repositoryId,"清除失败："+e.getMessage(),"fail");
                    }
                }});
            return "OK";
        }


        return "fail";

    }*/
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
/*
        git.rm().addFilepattern(repositoryCleanQuery.getFileName()).call();
        git.commit()
                .setMessage("Remove file: " + repositoryCleanQuery.getFileName())
                .call();

        // Clean up
        git.close();*/
    /*    // 获取所有提交
        Iterable<RevCommit> commits = git.log().all().call();
        Repository repository = git.getRepository();
        // 遍历每个提交
        for (RevCommit commit : commits) {
            // 执行过滤操作
            List<String> fileNameList = repositoryCleanQuery.getFileNameList();
            for (String filePath:fileNameList){
                // 检查文件是否存在
                File file = new File(repository.getWorkTree(), filePath);
                boolean exists = file.exists();
                if (exists) {
                    // 创建 RmCommand 对象
                    RmCommand rmCommand = git.rm();

                    // 添加要移除的文件或文件夹
                    rmCommand.addFilepattern(filePath);
                    //只从索引中删除文件，而不会删除实际的工作树文件
                    rmCommand.setCached(true);

                    // 执行 RmCommand 并提交更改
                    rmCommand.call();

                    // 清理索引文件
                    repository.getIndexFile().delete();

                    // 删除工作树中的对应文件
                    File fileToDelete = new File(repositoryPath, fileName);
                    FileUtils.delete(fileToDelete, FileUtils.RECURSIVE);


                } else {
                    logger.info("File does not exist: " + filePath);
                }
            }

            // 创建新的提交
            git.writeTree();
            RevCommit newCommit = commit.copy();
            newCommit.setTreeId(git.writeTree());

            // 更新提交对象
            try (RevWalk revWalk = new RevWalk(repository)) {
                RevCommit oldCommit = revWalk.parseCommit(commit);
                ObjectId newCommitId = git.getRepository().writeCommit(newCommit);
                revWalk.parseBody(oldCommit);
                revWalk.markStart(oldCommit);
                RevCommit[] parents = oldCommit.getParents();
                for (RevCommit parent : parents) {
                    revWalk.parseBody(parent);
                    revWalk.markUninteresting(parent);
                }
                newCommit.setParentCount(parents.length);
                if (parents.length > 0) {
                    newCommit.setParent(0, parents[0]);
                }
                newCommit.setId(newCommitId);
            }
        }*/
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
     //   Git git = Git.open(new File("/Users/limingliang/tiklab/thoughtware-gittok/repository/clean/fd3dda00c72d"));

        logger.info("保存临时修改文件");






        try (Git git = Git.open(new File(repositoryPath))) {
            Iterable<RevCommit> commits = git.log().all().call();

            StashCreateCommand stashCreateCommand = git.stashCreate();
            stashCreateCommand.call();

          /*  for (RevCommit commit : commits) {
                RevTree tree = commit.getTree();
                try (TreeWalk treeWalk = new TreeWalk(git.getRepository())) {
                    treeWalk.addTree(tree);
                    treeWalk.setRecursive(true);
                    while (treeWalk.next()) {
                        String pathString = treeWalk.getPathString();
                        if (pathString.equals(fileName)) {
                            logger.info("文件："+pathString);
                            RmCommand rmCommand = git.rm();
                            rmCommand.setCached(true); // 将文件从暂存区中删除

                            rmCommand.addFilepattern(fileName);
                            rmCommand.call();

                            // 运行 git filter-branch


                            // Keep the changes in the working directory
                           // git.checkout().setAllPaths(true).call();
                            // 清理索引文件
                           // git.getRepository().getIndexFile().delete();

                           // git.commit().setMessage("Remove file").call();
                        }
                    }
                }
            }*/
          /*  // 执行垃圾回收并清理无用的对象

            git.gc().setPrunePreserved(true).setAggressive(true).call();

            System.out.println("垃圾回收和清理完成！");
*/


            System.out.println("文件 " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
