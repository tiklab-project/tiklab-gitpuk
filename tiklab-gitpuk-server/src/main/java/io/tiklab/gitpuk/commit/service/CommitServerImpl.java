package io.tiklab.gitpuk.commit.service;


import io.tiklab.core.exception.SystemException;
import io.tiklab.gitpuk.commit.model.*;
import io.tiklab.gitpuk.common.GitPukYamlDataMaService;
import io.tiklab.gitpuk.common.RepositoryFileUtil;
import io.tiklab.gitpuk.common.RepositoryFinal;
import io.tiklab.gitpuk.common.RepositoryUtil;
import io.tiklab.gitpuk.common.git.GitCommitUntil;
import io.tiklab.gitpuk.file.model.FileMessage;
import io.tiklab.core.exception.ApplicationException;
import io.tiklab.gitpuk.merge.model.MergeCommit;
import io.tiklab.gitpuk.merge.model.MergeCommitQuery;
import io.tiklab.gitpuk.merge.service.MergeCommitService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CommitServerImpl implements CommitServer {
    private static Logger logger = LoggerFactory.getLogger(CommitServerImpl.class);

    @Autowired
    private GitPukYamlDataMaService yamlDataMaService;

    @Autowired
    MergeCommitService mergeCommitService;


    @Override
    public List<CommitMessage> findBranchCommit(Commit commit) {
        String rpyId = commit.getRpyId();
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),rpyId);
        List<CommitMessage> branchCommit;
        try {
            Git git = Git.open(new File(repositoryAddress));
            org.eclipse.jgit.lib.Repository repository = git.getRepository();
            branchCommit = GitCommitUntil.findBranchCommit(repository,commit);

            git.close();
        } catch (IOException e) {
            throw new ApplicationException("提交记录获取失败："+e);
        }
        if (branchCommit.isEmpty()){
           return Collections.emptyList();
        }

        return groupCommit(branchCommit, new ArrayList<>());
    }




    @Override
    public List<CommitMessage> findCommitDiffBranch(Commit commit) {
        String rpyId = commit.getRpyId();
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),rpyId);
        List<CommitMessage> branchCommit;
        try {
            Git git = Git.open(new File(repositoryAddress));

            //查询不同分支提交的差异
           branchCommit = GitCommitUntil.findDiffBranchCommit(git,commit.getBranch(),commit.getTargetBranch());
            git.close();
        } catch (Exception e) {
            throw new ApplicationException("提交记录获取失败："+e);
        }
        if (branchCommit.isEmpty()){
            return Collections.emptyList();
        }
        return groupCommit(branchCommit, new ArrayList<>());
    }



    @Override
    public List<CommitFileDiff> findDiffBranchFileDetails(Commit commit) {
        String rpyId = commit.getRpyId();
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),rpyId);
        List<CommitFileDiff> diffBranchFileDetails;
        try {
            Git git = Git.open(new File(repositoryAddress));

            //查询不同分支提交的差异
            diffBranchFileDetails = GitCommitUntil.findDiffBranchFileDetails(git, commit);
            git.close();
        } catch (Exception e) {
            throw new ApplicationException("提交记录获取失败："+e);
        }
        return diffBranchFileDetails;
    }

    @Override
    public CommitDiffData findStatisticsByBranchs(Commit commit) {
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),commit.getRpyId());
        try {
            CommitDiffData commitDiffData = GitCommitUntil.getDiffStatistics(repositoryAddress, commit);
            return  commitDiffData;
        }catch (Exception e){
            throw new ApplicationException(e);
        }
    }

    @Override
    public List<CommitMessage> findDiffCommitByMergeId(String mergeId) {
        List<MergeCommit> mergeCommitList = mergeCommitService.findMergeCommitList(new MergeCommitQuery().setMergeRequestId(mergeId));
        if (CollectionUtils.isNotEmpty(mergeCommitList)){
            MergeCommit mergeCommit = mergeCommitList.get(0);
            List<String> commitIdList = mergeCommitList.stream().map(MergeCommit::getCommitId).collect(Collectors.toList());

            try {
                String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),mergeCommit.getRepositoryId());
                Git git = Git.open(new File(repositoryAddress));
                //通过commitIdList 查询信息
                List<CommitMessage> commitMessageList = GitCommitUntil.findMessByCommitIdList(git, commitIdList);

                //分组
                Map<String, List<CommitMessage>> listMap = commitMessageList.stream().collect(Collectors.groupingBy(a -> RepositoryUtil.date(2, a.getDateTime())));
                List<CommitMessage> commitMessages = new ArrayList<>();
                CommitMessage commitMessage = new CommitMessage();
                for (String key:listMap.keySet()){
                    commitMessage.setCommitTime(key);
                    commitMessage.setCommitMessageList(listMap.get(key));
                    commitMessages.add(commitMessage);
                }
                return commitMessages;
            }catch (Exception e){
                throw new ApplicationException("合并请求的差异提交获取失败："+e);
            }
        }
        return null;
    }

    @Override
    public FileDiffEntry findDiffFileByMergeId(String mergeId) {
        List<MergeCommit> mergeCommitList = mergeCommitService.findMergeCommitList(new MergeCommitQuery().setMergeRequestId(mergeId));
        if (CollectionUtils.isNotEmpty(mergeCommitList)) {
            List<MergeCommit> mergeCommits = mergeCommitList.stream().sorted(Comparator.comparing(MergeCommit::getCommitTime)).collect(Collectors.toList());
            try {
                MergeCommit mergeCommit = mergeCommitList.get(0);
                String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),mergeCommit.getRepositoryId());
                Git git = Git.open(new File(repositoryAddress));

                Map<String, RevCommit> revCommit = getRevCommit(git,mergeCommits);
                RevCommit oldCommit = revCommit.get("oldRevCommit");
                RevCommit newCommit = revCommit.get("newRevCommit");
                FileDiffEntry diffFileEntry = GitCommitUntil.getDiffFileEntry(git.getRepository(), oldCommit, newCommit);
                return diffFileEntry;
            }catch (Exception e){
                throw new ApplicationException("合并请求的差异文件获取失败："+e);
            }
        }
            return null;
    }

    @Override
    public CommitDiffData findStatisticsByMergeId(String mergeId) {
        CommitDiffData commitDiffData = new CommitDiffData();

        List<MergeCommit> mergeCommitList = mergeCommitService.findMergeCommitList(new MergeCommitQuery().setMergeRequestId(mergeId));
        if (CollectionUtils.isNotEmpty(mergeCommitList)){

            //差异提交数量
            commitDiffData.setCommitNum(mergeCommitList.size());


            try {
                List<MergeCommit> mergeCommits = mergeCommitList.stream().sorted(Comparator.comparing(MergeCommit::getCommitTime)).collect(Collectors.toList());
                MergeCommit mergeCommit = mergeCommitList.get(0);
                String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),mergeCommit.getRepositoryId());

                Git git = Git.open(new File(repositoryAddress));

                Map<String, RevCommit> revCommit = getRevCommit(git,mergeCommits);
                RevCommit oldCommit = revCommit.get("oldRevCommit");
                RevCommit newCommit = revCommit.get("newRevCommit");

                List<DiffEntry> diffEntries = GitCommitUntil.getDiffEntry(git.getRepository(), oldCommit, newCommit);
                //差异文件数量
                commitDiffData.setFileNum(diffEntries.size());

                commitDiffData.setClash(0);

            }catch (Exception e){
                throw new ApplicationException("合并请求的统计失败："+e);
            }
        }
        return commitDiffData;
    }

    @Override
    public List<String> findCommitUserList(String repositoryId) {
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),repositoryId);

        try {
            List<String> commitUserList = GitCommitUntil.getCommitUserList(repositoryAddress);
            return commitUserList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<CommitMessage> findLatelyCommit(String repositoryId, Integer number) {
        try {
            //获取仓库提交
            String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),repositoryId);
            List<CommitMessage> latelyCommit = GitCommitUntil.getLatelyCommit(repositoryAddress, number);
            return latelyCommit;
        }catch (Exception e){
            logger.info("最近仓库最近提交失败："+e.getMessage());
            throw new SystemException(e);
        }
    }


    /**
     * 获取最近一次的提交记录
     * @param commit 仓库id
     * @return 提交记录
     */
    @Override
    public CommitMessage findLatelyBranchCommit(Commit commit) {


        String rpyId = commit.getRpyId();
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),rpyId);

        try {
            Git git = Git.open(new File(repositoryAddress));
            org.eclipse.jgit.lib.Repository repository = git.getRepository();
            CommitMessage commitMessage = GitCommitUntil.findNewestCommit(repository, commit.getRefCode(),commit.getRefCodeType());
            return commitMessage;
        }catch (Exception e){
            e.printStackTrace();
            throw new ApplicationException("最后提交记录获取失败："+e);
        }
    }

    @Override
    public FileDiffEntry findDiffFileByBranchs(Commit commit) {
        String rpyId = commit.getRpyId();
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),rpyId);
        FileDiffEntry diffBranchFile;
        try {
            Git git = Git.open(new File(repositoryAddress));

            //查询不同分支提交的差异
            diffBranchFile= GitCommitUntil.findDiffBranchFile(git, commit);


            List<CommitFileDiffList> diffList = diffBranchFile.getDiffList();

            //构造树结构
            List<FileDiffTree> diffTreeList = constructorTree(diffList);
            diffBranchFile.setDiffTreeList(diffTreeList);

            git.close();
        } catch (Exception e) {
            throw new ApplicationException("提交记录获取失败："+e);
        }
        return diffBranchFile;
    }

    @Override
    public FileDiffEntry findDiffFileByCommitId(Commit commit) {

        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),commit.getRpyId());
        try {
            Git git = Git.open(new File(repositoryAddress));
        
            //获取两个提交差异文件
            FileDiffEntry fileDiffEntry = GitCommitUntil.findDiffFileByCommitId(git, commit.getCommitId());

            List<CommitFileDiffList> diffList = fileDiffEntry.getDiffList();

            //构造树结构
            List<FileDiffTree> diffTreeList = constructorTree(diffList);
            fileDiffEntry.setDiffTreeList(diffTreeList);
            return fileDiffEntry;
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }





    /**
     * 提交文件模糊查询
     * @param commit 查询信息
     * @return 查询结果
     */
    @Override
    public FileDiffEntry findLikeCommitDiffFileList(Commit commit) {
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),commit.getRpyId());
        try {

            Git git = Git.open(new File(repositoryAddress));

            //获取两个提交差异文件
            FileDiffEntry fileDiffEntry = GitCommitUntil.findDiffFileByCommitId(git, commit.getCommitId());
            List<CommitFileDiffList> diffList = fileDiffEntry.getDiffList();

            //配置模糊查询
            String likePath = commit.getLikePath();
            List<CommitFileDiffList> collected = diffList.stream().filter(a -> a.getNewFilePath().contains(likePath) || a.getOldFilePath().contains(likePath)).collect(Collectors.toList());
            fileDiffEntry.setDiffList(collected);

            return fileDiffEntry;
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * 获取提交的具体文件的改变内容
     * @param commit commitId
     * @return 文件列表
     */
    @Override
    public List<CommitFileDiff> findCommitFileDiff(Commit commit) {
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),commit.getRpyId());
        try {
            Git git = Git.open(new File(repositoryAddress));
            org.eclipse.jgit.lib.Repository repository = git.getRepository();

            List<CommitFileDiff> fileChanged = GitCommitUntil.findDiffCommitFileDetails(repository,commit.getCommitId(),commit.getOriginCommitId(),commit.getFilePath());
            git.close();
            return fileChanged;
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * 读取指定提交下的指定文件的指定行数
     * @param commit 提交信息
     * @return 文件内容
     */
    @Override
    public List<CommitFileDiff> findCommitLineFile(CommitFile commit){
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),commit.getRpyId());

        try {
            String refCode;
            if(("branch").equals(commit.getQueryType())){
                 refCode = commit.getBranch();
            }else {
                refCode=commit.getCommitId();
            }
            FileMessage  fileMessage = RepositoryFileUtil.readBranchFile(repositoryAddress,
                    refCode, commit.getPath(), commit.getQueryType());

            String message = fileMessage.getFileMessage();
            String[] split = message.split("\n");

            List<CommitFileDiff> list = new ArrayList<>();
            String direction = commit.getDirection();
            int count = commit.getCount();
            int newStn = commit.getNewStn();
            int oldStn = commit.getOldStn();

            //向上获取
            int i = 0;
            int length = 0;
            if (direction.equals(RepositoryFinal.FILE_UP)){
                int number = newStn - count;
                length = newStn;
                if (number >= 0){
                    i = number;
                }
                if (count == -1){
                    i = 0;
                }
            }
            //向下获取
            if (direction.equals(RepositoryFinal.FILE_DOWN)){
                int number = newStn + count - split.length;
                i = newStn;
                if (number >= 0 || count == -1){
                    length = split.length;
                }else {
                    length = newStn + count;
                }
            }

            for (int i1 = i; i1 < length-1; i1++) {
                CommitFileDiff fileDiff = new CommitFileDiff();
                fileDiff.setText(split[i1]);
                fileDiff.setLeft(i1 + (newStn-oldStn));
                fileDiff.setRight(i1+1);
                fileDiff.setType(RepositoryFinal.DIFF_TYPE_TEXT);
                list.add(fileDiff);
            }

            return list;
        } catch (IOException e) {
            throw new ApplicationException(e);
        }
    }

    //构造树结构
    public List<FileDiffTree> constructorTree(List<CommitFileDiffList> diffList){
        //递归添加parent
        List<FileDiffTree> diffFileList = new ArrayList<>();
        for (CommitFileDiffList commitFile:diffList){
            FileDiffTree diffTree = new FileDiffTree();
            diffTree.setType(commitFile.getType());
            diffTree.setFileType("file");
            diffTree.setFileName(commitFile.getFileName());

            String folderPath = commitFile.getNewFilePath();
            String[] split = folderPath.split("/");
            if (split.length>1){
                addParentBorder(split[0],split,diffFileList,0);
                diffTree.setFolderPath(folderPath);

                diffTree.setPrentFolder(commitFile.getFolderPath());
                diffFileList.add(diffTree);
            }else {
                diffTree.setFolderPath(folderPath);
                diffFileList.add(diffTree);
            }
        }

        //移除掉.DS_Store文件
        diffFileList = diffFileList.stream().filter(b -> !(".DS_Store").equals(b.getFileName())).collect(Collectors.toList());

        //通过parent构造树结构
        List<FileDiffTree> diffTreeList = new ArrayList<>();
        List<FileDiffTree> fileDiffTrees = diffFileList.stream().filter(a -> StringUtils.isBlank(a.getPrentFolder()))
                .collect(Collectors.toList());
        //排序
        fileDiffTrees=fileDiffTrees.stream()
                .sorted(Comparator.comparing(FileDiffTree::getFileType,
                        Comparator.<String>comparingInt(s -> s.equals("tree") ? 0 : (s.equals("file") ? 1 : 2))
                                .thenComparing(Comparator.naturalOrder())))
                .collect(Collectors.toList());

        for (FileDiffTree diffTree:fileDiffTrees){
            if (("tree").equals(diffTree.getFileType())){
                joinTree(diffFileList,diffTree,1);
            }
            diffTreeList.add(diffTree);
        }

        return diffTreeList;
    }

    /**
     * 递归添加 Parent目录
     * @param folderPath folderPath
     */
    public void addParentBorder(String folderPath, String [] split,List<FileDiffTree> diffFileList,Integer index){
        FileDiffTree diffTree = new FileDiffTree();
        String border = split[index];
        diffTree.setFileType("tree");
        diffTree.setFileName(border);

        String s;
        //第一级文件夹
        if (index==0){
            List<FileDiffTree> fileDiffTrees = diffFileList.stream().filter(a -> a.getFileName().equals(border)
                            && "tree".equals(a.getFileType()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isEmpty(fileDiffTrees)){
                diffTree.setFolderPath(border);
                diffFileList.add(diffTree);
            }
            s=folderPath;
        }else {
            //父级别index
            diffTree.setPrentFolder(folderPath);

            s = folderPath + "/" + split[index];
            diffTree.setFolderPath(s);
        }

        int i = split.length - index;
        if (i>1){
            List<FileDiffTree> fileDiffTrees = diffFileList.stream().filter(a -> a.getFileName().equals(border)
                            & s.equals(a.getFolderPath())
                            && "tree".equals(a.getFileType()))
                    .collect(Collectors.toList());

            if (CollectionUtils.isEmpty(fileDiffTrees)){
                diffFileList.add(diffTree);
            }
            addParentBorder(s,split,diffFileList,index+1);
        }

    }

    /**
     * 通过 Parent目录构造树结构
     * @param diffTreeList folderPath
     */
    public void joinTree(List<FileDiffTree> diffTreeList,FileDiffTree diffTree,Integer index){
        List<FileDiffTree> fileDiffTrees;
        if (index==1){
            fileDiffTrees = diffTreeList.stream()
                    .filter(a -> (diffTree.getFileName()).equals(a.getPrentFolder()))
                    .collect(Collectors.toList());
        }else {
            fileDiffTrees = diffTreeList.stream()
                    .filter(a -> (diffTree.getPrentFolder()+"/" +diffTree.getFileName()).equals(a.getPrentFolder()))
                    .collect(Collectors.toList());
        }

        //获取文件夹下面所有更改的文件数量
        if (("tree").equals(diffTree.getFileType())){
            List<FileDiffTree> fileList = diffTreeList.stream().filter(a -> ("file").equals(a.getFileType()) && org.apache.commons.lang3.StringUtils.isNotBlank( a.getPrentFolder())&&
                            a.getPrentFolder().startsWith(diffTree.getFolderPath()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(fileList)){
                diffTree.setFileNum(fileList.size());
            }
        }

        //排序
        fileDiffTrees=fileDiffTrees.stream()
                .sorted(Comparator.comparing(FileDiffTree::getFileType,
                        Comparator.<String>comparingInt(s -> s.equals("tree") ? 0 : (s.equals("file") ? 1 : 2))
                                .thenComparing(Comparator.naturalOrder())))
                .collect(Collectors.toList());

        diffTree.setFileDiffTreeList(fileDiffTrees);
        if (CollectionUtils.isNotEmpty(fileDiffTrees)){
            for (FileDiffTree fileDiffTree:fileDiffTrees){
                joinTree(diffTreeList,fileDiffTree,index+1);
            }
        }
    }


    /**
     * 获取指定commitId的新旧树
     * @param repository 仓库
     * @param branch 分支
     * @param b 是否为commitId
     * @return 新旧树
     * @throws IOException commitId不存在
     */
    private Map<String,RevCommit> findCommitNewOldTree(org.eclipse.jgit.lib.Repository repository, String branch, boolean b) throws IOException {
        RevWalk walk = new RevWalk(repository);
        ObjectId objectId;
        if (!b){
            objectId = repository.resolve(Constants.R_HEADS + branch);
        }else {
            objectId = ObjectId.fromString(branch);
        }
        RevCommit revCommit =  walk.parseCommit(objectId);

        //获取旧树
        RevCommit oldRevCommit = GitCommitUntil.findPrevHash(revCommit, repository);

        Map<String,RevCommit> map = new HashMap<>();

        map.put("newTree",revCommit);
        map.put("oldTree",oldRevCommit);
        walk.close();
        return map;
    }

    /**
     * 提交记录根据日期排序（按天）
     * @param branchCommit 提交记录
     * @param list 每天的提交记录
     * @return 提交记录
     */
    private List<CommitMessage> groupCommit(List<CommitMessage> branchCommit, List<CommitMessage> list){
        List<CommitMessage> removeList = new ArrayList<>();
        CommitMessage commitMessage = new CommitMessage();
        Date date = branchCommit.get(0).getDateTime();
        String time= RepositoryUtil.date(2, date);
        commitMessage.setCommitTime(time);
        for (CommitMessage message : branchCommit) {
            Date dateTime = message.getDateTime();
            if (!time.equals(RepositoryUtil.date(2, dateTime))) {
                continue;
            }
            removeList.add(message);
        }
        commitMessage.setCommitMessageList(removeList);
        list.add(commitMessage);
        branchCommit.removeAll(removeList);
        if (branchCommit.size() != 0){
            groupCommit(branchCommit,list);
        }
        return list;
    }

    /**
     * 通过合并提交的差异commitId  获取新旧revCommit
     * @param mergeCommitList mergeCommitList
     * @param git
     * @return 提交记录
     */
    public Map<String, RevCommit> getRevCommit(Git git,List<MergeCommit> mergeCommitList) throws IOException {
        Map<String, RevCommit> revCommitMap = new HashMap<>();
        MergeCommit mergeCommit = mergeCommitList.get(0);

        //获取父级的commit
        RevWalk walk = new RevWalk(git.getRepository());
        RevCommit commit =  walk.parseCommit(ObjectId.fromString(mergeCommit.getCommitId()));
        RevCommit[] parents = commit.getParents();
        RevCommit oldCommit=null;
        if (!ObjectUtils.isEmpty(parents)){
            String parentCommitId = parents[0].getId().getName();
            ObjectId oldObjectId = ObjectId.fromString(parentCommitId);
            oldCommit = walk.parseCommit(oldObjectId);
        }

        //最新的提交commit
        MergeCommit newMergeCommit = mergeCommitList.get(mergeCommitList.size() - 1);
        RevCommit newCommit =  walk.parseCommit(ObjectId.fromString(newMergeCommit.getCommitId()));

        revCommitMap.put("oldRevCommit",oldCommit);
        revCommitMap.put("newRevCommit",newCommit);
        return revCommitMap;
    }



}
