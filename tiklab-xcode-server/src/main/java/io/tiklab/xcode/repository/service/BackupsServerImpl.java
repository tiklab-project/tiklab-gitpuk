package io.tiklab.xcode.repository.service;

import com.alibaba.fastjson.JSONObject;
import io.tiklab.beans.BeanMapper;
import io.tiklab.core.context.AppHomeContext;
import io.tiklab.core.exception.ApplicationException;
import io.tiklab.core.exception.SystemException;
import io.tiklab.eam.common.context.LoginContext;
import io.tiklab.xcode.repository.dao.BackupsDao;
import io.tiklab.xcode.repository.entity.BackupsEntity;
import io.tiklab.xcode.repository.entity.LeadAuthEntity;
import io.tiklab.xcode.repository.model.ExecLog;
import io.tiklab.xcode.repository.model.LeadAuth;
import io.tiklab.xcode.repository.model.Repository;
import io.tiklab.xcode.repository.service.BackupsServer;
import io.tiklab.xcode.repository.service.RepositoryServer;
import io.tiklab.xcode.common.XcodeYamlDataMaService;
import io.tiklab.xcode.repository.model.Backups;
import io.tiklab.xcode.common.RepositoryFileUtil;
import io.tiklab.xcode.common.RepositoryUtil;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


@Service
public class BackupsServerImpl implements BackupsServer {

    private static Logger logger = LoggerFactory.getLogger(BackupsServerImpl.class);

    @Autowired
    BackupsDao backupsDao;

    @Autowired
    RepositoryServer repositoryServer;

    @Autowired
    XcodeYamlDataMaService yamlDataMaService;


    @Value("${jdbc.username}")
    String jdbcUserName;

    @Value("${jdbc.password}")
    String jdbcPassword;



    //数据备份日志
    public static Map<String , ExecLog> backupsExecLog = new HashMap<>();

    //数据恢复日志
    public static Map<String , ExecLog> recoveryLog = new HashMap<>();


    @Override
    public Backups findBackups() {
        List<BackupsEntity> allBackups = backupsDao.findAllBackups();
        BackupsEntity backupsEntity = allBackups.get(0);
        Backups backups = BeanMapper.map(backupsEntity, Backups.class);

        backups.setBackupsAddress(yamlDataMaService.backupAddress());

        return backups;
    }

    @Override
    public void updateBackups(Backups backups)  {
        BackupsEntity backupsEntity = BeanMapper.map(backups, BackupsEntity.class);

        backupsDao.updateBackups(backupsEntity);
    }

    @Override
    public void uploadBackups(InputStream inputStream, String fileName) {
        try {
            String uploadAddress = yamlDataMaService.uploadAddress();
            //如果文件夹不存在就创建文件夹
            File reduceDir = new File(uploadAddress);
            if (!reduceDir.exists() && !reduceDir.isDirectory()) {
                reduceDir.mkdirs();
            }
            //上传备份压缩文件的绝对路径
            String reduceUrl = uploadAddress + "/" + fileName;
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


    @Override
    public String backupsExec(Backups backups) {
        backups.setExecState("exec");
        updateBackups(backups);

        String loginId = LoginContext.getLoginId();
        recoveryLog.remove(loginId);
        backupsExecLog.remove(loginId);
        joinBackupsLog("Dumping PostgreSQL database tiklab_xcode...");
        ExecutorService executorService = Executors.newCachedThreadPool();

        executorService.submit(new Runnable(){
            @Override
            public void run() {
                try {
                    File file = new File(yamlDataMaService.backupAddress());
                    if (!file.exists()){
                        file.mkdir();
                    }

                    File repositoryFile = new File(yamlDataMaService.repositoryAddress());

                    List<Repository> allRpy = repositoryServer.findAllRpyList();

                    boolean isResidue = memSize(file, repositoryFile);
                    //当剩余空间不足删除最先备份的那个文件
                    if(!isResidue){
                        //获取备份文件夹下面备份文件并根据创建时间排序
                        File[] files = file.listFiles();
                        List<File> collected = Arrays.stream(files).sorted(Comparator.comparing(a -> a.lastModified())).collect(Collectors.toList());
                        File firstFile = collected.get(0);
                        //删除文件
                        FileUtils.deleteQuietly(firstFile);
                    }
                    //添加最后一层目录压缩
                    String backupAddress = yamlDataMaService.backupAddress();
                    String lastName = backupAddress.substring(backupAddress.lastIndexOf("/"));

                    String backupPath=backupAddress+lastName;
                    //code 下面存放代码数据
                    File backUpsUrlFile = new File(backupPath+"/code");


                    if (!backUpsUrlFile.exists() && !backUpsUrlFile.isDirectory()) {
                        backUpsUrlFile.mkdirs();
                    }

                    //开始dump database备份脚本
                    executeScript(backupPath);
                    //完成dump database备份脚本
                    joinBackupsLog("Dumping PostgreSQL database tiklab_xcode...[DONE]");

                    for (Repository repository:allRpy){

                        String repositoryUrl = yamlDataMaService.repositoryAddress() +"/"+ repository.getRpyId() + ".git";
                        File codeFileUrl = new File(repositoryUrl);

                        /*
                         * 复制代码源文件到备份文件夹
                         * */
                        String backupsCodePath = backupPath + "/code/" + repository.getRpyId()+".git";
                        File backupsCodeFilePath = new File(backupsCodePath);

                        String name = repository.getName();


                        //复制代码文件
                        joinBackupsLog(name+"  start backups...");
                        FileUtils.copyDirectory(codeFileUrl,backupsCodeFilePath);
                        joinBackupsLog(name+"  backups success...[DONE]");
                    }

                    /*
                     * 压缩备份代码文件夹
                     * */
                    joinBackupsLog(" start compress tar.gz...");
                    String substring = backupPath.substring(0, backupPath.lastIndexOf("/"));
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

                    RepositoryFileUtil.compressFolder(backupPath,"",tos);
                    joinBackupsLog(" compress tar.gz success...[DONE]");
                    logger.info("压缩成功");
                    // 关闭流
                    tos.close();
                    gzos.close();
                    bos.close();
                    fos.close();

                    /*
                     *  删除备份文件夹
                     * */
                    FileUtils.deleteDirectory(new File(backupPath));

                    joinBackupsLog(" Backups file success end [DONE]");

                    //修该备份信息
                    updateBackup(backups,"success");

                }catch (Exception e){
                    joinBackupsLog(" Backups file fail end,errorMsg:"+e.getMessage());
                    logger.info("错误信息:"+e.getMessage());
                    //修该备份text信息
                    updateBackup(backups,"fail");
                    throw new SystemException(e.getMessage());
                }
            }
        });
        return "OK";
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
                try {
                    /**
                     *  解压tar.gz包
                     */
                    //压缩包的绝对路径
                    String DecFileUrl = yamlDataMaService.uploadAddress() + "/" + fileName;
                    //压缩后的文件绝对路径
                    String name = fileName.substring(0, fileName.indexOf(".tar.gz"));
                    String afterDecFileUrl = yamlDataMaService.uploadAddress() + "/" + name + "/";
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
                    List<Repository> allRpy = repositoryServer.findAllRpyList();
                    for (Repository repository:allRpy){
                        joinRecoveryLog(repository.getName()+ "  start Recovery ...");
                        String codePath = yamlDataMaService.repositoryAddress() + "/" + repository.getRpyId() + ".git";
                        FileUtils.copyDirectory(new File(afterDecFileUrl+"code/"+repository.getRpyId()+".git"),new File(codePath));
                        joinRecoveryLog(repository.getName()+ " Recovery  success [DONE]");
                    }

                  /* *
                     *  删除解压后的文件
                     */
                    FileUtils.deleteDirectory(new File(yamlDataMaService.uploadAddress() + "/" + name ));

                    joinRecoveryLog("Recovery success end [DONE]");

                }catch (Exception e){
                    joinRecoveryLog("Recovery fail end，errorMsg:"+e.getMessage());
                    throw  new SystemException(e.getMessage());
                }
            }
        });
        return "OK";
    }




    @Override
    public ExecLog gainBackupsRes(String type) {

        String loginId = LoginContext.getLoginId();
        //备份日志
        if (("backups").equals(type)){
            ExecLog execLog = backupsExecLog.get(loginId);
            if (ObjectUtils.isEmpty(execLog)){
                return null;
            }
            return execLog;
        }
        //恢复的日志
        ExecLog execLog = recoveryLog.get(loginId);
        if (ObjectUtils.isEmpty(execLog)){
            return null;
        }
        return execLog;
    }



    /**
     *  dump database 备份脚本
     */
    public void executeScript(String backUpsUrl) throws IOException, InterruptedException {

        logger.info("备份的pgsql地址："+yamlDataMaService.pgSqlAddress());
        String[] args = new String[8];
        args[0] = "host="+yamlDataMaService.host();
        args[1] = "port=5432";
        args[2] = "userName="+jdbcUserName;
        args[3] = "password="+jdbcPassword;
        args[4] = "dbName="+yamlDataMaService.dbName();
        args[5] = "schemaName="+yamlDataMaService.schemaName();
        args[6] = "backupsUrl="+backUpsUrl;
        args[7] = "pgsqlUrl="+yamlDataMaService.pgSqlAddress();

        String scriptFile = AppHomeContext.getAppHome() + "/file/backups.sh";
        Process ps = Runtime.getRuntime().exec(scriptFile,args);
        ps.waitFor();
    }


    /**
     *  执行恢复PostgreSQL 数据脚本
     * @param backUpsSqlUrl 备份sql地址
     */
    public void executeRecoveryScript(String backUpsSqlUrl) throws IOException, InterruptedException {
        String[] args = new String[8];
        args[0] = "host="+yamlDataMaService.host();
        args[1] = "port=5432";
        args[2] = "userName="+jdbcUserName;
        args[3] = "password="+jdbcPassword;
        args[4] = "dbName="+yamlDataMaService.dbName();
        args[5] = "schemaName="+yamlDataMaService.schemaName();
        args[6] = "backupsSqlUrl="+backUpsSqlUrl;
        args[7] = "pgsqlUrl="+yamlDataMaService.pgSqlAddress();

        Process ps = Runtime.getRuntime().exec(AppHomeContext.getAppHome()+"/file/recovery.sh",args);

        ps.waitFor();
        BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            logger.info("执行日志："+line);
            sb.append(line).append("\n");
        }
    }


    /**
     *  拼接备份日志
     *  @param log 日志
     */
    public void joinBackupsLog(String log){

        LocalDateTime now = LocalDateTime.now();
        // 自定义时间格式
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String customDateTime = now.format(customFormatter);
        String resultLog = customDateTime + "---" + log;
        String loginId = LoginContext.getLoginId();
        ExecLog execResultLog = backupsExecLog.get(loginId);
        if (ObjectUtils.isEmpty(execResultLog)){
            ExecLog execLog = new ExecLog();
            execLog.setExecState("exec");
            execLog.setLog(resultLog);
            backupsExecLog.put(loginId,execLog);
        }else {
            String backupsLog = execResultLog.getLog();
            String newLog = backupsLog + "\n" + resultLog;
            if (log.contains("end")){
                execResultLog.setExecState("end");
            }
            execResultLog.setLog(newLog);
            backupsExecLog.put(loginId,execResultLog);
        }
        logger.info("日志:"+log);
    }

    /**
     *  拼接恢复日志
     *  @param log 日志
     */
    public void joinRecoveryLog(String log){
        LocalDateTime now = LocalDateTime.now();
        // 自定义时间格式
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String customDateTime = now.format(customFormatter);
        String resultLog = customDateTime + "---" + log;
        String loginId = LoginContext.getLoginId();
        ExecLog execResultLog = recoveryLog.get(loginId);
        if (ObjectUtils.isEmpty(execResultLog)){
            ExecLog execLog = new ExecLog();
            execLog.setExecState("exec");
            execLog.setLog(resultLog);
            recoveryLog.put(loginId,execLog);
        }else {
            String backupsLog = execResultLog.getLog();
            String newLog = backupsLog + "\n" + resultLog;
            if (log.contains("end")){
                execResultLog.setExecState("end");
            }
            execResultLog.setLog(newLog);
            recoveryLog.put(loginId,execResultLog);
        }
        logger.info("日志:"+execResultLog);
    }

    /**
     *  是否有足够空间备份
     *  @param backupFile 备份file
     * @param   repositoryFile 代码库总大小
     */
    public boolean memSize(File backupFile,File repositoryFile){
        //当前文件夹所在磁盘的总大小
        long totalSpace = backupFile.getTotalSpace();
        //备份文件夹占用大小
        long folderSize = FileUtils.sizeOf(backupFile);

        //代码库仓库的大小
        long repositorySize = FileUtils.sizeOf(repositoryFile);
        //留出1G的空间
        long l = repositorySize + 1073741824;


        long residue = totalSpace - folderSize;
        if (residue>l){
            return  true;
        }
        return false;
    }

    public void updateBackup(Backups backups,String result){
        backups.setExecTime(RepositoryUtil.date(1,new Date()));
        backups.setExecResult(result);
        backups.setExecState("end");
        this.updateBackups(backups);
    }



    /**
     *  修改备份记录写入文件
     */
    public void writeFile(){
        File file = new File(AppHomeContext.getAppHome()+"/file/backups");
        if (file.exists()){
            String  fileData = gainFileData(file);

            JSONObject jsonObject = JSONObject.parseObject(fileData);

            String backupsTime = jsonObject.get("backups-time").toString();
            fileData =fileData.replace(backupsTime,RepositoryUtil.date(1,new Date()));

            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(fileData);
                fileWriter.close();
            }catch (Exception e){
                throw new ApplicationException(5000,e.getMessage());
            }
        }
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
}
