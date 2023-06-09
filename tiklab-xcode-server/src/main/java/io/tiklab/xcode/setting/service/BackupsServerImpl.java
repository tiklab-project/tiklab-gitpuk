package io.tiklab.xcode.setting.service;

import com.alibaba.fastjson.JSONObject;
import io.tiklab.core.exception.ApplicationException;
import io.tiklab.xcode.repository.model.Repository;
import io.tiklab.xcode.repository.service.RepositoryServer;
import io.tiklab.xcode.setting.model.Backups;
import io.tiklab.xcode.util.RepositoryUtil;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;


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

    //上传备份压缩文件路径
    public static Map<String , String> uploadFileUrlMap = new HashMap<>();

    @Override
    public void backupsExec() {
        List<Repository> allRpy = repositoryServer.findAllRpy();

        backupsExecState.clear();
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

            //执行备份脚本
            executeScript(backUpsUrl,allRpy);

            backupsExecState.put("result","ok");

            if (StringUtils.isNotEmpty(backupsTime)){
                fileData =fileData.replace(backupsTime,RepositoryUtil.date(1,new Date()));
            }
            writeFile(file,fileData);
        }catch (Exception e){
            backupsExecState.put("result","error");
            new ApplicationException(e.getMessage());
        }
    }



    @Override
    public String gainBackupsRes(String type) {
        if (("backups").equals(type)){
            return   backupsExecState.get("result");
        }
        return recoveryState.get("result");
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

        backups.setBackupsAddress(backUpsUrl);
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

            uploadFileUrlMap.remove(userId);
        }catch (Exception e){
            recoveryState.put("result","error");
            throw  new ApplicationException(e.getMessage());
        }
    }

    @Override
    public void uploadBackups(InputStream inputStream, String fileName,String userId) {
        try {
            File file = new File(appHome + "/file/backups");
            String fileData = gainFileData(file);

            JSONObject jsonObject = JSONObject.parseObject(fileData);
            String backUpsUrl = jsonObject.get("backups-url").toString();
            String substring = backUpsUrl.substring(0, backUpsUrl.lastIndexOf("/"));

            //上传备份压缩文件的绝对路径
            String reduceUrl = substring + "/" + fileName;

            File reduceFile = new File(reduceUrl);
            //文件已经存在
            if (!reduceFile.exists()) {
                //创建文件
                File backUpsUrlFile = new File(backUpsUrl);
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
            uploadFileUrlMap.put(userId,fileName);
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
        String reduceName = substring + "/"+System.currentTimeMillis();
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
        args[9] = "reduceName="+reduceName;
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
