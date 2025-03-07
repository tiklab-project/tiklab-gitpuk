package io.tiklab.gitpuk.common.git;

import io.tiklab.core.exception.SystemException;
import io.tiklab.gitpuk.branch.model.Branch;
import io.tiklab.gitpuk.branch.model.BranchQuery;
import io.tiklab.gitpuk.commit.model.CommitMessage;
import io.tiklab.core.exception.ApplicationException;
import io.tiklab.gitpuk.common.GitPukFinal;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.RefSpec;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


/**
 * jgit操作仓库分支
 */
public class GitBranchUntil {

    /**
     * 创建仓库分支
     * @param repositoryAddress 仓库地址
     * @param branchName 分支名称
     * @param point 分支起点
     * @throws IOException 仓库不存在
     * @throws GitAPIException 创建失败
     */
    public static String createRepositoryBranch(String repositoryAddress,String branchName, String point) throws IOException, GitAPIException {
        Git git = Git.open(new File(repositoryAddress));
        List<Ref> refList = git.tagList().call();
        List<Ref> collect = refList.stream().filter(a -> a.getName().endsWith(branchName)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)){
            throw new SystemException(GitPukFinal.REPEAT01_EXCEPTION,"分支名与标签名不可以重复");
        }

        Ref call = git.branchCreate()
                .setName(branchName)
                .setStartPoint(point) //起点
                .call();
        String commitId = call.getObjectId().getName();
        git.close();
        return  commitId;
    }


    /**
     * 删除分支
     * @param repositoryAddress 仓库地址
     * @param branchName 分支名称
     * @throws IOException 仓库不存在
     * @throws GitAPIException 仓库删除失败
     */
    public static void deleteRepositoryBranch(String repositoryAddress,String branchName) throws IOException, GitAPIException {
        Repository repository = Git.open(new File(repositoryAddress)).getRepository();
        Git git = new Git(repository);

        // 删除分支 (分支名字和标签名字相同时)
        git.push()
                .setRemote(repository.getDirectory().getAbsolutePath())
                .setRefSpecs(new RefSpec(":" + "refs/heads/" + branchName))
                .call();
        git.close();

      /*  git.branchDelete()
                .setBranchNames(branchName)
                .setForce(true) //是否强制删除分支
                .call();
        git.close();*/
    }

    /**
     * 更新分支名称
     * @param repositoryAddress 仓库地址
     * @param newName 分支新名称
     * @param oldName 分支旧名称
     * @throws IOException 仓库不存在
     * @throws GitAPIException 名称更新失败
     */
    public static void updateRepositoryBranch(String repositoryAddress,String newName,String oldName) throws IOException, GitAPIException {
        Git git = Git.open(new File(repositoryAddress));
        git.branchRename()
                .setNewName(newName)
                .setOldName(oldName)
                .call();
        git.close();
    }

    /**
     * 直接获取裸仓库的分支
     * @param repositoryAddress 仓库地址
     * @return 分支集合
     * @throws IOException 仓库不存在
     */
    public static List<Ref> findBareRepoBranchList(String repositoryAddress) throws IOException {
        Repository repository = Git.open(new File(repositoryAddress)).getRepository();

        // 获取本地分支  服务端裸仓库只存在本地分支
        List<Ref> localBranches = repository.getRefDatabase().getRefsByPrefix("refs/heads/");

        return localBranches;

    }

    /**
     * 获取仓库所有分支
     * @param repositoryAddress 仓库地址
     * @return 分支集合
     * @throws IOException 仓库不存在
     */
    public static List<Branch> findAllBranch(String repositoryAddress) throws IOException, GitAPIException {

        Git git = Git.open(new File(repositoryAddress));
        Repository repository = git.getRepository();

        List<Branch> list = new ArrayList<>();
        //分支和标签
        List<Ref> refs = repository.getRefDatabase().getRefs();

        String defaultBranch = " ";
        for (Ref ref : refs) {
            Branch branch = new Branch();
            String name = ref.getName();
            //排除标签
            if (name.contains(Constants.R_TAGS)){
                continue;
            }

            //排除远程分支
            if (name.contains(Constants.R_REMOTES)){
                continue;
            }

            //默认分支
            if (name.equals("HEAD")){
                Ref target = ref.getTarget();
                defaultBranch = target.getName();
                continue;
            }

            String Id = ref.getObjectId().getName();
            //分支名字
            String branchName = name.replace(Constants.R_HEADS, "");
            branch.setBranchId(Id);
            //判断是否为默认分支
            if (defaultBranch.equals(name)){
                branch.setDefaultBranch(true);
            }
            branch.setBranchName(branchName);
            list.add(branch);
        }

        // repository.close();
        git.close();

        if (list.isEmpty()){
            return Collections.emptyList();
        }

        return list;
    }


    /**
     * 条件获取获取仓库分支
     * @param branchQuery branchQuery
     * @return 分支集合
     * @throws IOException 仓库不存在
     */
    public static List<Branch> findBranchList(BranchQuery branchQuery) throws IOException {

        Git git = Git.open(new File(branchQuery.getRepositoryAddress()));
        Repository repository = git.getRepository();

        List<Branch> list = new ArrayList<>();
        List<Ref> refs = repository.getRefDatabase().getRefs();
        if (StringUtils.isNotEmpty(branchQuery.getName())){
            refs=refs.stream().filter(a->a.getName().contains(branchQuery.getName())||a.getName().equals("HEAD")).collect(Collectors.toList());
        }

        String defaultBranch = " ";
        for (Ref ref : refs) {
            Branch branch = new Branch();
            String name = ref.getName();

            //排除标签
            if (name.contains(Constants.R_TAGS)){
                continue;
            }

            if (name.equals("HEAD")){
                Ref target = ref.getTarget();
                defaultBranch = target.getName();
                continue;
            }

            //判断是否为默认分支
            if (defaultBranch.equals(name)){
                branch.setDefaultBranch(true);
            }

            //获取当前分支较默认分支超前和滞后提交数量
            Map<String, Integer> branchCommitNum = GitCommitUntil.getBranchCommitNum(repository, name);
            branch.setLagNum(branchCommitNum.get("lagNum"));
            branch.setAdvanceNum(branchCommitNum.get("advanceNum"));

            String branchId = ref.getObjectId().getName();
            String branchName = name.replace(Constants.R_HEADS, "");

            //查询分支最新的提交
            CommitMessage oneBranchCommit = GitCommitUntil.findNewestCommit(repository,branchName,"branch");
            branch.setUpdateUser(oneBranchCommit.getCommitUser());
            branch.setUpdateTime(oneBranchCommit.getCommitTime());

            Date dateTime = oneBranchCommit.getDateTime();
            Date days = DateUtils.addDays(dateTime, 30);
            //查询活跃的分支 分支最后提交时间在一个月内为活跃、否则为非活跃状态
            if (("active").equals(branchQuery.getState())){
                if (System.currentTimeMillis()<=days.getTime()){
                    branch.setBranchId(branchId);
                    branch.setBranchName(branchName);
                    list.add(branch);
                }
                continue;
            }
            //查询非活跃的分支
            if (("noActive").equals(branchQuery.getState())){
                if (System.currentTimeMillis()>days.getTime()){
                    branch.setBranchId(branchId);
                    branch.setBranchName(branchName);
                    list.add(branch);
                }
                continue;
            }
            //查询所有的分支
            branch.setBranchId(branchId);
            branch.setBranchName(branchName);
            list.add(branch);
        }
        git.close();

        if (list.isEmpty()){
            return Collections.emptyList();
        }

        return list;
    }




    /**
     * 返回仓库默认分支
     * @param repositoryAddress 仓库地址
     * @return 默认分支 无返回 Constants.MASTER
     * @throws IOException 仓库不存在
     */
    public static String findDefaultBranch(String repositoryAddress) throws Exception {
        List<Branch> branches = findAllBranch(repositoryAddress);
        for (Branch branch : branches) {
            if (branch.isDefaultBranch()){
                return branch.getBranchName();
            }
        }
        return Constants.MASTER;
    }

    /**
     * 获取指定commitId的提交树
     * @param repository 仓库
     * @param branch 分支
     * @return commitId
     * @throws IOException 仓库不存在
     */
    public static RevTree findBarthCommitRevTree(Repository repository,String branch,boolean b) throws IOException {
        ObjectId objectId;
        if (!b){
            objectId = repository.resolve(Constants.R_HEADS + branch);
//            objectId = repository.resolve( branch);
        }else {
            objectId = repository.resolve(Constants.R_TAGS + branch);
           // objectId = ObjectId.fromString(branch);
        }
        RevWalk walk = new RevWalk(repository);
        RevTree tree = walk.parseCommit(objectId).getTree();
        walk.close();
        return tree;
    }

    /**
     * 获取指定分子的提交树
     * @param repository 仓库
     * @param branch 分支
     * @return findType 查询类型
     * @throws IOException 仓库不存在
     */
    public static RevTree findBarthCommitRevTree(Repository repository,String branch,String findType) throws IOException {
        ObjectId objectId = findObjectId(repository, branch, findType);
        RevWalk walk = new RevWalk(repository);
        RevTree tree = walk.parseCommit(objectId).getTree();
        walk.close();
        return tree;
    }

    /**
     * 获取objectId
     * @param repository 仓库
     * @param branch 分支
     * @return findType 查询类型
     * @throws IOException 仓库不存在
     */
    public static ObjectId findObjectId(Repository repository,String branch,String findType) throws IOException {
        ObjectId objectId =null;
        if (("tag").equals(findType)){
            objectId = repository.resolve(Constants.R_TAGS + branch);
        }
        if (("branch").equals(findType)){
            objectId = repository.resolve(Constants.R_HEADS + branch);
        }
        if (("commit").equals(findType)){
            objectId = ObjectId.fromString(branch);
        }
        return objectId;
    }

    /**
     * 切换默认分支
     * @param repository 仓库
     * @param branch 分支
     * @throws ApplicationException 切换失败
     */
    public static void updateFullBranch(Repository repository,String branch) throws ApplicationException {
        String fullBranch = Constants.R_HEADS + branch;
        try {
            RefUpdate  updateRef = repository.updateRef("HEAD");
            updateRef.link(fullBranch);
      /*      updateRef.update();*/
        } catch (IOException e) {
            throw new ApplicationException("切换默认分支失败:"+fullBranch);
        }
    }

    /**
     * 查询两个分支是否有冲突
     * @param repositoryPath 仓库地址
     * @param branchA 分支a
     * @param branchB 分支b
     */

    public static boolean findIsBranchDiffsFile(String  repositoryPath,String branchA,String branchB){
        try {
            // 构建裸仓库对象
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository = builder.setGitDir(new File(repositoryPath))
                    .readEnvironment()
                    .findGitDir()
                    .build();

            //获取源分支的引用
            Ref originBranch = repository.exactRef("refs/heads/" + branchA);
            ObjectId originObjectId = originBranch.getObjectId();

            // 获取目标分支的引用
            Ref targetBranch = repository.exactRef("refs/heads/" + branchB);
            ObjectId targetObjectId = targetBranch.getObjectId();

            RevWalk revWalk = new RevWalk(repository);
            RevCommit originCommit = revWalk.parseCommit(originObjectId);
            RevCommit targetCommit = revWalk.parseCommit(targetObjectId);

            // 解析冲突文件的三方内容
            ObjectId originTreeId = originCommit.getTree();
            ObjectId targetTreeId = targetCommit.getTree();


            //获取两个分支的差异文件内容
            ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
            DiffFormatter diffFormatter = new DiffFormatter(fileOut);
            diffFormatter.setRepository(repository);
            diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
            diffFormatter.setDetectRenames(true);
            List<DiffEntry> diffs = diffFormatter.scan(targetTreeId, originTreeId);


            for (DiffEntry entry : diffs) {
                FileHeader fileHeader = diffFormatter.toFileHeader(entry);

                EditList editList = diffFormatter.toFileHeader(entry).toEditList();
                //获取差异差异文件类型
                String name = entry.getChangeType().name();

               /* //新的newObjectId 和旧的oldObjectId 不为空再执行差异内容
                ObjectId oldObjectId = fileHeader.getOldId().toObjectId();
                ObjectId newObjectId = fileHeader.getNewId().toObjectId();*/

                /*
                 * ADD: 表示文件是新增的，即在一个分支中添加了一个新文件。
                 * DELETE: 表示文件被删除，即在一个分支中删除了一个已存在的文件。
                 * MODIFY: 表示文件被修改，即在一个分支中对一个已存在的文件进行了修改。
                 * RENAME: 表示文件被重命名，即在一个分支中对一个已存在的文件进行了重命名操作。
                 * COPY: 表示文件被复制，即在一个分支中对一个已存在的文件进行了复制操作
                 * */

                //文件模式是重命名，且旧的和新的名字不同代码是有冲突的
                if (("RENAME").equals(name)&&!entry.getOldPath().equals(entry.getNewPath())){
                    return true;
                }
                if (("MODIFY").equals(name)){
                    //判断目标分支最后一个提交是否在源分支里面
                    boolean branchExistsInBranch = findBranchExistsInBranch(repository, originObjectId, targetObjectId);
                    if (branchExistsInBranch){
                        return false;
                    }
                    diffFormatter.format(fileHeader);
                    String diffOutput = fileOut.toString("UTF-8");
                    boolean deleteLine=false;
                    boolean addLine=false;
                    //判断差异内容是否 存在差异 （同时存在 +和-）
                    String[] diffLines = diffOutput.split("\n");
                    for (String line : diffLines) {
                        if (line.startsWith("+++") || line.startsWith("---")){
                            continue;
                        }
                        if (line.startsWith("-")) {
                            deleteLine=true;
                        }else if (line.startsWith("+")) {
                            addLine=true;
                        }
                    }
                    if (deleteLine&&addLine){
                        return true;
                    }
                }
            }
            return false;
        }catch (Exception e){
            throw new SystemException("获取分支"+branchA+"-"+branchB+"的冲突文件失败:"+e.getMessage());
        }

    }

    /**
     * 判断目标分支最后一个提交是否在源分支里面
     * @param repository repository
     * @param originObjectId originObjectId
     * @param targetObjectId targetObjectId
     */
    public static boolean  findBranchExistsInBranch(Repository repository,ObjectId originObjectId,ObjectId targetObjectId) throws IOException {
        RevWalk revWalk = new RevWalk(repository);

        // 获取 a 分支的最后一个提交
        RevCommit commitA = revWalk.parseCommit(targetObjectId);


        revWalk.sort(RevSort.COMMIT_TIME_DESC);
        revWalk.markStart(revWalk.parseCommit(originObjectId));
        for (RevCommit revCommit : revWalk) {
            if (revCommit.equals(commitA)) {
                return true; // a 分支的最后一个提交在 b 分支中
            }
        }
        return false;
    }
}















































