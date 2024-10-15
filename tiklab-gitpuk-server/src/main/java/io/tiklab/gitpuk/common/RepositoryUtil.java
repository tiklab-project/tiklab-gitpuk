package io.tiklab.gitpuk.common;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.KeyPair;
import io.tiklab.core.exception.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class RepositoryUtil {
    private static Logger logger = LoggerFactory.getLogger(RepositoryUtil.class);
    /**
     * 判断字符串是否为空
     * @param s 字符串
     * @return true:不为空 false:空
     */
    public static boolean isNoNull(String s){
        if (s == null){
            return false;
        }
        if (s.equals(" ")){
            return false;
        }
        if (s.equals("")){
            return false;
        }
        if (s.equals("\n")){
            return false;
        }
        if (s.equals("null")){
            return false;
        }
        return !s.isEmpty();
    }

    /**
     * 系统类型
     * @return 1.windows 2.其他
     */
    public static int findSystemType(){
        String property = System.getProperty("os.name");
        String[] s1 = property.split(" ");
       switch (s1[0]){
           case "Windows":
               return 1;
           case "Mac":
               return 2;
           default:
               return 3;
       }
    }

    /**
     * 执行cmd命令
     * @param path 执行文件夹
     * @param order 执行命令
     * @return 执行信息
     * @throws IOException 调取命令行失败
     */
    public static Process process(String path,String order) throws IOException {
        Runtime runtime=Runtime.getRuntime();
        Process process;
        if (RepositoryUtil.findSystemType()==1){
            if (!RepositoryUtil.isNoNull(path)){
                process = runtime.exec(" cmd.exe /c " + " " + order);
            }else {
                process = runtime.exec(" cmd.exe /c " + " " + order,null,new File(path));
            }
        }else {
            if (!RepositoryUtil.isNoNull(path)){
                String[]  cmd = new String[] { "/bin/sh", "-c", " source /etc/profile;"+ order };
                process = runtime.exec(cmd);
            }else {
                String[]  cmd = new String[] { "/bin/sh", "-c", "cd " + path + ";"+" source /etc/profile;"+ order };
                process = runtime.exec(cmd,null,new File(path));
            }
        }
        return process;
    }


    /**
     * 执行命令
     * @param path 执行文件夹
     * @param order 执行命令
     * @return 执行信息
     * @throws IOException 调取命令行失败
     */
    public static Process execOrder(String order,String path) throws IOException {
        Runtime runtime=Runtime.getRuntime();
        Process process = runtime.exec(order.toString());
        return process;
    }


    /**
     * 默认路径 gittok
     * @return 地址
     */
    public static String defaultPath(){

        String property = System.getProperty("user.home");
        //String property="/var/opt/gittok";
        String address = property +"/gitpuk/repository";
        //根目录
        File file = new File(address);
        return file.getAbsolutePath();
    }

    /**
     * 返回系统时间
     * @param type 时间类型 1.(yyyy-MM-dd HH:mm:ss) 2.(yyyy-MM-dd) 3.(HH:mm:ss) 4.([format]) 5.(HH:mm)
     * @return 时间
     */
    public static String date(int type,Date date){
        switch (type) {
            case 2 -> {
                return new SimpleDateFormat("yyyy-MM-dd").format(date);
            }
            case 3 -> {
                return new SimpleDateFormat("HH:mm:ss").format(date);
            }
            case 4 -> {
                String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                return "[" + format + "]" + "  ";
            }
            case 5 -> {
                return new SimpleDateFormat("HH:mm").format(date);
            }
            default -> {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
            }
        }
    }

    /**
     * 获取与当前时间的时间差
     * @param date 时间
     * @param  type 类型 scan、commit
     * @return 时间差
     */
    public static String time(Date date,String type){
        long time = new Date().getTime();
        long dateTime = date.getTime();
        long l=time-dateTime;


        long day=l/(24*60*60*1000);
        long hour=(l/(60*60*1000)-day*24);
        long minute=((l/(60*1000))-day*24*60-hour*60);
        long second=(l/1000-day*24*60*60-hour*60*60-minute*60);
        if (("scan").equals(type)||("clean").equals(type)){
            if (minute != 0){
                return minute+"分钟"+second+"秒";
            }
            return second+"秒";
        }
        if (day != 0){
            return day+"天";
        }
        if (hour != 0){
            return hour+"小时";
        }
        if (minute != 0){
            return minute+"分钟";
        }
        return second+"秒";
    }

    /**
     * 仓库在内存里面的存储路径
     * @param InitialPath 仓库存储初始路径
     * @param repositoryId 服务内存以仓库ID 存储仓库
     * @return 仓库详细地址
     */
    public static String findRepositoryAddress(String InitialPath, String repositoryId){
        String s = InitialPath + "/" + repositoryId+ ".git";
        File file = new File(s);
        return file.getAbsolutePath();
    }

    /**
     * 仓库的临时文件夹
     * @param InitialPath 仓库存储初始路径
     * @param repositoryId 服务内存以仓库ID 存储仓库
     * @return 仓库详细地址
     */
    public static String getRpyTemporaryPath(String InitialPath, String repositoryId){
        String s = InitialPath + "/temporary/" + repositoryId;
        File file = new File(s);
        return file.getAbsolutePath();
    }

    /**
     * 仓库的lfs文件地址
     * @param InitialPath 仓库存储初始路径
     * @param repositoryId 服务内存以仓库ID 存储仓库
     * @return 仓库详细地址
     */
    public static String getRpyLfsPath(String InitialPath, String repositoryId){
        String s = InitialPath + "/lfs/" + repositoryId;
        File file = new File(s);
        return file.getAbsolutePath();
    }

    /**
     * 效验地址是否存在配置文件
     * @param fileAddress 文件地址
     * @param type 文件类型
    // * @return 匹配状态  1.不是个目录或不存在这个文件夹  2. 空目录找不到可执行文件 0. 匹配成功
     */
    public static void validFile(String fileAddress, int type) throws ApplicationException {
        File file = new File(fileAddress);

        //不存在这个目录
        if (!file.exists()){
            throw new ApplicationException("git可执行程序地址错误，找不到 "+fileAddress+" 这个目录。");
        }
        //不是个目录
        if (!file.isDirectory()){
            throw new ApplicationException(fileAddress+"不是个目录。");
        }
        //不存在可执行文件
        File[] files = file.listFiles();
        if (files == null || files.length == 0){
            throw new ApplicationException("在"+fileAddress+"找不到可执行文件。");
        }

        for (File listFile : Objects.requireNonNull(file.listFiles())) {
            if (listFile.isDirectory()){
                continue;
            }
            String name = listFile.getName();
            switch (type) {
                case 1,2,3,4 -> {
                    if (name.equals("git") || name.equals("git.exe")) {
                        return ;
                    }
                }
                case 5 -> {
                    if (name.equals("svn") || name.equals("svn.exe")) {
                        return ;
                    }
                }
                case 21 -> {
                    if (name.equals("mvn")) {
                        return ;
                    }
                }
                case 22 -> {
                    if (name.equals("npm")) {
                        return ;
                    }
                }
            }
        }
    }

    /**
     * 不同系统返回的地址
     * @param address
     * @return 位置
     */
    public static String SystemTypeAddress(String address) {


        int systemType = findSystemType();
        if (systemType == 1) {
            return address.replace("/", "\\");
        }
        return address;
    }

    /**
     * 删除文件和文件夹
     * @param address
     * @param  dire 文件夹
     * @return 位置
     */
    public static Process deleteDireAndFile(String address,String dire) {
        try {
            Runtime runtime=Runtime.getRuntime();
            Process process;
            if (RepositoryUtil.findSystemType()==1){
                process = runtime.exec(" cmd.exe /c " + " " + "rm -rf "+ dire);
            }else {
                String[]  cmd = new String[] { "/bin/sh", "-c", "cd " + address + ";"+" source /etc/profile;"+ "rm -rf "+ dire };
                process=runtime.exec(cmd);
            }
            return process;
        }catch (Exception e){
            throw  new ApplicationException(e.getMessage());
        }
    }

    /**
     * 获取与当前时间的时间差
     * @param time
     * @return 位置
     */
    public static String timeBad(Long time){
        long longTime = System.currentTimeMillis() -time;
        long days = longTime / (24 * 60 * 60 * 1000); // 计算天数
        long hours = (longTime % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000); // 计算小时数
        long minutes = (longTime % (60 * 60 * 1000)) / (60 * 1000); // 计算分钟数

        String badTime=null;
        if (days!=0){
            badTime= days + "天"+ hours + "时前";
        }
        if (days==0&&hours!=0){
            badTime= hours + "时"+ minutes + "分前";
        }
        if (days==0&&hours==0){
            badTime= minutes + "分前";
        }

        return badTime;
    }

    /**
     * 递归获取文件夹下面的所有文件
     * @param path
     * @return 位置
     */
    public static List<String> getFilePath(File path, List<String> list){
        File[] fa = path.listFiles();
        if (fa != null) {
            for (File file : fa) {
                if (file.isDirectory()){
                    getFilePath(file,list);
                }
                list.add(file.getPath());
            }
        }
        return list;
    }

    /**
     * 转换文件大小单位
     * @param size 单位字节
     * @return 位置
     */
    public static String formatSize(long size) {
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        double temp = size;
        int index = 0;
        while (temp >= 1024) {
            temp /= 1024;
            index++;
        }
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(temp) + units[index];
    }


    /**
     * 生成随机数
     * @param length 随机数个数
     * @return 位置
     */
    public static String gen(int length) {
        char[] ss = new char[length];
        int[] flag = {0,0,0}; //A-Z, a-z, 0-9
        int i=0;
        while(flag[0]==0 || flag[1]==0 || flag[2]==0 || i<length) {
            i = i%length;
            int f = (int) (Math.random()*3%3);
            if(f==0)
                ss[i] = (char) ('A'+Math.random()*26);
            else if(f==1)
                ss[i] = (char) ('a'+Math.random()*26);
            else
                ss[i] = (char) ('0'+Math.random()*10);
            flag[f]=1;
            i++;
        }
        return new String(ss);
    }

    /**
     *通过字节计算大小
     * @param sizeByte 大小 单位字节
     * @return
     */
    public static String countStorageSize(long sizeByte){
        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        double num =(double) sizeByte / 1024;
        if (sizeByte<1048576){
            String KbNum = decimalFormat.format(num);
            return KbNum+"Kb";
        }
        //小于1G
        if (num<1048576){
            double l = (double) sizeByte / (1024 * 1024);
            String MB = decimalFormat.format(l);
            return MB+"MB";
        }
        //大于1G
        if (num>=1048576){
            double gbNum =(double) sizeByte / 1024/1024/1024;
            String GB = decimalFormat.format(gbNum);
            return GB+"GB";
        }
        return null;
    }

    /*
    * 获取磁盘空间大小
    * */
    public static float findDiskSize(String dir){
        File folder = new File(dir);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        while (folder.getParentFile() != null) {
            folder = folder.getParentFile();
        }
        String rootPath = folder.getPath();
        File root = new File(rootPath);
        long diskSpace =  root.getTotalSpace();
        float l = (float)diskSpace / (1024 * 1024 * 1024);
        // 使用 BigDecimal 控制小数位数
        BigDecimal decimalL = new BigDecimal(Float.toString(l));
        decimalL = decimalL.setScale(2, RoundingMode.HALF_UP);

        return decimalL.floatValue();
    }

    /*
     * 获取公钥指纹
     * */
    public static String getPublicKeyFinger(String data){
        try {
            // 获取公钥指纹
            byte[] publicKeyBytes = data.getBytes("UTF-8");
            JSch jsch = new JSch();
            KeyPair publicKey = KeyPair.load(jsch, null, publicKeyBytes);
            String fingerprint = publicKey.getFingerPrint();
            // 清理资源
            publicKey.dispose();
            return fingerprint;
        } catch (Exception e) {
            logger.error("获取公钥指纹报错:"+e.getMessage());
            throw  new ApplicationException("公钥的格式不正确");
        }
    }


    /**
     *执行maven编译
     * @param mavenPath maven地址
     * @param  repositoryPath 编译项目地址
     * @return
     */
    public static Process mavenBuild(String mavenPath,String repositoryPath) throws IOException {
        String path = mavenPath+"/mvn";
        ProcessBuilder processBuilder = new ProcessBuilder(
                path,
                "clean",
                "compile"
        );
        processBuilder.directory(new File(repositoryPath));
        return processBuilder.start();
    }

    /*
     * maven编译错误信息
     * */
    public static Map<String,String> mavenBuildError(){
        Map<String,String> map = new HashMap<>();
        map.put("svn: E170000:","");
        map.put("invalid option;","");
        map.put("BUILD FAILURE","构建失败！");
        return map;
    }



    /**
     * 获取公钥的Base64编码数据
     * @param key 公钥
     * @return 公钥
     */
    public static String findKeyBase64(String key){
        try {
            if (key.startsWith(RepositoryFinal.Key_TYPE_OPENSSH_RSA)){
                //截取ssh-rsa中的Base64编码数据
                int i = key.indexOf(" ");
                int i1 = key.lastIndexOf(" ");
                return key.substring(i+1,i1);
            }else if (key.startsWith(RepositoryFinal.Key_TYPE_SSH_RSA)){
                return null;
            }
        }catch (Exception e){
            return null;
        }
        return null;
    }

    /**
     * 保存的key
     * @param key 服务器存储的key
     * @return PublicKey对象
     */
    public static PublicKey ValidRsaKey(String key){
        try {
            byte[] decoded = Base64.getDecoder().decode(key);

            // 将字节数组包装到缓冲区中
            ByteBuffer byteBuffer = ByteBuffer.wrap(decoded);
            //自动更新的SIZEOF_INT的值
            AtomicInteger position = new AtomicInteger();

            String algorithm = readString(byteBuffer, position);
            //判断是否为 Key_TYPE_OPENSSH_RSA格式的公钥
            if (!RepositoryFinal.Key_TYPE_OPENSSH_RSA.equals(algorithm)){
                return null;
            }

            // 字符串转换成字节
            BigInteger publicExponent = readMpint(byteBuffer, position);
            BigInteger modulus = readMpint(byteBuffer, position);
            String string = modulus.toString();
            //字节转换成PublicKey公钥
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, publicExponent);
            KeyFactory kf = KeyFactory.getInstance(RepositoryFinal.SSH_ENCODER_RSA);
            return kf.generatePublic(keySpec);

        } catch (Exception e) {
            return null;
        }
    }



    //字节转换成大数
    public static BigInteger readMpint(ByteBuffer buffer, AtomicInteger pos){
        byte[] bytes = readBytes(buffer, pos);
        if(bytes.length == 0){
            return BigInteger.ZERO;
        }
        return new BigInteger(bytes);
    }

    //字节转换成字符
    public static String readString(ByteBuffer buffer, AtomicInteger pos){
        byte[] bytes = readBytes(buffer, pos);
        if(bytes.length == 0){
            return "";
        }
        return new String(bytes, StandardCharsets.US_ASCII);
    }

    //转换成字节
    public static byte[] readBytes(ByteBuffer buffer, AtomicInteger pos){
        int len = buffer.getInt(pos.get());
        byte[] buff = new byte[len];
        int SIZEOF_INT = 4;
        for(int i = 0; i < len; i++) {
            buff[i] = buffer.get(i + pos.get() + SIZEOF_INT);
        }
        pos.set(pos.get() + SIZEOF_INT + len);
        return buff;
    }

    // 根据扩展名判断文件类型
    public static String getFileTypeByExtension(String fileName) {
        if (fileName.endsWith("zip")) {
            return "zip";
        } else if (fileName.endsWith("tar")) {
            return "tar";
        } else if (fileName.endsWith("jpg")) {
            return "image/jpeg";
        } else if (fileName.endsWith("png")) {
            return "image/png";
        }else if(fileName.endsWith("tar.gz")){
            return "tar.gz";
        }
        return "file";
    }

    // 是否是压缩包
    public static boolean isCompressPackage(String fileName) {
        if (fileName.endsWith("zip")||fileName.endsWith("rar")||fileName.endsWith("7z")||fileName.endsWith("tar")||
                fileName.endsWith("tar.gz")||fileName.endsWith("tar.bz2")||fileName.endsWith("gz")||fileName.endsWith("bz2")||
                fileName.endsWith("xz")||fileName.endsWith("iso")||fileName.endsWith("dmg")||fileName.endsWith("cab")||
                fileName.endsWith("arj")){
            return true;
        }else {
            return false;
        }
    }
}