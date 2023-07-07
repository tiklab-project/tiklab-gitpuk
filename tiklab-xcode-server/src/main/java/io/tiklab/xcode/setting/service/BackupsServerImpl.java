package io.tiklab.xcode.setting.service;

import com.alibaba.fastjson.JSONObject;
import io.tiklab.core.exception.ApplicationException;
import io.tiklab.core.exception.SystemException;
import io.tiklab.eam.common.context.LoginContext;
import io.tiklab.xcode.repository.model.Repository;
import io.tiklab.xcode.repository.service.RepositoryServer;
import io.tiklab.xcode.setting.model.Backups;
import io.tiklab.xcode.util.RepositoryFileUtil;
import io.tiklab.xcode.util.RepositoryUtil;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.xml.crypto.Data;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipOutputStream;


@Service
public class BackupsServerImpl implements BackupsServer{

    private static Logger logger = LoggerFactory.getLogger(BackupsServerImpl.class);

    @Value("${APP_HOME:null}")
    String appHome;



    @Autowired
    RepositoryServer repositoryServer;

    //数据备份日志
    public static Map<String , String> backupsExecLog = new HashMap<>();

    //数据恢复日志
    public static Map<String , String> recoveryLog = new HashMap<>();


    //执行的文件绝对路径名称
    public static Map<String , String> fileAbUrlMap = new HashMap<>();

    @Override
    public String backupsExec() {
        List<Repository> allRpy = repositoryServer.findAllRpy();
        String loginId = LoginContext.getLoginId();
        recoveryLog.remove(loginId);
        backupsExecLog.remove(loginId);
        fileAbUrlMap.remove(loginId);

        joinBackupsLog("Dumping PostgreSQL database tiklab_xcode...");
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(new Runnable(){
            @Override
            public void run() {
                File file = new File(appHome+"/file/backups");
                String fileData = gainFileData(file);

                JSONObject jsonObject = JSONObject.parseObject(fileData);
                String backUpsUrl = jsonObject.get("backups-url").toString();
                String backupsTime = jsonObject.get("backups-time").toString();

                //添加最后一层目录压缩
                String lastName = backUpsUrl.substring(backUpsUrl.lastIndexOf("/"));
                backUpsUrl=backUpsUrl+"/"+lastName;
                //code 下面存放代码数据
                File backUpsUrlFile = new File(backUpsUrl+"/code");

                try {
                    if (!backUpsUrlFile.exists() && !backUpsUrlFile.isDirectory()) {
                        backUpsUrlFile.mkdirs();
                    }

                    //开始dump database备份脚本
                    executeScript(backUpsUrl);
                    //完成dump database备份脚本
                    joinBackupsLog("Dumping PostgreSQL database tiklab_xcode...[DONE]");

                    for (Repository repository:allRpy){
                        //源文件 地址
                        String defaultPath = RepositoryUtil.defaultPath();
                        String repositoryUrl = defaultPath +"/"+ repository.getRpyId() + ".git";
                        File codeFileUrl = new File(repositoryUrl);


                        /**
                         *  复制代码源文件到备份文件夹
                         */
                        String backupsCodePath = backUpsUrl + "/code/" + repository.getRpyId()+".git";
                        File backupsCodeFilePath = new File(backupsCodePath);

                        String name = repository.getName();


                        //复制代码文件
                        joinBackupsLog(name+"  start backups...");
                        FileUtils.copyDirectory(codeFileUrl,backupsCodeFilePath);
                        joinBackupsLog(name+"  backups success...[DONE]");
                    }


                    /**
                     *  压缩备份代码文件夹
                     */
                    joinBackupsLog(" start compress tar.gz...");
                    String substring = backUpsUrl.substring(0, backUpsUrl.lastIndexOf("/"));
                    LocalDateTime now = LocalDateTime.now();
                    //备份压缩文件名称
                    String backupsName="xcode_backups_"+now.getYear()+"_"+now.getMonthValue()+"_" +now.getDayOfMonth()+"_"
                            +now.getHour()+"_"+now.getMinute()+"_"+String.valueOf(System.currentTimeMillis()).substring(0,9)+".tar.gz";

                    String backupsAbsoluteUrl = substring + "/"+backupsName;
                    // 创建tar输出流
                    FileOutputStream fos = new FileOutputStream(backupsAbsoluteUrl);
                    BufferedOutputStream bos = new BufferedOutputStream(fos);
                    GzipCompressorOutputStream gzos = new GzipCompressorOutputStream(bos);
                    TarArchiveOutputStream tos = new TarArchiveOutputStream(gzos);

                    RepositoryFileUtil.compressFolder(backUpsUrl,"",tos);
                    joinBackupsLog(" compress tar.gz success...[DONE]");
                    logger.info("压缩成功");
                    // 关闭流
                    tos.close();
                    gzos.close();
                    bos.close();
                    fos.close();

                    /**
                     *  删除备份文件夹
                     */
                    FileUtils.deleteDirectory(new File(backUpsUrl));

                    if (StringUtils.isNotEmpty(backupsTime)){
                        fileData =fileData.replace(backupsTime,RepositoryUtil.date(1,new Date()));
                    }
                    //修该备份text信息
                    writeFile(file,fileData);

                    joinBackupsLog(" Backups file success end [DONE]");
                }catch (Exception e){
                    joinBackupsLog(" Backups file fail end,errorMsg:"+e.getMessage());
                    logger.info("错误信息:"+e.getMessage());
                    new ApplicationException(e.getMessage());
                }
            }
        });

        return "ok";
    }

    @Override
    public String recoveryData(String fileName) {
        String loginId = LoginContext.getLoginId();
        backupsExecLog.remove(loginId);
        recoveryLog.remove(loginId);
        joinRecoveryLog("start decompression "+fileName+" ...");

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(new Runnable(){
            @Override
            public void run() {
                File file = new File(appHome + "/file/backups");
                try {
                    String fileData = gainFileData(file);
                    JSONObject jsonObject = JSONObject.parseObject(fileData);
                    String backUpsUrl = jsonObject.get("backups-url").toString();
                    String substring = backUpsUrl.substring(0, backUpsUrl.lastIndexOf("/"));

                    /**
                     *  解压tar.gz包
                     */
                    //压缩包的绝对路径
                    String DecFileUrl = substring + "/" + fileName;
                    //压缩后的文件绝对路径
                    String name = fileName.substring(0, fileName.indexOf(".tar.gz"));
                    String afterDecFileUrl = substring + "/" + name + "/";
                    //解压tar.gz
                    RepositoryFileUtil.decompression(DecFileUrl,afterDecFileUrl);
                    joinRecoveryLog(" decompression "+fileName+" success [DONE]");

                    /**
                     *  恢复postgreSQL 数据
                     */
                    joinRecoveryLog("start Recovery PostgreSQL database tiklab_xcode ...");
                    executeRecoveryScript(afterDecFileUrl);
                    joinRecoveryLog("Recovery PostgreSQL database tiklab_xcode success [DONE]");

                    /**
                     *  copy代码文件到代码仓库
                     */
                    List<Repository> allRpy = repositoryServer.findAllRpy();
                    for (Repository repository:allRpy){
                        joinRecoveryLog(repository.getName()+ "  start Recovery ...");
                        String codePath = RepositoryUtil.defaultPath() + "/" + repository.getRpyId() + ".git";
                        FileUtils.copyDirectory(new File(afterDecFileUrl+"code/"+repository.getRpyId()+".git"),new File(codePath));
                        joinRecoveryLog(repository.getName()+ " Recovery  success [DONE]");
                    }

                    /**
                     *  删除解压后的文件
                     */
                    FileUtils.deleteDirectory(new File(substring + "/" + name ));

                    joinRecoveryLog("Recovery success end [DONE]");

                }catch (Exception e){
                    joinRecoveryLog("Recovery fail end，errorMsg:"+e.getMessage());
                    throw  new ApplicationException(e.getMessage());
                }
            }
        });
        return "ok";
    }

    @Override
    public void updateBackups(Backups backups)  {
        String backupsAddress = backups.getBackupsAddress();
        if (StringUtils.isNotEmpty(backupsAddress)&&backupsAddress.endsWith("/")){
            backupsAddress= backupsAddress.substring(0,backupsAddress.lastIndexOf("/")+1);
        }


        File file = new File(appHome+"/file/backups");
        String fileData = gainFileData(file);

        JSONObject jsonObject = JSONObject.parseObject(fileData);

        if (StringUtils.isNotEmpty(backups.getBackupsAddress())){
            String backUpsUrl = jsonObject.get("backups-url").toString();
            fileData =fileData.replace(backUpsUrl,backupsAddress);
        }
        if (StringUtils.isNotEmpty(backups.getTaskState())){
            String taskState = jsonObject.get("task-state").toString();
             fileData = fileData.replace(taskState, backups.getTaskState());
        }
        writeFile(file,fileData);
    }

    @Override
    public Backups findBackups() {
        Backups backups = new Backups();
        File file = new File(appHome+"/file/backups");
        String fileData = gainFileData(file);
        if (StringUtils.isEmpty(fileData)){
            throw  new ApplicationException(5000,"数据不存在");
        }
        JSONObject jsonObject = JSONObject.parseObject(fileData);
        String backUpsUrl = jsonObject.get("backups-url").toString();
        String taskState = jsonObject.get("task-state").toString();
        String backupsTime = jsonObject.get("backups-time").toString();

        backups.setBackupsAddress(backUpsUrl);
        backups.setTaskState(taskState);
        backups.setNewBackupsTime(backupsTime);
        backups.setNewResult("non");
        String result = backupsExecLog.get(LoginContext.getLoginId());
        if (StringUtils.isNotEmpty(result)){
            backups.setNewResult("fail");
           if (result.contains("Backups file success end")){
               backups.setNewResult("success");
           }
        }


        return backups;
    }


    @Override
    public String gainBackupsRes(String type) {

        String loginId = LoginContext.getLoginId();
        if (("backups").equals(type)){
            String backups = backupsExecLog.get(loginId);
            if (StringUtils.isEmpty(backups)){
                return null;
            }
           return backups;
        }
        String recovery = recoveryLog.get(loginId);
        if (StringUtils.isEmpty(recovery)){
            return null;
        }
        return recovery;
    }

    @Override
    public void uploadBackups(InputStream inputStream, String fileName,String userId) {
        try {
            //获取text文件信息
            File file = new File(appHome + "/file/backups");
            String fileData = gainFileData(file);

            JSONObject jsonObject = JSONObject.parseObject(fileData);
            String backUpsUrl = jsonObject.get("backups-url").toString();
            String substring = backUpsUrl.substring(0, backUpsUrl.lastIndexOf("/"));

            //如果文件夹不存在就创建文件夹
            File reduceDir = new File(substring);
            if (!reduceDir.exists() && !reduceDir.isDirectory()) {
                reduceDir.mkdirs();
            }

            //上传备份压缩文件的绝对路径
            String reduceUrl = substring + "/" + fileName;
            File reduceFile = new File(reduceUrl);
            //文件已经存在
            if (!reduceFile.exists()) {

                //创建文件
                File backUpsUrlFile = new File(reduceUrl);
                backUpsUrlFile.createNewFile();
                //写入文件
                FileOutputStream outputStream = new FileOutputStream(reduceUrl);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            }

        }catch (Exception e){
            throw  new ApplicationException(e.getMessage());
        }
    }


    /**
     *  dump database 备份脚本
     * @return
     */
    public void executeScript(String backUpsUrl) throws IOException, InterruptedException {

        String[] args = new String[7];
        args[0] = "host=172.10.1.10";
        args[1] = "port=5432";
        args[2] = "userName=postgres";
        args[3] = "password=darth2020";
        args[4] = "dbName=tiklab_xcode";
        args[5] = "schemaName=public";
        args[6] = "backupsUrl="+backUpsUrl;

        Process ps = Runtime.getRuntime().exec(appHome+"/file/backups.sh",args);
        ps.waitFor();
    }


        /**
         *  执行恢复PostgreSQL 数据脚本
         * @param backUpsSqlUrl 备份sql地址
         * @return
         */
    public void executeRecoveryScript(String backUpsSqlUrl) throws IOException, InterruptedException {

        String[] args = new String[7];
        args[0] = "host=172.10.1.10";
        args[1] = "port=5432";
        args[2] = "userName=postgres";
        args[3] = "password=darth2020";
        args[4] = "dbName=tiklab_xcode";
        args[5] = "schemaName=public";
        args[6] = "backupsSqlUrl="+backUpsSqlUrl;

        Process ps = Runtime.getRuntime().exec(appHome+"/file/recovery.sh",args);
        ps.waitFor();
    }


    /**
     *  读取file 文件
     *  @param file     文件
     * @return
     */
    public String gainFileData(File file){
        try {
            FileInputStream inputStream = new FileInputStream(file);
            StringBuilder result = new StringBuilder();
            BufferedReader bfr = new BufferedReader(new InputStreamReader(inputStream));
            String lineTxt = null;
            while ((lineTxt = bfr.readLine()) != null) {
                String a=lineTxt;
                result.append(lineTxt).append(System.lineSeparator());
            }
            String toString = result.toString();

            inputStream.close();
            return toString;
        }catch (IOException e){
            throw new ApplicationException(e.getMessage());
        }
    }

    /**
     *  写入文件
     *  @param file     文件
     * @return
     */
    public void writeFile(File file,String fileData){
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(fileData);
            fileWriter.close();
        }catch (Exception e){
            throw new ApplicationException(5000,e.getMessage());
        }
    }


    @Scheduled(cron = "0 0 2 * * ?")
    public void createTemplate(){
        File file = new File(appHome + "/file/backups");
        String fileData = gainFileData(file);

        JSONObject jsonObject = JSONObject.parseObject(fileData);
        String state = jsonObject.get("task-state").toString();
        if ("true".equals(state)){
            logger.info("定时备份执行");
           this.backupsExec();
        }
    }

    /**
     *  拼接备份日志
     *  @param log 日志
     * @return
     */
    public void joinBackupsLog(String log){
        LocalDateTime now = LocalDateTime.now();
        // 自定义时间格式
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String customDateTime = now.format(customFormatter);
        String resultLog = customDateTime + "---" + log;
        String loginId = LoginContext.getLoginId();
        String logs = backupsExecLog.get(loginId);
        if (StringUtils.isEmpty(logs)){
            backupsExecLog.put(loginId,resultLog);
        }else {
            backupsExecLog.put(loginId,logs+"\n"+resultLog);
        }
        logger.info("日志:"+logs);
    }

    /**
     *  拼接恢复日志
     *  @param log 日志
     * @return
     */
    public void joinRecoveryLog(String log){
        LocalDateTime now = LocalDateTime.now();
        // 自定义时间格式
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String customDateTime = now.format(customFormatter);
        String resultLog = customDateTime + "---" + log;
        String loginId = LoginContext.getLoginId();
        String logs = recoveryLog.get(loginId);
        if (StringUtils.isEmpty(logs)){
            recoveryLog.put(loginId,resultLog);
        }else {
            recoveryLog.put(loginId,logs+"\n"+resultLog);
        }
        logger.info("日志:"+logs);
    }

}
