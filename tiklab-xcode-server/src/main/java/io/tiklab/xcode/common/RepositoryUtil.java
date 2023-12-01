package io.tiklab.xcode.common;

import io.tiklab.core.exception.ApplicationException;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class RepositoryUtil {

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
     * 默认路径 xcode
     * @return 地址
     */
    public static String defaultPath(){

        String property = System.getProperty("user.home");
        //String property="/var/opt/xcode";
        String address = property +"/xcode/repository";
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
     * @return 时间差
     */
    public static String time(Date date){
        long time = new Date().getTime();
        long dateTime = date.getTime();
        long l=time-dateTime;
        long day=l/(24*60*60*1000);
        long hour=(l/(60*60*1000)-day*24);
        long minute=((l/(60*1000))-day*24*60-hour*60);
        long second=(l/1000-day*24*60*60-hour*60*60-minute*60);

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
}


























