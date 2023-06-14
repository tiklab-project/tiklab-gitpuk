package io.tiklab.xcode.setting.service;

import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.result.Field;
import io.tiklab.core.exception.ApplicationException;
import io.tiklab.eam.common.context.LoginContext;
import io.tiklab.xcode.repository.model.Repository;
import io.tiklab.xcode.repository.service.RepositoryServer;
import io.tiklab.xcode.setting.model.Backups;
import io.tiklab.xcode.util.RepositoryUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
public class BackupsServerImpl implements BackupsServer{

    private static Logger logger = LoggerFactory.getLogger(BackupsServerImpl.class);

    @Value("${APP_HOME:null}")
    String appHome;


    @Autowired
    RepositoryServer repositoryServer;

    //数据备份状态
    public static Map<String , String> backupsExecState = new HashMap<>();

    //数据恢复状态
    public static Map<String , String> recoveryState = new HashMap<>();

    //执行的文件绝对路径名称
    public static Map<String , String> fileAbUrlMap = new HashMap<>();

    @Override
    public String backupsExec() {
        List<Repository> allRpy = repositoryServer.findAllRpy();
        String loginId = LoginContext.getLoginId();
        backupsExecState.remove(loginId);
        fileAbUrlMap.remove(loginId);

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(new Runnable(){
            @Override
            public void run() {
                //执行备份脚本
                File file = new File(appHome+"/file/backups");
                String fileData = gainFileData(file);


                JSONObject jsonObject = JSONObject.parseObject(fileData);
                String backUpsUrl = jsonObject.get("backups-url").toString();
                String backupsTime = jsonObject.get("backups-time").toString();

                //code 下面存放代码数据
                File backUpsUrlFile = new File(backUpsUrl+"/code");

                try {
                    if (!backUpsUrlFile.exists() && !backUpsUrlFile.isDirectory()) {
                        backUpsUrlFile.mkdirs();
                    }
                    executeScript(backUpsUrl,allRpy);

                    backupsExecState.put(loginId,"ok");

                    if (StringUtils.isNotEmpty(backupsTime)){
                        fileData =fileData.replace(backupsTime,RepositoryUtil.date(1,new Date()));
                    }
                    writeFile(file,fileData);
                }catch (Exception e){
                    backupsExecState.put(loginId,"error");
                    new ApplicationException(e.getMessage());
                }
            }
        });

        return "ok";
    }



    @Override
    public void updateBackups(Backups backups)  {
        File file = new File(appHome+"/file/backups");
        String fileData = gainFileData(file);

        JSONObject jsonObject = JSONObject.parseObject(fileData);
        String backUpsUrl = jsonObject.get("backups-url").toString();
        String taskState = jsonObject.get("task-state").toString();

        if (StringUtils.isNotEmpty(backups.getBackupsAddress())){
            fileData =fileData.replace(backUpsUrl,backups.getBackupsAddress());
        }
        if (StringUtils.isNotEmpty(backups.getTaskState())){
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

        String substring = backUpsUrl.substring(0, backUpsUrl.lastIndexOf("/"));
        backups.setBackupsAddress(substring);
        backups.setTaskState(taskState);
        backups.setNewBackupsTime(backupsTime);
        return backups;
    }

    @Override
    public void recoveryData(String userId,String fileName) {
        recoveryState.clear();

        File file = new File(appHome + "/file/backups");
        try {
            String fileData = gainFileData(file);
            JSONObject jsonObject = JSONObject.parseObject(fileData);
            String backUpsUrl = jsonObject.get("backups-url").toString();
            String substring = backUpsUrl.substring(0, backUpsUrl.lastIndexOf("/"));

            executeRecoveryScript(backUpsUrl, substring, fileName);
            recoveryState.put("result","ok");

        }catch (Exception e){
            recoveryState.put("result","error");
            throw  new ApplicationException(e.getMessage());
        }
    }


    @Override
    public String gainBackupsRes(String type) {

        String loginId = LoginContext.getLoginId();
        if (("backups").equals(type)){

            String backupsAbsoluteUrl = fileAbUrlMap.get(loginId);
            //缓存中还未存入文件的绝对路径,执行恢复进度为0
            if (StringUtils.isEmpty(backupsAbsoluteUrl)){
                return  "0";
            }

            File file = new File(backupsAbsoluteUrl);
            long backupsFileSize = file.length();
            String defaultPath = RepositoryUtil.defaultPath();
            long fileSize = FileUtils.sizeOfDirectory(new File(defaultPath));

            return   backupsExecState.get("result");
        }
        File file = new File(appHome + "/file/backups");
        String fileData = gainFileData(file);
        JSONObject jsonObject = JSONObject.parseObject(fileData);
        String backUpsUrl = jsonObject.get("backups-url").toString();

        //备份文件git文件大小
        File backupsFile = new File(backUpsUrl + "/code");
        long backupsFileSize = backupsFile.length();

        //恢复数据的大小
        String defaultPath = RepositoryUtil.defaultPath();
        long recoveryFileSize = FileUtils.sizeOfDirectory(new File(defaultPath));
        if (recoveryFileSize==0){
            //在执恢复git 文件之前回先执行恢复数据库数据 默认20%进度
            return  "20";
        }
        //恢复失败
        if(("error").equals(recoveryState.get("result"))){
            return "error";
        }

        if (backupsFileSize!=0){
            Long progress = recoveryFileSize / backupsFileSize;
            long result = progress * 100;
            logger.info("执行进度"+result);
            return String.valueOf(result);
        }else {
            //在恢复成功后回删除掉解压的恢复文件，就取缓存中的恢复数据
            if(("ok").equals(recoveryState.get("result"))){
                logger.info("执行进度"+100);
                return "100";
            }
            return String.valueOf(0);
        }

       // return recoveryState.get("result");
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
     *  执行备份脚本
     * @return
     */
    public void executeScript(String backUpsUrl, List<Repository> allRpy) throws IOException, InterruptedException {
        String substring = backUpsUrl.substring(0, backUpsUrl.lastIndexOf("/"));

        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        //备份文件名称
        String backupsName="xcode_backups_"+now.getYear()+"_"+now.getMonthValue()+"_"
                +now.getDayOfMonth()+"_"+now.getHour()+"_"+now.getMinute()+"_"+String.valueOf(System.currentTimeMillis()).substring(0,9);
        
        String backupsAbsoluteUrl = substring + "/"+backupsName;

        fileAbUrlMap.put(LoginContext.getLoginId(),backupsAbsoluteUrl);
        
        int length = substring.split("/").length;

        String defaultPath = RepositoryUtil.defaultPath();
       String allUrl="";
        for (Repository repository:allRpy){
            String url=defaultPath+"/"+repository.getName()+".git"+"\n";
            allUrl=allUrl+url;
        }

        String[] args = new String[11];
        args[0] = "host=172.10.1.10";
        args[1] = "port=5432";
        args[2] = "userName=postgres";
        args[3] = "password=darth2020";
        args[4] = "dbName=tiklab_xcode";
        args[5] = "schemaName=public";
        args[6] = "backupsUrl="+backUpsUrl;
        args[7] = "backupsCodeUrl="+backUpsUrl+"/code";
        args[8] = "sourceFilePath="+allUrl;
        args[9] = "reduceName="+backupsAbsoluteUrl;
        args[10]="length="+length;
        Process ps = Runtime.getRuntime().exec(appHome+"/file/backups.sh",args);
        ps.waitFor();
    }


        /**
         *  执行恢复数据脚本
         * @param backUpsSqlUrl 备份sql地址
         * @param  substringUrl 存放备份压缩文件位置
         * @param fileName  恢复数据名称
         * @return
         */
    public void executeRecoveryScript(String backUpsSqlUrl,String substringUrl,String fileName) throws IOException, InterruptedException {
        String defaultPath = RepositoryUtil.defaultPath();
        String reduceUrl = substringUrl + "/" + fileName;
        String[] args = new String[11];
        args[0] = "host=172.10.1.10";
        args[1] = "port=5432";
        args[2] = "userName=postgres";
        args[3] = "password=darth2020";
        args[4] = "dbName=tiklab_xcode";
        args[5] = "schemaName=public";
        args[6] = "backupsSqlUrl="+backUpsSqlUrl;
        args[7] = "backupsCodeUrl="+backUpsSqlUrl+"/code";
        args[8] = "prePath="+substringUrl;
        args[9] = "sourceFilePath="+defaultPath;
        args[10] = "reduceUrl="+reduceUrl;

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

}
