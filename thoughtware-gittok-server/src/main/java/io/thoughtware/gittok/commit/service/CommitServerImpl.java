package io.thoughtware.gittok.commit.service;


import io.thoughtware.gittok.commit.model.*;
import io.thoughtware.gittok.common.GitTokYamlDataMaService;
import io.thoughtware.gittok.common.git.GitCommitUntil;
import io.thoughtware.gittok.file.model.FileMessage;
import io.thoughtware.core.exception.ApplicationException;
import io.thoughtware.gittok.common.RepositoryFileUtil;
import io.thoughtware.gittok.common.RepositoryFinal;
import io.thoughtware.gittok.common.RepositoryUtil;
import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommitServerImpl implements CommitServer {

    @Autowired
    private GitTokYamlDataMaService yamlDataMaService;

    /**
     * 获取分支提交记录
     * @param commit 信息
     * @return 提交记录
     */
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
        return commitSort(branchCommit, new ArrayList<>());
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
        return branchCommit.get(0).getCommitMessageList().get(0);
    }

    /**
     * 获取提交的文件信息
     * @param commit commitId
     * @return 文件列表
     */
    @Override
    public FileDiffEntry findCommitDiffFileList(Commit commit) {

        String repositoryAddress = RepositoryUtil.findRepositoryAddress(yamlDataMaService.repositoryAddress(),commit.getRpyId());
        try {
            Git git = Git.open(new File(repositoryAddress));
            org.eclipse.jgit.lib.Repository repository = git.getRepository();

            Map<String, RevCommit> newOldTree = findCommitNewOldTree(
                    repository, commit.getBranch(), commit.isFindCommitId());

            RevCommit oldRevCommit =  newOldTree.get("oldTree");
            RevCommit revCommit = newOldTree.get("newTree");

            FileDiffEntry changedList = GitCommitUntil.findFileChangedListX(repository, revCommit,
                    oldRevCommit,commit.getNumber());
            List<CommitFileDiffList> diffList = changedList.getDiffList();
            int allAddLine = 0;
            int allDeleteLine = 0;
            for (CommitFileDiffList list : diffList) {
                int addLine = list.getAddLine();
                int deleteLine = list.getDeleteLine();
                allAddLine = allAddLine + addLine;
                allDeleteLine = allDeleteLine + deleteLine;
            }
            changedList.setDeleteLine(allDeleteLine);
            changedList.setAddLine(allAddLine);
            return changedList;
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
            org.eclipse.jgit.lib.Repository repository = git.getRepository();
            Map<String, RevCommit> newOldTree = findCommitNewOldTree(
                    repository, commit.getBranch(), commit.isFindCommitId());
            RevCommit oldRevCommit =  newOldTree.get("oldTree");
            RevCommit revCommit = newOldTree.get("newTree");

            FileDiffEntry changedList = GitCommitUntil.findFileChangedListX(repository, revCommit,
                    oldRevCommit,null);
            List<CommitFileDiffList> diffList = changedList.getDiffList();
            List<CommitFileDiffList> lists = new ArrayList<>();
            String likePath = commit.getLikePath();
            for (CommitFileDiffList list : diffList) {
                String newFilePath = list.getNewFilePath();
                String oldFilePath = list.getOldFilePath();
                if (!newFilePath.contains(likePath) && !oldFilePath.contains(likePath)){
                    continue;
                }
                lists.add(list);
            }
            changedList.setDiffList(lists);

            return changedList;
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

            Map<String, RevCommit> newOldTree = findCommitNewOldTree(
                    repository, commit.getBranch(), commit.isFindCommitId());

            RevCommit revCommit = newOldTree.get("newTree");

            //获取旧树
            RevCommit oldRevCommit =  newOldTree.get("oldTree");

            List<CommitFileDiff> fileChanged = GitCommitUntil.findFileChanged(repository,
                    revCommit, oldRevCommit, commit.getFilePath());
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

            FileMessage fileMessage = RepositoryFileUtil.readBranchFile(repository,
                    commit.getCommitId(), commit.getPath(), true);
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
    private List<CommitMessage> commitSort(List<CommitMessage> branchCommit, List<CommitMessage> list){
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
            commitSort(branchCommit,list);
        }
        return list;
    }
}









































