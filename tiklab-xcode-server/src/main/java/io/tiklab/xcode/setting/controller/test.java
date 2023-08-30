package io.tiklab.xcode.setting.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.boot.SpringApplication;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class test {
    public static void main(String[] args) throws IOException {
        File file = new File("/Users/limingliang/tiklab/tiklab-xcode/backup");
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
        System.out.println("collected:"+collected);
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
}
