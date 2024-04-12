package io.thoughtware.gittok.repository.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.thoughtware.gittok.commit.model.Commit;
import io.thoughtware.gittok.commit.model.CommitFileDiff;
import io.thoughtware.gittok.commit.model.MergeData;
import io.thoughtware.gittok.common.RepositoryUtil;
import io.thoughtware.gittok.common.git.GitBranchUntil;
import io.thoughtware.gittok.common.git.GitCommitUntil;
import io.thoughtware.gittok.common.git.GitUntil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.internal.storage.file.PackFile;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.merge.ThreeWayMerger;
import org.eclipse.jgit.revwalk.*;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
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

    public static void main(String[] args) throws Exception {
        String bareRepositoryPath = "/Users/limingliang/tiklab/thoughtware-gittok/repository/585456fc2e3f.git"; // 裸仓库路径
        String RepositoryPath = "/Users/limingliang/tiklab/thoughtware-gittok/repository/clone/e35b85357fcb"; // 裸仓库路径

        MergeData mergeData = new MergeData();
        mergeData.setMergeOrigin("a1");
        mergeData.setMergeTarget("b1");

        GitBranchUntil.getMergeClashFile(mergeData,bareRepositoryPath);

       /* List<String> a = new ArrayList<>();
        a.add("1");
        a.add("2");
        a.add("3");

        List<String> b = new ArrayList<>();
        b.add("4");


        boolean hasSameObject = a.stream().anyMatch(b::contains);


        ThreeWayMerger merger = MergeStrategy.RECURSIVE.newMerger(repository,true);*/


        System.out.println("");



    }


}


