package net.tiklab.xcode.git;


import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.patch.Patch;
import net.tiklab.core.exception.ApplicationException;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.pkcs.RSAPublicKey;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;
import org.eclipse.jgit.api.DiffCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.*;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GitTest {


    public static void main(String[] args) throws Exception {

        findCommits(
                "C:\\Users\\admin\\xcode\\repository\\matflow.git",
                "6ddf6f4ebda29617e62d1935f781f2ba8b8bd0a2"
        );
    }

    public static void findCommits(String repositoryAddress,String commitId) throws IOException {

        Git git = Git.open(new File(repositoryAddress));
        Repository repository = git.getRepository();
        RevWalk walk = new RevWalk(repository);
        ObjectId objectId = repository.resolve(commitId);
        RevCommit firstCommit = walk.parseCommit(objectId);

        ObjectId oldObjectId = repository.resolve("53417287fa9665f713d020e3ec1ca0545e199fef");
        RevCommit oldCommit = walk.parseCommit(oldObjectId);

        DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
        df.setRepository(repository);
        df.setDiffComparator(RawTextComparator.DEFAULT);
        df.setDetectRenames(true);
        PathFilter pathFilter = PathFilter.create(".gitignore");
        df.setPathFilter(pathFilter);
        List<DiffEntry> scan = df.scan(oldCommit, firstCommit);
        //
        for (DiffEntry diffEntrys : scan) {
            String path = diffEntrys.getNewPath();
            // 获取文件内容变更
            ObjectReader reader = repository.newObjectReader();
            CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
            oldTreeIter.reset(reader, oldCommit.getTree());
            CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
            newTreeIter.reset(reader, firstCommit.getTree());
            DiffFormatter diffFormatter = new DiffFormatter(System.out);
            diffFormatter.setRepository(git.getRepository());
            diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
            diffFormatter.setDetectRenames(true);
            List<DiffEntry> diffEntries = diffFormatter.scan(oldTreeIter, newTreeIter);
            for (DiffEntry diffEntry : diffEntries) {
                    if (diffEntry.getNewPath().equals(path)) {
                        diffFormatter.format(diffEntry);
                    }
                }
            }


        }


    /**
     * 对比两文件的差异，返回原始文件+diff格式
     *
     * @param original         原文件内容
     * @param revised          对比文件内容
     * @param originalFileName 原始文件名
     * @param revisedFileName  对比文件名
     */
    public static List<String> diffString(
            List<String> original,
            List<String> revised,
            String originalFileName,
            String revisedFileName) {
        originalFileName = originalFileName == null ? "原始文件" : originalFileName;
        revisedFileName = revisedFileName == null ? "对比文件" : revisedFileName;
        //两文件的不同点
        Patch<String> patch = com.github.difflib.DiffUtils.diff(original, revised);
        //生成统一的差异格式
        List<String> unifiedDiff = UnifiedDiffUtils.generateUnifiedDiff(originalFileName, revisedFileName, original, patch, 0);
        int diffCount = unifiedDiff.size();
        if (unifiedDiff.size() == 0) {
            //如果两文件没差异则插入如下
            unifiedDiff.add("--- " + originalFileName);
            unifiedDiff.add("+++ " + revisedFileName);
            unifiedDiff.add("@@ -0,0 +0,0 @@");
        } else if (unifiedDiff.size() >= 3 && !unifiedDiff.get(2).contains("@@ -1,")) {
            unifiedDiff.set(1, unifiedDiff.get(1));
            //如果第一行没变化则插入@@ -0,0 +0,0 @@
            unifiedDiff.add(2, "@@ -0,0 +0,0 @@");
        }
        //原始文件中每行前加空格
        List<String> original1 = original.stream().map(v -> " " + v).collect(Collectors.toList());
        //差异格式插入到原始文件中
        return insertOrig(original1, unifiedDiff);
    }
    //统一差异格式插入到原始文件
    public static List<String> insertOrig(List<String> original, List<String> unifiedDiff) {
        List<String> result = new ArrayList<>();
        //unifiedDiff中根据@@分割成不同行，然后加入到diffList中
        List<List<String>> diffList = new ArrayList<>();
        List<String> d = new ArrayList<>();
        for (int i = 0; i < unifiedDiff.size(); i++) {
            String u = unifiedDiff.get(i);
            if (u.startsWith("@@") && !"@@ -0,0 +0,0 @@".equals(u) && !u.contains("@@ -1,")) {
                List<String> twoList = new ArrayList<>();
                twoList.addAll(d);
                diffList.add(twoList);
                d.clear();
                d.add(u);
                continue;
            }
            if (i == unifiedDiff.size() - 1) {
                d.add(u);
                List<String> twoList = new ArrayList<>();
                twoList.addAll(d);
                diffList.add(twoList);
                d.clear();
                break;
            }
            d.add(u);
        }

        //将diffList和原始文件original插入到result，返回result
        for (int i = 0; i < diffList.size(); i++) {
            List<String> diff = diffList.get(i);
            List<String> nexDiff = i == diffList.size() - 1 ? null : diffList.get(i + 1);
            //含有@@的一行
            String simb = i == 0 ? diff.get(2) : diff.get(0);
            String nexSimb = nexDiff == null ? null : nexDiff.get(0);
            //插入到result
            insert(result, diff);
            //解析含有@@的行，得到原文件从第几行开始改变，改变了多少（即增加和减少的行）
            Map<String, Integer> map = getRowMap(simb);
            if (null != nexSimb) {
                Map<String, Integer> nexMap = getRowMap(nexSimb);
                int start = 0;
                if (map.get("orgRow") != 0) {
                    start = map.get("orgRow") + map.get("orgDel") - 1;
                }
                int end = nexMap.get("revRow") - 2;
                //插入不变的
                insert(result, getOrigList(original, start, end));
            }

            if (simb.contains("@@ -1,") && null == nexSimb && map.get("orgDel") != original.size()) {
                insert(result, getOrigList(original, 0, original.size() - 1));
            } else if (null == nexSimb && (map.get("orgRow") + map.get("orgDel") - 1) < original.size()) {
                int start = (map.get("orgRow") + map.get("orgDel") - 1);
                start = start == -1 ? 0 : start;
                insert(result, getOrigList(original, start, original.size() - 1));
            }
        }
        //如果你想知道两文件有几处不同可以放开下面5行代码注释，会在文件名后显示总的不同点有几处（即预览图中的xxx different），放开注释后有一个小缺点就是如果对比的是java、js等代码文件那代码里的关键字就不会高亮颜色显示,有一点不美观。
        int diffCount = diffList.size() - 1;
        if (!"@@ -0,0 +0,0 @@".equals(unifiedDiff.get(2))) {
           diffCount = diffList.size() > 1 ? diffList.size() : 1;
        }
        result.set(1, result.get(1) + " ( " + diffCount + " different )");
        return result;
    }

    //将源文件中没变的内容插入result
    public static void insert(List<String> result, List<String> noChangeContent) {
        for (String ins : noChangeContent) {
            result.add(ins);
        }
    }

    //解析含有@@的行得到修改的行号删除或新增了几行
    public static Map<String, Integer> getRowMap(String str) {
        Map<String, Integer> map = new HashMap<>();
        if (str.startsWith("@@")) {
            String[] sp = str.split(" ");
            String org = sp[1];
            String[] orgSp = org.split(",");
            //源文件要删除行的行号
            map.put("orgRow", Integer.valueOf(orgSp[0].substring(1)));
            //源文件删除的行数
            map.put("orgDel", Integer.valueOf(orgSp[1]));

            String[] revSp = org.split(",");
            //对比文件要增加行的行号
            map.put("revRow", Integer.valueOf(revSp[0].substring(1)));
            map.put("revAdd", Integer.valueOf(revSp[1]));
        }
        return map;
    }

    //从原文件中获取指定的部分行
    public static List<String> getOrigList(List<String> original1, int start, int end) {
        List<String> list = new ArrayList<>();
        if (original1.size() >= 1 && start <= end && end < original1.size()) {
            for (; start <= end; start++) {
                list.add(original1.get(start));
            }
        }
        return list;
    }






















}








































