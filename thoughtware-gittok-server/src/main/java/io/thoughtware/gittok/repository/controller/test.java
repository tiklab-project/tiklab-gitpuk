package io.thoughtware.gittok.repository.controller;


import io.thoughtware.core.exception.SystemException;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.*;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class test {

    public static void main(String[] args) throws Exception {

        //裸仓库地址
        String address = "/Users/limingliang/tiklab/thoughtware-gittok/repository/temporary/355823dcb342/test/111";
        //String address = "/Users/limingliang/tiklab/thoughtware-gittok/repository/4fdb59458fdf.git";

        try {
            FileUtils.writeStringToFile(new File(address),"12344", StandardCharsets.UTF_8);


        }catch (Exception e){
            throw new SystemException(e);
        }


      /*  ProcessBuilder processBuilder = new ProcessBuilder("bash", "-c", a);
        Process npmProcess = processBuilder.start();
        readFile(npmProcess);*/



       /* List<String> a = new ArrayList<>();
        a.add("1");
        a.add("2");
        a.add("3");

        List<String> b = new ArrayList<>();
        b.add("4");


        boolean hasSameObject = a.stream().anyMatch(b::contains); */
    }


    public static void   readFile(Process process) throws IOException {


        // 获取命令行输出
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        // mvn 编译日志,读取执行信息
        while ((line = reader.readLine()) != null) {
            System.out.println("日志1:"+line);

        }

        //spotBugs日志,读取err执行信息
        InputStream errorStream = process.getErrorStream();
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
        String errorLine;
        while ((errorLine = errorReader.readLine()) != null) {
            System.out.println("日志2:"+errorLine);
        }
        inputStream.close();
        errorStream.close();

    }
}
