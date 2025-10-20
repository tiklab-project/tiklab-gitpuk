package io.tiklab.gitpuk.common.git;

import io.tiklab.gitpuk.commit.model.*;
import io.tiklab.gitpuk.common.RepositoryFinal;
import io.tiklab.gitpuk.common.RepositoryUtil;
import io.tiklab.core.exception.ApplicationException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.springframework.util.ObjectUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * jgit获取仓库提交信息
 */
public class GitCommitUntil {


    /**
     * 获取分支的提交记录
     * @param repository 仓库
     * @param commit 提交信息
     * @return 提交记录
     * @throws IOException 仓库不存在
     * @throws ApplicationException 分支不存在
     */
    public static List<CommitMessage> findBranchCommit(Repository repository , Commit commit)
            throws IOException , ApplicationException {

        ObjectId refObjectId = getRefObjectId(repository, commit.getRefCode(), commit.getRefCodeType());
        if (ObjectUtils.isEmpty(refObjectId)){
            return null;
        }

 /*       if (!isCommitId){
            //分支是否存在
            Ref head = repository.findRef(Constants.R_HEADS +branch);
            if (head == null) {
                return Collections.emptyList();
            }
            objectId = head.getObjectId();
        }else {
            objectId = ObjectId.fromString(branch);
        }*/
        RevWalk revWalk = new RevWalk(repository);
        revWalk.sort(RevSort.COMMIT_TIME_DESC);
        revWalk.markStart(revWalk.parseCommit(refObjectId));

        int i = 0;
        String number = commit.getNumber();
        List<CommitMessage> list = new ArrayList<>();
        for (RevCommit revCommit : revWalk) {

            if (StringUtils.isEmpty(commit.getCommitInfo())&&number != null && i < 50){
                i++;
                continue;
            }
            //默认50条，大于50条释放资源
            if (StringUtils.isEmpty(commit.getCommitInfo())&&number == null && i >= 50){
                revCommit.disposeBody();
                revWalk.close();
                list.sort(Comparator.comparing(CommitMessage::getDateTime).reversed());
                return list;
            }

         /*   TreeWalk treeWalk = new TreeWalk(repository);
            treeWalk.reset(revCommit.getTree());*/


            CommitMessage commitMessage;
            //根据提交用户查询
            if (StringUtils.isNotEmpty(commit.getCommitUser())){
                if (revCommit.getAuthorIdent().getName().equals(commit.getCommitUser())){
                    commitMessage = getCommitMessage(revCommit);
                    list.add(commitMessage);
                }
                continue;
            }

            //根据输入提交信息查询
            if (StringUtils.isNotEmpty(commit.getCommitInfo())){
                boolean contains = revCommit.getShortMessage().contains(commit.getCommitInfo());
                if (contains){
                    commitMessage = getCommitMessage(revCommit);
                    list.add(commitMessage);
                }
                continue;
            }

            commitMessage = getCommitMessage(revCommit);
            list.add(commitMessage);

            //treeWalk.close();
            revCommit.disposeBody();
            i++;
        }
        revWalk.close();

        list.sort(Comparator.comparing(CommitMessage::getDateTime).reversed());
        return list;
    }



    /**
     * 获取两个不同分支的差异提交
     * @param git 仓库
     * @return 提交记录
     * @throws IOException 仓库不存在
     * @throws ApplicationException 分支不存在
     */
    public static List<CommitMessage> findDiffBranchCommit(Git git ,String originBranch,  String targetBranch) throws IOException, GitAPIException {
        List<CommitMessage> diffList = new ArrayList<>();
        RevWalk revWalk = new RevWalk(git.getRepository());

        //源分支
        ObjectId originObjectId = git.getRepository().resolve(Constants.R_HEADS +originBranch);
        //目标分支
        ObjectId targetObjectId = git.getRepository().resolve(Constants.R_HEADS +targetBranch);

        RevCommit originCommit = revWalk.parseCommit(originObjectId);
        RevCommit targetCommit = revWalk.parseCommit(targetObjectId);


        //originCommit 比 targetCommit 多提交的commit
        Iterable<RevCommit> commits = git.log().addRange(targetCommit, originCommit).call();
        // 遍历差异提交并输出
        for (RevCommit revCommit : commits) {
            CommitMessage commitMessage = getCommitMessage(revCommit);
            diffList.add(commitMessage);
        }
        return diffList;

       /* //目标分支
        List<RevCommit> targetList = new ArrayList<>();
        RevWalk targetRevWalk = new RevWalk(git.getRepository());
        ObjectId targetObjectId = git.getRepository().resolve(Constants.R_HEADS +commit.getTargetBranch());
        targetRevWalk.markStart(targetRevWalk.parseCommit(targetObjectId));
        for (RevCommit revCommit:targetRevWalk){
            CommitMessage commitMessage = getCommitMessage(revCommit);
            diffList.add(commitMessage);
        }

        //源分支
        RevWalk originRevWalk = new RevWalk(git.getRepository());
        ObjectId originObjectId = git.getRepository().resolve(Constants.R_HEADS +commit.getBranch());
        originRevWalk.markStart(originRevWalk.parseCommit(originObjectId));
        for (RevCommit revCommit:originRevWalk){
            String commitId = revCommit.getId().getName();
           //
            List<RevCommit> collect = targetList.stream().filter(a -> a.getId().getName().equals(commitId)).collect(Collectors.toList());
            if (collect.isEmpty()){
                CommitMessage commitMessage = getCommitMessage(revCommit);
                diffList.add(commitMessage);
            }
        }*/


    }

    /**
     * 获取两个不同分支的差异的文件
     * @param git 仓库
     * @param commit 提交信息
     * @return 提交记录
     * @throws IOException 仓库不存在
     * @throws ApplicationException 分支不存在
     */
   public static FileDiffEntry findDiffBranchFile(Git git , Commit commit) throws IOException {

       Repository repository = git.getRepository();
       RevWalk revWalk = new RevWalk(git.getRepository());

       //目标分支
        ObjectId targetObjectId= git.getRepository().resolve(Constants.R_HEADS + commit.getTargetBranch());
        RevCommit targetCommit = revWalk.parseCommit(targetObjectId);

        //源分支
        ObjectId originObjectId = git.getRepository().resolve(Constants.R_HEADS + commit.getBranch());
        RevCommit originCommit = revWalk.parseCommit(originObjectId);

       FileDiffEntry diffEntry = getDiffFileEntry(repository,targetCommit,originCommit);

       return diffEntry;
    }


    /**
     * 获取两个不同分支的差异的文件的详情
     * @param git 仓库
     * @param commit 提交信息
     * @return 提交记录
     * @throws IOException 仓库不存在
     * @throws ApplicationException 分支不存在
     */
    public static List<CommitFileDiff>  findDiffBranchFileDetails(Git git , Commit commit) throws IOException{

        Repository repository = git.getRepository();
        RevWalk revWalk = new RevWalk(git.getRepository());

        //目标分支
        ObjectId targetObjectId= git.getRepository().resolve(Constants.R_HEADS+commit.getTargetBranch());
        RevCommit targetCommit = revWalk.parseCommit(targetObjectId);

        //源分支
        ObjectId originObjectId = git.getRepository().resolve(Constants.R_HEADS+commit.getBranch());
        RevCommit originCommit = revWalk.parseCommit(originObjectId);

        //旧树对象
        CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
        oldTreeIter.reset(repository.newObjectReader(), targetCommit.getTree());

        //新树对象
        CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
        newTreeIter.reset(repository.newObjectReader(), originCommit.getTree());

        return getFileDetailsDiff(repository,oldTreeIter,newTreeIter,commit.getFilePath());
    }




    /**
     * 通过commitIdList 查询信息
     * @param git git
     * @param commitIdList 提交的commitId集合
     */
    public static List<CommitMessage>   findMessByCommitIdList(Git git,List<String> commitIdList) throws IOException {
        List<CommitMessage> arrayList = new ArrayList<>();
        Repository repository = git.getRepository();
        for (String commitId:commitIdList){
            ObjectId objId = repository.resolve(commitId);
            RevWalk revWalk = new RevWalk(repository);
            RevCommit commit = revWalk.parseCommit(objId);
            revWalk.dispose();

            CommitMessage commitMessage = getCommitMessage(commit);

            arrayList.add(commitMessage);
        }
        return arrayList;
    }


    /**
     * 查询最新提交
     * @param repository 仓库
     * @param refCode 分支、commitId
     * @param refCodeType  类型  commitId、branch
     * @return 提交记录
     * @throws IOException 仓库不存在
     * @throws ApplicationException 分支不存在
     */
    public static CommitMessage findNewestCommit(Repository repository, String refCode,String refCodeType)
            throws IOException , ApplicationException {


        ObjectId refObjectId = getRefObjectId(repository, refCode, refCodeType);
        if (ObjectUtils.isEmpty(refObjectId)){
            return null;
        }

        List<CommitMessage> arrayList = new ArrayList<>();
        //创建提交遍历对象
        RevWalk revWalk = new RevWalk(repository);
        revWalk.sort(RevSort.COMMIT_TIME_DESC);
        revWalk.markStart(revWalk.parseCommit(refObjectId));
        for (RevCommit revCommit : revWalk) {
            CommitMessage commitMessage = getCommitMessage(revCommit);
            arrayList.add(commitMessage);
            revCommit.disposeBody();
        }
        arrayList.sort(Comparator.comparing(CommitMessage::getDateTime).reversed());
        revWalk.close();
        return arrayList.get(0);
    }

    /**
     * 查询仓库分支的第一次提交信息
     * @param repository 仓库
     * @param branch 分支、commitId
     * @param type  类型  commitId、branch
     * @return 提交记录
     * @throws IOException 仓库不存在
     * @throws ApplicationException 分支不存在
     */
    public static CommitMessage findFistCommit(Repository repository, String branch,String type)
            throws IOException , ApplicationException {

        ObjectId objectId;
        if (("commitId").equals(type)){
            //提交commitId
            objectId = ObjectId.fromString(branch);
        }else {
            //分支
            objectId=  repository.findRef(Constants.R_HEADS +branch).getObjectId();
        }

        //创建提交遍历对象
        RevWalk revWalk = new RevWalk(repository);
        revWalk.sort(RevSort.COMMIT_TIME_DESC);
        revWalk.sort(RevSort.REVERSE);
        revWalk.markStart(revWalk.parseCommit(objectId));
        for (RevCommit revCommit : revWalk) {
            CommitMessage commitMessage = getCommitMessage(revCommit);
            revCommit.disposeBody();
            revWalk.close();
            return commitMessage;
        }
        return null;
    }


    /**
     * 查询当前commitId和父级的commitId 差异文件
     * @param git git
     * @param commitId 提交信息
     * @return 文件对比信息
     * @throws IOException 扫描失败
     */
    public static FileDiffEntry findDiffFileByCommitId(Git git,  String commitId) throws IOException {
        Repository repository = git.getRepository();
        RevWalk walk = new RevWalk(git.getRepository());
        //当前commitId查询提交树
        RevCommit newCommit =  walk.parseCommit(ObjectId.fromString(commitId));
        RevCommit oldCommit=null;

        // 获取父提交ID 当前提交为合并分支时存在两个父级提交
        RevCommit[] parents = newCommit.getParents();
        List<String> parentCommitIds = new ArrayList<>();
        if (!ObjectUtils.isEmpty(parents)){
            for (RevCommit parent : parents) {
                parentCommitIds.add(parent.getId().getName());
            }
        }

        if (parents.length!=0){
            //旧的提交树
            String oldCommitId = parentCommitIds.get(0);
            ObjectId oldObjectId = ObjectId.fromString(oldCommitId);
            oldCommit =  walk.parseCommit(oldObjectId);
        }

        //获取不同树的提交
        FileDiffEntry fileDiffEntry = getDiffFileEntry(repository, oldCommit, newCommit);

        //添加不同树
        fileDiffEntry.setParentCommitIds(parentCommitIds);

        //查询提交的信息
        CommitMessage branchCommit = GitCommitUntil.findNewestCommit(repository,newCommit.getId().getName(),"commitId");
        if (branchCommit != null){
            fileDiffEntry.setCommitMessage(branchCommit.getCommitMessage());
            fileDiffEntry.setCommitTime(branchCommit.getCommitTime());
            fileDiffEntry.setCommitUser(branchCommit.getCommitUser());
        }

        return fileDiffEntry;
    }


    /**
     * 对比两棵树中具体文件的提交
     * @param repo 仓库信息
     * @param newCommit 新树
     * @param oldCommit 旧树
     * @param filePath  文件地址
     * @return 文件对比信息
     * @throws IOException 扫描失败
     */
    public static  String[] findFileChangedList(Repository repo, RevCommit newCommit, RevCommit oldCommit, String filePath)
            throws IOException {

        // 获取文件的详细差异
        ObjectReader reader = repo.newObjectReader();
        CanonicalTreeParser oldTreeIter = null;
        if (oldCommit != null){
            oldTreeIter = new CanonicalTreeParser();
            oldTreeIter.reset(reader, oldCommit.getTree());
        }

        CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
        newTreeIter.reset(reader, newCommit.getTree());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DiffFormatter diffFormatter = new DiffFormatter(out);
        diffFormatter.setRepository(repo);
        diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
        diffFormatter.setDetectRenames(true);
        //过滤指定文件
        if (StringUtils.isNotEmpty(filePath)){
            PathFilter pathFilter = PathFilter.create(filePath);
            diffFormatter.setPathFilter(pathFilter);
        }
        List<DiffEntry> diffs;
        if (oldTreeIter != null){
            diffs =  diffFormatter.scan(oldTreeIter, newTreeIter);
        }else {
            diffs =  diffFormatter.scan(oldCommit, newCommit);
        }
        String[] diffLines = new String[0];
        for (DiffEntry diff : diffs) {
            diffFormatter.format(diff);
            String diffText = out.toString();
             diffLines = diffText.split("\n");
        }

        return diffLines;
    }



    /**
     *  查询具体文件两次提交的差异
     * @param repository 仓库信息
     * @param commitId 当前的commitId
     * @param originCommitId 源commitId
     * @param filePath  文件地址
     * @return 文件对比信息
     */
    public static List<CommitFileDiff> findDiffCommitFileDetails(Repository repository,String commitId,
                                                                 String originCommitId,String filePath) throws IOException {

        //获取可遍历的仓库对象
        RevWalk walk = new RevWalk(repository);

        RevCommit revCommit =  walk.parseCommit(ObjectId.fromString(commitId));
        RevCommit targetRevCommit =  walk.parseCommit(ObjectId.fromString(originCommitId));

        //旧树对象
        CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
        oldTreeIter.reset(repository.newObjectReader(), targetRevCommit.getTree());

        //新树对象
        CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
        newTreeIter.reset(repository.newObjectReader(), revCommit.getTree());

       return getFileDetailsDiff(repository,oldTreeIter,newTreeIter,filePath);
    }

    /**
     * 获取上一次提交信息
     * @param commit 提交信息
     * @param repo 仓库
     * @return 上一次提交
     * @throws IOException 仓库不存在
     */
    public static RevCommit findPrevHash(RevCommit commit, Repository repo)
            throws  IOException {
        RevWalk walk = new RevWalk(repo);
        walk.markStart(commit);
        int count = 0;
        for (RevCommit rev : walk) {
            if (count == 1) {
                return rev;
            }
            count++;
        }
        walk.dispose();
        return null;
    }

    /**
     * 获取单个文件的 最后的提交历史
     * @param git git实例
     * @param file 文件名称
     * @return 提交历史
     * @throws GitAPIException 信息获取失败
     */
    public static Map<String,String> gitFileCommitLog(Git git,String commitId,String file)
            throws GitAPIException, IOException {
        List<Map<String,String>> list = new ArrayList<>();

        ObjectId objectId = ObjectId.fromString(commitId);


        RevCommit revCommits = git.getRepository().parseCommit(objectId);

        Iterable<RevCommit> log = git.log()
                .add(revCommits)
                .addPath(file)
                .call();

        Map<String,String> map = new HashMap<>();
        for (RevCommit revCommit : log) {
            Date date = revCommit.getAuthorIdent().getWhen();
            String message = revCommit.getShortMessage();

            map.put("message",message);//转换时间
            map.put("time", RepositoryUtil.time(date,"commit")+"前");
            map.put("date", String.valueOf(date.getTime()));
            break;
        }
        return map;
    }

    /**
     * 获取CommitMessage对象
     * @param revCommit revCommit
     */
    public static CommitMessage getCommitMessage(RevCommit revCommit ){
        Date date = revCommit.getAuthorIdent().getWhen();//时间
        String name = revCommit.getAuthorIdent().getName();//提交人


        CommitMessage commitMessage = new CommitMessage();
        commitMessage.setCommitId(revCommit.getId().getName());//commitId
        commitMessage.setCommitMessage(revCommit.getShortMessage());//提交信息
        commitMessage.setCommitUser(name);
        commitMessage.setDateTime(date);
        commitMessage.setCommitTime(RepositoryUtil.time(date,"commit")+"前");//转换时间

        return commitMessage;
    }



    /**
     * 获取文件详情的不同
     * @param repository repository
     */
    public static  List<CommitFileDiff> getFileDetailsDiff(Repository repository,CanonicalTreeParser oldTreeIter,
                                          CanonicalTreeParser newTreeIter,String filePath) throws IOException {
        //创建两次提交的差异对象
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DiffFormatter diffFormatter = new DiffFormatter(out);
        diffFormatter.setRepository(repository);
        diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
        diffFormatter.setDetectRenames(true);

        //过滤其他文件只匹配filePath文件
        if(!ObjectUtils.isEmpty(filePath)){
            PathFilter pathFilter = PathFilter.create(filePath);
            diffFormatter.setPathFilter(pathFilter);
        }
        List<DiffEntry> diffs =  diffFormatter.scan(oldTreeIter, newTreeIter);
        List<CommitFileDiff> list = new ArrayList<>();
        int left = 0;int right = 0;int number = 0;
        int oldStart = 0;int oldLines = 0;int newStart = 0;int newLines = 0;

        for (DiffEntry diff : diffs) {
            diffFormatter.format(diff);
            String diffText = out.toString();
            String[] diffLines = diffText.split("\n");
            for (int i = 3; i < diffLines.length; i++) {
                String line = diffLines[i];

                if (line.startsWith("+++") || line.startsWith("---")){
                    continue;
                }
                //将文件末尾没有换行 说明去掉
                if (line.equals("\\ No newline at end of file")){
                    continue;
                }
                CommitFileDiff fileDiff = new CommitFileDiff();
                if (line.startsWith("@@") && line.endsWith("@@")){
                    Pattern pattern = Pattern.compile(RepositoryFinal.DIFF_REGEX);
                    Matcher matcher = pattern.matcher(line);
                    //解析文件变更信息
                    if (matcher.matches()){
                        oldStart = Integer.parseInt(matcher.group(1));
                        oldLines = matcher.group(2) != null ? Integer.parseInt(matcher.group(2)) : 0;
                        newStart = Integer.parseInt(matcher.group(3));
                        newLines = matcher.group(4) != null ? Integer.parseInt(matcher.group(4)) : 0;
                    }
                    fileDiff.setType(RepositoryFinal.DIFF_TYPE);
                    left = 0;right = 0;number = 0;
                    fileDiff.setText(line);
                }
                //文件行号
                fileDiff.setRight(newStart + number + right);
                fileDiff.setLeft(oldStart + number + left);

                //文件添加内容
                boolean addWith = line.startsWith(RepositoryFinal.DIFF_TYPE_ADD);
                if (addWith){
                    right++;
                    fileDiff.setType(RepositoryFinal.DIFF_TYPE_ADD);
                    fileDiff.setText(line.substring(1));
                }
                //文件删除内容
                boolean deleteWith = line.startsWith(RepositoryFinal.DIFF_TYPE_DELETE);
                if (deleteWith){
                    left++;
                    fileDiff.setType(RepositoryFinal.DIFF_TYPE_DELETE);
                    fileDiff.setText(line.substring(1));
                }
                //未改变内容
                boolean b = !line.startsWith("@@") || !line.endsWith("@@");
                if(b && !addWith && !deleteWith ) {
                    number++;
                    fileDiff.setType(RepositoryFinal.DIFF_TYPE_TEXT);
                    fileDiff.setText(line);
                }
                fileDiff.setIndex(i-4);
                list.add(fileDiff);
            }
        }
        return list;
    }



    /**
     * 对比两棵树中具体文件的提交
     * @param repo 仓库信息
     * @param newCommit 新树
     * @param oldCommit 旧树
     * @param filePath  文件地址
     * @return 文件对比信息
     * @throws IOException 扫描失败
     */
    public static List<CommitFileDiff> findFileChanged(Repository repo, RevCommit newCommit, RevCommit oldCommit, String filePath)
            throws IOException {

        // 获取文件的详细差异
        ObjectReader reader = repo.newObjectReader();
        CanonicalTreeParser oldTreeIter = null;
        if (oldCommit != null){
            oldTreeIter = new CanonicalTreeParser();
            oldTreeIter.reset(reader, oldCommit.getTree());
        }

        CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
        newTreeIter.reset(reader, newCommit.getTree());

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DiffFormatter diffFormatter = new DiffFormatter(out);
        diffFormatter.setRepository(repo);
        diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
        diffFormatter.setDetectRenames(true);
        //过滤指定文件
        PathFilter pathFilter = PathFilter.create(filePath);
        diffFormatter.setPathFilter(pathFilter);
        List<DiffEntry> diffs;
        if (oldTreeIter != null){
            diffs =  diffFormatter.scan(oldTreeIter, newTreeIter);
        }else {
            diffs =  diffFormatter.scan(oldCommit, newCommit);
        }

        List<CommitFileDiff> list = new ArrayList<>();

        int left = 0;int right = 0;int number = 0;
        int oldStart = 0;int oldLines = 0;int newStart = 0;int newLines = 0;

        for (DiffEntry diff : diffs) {
            diffFormatter.format(diff);
            String diffText = out.toString();
            int length = diffText.length();
            // if (filePath.endsWith(".js") && length > 20000){
            //     CommitFileDiff fileDiff = new CommitFileDiff();
            //     fileDiff.setRight(1);
            //     fileDiff.setLeft(1);
            //     fileDiff.setType(RepositoryFinal.DIFF_TYPE_BIG);
            //     list.add(fileDiff);
            //     return list;
            // }
            String[] diffLines = diffText.split("\n");
            for (int i = 3; i < diffLines.length; i++) {
                String line = diffLines[i];

                if (line.startsWith("+++") || line.startsWith("---")){
                    continue;
                }
                CommitFileDiff fileDiff = new CommitFileDiff();
                if (line.startsWith("@@") && line.endsWith("@@")){
                    Pattern pattern = Pattern.compile(RepositoryFinal.DIFF_REGEX);
                    Matcher matcher = pattern.matcher(line);
                    //解析文件变更信息
                    if (matcher.matches()){
                        oldStart = Integer.parseInt(matcher.group(1));
                        oldLines = matcher.group(2) != null ? Integer.parseInt(matcher.group(2)) : 0;
                        newStart = Integer.parseInt(matcher.group(3));
                        newLines = matcher.group(4) != null ? Integer.parseInt(matcher.group(4)) : 0;
                    }
                    fileDiff.setType(RepositoryFinal.DIFF_TYPE);
                    left = 0;right = 0;number = 0;
                    fileDiff.setText(line);
                }
                //文件行号
                fileDiff.setRight(newStart + number + right);
                fileDiff.setLeft(oldStart + number + left);

                //文件添加内容
                boolean addWith = line.startsWith(RepositoryFinal.DIFF_TYPE_ADD);
                if (addWith){
                    right++;
                    fileDiff.setType(RepositoryFinal.DIFF_TYPE_ADD);
                    fileDiff.setText(line.substring(1));
                }
                //文件删除内容
                boolean deleteWith = line.startsWith(RepositoryFinal.DIFF_TYPE_DELETE);
                if (deleteWith){
                    left++;
                    fileDiff.setType(RepositoryFinal.DIFF_TYPE_DELETE);
                    fileDiff.setText(line.substring(1));
                }
                //未改变内容
                boolean b = !line.startsWith("@@") || !line.endsWith("@@");
                if(b && !addWith && !deleteWith ) {
                    number++;
                    fileDiff.setType(RepositoryFinal.DIFF_TYPE_TEXT);
                    fileDiff.setText(line);
                }

                fileDiff.setIndex(i-4);
                list.add(fileDiff);
            }
        }

        return list;
    }


    /**
     * 统计不同分支、提交的差异数据
     * @param repositoryAddress repositoryAddress
     * @param commit commit
     */
    public static CommitDiffData getDiffStatistics(String repositoryAddress, Commit commit) throws IOException, GitAPIException {
        Git git = Git.open(new File(repositoryAddress));

        CommitDiffData commitDiffData = new CommitDiffData();
        Repository repository = git.getRepository();
        RevWalk revWalk = new RevWalk(git.getRepository());

        ObjectId targetObjectId=null;
        ObjectId originObjectId=null;

        //（这里先定义分支的、commitID后续迭代）
        if (!commit.isFindCommitId()){
            originObjectId = git.getRepository().resolve(Constants.R_HEADS + commit.getBranch());
            targetObjectId= git.getRepository().resolve(Constants.R_HEADS + commit.getTargetBranch());
        }

        //目标分支
        RevCommit targetCommit = revWalk.parseCommit(targetObjectId);
        //源分支
        RevCommit originCommit = revWalk.parseCommit(originObjectId);


        //查询两个提交文件是否有不同的提交和提交文件
        Map<String,List<String>>  targetDiffBranchFile = getDiffBranchCommitClash(git, targetCommit, originCommit);
        //如果两个分支没有不同的提交文件 表示没有差异直接返回
        if (ObjectUtils.isEmpty(targetDiffBranchFile)){
            commitDiffData.setClash(0);
            commitDiffData.setCommitNum(0);
            commitDiffData.setFileNum(0);
            return commitDiffData;
        }
        List<String> targetFile = targetDiffBranchFile.get("filePath");
        List<String> commitId = targetDiffBranchFile.get("diffCommitId");


        boolean isBranchDiffsFile = GitBranchUntil.findIsBranchDiffsFile(repositoryAddress, commit.getBranch(), commit.getTargetBranch());
        int clash = isBranchDiffsFile ? 1 : 0;
        commitDiffData.setClash(clash);
    /*    Map<String,List<String>>  originDiffBranchFile = getDiffBranchCommitClash(git, originCommit, targetCommit);
        if (!ObjectUtils.isEmpty(originDiffBranchFile)){
            List<String> originFile = originDiffBranchFile.get("filePath");
            //判断是否有相同的文件路径
            boolean hasSameObject = targetFile.stream().anyMatch(originFile::contains);
            int clash = hasSameObject ? 1 : 0;
            commitDiffData.setClash(clash);
        }else {
            commitDiffData.setClash(0);
        }*/

        int commitNum = CollectionUtils.isNotEmpty(commitId) ? commitId.size() : 0;
        commitDiffData.setCommitNum(commitNum);

        //源分支树
        ObjectReader reader = repository.newObjectReader();
        CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
        oldTreeIter.reset(reader, originCommit.getTree());

        //目标分支树
        CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
        newTreeIter.reset(reader, targetCommit.getTree());
        DiffFormatter diffFormatter = new DiffFormatter(new ByteArrayOutputStream());
        diffFormatter.setRepository(repository);
        diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
        diffFormatter.setDetectRenames(true);
        List<DiffEntry> diffEntries = diffFormatter.scan(oldTreeIter, newTreeIter);
        int fileNum = CollectionUtils.isNotEmpty(diffEntries) ? diffEntries.size() : 0;
        commitDiffData.setFileNum(fileNum);
        return  commitDiffData;
    }

    /**
     * 获取两个树不同的文件
     * @param repository 仓库信息
     * @param newCommit 新树
     * @param oldCommit 旧树
     */
    public static FileDiffEntry getDiffFileEntry(Repository repository,RevCommit oldCommit,RevCommit newCommit) throws IOException {
        FileDiffEntry fileDiffEntry = new FileDiffEntry();
        List<CommitFileDiffList> list = new ArrayList<>();

        //获取两颗树的差异文对象
        List<DiffEntry> diffEntries = getDiffEntry(repository, oldCommit, newCommit);
        int allAddLine=0;     //总的增加行数
        int allDeleteLine=0;  //总的减少行数

        if (CollectionUtils.isNotEmpty(diffEntries)){
           // diffEntries.stream().filter(a->a.get)
            for (DiffEntry diffEntry : diffEntries) {

                CommitFileDiffList commitFileDiffList = new CommitFileDiffList();

                /*
                 * ADD: 表示文件是新增的，即在一个分支中添加了一个新文件。
                 * DELETE: 表示文件被删除，即在一个分支中删除了一个已存在的文件。
                 * MODIFY: 表示文件被修改，即在一个分支中对一个已存在的文件进行了修改。
                 * RENAME: 表示文件被重命名，即在一个分支中对一个已存在的文件进行了重命名操作。
                 * COPY: 表示文件被复制，即在一个分支中对一个已存在的文件进行了复制操作
                 * */
                String name = diffEntry.getChangeType().name();


                String oldPath = diffEntry.getOldPath();
                String newPath = diffEntry.getNewPath();
                FileMode oldMode = diffEntry.getOldMode();
                FileMode newMode = diffEntry.getNewMode();

                commitFileDiffList.setType(name);
                commitFileDiffList.setOldFilePath(oldPath);
                commitFileDiffList.setOldFileType(oldMode.toString());
                commitFileDiffList.setNewFilePath(newPath);
                commitFileDiffList.setNewFileType(newMode.toString());


                //因为类型为删除的时候，新文件路径为/dev/null，这里直接添加旧路径
                if (("DELETE").equals(name)){
                    commitFileDiffList.setNewFilePath(oldPath);
                }

                //如果是新增，那oldFilePath就为/dev/null;删除文件 newFilePath就为/dev/null
                String newFilePath = commitFileDiffList.getNewFilePath();

                if (!newFilePath.contains("/")){
                    commitFileDiffList.setFileName(newFilePath);
                }else {
                    String before = StringUtils.substringBeforeLast(newFilePath, "/");
                    commitFileDiffList.setFolderPath(before);
                    String filName = StringUtils.substringAfterLast(newFilePath, "/");
                    commitFileDiffList.setFileName(filName);
                }

                String path ;
                //通过文件的后缀获取类型 java、js...
                if (!newPath.equals(RepositoryFinal.FILE_PATH_NULL)){
                    path = newPath;
                    commitFileDiffList.setFileType(StringUtils.substringAfterLast(newPath,"/"));
                }else {
                    path = oldPath;
                    commitFileDiffList.setFileType(StringUtils.substringAfterLast(oldPath,"/"));
                }



                //获取文件差异详情
                int deleteLine=0;
                int addLine=0;
                String[]  diffLines = findFileChangedList(repository,newCommit,oldCommit,path);
                for (String line : diffLines) {
                    if (line.startsWith("+++") || line.startsWith("---")){
                        continue;
                    }
                    if (line.startsWith("-")  ){
                        deleteLine+=1;
                    }
                    if (line.startsWith("+")  ){
                        addLine+=1;
                    }
                }
                //获取总的变动行数
                allAddLine+=addLine;
                allDeleteLine+=deleteLine;

                commitFileDiffList.setAddLine(addLine);
                commitFileDiffList.setDeleteLine(deleteLine);
                list.add(commitFileDiffList);
            }
            //添加不同提交的差异文件
            fileDiffEntry.setDiffList(list);

            //总修改文件的增加行、减少行
            fileDiffEntry.setDeleteLine(allDeleteLine);
            fileDiffEntry.setAddLine(allAddLine);
        }
        return fileDiffEntry;
    }

    /**
     * 查询不同分支的commit的提交文件
     * @param git git
     */
    public  static Map<String,List<String>>  getDiffBranchCommitClash(Git git,RevCommit targetCommit,RevCommit originCommit) throws GitAPIException, IOException {
        HashMap<String, List<String>> resultMap = new HashMap<>();
        RevWalk revWalk = new RevWalk(git.getRepository());

        //源分支相对于目标分支的差异提交
        List<String> originCommitIdList = new ArrayList<>();
        Iterable<RevCommit> commits = git.log().addRange(targetCommit, originCommit).call();
        for (RevCommit revCommit : commits) {
            String commitId = revCommit.getId().getName();
            originCommitIdList.add(commitId);
        }
        if (CollectionUtils.isEmpty(originCommitIdList)){
            return null;
        }
        //第一个不同提交的commitId
        String fistDiffCommitId = originCommitIdList.get(originCommitIdList.size() - 1);
        // 获取提交对象
        ObjectId objectId = git.getRepository().resolve(fistDiffCommitId);
        RevCommit commit = revWalk.parseCommit(objectId);
        RevCommit[] parents = commit.getParents();
        RevCommit parentCommit=null;
        if (!ObjectUtils.isEmpty(parents)){
            //父级的树
            parentCommit = revWalk.parseCommit(parents[0]);
        }

        //最后一次提交
        String lastDiffCommitId = originCommitIdList.get(0);
        ObjectId lastObjectId = git.getRepository().resolve(lastDiffCommitId);
        RevCommit lastCommit = revWalk.parseCommit(lastObjectId);
        //获取不同树的提交
        List<DiffEntry> diffEntry = getDiffEntry(git.getRepository(), parentCommit, lastCommit);
        List<String> filePath = diffEntry.stream().map(DiffEntry::getNewPath).collect(Collectors.toList());
        resultMap.put("diffCommitId",originCommitIdList);
        resultMap.put("filePath",filePath);
        return resultMap;
    }

    /**
     * 获取两个提交树的差异文件对象
     * @param  repository
     * @param oldCommit 旧提交树
     * @param newCommit 新提交树
     */
    public static List<DiffEntry> getDiffEntry(Repository repository,RevCommit oldCommit, RevCommit newCommit) throws IOException {
        // 获取文件的详细差异
        ObjectReader reader = repository.newObjectReader();
        CanonicalTreeParser oldTreeIter = null;
        if (oldCommit != null){
            oldTreeIter = new CanonicalTreeParser();
            oldTreeIter.reset(reader, oldCommit.getTree());
        }

        CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
        newTreeIter.reset(reader, newCommit.getTree());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DiffFormatter diffFormatter = new DiffFormatter(out);
        diffFormatter.setRepository(repository);
        diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
        diffFormatter.setDetectRenames(true);
        List<DiffEntry> diffEntries;
        if (oldTreeIter != null){
            diffEntries = diffFormatter.scan(oldTreeIter, newTreeIter);
        }else {
            diffEntries =  diffFormatter.scan(oldCommit, newCommit);
        }
        return diffEntries;
    }



    /**
     * 获取两个分支的滞后或者领先提交数量
     * @param  repository
     */
    public static Map<String, Integer> getBranchCommitNum(Repository repository,String branch) throws IOException {
        Map<String, Integer> resultMap = new HashMap<>();

        // 获取默认分支
        Ref defaultRef = repository.getRefDatabase().getRef("HEAD");
        // 获取默认分支的最新提交
        ObjectId defaultHead = defaultRef.getObjectId();
        RevCommit defaultCommit = repository.parseCommit(defaultHead);


        // 获取当前分支的最新提交
        Ref currentRef = repository.findRef( branch);
        ObjectId  currentHead = currentRef.getObjectId();
        RevCommit currentCommit = repository.parseCommit(currentHead);

        //实例遍历提交历史对象
        RevWalk revWalk = new RevWalk(repository);
        RevCommit commit1 = revWalk.parseCommit(defaultCommit);
        RevCommit commit2 = revWalk.parseCommit(currentCommit);
        //获取两个分支公共提交commitId
        revWalk.setRevFilter(RevFilter.MERGE_BASE);
        revWalk.markStart(commit1);
        revWalk.markStart(commit2);
        RevCommit mergeBase = revWalk.next();
        String mergeBaseCommitId = mergeBase.getId().getName();

        RevWalk revWalk1 = new RevWalk(repository);
        ObjectId targetCommit = repository.resolve(mergeBaseCommitId);
        revWalk1.markUninteresting(revWalk1.parseCommit(targetCommit));

        //当前分支相对与默认分支的滞后提交数
        revWalk1.markStart(revWalk1.parseCommit(defaultHead));
        int lagNum = 0;
        for (RevCommit commit : revWalk1) {
            lagNum++;
        }

        //当前分支相对与默认分支的超前提交数
        revWalk1.markStart(revWalk1.parseCommit(currentCommit));
        int advanceNum = 0;
        for (RevCommit commit : revWalk1) {
            advanceNum++;
        }

        resultMap.put("lagNum", lagNum);
        resultMap.put("advanceNum",advanceNum);
        return resultMap;
    }

    /**
     * 获取仓库提交的用户
     * @param  repositoryAddress 仓库地址
     */
    public static List getCommitUserList(String repositoryAddress) throws IOException, GitAPIException {
        List<String> arrayList = new ArrayList<>();
        Git git = Git.open(new File(repositoryAddress));
        LogCommand log = git.log();
        // 检查是否有可用的分支
        List<Ref> head = git.getRepository().getRefDatabase().getRefs();
        if (CollectionUtils.isEmpty(head) ) {
            return null;
        }
        Iterable<RevCommit> commits = git.log().call();
        for (RevCommit commit : commits) {
            PersonIdent authorIdent = commit.getAuthorIdent();
            arrayList.add(commit.getAuthorIdent().getName());
        }
        List<String> stringList = arrayList.stream().distinct().collect(Collectors.toList());
        return stringList;
    }


    /**
     * 查询仓库所有分支最近 提交数量
     * @param repositoryAddress 仓库地址
     * @param  number 查询条数
     */
    public static List<CommitMessage> getLatelyCommit(String repositoryAddress,int number) throws IOException, GitAPIException {
        List<CommitMessage> list = new ArrayList<>();

        Git git = Git.open(new File(repositoryAddress));
        Repository repository = git.getRepository();
        RevWalk revWalk = new RevWalk(repository);

        //所有分支
        List<Ref> refs = git.branchList().call();
        for (Ref ref : refs) {
            String name = ref.getName();
            revWalk.markStart(revWalk.parseCommit(ref.getObjectId()));
        }

        int findNum=0;
        //所有分支的提交  重复的提交只会算一个
        for (RevCommit commit : revWalk) {
            if (findNum>number){
                break;
            }
            CommitMessage commitMessage = getCommitMessage(commit);
            list.add(commitMessage);
            findNum+=1;
        }
        return list;
    }

    /**
     * 获取refObjectId
     * @param repository 裸仓库
     * @param  refCode refCode 分支、标签名字或者commitId
     * @param  refCodeType 类型  branch、tag、commit
     */
    public static ObjectId getRefObjectId(Repository repository,String refCode,String refCodeType) throws IOException {
        ObjectId objectId;
        if (("branch").equals(refCodeType)){
            Ref ref = repository.findRef(Constants.R_HEADS + refCode);
            if (ObjectUtils.isEmpty(ref)){
                return null;
            }
            //分支
            objectId=  repository.findRef(Constants.R_HEADS +refCode).getObjectId();
        }else if (("tag").equals(refCodeType)){
            Ref ref = repository.findRef(Constants.R_TAGS + refCode);
            if (ObjectUtils.isEmpty(ref)){
                return null;
            }
            objectId=  repository.findRef(Constants.R_TAGS +refCode).getObjectId();
        }else {
            //提交commitId
            objectId = ObjectId.fromString(refCode);
        }
        return objectId;
    }

}
