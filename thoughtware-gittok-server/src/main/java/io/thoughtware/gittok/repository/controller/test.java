package io.thoughtware.gittok.repository.controller;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;

public class test {

    public static void main(String[] args) throws IOException, ParseException {
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();

        // 获取当前属于的星期几的阿拉伯数字
        int currentDayOfWeek = currentDate.getDayOfWeek().getValue();


        // 打印结果
        System.out.println("当前是星期: " + currentDayOfWeek);



        // 要添加的天数
        int daysToAdd = 57;

        // 将当前日期加上指定天数
        LocalDate newDate = currentDate.plusDays(daysToAdd);

        // 获取当前属于的星期几的阿拉伯数字
        int neWeek = newDate.getDayOfWeek().getValue();
        DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
        // 打印结果
        System.out.println("当前日期: " + currentDate);
        System.out.println("添加 " + daysToAdd + " 天后的日期: " + newDate);

        System.out.println("新的日期 " + daysToAdd + " 天后的周: " + neWeek);



        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(date);

        SimpleDateFormat simpleDateFormat01 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date parse = simpleDateFormat01.parse(format + " 06:06");

         format = simpleDateFormat01.format(parse);
        System.out.println("format " + format );


        SimpleDateFormat dateFormat = new SimpleDateFormat("ss mm HH dd MM ? yyyy");

        // 解析cron表达式为日期对象
        Date date02 = dateFormat.parse("00 40 15 08 01 ? 2024");
        format = simpleDateFormat01.format(date02);
        System.out.println("date02 " + format );
    }


}
