package io.thoughtware.gittok.repository.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.thoughtware.gittok.common.RepositoryUtil;
import io.thoughtware.gittok.common.git.GitBranchUntil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.api.CreateBranchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.PackFile;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.revwalk.*;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class test {

    public static void main(String[] args) throws IOException, ParseException {


        try {
            String bareRepositoryPath = "/Users/limingliang/tiklab/thoughtware-gittok/repository/e35b85357fcb.git"; // 裸仓库路径
            String freeRepositoryPath = "/Users/limingliang/tiklab/thoughtware-gittok/repository/e35b85357fcb"; // 裸仓库路径

           /* Git.cloneRepository()
                    .setURI("file://" + bareRepositoryPath)
                    .setDirectory(new File(freeRepositoryPath))
                    .setCloneAllBranches(true)  // 克隆所有分支
                    .call();*/


            Repository repository = Git.open(new File(freeRepositoryPath)).getRepository();
            Collection<Ref> refs = Git.lsRemoteRepository().setHeads(true).setRemote(bareRepositoryPath).call();
            for (Ref branch : refs) {
                String branchName = branch.getName();
                String localBranchName = branchName.substring(branchName.lastIndexOf('/') + 1);

                // Check out the branch
                Git git = Git.open(new File(freeRepositoryPath));
                git.checkout()
                        .setName(localBranchName)
                        .setCreateBranch(true)
                        .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
                        .setStartPoint(branchName)
                        .call();

                System.out.println("Checked out branch: " + localBranchName);
            }



            /*String branchToMerge="source1";
            Git git = Git.open(Paths.get(freeRepositoryPath).toFile());



            // 切换到目标分支
            git.checkout()
                    .setName(branchToMerge)
                    .call();*/

           /* // 获取要合并的分支的 ObjectId
                ObjectId branchToMergeId = git.getRepository().resolve(branchToMerge);


                // 获取要合并的分支的 Ref
                Ref branchToMergeRef = git.getRepository().findRef(branchToMerge);

                // 创建合并节点
                ObjectId mergeCommitId = git.commit()
                        .setMessage("Merge " + branchToMerge)
                        .setAuthor("Your Name", "your.email@example.com")
                        .call();



            git.merge()
                    .include(branchToMergeRef.getObjectId())
                    .setStrategy(MergeStrategy.RESOLVE)
                    .setFastForward(MergeCommand.FastForwardMode.NO_FF)
                    .setCommit(false)
                    .call();*/


           /* String gitLabUrl = "https://api.github.com/user/repos";
            String privateToken = "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";

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(gitLabUrl + "?owned=true&simple=true")
                    .header("Authorization", "token "+privateToken)
                    .header("accept","application/vnd.github+json")
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                Object parse = JSONArray.parse(responseBody);
                List<Object> jsonObjects = (List<Object>) parse;
                System.out.println("");
            }*/

       /*     String jsonString = "{\"_id\":\"bytewise-core\",\"_rev\":\"11-69581e869d1917853984be117a92c068\",\"name\":\"bytewise-core\",\"description\":\"Binary serialization of arbitrarily complex structures that sort element-wise\",\"dist-tags\":{\"latest\":\"1.2.3\"},\"versions\":{\"1.0.0\":{\"maintainers\":[{\"name\":\"deanlandolt\",\"email\":\"dean@deanlandolt.com\"}],\"dist\":{\"shasum\":\"653f08fe684700611d5dfa00e10beee8b91b387b\",\"tarball\":\"https://registry.npmjs.org/bytewise-core/-/bytewise-core-1.0.0.tgz\",\"integrity\":\"sha512-XM2I3/jx62HbW4FqtfCWwgBjZujfn3PJN3ftZp352BRD2dQfJbG+cSpdCnfDQnXGwqtSTRjZOZTm7uealGMrng==\",\"signatures\":[{\"keyid\":\"SHA256:jl3bwswu80PjjokCgh0o2w5c2U4LhQAE57gj9cz1kzA\",\"sig\":\"MEYCIQCj4n9ox7svx6DVqdEdju6n2GD8lqhJ+3vpWZWQd7NyOgIhAOf7BwL6v0V70HICWscjjBBeVchzCMi9hqOyQxll0wYN\"}]},\"directories\":{}}}}";

        // 解析JSON字符串为JSONObject
          JSONObject jsonObject = JSON.parseObject(jsonString);

        // 替换tarball字段的值
            jsonObject.getJSONObject("versions").getJSONObject("1.0.0").getJSONObject("dist").put("tarball", "http://172.13.1.11:8081/repository/npm-public/bytewise-core/-/bytewise-core-1.0.0.tgz");

        // 将Java对象转换为JSON字符串
            String updatedJsonString = jsonObject.toJSONString();

            System.out.println(updatedJsonString);*/


         /*   // 创建请求头对象
            HttpHeaders headers = new HttpHeaders();
            String s ="admin"+ ":" +"123456";
            String baseString = Base64.getEncoder().encodeToString(s.getBytes());
            headers.set("Authorization","Basic "+baseString);


            // 创建 HttpEntity 包含请求体和请求头
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            //设置连接超时时间
            ClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
            ((SimpleClientHttpRequestFactory) factory).setConnectTimeout(10000);

            RestTemplate restTemplate = new RestTemplate(factory);
            ResponseEntity<byte[]> entity = restTemplate.exchange("http://192.168.10.9:8080/repository/maven-public/test", HttpMethod.GET, requestEntity, byte[].class);
            // ResponseEntity<byte[]> entity = restTemplate.getForEntity(relativeAbsoluteUrl, byte[].class);
            System.out.println("123");*/
           /* LocalDateTime currentTime = LocalDateTime.now();
            LocalDateTime newTime = currentTime.minusMinutes(1).minusSeconds(5);

            ZoneId zoneId = ZoneId.systemDefault();
            java.util.Date date = Date.from(newTime.atZone(zoneId).toInstant());
            String time = RepositoryUtil.time(date,"scan");

            System.out.println("time"+time);


            FileUtils.deleteQuietly(new File("/Users/limingliang/source/thoughtware-cms-api-1.0.0-javadoc.jar.asc"));
*/



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


