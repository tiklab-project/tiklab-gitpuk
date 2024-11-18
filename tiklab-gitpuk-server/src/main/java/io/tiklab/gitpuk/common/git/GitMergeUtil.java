package io.tiklab.gitpuk.common.git;

import io.tiklab.core.exception.ApplicationException;
import io.tiklab.core.exception.SystemException;
import io.tiklab.gitpuk.merge.model.MergeClashFile;
import io.tiklab.gitpuk.merge.model.MergeClashFileSet;
import io.tiklab.gitpuk.merge.model.MergeData;
import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.*;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.dircache.DirCacheBuilder;
import org.eclipse.jgit.dircache.DirCacheEntry;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.merge.MergeStrategy;
import org.eclipse.jgit.merge.Merger;
import org.eclipse.jgit.merge.ThreeWayMerger;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class GitMergeUtil {
    private static Logger logger = LoggerFactory.getLogger(GitMergeUtil.class);
    /**
     * 执行合并请求
     * @param mergeData    mergeData
     * @param repositoryPath  裸仓库地址
     */
    public static String mergeBranch(MergeData mergeData, String repositoryPath) {
        try {
            // 构建裸仓库对象
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository = builder.setGitDir(new File(repositoryPath))
                    .readEnvironment()
                    .findGitDir()
                    .build();
            // 打开 Git 对象
            Git git = new Git(repository);


            //获取源分支的引用
            Ref originBranch = repository.exactRef("refs/heads/"+mergeData.getMergeOrigin());
            ObjectId originObjectId = originBranch.getObjectId();

            // 获取目标分支的引用
            Ref targetBranch = repository.exactRef("refs/heads/"+mergeData.getMergeTarget());
            ObjectId targetObjectId = targetBranch.getObjectId();

            RevWalk revWalk = new RevWalk(repository);
            RevCommit originCommit = revWalk.parseCommit(originObjectId);
            RevCommit targetCommit = revWalk.parseCommit(targetObjectId);

            //用FastForward 合并
            if (("fast").equals(mergeData.getMergeWay())){
                // 检查是否可以进行 FastForward 合并
                boolean canFastForward = revWalk.isMergedInto(targetCommit, originCommit);
                if (canFastForward) {
                    // 可以进行 FastForward 合并，直接更新基础分支的引用指向目标分支的最新提交
                    RefUpdate refUpdate = repository.updateRef("refs/heads/"+mergeData.getMergeTarget());
                    refUpdate.setNewObjectId(originObjectId);
                    RefUpdate.Result updateResult = refUpdate.update();
                    /*
                     *   NEW：表示引用是新创建的。
                     *   FORCED：表示引用已被强制更新。
                     *   FAST_FORWARD：表示引用是通过快进方式更新的。
                     *   NO_CHANGE：表示引用没有发生变化，无需更新。
                     *   LOCK_FAILURE：表示在更新引用时遇到了锁定失败。
                     *   REJECTED：表示更新引用被拒绝。
                     *   IO_FAILURE：表示在更新引用时遇到了I/O错误。
                     * */
                    String name = updateResult.name();

                }else {
                    logger.info(repositoryPath+"合并不满足FastForward条件" );
                    throw new ApplicationException(5000,"当前合并不满足FastForward条件");
                }
            }

            //总是创建一个合并节点，记录合并信息。源分支和目标分支的提交记录不变
            if (("createNode").equals(mergeData.getMergeWay())){
                // 使用三方合并策略
                ThreeWayMerger merger = MergeStrategy.RECURSIVE.newMerger(repository, true);
                boolean mergedSuccessfully = merger.merge(targetCommit, originCommit);
                if (!mergedSuccessfully) {
                    throw new ApplicationException(5000,"三方合并策略失败");
                }

                // 获取合并后的树结构
                ObjectId mergedTreeId = merger.getResultTreeId();

                // 创建合并提交，设置两个父提交（基础分支和目标分支）
                CommitBuilder commitBuilder = new CommitBuilder();
                commitBuilder.setTreeId(mergedTreeId);  // 使用合并后的树结构
                commitBuilder.setParentIds(targetObjectId, originObjectId);  // 两个父提交
                commitBuilder.setAuthor(new PersonIdent("Your Name", "you@example.com"));  // 设置提交者
                commitBuilder.setCommitter(new PersonIdent("Your Name", "you@example.com"));  // 设置提交者
                commitBuilder.setMessage("Merge feature into master");  // 合并消息
                // 将新的合并提交插入仓库
                try (ObjectInserter inserter = repository.newObjectInserter()) {
                    ObjectId mergeCommitId = inserter.insert(commitBuilder);
                    inserter.flush();

                    // 更新基础分支（例如 master）指向新提交
                    RefUpdate refUpdate = repository.updateRef("refs/heads/"+mergeData.getMergeTarget());
                    refUpdate.setNewObjectId(mergeCommitId);
                    refUpdate.update();
                }
            }

             //将合并请求中的提交记录压缩成一条， 然后添加到目标分支。
            if (("squash").equals(mergeData.getMergeWay())){
                // 创建合并器  源分支合并到目标分支
                Merger merger = MergeStrategy.THEIRS.newMerger(repository, true);
                boolean mergeSucceeded = merger.merge(targetCommit, originCommit);
                if (mergeSucceeded) {
                    PersonIdent author = new PersonIdent("Author Name", "author@example.com");

                    // 构建一个新的提交
                    CommitBuilder commitBuilder = new CommitBuilder();
                    commitBuilder.setParentIds(Collections.singletonList(targetCommit.getId()));  // 设置父提交（基础分支的提交）
                    commitBuilder.setTreeId(merger.getResultTreeId());  // 使用合并后的树对象
                    commitBuilder.setAuthor(author);
                    commitBuilder.setCommitter(author);
                    commitBuilder.setMessage(mergeData.getMergeMessage());


                    // 插入新的提交对象
                    try (ObjectInserter inserter = repository.newObjectInserter()) {
                        ObjectId commitId = inserter.insert(commitBuilder);
                        inserter.flush();

                        // 更新基础分支（例如 master）指向新提交
                        RefUpdate refUpdate = repository.updateRef("refs/heads/"+mergeData.getMergeTarget());
                        refUpdate.setNewObjectId(commitId);
                        refUpdate.update();
                    }

                } else {
                    throw new SystemException("合并失败:"+merger.getResultTreeId().getName());
                }
            }

            //变基合并  将评审（源分支）分支提交逐一编辑到目标分支
            if (("rebase").equals(mergeData.getMergeWay())){
                // 找到两个分支的公共祖先（merge base）
                RevCommit commonAncestor = getCommonAncestor(revWalk,originCommit, targetCommit );

                //重新创建revWalk对象 将源分支放入revWalk对象中进行循环处理
                revWalk.reset();
                revWalk.sort(RevSort.TOPO);
                revWalk.sort(RevSort.REVERSE);
                revWalk.markStart(originCommit);
                //排序公共提交前的提交
                revWalk.markUninteresting(commonAncestor);

                RevCommit newTargetCommit=  targetCommit;
                // 遍历源分支的提交并“重放”到目标分支
                for (RevCommit commit : revWalk) {

                    // 使用 Merger 来合并基础分支和目标分支的树结构
                    ThreeWayMerger merger = MergeStrategy.SIMPLE_TWO_WAY_IN_CORE.newMerger(repository, true);
                   // merger.setBase(commonAncestor);
                    boolean mergedSuccessfully = merger.merge(targetCommit, commit);
                    if (!mergedSuccessfully){
                        throw new SystemException("不支持该合并方式,请换一个合并方式");
                    }
                    // 获取合并后的树对象
                    ObjectId mergedTreeId = merger.getResultTreeId();

                    // 将目标分支的提交应用到基础分支
                    CommitBuilder commitBuilder = new CommitBuilder();
                    commitBuilder.setTreeId(mergedTreeId);  // 重用目标分支的树对象
                    commitBuilder.setParentId(newTargetCommit);  // 设定新父提交为源分支的最新提交
                    commitBuilder.setAuthor(new PersonIdent(commit.getAuthorIdent(), new Date()));  // 保留原提交的作者信息
                    commitBuilder.setCommitter(new PersonIdent(commit.getCommitterIdent(), new Date()));
                    commitBuilder.setMessage(commit.getFullMessage());  // 保留提交信息

                    // 插入新的提交到裸仓库
                    try (ObjectInserter inserter = repository.newObjectInserter()) {
                        ObjectId newCommitId = inserter.insert(commitBuilder);
                        inserter.flush();

                        // 更新基础分支的提交
                        targetObjectId = newCommitId;
                        newTargetCommit = revWalk.parseCommit(targetObjectId);
                    }
                    // 更新基础分支（例如 master）指向新提交
                    RefUpdate refUpdate = repository.updateRef("refs/heads/"+mergeData.getMergeTarget());
                    refUpdate.setNewObjectId(targetObjectId);
                    refUpdate.update();
                }
            }

            // 关闭资源
            revWalk.close();
            repository.close();
            git.close();
            return "ok";
        }catch (Exception e){
            throw new SystemException("合并失败"+e.getMessage());
        }
    }


    /**
     * 查询两个分支的冲突文件
     * @param mergeData    mergeData
     * @param repositoryPath  裸仓库地址
     */
    public static List<String> findConflictingFile(MergeData mergeData, String repositoryPath){
        List<String> arrayList = new ArrayList<>();
        try {

            // 构建裸仓库对象
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository = builder.setGitDir(new File(repositoryPath))
                    .readEnvironment()
                    .findGitDir()
                    .build();
            // 打开 Git 对象
            Git git = new Git(repository);


            //获取源分支的引用
            Ref originBranch = repository.exactRef("refs/heads/"+mergeData.getMergeOrigin());
            ObjectId originObjectId = originBranch.getObjectId();

            // 获取目标分支的引用
            Ref targetBranch = repository.exactRef("refs/heads/"+mergeData.getMergeTarget());
            ObjectId targetObjectId = targetBranch.getObjectId();

            RevWalk revWalk = new RevWalk(repository);
            RevCommit originCommit = revWalk.parseCommit(originObjectId);
            RevCommit targetCommit = revWalk.parseCommit(targetObjectId);


            // 获取分支的 TreeId
            ObjectId baseTreeId = originCommit.getTree();
            ObjectId targetTreeId = targetCommit.getTree();

            // 使用 TreeWalk 比较树对象
            TreeWalk treeWalk = new TreeWalk(repository);
            treeWalk.addTree(baseTreeId);
            treeWalk.addTree(targetTreeId);
            treeWalk.setRecursive(true);

            while (treeWalk.next()) {
                String path = treeWalk.getPathString();
                int baseMode = treeWalk.getRawMode(0); // 获取第一个树中的文件模式
                int targetMode = treeWalk.getRawMode(1); // 获取第二个树中的文件模式
                ObjectId baseObjectId = treeWalk.getObjectId(0);  // 获取第一个树的文件 ObjectId
                ObjectId ObjectId = treeWalk.getObjectId(1); // 获取第二个树的文件 ObjectId

                //源分支模式不为0,目标分支的模式为0代表源分支是新建;源分支为0，目标分支不为0代表目标分支是新建
                if (targetMode!=0&&targetMode!=0){
                    if (baseMode == targetMode) {
                        if (!baseObjectId.equals(ObjectId)) {
                            // 文件模式相同，但内容不同
                            logger.info("文件内容修改: " + path);
                            arrayList.add(path);
                        }
                    } else {
                        // 文件模式不同，可能是类型变化或文件新增/删除
                        logger.info("文件模式变化或文件新增/删除: " + path);
                        //arrayList.add(path);
                    }
                }
            }

            // 关闭资源
            revWalk.close();
            repository.close();
            git.close();
            return arrayList;
        }catch (Exception e){
            logger.info("获取冲突文件失败: " + e.getMessage());
            throw new SystemException("获取冲突文件失败："+e.getMessage());
        }
    }

    /**
     * 查询两个分支的冲突文件的详情
     * @param mergeData    mergeData
     * @param repositoryPath  裸仓库地址
     */
    public static MergeClashFile findConflictingFileDetails(MergeData mergeData, String repositoryPath){
        try {
            MergeClashFile mergeClashFile = new MergeClashFile();
            //冲突文件路径
            String conflictFilePath = mergeData.getFilePath();
            mergeClashFile.setFilePath(conflictFilePath);

            // 构建裸仓库对象
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository = builder.setGitDir(new File(repositoryPath))
                    .readEnvironment()
                    .findGitDir()
                    .build();


            //获取源分支的引用
            Ref originBranch = repository.exactRef("refs/heads/" + mergeData.getMergeOrigin());
            ObjectId originObjectId = originBranch.getObjectId();

            // 获取目标分支的引用
            Ref targetBranch = repository.exactRef("refs/heads/" + mergeData.getMergeTarget());
            ObjectId targetObjectId = targetBranch.getObjectId();

            RevWalk revWalk = new RevWalk(repository);
            RevCommit originCommit = revWalk.parseCommit(originObjectId);
            RevCommit targetCommit = revWalk.parseCommit(targetObjectId);

            // 解析冲突文件的三方内容
            ObjectId originTreeId = originCommit.getTree();
            ObjectId targetTreeId = targetCommit.getTree();

            //获取两个分支的文件内容
            ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
            DiffFormatter diffFormatter = new DiffFormatter(fileOut);
            diffFormatter.setRepository(repository);
            diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
            diffFormatter.setDetectRenames(true);
            List<DiffEntry> diffs = diffFormatter.scan(targetTreeId, originTreeId);

            for (DiffEntry entry : diffs) {
                if (conflictFilePath.equals(entry.getNewPath())){
                    FileHeader fileHeader = diffFormatter.toFileHeader(entry);
                    diffFormatter.format(fileHeader);
                    String diffOutput = fileOut.toString("UTF-8");


                    // 获取old文件内容
                    ObjectId oldBlobId = entry.getOldId().toObjectId();
                    ObjectLoader oldLoader = repository.open(oldBlobId);
                    ByteArrayOutputStream oldOut = new ByteArrayOutputStream();
                    oldLoader.copyTo(oldOut);
                    String oldFileContent = oldOut.toString();
                    RawText oldrawText = new RawText(oldFileContent.getBytes());

                    // 获取文件内容
                    ObjectId blobId = entry.getNewId().toObjectId();
                    ObjectLoader loader = repository.open(blobId);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    loader.copyTo(out);
                    String fileContent = out.toString();
                    String[] fileLines = fileContent.split("\n");

                    EditList editList = diffFormatter.toFileHeader(entry).toEditList();
                    RawText rawText = new RawText(fileContent.getBytes());

                    StringBuilder output = new StringBuilder();
                    int currentLine = 0;
                    for (Edit edit : editList) {
                        // 输出未修改的部分
                        while (currentLine < edit.getBeginB()) {
                            output.append(fileLines[currentLine]).append("\n");
                            currentLine++;
                        }

                        // 输出添加的行（在目标文件中新增的内容）
                        for (int i = edit.getBeginA(); i < edit.getEndA(); i++) {
                            output.append("-" +oldrawText.getString(i)).append("\n");
                        }
                        // 输出删除的行（在源文件中被删除的内容）
                        for (int i = edit.getBeginB(); i < edit.getEndB(); i++) {
                            output.append("+" +rawText.getString(i)).append("\n");
                        }
                        currentLine = edit.getEndB();
                    }
                    // 输出剩下的文件内容
                    while (currentLine < fileLines.length) {
                        output.append(fileLines[currentLine]).append("\n");
                        currentLine++;
                    }
                    //合并冲突的输出处理
                    String diffFile = mergeConflictOutputWithDiff(mergeData, output.toString(), mergeClashFile);
                    mergeClashFile.setFileData(diffFile);
                }
            }
            revWalk.close();
            return mergeClashFile;
        }catch (Exception e){
            logger.info("查询冲突文件详情失败："+e.getMessage());
            throw new SystemException("查询冲突文件详情失败："+e.getMessage());
        }
    }



    /**
     * 解决文件冲突
     * @param mergeClashFileSet    mergeClashFileSet
     * @param repositoryPath  裸仓库地址
     */
    public static void conflictResolutionFile(MergeClashFileSet mergeClashFileSet, String repositoryPath){
        try {
            MergeClashFile mergeClashFile = mergeClashFileSet.getMergeClashFileList().get(0);
            List<MergeClashFile> clashFileList = mergeClashFileSet.getMergeClashFileList();

            File bareRepoDir = new File(repositoryPath);
            // 加载裸仓库
            Repository repository = new FileRepositoryBuilder()
                    .setGitDir(bareRepoDir)
                    .build();

            //获取源分支的引用
            Ref originBranch = repository.exactRef("refs/heads/"+mergeClashFileSet.getMergeOrigin());
            ObjectId originObjectId = originBranch.getObjectId();

            // 获取目标分支的引用
            Ref targetBranch = repository.exactRef("refs/heads/"+mergeClashFileSet.getMergeTarget());
            ObjectId targetObjectId = targetBranch.getObjectId();

            RevWalk revWalk = new RevWalk(repository);
            RevCommit originCommit = revWalk.parseCommit(originObjectId);
            RevCommit targetCommit = revWalk.parseCommit(targetObjectId);

            // 用 TreeWalk 遍历需要修改的树
            RevTree tree = targetCommit.getTree();
            TreeWalk treeWalk = new TreeWalk(repository);
            treeWalk.addTree(tree);
            treeWalk.setRecursive(true);

            // 创建新的树对象
            ObjectInserter inserter = repository.newObjectInserter();
            //更新内容
            ObjectId newTreeId = updateBareRepoFile(inserter, treeWalk, clashFileList);


            // 找到两个分支的公共祖先（merge base）
            RevCommit commonAncestor = getCommonAncestor(revWalk,originCommit, targetCommit );
            //重新创建revWalk对象 将源分支放入revWalk对象中进行循环处理
            revWalk.reset();
            revWalk.markStart(originCommit);
            revWalk.sort(RevSort.COMMIT_TIME_DESC);
            //最后提交的commitId
            ObjectId lastCommitId=null;
            for (RevCommit commit : revWalk) {
                 lastCommitId = commit.getId();
                break;
            }

            //重新创建revWalk对象 将源分支放入revWalk对象中进行循环处理
            revWalk.reset();
            revWalk.markStart(originCommit);
            revWalk.sort(RevSort.TOPO);
            revWalk.sort(RevSort.REVERSE);
            //排序公共提交后的提交
            revWalk.markUninteresting(commonAncestor);
            // 遍历源分支的提交并“重放”到目标分支
            for (RevCommit commit : revWalk) {
                ObjectId commitId = commit.getId();
                // 将目标分支的提交应用到基础分支
                CommitBuilder commitBuilder = new CommitBuilder();

                //
                if (lastCommitId.equals(commitId)){
                    commitBuilder.setTreeId(newTreeId);  // 使用修改后的树结构
                    commitBuilder.setParentIds(targetObjectId, originObjectId);  // 两个父提交
                    commitBuilder.setAuthor(new PersonIdent("Your Name", "you@example.com"));  // 设置提交者
                    commitBuilder.setCommitter(new PersonIdent("Your Name", "you@example.com"));  // 设置提交者
                    commitBuilder.setMessage("Merge feature into master");  // 合并消息
                }else {
                    // 将目标分支的提交应用到基础分支
                    commitBuilder.setTreeId(commit.getTree());  // 重用目标分支的树对象
                    commitBuilder.setParentIds(commit.getParents());
                    commitBuilder.setAuthor(new PersonIdent(commit.getAuthorIdent()));  // 保留原提交的作者信息
                    commitBuilder.setCommitter(new PersonIdent(commit.getCommitterIdent()));
                    commitBuilder.setMessage(commit.getFullMessage());  // 保留提交信息
                }

                ObjectId mergeCommitId = inserter.insert(commitBuilder);
                inserter.flush();

                // 更新基础分支（例如 master）指向新提交
                RefUpdate refUpdate = repository.updateRef("refs/heads/"+mergeClashFileSet.getMergeTarget());
                refUpdate.setNewObjectId(mergeCommitId);
                refUpdate.update();
            }

        }catch (Exception e){
            throw new SystemException("在线解决文件冲突失败："+e.getMessage());
        }

    }


    // 合并冲突的输出处理，结合 DiffFormatter 的差异内容
    public static String mergeConflictOutputWithDiff(MergeData mergeData,String diffOutput,MergeClashFile mergeClashFile) {
        StringBuilder output = new StringBuilder();
        boolean inConflict = false;
        StringBuilder baseContent = new StringBuilder();
        StringBuilder targetContent = new StringBuilder();

        String[] diffLines = diffOutput.split("\n");
        int clashNum=0;  //冲突的数量
       int a=0;
        for (String line : diffLines) {
          /*  a+=1;
            if (a<=3){
                continue;
            }*/
        /*    if (line.startsWith("+++") || line.startsWith("---")){
                continue;
            }
            if (line.startsWith("@@") || line.endsWith("@@")){
                continue;
            }*/
            //将文件末尾没有换行 说明去掉
            if (line.equals("\\ No newline at end of file")){
                continue;
            }
            if (line.startsWith("-")) {
                // 基础分支的修改部分
                baseContent.append(line.substring(1)).append("\n");
                inConflict = true;
            } else if (line.startsWith("+")) {
                // 目标分支的修改部分
                targetContent.append(line.substring(1)).append("\n");
                inConflict = true;
            } else {
                // 非冲突部分，直接输出
                if (inConflict) {
                    // 输出冲突块
                    output.append("<<<<<<< "+mergeData.getMergeTarget()+"\n")
                            .append(baseContent)
                            .append("=======\n")
                            .append(targetContent)
                            .append(">>>>>>> "+mergeData.getMergeOrigin()+"\n");

                    // 清空缓存，处理下一段内容
                    baseContent.setLength(0);
                    targetContent.setLength(0);
                    inConflict = false;
                    clashNum+=1;
                }
                output.append(line).append("\n");
            }
        }

        // 处理最后一个冲突块
        if (inConflict) {
            output.append("<<<<<<< "+mergeData.getMergeTarget()+"\n")
                    .append(baseContent)
                    .append("=======\n")
                    .append(targetContent)
                    .append(">>>>>>> "+mergeData.getMergeOrigin());
            clashNum+=1;
        }
        mergeClashFile.setClashNum(clashNum);

        return output.toString();
    }





    // 读取文件内容
    private static String readFileContent(ObjectReader reader, ObjectId treeId, String filePath) throws Exception {
        TreeWalk treeWalk = TreeWalk.forPath(reader, filePath, treeId);
        if (treeWalk != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            reader.open(treeWalk.getObjectId(0)).copyTo(outputStream);
            return outputStream.toString(StandardCharsets.UTF_8);
        }
        return "";
    }

    /**
     * 查询两个分支最后一个共同的提交
     * @param revWalk    revWalk
     * @param originCommit 分支一的commit对象
     * @param targetCommit  分支二的commit对象
     */
    public static RevCommit getCommonAncestor(RevWalk revWalk, RevCommit originCommit, RevCommit targetCommit) throws Exception {
        HashSet<Object> originSet = new HashSet<>();

        // Traverse commitA's ancestors
        revWalk.markStart(originCommit);
        for (RevCommit ancestor : revWalk) {
            originSet.add(ancestor);
        }

        // Traverse commitB's ancestors
        revWalk.reset();
        revWalk.markStart(targetCommit);
        for (RevCommit ancestor : revWalk) {
            if (originSet.contains(ancestor)) {
                return ancestor; // Found common ancestor
            }
        }
        return null;
    }


    /**
     * 更新裸仓库的文件
     * @param inserter  inserter
     * @param treeWalk treeWalk
     * @param clashFileList clashFileList
     */
    public static ObjectId updateBareRepoFile( ObjectInserter inserter, TreeWalk treeWalk,
                                               List<MergeClashFile> clashFileList) throws IOException {

            // 初始化 DirCache
            DirCache dirCache = DirCache.newInCore();
            DirCacheBuilder builder = dirCache.builder();


            ObjectId newBlobId = null;
            while (treeWalk.next()) {
                String path = treeWalk.getPathString();
                List<MergeClashFile> clashFiles = clashFileList.stream().filter(a -> (path).equals(a.getFilePath())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(clashFiles)){
                    String fileData = clashFiles.get(0).getFileData();
                    // 创建新的 Blob 对象，包含新的文件内容
                    newBlobId = inserter.insert(Constants.OBJ_BLOB, fileData.getBytes(StandardCharsets.UTF_8));

                    // 创建 DirCacheEntry
                    DirCacheEntry entry = new DirCacheEntry(path);
                    entry.setObjectId(newBlobId);
                    entry.setFileMode(treeWalk.getFileMode(0));
                    builder.add(entry);
                }else {
                    // 保持其他文件不变
                    DirCacheEntry entry = new DirCacheEntry(path);
                    entry.setObjectId(treeWalk.getObjectId(0));
                    entry.setFileMode(treeWalk.getFileMode(0));
                    builder.add(entry);
                }
            }
          // 完成构建新的索引
         builder.finish();
          // 创建新的树对象
         ObjectId newTreeId = dirCache.writeTree(inserter);
         inserter.flush();
        return newTreeId;
        }

}
