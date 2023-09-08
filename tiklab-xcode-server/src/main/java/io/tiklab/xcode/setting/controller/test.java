package io.tiklab.xcode.setting.controller;

import io.tiklab.core.context.AppHomeContext;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class test {
    public static void main(String[] args) throws IOException {
       // time();
        String appHome = AppHomeContext.getAppHome();
        System.out.println("appHome:"+appHome);
       /* File file = new File("/Users/limingliang/tiklab/tiklab-xcode/backup");
        long totalSpace = file.getTotalSpace();
        // 3. 对返回的总空间进行格式化处理
        String formattedSpace = formatSize(totalSpace);

        // 输出结果
        System.out.println("磁盘总空间：" + formattedSpace);

        long sizeOf = FileUtils.sizeOf(file);

        String formatted = formatSize(sizeOf);
        System.out.println("使用空间："+formatted);
        File[] files = file.listFiles();
        List<File> collected = Arrays.stream(files).sorted(Comparator.comparing(a -> a.lastModified())).collect(Collectors.toList());
        //删除文件
        FileUtils.deleteQuietly(collected.get(0));
        System.out.println("collected:"+collected);*/
    }

    // 格式化文件大小，将字节转换为可读性更好的格式
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


    // 格式化文件大小，将字节转换为可读性更好的格式
    public static void time() {
           Long timestamp1=1693401943L;
        long timestamp2 = System.currentTimeMillis();

        Date date1 = new Date(timestamp1);
        Date date2 = new Date(timestamp2);

        LocalDate localDate1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        long days = ChronoUnit.DAYS.between(localDate1, localDate2);
        System.out.println("Days difference: " + days);

    }
}
