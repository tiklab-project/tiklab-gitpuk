package net.tiklab.xcode.until;

import net.tiklab.core.exception.ApplicationException;
import net.tiklab.xcode.code.model.CodeGroup;

import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CodeUntil {

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
        if (s1[0].equals("Windows")){
            return 1;
        }else {
            return 2;
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
        String[] cmd;
        if (CodeUntil.findSystemType()==1){
            if (!CodeUntil.isNoNull(path)){
                process = runtime.exec(" cmd.exe /c " + " " + order);
            }else {
                process = runtime.exec(" cmd.exe /c " + " " + order,null,new File(path));
            }
        }else {
            if (!CodeUntil.isNoNull(path)){
                cmd = new String[] { "/bin/sh", "-c", " source /etc/profile;"+ order };
                process = runtime.exec(cmd);
            }else {
                cmd = new String[] { "/bin/sh", "-c", "cd " + path + ";"+" source /etc/profile;"+ order };
                process = runtime.exec(cmd,null,new File(path));
            }
        }
        return process;
    }

    /**
     * 默认路径
     * @return 地址
     */
    public static String defaultPath(){
        String property = System.getProperty("user.home");
        String address = property + "/xcode/repository";
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

        // long between=(new Date().getTime()-date.getTime())/1000;//除以1000是为了转换成秒
        // long day = between / (24*3600) ;
        // long hour = between % (24*3600) / 3600;
        // long minute = between % 3600 / 60;
        // long second = between % 60 ;

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
     * 获取仓库地址
     * @param address 仓库地址
     * @param codeGroup 仓库组
     * @return 仓库详细地址
     */
    public static String findRepositoryAddress(String address, CodeGroup codeGroup){
        String s = defaultPath() + "/" + address;

        if (codeGroup != null){
            s =  defaultPath() + "/" + codeGroup.getName() + "/" + address;
        }
        return s;
    }


}


























