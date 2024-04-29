package io.thoughtware.gittok.commit.service;


import io.thoughtware.gittok.commit.model.*;
import io.thoughtware.gittok.common.GitTokYamlDataMaService;
import io.thoughtware.gittok.common.git.GitBranchUntil;
import io.thoughtware.gittok.common.git.GitCommitUntil;
import io.thoughtware.gittok.file.model.FileMessage;
import io.thoughtware.core.exception.ApplicationException;
import io.thoughtware.gittok.common.RepositoryFileUtil;
import io.thoughtware.gittok.common.RepositoryFinal;
import io.thoughtware.gittok.common.RepositoryUtil;
import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommitServerImpl implements CommitServer {

    @Autowired
    private GitTokYamlDataMaService yamlDataMaService;

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
    public FileDiffEntry findDiffFileByBranchs(Commit commit) {
        String rpyId = commit.getRpyId();
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),rpyId);
        FileDiffEntry diffBranchFile;
        try {
            Git git = Git.open(new File(repositoryAddress));

            //查询不同分支提交的差异
            diffBranchFile= GitCommitUntil.findDiffBranchFile(git, commit);
            git.close();
        } catch (Exception e) {
            throw new ApplicationException("提交记录获取失败："+e);
        }
        return diffBranchFile;
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
            Git git = Git.open(new File(repositoryAddress));
             return   GitCommitUntil.getDiffStatistics(git,commit);
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


    /**
     * 获取最近一次的提交记录
     * @param commit 仓库id
     * @return 提交记录
     */
    @Override
    public CommitMessage findLatelyBranchCommit(Commit commit) {
        List<CommitMessage> branchCommit = findBranchCommit(commit);
        if (branchCommit.isEmpty()){
            return null;
        }

        String rpyId = commit.getRpyId();
        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),rpyId);

        try {
            Git git = Git.open(new File(repositoryAddress));
            org.eclipse.jgit.lib.Repository repository = git.getRepository();

            CommitMessage commitMessage = GitCommitUntil.findNewestCommit(repository, commit.getBranch(),"branch");
            return commitMessage;
        }catch (Exception e){
            throw new ApplicationException("最后提交记录获取失败："+e);
        }
        /*return branchCommit.get(0).getCommitMessageList().get(0);*/
    }


    @Override
    public FileDiffEntry findDiffFileByCommitId(Commit commit) {

        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),commit.getRpyId());
        try {
            Git git = Git.open(new File(repositoryAddress));

            //获取两个提交差异文件
            FileDiffEntry fileDiffEntry = GitCommitUntil.findDiffFileByCommitId(git, commit.getCommitId());

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
            Git git = Git.open(new File(repositoryAddress));
            org.eclipse.jgit.lib.Repository repository = git.getRepository();

            //查询方式通过提交id 或者分查询
            FileMessage fileMessage;
            if (("commit").equals(commit.getQueryType())){
                 fileMessage = RepositoryFileUtil.readBranchFile(repository,
                        commit.getCommitId(), commit.getPath(), true);
            }else {
                fileMessage = RepositoryFileUtil.readBranchFile(repository,
                        commit.getBranch(), commit.getPath(), false);
            }

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
            git.close();
            return list;
        } catch (IOException e) {
            throw new ApplicationException(e);
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
